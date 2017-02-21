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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.visual.sigVerification.algorithm.Input;
import org.jcryptool.visual.sigVerification.algorithm.SigVerification;

/**
 * This class contains the page containing the key file input composite. It is a part of the Input
 * key wizard.
 * 
 * @author Wilfing
 */
public class InputKeyFileWizardPage extends WizardPage {
    private InputKeyFileComposite compositeFile;
    Input input;
    SigVerification sigVerification;
    InputKeyWizard inputKeyWizard;

    public InputKeyFileWizardPage(String pageName, InputKeyWizard inputKeyWizard) {
        super(pageName);
        this.input = inputKeyWizard.input;
        this.sigVerification = inputKeyWizard.sigVerification;
        this.inputKeyWizard = inputKeyWizard;

        setTitle(Messages.InputKeyFileWizard_title);
        setDescription(Messages.InputKeyFileWizard_header);
    }

    public void createControl(Composite parent) {
        setPageComplete(false);
        compositeFile = new InputKeyFileComposite(parent, NONE, this);
        setControl(compositeFile);
    }

    /**
     * @return the compositeFile
     */
    public InputKeyFileComposite getCompositeFile() {
        return compositeFile;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.WizardPage#getPreviousPage()
     */
    public WizardPage getPreviousPage(){
        inputKeyWizard.enableFinish = false;
        sigVerification.reset();
        return inputKeyWizard.page;        
    }
}
