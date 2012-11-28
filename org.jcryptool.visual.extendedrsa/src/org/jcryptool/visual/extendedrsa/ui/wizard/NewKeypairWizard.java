package org.jcryptool.visual.extendedrsa.ui.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.extendedrsa.ui.wizard.wizardpages.NewKeypairPage1;

public class NewKeypairWizard extends Wizard{
	
	@Override
	public final void addPages() {
		addPage(new NewKeypairPage1());
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
