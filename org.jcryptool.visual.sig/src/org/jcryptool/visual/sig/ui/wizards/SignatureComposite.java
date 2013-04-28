package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

public class SignatureComposite extends Composite implements PaintListener, SelectionListener{
	private Group grpSignatures;
	private Text txtDescription;
	private Button rdo1;
	private Button rdo2;
	private Button rdo3;
	private Button rdo4;
	private Button rdo5;
	
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
		grpSignatures.setBounds(10, 10, 250, 170);
		
		rdo1 = new Button(grpSignatures, SWT.RADIO);
		rdo1.setSelection(true);
		rdo1.setBounds(10, 19, 118, 18);
		rdo1.setText(Messages.SignatureWizard_rdomd5);
		
		rdo2 = new Button(grpSignatures, SWT.RADIO);
		rdo2.setBounds(10, 43, 118, 18);
		rdo2.setText(Messages.SignatureWizard_rdosha1);
		
		rdo3 = new Button(grpSignatures, SWT.RADIO);
		rdo3.setBounds(10, 67, 118, 18);
		rdo3.setText(Messages.SignatureWizard_rdosha256);
		
		rdo4 = new Button(grpSignatures, SWT.RADIO);
		rdo4.setBounds(10, 91, 118, 18);
		rdo4.setText(Messages.SignatureWizard_rdosha384);
		
		rdo5 = new Button(grpSignatures, SWT.RADIO);
		rdo5.setBounds(10, 115, 118, 18);
		rdo5.setText(Messages.SignatureWizard_rdosha512);
		
		Group grpDescription = new Group(this, SWT.NONE);
		grpDescription.setText(Messages.SignatureWizard_grpDescription);
		grpDescription.setBounds(10, 184, 250, 163);
		
		txtDescription = new Text(grpDescription, SWT.WRAP | SWT.TRANSPARENT);
		txtDescription.setEditable(false);
		txtDescription.setBounds(10, 20, 226, 133);
		txtDescription.setBackground(new Color(Display.getCurrent(), 220, 220, 220));
		txtDescription.setText(Messages.HashWizard_rdomd5_description);
		
		 //Add event listeners
	    rdo1.addSelectionListener(this);
	    rdo2.addSelectionListener(this);
	    rdo3.addSelectionListener(this);
	    rdo4.addSelectionListener(this);
	    rdo5.addSelectionListener(this);
	}

	@Override
	public void paintControl(PaintEvent e) {
		// TODO Auto-generated method stub
		
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
			txtDescription.setText(Messages.HashWizard_rdomd5_description);

		if (rdo2.getSelection()) 
			txtDescription.setText(Messages.HashWizard_rdosha1_description);
		
		if (rdo3.getSelection()) 
			txtDescription.setText(Messages.HashWizard_rdosha256_description);
		
		if (rdo4.getSelection()) 
			txtDescription.setText(Messages.HashWizard_rdosha384_description);
		
		if (rdo5.getSelection()) 
			txtDescription.setText(Messages.HashWizard_rdosha512_description);
		
	}//end widgetSelected

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
