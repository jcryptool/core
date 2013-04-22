package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class InputFileWizardPage extends WizardPage {

	private InputFileComposite compositeFile;
	
	protected InputFileWizardPage(String pageName) {
		super("InputFile Wizard");
		// TODO Auto-generated constructor stub
		
		setTitle(Messages.InputFileWizard_title);
		setDescription(Messages.InputFileWizard_header);
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		
		compositeFile = new InputFileComposite(parent, NONE);
		//composite.setBounds(x, y, width, height);
		setControl(compositeFile);
		setPageComplete(true);
	}

}
