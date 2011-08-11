// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.autovigenere.ui;

import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.classic.autovigenere.AutoVigenerePlugin;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;

/**
 * The wizard page for the Autokey-Vigenere wizard.
 *
 * @author SLeischnig
 *
 */
public class AutoVigenereWizardPage extends AbstractClassicCryptoPage {

    /**
     * Creates a new instance of CaesarWizardPage.
     */
    public AutoVigenereWizardPage() {
        super(Messages.AutoVigenereWizardPage_autokeyvigenere,
                Messages.AutoVigenereWizardPage_orders);
    }

    @Override
    protected void setHelpAvailable() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
                AutoVigenerePlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }

}
