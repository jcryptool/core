// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.keystore.ui.wizardpages.BackupRestorePage;

public class BackupRestoreWizard extends Wizard {

	public BackupRestoreWizard() {
		setWindowTitle(Messages.BackupRestoreWizard_0);
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
