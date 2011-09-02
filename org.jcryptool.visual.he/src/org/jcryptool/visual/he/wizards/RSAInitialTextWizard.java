package org.jcryptool.visual.he.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.RSAData;

/**
 * Wizard to enter an initial number for RSA homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class RSAInitialTextWizard extends Wizard {
	/** wizard title, displayed in the titlebar. */
	private static final String TITLE = Messages.InitialTextWizard_Title;
	
	/** Will hold data */
	private RSAData data;
	
	public RSAInitialTextWizard(RSAData data) {
		this.data = data;
		this.setWindowTitle(TITLE);
	}
	
	@Override
	public void addPages() {
		addPage(new RSAChooseInitialTextPage(data));
	}
	
	@Override
	public boolean canFinish() {
		return getPage(RSAChooseInitialTextPage.getPagename()).isPageComplete();
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}
}
