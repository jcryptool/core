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
import org.jcryptool.visual.he.algo.GHData;

/**
 * Wizard to enter an initial number for Gentry & Halevi fully homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class GHInitialTextWizard extends Wizard {
	/** wizard title, displayed in the titlebar. */
	private static final String TITLE = Messages.InitialTextWizard_Title;

	/** 2-log of modulus */
	private int logMod;

	/** Will hold data */
	private GHData data;

	public GHInitialTextWizard(int logMod, GHData data) {
		this.logMod = logMod;
		this.data = data;
		this.setWindowTitle(TITLE);
		this.setHelpAvailable(false);
	}

	@Override
	public void addPages() {
		addPage(new GHChooseInitialTextPage(logMod, data));
	}

	@Override
	public boolean canFinish() {
		return getPage(GHChooseInitialTextPage.getPagename()).isPageComplete();
	}

	@Override
	public boolean performFinish() {
		return true;
	}
}
