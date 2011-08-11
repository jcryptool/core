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
package org.jcryptool.crypto.classic.substitution.ui;

import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicTransformationPage;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicWizard;
import org.jcryptool.crypto.classic.substitution.algorithm.SubstitutionAlgorithm;

/**
 * The wizard for the substitution cipher.
 * 
 * @author t-kern
 * @author SLeischnig
 *
 */
public class SubstitutionWizard extends AbstractClassicWizard {

	
	

	/**
	 * Creates a new instance of SubstitutionWizard. 
	 */
	public SubstitutionWizard() {
		super(Messages.SubstitutionWizard_substitution);
	}
	
	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		page = new SubstitutionWizardPage();
		page.setAlgorithmSpecification(SubstitutionAlgorithm.specification);
		addPage(page);
		page2 = new AbstractClassicTransformationPage();
		addPage(page2);
	}
	
}
