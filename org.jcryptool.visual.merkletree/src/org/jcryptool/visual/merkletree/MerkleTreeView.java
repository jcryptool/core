package org.jcryptool.visual.merkletree;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.ui.InteractiveSignatureComposite;
import org.jcryptool.visual.merkletree.ui.MerkleConst;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;
import org.jcryptool.visual.merkletree.ui.MerkleTreeComposite;
import org.jcryptool.visual.merkletree.ui.MerkleTreeKeyComposite;
import org.jcryptool.visual.merkletree.ui.MerkleTreeSignatureComposite;
import org.jcryptool.visual.merkletree.ui.PlainSignatureComposite;
import org.jcryptool.visual.merkletree.ui.MerkleTreeVerifikationComposite;
import org.jcryptool.visual.merkletree.ui.MerkleTreeZestComposite;

/**
 * This class holds everything that you see on the screen; It provides the Tabs
 * of its tool.
 * 
 * @author Kevin Muehlboeck
 * @author <i>revised by</i>
 * @author Maximilian Lindpointner
 * 
 * 
 */
public class MerkleTreeView extends ViewPart {

	public MerkleTreeView() {

	}

	private Composite parent;

	private TabFolder tabFolder;

	// this composite is what actually holds the plug-in contents
	private MerkleTreeComposite baseComposite;

	private MerkleTreeZestComposite zestTab;
	private MerkleTreeKeyComposite keyTab;
	private MerkleTreeSignatureComposite signatureTab;
	private MerkleTreeVerifikationComposite verificationTab;
	private ISimpleMerkle merkle;
	private ISimpleMerkle oldMerkle;
	private SUIT mode;
	private int previousTab = 0;
	private ViewPart masterView;
	private ScrolledComposite scrolledComposite;

	TabItem tbtmParameter0;
	TabItem tbtmParameter1;
	TabItem tbtmParameter2;
	TabItem tbtmParameter3;
	TabItem tbtmParameter4;

	String signatures[];
	String messages[];

