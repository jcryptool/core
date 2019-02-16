// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.rsa.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.visual.rsa.Action;
import org.jcryptool.visual.rsa.Messages;
import org.jcryptool.visual.rsa.RSAData;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.EnterCiphertextPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.EnterPlaintextPage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.EnterSignaturePage;
import org.jcryptool.visual.rsa.ui.wizards.wizardpages.RsaTextModifyPage;
/**
 * wizard for entering any type of text. plaintext, ciphertext or signature.
 * @author Michael Gaber
 */
public class TextEntryWizard extends Wizard {
	/** action, whether it's encrypt, decrypt, verify or sign. */
	private final Action action;

	/** shared data object for exchanging data. */
	private final RSAData data;

	private boolean finished = false;
	private TransformData transformData;
	private EnterPlaintextPage textPage;
	private RsaTextModifyPage modifyPage;

	/**
	 * Constructor, setting title, action and data.
	 * @param action the cryptographic action
	 * @param data the data object
	 */
	public TextEntryWizard(Action action, RSAData data) {
		this.action = action;
		this.data = data;
		this.setWindowTitle(Messages.EnterCiphertextPage_textentry);
		modifyPage = new RsaTextModifyPage();
		textPage = new EnterPlaintextPage(action, data);
	}

	public TransformData getTransformData() {
		if (!finished) return modifyPage.getSelectedData();
		else return transformData;
	}
	
	public void setTransformData(TransformData transformData) {
		modifyPage.setSelectedData(transformData);
	}
	
	@Override
	public final void addPages() {
		switch (action) {
		case EncryptAction:
		case SignAction:
			addPage(textPage);
			addPage(modifyPage);
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
			this.transformData = getTransformData();
			finished = true;
			// Sets plain text using the inserted text and the transformData
			data.setPlainText_Old(Transform.transformText(((EnterPlaintextPage) getPage(EnterPlaintextPage.getPagename())).getText(), transformData));
			break;
		case DecryptAction:
			data.setCipherText_Old(((EnterCiphertextPage) getPage(EnterCiphertextPage.getPagename())).getText().trim());
			break;
		case VerifyAction:
			data.setSignature_Old(((EnterSignaturePage) getPage(EnterSignaturePage.getPagename())).getText().trim());
			data.setPlainText_Old(((EnterSignaturePage) getPage(EnterSignaturePage.getPagename())).getPlaintext().trim());
		default:
			break;
		}
		return true;
	}

}
