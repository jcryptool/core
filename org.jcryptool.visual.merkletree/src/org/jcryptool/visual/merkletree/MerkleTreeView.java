package org.jcryptool.visual.merkletree;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.algorithm.XMSSTree;
import org.jcryptool.visual.merkletree.ui.MerkleConst;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;
import org.jcryptool.visual.merkletree.ui.MerkleTreeComposite;
import org.jcryptool.visual.merkletree.ui.MerkleTreeKeyPairs;
import org.jcryptool.visual.merkletree.ui.MerkleTreeSeed;
import org.jcryptool.visual.merkletree.ui.MerkleTreeSignatureComposite;
import org.jcryptool.visual.merkletree.ui.MerkleTreeVerifikationComposite;
import org.jcryptool.visual.merkletree.ui.MerkleTreeZestComposite;

/**
 * This class holds everything that you see on the screen; It provides the Tabs
 * of its tool.
 * 
 * @author Kevin Muehlboeck
 * 
 * TODO: Kommentare
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
	private MerkleTreeSignatureComposite mtS;
	private MerkleTreeVerifikationComposite mtV;
	private ISimpleMerkle merkle;
	private SUIT mode;

	//bei tabwechsel, wenn true -> msg Box (y|n) "achtung" änderungen wurden nicht in neuen Tree übertragen, bitte neuen Key Erzeugen!
	//TODO: if abfrage bei Tabwechsel einbauen
	private Boolean unsavedChanges;
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 * This Method declares and sets the GUI-Control elements
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

		//Key-generation
		TabItem tbtmParameter0 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter0.setText(Descriptions.MerkleTreeView_0);
		mtC = new MerkleTreeComposite(tabFolder, this);
		tbtmParameter0.setControl(mtC);
		
		//TreeView
		TabItem tbtmParameter1 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter1.setText(Descriptions.MerkleTreeView_1);
		
		//Signing
		TabItem tbtmParameter2 = new TabItem(tabFolder,SWT.NONE);
		tbtmParameter2.setText(Descriptions.MerkleTreeView_2);
		
		//Verification
		TabItem tbtmParameter3 = new TabItem(tabFolder,SWT.NONE);
		tbtmParameter3.setText(Descriptions.MerkleTreeView_3);
		//christoph: doesnt seem to do anything -> removed because of the reset button
		//tbtmParameter3.setControl(mtV);
		
		
		
		tabFolder.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
				if(unsavedChanges == true){
					int select = tabFolder.getSelectionIndex();
					tabFolder.setSelection(0);
					MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION |SWT.YES|SWT.NO|SWT.CANCEL);
					messageBox.setMessage(Descriptions.UnsavedChanges);
					messageBox.setText("Info");
					switch(messageBox.open()){
						
						case SWT.YES:
							switch(mode){
								case XMSS:
									merkle = new XMSSTree();
									break;
								case XMSS_MT:
									//new XMSS_MT_TREE
									//break;
								case MSS:
								default:
									merkle = new SimpleMerkleTree();
									break;
							}
							merkle.setLeafCount(mtC.getMTS().getMTKP().getKeyAmmount());
							merkle.setSeed(mtC.getMTS().getSeed());
							
							/*
							 * if the generated Tree is a XMSSTree -> the Bitmaskseed is also needed
							 */
							if(merkle instanceof XMSSTree){
								((XMSSTree) merkle).setBitmaskSeed(mtC.getMTS().getBitmaskSeed());
							}	
							merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
							merkle.generateKeyPairsAndLeaves();
							merkle.generateMerkleTree();
							unsavedChanges = false;
							
							break;
						case SWT.NO:
							Control[] mtsC = mtC.getMTS().getChildren();
							for(int i = 0; i < mtsC.length; i++){
								if(mtsC[i] instanceof Text){
									((Text)mtsC[i]).setText(merkle.getSeed().toString());
								}
							}
							
							Control[] mtbC = mtC.getMTS().getChildren();
							for(int i = 0; i < mtbC.length; i++){
								if(mtbC[i] instanceof Text){
									((Text)mtbC[i]).setText(((XMSSTree) merkle).getBitmaskSeed().toString());
								}
							}
							
							Control[] mtkC = mtC.getMTS().getMTKP().getChildren();
							for(int i = 0; i < mtkC.length; i++){
								if(mtkC[i] instanceof Spinner){
									((Spinner)mtkC[i]).setSelection(merkle.getLeafCounter());
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
				
				if(merkle == null){
					tabFolder.setSelection(0);
					MessageBox messageBoxx = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
					messageBoxx.setMessage(Descriptions.MerkleTree_Generation_Info);
					messageBoxx.setText("Info");
					messageBoxx.open();
				} else {
					switch(tabFolder.getSelectionIndex()){
						case 1:
							mtZ = new MerkleTreeZestComposite(tabFolder, SWT.NONE, merkle,mode);
							tbtmParameter1.setControl(mtZ);
							break;
						case 2:
							mtS = new MerkleTreeSignatureComposite(tabFolder,SWT.NONE,merkle);
							tbtmParameter2.setControl(mtS);
							break;
						case 3:
							if(!mtS.getSignatureFromForm().isEmpty()) {
								String signature=mtS.getSignatureFromForm();
								String[]splittedSign = signature.split("\\|");
								String keyIndex = "";
								String message;
								message=mtS.getMessageFromForm();
								if(splittedSign.length> 1){
									//otSign =splittedSign[0];
									keyIndex =splittedSign[0];
								}
								mtV=new MerkleTreeVerifikationComposite(tabFolder, SWT.NONE, merkle, Integer.parseInt(keyIndex),signature,message);
								tabFolder.getSelection()[0].setControl(mtV);
								
							}else
								{
								MessageBox messageBoxx = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
								messageBoxx.setMessage(Descriptions.MerkleTree_Signature_Generation_Info);
								messageBoxx.setText("Info");
								messageBoxx.open();
								tabFolder.setSelection(2);
							}
							break;
						case 0:
						default:
							break;
					}
				}
			}
		});


		scrolledComposite.setContent(tabFolder);
		scrolledComposite.setMinSize(MerkleConst.PLUGIN_WIDTH,MerkleConst.PLUGIN_HEIGTH);

		// makes the connection to the help of the plug-in
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(),
		"org.jcryptool.visual.merkletree.merkletreeview");
	}

	/**
	 * This method synchronizes the merkleTree
	 * @param merkle
	 */
	public void setAlgorithm(ISimpleMerkle merkle, SUIT mode) {
			this.merkle = merkle;
			this.mode = mode;
			unsavedChanges = false;
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

	public void updateElement(){
		unsavedChanges = true;
	}
}