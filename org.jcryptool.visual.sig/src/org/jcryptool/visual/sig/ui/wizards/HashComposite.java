package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

//Contains the elements (2 group boxes) of the HashWizard
public class HashComposite extends Composite implements PaintListener{

	//Constructor
	 public HashComposite(Composite parent, int style) {
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
	    GridData gd_grpSignatures = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	    gd_grpSignatures.widthHint = 141;
	    grpSignatures.setLayoutData(gd_grpSignatures);
	    grpSignatures.setText("Signature methods");
	    grpSignatures.setBounds(10, 10, 201, 170);
			
		Group grpDescription = new Group(this, SWT.NONE);
		GridData gd_grpDescription = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grpDescription.widthHint = 129;
		grpDescription.setLayoutData(gd_grpDescription);
		grpDescription.setText("Description");
		grpDescription.setBounds(10, 186, 201, 163);
		
		setSize(new Point(250, 400));
	    }


	@Override
	public void paintControl(PaintEvent e) {
		// TODO Auto-generated method stub
		
	}
}
