package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class SignatureWizardPage extends WizardPage {

	private SignatureComposite composite;

	//Construcor
	protected SignatureWizardPage(String pageName) {
		super(Messages.SignatureWizard_header);
		setDescription(Messages.SignatureWizard_header);
	}
	
	public void createControl(Composite parent) {
		composite = new SignatureComposite(parent, NONE);
		setControl(composite);
		setPageComplete(true);
	}
	
	/**
	 * @return the grpSignatures
	 */
	public Group getGrpSignatures() {
		return composite.getgrpSignatures();
	}

}
