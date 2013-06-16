package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * 
 * @author Grebe
 * This class contains the page containing the hash wizard composite. It is a part of the hash wizard.
 *
 */
public class HashWizardPage extends WizardPage {

	private HashComposite composite;

	//Construcor
	protected HashWizardPage(String pageName) {
		//Header is the fat text
		super(Messages.HashWizard_header);
		setDescription(Messages.HashWizard_header);
		setTitle(Messages.HashWizard_WindowTitle);
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
