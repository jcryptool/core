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
import org.jcryptool.visual.dsa.DSAData;
import org.jcryptool.visual.dsa.ui.wizards.wizardpages.ChooseKPage;

public class UniqueKeyWizard extends Wizard {

	private static final String TITLE = "Enter unique parameter.";
	private final DSAData data;

	public UniqueKeyWizard(final DSAData data) {
		this.data = data;
		this.setWindowTitle(TITLE);
	}

	@Override
	public void addPages() {
		addPage(new ChooseKPage(data));
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
