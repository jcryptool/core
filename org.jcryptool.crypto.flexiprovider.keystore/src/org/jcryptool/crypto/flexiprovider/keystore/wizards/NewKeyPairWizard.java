//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
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
public class NewKeyPairWizard extends Wizard implements INewKeyWizard {

	private NewKeyPairWizardPage page;

	private INewEntryDescriptor keyDescriptor;

	private String keyType;

	public NewKeyPairWizard() {
		this(KeyStoreAlias.EVERYTHING_MATCHER);
	}

	public NewKeyPairWizard(String keyType) {
		this.keyType = keyType;
		setWindowTitle(Messages.NewKeyPairWizard_0);
		setHelpAvailable(false);
	}

	public void addPages() {
		page = new NewKeyPairWizardPage(keyType);
		addPage(page);
	}

	/* (non-Javadoc)
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
