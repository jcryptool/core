package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;

public class HashComposite extends Composite implements PaintListener{

	//Constructor
	 public HashComposite(Composite parent, int style) {
	        super(parent, style);
	        parent.setSize(600, 600); 
	        initialize();
	    }

	private void initialize() {
		
	    Group grpSignatures = new Group(this, SWT.NONE);
	    GridData gd_grpSignatures = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	    gd_grpSignatures.widthHint = 141;
	    grpSignatures.setLayoutData(gd_grpSignatures);
	    grpSignatures.setText("Signature methods");
	    grpSignatures.setBounds(10, 10, 201, 213);
			
		Group grpDescription = new Group(this, SWT.NONE);
		GridData gd_grpDescription = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grpDescription.widthHint = 129;
		grpDescription.setLayoutData(gd_grpDescription);
		grpDescription.setText("Description");
		grpDescription.setBounds(10, 229, 201, 120);
			
		Button btnOk = new Button(this, SWT.NONE);
		btnOk.setBounds(10, 355, 94, 28);
		btnOk.setText("OK");
			
		Button btnCancel = new Button(this, SWT.NONE);
		btnCancel.setBounds(117, 355, 94, 28);
		btnCancel.setText("Cancel");
		
		
	    }


	@Override
	public void paintControl(PaintEvent e) {
		// TODO Auto-generated method stub
		
	}
}
