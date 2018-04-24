// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
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
import org.jcryptool.visual.elGamal.ui.wizards.wizardpages.EnterPlaintextforVerification;

public class PlaintextforSignatureVerificationWizard extends Wizard {
	
    /** title of this wizard */
    private static String TITLE = Messages.PlaintextforSignatureVerificationWizard_enter_plaintext;

    /** shared data object */
    private ElGamalData data;
    
    /**
     * Constructor setting title and {@link #data}
     *
     * @param data {@link #data}
     */
    public PlaintextforSignatureVerificationWizard(ElGamalData data) {
        this.data = data;
        this.setWindowTitle(TITLE);
    }
    
    @Override
    public void addPages() {
        addPage(new EnterPlaintextforVerification(data));
    }

	@Override
	public boolean performFinish() {
		data.setPlainText(((EnterPlaintextforVerification) getPage(EnterPlaintextforVerification
				.getPagename())).getPlaintext().trim());
		return true;
	}

}
