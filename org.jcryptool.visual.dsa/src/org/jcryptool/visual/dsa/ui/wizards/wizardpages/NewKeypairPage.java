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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.dsa.DSAData;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

/**
 * Wizardpage for creating a new RSA public-private-keypair.
 * @author Michael Gaber
 */
public class NewKeypairPage extends WizardPage {

	/** unique pagename to get this page from inside a wizard. */
	private static final String PAGENAME = "New Keypair Page";

	/** title of this page, displayed in the head of the wizard. */
	private static final String TITLE = "Parameter festlegen.";

	/**
	 * a {@link VerifyListener} instance that makes sure only digits are entered.
	 */
	private static final VerifyListener VL = Lib.getVerifyListener(Lib.DIGIT);

	protected static final BigInteger EIGHT = new BigInteger("8");

	protected static final BigInteger ONESIXTY = new BigInteger("160");

	/**
	 * getter for the pagename constant for easy access.
	 * @return the pagename
	 */
	public static String getPagename() {
		return PAGENAME;
	}

	/** shared data-object to push around. */
	private final DSAData data;

	/** Selection whether the user wants to sve the new Keypair. */
	private Button saveKeypairButton;

	/** Drop-Down for selecting the p values. */
	private Text lField;

	private Text nField;

	private Combo qField;

	private Text xfield;

	private Text ytext;

	private Combo pField;

	private Text gField;

	/**
	 * Constructor, setting description completeness-status and data-object.
	 * @param data the data object to store the entered values
	 */
	public NewKeypairPage(final DSAData data) {
		super(PAGENAME, TITLE, null);
		this.setDescription("Bitte geben Sie hier die Parameter ein, um ein neues ElGamal-Schlüsselpaar zu erzeugen.");
		this.data = data;
		setPageComplete(false);
	}

