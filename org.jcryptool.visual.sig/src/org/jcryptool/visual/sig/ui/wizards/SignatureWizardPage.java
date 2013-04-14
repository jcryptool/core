package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class SignatureWizardPage extends WizardPage {

	private SignatureComposite composite;

	//Construcor
	protected SignatureWizardPage(String pageName) {
		//TEST!
		super("Siganture Wizard");
		setDescription("YEESSSSS");
	}
	
	public void createControl(Composite parent) {
		composite = new SignatureComposite(parent, NONE);
		//composite.setBounds(x, y, width, height);
		setControl(composite);
		setPageComplete(true);
	}

}
