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
package org.jcryptool.crypto.keystore.ui.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.ui.views.KeystoreView;
import org.jcryptool.crypto.keystore.ui.wizards.BackupRestoreWizard;

public class KeyStoreBackupHandler extends AbstractHandler {

	private KeystoreView view;
	
	public KeyStoreBackupHandler(KeystoreView view) {
		this.view = view;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			BackupRestoreWizard wizard = new BackupRestoreWizard();
			WizardDialog dlgWizard = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				wizard);
			// WizardDialog.open() turns out to return 0 for Finish and 1 for Cancel.
			// I've found no corresponding SWT constants, though...
			if(dlgWizard.open() == 0) {
				if(wizard.getRestoreRequested())
					restore(event);
				else
					backup(event);
			}
		} catch(Exception ex) {
			LogUtil.logError(KeyStorePlugin.PLUGIN_ID, ex);
		}
		return null;
	}

	public void backup(ExecutionEvent event) {
		FileDialog dlgFile = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.SAVE);
		String pathToFile = dlgFile.open();
		if(pathToFile != null)
			KeyStoreManager.getInstance().backupKeystore(pathToFile);
	}

	public void restore(ExecutionEvent event) {
		FileDialog dlg = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.OPEN);
		String pathToFile = dlg.open();
		if(pathToFile != null) {
			KeyStoreManager.getInstance().restoreKeystore(pathToFile);
			view.getViewer().reload();
		}	
	}
}
