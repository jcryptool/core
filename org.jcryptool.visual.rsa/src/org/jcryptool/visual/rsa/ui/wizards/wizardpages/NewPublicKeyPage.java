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

import static org.jcryptool.visual.library.Lib.isPrime;

import java.math.BigInteger;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.library.BigSquareRoot;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.rsa.Messages;
import org.jcryptool.visual.rsa.RSAData;

/**
 * Wizardpage for entering the parameters of a new Public Key.
 * 
 * @author Michael Gaber
 */
public class NewPublicKeyPage extends WizardPage implements ModifyListener,
		VerifyListener {

	/**
	 * Runnable for setting the calculated N in
	 * {@link NewPublicKeyPage#modifyText(ModifyEvent)}
	 * 
	 * @author Michael Gaber
	 */
	private class CalcRunnable implements Runnable {

		/** the N to set. */
		private BigInteger n;
		private BigInteger e;

		@Override
		public void run() {
			calcNField.setText(n.toString());
			calcEField.setText(e.toString());
		}

		/**
		 * @param n
		 *            the n to set
		 */
		public void setN(BigInteger n) {
			this.n = n;
		}

		/**
		 * @param e
		 *            the e to set
		 */
		public void setE(BigInteger e) {
			this.e = e;
		}
	}

	/** unique pagename to get this page from inside a wizard. */
	private static final String PAGENAME = "New Public Key Page"; //$NON-NLS-1$

	/** title of this page, displayed in the head of the wizard. */
	private static final String TITLE = Messages.NewPublicKeyPage_choose_params;

	/** {@link CalcRunnable} for using later. */
	private final CalcRunnable calcRunnable = new CalcRunnable();

	/** data-object storing all relevant information about the algorithm. */
	private final RSAData data;

	/** field for entering the encryption-exponent. */
	private Text eField;

	/** field for entering the RSA-modulo. */
	private Text nField;

	/** selection whether this key should be saved. */
	private Button saveButton;

	/** Field for the calculated replacement-n. */
	private Text calcNField;

	/** Field for the calculated replacement-e. */
	private Text calcEField;

	/**
	 * Constructor for a new wizardpage getting the data object.
	 * 
	 * @param data
	 *            the data object
	 */
	public NewPublicKeyPage(RSAData data) {
		super(PAGENAME, TITLE, null);
		this.data = data;
		this.setDescription(Messages.NewPublicKeyPage_enter_params_text);
		setPageComplete(false);
	}

	/**
	 * Set up the UI stuff.
	 * 
	 * @param parent
	 *            the parent composite
	 */
	@Override
	public final void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		// do stuff like layout et al
		composite.setLayout(new GridLayout(5, false));

		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		label.setText(Messages.NewPublicKeyPage_choose_rsa_mod);

		label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		label.setText(Messages.NewPublicKeyPage_suggestion);

		// field for entering N
		nField = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd_nField = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_nField.widthHint = 150;
		nField.setLayoutData(gd_nField);
		nField.addModifyListener(this);
		nField.addVerifyListener(this);
		nField.setToolTipText(Messages.NewPublicKeyPage_enter_n_popup);

		// new label for <-
		label = new Label(composite, SWT.CENTER);
		label.setText("<-");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// button for moving the value from calcN to N
		Button moveButton = new Button(composite, SWT.PUSH);
		moveButton.setText(Messages.NewPublicKeyPage_use);
		moveButton.setToolTipText(Messages.NewPublicKeyPage_use_popup);
		moveButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		moveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				nField.setText(calcNField.getText());
			}
		});

		// new label for <-
		label = new Label(composite, SWT.CENTER);
		label.setText("<-");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// field for calculated N value
		calcNField = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		calcNField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
//		calcNField.setText("323");
		calcRunnable.setN(new BigInteger("323"));
		Display d = Display.getDefault();
		d.asyncExec(calcRunnable);

		//Please enter your encryption exponent e.
		label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		label.setText(Messages.NewPublicKeyPage_enter_e);

		// field for entering e
		eField = new Text(composite, SWT.SINGLE | SWT.BORDER);
		eField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		eField.addModifyListener(this);
		eField.addVerifyListener(this);
		eField.setToolTipText(Messages.NewPublicKeyPage_enter_e_popup);

		// new label for <-
		label = new Label(composite, SWT.CENTER);
		label.setText("<-");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// button for moving the value from calcE to e
		Button moveButton2 = new Button(composite, SWT.PUSH);
		moveButton2.setText(Messages.NewPublicKeyPage_use);
		moveButton2.setToolTipText(Messages.NewPublicKeyPage_use_popup);
		moveButton2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		moveButton2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				eField.setText(calcEField.getText());
			}
		});

		// new label for <-
		label = new Label(composite, SWT.CENTER);
		label.setText("<-");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// field for calculated e value
		calcEField = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		calcEField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
