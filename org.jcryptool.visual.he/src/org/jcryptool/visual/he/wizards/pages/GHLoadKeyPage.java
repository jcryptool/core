// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.wizards.pages;

import java.io.BufferedReader;
import java.io.FileReader;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.visual.he.Messages;

/**
 * Page to load an existing key for Gentry & Halevi fully homomorphic visualization
 *
 * @author Coen Ramaekers
 *
 */
public class GHLoadKeyPage extends WizardPage {
    private static final String PAGENAME = "Load key page"; //$NON-NLS-1$
    private static final String TITLE = Messages.GHLoadKeyPage_Title;

    /** labels for key information */
    private Label own, desc;

    /** field for the owner of this keypair. */
    protected Text owner;

    /** field for the password. */
    private Text password;

    /** Key file */
    private String filename = null;

    public GHLoadKeyPage(Shell shell) {
        super(PAGENAME, TITLE, null);
        setDescription(Messages.LoadKeyPage_description);
        setPageComplete(false);
    }

    public static String getPagename() {
        return PAGENAME;
    }

    public void createControl(Composite parent) {
    	 final Composite composite = new Composite(parent, SWT.NONE);
         composite.setLayout(new GridLayout());	
         
         final Button loadKey = new Button(composite, SWT.PUSH);
         final GridData gd_loadKey = new GridData(SWT.LEFT, SWT.CENTER, false, false);
         gd_loadKey.widthHint = 220;
         loadKey.setLayoutData(gd_loadKey);
         loadKey.setText(Messages.LoadKeyPage_select_file);
         loadKey.addSelectionListener(new SelectionAdapter() {
             @Override
             public void widgetSelected(final SelectionEvent e) {
               loadKeyData();
             }
         });

         own = new Label(composite, SWT.NONE);
         GridData gd_own = new GridData(SWT.FILL, SWT.CENTER, true, false);
         gd_own.verticalIndent = 15;
         own.setLayoutData(gd_own);
         own.setText(Messages.GHLoadKeyPage_Owner + ": ");

         desc = new Label(composite, SWT.NONE);
         GridData gd_desc = new GridData(SWT.FILL, SWT.CENTER, true, false);
         desc.setLayoutData(gd_desc);
         desc.setText(Messages.GHLoadKeyPage_Description + ": ");

         final Label pass = new Label(composite, SWT.NONE);
         pass.setText(Messages.GHLoadKeyPage_Enter_Password + ":");
         GridData gd_pass = new GridData(SWT.FILL, SWT.CENTER, true, false);
         gd_pass.verticalIndent = 15;
         pass.setLayoutData(gd_pass);

         password = new Text(composite, SWT.BORDER | SWT.PASSWORD);
         GridData gd_password = new GridData(SWT.LEFT, SWT.CENTER, false, false);
         gd_password.widthHint = 200;
         password.setLayoutData(gd_password);
         password.setEnabled(false);
         password.addModifyListener(new ModifyListener() {
             public void modifyText(final ModifyEvent e) {
                 if (filename != null) {
                     setPageComplete(true);
                 }
             }
         });

         setControl(composite);
    }
    
    private void loadKeyData() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterExtensions(new String[] {"*.ghpub"});

        filename = dialog.open();

        if (filename == null) {
            setErrorMessage(Messages.GHLoadKeyPage_File_Error);
        } else {
            password.setEnabled(true);
        }
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
            reader.close();
            String[] contents = fileData.toString().split("%");
            own.setText(Messages.GHLoadKeyPage_Owner + ": " + contents[1]);
            desc.setText(Messages.GHLoadKeyPage_Description + ": " + contents[3]);
        } catch (Exception e1) {
        }
    }

    public String getFileName() {
        return filename;
    }

    public String getPassword() {
        return password.getText();
    }
}
