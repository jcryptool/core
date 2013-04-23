package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class HashWizardPage extends WizardPage {

	private HashComposite composite;

	//Construcor
	protected HashWizardPage(String pageName) {
		//TEST!
		super(Messages.HashWizard_header);
		setDescription(Messages.HashWizard_header);
	}
	
	public void createControl(Composite parent) {
		composite = new HashComposite(parent, NONE);
		setControl(composite);
		setPageComplete(true);
	}
	
	/**
	 * @return the grpHashes
	 */
	public Group getGrpHashes() {
		return composite.getGrpHashes();
	}

}
