//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.keystore.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewEntryDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewKeyWizard;

/**
 * @author t-kern
 *
 */
public class NewSymmetricKeyWizard extends Wizard implements INewKeyWizard {

	private NewSymmetricKeyWizardPage page;

	private INewEntryDescriptor keyDescriptor;

	private String keyType;

	public NewSymmetricKeyWizard() {
		this(KeyStoreAlias.EVERYTHING_MATCHER);
	}

	public NewSymmetricKeyWizard(String keyType) {
		setWindowTitle(Messages.NewSymmetricKeyWizard_0);
		this.keyType = keyType;
		setHelpAvailable(false);
	}

	public void addPages() {
		page = new NewSymmetricKeyWizardPage(keyType);
		addPage(page);
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		keyDescriptor = page.getKeyDescriptor();
		return true;
	}

	public INewEntryDescriptor getNewEntryDescriptor() {
		return keyDescriptor;
	}

	@Override
	public boolean isHelpAvailable() {
		return false;
	}
}
