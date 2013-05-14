package org.jcryptool.visual.sig.ui.wizards;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sig.SigPlugin;

public class InputFileComposite extends Composite implements PaintListener, SelectionListener {
	
	private Text txtPath;
	private File file = null;
	private InputFileWizardPage page;

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
			convertInput(file);
			
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
	 */
	public void convertInput(File file) throws Exception {
		byte[] array = new byte[4096]; //Maximum size??
		int read = 0;
		if (file != null){
			ByteArrayOutputStream baos = null;
			InputStream is = null;
			try {
				baos = new ByteArrayOutputStream();
				is = new FileInputStream(file);
		        while ( (read = is.read(array)) != -1 ) {
		            baos.write(array, 0, read);
		        }//end while
			} finally { 
		        try {
		             if ( baos != null ) 
		                 baos.close();
		        } catch ( Exception e) {
		        	LogUtil.logError(SigPlugin.PLUGIN_ID, e);
		        }//end catch

		        try {
		             if ( is != null ) 
		                  is.close();
		        } catch ( Exception e) {
		        	LogUtil.logError(SigPlugin.PLUGIN_ID, e);
		        }//end catch
		    }//end finally
			org.jcryptool.visual.sig.algorithm.Input.data = array;
			//input.setInput(array);
		} else {
			//array =
		}
	}//end convertInput
	
	
}
