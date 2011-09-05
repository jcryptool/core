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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.dsa.DSAData;

/**
 * page for saving a keypair.
 * @author Michael Gaber
 */
public class SaveKeypairPage extends SaveWizardPage {

	/** minimal height for a textfield so it diesn't cut the entered text. */
	private static final int TEXTFIELD_MIN_HEIGHT = 15;

	/** unique pagename to get this page from inside a wizard. */
	private static final String PAGENAME = "Save Keypair Page";

	/** title of this page, displayed in the head of the wizard. */
	private static final String TITLE = "Schlüsselpaar speichern.";

	/** field for the password. */
	private Text password;

	/** password verification field. */
	private Text passwordverify;

	/** modifyListener for the fields. */
	private final ModifyListener ml = new ModifyListener() {

		public void modifyText(final ModifyEvent e) {
			final boolean pwmatch = password.getText().equals(passwordverify.getText());
			setPageComplete(!owner.getText().equals("") && !password.getText().equals("")
					&& pwmatch);
			if (pwmatch) {
				data.setContactName(owner.getText());
				data.setPassword(password.getText());
				setErrorMessage(null);
			} else {
				setErrorMessage("Die eingegebenen Passw\u00F6rter stimmen nicht \u00FCberein");
				data.setPassword(null);
				data.setContactName(null);
			}
		}
	};

	/** shared data object. */
	private final DSAData data;

	/**
	 * Constructor, sets page incomplete and calls super and sets the description.
	 * @param data the data object
	 */
	public SaveKeypairPage(final DSAData data) {
		super(PAGENAME, TITLE, null);
		setPageComplete(false);
		this
				.setDescription("Bitte geben Sie die Speicherparameter für das eben erzeugte Schlüsselpaar an");
		this.data = data;
	}

	/**
	 * Set up UI stuff.
	 * @param parent the parent composite
	 */
	public final void createControl(final Composite parent) {
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
		own.setText("Bitte geben Sie den Namen des Schl\u00FCsselbesitzers ein");
		own.setLayoutData(gd2);

		owner = new Text(composite, SWT.BORDER | SWT.SINGLE);
		owner.addModifyListener(ml);
		owner.setLayoutData(gd);

		final Label pass = new Label(composite, SWT.NONE);
		pass.setText("Bitte geben Sie, ein Passwort ein und best\u00E4tigen Sie es");
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
