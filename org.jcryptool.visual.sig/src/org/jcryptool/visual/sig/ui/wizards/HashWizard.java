package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

public class HashWizard extends Wizard{
	HashWizardPage page;
	private String name;

	//Constructor
	public HashWizard() {
		super();
		name = "test";
	}	
	
	@Override
	public void addPages() {
		page = new HashWizardPage(name);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return true;
	}
}
