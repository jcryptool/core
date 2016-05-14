package org.jcryptool.visual.merkletree.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.MerkleTreeView;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

//TODO: MAXI Kommentare
/**
 * Class for the Composite with the KeyPair generation in Tabpage 1
 * @author Fabian Mayer
 *
 */
public class MerkleTreeKeyPairs extends Composite {
	//private Composite parent;
	Button buttonCreateKeys;
	Label createLabel;
	StyledText descText;
	private byte[] seedArray;
	//private int leafcounter;
	//private Spinner spinnerkeysum;
	//private ViewPart masterView;
	private int spinnerValue;

	/**
	 * Create the composite.
	 * Including KeyPair content and button for keyPair generation
	 * @param parent
	 * @param style
	 */
	public MerkleTreeKeyPairs(Composite parent, int style, SUIT verfahren, ViewPart masterView) {
		super(parent, style);
		//this.parent = parent;
		//this.masterView = masterView;
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));

		createLabel = new Label(this, SWT.NONE);
		createLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));
		

		descText = new StyledText(this, SWT.WRAP | SWT.BORDER);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 2));
		descText.setCaret(null);
		


		Label keysum = new Label(this, SWT.NONE);
		keysum.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));


		Spinner spinnerkeysum = new Spinner(this, SWT.BORDER);
		spinnerkeysum.setMaximum(1073741824);
		spinnerkeysum.setMinimum(2);
		spinnerValue = 2;
		spinnerkeysum.setSelection(0);
		spinnerkeysum.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
		
		
		
		//spinner for power of two
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
			}
		});
		
		

		buttonCreateKeys = new Button(this, SWT.NONE);
		buttonCreateKeys.setEnabled(false);
		buttonCreateKeys.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
		
		
		//TEXT
		//TODO: switch XMMSMT/MSS
		if(verfahren == SUIT.XMSS_MT){
			
			Label trees = new Label(this, SWT.NONE);
			trees.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			trees.setText(Descriptions.XMSS_MT.Tab0_Lable2);

			Spinner treespinner = new Spinner(this, SWT.BORDER);
			treespinner.setMaximum(1073741824);
			treespinner.setMinimum(2);
			treespinner.setSelection(0);
			treespinner.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
		}
		
		
		keysum.setText(Descriptions.Tab0_Lable1);
		createLabel.setText(Descriptions.Tab0_Head2);
		buttonCreateKeys.setText(Descriptions.Tab0_Button2);
		switch(verfahren){
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
				
				

		/*
		 * Table table = new Table(this, SWT.MULTI | SWT.BORDER |
		 * SWT.FULL_SELECTION); table.setLayoutData(new GridData(SWT.CENTER,
		 * SWT.CENTER, true, true, MerkleConst.H_SPAN_MAIN, 1));
		 * 
		 * TableColumn column = new TableColumn(table, SWT.NONE);
		 * column.setText(Descriptions.MerkleTreeKey_column_0); column = new
		 * TableColumn(table, SWT.NULL);
		 * column.setText(Descriptions.MerkleTreeKey_column_1); column = new
		 * TableColumn(table, SWT.NULL);
		 * column.setText(Descriptions.MerkleTreeKey_column_2);table.
		 * setHeaderVisible(true);
		 */
		buttonCreateKeys.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 generates the MerkleTree and the KeyPairs
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Control[] controls = parent.getChildren();
				for (int i = 0; i < controls.length; i++) {
					if (controls[i] instanceof Composite && !(controls[i] instanceof MerkleTreeKeyPairs)) {
						for(Control control : ((Composite) controls[i]).getChildren()) {
							if(control instanceof Text)
								seedArray = ((Text) control).getText().getBytes();
						}
						
					}
				}
				ISimpleMerkle merkle = new SimpleMerkleTree(seedArray, seedArray, spinnerkeysum.getSelection());
				merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
				merkle.generateMerkleTree();
				MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
				messageBox.setMessage(Descriptions.MerkleTreeKey_Message);
				messageBox.setText("Info");
				messageBox.open();
				MerkleTreeView view = (MerkleTreeView) masterView;
				view.setAlgorithm(merkle, verfahren);
			}
		});

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

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
