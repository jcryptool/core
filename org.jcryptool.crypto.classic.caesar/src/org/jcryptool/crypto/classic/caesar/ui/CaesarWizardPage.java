//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.caesar.ui;

import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.classic.caesar.CaesarPlugin;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;

/**
 * The wizard page for the caesar wizard.
 *
 * @author t-kern
 *
 */
public class CaesarWizardPage extends AbstractClassicCryptoPage {


	/**
	 * Creates a new instance of CaesarWizardPage.
	 */
	public CaesarWizardPage() {
		super(Messages.Label_Caesar, Messages.Label_WizardPageMessage);
	}

    @Override
    protected void setHelpAvailable() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
                CaesarPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }

}
