package org.jcryptool.visual.he.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.RSAData;

/**
 * Wizard to enter an operation number for Paillier homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class RSAOperationTextWizard extends Wizard {
	/** wizard title, displayed in the titlebar. */
	private static final String TITLE = Messages.OperationTextWizard_Title;
	
	/** Will hold data */
	private RSAData data;
	
	public RSAOperationTextWizard(RSAData data) {
		this.data = data;
		this.setWindowTitle(TITLE);
	}
	
	@Override
	public void addPages() {
		addPage(new RSAChooseOperationTextPage(data));
	}
	
	@Override
	public boolean canFinish() {
		return getPage(RSAChooseOperationTextPage.getPagename()).isPageComplete();
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}
}