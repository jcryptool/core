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
package org.jcryptool.crypto.classic.caesar.ui;

import org.jcryptool.crypto.classic.caesar.algorithm.CaesarAlgorithm;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicTransformationPage;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicWizard;

/**
 * The wizard for the caesar cipher.
 *
 * @author t-kern
 * @author SLeischnig
 *
 */
public class CaesarWizard extends AbstractClassicWizard {

	/**
	 * Creates a new instance of CaesarWizard.
	 */
	public CaesarWizard() {
		super(Messages.Label_Caesar);
	}

	@Override
	public void addPages() {
		page = new CaesarWizardPage();
		page.setAlgorithmSpecification(CaesarAlgorithm.specification);
		addPage(page);
		page2 = new AbstractClassicTransformationPage();
		addPage(page2);
	}
}
