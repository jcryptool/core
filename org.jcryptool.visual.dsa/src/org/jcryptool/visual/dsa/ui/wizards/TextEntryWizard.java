// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.dsa.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.dsa.Action;
import org.jcryptool.visual.dsa.DSAData;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.EnterPlaintextPage;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.EnterSignaturePage;

/**
 * wizard for entering any type of text. plaintext, ciphertext or signature.
 * @author Michael Gaber
 */
public class TextEntryWizard extends Wizard {

	/** wizard title, displayed in the titlebar. */
	private static final String TITLE = "Texteingabe";

	/** action, whether it's encrypt, decrypt, verify or sign. */
	private final Action action;

	/** shared data object for exchanging data. */
	private final DSAData data;

	/**
	 * Constructor, setting title, action and data.
	 * @param action the cryptographic action
	 * @param data the data object
	 */
	public TextEntryWizard(final Action action, final DSAData data) {
		this.action = action;
		this.data = data;
		this.setWindowTitle(TITLE);
	}

	@Override
	public final void addPages() {
		switch (action) {
		case SignAction:
			addPage(new EnterPlaintextPage(action, data));
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
		case SignAction:
			data.setPlainText(((EnterPlaintextPage) getPage(EnterPlaintextPage.getPagename())).getText());
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
