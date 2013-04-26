package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class InputWizard extends Wizard {
	
	// The pages of the wizard to select a document or to enter a text
	private InputWizardPage page;
	private InputEditorWizardPage pageEditor;
	private InputFileWizardPage pageFile;

	// Constructor
	public InputWizard() {
		super();
	}

	@Override
	public void addPages() {
		//Create and add pages
		page = new InputWizardPage("Input Wizard");
		addPage(page);
		
		pageEditor = new InputEditorWizardPage("InputEditor Wizard");
		addPage(pageEditor);
		
		pageFile = new InputFileWizardPage("InputFile Wizard");
		addPage(pageFile);
		
	}

	@Override
	public boolean performFinish() {
		if(pageEditor.isPageComplete() || pageFile.isPageComplete())
			return true;
		return false;
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if(page instanceof InputWizardPage) {
			InputWizardPage p = (InputWizardPage) page;
			//Checks which radiobutton is selected (0=Editor, 1=File)
			if (p.getRdoSelection() == 0) {
				pageFile.setPageComplete(true);
				pageEditor.setPageComplete(false);
				return pageEditor;
			}
			else {
				pageEditor.setPageComplete(true);
				pageFile.setPageComplete(false);
				return pageFile;
			}
		}
		
		return null;
	}

}