	private Boolean unsavedChanges;
	private boolean mustCreateTab[];

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.
	 * widgets.Composite) This Method declares and sets the GUI-Control elements
	 */
	@Override
	public void createPartControl(final Composite parent) {

		this.parent = parent;
		unsavedChanges = false;
		masterView = this;
		mustCreateTab = new boolean[5];

		parent.setLayout(new GridLayout(1, false));

		scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		tabFolder = new TabFolder(scrolledComposite, SWT.NONE);
		tabFolder.setFont(FontService.getLargeFont());

		// Key-generation
		tbtmParameter0 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter0.setText(Descriptions.MerkleTreeTab_0);
		baseComposite = new MerkleTreeComposite(tabFolder, masterView);
		tbtmParameter0.setControl(baseComposite);

		// TreeView
		tbtmParameter1 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter1.setText(Descriptions.MerkleTreeTab_1);

		// keys
		tbtmParameter2 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter2.setText(Descriptions.MerkleTreeTab_2);

		// Signing
		tbtmParameter3 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter3.setText(Descriptions.MerkleTreeTab_3);

		// Verification
		tbtmParameter4 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter4.setText(Descriptions.MerkleTreeTab_4);

		// gets called when a user clicks a tab
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				int select = tabFolder.getSelectionIndex();
				// this part calls a messagebox if parameters have been changed
				// on the UI but no new key was created
				if (unsavedChanges == true) {

					tabFolder.setSelection(0);
					MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.YES | SWT.NO | SWT.CANCEL);
					messageBox.setMessage(Descriptions.UnsavedChanges);
					messageBox.setText("Info");
					// asks if changes should be done to key, discarded or
					// action cancelled
					switch (messageBox.open()) {
					case SWT.YES:
						merkle = baseComposite.getMTS().generateMerkleTree();
						unsavedChanges = false;
						break;
					case SWT.NO:
						unsavedChanges = false;
						break;
					case SWT.CANCEL:
						removeFocus();
					default:
						select = 0;
					}
					tabFolder.setSelection(select);
				}
				// Calls messagebox if no key was created
				if (merkle == null) {
					tabFolder.setSelection(0);
					MessageBox messageBoxx = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
					messageBoxx.setMessage(Descriptions.MerkleTree_Generation_Info);
					messageBoxx.setText("Info");
					messageBoxx.open();
					removeFocus();
				} else {
					// else sets tab with logic in setTab
					setTab(select);
				}
			}
		});

		scrolledComposite.setContent(tabFolder);
		scrolledComposite.setMinSize(MerkleConst.PLUGIN_WIDTH, MerkleConst.PLUGIN_HEIGTH);

		// makes the connection to the help of the plug-in
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), "org.jcryptool.visual.merkletree.merkletreeview");
	}

	/**
	 * This method synchronizes the merkleTree
	 * 
	 * @param merkle
	 */
	public void setAlgorithm(ISimpleMerkle merkle, SUIT mode) {
		this.merkle = merkle;
		this.mode = mode;
		unsavedChanges = false;
		Arrays.fill(mustCreateTab, true);
		signatureTab = null;
	}

	/**
	 * resets the view, it's needed by JCrypTool
	 */
	public void resetView() {
		Control[] children = parent.getChildren();
		for (Control control : children) {
			control.dispose();
		}
		createPartControl(parent);
		parent.layout();

		reset();
	}

	@Override
	public void setFocus() {
		scrolledComposite.setFocus();
	}

	/**
	 * For the reset button
	 */
	public void reset() {
		Control[] children = parent.getChildren();
		for (Control control : children) {
			control.dispose();
		}
		createPartControl(parent);
		parent.layout();
	}

	public void updateElement() {
		unsavedChanges = true;
	}

	public void setTab(int tab) {
		switch (tab) {
		case 0:
			tbtmParameter0.setControl(baseComposite);
			previousTab = 0;
			break;
		case 1:
			if (zestTab == null || mustCreateTab[1]) {
				zestTab = new MerkleTreeZestComposite(tabFolder, SWT.NONE, merkle, mode, masterView);
				mustCreateTab[1] = false;
			}

			tbtmParameter1.setControl(zestTab);
			previousTab = 1;
			break;
		case 2:
			// Creates instance if tab was not clicked before or keys changed
			if (keyTab == null || mustCreateTab[2]) {
				BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

					@Override
					public void run() {
						keyTab = new MerkleTreeKeyComposite(tabFolder, SWT.NONE, merkle);
						keyTab.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
						mustCreateTab[2] = false;
					}
				});
			}
			tbtmParameter2.setControl(keyTab);
			previousTab = 2;
			break;
		case 3:
			/*
			 * only create a new SignatureComposite at the first time if synced
			 * == true -> dont create a new SignatureComposite necessary for tab
			 * changes
			 */
			if (signatureTab == null || mustCreateTab[3]) {
				signatureTab = new MerkleTreeSignatureComposite(tabFolder, SWT.NONE, merkle, mode, this);
				signatureTab.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				mustCreateTab[3] = false;
			}
			tbtmParameter3.setControl(signatureTab);
			previousTab = 3;
			break;
		case 4:

			if (signatureTab != null && signatureTab.getSignatures()[0] != null) {
				signatures = signatureTab.getSignatures();
				messages = signatureTab.getMessages();
				if (mustCreateTab[4]) {
					verificationTab = new MerkleTreeVerifikationComposite(tabFolder, SWT.NONE, merkle, signatures, messages);
					verificationTab.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
					mustCreateTab[4] = false;
				}
				verificationTab.setSignatureMessagePair(signatures, messages);
				tbtmParameter4.setControl(verificationTab);
			} else {
				tabFolder.setSelection(previousTab);
				MessageBox messageBoxx = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
				messageBoxx.setMessage(Descriptions.MerkleTree_Signature_Generation_Info);
				messageBoxx.setText("Info");
				messageBoxx.open();
				tab = previousTab;
			}
			break;
		default:
			break;
		}

		tabFolder.setSelection(tab);

	}

	public void removeFocus() {
		baseComposite.setLocalFocus();
	}

	public void deleteTree() {
		merkle = null;
	}

}
