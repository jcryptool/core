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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * Page for choosing whether to create a public key or a private keypair.
 *
 * @author Michael Gaber
 */
public class ChooseKeytypePage extends WizardPage {

	/** title of this page. */
	private static final String TITLE = "Schlüsselart wählen";

	/** name of this page. */
	private static final String PAGENAME = "Choose Keytype Page";

	/** the button for selecting to create a keypair. */
	private Button keypairButton;

	/**
	 * the selection listener which updates the buttons when changing from
	 * keypair to pubkey and vice versa.
	 */
	private final SelectionListener sl = new SelectionListener() {

		public void widgetSelected(final SelectionEvent e) {
			getContainer().updateButtons();
		}

		public void widgetDefaultSelected(final SelectionEvent e) {
			// won't be called;
		}
	};

	/**
	 * Constructor does nothing special.
	 */
	public ChooseKeytypePage() {
		super("Choose Keytype Page", TITLE, null);
		setDescription("Bitte wählen Sie aus, ob Sie ein neues Schlüsselpaar erzeugen oder einen privaten Schlüssel eingeben möchten.");
	}

	/**
	 * sets up all the UI stuff.
	 *
	 * @param parent
	 *            the parent composite.
	 */
	public final void createControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(1, false));

		keypairButton = new Button(composite, SWT.RADIO);
		keypairButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false));
		keypairButton.setText("Neues Schlüsselpaar");
		keypairButton
				.setToolTipText("Wählen Sie diesen Button, wenn Sie ein neues Schlüsselpaar erzeugen wollen");
		keypairButton.addSelectionListener(sl);

		final Button pubkeyButton = new Button(composite, SWT.RADIO);
		pubkeyButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false));
		pubkeyButton.setText("Neuer öffentlicher Schlüssel");
		pubkeyButton
				.setToolTipText("Wählen Sie diesen Button, wenn Sie einen neuen privaten Schlüssel eigeben möchten");
		pubkeyButton.addSelectionListener(sl);

		setControl(composite);
	}

	@Override
	public final IWizardPage getNextPage() {
		if (keypairButton.getSelection()) {
			return getWizard().getPage(NewKeypairPage.getPagename());
		} else {
			return getWizard().getPage(NewPublicKeyPage.getPagename());
		}
	}

	/**
	 * @return the pagename
	 */
	public static String getPagename() {
		return PAGENAME;
	}

	/**
	 * getter for the selection status of the keypair button.
	 *
	 * @return whether the user wants a keypair
	 */
	public final boolean keypair() {
		return keypairButton.getSelection();
	}
}
