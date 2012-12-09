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
