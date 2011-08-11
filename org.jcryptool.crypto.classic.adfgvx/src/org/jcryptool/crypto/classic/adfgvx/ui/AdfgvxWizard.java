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
package org.jcryptool.crypto.classic.adfgvx.ui;

import org.jcryptool.crypto.classic.adfgvx.algorithm.AdfgvxAlgorithm;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicTransformationPage;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicWizard;

/**
 * The wizard for the ADFGVX cipher.
 * 
 * @author t-kern
 * @author SLeischnig
 *
 */
public class AdfgvxWizard extends AbstractClassicWizard {

	/**
	 * Creates a new instance of AdfgvxWizard.
	 */
	public AdfgvxWizard() {
		super(Messages.AdfgvxWizard_adfgvx);
	}
	
	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		page = new AdfgvxWizardPage();
		page.setAlgorithmSpecification(AdfgvxAlgorithm.specification);
		addPage(page);
		page2 = new AbstractClassicTransformationPage();
		addPage(page2);
	}
	
	/**
	 * Returns the substitution key.
	 * 
	 * @return	The substitution key
	 */
	public String getSubstitutionKey() {
		return ((AdfgvxWizardPage)page).getSubstitutionKey();
	}
	
	/**
	 * Returns the transposition key.
	 * 
	 * @return	The transposition key
	 */
	public String getTranspositionKey() {
		return ((AdfgvxWizardPage)page).getTranspositionKey();
	}


}
