package org.jcryptool.visual.he.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.GHData;
import org.jcryptool.visual.he.wizards.pages.GHChooseModulusPage;

/**
 * Wizard to enter an initial number for Gentry & Halevi fully homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class GHModulusWizard extends Wizard {
	/** wizard title, displayed in the titlebar. */
	private static final String TITLE = Messages.InitialTextWizard_Title;
	
	/** Will hold data */
	private GHData data;
	
	public GHModulusWizard(GHData data) {
		this.data = data;
		this.setWindowTitle(TITLE);
		this.setHelpAvailable(false);
	}
	
	@Override
	public void addPages() {
		addPage(new GHChooseModulusPage(data));
	}
	
	@Override
	public boolean canFinish() {
		return getPage(GHChooseModulusPage.getPagename()).isPageComplete();
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}
}
