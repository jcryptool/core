package org.jcryptool.visual.merkletree;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
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
import org.eclipse.ui.internal.UISynchronizer;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
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
	private MerkleTreeComposite mtC;
	private MerkleTreeZestComposite mtZ;
	private MerkleTreeKeyComposite mtK;
	private MerkleTreeSignatureComposite mtS;
	private MerkleTreeVerifikationComposite mtV;
	private ISimpleMerkle merkle;
	private SUIT mode;
	private int previousTab = 0;

	boolean sync = false;

	// bei tabwechsel, wenn true -> msg Box (y|n) "achtung" änderungen wurden
	// nicht in neuen Tree
	// übertragen, bitte neuen Key Erzeugen!
	// TODO: if abfrage bei Tabwechsel einbauen
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

		parent.setLayout(new GridLayout(1, false));

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		tabFolder = new TabFolder(scrolledComposite, SWT.NONE);

		// Key-generation
		TabItem tbtmParameter0 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter0.setText(Descriptions.MerkleTreeView_0);
		mtC = new MerkleTreeComposite(tabFolder, this);
		tbtmParameter0.setControl(mtC);

		// TreeView
		TabItem tbtmParameter1 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter1.setText(Descriptions.MerkleTreeView_1);

		// keys
		TabItem tbtmParameter2 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter2.setText("Key Pair");

		// Signing
		TabItem tbtmParameter3 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter3.setText(Descriptions.MerkleTreeView_2);

		// Verification
		TabItem tbtmParameter4 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter4.setText(Descriptions.MerkleTreeView_3);

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
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
				if (unsavedChanges == true) {
					int select = tabFolder.getSelectionIndex();
					tabFolder.setSelection(0);
					MessageBox messageBox = new MessageBox(new Shell(),
							SWT.ICON_INFORMATION | SWT.YES | SWT.NO | SWT.CANCEL);
					messageBox.setMessage(Descriptions.UnsavedChanges);
					messageBox.setText("Info");
					switch (messageBox.open()) {

					case SWT.YES:
						switch (mode) {
						case XMSS:
							merkle = new XMSSTree();
							break;
						case XMSS_MT:
							// new XMSS_MT_TREE
							// break;
						case MSS:
						default:
							merkle = new SimpleMerkleTree();
							break;
						}
						merkle.setLeafCount(mtC.getMTS().getMTKP().getKeyAmmount());
						merkle.setSeed(mtC.getMTS().getSeed());
						merkle.setWinternitzParameter(mtC.getMTS().getWinternitzParameter());

						/*
						 * if the generated Tree is a XMSSTree -> the
						 * Bitmaskseed is also needed
						 */
						if (merkle instanceof XMSSTree) {
							((XMSSTree) merkle).setBitmaskSeed(mtC.getMTS().getBitmaskSeed());
						}
						merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
						merkle.generateKeyPairsAndLeaves();
						merkle.generateMerkleTree();
						unsavedChanges = false;
						sync = false;

						break;
					case SWT.NO:
						Control[] mtsC = mtC.getMTS().getChildren();
						for (int i = 0; i < mtsC.length; i++) {
							if (mtsC[i] instanceof Text) {
								((Text) mtsC[i]).setText(merkle.getSeed().toString());
							}
						}

						Control[] mtbC = mtC.getMTS().getChildren();
						for (int i = 0; i < mtbC.length; i++) {
							if (mtbC[i] instanceof Text) {
								((Text) mtbC[i]).setText(((XMSSTree) merkle).getBitmaskSeed().toString());
							}
						}

						Control[] mtkC = mtC.getMTS().getMTKP().getChildren();
						for (int i = 0; i < mtkC.length; i++) {
							if (mtkC[i] instanceof Spinner) {
								((Spinner) mtkC[i]).setSelection(merkle.getLeafCounter());
							}
						}
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
					switch (tabFolder.getSelectionIndex()) {
					case 0:
						previousTab = 0;
						break;
					case 1:
						// Creates instance if tab was not clicked before
						if (mtZ == null)
							mtZ = new MerkleTreeZestComposite(tabFolder, SWT.NONE, merkle, mode);
						tbtmParameter1.setControl(mtZ);
						previousTab = 1;
						break;
					case 2:
						// Creates instance if tab was not clicked before
						// if (mtK == null) //TODO: implement feature so that
						// this knows: have the keys changed?
						tbtmParameter2.setControl(mtK);
						previousTab = 2;
						break;
					case 3:
						/*
						 * only create a new SignatureComposite at the first
						 * time if synced == true -> dont create a new
						 * SignatureComposite necessary for tab changes
						 */
						if (sync == false || mtS == null) {
							mtS = new MerkleTreeSignatureComposite(tabFolder, SWT.NONE, merkle);
							tbtmParameter3.setControl(mtS);
						}
						previousTab = 3;
						break;
					case 4:
						sync = true;
						if (mtS == null || mtS.getSignature() == null) {
							MessageBox messageBoxx = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
							messageBoxx.setMessage(Descriptions.MerkleTree_Signature_Generation_Info);
							messageBoxx.setText("Info");
							messageBoxx.open();
							tabFolder.setSelection(previousTab);
						} else {
							String signature = mtS.getSignature();
							String[] splittedSign = signature.split("\\|");
							String keyIndex = "";
							String message = mtS.getMessage();
							if (splittedSign.length > 1) {
								keyIndex = splittedSign[0];
							}

							mtV = new MerkleTreeVerifikationComposite(tabFolder, SWT.NONE, merkle,
									Integer.parseInt(keyIndex), signature, message);
							tabFolder.getSelection()[0].setControl(mtV);

						}
						break;
					default:
						break;
					}
				}
			}
		});

		scrolledComposite.setContent(tabFolder);
		scrolledComposite.setMinSize(MerkleConst.PLUGIN_WIDTH, MerkleConst.PLUGIN_HEIGTH);

		// makes the connection to the help of the plug-in
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(),
				"org.jcryptool.visual.merkletree.merkletreeview");
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

	public void generateKeyTab() {

		Display.getCurrent().asyncExec(new Runnable() {

			@Override
			public void run() {
				mtK = new MerkleTreeKeyComposite(tabFolder, SWT.NONE, merkle);
			}
		});

	}

}
