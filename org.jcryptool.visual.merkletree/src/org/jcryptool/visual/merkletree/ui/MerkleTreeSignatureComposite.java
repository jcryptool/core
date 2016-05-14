package org.jcryptool.visual.merkletree.ui;

//import java.security.SecureRandom;

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
//import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;

/**
 * Composite for the Tabpage "Signatur"
 * @author Kevin Muehlboeck
 *
 */
public class MerkleTreeSignatureComposite extends Composite {

	/**
	 * Create the composite.
	 * Includes Message definition, Signature generation and Signature content
	 * @param parent
	 * @param style
	 */
	Label sign;
	Text textSign;
	Button createSign;
	StyledText styledTextSign;
	StyledText styledTextSignSize;
	Label lSignaturSize;
	Label lkeyNumber;
	StyledText styledTextKeyNumber;
	ISimpleMerkle merkle;
	private String usedText;
	public MerkleTreeSignatureComposite(Composite parent, int style, ISimpleMerkle merkle) {
		super(parent, SWT.NONE);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		
		this.merkle=merkle;
		sign = new Label(this, SWT.NONE);
		sign.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));
		sign.setText(Descriptions.MerkleTreeSign_0);

		textSign = new Text(this, SWT.BORDER | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textSign = new GridData(SWT.FILL,SWT.FILL,true,true,MerkleConst.H_SPAN_MAIN,1);
		textSign.setLayoutData(gd_textSign);
		textSign.setText(Descriptions.MerkleTreeSign_1);
		createSign = new Button(this, SWT.NONE);
		createSign.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN/2, 1));
		createSign.setText(Descriptions.MerkleTreeSign_2);

		lkeyNumber = new Label(this,SWT.READ_ONLY | SWT.WRAP | SWT.RIGHT);
		lkeyNumber.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false,MerkleConst.H_SPAN_MAIN/5,1));
		lkeyNumber.setText(Descriptions.MerkleTreeSign_7);
		
		styledTextKeyNumber = new StyledText(this,SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		styledTextKeyNumber.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false,MerkleConst.H_SPAN_MAIN/5,1));
		styledTextKeyNumber.setText("");
		
		lSignaturSize = new Label(this,SWT.READ_ONLY | SWT.WRAP | SWT.RIGHT);
		lSignaturSize.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false,MerkleConst.H_SPAN_MAIN/5,1));
		lSignaturSize.setText(Descriptions.MerkleTreeSign_6);
		
		styledTextSignSize = new StyledText(this,SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		styledTextSignSize.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false,MerkleConst.H_SPAN_MAIN/5,1));
		styledTextSignSize.setText("");
		
		styledTextSign = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		styledTextSign.setText(Descriptions.MerkleTreeSign_3);
		//gd_styledTextTree.widthHint = 960;
		//gd_styledTextTree.heightHint = 40;;
		GridData gd_textTextSign = new GridData(SWT.FILL,SWT.FILL,true,true,MerkleConst.H_SPAN_MAIN,1);
		styledTextSign.setLayoutData(gd_textTextSign);
		
		createSign.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 * Event to create a Signature
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( textSign.getText()!= "") {
				String signature = merkle.sign(textSign.getText());
				usedText=textSign.getText();
				String[]splittedSign = signature.split("\r\n");
				String otSign = "";
				String keyIndex = "";
				if(splittedSign.length> 1){
					otSign =splittedSign[0];
					keyIndex =splittedSign[1];
				}
				styledTextSignSize.setText(Integer
						.toString(org.jcryptool.visual.merkletree.files.Converter._stringToByte(otSign).length / 2)
						+ "/" + (merkle.getOneTimeSignatureAlgorithm().getN() * merkle.getOneTimeSignatureAlgorithm().getL()) + " Bytes");
				styledTextKeyNumber.setText(keyIndex);
				if(signature == "") {
					styledTextSign.setText(Descriptions.MerkleTreeSign_4);
				}
				else
					styledTextSign.setText(signature);
				}
				else {
					styledTextSign.setText(Descriptions.MerkleTreeSign_5);
				}
				
			}
		});
		textSign.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
						if (textSign.getText().length() > 0) {
							createSign.setEnabled(true);
						} else {
							createSign.setEnabled(false);
						}
					
				
			}
		});
	}

	/**
	 * Synchronizes Signature with the other Tabpages
	 * @return Signature
	 */
	public String getSignatureFromForm() {
		if (this.styledTextSign.getText().equals(Descriptions.MerkleTreeSign_3) ||
				this.styledTextSign.getText().equals(Descriptions.MerkleTreeSign_4) ||
				this.styledTextSign.getText().equals(Descriptions.MerkleTreeSign_5))
			return "";
		
		return this.styledTextSign.getText();
	}
	public String getMessageFromForm() {
		return usedText;
	}

	/**
	 * Synchronizes the MerkleTree Object with the other Tabpages
	 * @return ISimpleMerkle Object
	 */
	public ISimpleMerkle getMerkleFromForm(){
		return this.merkle;
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
