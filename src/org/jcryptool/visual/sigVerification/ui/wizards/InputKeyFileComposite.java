//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.jcryptool.visual.sigVerification.ui.wizards.Messages;

/**
 * This class contains the GUI elements for file input. It also contains a method to convert the opened file into a byte
 * array.
 * 
 * @author Grebe/Wilfing
 */
public class InputKeyFileComposite extends Composite implements SelectionListener {
    private Text txtPath;
    private File file = null;
    private InputKeyFileWizardPage page;
    private int maxSize = 10485760; // Maximal size of the file (10 MB)

    public InputKeyFileComposite(Composite parent, int style, InputKeyFileWizardPage p) {
        super(parent, style);

        txtPath = new Text(this, SWT.BORDER);
        txtPath.setEditable(false);
        txtPath.setBounds(10, 10, 323, 19);

        Button btnBrowse = new Button(this, SWT.NONE);
        btnBrowse.setBounds(339, 6, 94, 28);
        btnBrowse.setText(Messages.InputKeyFileWizard_btnBrowse);
        btnBrowse.addSelectionListener(this);
        btnBrowse.setFocus();

        page = p;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        try {
            FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
            fd.setText(Messages.InputKeyWizard_FileOpenDialog);
            String strFile = fd.open();
            
            if (strFile == null || strFile.isEmpty()) {
                return;
            }
            
            file = new File(strFile);
            if (file.length() > maxSize) {
                MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_WARNING | SWT.OK);
                messageBox.setText(Messages.InputKeyWizard_WarningTitle);
                messageBox.setMessage(Messages.InputKeyWizard_WarningMessageTooLarge);
                messageBox.open();
                throw new Exception("The file " + file.getName() + " is too large."); //$NON-NLS-1$ //$NON-NLS-2$
            }

            // Call a method that converts the input file to a byte array and save the returned array in Input.java
            Input.pubKey = getBytesFromFile(file);

            if (Input.pubKey == null) {
                MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_WARNING | SWT.OK);
                messageBox.setText(Messages.InputKeyWizard_WarningTitle);
                messageBox.setMessage(Messages.InputKeyWizard_WarningMessageEmpty);
                messageBox.open();
                throw new Exception("The file " + file.getName() + " appears to be empty."); //$NON-NLS-1$ //$NON-NLS-2$
            }

            txtPath.setText(file.getAbsolutePath());
            page.setPageComplete(true);
        } catch (Exception ex) {
            LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
        }
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
        int maxSize = 10485760; // 10 MB
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
}
