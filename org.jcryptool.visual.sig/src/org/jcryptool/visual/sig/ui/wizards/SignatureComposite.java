package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

public class SignatureComposite extends Composite implements PaintListener{

	//Constructor
	public SignatureComposite(Composite parent, int style) {
		super(parent, style);
		parent.setSize(600, 400); 
	    //Draw the controls
	    initialize();
	}
		
	/**
	* Draws all the controls of the composite
    */
	private void initialize() {
		Group grpSignatures = new Group(this, SWT.NONE);
		grpSignatures.setText("Signature methods");
		grpSignatures.setBounds(10, 10, 250, 170);
		
		Button btnRadioButton = new Button(grpSignatures, SWT.RADIO);
		btnRadioButton.setBounds(10, 10, 118, 18);
		btnRadioButton.setText("MD5 with RSA");
		
		Button btnRadioButton_1 = new Button(grpSignatures, SWT.RADIO);
		btnRadioButton_1.setBounds(10, 34, 118, 18);
		btnRadioButton_1.setText("SHA1 with RSA");
		
		Button btnRadioButton_2 = new Button(grpSignatures, SWT.RADIO);
		btnRadioButton_2.setBounds(10, 58, 118, 18);
		btnRadioButton_2.setText("SHA256 with RSA");
		
		Button btnRadioButton_3 = new Button(grpSignatures, SWT.RADIO);
		btnRadioButton_3.setBounds(10, 82, 118, 18);
		btnRadioButton_3.setText("SHA384 with RSA");
		
		Button btnRadioButton_4 = new Button(grpSignatures, SWT.RADIO);
		btnRadioButton_4.setBounds(10, 106, 118, 18);
		btnRadioButton_4.setText("SHA512 with RSA");
		
		Group grpDescription = new Group(this, SWT.NONE);
		grpDescription.setText("Description");
		grpDescription.setBounds(10, 188, 250, 163);
	}

	@Override
	public void paintControl(PaintEvent e) {
		// TODO Auto-generated method stub
		
	}
}
