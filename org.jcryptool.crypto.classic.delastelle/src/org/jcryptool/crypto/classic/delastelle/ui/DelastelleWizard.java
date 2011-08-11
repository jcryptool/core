//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.delastelle.ui;

import org.jcryptool.crypto.classic.delastelle.algorithm.DelastelleAlgorithm;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicTransformationPage;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicWizard;

/**
 * The wizard for the Delastelle Chiffre
 * 
 * @author SLeischnig
 *
 */
public class DelastelleWizard extends AbstractClassicWizard {

	/**
	 * Creates a new instance of DelastelleWizard.
	 */
	public DelastelleWizard() {
		super(Messages.DelastelleWizardPage_delastelle);
	}
	
	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		page = new DelastelleWizardPage();
		page.setAlgorithmSpecification(DelastelleAlgorithm.specification);
		addPage(page);
		page2 = new AbstractClassicTransformationPage();
		addPage(page2);
	}
	
}
