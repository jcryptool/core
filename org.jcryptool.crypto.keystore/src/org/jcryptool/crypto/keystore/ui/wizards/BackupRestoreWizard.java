package org.jcryptool.crypto.keystore.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.keystore.ui.wizardpages.BackupRestorePage;

public class BackupRestoreWizard extends Wizard {

	public BackupRestoreWizard() {
		// TODO Auto-generated constructor stub
	}

	BackupRestorePage page;
	
	@Override
	public final void addPages() {
		page = new BackupRestorePage();
		addPage(page);
	}
	
	@Override
	public final boolean canFinish() {
		return true;
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean getRestoreRequested() {
		return page.getRestoreRequested();
	}
}
