package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

public class InputComposite extends Composite implements PaintListener{
	
	//Constructor
	public InputComposite(Composite parent, int style) {
		super(parent, style);
		
		Button rdoFromFile = new Button(this, SWT.RADIO);
		rdoFromFile.setBounds(10, 10, 91, 18);
		rdoFromFile.setText(Messages.InputWizard_rdoFromFile);
		
		Button rdoFromEditor = new Button(this, SWT.RADIO);
		rdoFromEditor.setBounds(10, 34, 157, 18);
		rdoFromEditor.setText(Messages.InputWizard_rdoFromEditor);
		parent.setSize(600, 400); 
		//Draw the controls
		initialize();
	}
	/**
	* Draws all the controls of the composite
	*/
	private void initialize() {
				
	}//end initialize
	
	@Override
	public void paintControl(PaintEvent e) {
		// TODO Auto-generated method stub
		
	}
}
