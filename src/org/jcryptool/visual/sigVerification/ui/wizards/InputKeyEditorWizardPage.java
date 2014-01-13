// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
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
 * This class contains the page containing the key editor input composite. It is a part of the Input
 * key wizard.
 * 
 * @author Wilfing
 */
public class InputKeyEditorWizardPage extends WizardPage {
    private InputKeyEditorComposite compositeEditor;
    Input input;
    SigVerification sigVerification;
    InputKeyWizard inputKeyWizard;

    public InputKeyEditorWizardPage(String pageName, InputKeyWizard inputKeyWizard) {
        super(pageName);
        this.input = inputKeyWizard.input;
        this.sigVerification = inputKeyWizard.sigVerification;
        this.inputKeyWizard = inputKeyWizard;
        setTitle(Messages.InputKeyEditorWizard_title);
        setDescription(Messages.InputKeyEditorWizard_header);
    }

    @Override
    public void createControl(Composite parent) {
        compositeEditor = new InputKeyEditorComposite(parent, NONE, this);
        compositeEditor.setFocus();
        setControl(compositeEditor);
        setPageComplete(false);
    }
}