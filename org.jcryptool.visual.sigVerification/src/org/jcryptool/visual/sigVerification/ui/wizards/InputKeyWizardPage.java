// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.visual.sigVerification.algorithm.Hash;
import org.jcryptool.visual.sigVerification.algorithm.Input;
import org.jcryptool.visual.sigVerification.algorithm.SigVerification;

/**
 * This class contains loads the composite for the first page of the input key wizard.
 * 
 * @author Wilfing
 */
public class InputKeyWizardPage extends WizardPage {
    private InputKeyComposite composite;
    public boolean enableNext = false;
    SigVerification sigVerifiaction;
    Hash hash;
    Input input;
    InputKeyWizard inputKeyWizard;

    public InputKeyWizardPage(String pageName, InputKeyWizard inputKeyWizard) {
        super(pageName);
        this.sigVerifiaction = inputKeyWizard.sigVerification;
        this.hash = inputKeyWizard.hash;
        this.input = inputKeyWizard.input;
        this.inputKeyWizard = inputKeyWizard;
        this.sigVerifiaction.reset();
        setTitle(Messages.InputKeyWizard_title);
        setDescription(Messages.InputKeyWizard_header);
    }

    public void createControl(Composite parent) {
        composite = new InputKeyComposite(parent, NONE, this);
        composite.getRdoFromFile().setBounds(10, 10, 430, 18);
        composite.getRdoFromEditor().setBounds(10, 34, 430, 18);
        composite.getRdoFromKeyStore().setBounds(10, 58, 430, 18);

        setControl(composite);

    }

    public int getRdoSelection() {
        if (composite.getRdoFromEditor().getSelection()) {
            return 0;
        } else if (composite.getRdoFromFile().getSelection()) {
            return 1;
        } else {
            return 2;
        }
    }

    public boolean canFlipToNextPage() {
        if (composite.getRdoFromEditor().getSelection()) {
            enableNext = true;
        } else if (composite.getRdoFromFile().getSelection()) {
            enableNext = true;
        } else {
            enableNext = false;
        }
        return enableNext;
    }
}
