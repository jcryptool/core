//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.delastelle.ui;

import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.classic.delastelle.DelastellePlugin;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;

/**
 * The wizard page for the Delastelle wizard.
 *
 * @author SLeischnig
 *
 */
public class DelastelleWizardPage extends AbstractClassicCryptoPage {

	
	/**
	 * Creates a new instance of DelastelleWizardPage.
	 */
	public DelastelleWizardPage() {
		super(Messages.DelastelleWizardPage_delastelle, Messages.DelastelleWizardPage_enterkey); //$NON-NLS-1$
	}
	
	
	
	
	@Override
	protected void setHelpAvailable() {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), DelastellePlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
	}
	
}
