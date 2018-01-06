// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.rsa.ui.wizards.wizardpages;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.jcryptool.visual.library.Lib.LOW_PRIMES;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;
import org.jcryptool.visual.rsa.Messages;
import org.jcryptool.visual.rsa.RSAData;
import org.jcryptool.visual.rsa.RSAPlugin;

/**
 * Wizardpage for creating a new RSA public-private-keypair.
 * 
 * @author Michael Gaber
 */
public class NewKeypairPage extends WizardPage {

	/** Button to load the whole list of e's to the combo. */
	private Button elistUpdate;

	/** unique pagename to get this page from inside a wizard. */
	private static final String PAGENAME = "New Keypair Page"; //$NON-NLS-1$

	/** title of this page, displayed in the head of the wizard. */
	private static final String TITLE = Messages.NewKeypairPage_choose_params;

	/**
	 * a {@link VerifyListener} instance that makes sure only digits are entered.
	 */
	private static final VerifyListener VL = Lib.getVerifyListener(Lib.DIGIT);

	/**
	 * a {@link ModifyListener} instance that calls {@link #calcParams()} whenever a
	 * value is changed.
	 */
	private final ModifyListener ml = new ModifyListener() {
		@Override
		public void modifyText(ModifyEvent e) {
			calcParams();
		}
	};

	/**
	 * getter for the pagename constant for easy access.
	 * 
	 * @return the pagename
	 */
	public static String getPagename() {
		return PAGENAME;
	}

	/** shared data-object to push around. */
	private final RSAData data;

	/** field for showing the chosen d. */
	private Text dfield;

	/** selection field for a possible e. */
	private Combo elist;

	/** field for showing the calculated N. */
	private Text modulfield;

	/** field for showing the calculated φ(N). */
	private Text phinfield;

	/** list for choosing or entering p. */
	private Combo plist;

	/** list for choosing or entering q. */
	private Combo qlist;

	/** checkbox to choose whether the generated key should be saved. */
	private Button saveKeypairButton;

	/** storage space for the φ(N). */
	private BigInteger phin;

	protected Integer lastValidE;

	/**
	 * Constructor, setting description completeness-status and data-object.
	 * 
	 * @param data
	 *            the data object to store the entered values
	 */
	public NewKeypairPage(RSAData data) {
		super(PAGENAME, TITLE, null);
		this.setDescription(Messages.NewKeypairPage_choose_params_text);
		setPageComplete(false);
		this.data = data;
	}

	/**
	 * calculates d so that e∙d≡1 mod φ(N) holds.
	 * 
	 * @return the d matching the current e
	 */
	private BigInteger calcd() {
		BigInteger e = new BigInteger(elist.getText());
		BigInteger d = Lib.exteucl(e, phin)[1];
		if (d.compareTo(ZERO) < 0) {
			d = d.add(phin);
		}
		return d;
	}

	/**
	 * calculates the parameters N, φ(N) and a list of possible e's.
	 */
	private void calcParams() {
		try {
			BigInteger p = new BigInteger(plist.getText());
			BigInteger q = new BigInteger(qlist.getText());
			if (!p.equals(q)) {
				modulfield.setText(p.multiply(q).toString());
				phin = Lib.calcPhi(p, q);
				phinfield.setText(phin.toString());
				elist.removeAll();
				dfield.setText(""); //$NON-NLS-1$
				fillElist();
			} else {
				modulfield.setText(""); //$NON-NLS-1$
				phinfield.setText(""); //$NON-NLS-1$
				elist.removeAll();
				dfield.setText(""); //$NON-NLS-1$
				setPageComplete(false);
				setErrorMessage(Messages.NewKeypairPage_error_p_equals_q);
			}
		} catch (NumberFormatException e) {
			modulfield.setText(""); //$NON-NLS-1$
			phinfield.setText(""); //$NON-NLS-1$
			elist.removeAll();
			dfield.setText(""); //$NON-NLS-1$
			setPageComplete(false);
		}
	}

