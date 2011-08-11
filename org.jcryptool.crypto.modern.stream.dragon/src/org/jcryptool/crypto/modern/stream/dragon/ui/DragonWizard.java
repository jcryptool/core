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
package org.jcryptool.crypto.modern.stream.dragon.ui;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.modern.stream.dragon.ui.DragonWizardPage.DisplayOption;

/**
 * The Wizard for the Dragon algorithm
 * @author Tahir Kacak
 *
 */
public class DragonWizard extends Wizard {

	/** The wizard page for the Dragon cipher */
	private DragonWizardPage page;

	/**
	 * Creates a new instance of DragonWizard.
	 */
	public DragonWizard() {
		setWindowTitle(Messages.DragonWizard_0); 
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		page = new DragonWizardPage();
		addPage(page);
	}

	/**
	 * Returns the selected size of the key and IV (128 bit or 256 bit).
	 *
	 * @return the size of the key and IV
	 */
	public boolean getIs128Bit() {
		return page.getIs128Bit();
	}

	/**
	 * Returns the value input for the key as a String.
	 *
	 * @return the key of the dragon cipher
	 */
	public String getKey() {
		return page.getKeyValue();
	}

	/**
	 * The format selected for the inputting of the key (hexadecimal or binary).
	 *
	 * @return the format of the key
	 */
	public boolean getKeyFormatIsHexadecimal() {
		return page.getKeyFormatIsHexadecimal();
	}

	/**
	 * Returns the value input for the IV as a String.
	 *
	 * @return the IV of the dragon cipher
	 */
	public String getIV() {
		return page.getIvValue();
	}

	/**
	 * The format selected for the inputting of the IV (hexadecimal or binary).
	 *
	 * @return the format of the IV
	 */
	public boolean getIVFormatIsHexadecimal() {
		return page.getIvFormatIsHexadecimal();
	}

	/**
	 * Returns the DisplayOption selected for displaying the output files.
	 *
	 * @return the selected display option
	 */
	public DisplayOption getDisplayOption() {
		return page.getDisplayOption();
	}

	/**
	 * Returns the value input for the number of bytes of keystream to generate.
	 *
	 * @return the length of keystream to generate
	 */
	public String getKeystreamLengthValue() {
		return page.getKeystreamLengthValue();
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return true;
	}
}
