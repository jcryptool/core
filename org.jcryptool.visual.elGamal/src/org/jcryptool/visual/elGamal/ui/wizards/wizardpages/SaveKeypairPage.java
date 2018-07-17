// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.elGamal.ui.wizards.wizardpages;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;

/**
 * page for saving a keypair.
 *
 * @author Michael Gaber
 * @author Thorben Groos
 */
public class SaveKeypairPage extends SaveWizardPage {

    /** field for the password. */
    private Text password;

    /** password verification field. */
    private Text passwordverify;

    /** modifyListener for the fields. */
    private final ModifyListener ml = new ModifyListener() {

        @Override
		public void modifyText(final ModifyEvent e) {
            boolean pwmatch = password.getText().equals(passwordverify.getText());
            setPageComplete(!owner.getText().isEmpty() && !password.getText().isEmpty() && pwmatch);
            
            if (pwmatch) {
                setErrorMessage(null);
            } else {
                setErrorMessage(Messages.SaveKeypairPage_error_passwords_dont_match);
            }
        }
    };

    /**
     * Constructor, sets page incomplete and calls super and sets the description.
     *
     * @param data the data object
     */
    public SaveKeypairPage(final ElGamalData data) {
        super("Save Keypair Page", Messages.SaveKeypairPage_save_keypair, null);
        setPageComplete(false);
        setDescription(Messages.SaveKeypairPage_enter_save_params);
    }

    /**
     * Set up UI stuff.
     *
     * @param parent the parent composite
     */
    @Override
	public final void createControl(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        // do stuff like layout et al
        GridLayout gl_composite = new GridLayout();
        gl_composite.marginWidth = 50;
        composite.setLayout(gl_composite);
        final Label own = new Label(composite, SWT.NONE);
        own.setText(Messages.SaveKeypairPage_name);
        own.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        owner = new Text(composite, SWT.BORDER | SWT.SINGLE);
        owner.addModifyListener(ml);
        owner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        final Label pass = new Label(composite, SWT.NONE);
        pass.setText(Messages.SaveKeypairPage_password);
        pass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        password = new Text(composite, SWT.BORDER | SWT.PASSWORD);
        password.addModifyListener(ml);
        password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        passwordverify = new Text(composite, SWT.BORDER | SWT.PASSWORD);
        passwordverify.addModifyListener(ml);
        passwordverify.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // finish
        setControl(composite);
    }

    @Override
    public IWizardPage getNextPage() {
        return null;
    }

	@Override
	public String getOwner() {
		return owner.getText();
	}
	
	/**
	 * Getter for the entered password
	 * @return The password which the user has set
	 */
	public String getPassword() {
		return password.getText();
	}
}
