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
package org.jcryptool.crypto.classic.transposition.ui;

import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicTransformationPage;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicWizard;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionAlgorithm;

/**
 * The wizard for the Autokey-Vigenère-Chiffre.
 * 
 * @author SLeischnig
 *
 */
public class TranspositionWizard extends AbstractClassicWizard {
	
	/**
	 * Creates a new instance of CaesarWizard.
	 */
	public TranspositionWizard() {
		super(Messages.TranspositionWizard_classic);
	}
	
	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		page = new TranspositionWizardPage();
		page.setAlgorithmSpecification(TranspositionAlgorithm.specification);
		addPage(page);
		page2 = new AbstractClassicTransformationPage();
		addPage(page2);
	}
	
	/** returns the second key, if a second key was entered.
	 * @return the second key.
	 */
	public String getKey2() {
		return ((TranspositionWizardPage)page).getKey2();
	}
	
	public boolean getTransp1InOrder() {
    	return ((TranspositionWizardPage)page).getTransp1InOrder();
    }
    
    public boolean getTransp1OutOrder() {
    	return ((TranspositionWizardPage)page).getTransp1OutOrder();
    }

    public boolean getTransp2InOrder() {
    	return ((TranspositionWizardPage)page).getTransp2InOrder();
	}

    public boolean getTransp2OutOrder() {
    	return ((TranspositionWizardPage)page).getTransp2OutOrder();
    }

}
