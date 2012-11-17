package org.jcryptool.crypto.classic.alphabets.ui.abc;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;

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
