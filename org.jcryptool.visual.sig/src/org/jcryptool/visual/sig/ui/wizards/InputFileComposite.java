//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sig.ui.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

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
import org.jcryptool.visual.sig.SigPlugin;
import org.jcryptool.visual.sig.algorithm.Input;

/**
 * This class contains the GUI elements for file input. It also contains a method to convert the opened file into a byte
 * array.
 * 
 * @author Grebe
 */
public class InputFileComposite extends Composite implements SelectionListener {
    private Text txtPath;
    private InputFileWizardPage page;
    private int maxSize = 10485760; // Maximal size of the file (initial 10 MB but gets computed from free heap space later)
	private Button btnBrowse;

    public InputFileComposite(Composite parent, int style, InputFileWizardPage p) {
        super(parent, style);

        setLayout(new GridLayout(2, false));
               
        txtPath = new Text(this, SWT.BORDER);
        txtPath.setEditable(false);
        GridData gd_txtPath = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_txtPath.widthHint = 320;
        txtPath.setLayoutData(gd_txtPath);
        
        btnBrowse = new Button(this, SWT.NONE);
        GridData gd_btnBrowse = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_btnBrowse.widthHint = 150;
        btnBrowse.setLayoutData(gd_btnBrowse);
        btnBrowse.setText(Messages.InputFileWirard_btnBrowse);
        btnBrowse.addSelectionListener(this);

        page = p;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
    	//Listener for the browse button
        try {
            FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
            fd.setText(Messages.InputWizard_FileOpenDialog);
            String strFile = fd.open();
            
            if (strFile == null || strFile.isEmpty()) {
                return;
            }
            
            updateMaxSize();
            
            File file = new File(strFile);
            
            if (file.length() > maxSize) {
                MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_WARNING | SWT.OK);
                messageBox.setText(Messages.InputWizard_WarningTitle);
                messageBox.setMessage(Messages.InputWizard_WarningMessageTooLarge);
                messageBox.open();
                throw new Exception("The file " + file.getName() + " is too large."); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                // Call a method that converts the input file to a byte array and save the returned array in Input.data
                Input.data = getBytesFromFile(file);
            }

            if (Input.data == null) {
                MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_WARNING | SWT.OK);
                messageBox.setText(Messages.InputWizard_WarningTitle);
                messageBox.setMessage(Messages.InputWizard_WarningMessageEmpty);
                messageBox.open();
                throw new Exception("The file " + file.getName() + " appears to be empty."); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
            	Input.filename = strFile;
            	
            	//get Hex values from first 10 bytes for representatin via tooltip in the main screen
            	String dataPlain = Input.bytesToHex(Arrays.copyOfRange(Input.data, 0, Math.min(Input.data.length, 10))); 
            	
            	//Insert whitespace after each two hex chars
            	int initialLength = dataPlain.length();
            	for (int i = 2, count = 0; i < initialLength; i+=2, count++) {
        			dataPlain = new StringBuilder(dataPlain).insert(i + count, " ").toString();
            	}
            	Input.dataPlain = dataPlain;
            }
            txtPath.setText(file.getAbsolutePath());
            page.setPageComplete(true);
        } catch (Exception ex) {
            LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
        }
    }

	public void setInitialFocus() {
    	txtPath.setFocus();
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    /**
     * Converts the input file to a byte array
     * 
     * @param file The file elected by the user
     * @return The byte array
     */
    public byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size (in bytes) of the file
        long length = file.length();

        // Check if the file isn't 0 or larger than 10 MB
        if (length > maxSize || length <= 0) {
            // File is too large to process or empty
            is.close();
            return null;
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while ((offset < bytes.length) && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
            offset += numRead;
        }

        is.close();
        return bytes;
    }
    
    public void updateMaxSize() {
    	long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    	long maxFree = Runtime.getRuntime().maxMemory() - usedMemory;
    	maxSize = (int) (0.9*(maxFree - 52428800)); //returns lower value for safety reasons
    }
    
    public int getMaxSizeInMB() {
    	return (int) (maxSize / 1048576);
    }
}
