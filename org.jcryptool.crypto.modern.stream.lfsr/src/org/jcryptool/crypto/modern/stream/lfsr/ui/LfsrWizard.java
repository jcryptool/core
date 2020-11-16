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
package org.jcryptool.crypto.modern.stream.lfsr.ui;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.crypto.modern.stream.lfsr.ui.LfsrWizardPage.DisplayOption;

/**
 * The Wizard for the LFSR algorithm
 * @author Tahir Kacak, Daniel Dwyer
 *
 */
public class LfsrWizard extends Wizard {

	/** The wizard page for the LFSR cipher */
	private LfsrWizardPage page;

	public static final int MAX_LFSR_LENGTH = 15;

	/**
	 * Creates a new instance of LfsrWizard.
	 */
	public LfsrWizard() {
		EditorsManager editor = EditorsManager.getInstance();
		setWindowTitle(Messages.LfsrWizard_0 + " \"" + editor.getActiveEditorTitle() + "\"");
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		page = new LfsrWizardPage();
		addPage(page);
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return true;
	}

	/**
	 * Returns the value input for the tap settings of the LFSR as a boolean array.
	 *
	 * @return the tap settings of the LFSR
	 */
	public boolean[] getTapSettings() {
		return page.getTapSettings();
	}

	/**
	 * Returns the value input for the seed (initial state) of the LFSR as a boolean array.
	 *
	 * @return the seed of the LFSR
	 */
	public boolean[] getSeed() {
		return page.getSeed();
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
	 * 
	 * @return The selected LFSR length
	 */
	public int getLfsrLength() {
		return page.getLFSRLength();
	}
}
