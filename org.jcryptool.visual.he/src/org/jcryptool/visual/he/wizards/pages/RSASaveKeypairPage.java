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

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.rsa.RSAData;

/**
 * page for saving a keypair.
 *
 * @author Michael Gaber
 */
public class RSASaveKeypairPage extends RSASaveWizardPage {
    /** unique pagename to get this page from inside a wizard. */
    private static final String PAGENAME = "Save Keypair Page"; //$NON-NLS-1$

    /** title of this page, displayed in the head of the wizard. */
    private static final String TITLE = Messages.RSASaveKeypairPage_save_keypair_text;

    /** field for the password. */
    private Text password;

    /** password verification field. */
    private Text passwordverify;

    /** modifyListener for the fields. */
    private final ModifyListener ml = new ModifyListener() {

        public void modifyText(final ModifyEvent e) {
            final boolean pwmatch = password.getText().equals(passwordverify.getText());
            setPageComplete(!owner.getText().equals("") && !password.getText().equals("") //$NON-NLS-1$ //$NON-NLS-2$
                    && pwmatch);
            if (pwmatch) {
                data.setContactName(owner.getText());
                data.setPassword(password.getText());
                setErrorMessage(null);
            } else {
                setErrorMessage(Messages.RSASaveKeypairPage_error_passwords_mismatch);
                data.setPassword(null);
                data.setContactName(null);
            }
        }
    };

    /** shared data object. */
    private final RSAData data;

    /**
     * Constructor, sets page incomplete and calls super and sets the description.
     *
     * @param data the data object
     */
    public RSASaveKeypairPage(final RSAData data) {
        super(PAGENAME, TITLE, null);
        setPageComplete(false);
        this.setDescription(Messages.RSASaveKeypairPage_enter_params_text);
        this.data = data;
    }

    /**
     * Set up UI stuff.
     *
     * @param parent the parent composite
     */
    public final void createControl(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());

        final Label own = new Label(composite, SWT.NONE);
        own.setText(Messages.RSASaveKeypairPage_name);
        GridData gd_own = new GridData(SWT.FILL, SWT.CENTER, false, false);
        own.setLayoutData(gd_own);

        owner = new Text(composite, SWT.BORDER | SWT.SINGLE);
        owner.addModifyListener(ml);
        GridData gd_owner = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd_owner.widthHint = 300;
        owner.setLayoutData(gd_owner);

        final Label pass = new Label(composite, SWT.NONE);
        pass.setText(Messages.RSASaveKeypairPage_password);
        GridData gd_pass = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd_pass.verticalIndent = 20;
        pass.setLayoutData(gd_pass);

        password = new Text(composite, SWT.BORDER | SWT.PASSWORD);
        password.addModifyListener(ml);
        GridData gd_password = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd_password.widthHint = 300;
        password.setLayoutData(gd_password);
        
        passwordverify = new Text(composite, SWT.BORDER | SWT.PASSWORD);
        passwordverify.addModifyListener(ml);
        GridData gd_passwordverify = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd_passwordverify.widthHint = 300;
        passwordverify.setLayoutData(gd_passwordverify);    

        owner.forceFocus();

        // finish
        setControl(composite);
    }

    /**
     * getter for the pagename.
     *
     * @return the pagename
     */
    public static String getPagename() {
        return PAGENAME;
    }

    @Override
    public IWizardPage getNextPage() {
        return null;
    }
}
