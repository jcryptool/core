//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage.PageConfiguration;

/**
 * The wizard for the transformation settings
 * 
 * @author SLeischnig
 * 
 */
public class TranspTextWizard extends Wizard {

	private TranspTextWizardPage firstPage;

	private boolean finished = false;
	private PageConfiguration textPageConfig;

	/**
	 * Creates a new instance of CaesarWizard.
	 * 
	 * @param alphabets       the alphabets to be displayed in the alphabet box
	 * @param defaultAlphabet the name of the default alphabet (the selected entry
	 *                        in the alphabet combo box) - if the alphabet is not
	 *                        found, the first Alphabet is used
	 */
	public TranspTextWizard() {
		setWindowTitle(Messages.TranspTextWizard_textwizard);
		firstPage = new TranspTextWizardPage();
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public final void addPages() {
		addPage(firstPage);
	}

	public TranspTextWizardPage.PageConfiguration getTextPageConfig() {
		if (!finished)
			return firstPage.getPageConfiguration();
		else
			return textPageConfig;
	}

	private void saveResults() {
		this.textPageConfig = getTextPageConfig();
		finished = true;
	}

	public void setTextPageConfig(TranspTextWizardPage.PageConfiguration config) {
		firstPage.setPageConfiguration(config);
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public final boolean performFinish() {
		saveResults();
		return true;
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#performCancel()
	 */
	@Override
	public final boolean performCancel() {
		saveResults();
		return true;
	}

}
