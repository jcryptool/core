// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.RSAData;

/**
 * Wizard to enter an initial number for RSA homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class RSAInitialTextWizard extends Wizard {
	/** wizard title, displayed in the titlebar. */
	private static final String TITLE = Messages.InitialTextWizard_Title;

	/** Will hold data */
	private RSAData data;

	public RSAInitialTextWizard(RSAData data) {
		this.data = data;
		this.setWindowTitle(TITLE);
	}

	@Override
	public void addPages() {
		addPage(new RSAChooseInitialTextPage(data));
	}

	@Override
	public boolean canFinish() {
		return getPage(RSAChooseInitialTextPage.getPagename()).isPageComplete();
	}

	@Override
	public boolean performFinish() {
		return true;
	}
}
