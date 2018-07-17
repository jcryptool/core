// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.visual.sigVerification.algorithm.Input;

/**
 * This class adds the page for the signature wizard.
 * 
 * @author Wilfing
 */
public class SignatureWizard extends Wizard {
    private SignatureWizardPage page;
    private int signature;
    private int method;
    private Input input;
    private KeyStoreAlias alias;

    public SignatureWizard(int m, Input input) {
        super();
        this.input = input;
        method = m;
        setWindowTitle(Messages.SignatureWizard_Title);
    }

    @Override
    public void addPages() {
    	page = new SignatureWizardPage("SignatureWizard", method, input); //$NON-NLS-1$
        addPage(page); 
    }

    @Override
    public boolean performFinish() {
        // get all the radiobuttons from the WizardPage
        Control[] radioButtons = (Control[]) page.getGrpSignatures().getChildren();
        
        // Check which radiobutton is selected
        for (int i = 0; i < radioButtons.length; i++) {
            if (((Button) radioButtons[i]).getSelection()) {
                signature = i; //set this.signature to selected radioButton
                break; // leave the loop
            }
        }
        
        // Get the Alias
        alias = page.getAlias();

        return true;
    }

    /**
     * @return the selected signature
     */
    public int getSignature() {
        return signature;
    }
    
    /**
     * @return the selected KeyStoreAlias (containing the public key for the verification)
     */
    public KeyStoreAlias getAlias() {
    	return alias;
    }
}
