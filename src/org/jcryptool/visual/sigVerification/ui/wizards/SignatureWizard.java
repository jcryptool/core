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

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.jcryptool.visual.sigVerification.algorithm.Input;

/**
 * This class adds the page for the signature wizard.
 * 
 * @author Wilfing
 */
public class SignatureWizard extends Wizard {
    private SignatureWizardPage page;
    private int signature;
    private int method = 0;
    Input input;

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
        int i = 0; // 0-4
        // get all the radiobuttons from the WizardPage
        Control[] radiobutton = (Control[]) page.getGrpSignatures().getChildren();
        // Check which radiobutton is selected
        while (i <= 4) {
            // Check if the current button is selected
            if (((Button) radiobutton[i]).getSelection()) {
                signature = i;
                i = 5; // leave the loop
            }
            i++;
        }
        return true;
    }

    /**
     * @return the signature
     */
    public int getSignature() {
        return signature;
    }
}
