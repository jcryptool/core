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
package org.jcryptool.crypto.classic.substitution.ui;

import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;
import org.jcryptool.crypto.classic.substitution.SubstitutionPlugin;

/**
 * The wizard page for the substitution cipher.
 *
 * @author t-kern
 *
 */
public class SubstitutionWizardPage extends AbstractClassicCryptoPage {

	public SubstitutionWizardPage() {
		super(Messages.SubstitutionWizardPage_substitution, Messages.SubstitutionWizardPage_enterkey1);
	}

    @Override
    protected void setHelpAvailable() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
                SubstitutionPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }
}
