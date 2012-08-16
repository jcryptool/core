package org.jcryptool.crypto.classic.alphabets.ui;

import org.eclipse.jface.wizard.Wizard;

public class NewAlphabetBlockWizard extends Wizard {

	private NewAlphabetBlockWizardPage page;

	public NewAlphabetBlockWizard() {
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
		page = new NewAlphabetBlockWizardPage();
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		return false;
	}

}
