//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.keystore.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IImportDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IImportWizard;

/**
 * @author tkern
 *
 */
public class ImportWizard extends Wizard implements IImportWizard {

	private ImportWizardPage page;

	private IImportDescriptor descriptor;

	public ImportWizard() {
		setWindowTitle(Messages.ImportWizard_0);
	}

	public void addPages() {
		page = new ImportWizardPage();
		addPage(page);
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		descriptor = page.getImportDescriptor();
		return true;
	}

	public IImportDescriptor getImportDescriptor() {
		return descriptor;
	}



}
