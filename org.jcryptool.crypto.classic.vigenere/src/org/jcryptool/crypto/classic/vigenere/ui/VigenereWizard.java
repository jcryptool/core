// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.vigenere.ui;

import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicTransformationPage;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicWizard;
import org.jcryptool.crypto.classic.vigenere.algorithm.VigenereAlgorithm;

/**
 * The wizard for the Vigenère cipher.
 *
 * @author t-kern
 * @author SLeischnig
 * @version 0.2.1
 */
public class VigenereWizard extends AbstractClassicWizard {
    /** The wizard page for the Vigenère cipher */

	/**
     * Creates a new instance of VigenereWizard.
     */
    public VigenereWizard() {
        super(Messages.VigenereWizard_title);
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages() {
    	page = new VigenereWizardPage();
    	page.setAlgorithmSpecification(VigenereAlgorithm.specification);
		addPage(page);
		page2 = new AbstractClassicTransformationPage();
		addPage(page2);
    }

}
