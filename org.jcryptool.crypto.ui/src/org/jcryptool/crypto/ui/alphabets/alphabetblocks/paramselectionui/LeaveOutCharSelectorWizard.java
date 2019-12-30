//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.ui.alphabets.alphabetblocks.paramselectionui;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.ui.alphabets.alphabetblocks.BlockAlphabet;

public class LeaveOutCharSelectorWizard extends Wizard {

	private LeaveOutCharSelectorWizardPage page1;
	private Character selectedChar = null;
	
	public LeaveOutCharSelectorWizard(BlockAlphabet alpha) {
		setWindowTitle("New Wizard");
		page1 = new LeaveOutCharSelectorWizardPage(alpha);
	}

	@Override
	public void addPages() {
		addPage(page1);
	}
	
	public Character getSelectedChar() {
		return selectedChar;
	}

	@Override
	public boolean performCancel() {
		selectedChar = null;
		return super.performCancel();
	}
	
	@Override
	public boolean performFinish() {
		selectedChar = page1.getSelectedChar();
		return true;
	}

}
