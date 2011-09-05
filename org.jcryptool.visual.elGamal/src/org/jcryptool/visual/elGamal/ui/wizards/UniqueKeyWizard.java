// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.elGamal.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.ChooseBPage;
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.ChooseKPage;

/**
 * Wizard for entering the unique key for any operation that needs it
 *
 * @author Michael Gaber
 */
public class UniqueKeyWizard extends Wizard {

    /** title of this wizard */
    private static final String TITLE = Messages.UniqueKeyWizard_unique_param;

    /** shared data object */
    private final ElGamalData data;

    /**
     * Constructor setting title and {@link #data}
     *
     * @param data {@link #data}
     */
    public UniqueKeyWizard(final ElGamalData data) {
        this.data = data;
        this.setWindowTitle(TITLE);
    }

    @Override
    public void addPages() {
        switch (data.getAction()) {
            case DecryptAction:
            case EncryptAction:
                addPage(new ChooseBPage(data));
                break;
            case SignAction:
            case VerifyAction:
                addPage(new ChooseKPage(data));
                break;
        }
    }

    @Override
    public boolean performFinish() {
        return true;
    }

}
