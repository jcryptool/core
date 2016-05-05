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
 */
public class MerkleTreeView extends ViewPart {
	public MerkleTreeView() {
	}

	private Composite parent;

	// for this scrolling
	private ScrolledComposite scroll;
	private TabFolder tabFolder;
	private TabItem tbtmCodeTable;
	private Boolean extended;

	// this composite is what actually holds the plug-in contents
	private MerkleTreeComposite mtC;
	private MerkleTreeZestComposite mtZ;
	private MerkleTreeSignatureComposite mtS;
	private MerkleTreeVerifikationComposite mtV;
	private ISimpleMerkle merkle;


	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 * This Method declares and sets the GUI-Control elements
	 */
	@Override
	public void createPartControl(final Composite parent) {
		this.parent = parent;
		extended = false;
		merkle = new SimpleMerkleTree(null, null, 0);
		// provides horizontal and vertical scrolling for the plug-in
		// scroll = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		// scroll.setExpandHorizontal(true);
		// scroll.setExpandVertical(true);
		// grid layout is used, because currently it seems to be the only layout
		// provided by SWT
		// that is capable of
		// dealing with 4K monitors
		// scroll.setLayout(new GridLayout(1, true));
		// scroll.setMinSize(tabFolder.computeSize(MerkleConst.PLUGIN_WIDTH,
		// MerkleConst.PLUGIN_HEIGTH));
		// scroll.setContent(tabFolder);
		parent.setLayout(new GridLayout(1, false));

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		

		tabFolder = new TabFolder(scrolledComposite, SWT.NONE);

		TabItem tbtmParameter = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter.setText(Descriptions.MerkleTreeView_0);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
				
				//Changing Content on the first tab should automatically redraw the merkleTree
				//Fabian, 28.01.2016, getting content from first tab
				byte[] seedCheck = new byte[(byte)0x00];
				int keyCheck = 0;
				Control[] controlView = mtC.getChildren();
				for (int i = 0; i < controlView.length; i++) {
					if ((controlView[i] instanceof Composite)) {
						Control[] controlComposite = ((Composite)controlView[i]).getChildren();
						for(int j = 0;j<controlComposite.length;j++) {
							if((controlComposite[j] instanceof MerkleTreeSeed)){
								Control[] controlSeed = ((Composite)controlComposite[j]).getChildren();
								for(int m = 0;m<controlSeed.length;m++){
									if((controlSeed[m] instanceof MerkleTreeKeyPairs)){
										Control[] controlKey = ((Composite)controlSeed[m]).getChildren();
										for(int n=0;n<controlKey.length;n++){
											if(controlKey[n] instanceof Spinner){
												keyCheck=((Spinner)controlKey[n]).getSelection();
											}
										}
									}else if((controlSeed[m] instanceof Composite)&& !(controlSeed[m] instanceof MerkleTreeSeed)){
										Control[] seedHelp = ((Composite)controlSeed[m]).getChildren();
										for(int k=0;k<seedHelp.length;k++){
											if(seedHelp[k] instanceof Text){
												if(((Text)seedHelp[k]).getText().length()!=0){
														seedCheck = ((Text) seedHelp[k]).getText().getBytes();
												}
											}
										}	
									}
								}
							}		
						}
					}
				}
				//Kevin, 29.01.2016 generate new merkleTree
				if(seedCheck.length > 0) {
					if(!Arrays.equals(seedCheck, merkle.getPrivateSeed()) || keyCheck != merkle.getLeafCounter()) {
						
						//merkle neu zuweisen
						merkle = new SimpleMerkleTree(seedCheck,seedCheck,keyCheck);
						merkle.selectOneTimeSignatureAlgorithm("SHA-256","WOTSPlus");
						merkle.generateKeyPairsAndLeaves();
						merkle.generateMerkleTree();
						if (tabFolder.getSelection()[0].getText().equals(Descriptions.MerkleTreeView_2)) {
							tabFolder.getSelection()[0].setControl(mtS);
						}
						if (tabFolder.getSelection()[0].getText().equals(Descriptions.MerkleTreeView_1)) {
							tabFolder.getSelection()[0].setControl(mtZ);
						}
					}
				}	

				if (tabFolder.getSelection()[0].getText().equals(Descriptions.MerkleTreeView_1)
						&& !merkle.isGenerated()) {
					MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setMessage(Descriptions.MerkleTree_Generation_Info);
					messageBox.setText("Info");
					messageBox.open();
					tabFolder.setSelection(0);
				} else if (tabFolder.getSelection()[0].getText().equals(Descriptions.MerkleTreeView_1)
						&& merkle.isGenerated()) {
					mtZ = new MerkleTreeZestComposite(tabFolder, SWT.NONE, merkle);
					tabFolder.getSelection()[0].setControl(mtZ);
				}
				if(tabFolder.getSelection()[0].getText().equals(Descriptions.MerkleTreeView_2) && !merkle.isGenerated()){
					MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setMessage(Descriptions.MerkleTree_Generation_Info);
					messageBox.setText("Info");
					messageBox.open();
					tabFolder.setSelection(0);
				} else if (tabFolder.getSelection()[0].getText().equals(Descriptions.MerkleTreeView_2)
						&& merkle.isGenerated()) {
					if(!mtS.getMerkleFromForm().equals(merkle)){
						mtS = new MerkleTreeSignatureComposite(tabFolder,SWT.NONE,merkle);
						
					}
					/*if(mtS.getSignatureFromForm().equals(Descriptions.MerkleTreeSign_3)
							|| mtS.getSignatureFromForm().equals(Descriptions.MerkleTreeSign_4)
							|| mtS.getSignatureFromForm().equals(Descriptions.MerkleTreeSign_5)) {
						//mtS.setSignatureFromForm(Descriptions.MerkleTreeSign_3);
						mtZ=mtZ;
					}*/
					tabFolder.getSelection()[0].setControl(mtS);
				}
				if (tabFolder.getSelection()[0].getText().equals(Descriptions.MerkleTreeView_3) && merkle.isGenerated()) {
					if(!mtS.getSignatureFromForm().isEmpty()) {
						String signature=mtS.getSignatureFromForm();
						String[]splittedSign = signature.split("\r\n");
						String otSign = "";
						String keyIndex = "";
						String message;
						message=mtS.getMessageFromForm();
						if(splittedSign.length> 1){
							otSign =splittedSign[0];
							keyIndex =splittedSign[1];
						}
						mtV=new MerkleTreeVerifikationComposite(tabFolder, SWT.NONE, merkle, Integer.parseInt(keyIndex),signature,message);
						tabFolder.getSelection()[0].setControl(mtV);
						
					}
					else
					{
						MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
						messageBox.setMessage(Descriptions.MerkleTree_Signature_Generation_Info);
						messageBox.setText("Info");
						messageBox.open();
						tabFolder.setSelection(0);
					}
				}
				else if(tabFolder.getSelection()[0].getText().equals(Descriptions.MerkleTreeView_3) && !merkle.isGenerated()){
					MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setMessage(Descriptions.MerkleTree_Generation_Info);
					messageBox.setText("Info");
					messageBox.open();
					tabFolder.setSelection(0);
				}
				
				
			}
		});
		// mtC.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
		// 1));

		
		TabItem tbtmParameter2 = new TabItem(tabFolder, SWT.NONE);
		tbtmParameter2.setText(Descriptions.MerkleTreeView_1);
		// Composite compositeTree = new Composite(tabFolder, SWT.NONE);
		// composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
		// 1, 1));

		// Hier soll der Baum bereits fertig sein! ACHTUNG BUG !!!!!
		// mtZ=new MerkleTreeZestComposite(tabFolder, SWT.NONE);
		tbtmParameter2.setControl(mtZ);
		
		TabItem tbtmParameter3 = new TabItem(tabFolder,SWT.NONE);
		tbtmParameter3.setText(Descriptions.MerkleTreeView_2);
		mtS=new MerkleTreeSignatureComposite(tabFolder,SWT.NONE,merkle);
		tbtmParameter3.setControl(mtS);
		
		TabItem tbtmParameter4 = new TabItem(tabFolder,SWT.NONE);
		tbtmParameter4.setText(Descriptions.MerkleTreeView_3);
		tbtmParameter4.setControl(mtV);
		
		mtC = new MerkleTreeComposite(tabFolder, SWT.NONE, this);
		tbtmParameter.setControl(mtC);

		scrolledComposite.setContent(tabFolder);
		scrolledComposite.setMinSize(tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// makes the connection to the help of the plug-in
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(),
		"org.jcryptool.visual.merkletree.merkletreeview");
	}

	/**
	 * This method synchronizes the merkleTree
	 * @param merkle
	 */
	public void setAlgorithm(ISimpleMerkle merkle) {
		if (merkle.getClass() == SimpleMerkleTree.class) {
			this.merkle = merkle;
			extended = false;
		} else if (merkle.getClass() == XMSSTree.class) {
			this.merkle = merkle;
			extended = true;
		}
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

}