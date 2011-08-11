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
package org.jcryptool.crypto.classic.xor.ui;

import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicTransformationPage;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicWizard;
import org.jcryptool.crypto.classic.xor.algorithm.XorAlgorithm;

/**
 * The wizard for the Xor cipher.
 * 
 * @author t-kern
 *
 */
public class XorWizard extends AbstractClassicWizard {


	/**
	 * Creates a new instance of XorWizard
	 */
	public XorWizard() {
		super(Messages.XorWizard_XOR);
	}
	
	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		page = new XorWizardPage();
		page.setAlgorithmSpecification(XorAlgorithm.specification);
		addPage(page);
		page2 = new AbstractClassicTransformationPage();
		addPage(page2);
	}
	/**
	 * Returns the (absolute) name of the selected file
	 * 
	 * @return	The (absolute) name of the selected file
	 */
	public String getPathToKeyFile() {
		return ((XorWizardPage)page).getPathToKeyFile();
	}
	
	/**
	 * @return the method of key input - true:textual input; false:vernam file operation
	 */
	public boolean getKeyMethod() {
		return ((XorWizardPage)page).getKeyMethod();
	}
}
