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

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.sigVerification.algorithm.Hash;
import org.jcryptool.visual.sigVerification.algorithm.Input;
import org.jcryptool.visual.sigVerification.algorithm.SigVerification;
import org.jcryptool.visual.sigVerification.ui.wizards.Messages;

/**
 * This class contains the three pages of the input wizard.
 * 
 * @author Wilfing
 */
public class InputKeyWizard extends Wizard {
    private InputKeyWizardPage page;
    private InputKeyEditorWizardPage pageEditor;
    private InputKeyFileWizardPage pageFile;
    Input input;
    SigVerification sigVerification;
    Hash hash;
    
    public InputKeyWizard(Input input, SigVerification sigVerification, Hash hash) {
        super();
        this.input = input;
        this.sigVerification = sigVerification;
        this.hash = hash;
        setWindowTitle(Messages.InputKeyWizard_title);
    }

    @Override
    public void addPages() {
        page = new InputKeyWizardPage("InputKey Wizard"); //$NON-NLS-1$
        addPage(page);

        pageEditor = new InputKeyEditorWizardPage("InputKeyEditor Wizard", input, sigVerification); //$NON-NLS-1$
        addPage(pageEditor);

        pageFile = new InputKeyFileWizardPage("InputKeyFile Wizard", input, sigVerification); //$NON-NLS-1$
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
        if (page instanceof InputKeyWizardPage) {
            InputKeyWizardPage p = (InputKeyWizardPage) page;
            // Checks which radiobutton is selected (0=Editor, 1=File)
            if (p.getRdoSelection() == 0) {
                pageFile.setPageComplete(true);
                pageEditor.setPageComplete(false);
                return pageEditor;
            } else if (p.getRdoSelection() == 1){
                pageEditor.setPageComplete(true);
                pageFile.setPageComplete(false);
                return pageFile;
            } else {            	
                pageEditor.setPageComplete(true);
                pageFile.setPageComplete(true);
                p.setPageComplete(true);
                sigVerification.verifySignature(input, hash);
                return p;
            }
        }

        return null;
    }
}
