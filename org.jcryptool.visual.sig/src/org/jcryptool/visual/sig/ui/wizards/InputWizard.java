package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

public class InputWizard extends Wizard{
	//The only page of the wizard (for selecting the Hash method)
	InputWizardPage page;
	InputFileWizardPage pageFile;
	InputEditorWizardPage pageEditor;
	
	//Constructor
	public InputWizard() {
		super();
	}	
	
	@Override
	public void addPages() {
		//Create pages
		page = new InputWizardPage("Input Wizard");
		pageFile = new InputFileWizardPage("InputFile Wizard");
		pageEditor = new InputEditorWizardPage("InputEditor Wizard");
		
		//Add the pages to the wizard
		addPage(page);
		addPage(pageFile);
		addPage(pageEditor);

	}

	@Override
	public boolean performFinish() {
		//Controls what happens after clicking "Finish"
		return true;
	}
}
