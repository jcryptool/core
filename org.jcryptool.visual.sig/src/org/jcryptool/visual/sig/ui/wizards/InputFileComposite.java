package org.jcryptool.visual.sig.ui.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sig.SigPlugin;

/**
 * 
 * @author Grebe
 * This class contains the GUI elements for file input. It also contains a method to convert 
 * the opened file into a byte array.
 *
 */
public class InputFileComposite extends Composite implements PaintListener, SelectionListener {
	
	private Text txtPath;
	private File file = null;
	private InputFileWizardPage page;
	private int maxSize = 10485760; //Maximal size of the file (10 MB)

	public InputFileComposite(Composite parent, int style, InputFileWizardPage p) {
		super(parent, style);
		
		txtPath = new Text(this, SWT.BORDER);
		txtPath.setEditable(false);
		txtPath.setBounds(10, 10, 323, 19);
		//txtPath.setText(org.jcryptool.visual.sig.algorithm.Input.path);
		
		Button btnBrowse = new Button(this, SWT.NONE);
		btnBrowse.setBounds(339, 6, 94, 28);
		btnBrowse.setText(Messages.InputFileWirard_btnBrowse);
		btnBrowse.addSelectionListener(this);
		btnBrowse.setFocus();
		
		page = p;
	}

	@Override
	public void paintControl(PaintEvent e) {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		try {
			String strFile = null;
			FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
			//fd.setFilterExtensions(new String[] { "*.txt", "*.*" });
			fd.setText(Messages.InputWizard_FileOpenDialog);
			strFile = fd.open();
			
			file = new File(strFile);
			//long l = file.length();
			if (file.length() > maxSize) {
				MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_WARNING | SWT.OK);
                messageBox.setText(Messages.InputWizard_WarningTitle); 
                messageBox.setMessage(Messages.InputWizard_WarningMessageTooLarge);
                messageBox.open();				
                throw new Exception ("The file " + file.getName() + " is too large.");
			}
			
			//Call a method that converts the input file to a byte array and save the returned array in Input.java
			org.jcryptool.visual.sig.algorithm.Input.data = getBytesFromFile(file); 
			
			if (org.jcryptool.visual.sig.algorithm.Input.data == null) {
				 MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_WARNING | SWT.OK);
                 messageBox.setText(Messages.InputWizard_WarningTitle); 
                 messageBox.setMessage(Messages.InputWizard_WarningMessageEmpty);
                 messageBox.open();				
                 throw new Exception ("The file " + file.getName() + " appears to be empty.");
			}
			
			txtPath.setText(file.getAbsolutePath());
			//org.jcryptool.visual.sig.algorithm.Input.path = file.getAbsolutePath();
			page.setPageComplete(true);
		}
		catch (Exception ex) {
			
			LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		
	}
	
	/**
	 * Converts the input file to a byte array
	 * @param file The file elected by the user 
	 * @return The byte array
	 */
	public byte[] getBytesFromFile(File file) throws IOException {
		int maxSize = 10485760; //10 MB
	    InputStream is = new FileInputStream(file);
	 
	    // Get the size (in bytes) of the file
	    long length = file.length();
	 
	    //Check if the file isn't 0 or larger than 10 MB
	    if (length > maxSize || length <= 0) {	
	        //File is too large to process or empty
	    	is.close();
	        return null;
	    }
	 
	    // Create the byte array to hold the data
	    byte[] bytes = new byte[(int)length];
	 
	    // Read in the bytes
	    int offset = 0;
	    int numRead = 0;
	    while ((offset < bytes.length) && ((numRead=is.read(bytes, offset, bytes.length-offset)) >= 0)) {
	        offset += numRead;
	    }
	    /*
	    // Ensure all the bytes have been read in
	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file " + file.getName());
	    }
	 	*/
	    is.close();
	    return bytes;
	}
	
}
