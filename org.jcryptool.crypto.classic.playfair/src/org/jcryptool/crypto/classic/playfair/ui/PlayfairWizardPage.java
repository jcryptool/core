// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.playfair.ui;

import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;
import org.jcryptool.crypto.classic.playfair.PlayfairPlugin;

/**
 * The wizard page for the Playfair wizard.
 *
 * @author SLeischnig
 *
 */
public class PlayfairWizardPage extends AbstractClassicCryptoPage {

    /**
     * Creates a new instance of CaesarWizardPage.
     */
    public PlayfairWizardPage() {
        super(Messages.PlayfairWizardPage_playfair, Messages.PlayfairWizardPage_enterkey1); //$NON-NLS-1$
    }

    @Override
    protected void setHelpAvailable() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), PlayfairPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }

}
