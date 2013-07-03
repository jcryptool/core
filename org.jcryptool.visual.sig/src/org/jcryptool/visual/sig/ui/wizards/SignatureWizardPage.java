package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * 
 * @author Grebe
 * This class contains loads the composite for the signature wizard.
 *
 */
public class SignatureWizardPage extends WizardPage {

	private SignatureComposite composite;
	private int method = 0;

	//Construcor
	protected SignatureWizardPage(String pageName, int m) {
		//Header is the fat text above the description
		super(Messages.SignatureWizard_header);
		setDescription(Messages.SignatureWizard_header);
		setTitle(Messages.SignatureWizard_WindowTitle);
		method = m;
	}
	
	public void createControl(Composite parent) {
		composite = new SignatureComposite(parent, NONE, method, this);
		setControl(composite);
		setPageComplete(false);
	}
	
	/**
	 * @return the grpSignatures
	 */
	public Group getGrpSignatures() {
		return composite.getgrpSignatures();
	}
	
	/**
	 * @return the KeyStoreAlias
	 */
	public KeyStoreAlias getAlias() {
		return composite.getAlias();
	}

}
