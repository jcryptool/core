package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

public class SignatureComposite extends Composite implements SelectionListener{
	private Group grpSignatures;
	private Text txtDescription;
	private Button rdo1;
	private Button rdo2;
	private Button rdo3;
	private Button rdo4;
	
	//Constructor
	public SignatureComposite(Composite parent, int style) {
		super(parent, style);
		//parent.setSize(600, 400); 
	    //Draw the controls
	    initialize();
	}
		
	/**
	* Draws all the controls of the composite
    */
	private void initialize() {
		grpSignatures = new Group(this, SWT.NONE);
		grpSignatures.setText(Messages.SignatureWizard_grpSignatures);
		grpSignatures.setBounds(10, 10, 300, 151);
		
		rdo1 = new Button(grpSignatures, SWT.RADIO);
		rdo1.setSelection(true);
		rdo1.setBounds(10, 19, 118, 18);
		rdo1.setText(Messages.SignatureWizard_DSA);
		
		rdo2 = new Button(grpSignatures, SWT.RADIO);
		rdo2.setBounds(10, 43, 118, 18);
		rdo2.setText(Messages.SignatureWizard_RSA);
		
		rdo3 = new Button(grpSignatures, SWT.RADIO);
		rdo3.setEnabled(false);
		rdo3.setBounds(10, 67, 118, 18);
		rdo3.setText(Messages.SignatureWizard_ECDSA);
		
		rdo4 = new Button(grpSignatures, SWT.RADIO);
		rdo4.setEnabled(false);
		rdo4.setBounds(10, 91, 118, 18);
		rdo4.setText(Messages.SignatureWizard_RSAandMGF1);
	
		Group grpDescription = new Group(this, SWT.NONE);
		grpDescription.setText(Messages.SignatureWizard_grpDescription);
		grpDescription.setBounds(10, 171, 300, 255);
		
		txtDescription = new Text(grpDescription, SWT.WRAP | SWT.TRANSPARENT);
		txtDescription.setEditable(false);
		txtDescription.setBounds(10, 15, 275, 213);
		txtDescription.setBackground(new Color(Display.getCurrent(), 220, 220, 220));
		txtDescription.setText(Messages.SignatureWizard_DSA_description);
		
		 //Add event listeners
	    rdo1.addSelectionListener(this);
	    rdo2.addSelectionListener(this);
	    rdo3.addSelectionListener(this);
	    rdo4.addSelectionListener(this);
	    //rdo5.addSelectionListener(this);
	    
	  //If called by JCT-CA only SHA-256 can be used! Therefore only ECDSA, RSA and RSA with MGF1 will work
	    if (org.jcryptool.visual.sig.algorithm.Input.privateKey != null) {
			rdo1.setEnabled(false);
			rdo1.setSelection(false);
			rdo2.setSelection(true);
			rdo3.setEnabled(false);
			rdo4.setEnabled(false);
		}   
	}
	
	/**
	 * @return the grpSignatures
	 */
	public Group getgrpSignatures() {
		return grpSignatures;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (rdo1.getSelection()) 
			txtDescription.setText(Messages.SignatureWizard_DSA_description);

		else if (rdo2.getSelection()) 
			txtDescription.setText(Messages.SignatureWizard_RSA_description);
		
		else if (rdo3.getSelection()) 
			txtDescription.setText(Messages.SignatureWizard_ECDSA_description);
		
		else if (rdo4.getSelection()) 
			txtDescription.setText(Messages.SignatureWizard_RSAandMGF1_description);
		
	}//end widgetSelected

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
