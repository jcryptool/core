package org.jcryptool.visual.sig.ui.wizards;




import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;


public class InputFileComposite extends Composite {

	String strFile = null;
	
	//Constructor
	public InputFileComposite(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		
		//InputFileDialog();
		
	}

	public void InputFileDialog () {
		// only txt-files are allowed
		// TODO starts at the beginning... 
		
		FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
		fd.setFilterExtensions(new String[] {"*.txt", "*.*"});
		fd.setText("Open file");  
		strFile = fd.open();
		
	}
	
}// end class

