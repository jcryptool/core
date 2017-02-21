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
import org.jcryptool.visual.sigVerification.algorithm.Input;

/**
 * This class contains the three pages of the input wizard.
 * 
 * @author Wilfing
 */
public class InputWizard extends Wizard {
    private InputFileWizardPage page;
    Input input;

    public InputWizard(Input input) {
        super();
        this.input = input;
        setWindowTitle(Messages.InputWizard_Title);
    }

    @Override
    public void addPages() {
        page = new InputFileWizardPage("InputFile Wizard", input); //$NON-NLS-1$
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        if (page.isPageComplete())
            return true;
        return false;
    }
}
