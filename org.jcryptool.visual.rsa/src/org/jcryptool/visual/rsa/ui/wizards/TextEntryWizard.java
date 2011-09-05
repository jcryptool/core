// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.rsa.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.rsa.Action;
import org.jcryptool.visual.rsa.Messages;
import org.jcryptool.visual.rsa.RSAData;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.EnterCiphertextPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.EnterPlaintextPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.EnterSignaturePage;

/**
 * wizard for entering any type of text. plaintext, ciphertext or signature.
 * @author Michael Gaber
 */
public class TextEntryWizard extends Wizard {
	/** action, whether it's encrypt, decrypt, verify or sign. */
	private final Action action;

	/** shared data object for exchanging data. */
	private final RSAData data;

	/**
	 * Constructor, setting title, action and data.
	 * @param action the cryptographic action
	 * @param data the data object
	 */
	public TextEntryWizard(Action action, RSAData data) {
		this.action = action;
		this.data = data;
		this.setWindowTitle(Messages.EnterCiphertextPage_textentry);
	}

	@Override
	public final void addPages() {
		switch (action) {
		case EncryptAction:
		case SignAction:
			addPage(new EnterPlaintextPage(action, data));
			break;
		case DecryptAction:
			addPage(new EnterCiphertextPage(data));
			break;
		case VerifyAction:
			addPage(new EnterSignaturePage(data));
		default:
			break;
		}
	}

	@Override
	public final boolean performFinish() {
		switch (action) {
		case EncryptAction:
		case SignAction:
			data.setPlainText(((EnterPlaintextPage) getPage(EnterPlaintextPage.getPagename())).getText());
			break;
		case DecryptAction:
			data.setCipherText(((EnterCiphertextPage) getPage(EnterCiphertextPage.getPagename())).getText().trim());
			break;
		case VerifyAction:
			data.setSignature(((EnterSignaturePage) getPage(EnterSignaturePage.getPagename())).getText().trim());
			data.setPlainText(((EnterSignaturePage) getPage(EnterSignaturePage.getPagename())).getPlaintext().trim());
		default:
			break;
		}
		return true;
	}

}
