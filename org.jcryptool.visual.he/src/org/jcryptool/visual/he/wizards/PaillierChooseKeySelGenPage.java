// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.visual.he.Messages;

/**
 * Page to choose whether to generate a new key or to enter an existing one
 * for Paillier homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class PaillierChooseKeySelGenPage extends WizardPage {
	private static final String PAGENAME = "Paillier Keyselection or -generation";
	private static final String TITLE=Messages.GHChooseKeySelGenPage_Title;
	private Button newKeyButton;
	private Button existingKeyButton;

	public PaillierChooseKeySelGenPage() {
		super(PAGENAME, TITLE, null);
		this.setDescription(Messages.GHChooseKeySelGenPage_Message);
	}

	public final void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);

		newKeyButton = new Button(composite, SWT.RADIO);
		newKeyButton.setText(Messages.GHChooseKeySelGenPage_Generate);
		newKeyButton.setToolTipText(Messages.GHChooseKeySelGenPage_Generate_Popup);
		newKeyButton.setSelection(true);
		newKeyButton.setLayoutData(gd);

		existingKeyButton = new Button(composite, SWT.RADIO);
		existingKeyButton.setText(Messages.GHChooseKeySelGenPage_Select);
		existingKeyButton.setToolTipText(Messages.GHChooseKeySelGenPage_Select_Popup);
		existingKeyButton.setLayoutData(gd);

		setControl(composite);

	}

	@Override
	public final IWizardPage getNextPage() {
		if (newKeyButton.getSelection()) {
			return this.getWizard().getPage(PaillierNewKeyPage.getPagename());
		} else {
			return this.getWizard().getPage(GHLoadKeyPage.getPagename());
		}
	}

	public static String getPagename() {
		return PAGENAME;
	}

	public final boolean wantNewKey() {
		return newKeyButton.getSelection();
	}
}
