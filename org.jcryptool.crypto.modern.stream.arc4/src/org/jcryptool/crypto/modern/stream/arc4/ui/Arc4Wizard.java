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
package org.jcryptool.crypto.modern.stream.arc4.ui;

import org.eclipse.jface.wizard.Wizard;

/**
 * 
 * from here we interact with the ui "background"
 * 
 * @author David
 *
 */
public class Arc4Wizard extends Wizard {

    private Arc4WizardPage page;

    public Arc4Wizard() {
        setWindowTitle(Messages.Arc4_name);
    }

    public void addPages() {
        page = new Arc4WizardPage();
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        return true;
    }

    /**
     * Returns the value input for the key as a String.
     *
     * @return the key of the cipher
     */
    public String getKey() {
        return page.getKeyValue();
    }

    /**
     * The format selected for the inputting of the key (hexadecimal or binary).
     *
     * @return the format of the key
     */
    public boolean getKeyFormatIsHexadecimal() {
        return page.getKeyFormatIsHexadecimal();
    }

    /**
     * Spritz or Arc4
     * 
     * @return the algo spritz or arc4 true for arc4
     */
    public boolean getAlgoIsArc4() {
        return page.getAlgo();
    }

    /**
     * w for Spritz
     * 
     * @return the free w
     */
    public int getFreeW() {
        return page.freeW();
    }

}
