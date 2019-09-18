package org.jcryptool.visual.sigVerification.ui.wizards;

import java.io.File;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;
import org.jcryptool.visual.sigVerification.algorithm.Input;

/**
 * This class contains the GUI elements for file input. It also contains a method to convert the
 * opened file into a byte array.
 * 
 * @author Wilfing
 */
public class InputFileComposite extends Composite implements SelectionListener {
    private Text txtPath;
    private InputFileWizardPage page;
    private int maxSize = 10485760; // Maximal size of the file (initial 10 MB but gets computed from free heap space later)
    private Input input;
    private File file;

    public InputFileComposite(Composite parent, int style, InputFileWizardPage p, Input input) {
        super(parent, style);
        this.input = input;
        this.page = p;
        
        setLayout(new GridLayout(2, false));

        txtPath = new Text(this, SWT.BORDER);
        txtPath.setEditable(false);
        GridData gd_txtPath = new GridData(SWT.FILL, SWT.FILL, true, false);
        txtPath.setLayoutData(gd_txtPath);
        if (input.path != "") {
        	try {
            	savePathString(input.path);
                page.setPageComplete(true);
        	} catch (Exception ex) { 
                LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
        	}
        }
        
        Button btnBrowse = new Button(this, SWT.NONE);
        GridData gd_btnBrowse = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_btnBrowse.widthHint = 150;
        gd_btnBrowse.horizontalIndent = 30;
        btnBrowse.setLayoutData(gd_btnBrowse);
        btnBrowse.setText(Messages.InputFileWizard_btnBrowse);
        btnBrowse.addSelectionListener(this);
        btnBrowse.setFocus();
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        try {
            FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
            fd.setText(Messages.InputWizard_FileOpenDialog);
            String strFile = fd.open();

            if (strFile != null && !strFile.isEmpty()) {
                savePathString(strFile);
                page.setPageComplete(true);
            }
        } catch (Exception ex) {
            LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
        }
    }
    
    /**
     * Saves the file path at input.path and the beginning of the byte[] as hex representation in input.tooltipData
     * 
     * @param pathString
     * @throws Exception
     */
    private void savePathString(String pathString) throws Exception {
    	updateMaxSize();
        file = new File(pathString);
        if (file.length() > maxSize) {
            MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_WARNING | SWT.OK);
            messageBox.setText(Messages.InputWizard_WarningTitle);
            messageBox.setMessage(Messages.InputWizard_WarningMessageTooLarge);
            messageBox.open();
            throw new Exception("The file " + file.getName() + " is too large."); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        //path string seems to be valid, save it!
    	input.path = pathString;
    	input.filename = Paths.get(pathString).getFileName().toString();
    	
    	//get Hex values from first 10 bytes for representatin via tooltip in the main screen
    	String dataPlain = Input.bytesToHex(Input.getBytesFromFile(file, 0, 10)); 
    	
    	//Insert whitespace after each two hex chars
    	int initialLength = dataPlain.length();
    	for (int i = 2, count = 0; i < initialLength; i+=2, count++) {
			dataPlain = new StringBuilder(dataPlain).insert(i + count, " ").toString();
    	}
    	input.tooltipData = dataPlain;
        txtPath.setText(file.getAbsolutePath());
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }
    
    /**
     * Computes the maximum size of files that can be opened and saves it in the member variable maxSize
     */
    public void updateMaxSize() {
    	long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    	long maxFree = Runtime.getRuntime().maxMemory() - usedMemory;
    	maxSize = (int) (0.9*(maxFree - 52428800)); //returns lower value for safety/stability reasons
    }
    
    /**
     * Converts the member variable maxSize into a MegaByte value. 
     * 
     * @return the value of maxSize in MB (Megabytes)
     */
    public int getMaxSizeInMB() {
    	return maxSize / 1048576;
    }
}
