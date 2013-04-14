package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

public class SignatureWizard extends Wizard{
	//The only page of the wizard (for selecting the Hash method)
	SignatureWizardPage page;
	private String name;

	//Constructor
	public SignatureWizard() {
		super();
		name = "test";
	}	
	
	@Override
	public void addPages() {
		//Create page
		page = new SignatureWizardPage(name);
		//Add the page to the wizard
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		//Controls what happens after clicking "Finish"
		return true;
	}
}
