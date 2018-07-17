// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.elGamal.ui.wizards.wizardpages;

import java.math.BigInteger;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.ElGamalPlugin;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

/**
 * Wizardpage for entering the parameters of a new Public Key.
 *
 * @author Michael Gaber
 * @author Thorben Groos
 */
public class NewPublicKeyPage extends WizardPage {

	/** verify listener for checking inputs. */
	private static final VerifyListener VL = Lib.getVerifyListener(Lib.DIGIT);

	private final ModifyListener ml = new ModifyListener() {
		@Override
		public void modifyText(final ModifyEvent e) {
			checkAndSetPageComplete();
		}
	};

	/** data-object storing all relevant information about the algorithm. */
	private final ElGamalData data;

	/** selection whether this key should be saved. */
	private Button saveButton;

	/** field for entering the value for p */
	private Text dtext;

	/** field for entering the value for g */
	private Text gtext;

	/** field for entering the value for a */
	private Text btext;

	/** basic composite of this page */
	private Composite composite;
	
	/** Note that is displayed when p is too long */
	private CLabel dTooLongLable;
	
	/** Lable that is displayed when g is too long */
	private CLabel gTooLongLable;
	
	/** Button sugges Generator */
	private Button suggestGenerator;
	/**
	 * Constructor for a new wizardpage getting the data object.
	 *
	 * @param data
	 *            the data object
	 */
	public NewPublicKeyPage(final ElGamalData data) {
		super("New Public Key Page", Messages.NewPublicKeyPage_select_params, null);
		this.data = data;
		this.setDescription(Messages.NewPublicKeyPage_select_params_text);
		setPageComplete(false);
	}

