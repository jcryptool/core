// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.dsa.ui.wizards.wizardpages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Text;

/**
 * abstract superclass for all wizardpages that want only text-input.
 * @author Michael Gaber
 */
public abstract class TextWizardPage extends WizardPage {

	/**
	 * Constructor does only call super.
	 * @see WizardPage
	 * @param pageName name of the Page
	 * @param title title to be shown
	 * @param titleImage the ImageDescriptor of the image to be shown
	 */
	protected TextWizardPage(final String pageName, final String title, final ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/** textfield. */
	protected Text text;

	/**
	 * getter for the text that was entered in this wizardpage.
	 * @return content of the text-field
	 */
	public final String getText() {
		return text.getText();
	}

}
