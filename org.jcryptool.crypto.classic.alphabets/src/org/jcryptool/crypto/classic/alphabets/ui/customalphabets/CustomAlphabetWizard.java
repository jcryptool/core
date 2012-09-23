package org.jcryptool.crypto.classic.alphabets.ui.customalphabets;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.classic.alphabets.ui.AddAlphabetWizardPage2;

public class CustomAlphabetWizard extends Wizard {
	
	public static final String USE_HISTORY_ALPHABET = "use history";
	public static final String MAKE_NEW_ALPHABET = "make custom alphabet";
	
	CreateCustomAlphabetIntroPage page1;
	AddAlphabetWizardPage2 page2;
	
	private String alphaSelectMode = MAKE_NEW_ALPHABET;
	
	public CustomAlphabetWizard() {
		setWindowTitle("Create custom alphabets");
		page1 = new CreateCustomAlphabetIntroPage();
		page2 = new AddAlphabetWizardPage2();
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if(getAlphaSelectMode() == USE_HISTORY_ALPHABET && page == page1) 
			return null;
		
		return super.getNextPage(page);
	}
	
	
	@Override
	public void addPages() {
		addPage(page1);
		addPage(page2);
	}

	@Override
	public boolean performFinish() {
		return false;
	}

	public String getAlphaSelectMode() {
		return alphaSelectMode;
	}

	public void setAlphaSelectMode(String alphaSelectMode) {
		this.alphaSelectMode = alphaSelectMode;
	}
	
}
