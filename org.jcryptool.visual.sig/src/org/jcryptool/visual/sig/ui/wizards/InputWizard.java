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

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * This class contains the three pages of the input wizard.
 * 
 * @author Grebe
 */
public class InputWizard extends Wizard {
    private InputWizardPage page;
    private InputEditorWizardPage pageEditor;
    private InputFileWizardPage pageFile;

    public InputWizard() {
        super();
        setWindowTitle(Messages.InputWizard_Title);
    }

    @Override
    public void addPages() {
        page = new InputWizardPage("Input Wizard"); //$NON-NLS-1$
        addPage(page);

        pageEditor = new InputEditorWizardPage("InputEditor Wizard"); //$NON-NLS-1$
        addPage(pageEditor);

        pageFile = new InputFileWizardPage("InputFile Wizard"); //$NON-NLS-1$
        addPage(pageFile);
    }

    @Override
    public boolean performFinish() {
        if (pageEditor.isPageComplete() || pageFile.isPageComplete())
            return true;
        return false;
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        if (page instanceof InputWizardPage) {
            InputWizardPage p = (InputWizardPage) page;
            // Checks which radiobutton is selected (0=Editor, 1=File)
            if (p.getRdoSelection() == 0) {
                pageFile.setPageComplete(true);
                pageEditor.setPageComplete(false);
                return pageEditor;
            } else {
                pageEditor.setPageComplete(true);
                pageFile.setPageComplete(false);
                return pageFile;
            }
        }

        return null;
    }
}
