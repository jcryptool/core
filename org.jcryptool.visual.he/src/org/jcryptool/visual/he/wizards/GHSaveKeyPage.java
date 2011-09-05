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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.GHKeyPair;

/**
 * Page to save a key for Gentry & Halevi fully homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class GHSaveKeyPage extends WizardPage {

	/** minimal height for a textfield so it doesn't cut the entered text. */
	private static final int TEXTFIELD_MIN_HEIGHT = 15;

	/** page title */
	private static final String PAGENAME = "Save key page";

	/** title of this page, displayed in the head of the wizard. */
	private static final String TITLE = Messages.GHSaveKeyPage_Title;

	/** data */
	private final GHKeyPair keyPair;

	/** field for the owner of this keypair. */
	protected Text owner;

	/** field for the password. */
	private Text password;

	/** password verification field. */
	private Text passwordverify;


	/** modifyListener for the fields. */
	private final ModifyListener ml = new ModifyListener() {

		public void modifyText(final ModifyEvent e) {
			final boolean pwmatch = password.getText().equals(passwordverify.getText());
			setPageComplete(!owner.getText().equals("") && !password.getText().equals("") //$NON-NLS-1$ //$NON-NLS-2$
					&& pwmatch);
			if (pwmatch) {
				keyPair.setContactName(owner.getText());
				keyPair.setPassword(password.getText());
				setErrorMessage(null);
			} else {
				setErrorMessage(Messages.RSASaveKeypairPage_error_passwords_mismatch);
				keyPair.setPassword(null);
				keyPair.setContactName(null);
			}
		}
	};

	GHSaveKeyPage(final GHKeyPair keyPair) {
		super(PAGENAME, TITLE, null);
		this.keyPair = keyPair;
		setPageComplete(false);
	}

	public static String getPagename() {
		return PAGENAME;
	}

	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		// do stuff like layout et al
		final int ncol = 2;
		final GridLayout gl = new GridLayout(ncol, false);
		composite.setLayout(gl);
		final GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		// needed so textfields don't cut text
		gd.heightHint = TEXTFIELD_MIN_HEIGHT;
		final GridData gd2 = new GridData(SWT.FILL, SWT.CENTER, false, false, ncol, 1);
		final Label own = new Label(composite, SWT.NONE);
		own.setText(Messages.RSASaveKeypairPage_name);
		own.setLayoutData(gd2);

		owner = new Text(composite, SWT.BORDER | SWT.SINGLE);
		owner.addModifyListener(ml);
		owner.setLayoutData(gd);

		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(gd2);
		final Label pass = new Label(composite, SWT.NONE);
		pass.setText(Messages.RSASaveKeypairPage_password);
		pass.setLayoutData(gd2);

		password = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		password.addModifyListener(ml);
		password.setLayoutData(gd);
		new Label(composite, SWT.NONE).setLayoutData(gd);
		passwordverify = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		passwordverify.addModifyListener(ml);
		passwordverify.setLayoutData(gd);

		// finish
		setControl(composite);
	}

	@Override
	public IWizardPage getNextPage() {
		return null;
	}
}
