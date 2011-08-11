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
package org.jcryptool.crypto.keystore.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author tkern
 *
 */
public class ChooseKeyStoreWizard extends Wizard implements IChooseKeyStoreWizard {

	private ChooseKeyStoreWizardPage page;
	
	private String selectedKeyStore;
	
	public ChooseKeyStoreWizard() {
		setWindowTitle(Messages.getString("Label.SelectKeyStore")); //$NON-NLS-1$
	}
	
	public void addPages() {
		page = new ChooseKeyStoreWizardPage();
		addPage(page);
	}
	
	/**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		selectedKeyStore = page.getSelectedKeyStore();
		return true;
	}
	
	public String getSelectedKeyStore() {
		return selectedKeyStore;
	}

}
