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
package org.jcryptool.crypto.classic.doppelkasten.ui;

import org.jcryptool.crypto.classic.doppelkasten.algorithm.DoppelkastenAlgorithm;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicTransformationPage;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicWizard;

/**
 * The wizard for the Autokey-Vigenere-Chiffre.
 * 
 * @author SLeischnig
 *
 */
public class DoppelkastenWizard extends AbstractClassicWizard {
	
	
	
	/**
	 * Creates a new instance of CaesarWizard.
	 */
	public DoppelkastenWizard() {
		super(Messages.DoppelkastenWizard_classic); 
	}
	
	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		page = new DoppelkastenWizardPage();
		page.setAlgorithmSpecification(DoppelkastenAlgorithm.specification);
		addPage(page);
		page2 = new AbstractClassicTransformationPage();
		addPage(page2);
	}
	
}
