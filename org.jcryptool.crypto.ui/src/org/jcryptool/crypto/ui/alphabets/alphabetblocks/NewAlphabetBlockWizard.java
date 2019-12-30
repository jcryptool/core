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
package org.jcryptool.crypto.ui.alphabets.alphabetblocks;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.ui.alphabets.composite.AtomAlphabet;

public class NewAlphabetBlockWizard extends Wizard {

	private NewAlphabetBlockWizardPage page;
	private String name = "";
	private AtomAlphabet alpha = null;

	public AtomAlphabet getAlpha() {
		return alpha;
	}

	public String getName() {
		return name;
	}

	public NewAlphabetBlockWizard() {
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
		page = new NewAlphabetBlockWizardPage();
		addPage(page);
	}

	public void setName(String name) {
		this.name = name;
		getContainer().updateButtons();
	}
	
	public AtomAlphabet getAlphabetInput() {
		return page.getAlphabetInput().getContent();
	}

	public void setAlphabet(AtomAlphabet content) {
		this.alpha = content;
		getContainer().updateButtons();
	}

	@Override
	public boolean performFinish() {
		return true;
	}
	
	@Override
	public boolean canFinish() {
		return page!=null && page.isPageComplete();
	}

}
