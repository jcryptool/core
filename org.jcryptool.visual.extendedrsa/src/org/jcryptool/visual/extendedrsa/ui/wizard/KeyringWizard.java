package org.jcryptool.visual.extendedrsa.ui.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.extendedrsa.ui.wizard.wizardpages.KeyringPage1;

public class KeyringWizard extends Wizard{
	
	@Override
	public final void addPages() {
		addPage(new KeyringPage1());
	}
	
	@Override
	public boolean performFinish() {
		this.dispose();
		return false;
	}

}
