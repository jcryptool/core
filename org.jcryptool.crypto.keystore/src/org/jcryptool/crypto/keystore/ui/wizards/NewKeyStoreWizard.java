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
/**
 * 
 */
package org.jcryptool.crypto.keystore.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author tkern
 *
 */
public class NewKeyStoreWizard extends Wizard implements INewKeyStoreWizard {

	private NewKeyStoreWizardPage page;
	
	private INewKeyStoreDescriptor descriptor;
	
	public NewKeyStoreWizard() {
		setWindowTitle(Messages.getString("Label.NewKeyStore")); //$NON-NLS-1$
	}
	
	public void addPages() {
		page = new NewKeyStoreWizardPage();
		addPage(page);
	}
	
	/**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		descriptor = page.getNewKeyStoreDescriptor();
		return true;
	}

	/**
	 * @see org.jcryptool.crypto.keystore.ui.wizards.INewKeyStoreWizard#getNewKeyStoreDescriptor()
	 */
	public INewKeyStoreDescriptor getNewKeyStoreDescriptor() {
		return descriptor;
	}
	
}
