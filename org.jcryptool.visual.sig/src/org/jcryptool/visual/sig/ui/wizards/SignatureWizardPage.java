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
package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * This class contains loads the composite for the signature wizard.
 * 
 * @author Grebe
 */
public class SignatureWizardPage extends WizardPage {
    private SignatureComposite composite;
    private int method = 0;

    public SignatureWizardPage(String pageName, int m) {
        super(Messages.SignatureWizard_header);
        setDescription(Messages.SignatureWizard_header);
        setTitle(Messages.SignatureWizard_WindowTitle);
        method = m;
    }

    public void createControl(Composite parent) {
        composite = new SignatureComposite(parent, NONE, method, this);
        setControl(composite);
    }

    /**
     * @return the grpSignatures
     */
    public Group getGrpSignatures() {
        return composite.getgrpSignatures();
    }

    /**
     * @return the KeyStoreAlias
     */
    public KeyStoreAlias getAlias() {
        return composite.getAlias();
    }
}
