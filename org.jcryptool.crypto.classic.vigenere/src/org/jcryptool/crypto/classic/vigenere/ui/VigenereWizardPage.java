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

import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;
import org.jcryptool.crypto.classic.vigenere.VigenerePlugin;

/**
 * The wizard page for the Vigenère cipher.
 *
 * @author t-kern
 * @author SLeischnig
 */
public class VigenereWizardPage extends AbstractClassicCryptoPage{

    public VigenereWizardPage() {
        super(Messages.VigenereWizardPage_title, Messages.VigenereWizardPage_description);
    }


    @Override
    protected void setHelpAvailable() {
    	PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), VigenerePlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }


}
