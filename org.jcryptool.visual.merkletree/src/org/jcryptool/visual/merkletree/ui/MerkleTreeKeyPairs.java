package org.jcryptool.visual.merkletree.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.MerkleTreeView;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.algorithm.XMSSTree;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;


/**
 * Class for the Composite with the KeyPair generation in Tabpage 1
 * @author Fabian Mayer
 * TODO: Kommentare
 * TODO: Keygeneration
 *
 */
public class MerkleTreeKeyPairs extends Composite {

	Button buttonCreateKeys;
	Label createLabel;
	StyledText descText;
	private int spinnerValue;
	private int treeValue;

	/**
	 * Create the composite.
	 * Including KeyPair content and button for keyPair generation
	 * @param parent
	 * @param style
	 */
	public MerkleTreeKeyPairs(Composite parent, int style, SUIT mode, ViewPart masterView) {
		super(parent, style);
		treeValue = 0;
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));

		createLabel = new Label(this, SWT.NONE);
		createLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));
		

		descText = new StyledText(this, SWT.WRAP | SWT.BORDER);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 2));
		descText.setCaret(null);
		


		Label keysum = new Label(this, SWT.NONE);
		keysum.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));


		Spinner spinnerkeysum = new Spinner(this, SWT.BORDER);
		spinnerkeysum.setMaximum(1024);
		spinnerkeysum.setMinimum(2);
		spinnerValue = 2;
		spinnerkeysum.setSelection(0);
		spinnerkeysum.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
		
		//spinner power of 2 amount of keys
		spinnerkeysum.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() instanceof Spinner) {
					Spinner spinner = (Spinner) e.getSource();
					int selection = spinner.getSelection();
					//
					if (selection < spinnerValue) {
						
						spinner.setSelection(spinnerValue / 2);
					} else {
						spinner.setSelection(spinnerValue * 2);
					}
					spinnerValue = spinner.getSelection();
				}
				((MerkleTreeView)masterView).updateElement();
			}
		});
		
		

		buttonCreateKeys = new Button(this, SWT.NONE);
		buttonCreateKeys.setEnabled(true);
		buttonCreateKeys.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
		
		
		// if MT -> Anzahl ebenen
		if(mode == SUIT.XMSS_MT){
			
			Label trees = new Label(this, SWT.NONE);
			trees.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			trees.setText(Descriptions.XMSS_MT.Tab0_Lable2);
			
			Spinner treespinner = new Spinner(this, SWT.BORDER);
			treespinner.setMaximum(64);
			treespinner.setMinimum(2);
			treespinner.setSelection(0);
			treespinner.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
			treespinner.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					((MerkleTreeView)masterView).updateElement();
					treeValue = treespinner.getSelection();
				}
			});
		}
		
		
		keysum.setText(Descriptions.Tab0_Lable1);
		createLabel.setText(Descriptions.Tab0_Head2);
		buttonCreateKeys.setText(Descriptions.Tab0_Button2);
		switch(mode){
			case XMSS:
				descText.setText(Descriptions.XMSS.Tab0_Txt2);
				break;
			case XMSS_MT:
				descText.setText(Descriptions.XMSS_MT.Tab0_Txt2);
				break;
			case MSS:
			default:
				descText.setText(Descriptions.MSS.Tab0_Txt2);
				break;
		}
		descText.setEditable(false);
		

		buttonCreateKeys.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 generates the MerkleTree and the KeyPairs
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISimpleMerkle merkle;
				
				/*
				 * select the type of scheme
				 */
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
				
				
				merkle.setPublicSeed(((MerkleTreeSeed)parent).getSeed());
				/*
				 * if the generated Tree is a XMSSTree -> the Bitmaskseed is also needed
				 */
				if(merkle instanceof XMSSTree){
					((XMSSTree) merkle).setBitmaskSeed(((MerkleTreeSeed)parent).getBitmaskSeed());	
				}
				merkle.setLeafCount(spinnerValue);
				merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
				merkle.generateKeyPairsAndLeaves();
				merkle.generateMerkleTree();
				((MerkleTreeView) masterView).setAlgorithm(merkle, mode);
				
			}
		});

		//spinner power of 2 if empty (possible?) 
		/* Not Possible empty @Lindi
		spinnerkeysum.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {

				Control[] controls = parent.getChildren();
				for (int i = 0; i < controls.length; i++) {
					if (controls[i] instanceof Text) {
						if (((Text) controls[i]).getText().length() == 0) {
							buttonCreateKeys.setEnabled(false);
						} else {
							buttonCreateKeys.setEnabled(true);
						}
					}
				}
			}
		});
		*/

	}

	public int getKeyAmmount(){
		return spinnerValue;
	}
	public int getTreeAmmount(){
		return treeValue;
		
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
