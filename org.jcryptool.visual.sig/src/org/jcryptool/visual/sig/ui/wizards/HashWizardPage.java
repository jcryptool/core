package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class HashWizardPage extends WizardPage {

	private HashComposite composite;

	//Construcor
	protected HashWizardPage(String pageName) {
		//TEST!
		super("Hashwizard");
		setDescription("Select a hash method");
	}
	
	public void createControl(Composite parent) {
		composite = new HashComposite(parent, NONE);
		//composite.setBounds(x, y, width, height);
		setControl(composite);
		setPageComplete(true);
	}

}
