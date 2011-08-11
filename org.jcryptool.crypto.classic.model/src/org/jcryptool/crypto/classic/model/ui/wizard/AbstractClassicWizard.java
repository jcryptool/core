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
package org.jcryptool.crypto.classic.model.ui.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;


/**
 * Implementation of a generic classic cryptoalgorithm wizard. 
 * Can be instantiated directly, but is rather intended to be subclassed. 
 * 
 * @author SLeischnig
 *
 */
public class AbstractClassicWizard extends Wizard {
	
	/** Wizard pages */
	protected AbstractClassicCryptoPage page;
	protected AbstractClassicTransformationPage page2;
	
	/**
	 * Creates a new instance of AbstractClassicWizard and sets the window title
	 */
	public AbstractClassicWizard(String windowTitle) {
		setWindowTitle(windowTitle);
	}
	
	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		page = new AbstractClassicCryptoPage();
		addPage(page);
		page2 = new AbstractClassicTransformationPage();
		addPage(page2);
	}
	
	/**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return true;
	}
	
	/**
	 * Returns the selected key.
	 * 
	 * @return	The selected key
	 */
	public String getKey() {
		return page.getKey();
	}
	
	/**
	 * Returns the selected currentAlphabet.
	 * 
	 * @return	The selected currentAlphabet
	 */
	public AbstractAlphabet getSelectedAlphabet() {
		return page.getSelectedAlphabet();
	}

	/**
	 * Returns <code>true</code>, if the selected operation is <i>Encrypt</i>; <code>false</code> if it is <i>Decrypt</i>.
	 * 
	 * @return	<code>true</code>, if the selected operation is <i>Encrypt</i>; <code>false</code> if it is <i>Decrypt</i>
	 */
	public boolean encrypt() {
		return page.encrypt();
	}
	
	/**
	 * Returns <code>true</code>, if characters who are not part of the selected currentAlphabet are supposed to be filtered out.
	 * 
	 * @return	<code>true</code>, if characters who are not part of the selected currentAlphabet are supposed to be filtered out
	 */
	public boolean isNonAlphaFilter() {
		return page.isNonAlphaFilter();
	}
	
	/**
	 * Returns <code>true</code>, if characters who are not part of the selected currentAlphabet are supposed to be filtered out.
	 * 
	 * @return	<code>true</code>, if characters who are not part of the selected currentAlphabet are supposed to be filtered out
	 */
	public TransformData getTransformData() {
		return page2.getTransformData();
	}

}