	/**
	 * checks whether p q and n are all valid and sets error-messages accordingly.
	 * 
	 * @return whether all parameters are correct
	 */
	private boolean checkParams() {
		BigInteger p = Constants.MINUS_ONE;
		BigInteger q = Constants.MINUS_ONE;
		BigInteger n = Constants.MINUS_ONE;
		// expected exceptions, so don't do anything about it
		try {
			if (!(modulfield.getText() == null || modulfield.getText().equals(""))) {
				n = new BigInteger(modulfield.getText());
			}
		} catch (NumberFormatException e) {
			LogUtil.logError(e);
		}
		try {
			if (!(plist.getText() != null || plist.getText().equals(""))) {
				p = new BigInteger(plist.getText());
			}
		} catch (NumberFormatException e) {
			LogUtil.logError(e);
		}
		try {
			if (!(qlist.getText() != null || qlist.getText().equals(""))) {
				q = new BigInteger(qlist.getText());
			}

		} catch (NumberFormatException e) {
			LogUtil.logError(e);
		}
		setErrorMessage(null);
		if (!p.equals(Constants.MINUS_ONE) && !Lib.isPrime(p)) {
			setErrorMessage(Messages.NewKeypairPage_error_p_not_prime);
		}
		if (!q.equals(Constants.MINUS_ONE) && !Lib.isPrime(q)) {
			String error = getErrorMessage();
			if (error != null) {
				error += "\n"; //$NON-NLS-1$
			}
			setErrorMessage(error + Messages.NewKeypairPage_error_q_not_prime);
		}
		// if (!n.equals(Constants.MINUS_ONE) &&
		// n.compareTo(Constants.TWOFIVESIX) < 0) {
		// String error = getErrorMessage();
		// if (error == null) {
		// error = ""; //$NON-NLS-1$
		// } else {
		// error += "\n"; //$NON-NLS-1$
		// }
		// setErrorMessage(error + Messages.NewKeypairPage_error_n_lt_256);
		// }
		return p.compareTo(ZERO) > 0 && q.compareTo(ZERO) > 0/*
																 * && n.compareTo(Constants .TWOFIVESIX) >= 0
																 */;
	}

	/**
	 * set up the UI stuff.
	 * 
	 * @param parent
	 *            the parent composite
	 */
	@Override
	public final void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		// do stuff like layout et al

		CLabel caution = new CLabel(composite, SWT.WRAP);
		caution.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		caution.setText(Messages.NewKeypairPage_hard_calculations_text);
		caution.setImage(RSAPlugin
				.getImageDescriptor("platform:/plugin/org.eclipse.jface/icons/full/message_warning.png").createImage());

		Label separator1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Composite primes = new Composite(composite, SWT.NONE);
		primes.setLayout(new GridLayout(4, false));
		primes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// "Wähle p und q"-text
		Label choosePrimes = new Label(primes, SWT.NONE);
		choosePrimes.setText(Messages.NewKeypairPage_eror_p_eq_q);
		choosePrimes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		// p
		Label pLabel = new Label(primes, SWT.NONE);
		pLabel.setText("p=");

		plist = new Combo(primes, SWT.NONE);
		GridData gd_plist = new GridData();
		gd_plist.widthHint = 150;
		plist.setLayoutData(gd_plist);
		fillPrimesTo(plist);
		plist.addModifyListener(ml);
		plist.addVerifyListener(VL);
		// q
		Label qtext = new Label(primes, SWT.NONE);
		GridData gd_qtext = new GridData();
		gd_qtext.horizontalIndent = 20;
		qtext.setLayoutData(gd_qtext);
		qtext.setText("q="); //$NON-NLS-1$

		qlist = new Combo(primes, SWT.NONE);
		GridData gd_qlist = new GridData();
		gd_qlist.widthHint = 150;
		qlist.setLayoutData(gd_qlist);
		fillPrimesTo(qlist);
		qlist.addModifyListener(ml);
		qlist.addVerifyListener(VL);

