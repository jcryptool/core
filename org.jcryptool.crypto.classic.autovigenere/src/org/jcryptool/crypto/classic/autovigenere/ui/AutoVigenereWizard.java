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
package org.jcryptool.crypto.classic.autovigenere.ui;

import org.jcryptool.crypto.classic.autovigenere.algorithm.AutoVigenereAlgorithm;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicTransformationPage;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicWizard;

/**
 * The wizard for the Autokey-Vigenère-Chiffre.
 * 
 * @author SLeischnig
 *
 */
public class AutoVigenereWizard extends AbstractClassicWizard {
	
	
	/**
	 * Creates a new instance of CaesarWizard.
	 */
	public AutoVigenereWizard() {
		super(Messages.AutoVigenereWizard_title);
	}
	
	@Override
	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		page = new AutoVigenereWizardPage();
		page.setAlgorithmSpecification(AutoVigenereAlgorithm.specification);
		addPage(page);
		page2 = new AbstractClassicTransformationPage();
		addPage(page2);
	}
}
