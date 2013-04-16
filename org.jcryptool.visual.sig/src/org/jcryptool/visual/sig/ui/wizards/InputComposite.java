package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;

public class InputComposite extends Composite implements PaintListener{
	
	//Constructor
	public InputComposite(Composite parent, int style) {
		super(parent, style);
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