		// Trennlinie
		Label separator2 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Composite rsaModulus = new Composite(composite, SWT.NONE);
		rsaModulus.setLayout(new GridLayout(4, false));
		rsaModulus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// RSA-Modul Text
		Label rsaModul = new Label(rsaModulus, SWT.NONE);
		rsaModul.setText(Messages.NewKeypairPage_n_result);
		rsaModul.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));

		// N
		Label nLabel = new Label(rsaModulus, SWT.NONE);
		nLabel.setText("N=");

		modulfield = new Text(rsaModulus, SWT.BORDER | SWT.READ_ONLY);
		modulfield.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				checkParams();
			}
		});

		// phi(N)
		Label phiLabel = new Label(rsaModulus, SWT.NONE);
		GridData gd_phiLabel = new GridData();
		gd_phiLabel.horizontalIndent = 20;
		phiLabel.setLayoutData(gd_phiLabel);
		phiLabel.setText(Messages.NewKeypairPage_phi_n);

		phinfield = new Text(rsaModulus, SWT.BORDER | SWT.READ_ONLY);

		// Trennline
		Label separator3 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Composite exponents = new Composite(composite, SWT.NONE);
		exponents.setLayout(new GridLayout(3, false));
		exponents.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// e wählen text
		Label selectetext = new Label(exponents, SWT.NONE);
		selectetext.setText(Messages.NewKeypairPage_select_e);
		selectetext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		Label eLabel = new Label(exponents, SWT.NONE);
		eLabel.setText("e=");
		eLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		// e
		elist = new Combo(exponents, SWT.READ_ONLY | SWT.SIMPLE);
		GridData gd_elist = new GridData(SWT.DEFAULT, SWT.FILL, false, true);
		gd_elist.minimumHeight = 100;
		elist.setLayoutData(gd_elist);
		elist.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				lastValidE = new BigInteger(elist.getText()).intValue();
				dfield.setText(calcd().toString());
			}
		});
		elist.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					Integer enteredNumber = Integer.parseInt(elist.getText());
					boolean isValid = phin != null && isValidNumberForE(enteredNumber, phin);
					if (isValid) {
						dfield.setText(calcd().toString());
						lastValidE = enteredNumber;
					} else {
						dfield.setText("");
					}
				} catch (NumberFormatException e2) {
					dfield.setText("");
				}
			}
		});

		elistUpdate = new Button(exponents, SWT.PUSH);
		elistUpdate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
		elistUpdate.setText(Messages.NewKeypairPage_whole_list);
		elistUpdate.setToolTipText(Messages.NewKeypairPage_whole_list_popup);
		elistUpdate.setEnabled(false);
		elistUpdate.setVisible(false);
		elistUpdate.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				elist.setItems((String[]) elistUpdate.getData());
				elistUpdate.setEnabled(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// won't be called
			}
		});

		// d Text
		Label selectdtext = new Label(exponents, SWT.NONE);
		selectdtext.setText(Messages.NewKeypairPage_e_text);
		selectdtext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		// d
		Label dLabel = new Label(exponents, SWT.NONE);
		dLabel.setText("d=");
		dfield = new Text(exponents, SWT.BORDER | SWT.READ_ONLY);
		dfield.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				try {
					int eNr = Integer.parseInt(elist.getText());
					setPageComplete(!dfield.getText().equals("") && (isValidNumberForE(eNr, phin))); //$NON-NLS-1$
				} catch (NumberFormatException nrEx) {
					// e could not be parsed -> no completion
					return;
				}
			}
		});

		// Trennlinie
		Label separator4 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// speichern?
		saveKeypairButton = new Button(composite, SWT.CHECK);
		saveKeypairButton.setText(Messages.NewKeypairPage_save_keypair);
		saveKeypairButton.setToolTipText(Messages.NewKeypairPage_save_keypair_popup);
		saveKeypairButton.setSelection(data.isStandalone());
		saveKeypairButton.setEnabled(!data.isStandalone());
		saveKeypairButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// won't be called
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().updateButtons();
			}
		});

		// fill in old data
		fillIn();

		// finish
		setControl(composite);

	}

	protected boolean isValidNumberForE(Integer e, BigInteger phin2) {
		int ph = phin2.intValue();
		return e > 1 && gcdThing(e, ph) == 1 && e <= ph;
	}

	private static int gcdThing(int a, int b) {
		BigInteger b1 = new BigInteger("" + a); // there's a better way to do this. I forget.
		BigInteger b2 = new BigInteger("" + b);
		BigInteger gcd = b1.gcd(b2);
		return gcd.intValue();
	}

	/**
	 * fills the list of possible e values for selection.
	 */
	private void fillElist() {
		elistUpdate.setVisible(false);
		new Thread() {

			/**
			 * Runnable for accessing the user interface
			 * 
			 * @author Michael Gaber
			 */
			final class KeyRunnable implements Runnable {

				/** itemcount that is added directly to the list */
				private static final int TRIGGERLENGTH = 1000;

				/** list of items to add */
				private final String[] newList;

				private final boolean intermediate;

				/**
				 * Constructor sets the list
				 * 
				 * @param list
				 *            the list of items to add to elist
				 * @param intermediate
				 *            whether this result is an intermediate one, so enable the
				 *            transfer-button
				 */
				private KeyRunnable(String[] list, boolean intermediate) {
					this.newList = list;
					this.intermediate = intermediate;
				}

				@Override
				public void run() {
					if (newList.length <= TRIGGERLENGTH) {
						elist.setItems(newList);
						elistUpdate.setData(null);
						elistUpdate.setEnabled(false);
						elistUpdate.setVisible(intermediate);
					} else {
						elistUpdate.setData(newList);
						elistUpdate.setEnabled(true);
					}
					if (data.getE() != null) {
						elist.setText(data.getE().toString());
						dfield.setText(data.getD().toString());
					}
				}
			}

			@Override
			public void run() {
				Set<BigInteger> tempEList = new TreeSet<BigInteger>();
				BigInteger ih;
				for (int i = 2; i < phin.intValue(); i++) {
					ih = new BigInteger("" + i); //$NON-NLS-1$
					if (phin.gcd(ih).equals(ONE)) {
						if (tempEList.size() == KeyRunnable.TRIGGERLENGTH) {
							fillToE(tempEList, true);
						}
						tempEList.add(ih);
					}
				}
				fillToE(tempEList, false);

			}

			/**
			 * transfers the given list of items to an array of strings, creates a new
			 * Keyrunnable and starts it using the {@link Display#asyncExec(Runnable)}
			 * Method.
			 * 
			 * @param list
			 *            the list of items to set
			 * @param intermediate
			 *            whether this result is an intermediate one, so enable the
			 *            transfer-button
			 */
			private void fillToE(Set<BigInteger> list, boolean intermediate) {
				List<String> newList = new LinkedList<String>();
				for (BigInteger integer : list) {
					newList.add(integer.toString());
				}
				Display.getDefault()
						.asyncExec(new KeyRunnable(newList.toArray(new String[newList.size()]), intermediate));
			}
		}.start();
	}

	/**
	 * fills in the already entered data from the last wizard run.
	 */
	private void fillIn() {
		if (data.getN() != null && data.getP() != null && data.getQ() != null && data.getE() != null) {
			plist.setText(data.getP().toString());
			qlist.setText(data.getQ().toString());
			elist.setText(data.getE().toString());
		}
	}

	/**
	 * enters all primes into the given combo item.
	 * 
	 * @param combo
	 *            the list from which a prime can be selected
	 */
	private void fillPrimesTo(Combo combo) {
		for (Integer i : LOW_PRIMES) {
			combo.add(i.toString());
		}
	}

	@Override
	public final IWizardPage getNextPage() {
		if (saveKeypairButton.getSelection())
			return super.getNextPage();
		else
			return null;
	}

	@Override
	public final void setPageComplete(boolean complete) {
		if (complete) {
			data.setP(new BigInteger(plist.getText()));
			data.setQ(new BigInteger(qlist.getText()));
			data.setE(new BigInteger(elist.getText()));
			data.setN(new BigInteger(modulfield.getText()));
			data.setD(new BigInteger(dfield.getText()));
		}
		super.setPageComplete(complete);
	}

	/**
	 * getter for the selection-status of the save-button.
	 * 
	 * @return the selection-status
	 */
	public final boolean wantSave() {
		return saveKeypairButton.getSelection();
	}
}