	/**
	 * set up the UI stuff.
	 * @param parent the parent composite
	 */
	public final void createControl(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		// do stuff like layout et al
		// set layout
		final int ncol = 4;
		final GridLayout gl = new GridLayout(ncol, false);
		composite.setLayout(gl);
		final GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
		final GridData gd1 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
		final GridData gd2 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
		final GridData gd3 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
		final GridData gd4 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
		final GridData gd5 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
		final GridData gd6 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
		final GridData gd7 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
		final GridData gd8 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
		// begin stuff
		Label label = new Label(composite, SWT.NONE);
		label.setText("Bitte wählen Sie Bitlängen L und N für Ihren Schlüssel, dabei muss L>N gelten.");
		label.setLayoutData(gd);
		new Label(composite, SWT.NONE).setText("L");
		lField = new Text(composite, SWT.BORDER);
		lField.addVerifyListener(VL);
		lField.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (nField.getText().length() != 0 && lField.getText().length() != 0) {
					final BigInteger n = new BigInteger(nField.getText());
					final BigInteger l = new BigInteger(lField.getText());
					if (n.compareTo(l) > 0) {
						setErrorMessage("Bitte wählen Sie L>N damit der Algorithmus funktionieren kann");
					} else {
						setErrorMessage(null);
					}
				} else {
					setErrorMessage(null);
				}
			}
		});
		new Label(composite, SWT.NONE).setText("N");
		nField = new Text(composite, SWT.BORDER);
		nField.addVerifyListener(VL);
		nField.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				final BigInteger n = new BigInteger(nField.getText());
				if (n.compareTo(EIGHT) > 0) {
					setMessage(
							"Achtung, wenn Sie N>8 wählen, wird es nicht möglich sein, den einfachen Hash-Algorithmus zu verwenden.",
							WARNING);
				} else if (n.compareTo(ONESIXTY) > 0) {
					setErrorMessage("Ihr N ist leider für die implementierten Hash-Algorithmen zu groß, bitte wählen Sie ein kleineres N");
				} else {
					setMessage(null);
					qField.setItems(Lib.getPrimesWithBitLength(n));
				}
			}
		});
		// Separator
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd1);
		// primitive root
		label = new Label(composite, SWT.NONE);
		label.setText("Wählen Sie nun aus den angebotenen Zahlen je einen Wert für q und p aus");
		label.setLayoutData(gd2);
		new Label(composite, SWT.NONE).setText("q");
		qField = new Combo(composite, SWT.SINGLE | SWT.READ_ONLY);
		qField.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				pField.setItems(Lib.getPrimesWithBitLength(new BigInteger(lField.getText()), new BigInteger(qField.getText())));
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// won't be called
			}
		});
		new Label(composite, SWT.NONE).setText("p");
		pField = new Combo(composite, SWT.SINGLE | SWT.READ_ONLY);
		// Separator
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd3);
		// a
		label = new Label(composite, SWT.NONE);
		label.setText("Wählen Sie als letzten Parameter ein h, aus dem ein Generator für die Gruppe bestimmt wird.");
		label.setLayoutData(gd4);
		new Label(composite, SWT.NONE).setText("h");
		new Label(composite, SWT.NONE).setText("g");
		gField = new Text(composite, SWT.READ_ONLY | SWT.BORDER);
		// Separator
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd5);
		// x
		label = new Label(composite, SWT.NONE);
		label
				.setText("Zuletzt wählen Sie noch Ihren privaten Schlüssel x, aus dem sich der öffentliche Schlüssel y berechnet.");
		label.setLayoutData(gd6);
		new Label(composite, SWT.NONE).setText("x");
		xfield = new Text(composite, SWT.BORDER);
		xfield.addVerifyListener(VL);
		xfield.addModifyListener(new ModifyListener() {

			public void modifyText(final ModifyEvent e) {
				if (xfield.getText() != "") {
					final BigInteger a = new BigInteger(xfield.getText());
					if (a.compareTo(Constants.TWO) < 0 || a.compareTo(new BigInteger(lField.getText()).subtract(Constants.TWO)) > 0) {
						setErrorMessage("Bitte wählen Sie a im angegebenen, gültigen Bereich");
						setPageComplete(false);
					} else {
						ytext.setText(new BigInteger(qField.getText()).modPow(a, new BigInteger(lField.getText()))
								.toString());
						setErrorMessage(null);
						setPageComplete(true);
					}
				} else {
					setPageComplete(false);
				}
			}
		});

		new Label(composite, SWT.NONE).setText("y");
		ytext = new Text(composite, SWT.READ_ONLY | SWT.BORDER);

		// Separator
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd7);
		// Save?
		saveKeypairButton = new Button(composite, SWT.CHECK);
		saveKeypairButton.setText("Schlüsselpaar speichern");
		saveKeypairButton
				.setToolTipText("Wählen Sie diese Box an, um das Schlüsselpaar im mitgelieferten Keystore für weitere Verwendung zu speichern");
		saveKeypairButton.setLayoutData(gd8);
		saveKeypairButton.setSelection(data.isStandalone());
		saveKeypairButton.setEnabled(!data.isStandalone());
		saveKeypairButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
			}

			public void widgetSelected(final SelectionEvent e) {
				getContainer().updateButtons();
			}
		});

		// fill in old data
		if (data.getX() != null) {
			lField.setText(data.getP().bitLength() + "");
			nField.setText(data.getQ().bitLength() + "");
			pField.setText(data.getP().toString());
			qField.setText(data.getQ().toString());
			gField.setText(data.getGenerator().toString());
			xfield.setText(data.getX().toString());
		}

		// finish
		setControl(composite);
	}

	@Override
	public final IWizardPage getNextPage() {
		if (saveKeypairButton.getSelection()) {
			return super.getNextPage();
		} else {
			return null;
		}
	}

	@Override
	public void setPageComplete(final boolean complete) {
		if (complete) {
			data.setP(new BigInteger(lField.getText()));
			data.setGenerator(new BigInteger(qField.getText()));
			data.setX(new BigInteger(xfield.getText()));
			data.setY(new BigInteger(ytext.getText()));
		}
		super.setPageComplete(complete);
	}

	/**
	 * getter for the selection-status of the save-button.
	 * @return the selection-status
	 */
	public final boolean wantSave() {
		return saveKeypairButton.getSelection();
	}
}