	/**
	 * Set up the UI stuff.
	 *
	 * @param parent
	 *            the parent composite
	 */
	@Override
	public final void createControl(final Composite parent) {
		// do stuff like layout et al
		composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.marginWidth = 50;
		composite.setLayout(gl_composite);

		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		label.setText(Messages.NewPublicKeyPage_select_p);
		new Label(composite, SWT.NONE).setText("d = "); //$NON-NLS-1$
		dtext = new Text(composite, SWT.BORDER);
		dtext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		dtext.addVerifyListener(VL);
		dtext.addModifyListener(ml);
		Button suggestPrime = new Button(composite, SWT.PUSH);
		suggestPrime.setText(Messages.NewPublicKeyPage_4);
		suggestPrime.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dtext.getText().equals("")) { //$NON-NLS-1$
					// Get a random Prime
					int rndm = (int) (Math.random() * 19500 + 257);
					dtext.setText(Integer.toString(Lib.POSSBLE_PS.lower(rndm)));
				} else {
					// Get a prime near the entered value
					dtext.setText(Integer.toString(Lib.POSSBLE_PS.lower(Integer.parseInt(dtext.getText()))));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		dTooLongLable = new CLabel(composite, WARNING);
		dTooLongLable.setText(Messages.NewPublicKeyPage_pProbablyNotPrime);
		dTooLongLable.setVisible(false);
		dTooLongLable.setToolTipText(Messages.NewPublicKeyPage_toolTippTooLong);
		dTooLongLable.setImage(ElGamalPlugin.getImageDescriptor("platform:/plugin/org.eclipse.jface/org/eclipse/jface/dialogs/images/message_info.png").createImage()); //$NON-NLS-1$

		Label separator1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_separator1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
		gd_separator1.verticalIndent = 10;
		separator1.setLayoutData(gd_separator1);
		label = new Label(composite, SWT.NONE);
		label.setText(Messages.NewPublicKeyPage_select_g);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
		gd_label.verticalIndent = 10;
		label.setLayoutData(gd_label);
		new Label(composite, SWT.NONE).setText("g = "); //$NON-NLS-1$
		gtext = new Text(composite, SWT.BORDER);
		gtext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		gtext.addVerifyListener(VL);
		gtext.addModifyListener(ml);

		suggestGenerator = new Button(composite, SWT.PUSH);
		suggestGenerator.setText(Messages.NewPublicKeyPage_7);
		suggestGenerator.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (gtext.getText().isEmpty()) { 
					// get a random generator between 200 and 15000
					if (dtext.getText().isEmpty()) {
						//Es wurde noch kein d eingegeben
						setErrorMessage(Messages.NewPublicKeyPage_enter_d_first);
					} else {
						// d wurde bereits eingegeben.
						int rnd = (int) (Math.random() * 15000 + 200);
						gtext.setText(getRandomGenerator(Integer.toString(rnd), dtext.getText()).toString());
					}
				} else {
					// Get a Generator near the entered value
					gtext.setText(getRandomGenerator(gtext.getText(), dtext.getText()).toString());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		gTooLongLable = new CLabel(composite, WARNING);
		gTooLongLable.setText(Messages.NewPublicKeyPage_gProbablyNoGenerator);
		gTooLongLable.setVisible(false);
		gTooLongLable.setToolTipText(Messages.NewPublicKeyPage_ToolTipgTooLong);
		gTooLongLable.setImage(ElGamalPlugin.getImageDescriptor("platform:/plugin/org.eclipse.jface/org/eclipse/jface/dialogs/images/message_info.png").createImage()); //$NON-NLS-1$

		Label separator2 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_separator2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
		gd_separator2.verticalIndent = 20;
		separator2.setLayoutData(gd_separator2);
		label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		label.setText(Messages.NewPublicKeyPage_select_A);
		GridData gd_label2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
		gd_label2.verticalIndent = 10;
		label.setLayoutData(gd_label2);
		new Label(composite, SWT.NONE).setText("B = "); //$NON-NLS-1$
		btext = new Text(composite, SWT.BORDER);
		btext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		btext.addVerifyListener(VL);
		btext.addModifyListener(ml);
		Button suggestB = new Button(composite, SWT.PUSH);
		suggestB.setText(Messages.NewPublicKeyPage_10);
		suggestB.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!dtext.getText().equals("")) { //$NON-NLS-1$
					// if p is set, set A to a value between 1 and p - 1
					int rnd = (int) (Math.random() * Integer.parseInt(dtext.getText()));
					btext.setText(Integer.toString(rnd));
				} else {
					// if p isn't set, set A to a number between 1 and 1000
					int rnd = (int) (Math.random() * 10000 + 1);
					btext.setText(Integer.toString(rnd));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		// Spacer
		new Label(composite, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));

		// Separator
		Label separator3 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_separator3 = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
		gd_separator3.verticalIndent = 10;
		separator3.setLayoutData(gd_separator3);

		// should this key be saved?
		saveButton = new Button(composite, SWT.CHECK);
		saveButton.setSelection(false);
		saveButton.setText(Messages.NewPublicKeyPage_save_pubkey);
		saveButton.setToolTipText(Messages.NewPublicKeyPage_save_pubkey_popup);
		saveButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				checkAndSetPageComplete();
			}
		});


		// fill in old data
		if (data.getPublicA() != null) {
			dtext.setText(data.getModulus().toString());
			gtext.setText(data.getGenerator().toString());
			btext.setText(data.getPublicA().toString());
		}
		
		//Select the save field and disable it for the user when it is standalone (only key creation)
		if (data.isStandalone()) {
			saveButton.setSelection(true);
			saveButton.setEnabled(false);
		}

		// finishing touch
		setControl(composite);
	}

	/**
	 * 
	 * @param generator
	 *            a value near a generator for the prime
	 * @param prime
	 *            a prime number
	 * @return a generator to the given prime
	 */
	protected BigInteger getRandomGenerator(String generator, String prime) {
		BigInteger d = new BigInteger(prime);
		BigInteger g = new BigInteger(generator);
		while (!Lib.checkGenerator(g, d)) {
			g = g.add(BigInteger.ONE);
		}
		return g;
	}

	/**
	 * checks whether this page is completed and sets the status accordingly
	 * @return True if all Parameters are set correctly
	 */
	private boolean checkAndSetPageComplete() {
		boolean pageComplete = true;
		setErrorMessage(null);
		if (!dtext.getText().isEmpty()) { 
			final BigInteger p = new BigInteger(dtext.getText());
			
			if (p.compareTo(Constants.TWOFIVESIX) < 0) {
				setErrorMessage(Messages.NewPublicKeyPage_error_p_lt_256);
				pageComplete = false;
			}
			
			//If p is too long don't check if it is prime and display the note.
			if (dtext.getText().length() > 6) {
				dTooLongLable.setVisible(true);
				suggestGenerator.setEnabled(false);
			} else {
				if (!Lib.isPrime(p)) {
					setErrorMessage(Messages.NewPublicKeyPage_error_p_not_prime);
					pageComplete = false;
				}
				dTooLongLable.setVisible(false);
				suggestGenerator.setEnabled(true);
			}

			if (!gtext.getText().equals("")) { //$NON-NLS-1$
				final BigInteger g = new BigInteger(gtext.getText());
				//if g is too long don't check if g is a valid generator for p
				if (gtext.getText().length() > 6 || dtext.getText().length() > 6) {
					gTooLongLable.setVisible(true);
				} else {
					if (!Lib.checkGenerator(g, p)) {
						setErrorMessage(Messages.NewPublicKeyPage_error_g_not_generator);
						pageComplete = false;
					}
					gTooLongLable.setVisible(false);
				}
			} else {
				pageComplete = false;
			}

			if (!btext.getText().equals("")) { //$NON-NLS-1$
				final BigInteger a = new BigInteger(btext.getText());
				if (a.compareTo(p) > 0) {
					setErrorMessage(Messages.NewPublicKeyPage_error_A_gt_p);
					pageComplete = false;
				}
			} else {
				pageComplete = false;
			}
		} else {
			pageComplete = false;
		}
		
		setPageComplete(pageComplete);
		return pageComplete;
	}

	@Override
	public void setErrorMessage(final String newMessage) {
		if (newMessage == null || getErrorMessage() == null) {
			super.setErrorMessage(newMessage);
		} else {
			super.setErrorMessage(getErrorMessage() + "\n" + newMessage); //$NON-NLS-1$
		}
	}

	@Override
	public final IWizardPage getNextPage() {
		if (saveButton.getSelection()) {
			return getWizard().getPage("Save Public Key Page");
		} else {
			return null;
		}
	}
	
	/**
	 * Return true if the page is complete and the user wants to save the key. 
	 * Else return false.
	 */
	@Override
	public boolean canFlipToNextPage() {
		if (isPageComplete() && wantSave()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * getter for the status of the save button to be accessed externally.
	 *
	 * @return whether the user wants to save the key
	 */
	public boolean wantSave() {
		return saveButton.getSelection();
	}

	/**
	 * @return the modulus d as BigInteger
	 * If something went wrong null is returned.
	 */
	public BigInteger getModulus() {
		try {
			return new BigInteger(dtext.getText());
		} catch (Exception e) {
			LogUtil.logError(ElGamalPlugin.PLUGIN_ID, e.getMessage());
		}
		return null;
	}

	/**
	 * @return the generator g
	 * If something went wrong null is returned.
	 */
	public BigInteger getGenerator() {
		try {
			return new BigInteger(gtext.getText());
		} catch (Exception e) {
			LogUtil.logError(ElGamalPlugin.PLUGIN_ID, e.getMessage());
		}
		return null;
	}

	/**
	 * @return the public Exponent B
	 * If something went wrong null is returned.
	 */
	public BigInteger getExponentB() {
		try {
			return new BigInteger(btext.getText());
		} catch (Exception e) {
			LogUtil.logError(ElGamalPlugin.PLUGIN_ID, e.getMessage());
		}
		return null;
	}
	
	
}
