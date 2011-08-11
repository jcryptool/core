package org.jcryptool.visual.he.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.PaillierData;

/**
 * Wizard to enter an operation number for Paillier homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class PaillierOperationTextWizard extends Wizard {
	/** wizard title, displayed in the titlebar. */
	private static final String TITLE = Messages.OperationTextWizard_Title;
	
	/** Will hold data */
	private PaillierData data;
	
	public PaillierOperationTextWizard(PaillierData data) {
		this.data = data;
		this.setWindowTitle(TITLE);
	}
	
	@Override
	public void addPages() {
		addPage(new PaillierChooseOperationTextPage(data));
	}
	
	@Override
	public boolean canFinish() {
		return getPage(PaillierChooseOperationTextPage.getPagename()).isPageComplete();
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}
}