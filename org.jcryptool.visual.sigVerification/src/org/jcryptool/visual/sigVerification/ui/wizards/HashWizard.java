// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014, 2020 JCrypTool Team and Contributors
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
import org.jcryptool.visual.sigVerification.algorithm.Input;

/**
 * Adds the page of the hash wizard.
 * 
 * @author Wilfing
 */
public class HashWizard extends Wizard {
    // The only page of the wizard (for selecting the Hash method)
    private HashWizardPage page;
    private String name;
    // Integer representing the chosen hash (0-4)
    private int hash;
    private Input input;

    public HashWizard(Input input) {
        super();
        this.input = input;
        name = "HashWizard"; //$NON-NLS-1$
        setWindowTitle(Messages.HashWizard_Title);
    }

    @Override
    public void addPages() {
    	page = new HashWizardPage(name, input);
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        // get all the radiobuttons from the WizardPage
        Control[] radioButtons = page.getGrpHashes().getChildren();
        
        // Check which radiobutton is selected
        for (int i = 0; i < radioButtons.length; i++) {
            if (((Button) radioButtons[i]).getSelection()) {
                hash = i;
                break;
            }
        }
        
        return true;
    }

    /**
     * @return the hash
     */
    public int getHash() {
        return hash;
    }
}
