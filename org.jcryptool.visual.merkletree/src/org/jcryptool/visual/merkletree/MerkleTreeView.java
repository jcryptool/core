package org.jcryptool.visual.merkletree;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.XMSSTree;
import org.jcryptool.visual.merkletree.ui.MerkleConst;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;
import org.jcryptool.visual.merkletree.ui.MerkleTreeComposite;
import org.jcryptool.visual.merkletree.ui.MerkleTreeKeyComposite;
import org.jcryptool.visual.merkletree.ui.MerkleTreeSignatureComposite;
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

	TabItem tbtmParameter0;
	TabItem tbtmParameter1;
	TabItem tbtmParameter2;
	TabItem tbtmParameter3;
	TabItem tbtmParameter4;

	boolean sync = false;

	private Boolean unsavedChanges;

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

		parent.setLayout(new GridLayout(1, false));

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		tabFolder = new TabFolder(scrolledComposite, SWT.NONE);

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

		tabFolder.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.
			 * eclipse.swt.events. SelectionEvent)
			 */
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.
			 * eclipse.swt.events. SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (unsavedChanges == true) {
					int select = tabFolder.getSelectionIndex();
					tabFolder.setSelection(0);
					MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.YES | SWT.NO | SWT.CANCEL);
					messageBox.setMessage(Descriptions.UnsavedChanges);
					messageBox.setText("Info");
					switch (messageBox.open()) {

					case SWT.YES:
						merkle = baseComposite.getMTS().generateMerkleTree();
						unsavedChanges = false;
						sync = false;

						break;
					case SWT.NO:
						unsavedChanges = false;
						break;
					case SWT.CANCEL:
					default:
						select = 0;
					}
					tabFolder.setSelection(select);
				}

				if (merkle == null) {
					tabFolder.setSelection(0);
					MessageBox messageBoxx = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
					messageBoxx.setMessage(Descriptions.MerkleTree_Generation_Info);
					messageBoxx.setText("Info");
					messageBoxx.open();
				} else {
					setTab(tabFolder.getSelectionIndex());
					// switch (tabFolder.getSelectionIndex()) {
					// case 0:
					// previousTab = 0;
					// break;
					// case 1:
					// zestTab = new MerkleTreeZestComposite(tabFolder,
					// SWT.NONE, merkle, mode, masterView);
					// tbtmParameter1.setControl(zestTab);
					// previousTab = 1;
					// break;
					// case 2:
					// // Creates instance if tab was not clicked before
					// if (keyTab == null || ((oldMerkle != merkle) || oldMerkle
					// == null)) {
					// // TODO: implement feature so that
					// // this knows: have the keys changed?
					// BusyIndicator.showWhile(Display.getCurrent(), new
					// Runnable() {
					//
					// @Override
					// public void run() {
					// // TODO Auto-generated method stub
					// keyTab = new MerkleTreeKeyComposite(tabFolder, SWT.NONE,
					// merkle);
					// keyTab.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
					// }
					// });
					// }
					// oldMerkle = merkle;
					// tbtmParameter2.setControl(keyTab);
					// previousTab = 2;
					// break;
					// case 3:
					// /*
					// * only create a new SignatureComposite at the first
					// * time if synced == true -> dont create a new
					// * SignatureComposite necessary for tab changes
					// */
					// if (sync == false || signatureTab == null) {
					// signatureTab = new
					// MerkleTreeSignatureComposite(tabFolder, SWT.NONE,
					// merkle);
					// signatureTab.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
					// tbtmParameter3.setControl(signatureTab);
					// }
					// previousTab = 3;
					// break;
					// case 4:
					// sync = true;
					// if (signatureTab == null || signatureTab.getSignature()
					// == null) {
					// tabFolder.setSelection(previousTab);
					// MessageBox messageBoxx = new MessageBox(new Shell(),
					// SWT.ICON_INFORMATION | SWT.OK);
					// messageBoxx.setMessage(Descriptions.MerkleTree_Signature_Generation_Info);
					// messageBoxx.setText("Info");
					// messageBoxx.open();
					// } else {
					// String signature = signatureTab.getSignature();
					// String[] splittedSign = signature.split("\\|");
					// String keyIndex = "";
					// String message = signatureTab.getMessage();
					// if (splittedSign.length > 1) {
					// keyIndex = splittedSign[0];
					// }
					//
					// verificationTab = new
					// MerkleTreeVerifikationComposite(tabFolder, SWT.NONE,
					// merkle, Integer.parseInt(keyIndex), signature, message);
					// verificationTab.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
					// tabFolder.getSelection()[0].setControl(verificationTab);
					//
					// }
					// break;
					// default:
					// break;
					// }
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
		// set sync back to false -> needed if the verification tab was clicked
		// before
		sync = false;
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
			previousTab = 0;
			break;
		case 1:
			zestTab = new MerkleTreeZestComposite(tabFolder, SWT.NONE, merkle, mode, masterView);
			tbtmParameter1.setControl(zestTab);
			previousTab = 1;
			break;
		case 2:
			// Creates instance if tab was not clicked before or keys changed
			if (keyTab == null || ((oldMerkle != merkle) || oldMerkle == null)) {
				BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

					@Override
					public void run() {
						keyTab = new MerkleTreeKeyComposite(tabFolder, SWT.NONE, merkle);
						keyTab.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
					}
				});
			}
			oldMerkle = merkle;
			tbtmParameter2.setControl(keyTab);
			previousTab = 2;
			break;
		case 3:
			/*
			 * only create a new SignatureComposite at the first time if synced
			 * == true -> dont create a new SignatureComposite necessary for tab
			 * changes
			 */
			if (sync == false || signatureTab == null) {
				signatureTab = new MerkleTreeSignatureComposite(tabFolder, SWT.NONE, merkle);
				signatureTab.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				tbtmParameter3.setControl(signatureTab);
			}
			previousTab = 3;
			break;
		case 4:
			sync = true;
			String signature;
			String[] splittedSign;
			String keyIndex;
			String message;
			if (zestTab != null && zestTab.getSignature() != null) {
				signature = zestTab.getSignature();
				splittedSign = signature.split("\\|");
				keyIndex = "";
				message = zestTab.getMessage();
				if (splittedSign.length > 1) {
					keyIndex = splittedSign[0];
				}
				verificationTab = new MerkleTreeVerifikationComposite(tabFolder, SWT.NONE, merkle, Integer.parseInt(keyIndex), signature, message);
				verificationTab.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				tabFolder.getSelection()[0].setControl(verificationTab);
			} else if (signatureTab != null && signatureTab.getSignature() != null) {
				signature = signatureTab.getSignature();
				splittedSign = signature.split("\\|");
				keyIndex = "";
				message = signatureTab.getMessage();
				if (splittedSign.length > 1) {
					keyIndex = splittedSign[0];
				}
				verificationTab = new MerkleTreeVerifikationComposite(tabFolder, SWT.NONE, merkle, Integer.parseInt(keyIndex), signature, message);
				verificationTab.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				tabFolder.getSelection()[0].setControl(verificationTab);
			} else {
				tabFolder.setSelection(previousTab);
				MessageBox messageBoxx = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
				messageBoxx.setMessage(Descriptions.MerkleTree_Signature_Generation_Info);
				messageBoxx.setText("Info");
				messageBoxx.open();
			}
			break;
		default:
			break;
		}
	}

}
