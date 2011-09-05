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

import java.math.BigInteger;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.dsa.DSAData;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

/**
 * Wizardpage for entering the parameters of a new Public Key.
 * @author Michael Gaber
 */
public class NewPublicKeyPage extends WizardPage {

	/** unique pagename to get this page from inside a wizard. */
	private static final String PAGENAME = "New Public Key Page";

	/** title of this page, displayed in the head of the wizard. */
	private static final String TITLE = "Parameter festlegen.";

	/**
	 * getter for the pagename constant for easy access.
	 * @return the pagename
	 */
	public static String getPagename() {
		return PAGENAME;
	}

	/** verify listener for checking inputs. */
	private static final VerifyListener VL = Lib.getVerifyListener(Lib.DIGIT);

	private final ModifyListener ml = new ModifyListener() {

		public void modifyText(final ModifyEvent e) {
			setPageComplete();
		}
	};

	/** data-object storing all relevant information about the algorithm. */
	private final DSAData data;

	/** selection whether this key should be saved. */
	private Button saveButton;

	private Text pField;

	private Text qField;

	private Text gField;

	private Text yField;

	private Composite composite;

	/**
	 * Constructor for a new wizardpage getting the data object.
	 * @param data the data object
	 */
	public NewPublicKeyPage(final DSAData data) {
		super(PAGENAME, TITLE, null);
		this.data = data;
		this.setDescription("Bitte geben Sie die Parameter des öffentlichen Schlüssels ein.");
		setPageComplete(false);
	}

	/**
	 * Set up the UI stuff.
	 * @param parent the parent composite
	 */
	public final void createControl(final Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		// do stuff like layout et al
		final int ncol = 4;
		final GridLayout gl = new GridLayout(ncol, false);
		composite.setLayout(gl);
		final GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);

		Label label = new Label(composite, SWT.NONE);
		label.setText("Bitte wählen Sie Primzahlen für p und q, so dass p > 256 und gcd(q, p-1)!=1 gelten.");
		label.setLayoutData(gd);

		new Label(composite, SWT.NONE).setText("p");
		pField = new Text(composite, SWT.BORDER);
		pField.addVerifyListener(VL);
		pField.addModifyListener(ml);

		new Label(composite, SWT.NONE).setText("q");
		qField = new Text(composite, SWT.BORDER);
		qField.addVerifyListener(VL);
		qField.addModifyListener(ml);

		label = new Label(composite, SWT.NONE);
		label.setText("Bitte wählen Sie Ihren Generator g.");
		label.setLayoutData(gd);
		new Label(composite, SWT.NONE).setText("g");
		gField = new Text(composite, SWT.BORDER);
		gField.addVerifyListener(VL);
		gField.addModifyListener(ml);

		label = new Label(composite, SWT.NONE);
		label.setLayoutData(gd);
		label.setText("Zuletzt wählen Sie bitte noch Ihren öffentlichen Schlüssel y.");
		new Label(composite, SWT.NONE).setText("y");
		yField = new Text(composite, SWT.BORDER);
		yField.addVerifyListener(VL);
		yField.addModifyListener(ml);

		// Separator
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd);

		// should this key be saved?
		saveButton = new Button(composite, SWT.CHECK);
		saveButton.setText("Öffentlichen Schlüssel speichern");
		saveButton
				.setToolTipText("wählen Sie dieses Feld um den öffentlichen Schlüssel im mitgelieferten Keystore zu speichern.");
		saveButton.setSelection(data.isStandalone());
		saveButton.setEnabled(!data.isStandalone());
		saveButton.setLayoutData(gd);
		saveButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
			}

			public void widgetSelected(final SelectionEvent e) {
				getContainer().updateButtons();
			}
		});

		// fill in old data
		if (data.getY() != null) {
			pField.setText(data.getP().toString());
			qField.setText(data.getQ().toString());
			gField.setText(data.getGenerator().toString());
			yField.setText(data.getY().toString());
		}

		// finishing touch
		setControl(composite);
	}

	private void setPageComplete() {
		setErrorMessage(null);
		if (!pField.getText().equals("")) {
			final BigInteger p = new BigInteger(pField.getText());
			if (p.compareTo(Constants.TWOFIVESIX) < 0) {
				setErrorMessage("Bitte wählen Sie Ihr p>256.");
			}
			if (!Lib.isPrime(p)) {
				setErrorMessage("Bitte wählen Sie p prim.");
			}
			if (!gField.getText().equals("")) {
				final BigInteger g = new BigInteger(gField.getText());
				if (!Lib.checkGenerator(g, p)) {
					setErrorMessage("Bitte wählen Sie g als Generator zu p.");
				}
			}
			if (!yField.getText().equals("")) {
				final BigInteger a = new BigInteger(yField.getText());
				if (a.compareTo(p) > 0) {
					setErrorMessage("Bitte wählen Sie Ihr A<p.");
				}
			}
		}
		setPageComplete(getErrorMessage() == null);
	}

	@Override
	public void setErrorMessage(final String newMessage) {
		if (newMessage == null || getErrorMessage() == null) {
			super.setErrorMessage(newMessage);
		} else {
			super.setErrorMessage(getErrorMessage() + "\n" + newMessage);
		}
	}

	@Override
	public final IWizardPage getNextPage() {
		if (wantSave()) {
			return super.getNextPage();
		} else {
			return null;
		}
	}

	/**
	 * getter for the status of the save button to be accessed externally.
	 * @return whether the user wants to save the key
	 */
	public final boolean wantSave() {
		return saveButton.getSelection();
	}
}