//		calcEField.setText("17");
		calcRunnable.setE(new BigInteger("17"));
		Display x = Display.getDefault();
		x.asyncExec(calcRunnable);

		// Separator
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).
			setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));

		// should this key be saved?
		saveButton = new Button(composite, SWT.CHECK);
		saveButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		saveButton.setText(Messages.NewPublicKeyPage_save_pubkey);
		saveButton.setToolTipText(Messages.NewPublicKeyPage_save_pubkey_popup);
		saveButton.setSelection(data.isStandalone());
		saveButton.setEnabled(!data.isStandalone());
		saveButton.addSelectionListener(new SelectionListener() {

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
		if (data.getN() != null) {
			nField.setText(data.getN().toString());
		}
		if (data.getE() != null) {
			eField.setText(data.getE().toString());
		}
		// finishing touch
		setControl(composite);
	}
	
	/**
	 * getter for the pagename constant for easy access.
	 * 
	 * @return the pagename
	 */
	public static String getPagename() {
		return PAGENAME;
	}

	@Override
	public final IWizardPage getNextPage() {
		if (wantSave()) {
			return super.getNextPage();
		} else {
			return null;
		}
	}

	@Override
	public final void modifyText(ModifyEvent evt) {
		final BigInteger n;
		BigInteger e;
		try {
			n = new BigInteger(nField.getText());
			/*
			 * if (n.compareTo(Constants.TWOFIVESIX) < 0) {
			 * setPageComplete(false); setErrorMessage(null);
			 * setErrorMessage(Messages.NewPublicKeyPage_error_n_lt_256); } else
			 */if (!isTwoComposite(n)) {
				setPageComplete(false);
				setErrorMessage(null);
				setErrorMessage(Messages.NewPublicKeyPage_error_n_not_p_by_q);
				suggestN(n);
			} else {
				setErrorMessage(null);
			}
			data.setN(n);
		} catch (NumberFormatException e1) {
			setPageComplete(false);
			data.setN(null);
		}
		try {
			e = new BigInteger(eField.getText());
			data.setE(e);
			boolean eok = e.compareTo(data.getN()) < 0;
			// check for valid e and n values (n >256, e<floor(sqrt(n)-1)^2
			if (data.getN().compareTo(Constants.TWOFIVESIX) > 0
					&& isTwoComposite(data.getN())
					&& e.compareTo(BigInteger.ONE) > 0
					&& (eok && e.compareTo(BigSquareRoot.get(data.getN())
							.toBigInteger().subtract(BigInteger.ONE).pow(2)) < 0)) {
				data.setD(null);
				data.setQ(null);
				data.setP(null);
				setPageComplete(true);
			}
			if (!eok) {
				String error = getErrorMessage();
				if (error == null) {
					error = ""; //$NON-NLS-1$
				} else {
					error += "\n"; //$NON-NLS-1$
				}
				setErrorMessage(error + Messages.NewPublicKeyPage_error_e_lt_1);
			}
		} catch (NumberFormatException e1) {
			setPageComplete(false);
		} catch (NullPointerException e2) {
			setPageComplete(false);
		}
	}

	/**
	 * @param n
	 */
	private void suggestN(final BigInteger n) {
		new Thread() {
			@Override
			public void run() {

				// TODO / BUG , nextProbablePrime doesnt work properly?
				// BigDecimal root = BigSquareRoot.get(n);
				// BigInteger possibleP =
				// root.toBigInteger().nextProbablePrime();
				// BigInteger possibleQ =
				// possibleP.add(BigInteger.ONE).nextProbablePrime();
				BigInteger possibleQ = new BigInteger("19");
				BigInteger possibleP = new BigInteger("17");

				BigInteger possibleN = possibleP.multiply(possibleQ);
				while (n.compareTo(Constants.TWOFIVESIX) < 0) {
					possibleQ = possibleQ.add(BigInteger.ONE)
							.nextProbablePrime();
				}
				calcRunnable.setN(possibleN);
				Display d = Display.getDefault();
				d.asyncExec(calcRunnable);
			}
		}.start();
	}

	/**
	 * checks whether a number is a composite of exactly two prime numbers and
	 * is not a square.
	 * 
	 * @param number
	 *            the number to check
	 * @return <code>true</code> if and only if the number is
	 *         <ol>
	 *         <li>a composite of two prime factors</li>
	 *         <li>these two prime factors are different</li>
	 *         </ol>
	 */
	private boolean isTwoComposite(BigInteger number) {
		if (number == null) {
			return false;
		}
		if (isPrime(number)) {
			return false;
		}
		if (!number.testBit(0)) {
			return isPrime(number.divide(new BigInteger("2"))); //$NON-NLS-1$
		}
		BigInteger j;
		BigInteger[] k;
		for (int i = 3; i <= BigSquareRoot.get(number).intValue(); i += 2) {
			j = new BigInteger("" + i); //$NON-NLS-1$
			k = number.divideAndRemainder(j);
			if (k[1].equals(BigInteger.ZERO)) {
				return !j.equals(k[0]) && isPrime(j) && isPrime(k[0]);
			}
		}
		return false;
	}

	@Override
	public final void verifyText(VerifyEvent e) {
		switch (e.keyCode) {
		case SWT.DEL:
		case SWT.BS:
			return;
		default:
			break;
		}
		// allow only digits and spaces as input
		if (!(e.text).matches(TextWizardPage.DIGIT)) {
			e.doit = false;
		}
	}

	/**
	 * getter for the status of the save button to be accessed externally.
	 * 
	 * @return whether the user wants to save the key
	 */
	public final boolean wantSave() {
		return saveButton.getSelection();
	}
}
