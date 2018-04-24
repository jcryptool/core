// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.wizards.pages;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.rsa.RSAData;

/**
 * page for saving a public key.
 * @author Michael Gaber
 */
public class RSASavePublicKeyPage extends RSASaveWizardPage {
	/** unique pagename to get this page from inside a wizard. */
	private static final String PAGENAME = "Save Public Key Page"; //$NON-NLS-1$

	/** title of this page, displayed in the head of the wizard. */
	private static final String TITLE = Messages.RSASavePublicKeyPage_save_pubkey;

	/** shared data object. */
	private final RSAData data;

	/**
	 * constructor, sets this page incomplete and the description.
	 * @param data shared Data object
	 */
	public RSASavePublicKeyPage(final RSAData data) {
		super(PAGENAME, TITLE, null);
		setPageComplete(false);
		this
				.setDescription(Messages.RSASavePublicKeyPage_enter_params);
		this.data = data;
	}

	/**
	 * Set up UI stuff.
	 * @param parent the parent composite
	 */
	public final void createControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());

        final Label own = new Label(composite, SWT.NONE);
        own.setText(Messages.RSASaveKeypairPage_name);
        GridData gd_own = new GridData(SWT.FILL, SWT.CENTER, false, false);
        own.setLayoutData(gd_own);

        owner = new Text(composite, SWT.BORDER | SWT.SINGLE);
        GridData gd_owner = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd_owner.widthHint = 300;
        owner.setLayoutData(gd_owner);
		owner.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				setPageComplete(!owner.getText().equals("")); //$NON-NLS-1$
				data.setContactName(owner.getText());
			}
		});

		// finish
		setControl(composite);
	}

	/**
	 * getter for the pagename.
	 * @return the pagename
	 */
	public static String getPagename() {
		return PAGENAME;
	}

	@Override
	public IWizardPage getNextPage() {
		return null;
	}
}
