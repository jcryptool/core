//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.ui.views.interfaces.IChangeKeyStoreListener;
import org.jcryptool.crypto.keystore.ui.wizards.ChooseKeyStoreWizard;
import org.jcryptool.crypto.keystore.ui.wizards.IChooseKeyStoreWizard;

/**
 * @author tkern
 *
 */
public class ChooseKeyStoreAction extends Action {
	private Shell shell;
	private WizardDialog dialog;
	private IChangeKeyStoreListener listener;

	public ChooseKeyStoreAction(IChangeKeyStoreListener listener) {
		this.setText(Messages.getString("Label.ChooseKeyStore")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("Label.ChooseKeyStore")); //$NON-NLS-1$
		this.setImageDescriptor(KeyStorePlugin.getImageDescriptor("icons/16x16/kgpg_info.png")); //$NON-NLS-1$
		this.listener = listener;
	}

	public void run() {
		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		Wizard wizard = new ChooseKeyStoreWizard();
		dialog = new WizardDialog(shell, wizard);
		dialog.setMinimumPageSize(300, 350);

		int result = dialog.open();
		if (result == Window.OK) {
			String selected = ((IChooseKeyStoreWizard)wizard).getSelectedKeyStore();
			LogUtil.logInfo("Selected KeyStore: " + selected); //$NON-NLS-1$
			listener.changeKeyStoreTo(selected);
		}
	}

}
