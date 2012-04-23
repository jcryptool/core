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
package org.jcryptool.crypto.classic.alphabets.ui;

import org.eclipse.jface.wizard.Wizard;

/**
 * The wizard for adding a new alphabet.
 * 
 * @author t-kern
 *
 */
public class AddAlphabetWizard extends Wizard {

	/** The wizard page */
	private AddAlphabetWizardPage page;
	
	/**
	 * Creates a new instance of AddAlphabetWizard.
	 */
	public AddAlphabetWizard() {
		setWindowTitle(Messages.getString("AddAlphabetWizard.0")); //$NON-NLS-1$
	}
	
	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		page = new AddAlphabetWizardPage();
		addPage(page);
	}
	
	/**
	 * Returns the name of the new alphabet.
	 * 
	 * @return	The name of the new alphabet
	 */
	public String getAlphabetName() {
		return page.getAlphabetName();
	}
	
	/**
	 * Returns the charset of the new alphabet.
	 * 
	 * @return	The charset of the new alphabet
	 */
	public String getAlphabetCharset() {
		return page.getAlphabetCharset();
	}
	
	/**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return true;
	}

}
