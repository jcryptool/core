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

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * page to choose whether to use a new key or enter the parameters manually.
 * @author Michael Gaber
 */
public class EncryptVerifyPage extends WizardPage {

	/** unique pagename to get this page from inside a wizard. */
	private static final String PAGENAME = "Encrypt/Verify Page";

	/** title of this page, displayed in the head of the wizard. */
	private static final String TITLE = "Vorgehensweise wählen.";

	/** Button for selecting to create a new key. */
	private Button newPubkeyButton;

	/** Button for selecting to load an existing key. */
	private Button existingPubkeyButton;

	/**
	 * Constructor, setting name, title and description.
	 */
	public EncryptVerifyPage() {
		super(PAGENAME, TITLE, null);
		this
				.setDescription("Bitte wählen Sie ob Sie einen bestehenden Schlüssel nutzen oder einen neuen erzeugen wollen");
	}

	/**
	 * sets up all the UI stuff.
	 * @param parent the parent composite
	 */
	public final void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		// set layout
		composite.setLayout(new GridLayout());
		// create grid data
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
		// add enter Pubkey button
		newPubkeyButton = new Button(composite, SWT.RADIO);
		newPubkeyButton.setText("Manuelle Eingabe");
		newPubkeyButton.setToolTipText("Wählen Sie dies, wenn Sie den öffentlichen Schlüssel manuell eingeben möchten");
		newPubkeyButton.setSelection(true);
		newPubkeyButton.setLayoutData(gd);
		// add existing Pubkey button
		existingPubkeyButton = new Button(composite, SWT.RADIO);
		existingPubkeyButton.setText("Bestehender öffentlicher Schlüssel");
		existingPubkeyButton
				.setToolTipText("Wählen Sie dies, wenn Sie einen bestehenden öffentlichen Schlüssel aus dem Keystore laden möchten");
		existingPubkeyButton.setLayoutData(gd);
		// finally set control something
		setControl(composite);
	}

	@Override
	public final IWizardPage getNextPage() {
		if (newPubkeyButton.getSelection()) {
			return this.getWizard().getPage(NewPublicKeyPage.getPagename());
		} else {
			return this.getWizard().getPage(LoadPublicKeyPage.getPagename());
		}
	}

	/**
	 * getter for the pagename.
	 * @return the pagename
	 */
	public static String getPagename() {
		return PAGENAME;
	}

	/**
	 * convenience method for checking whether key creation is enabled or not.
	 * @return selection status of the corresponding checkbox
	 */
	public final boolean wantNewKey() {
		return newPubkeyButton.getSelection();
	}
}
