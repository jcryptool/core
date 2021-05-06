//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
/**
 *
 */
package org.jcryptool.crypto.flexiprovider.keystore.actions;

import java.security.cert.Certificate;

import javax.crypto.SecretKey;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.flexiprovider.keystore.ImportManager;
import org.jcryptool.crypto.flexiprovider.keystore.wizards.ImportWizard;
import org.jcryptool.crypto.keystore.descriptors.ImportDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IImportDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IImportWizard;
import org.jcryptool.crypto.keystore.keys.KeyType;
import org.jcryptool.crypto.keystore.ui.actions.AbstractImportKeyStoreEntryHandler;

import codec.pkcs12.PFX;

/**
 * @author tkern
 * @author Holger Friedrich (support for Commands, additional class based on ImportKeyAction)
 *
 */
public class ImportKeyHandler extends AbstractImportKeyStoreEntryHandler {

	private Shell shell;
	private WizardDialog dialog;

	public ImportKeyHandler() {
		// this.setText(Messages.ImportKeyAction_0);
		// this.setToolTipText(Messages.ImportKeyAction_1);
		// this.setImageDescriptor(FlexiProviderKeystorePlugin.getImageDescriptor("icons/16x16/kgpg_import.png")); //$NON-NLS-1$
	}

	public Object execute(ExecutionEvent event) {
		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();



		Wizard wizard = new ImportWizard();
		dialog = new WizardDialog(shell, wizard);
		dialog.setMinimumPageSize(300, 350);

		int result = dialog.open();
		if (result == Window.OK) {
			if (wizard instanceof IImportWizard) {
				IImportDescriptor desc = ((IImportWizard)wizard).getImportDescriptor();
				IPath path = new Path(desc.getFileName());
				if (desc.getKeyStoreEntryType().equals(KeyType.SECRETKEY)) {
					SecretKey key = ImportManager.getInstance().importSecretKey(path);
					performImportAction(
							new ImportDescriptor(
									desc.getContactName(),
									key.getAlgorithm(),
									KeyType.SECRETKEY,
									desc.getFileName(),
									desc.getPassword(),
									"FlexiCore", //$NON-NLS-1$
									-1
							),
							key);
				} else if (desc.getKeyStoreEntryType().equals(KeyType.KEYPAIR)) {
					PFX pfx = ImportManager.getInstance().importPFX(path);
					performImportAction(desc, pfx);
				} else if (desc.getKeyStoreEntryType().equals(KeyType.PUBLICKEY)) {
					Certificate cert = ImportManager.getInstance().importCertificate(path);
					performImportAction(desc, cert);
				}
			}
		}
		return(null);
	}

}