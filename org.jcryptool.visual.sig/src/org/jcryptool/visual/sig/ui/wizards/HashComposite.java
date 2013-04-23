package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

//Contains the elements (2 group boxes) of the HashWizard
public class HashComposite extends Composite implements PaintListener {
	private Group grpHashes;

	//Constructor
	 public HashComposite(Composite parent, int style) {
	        super(parent, style);
	        //parent.setSize(600, 400); 
	        //Draw the controls
	        initialize();
	    }

	/**
	 * Draws all the controls of the composite
	 */
	private void initialize() {
	    GridData gd_grpSignatures = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	    gd_grpSignatures.widthHint = 141;
			
		Group grpDescription = new Group(this, SWT.NONE);
		GridData gd_grpDescription = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grpDescription.widthHint = 129;
		grpDescription.setLayoutData(gd_grpDescription);
		grpDescription.setText(Messages.HashWizard_grpDescription);
		grpDescription.setBounds(10, 186, 250, 163);
		
		setSize(new Point(273, 360));
		
	    grpHashes = new Group(this, SWT.NONE);
	    grpHashes.setLayoutData(gd_grpSignatures);
	    grpHashes.setText(Messages.HashWizard_grpHashes);
	    grpHashes.setBounds(10, 10, 250, 170);
	    
	    Button btnRadioButton = new Button(grpHashes, SWT.RADIO);
	    btnRadioButton.setSelection(true);
	    btnRadioButton.setBounds(10, 19, 91, 18);
	    btnRadioButton.setText(Messages.HashWizard_rdomd5);
	    
	    Button btnRadioButton_1 = new Button(grpHashes, SWT.RADIO);
	    btnRadioButton_1.setBounds(10, 43, 91, 18);
	    btnRadioButton_1.setText(Messages.HashWizard_rdosha1);
	    
	    Button btnRadioButton_2 = new Button(grpHashes, SWT.RADIO);
	    btnRadioButton_2.setBounds(10, 67, 91, 18);
	    btnRadioButton_2.setText(Messages.HashWizard_rdosha256);
	    
	    Button btnRadioButton_3 = new Button(grpHashes, SWT.RADIO);
	    btnRadioButton_3.setBounds(10, 91, 91, 18);
	    btnRadioButton_3.setText(Messages.HashWizard_rdosha384);
	    
	    Button btnRadioButton_4 = new Button(grpHashes, SWT.RADIO);
	    btnRadioButton_4.setBounds(10, 115, 91, 18);
	    btnRadioButton_4.setText(Messages.HashWizard_rdosha512);
	    
	    }


	@Override
	public void paintControl(PaintEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the grpHashes
	 */
	public Group getGrpHashes() {
		return grpHashes;
	}

}
