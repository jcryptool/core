//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

/**
 * Adds the page of the hash wizard.
 * 
 * @author Grebe
 */
public class HashWizard extends Wizard {
    // The only page of the wizard (for selecting the Hash method)
    private HashWizardPage page;
    private String name;
    // Integer representing the chosen hash (0-4)
    private int hash;

    public HashWizard() {
        super();
        name = "HashWizard"; //$NON-NLS-1$
        setWindowTitle(Messages.HashWizard_Title);
    }

    @Override
    public void addPages() {
        page = new HashWizardPage(name);
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        int i = 0; // 0-4
        // get all the radiobuttons from the WizardPage
        Control[] radiobutton = page.getGrpHashes().getChildren();
        // Check which radiobutton is selected
        while (i <= 4) {
            // Check if the current button is selected
            if (((Button) radiobutton[i]).getSelection()) {
                hash = i;
                i = 5; // leave the loop
            }
            i++;
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
