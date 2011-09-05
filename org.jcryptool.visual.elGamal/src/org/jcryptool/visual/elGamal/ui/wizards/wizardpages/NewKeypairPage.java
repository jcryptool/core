// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.elGamal.ui.wizards.wizardpages;

import static java.math.BigInteger.ONE;
import static org.jcryptool.visual.library.Constants.TWOFIVESIX;

import java.math.BigInteger;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

/**
 * Wizardpage for creating a new RSA public-private-keypair.
 *
 * @author Michael Gaber
 */
public class NewKeypairPage extends WizardPage {

    /** unique pagename to get this page from inside a wizard. */
    private static final String PAGENAME = "New Keypair Page"; //$NON-NLS-1$

    /** title of this page, displayed in the head of the wizard. */
    private static final String TITLE = Messages.NewKeypairPage_choose_params;

    // /**
    // * set up how many generators should be produced. might be that the actual count differs. this depends on the
    // * implementation and the modulus.
    // */
    //	private static final BigInteger RESULTCOUNT = new BigInteger("50"); //$NON-NLS-1$

    /**
     * a {@link VerifyListener} instance that makes sure only digits are entered.
     */
    private static final VerifyListener VL = Lib.getVerifyListener(Lib.DIGIT);

    // /**
    // * a {@link ModifyListener} instance that calls {@link #calcParams()} whenever a value is changed.
    // */
    // private final ModifyListener ml = new ModifyListener() {
    // public void modifyText(final ModifyEvent e) {
    // // calcParams();
    // }
    // };

    /**
     * getter for the pagename constant for easy access.
     *
     * @return the pagename
     */
    public static String getPagename() {
        return PAGENAME;
    }

    /** shared data-object to push around. */
    private final ElGamalData data;

    /** Selection whether the user wants to save the new Keypair. */
    private Button saveKeypairButton;

    /** Drop-Down for selecting the p values. */
    private Combo pfield;

    /** field to enter q */
    private Text qfield;

    /** field for selecting a g */
    private Combo gfield;

    /** field for entering the private a */
    private Text afield;

    /** field for entering the public a */
    private Text atext;

    /**
     * Constructor, setting description completeness-status and data-object.
     *
     * @param data the data object to store the entered values
     */
    public NewKeypairPage(final ElGamalData data) {
        super(PAGENAME, TITLE, null);
        this.setDescription(Messages.NewKeypairPage_choose_params_text);
        this.data = data;
        setPageComplete(false);
    }

    /**
     * set up the UI stuff.
     *
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
        final GridData gd9 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
        // begin stuff
        // modulus
        Label label = new Label(composite, SWT.NONE);
        label.setLayoutData(gd);
        label.setText(Messages.NewKeypairPage_choose_p_text);
        label = new Label(composite, SWT.NONE);
        label.setLayoutData(gd1);
        label.setText(Messages.NewKeypairPage_choose_q_text);
        new Label(composite, SWT.NONE).setText("p"); //$NON-NLS-1$
        pfield = new Combo(composite, SWT.SINGLE);
        pfield.addVerifyListener(VL);
        pfield.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                qfield.setText(""); //$NON-NLS-1$
            }
        });
        pfield.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                final BigInteger modulus = new BigInteger(pfield.getText());
                final String error = getErrorMessage();
                if (!Lib.isPrime(modulus)) {
                    if (error == null) {
                        setErrorMessage(Messages.NewKeypairPage_error_p_not_prime);
                    } else {
                        setErrorMessage(error + "\n" //$NON-NLS-1$
                                + Messages.NewKeypairPage_error_p_not_prime);
                    }
                } else {
                    setErrorMessage(null);
                }
                if (modulus.compareTo(TWOFIVESIX) <= 0) {
                    setErrorMessage(Messages.NewKeypairPage_error_p_lt_256);
                }
                afield.setText(""); //$NON-NLS-1$
                gfield.setItems(Lib.calcG(modulus).toArray(new String[0]));
            }
        });
        fillP();
        new Label(composite, SWT.NONE).setText("q"); //$NON-NLS-1$
        qfield = new Text(composite, SWT.BORDER);
        qfield.addVerifyListener(VL);
        qfield.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                if (qfield.getText().equals("")) { //$NON-NLS-1$
                    return;
                }
                final BigInteger q = new BigInteger(qfield.getText());
                if (Lib.isPrime(q)) {
                    setErrorMessage(null);
                    pfield.setText(q.multiply(Constants.TWO).add(ONE).toString());
                } else {
                    setErrorMessage(Messages.NewKeypairPage_error_q_not_prime);
                }
            }
        });
        // Separator
        new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd2);
        // primitive root
        label = new Label(composite, SWT.NONE);
        label.setText(Messages.NewKeypairPage_select_g);
        label.setLayoutData(gd3);
        label = new Label(composite, SWT.NONE);
        label.setLayoutData(gd4);
        label.setText(Messages.NewKeypairPage_real_g_values);
        new Label(composite, SWT.NONE).setText("g"); //$NON-NLS-1$
        gfield = new Combo(composite, SWT.SINGLE | SWT.READ_ONLY);
        // Separator
        new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd5);
        // a
        label = new Label(composite, SWT.NONE);
        label.setText(Messages.NewKeypairPage_select_a);
        label.setLayoutData(gd6);
        label = new Label(composite, SWT.NONE);
        label.setText(Messages.NewKeypairPage_A_explanation);
        label.setLayoutData(gd7);
        new Label(composite, SWT.NONE).setText("a"); //$NON-NLS-1$
        afield = new Text(composite, SWT.SINGLE | SWT.BORDER);
        afield.addVerifyListener(VL);
        afield.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (afield.getText() != "") { //$NON-NLS-1$
                    final BigInteger a = new BigInteger(afield.getText());
                    if (a.compareTo(Constants.TWO) < 0
                            || a.compareTo(new BigInteger(pfield.getText()).subtract(Constants.TWO)) > 0) {
                        setErrorMessage(Messages.NewKeypairPage_error_invalid_a);
                        setPageComplete(false);
                    } else {
                        atext.setText(new BigInteger(gfield.getText()).modPow(a, new BigInteger(pfield.getText()))
                                .toString());
                        setErrorMessage(null);
                        setPageComplete(true);
                    }
                } else {
                    setPageComplete(false);
                }
            }
        });

        new Label(composite, SWT.NONE).setText("A"); //$NON-NLS-1$
        atext = new Text(composite, SWT.READ_ONLY | SWT.BORDER);

        // Separator
        new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd8);
        // Save?
        saveKeypairButton = new Button(composite, SWT.CHECK);
        saveKeypairButton.setText(Messages.NewKeypairPage_save_keypair);
        saveKeypairButton.setToolTipText(Messages.NewKeypairPage_save_keypair_popup);
        saveKeypairButton.setLayoutData(gd9);
        saveKeypairButton.setSelection(data.isStandalone());
        saveKeypairButton.setEnabled(!data.isStandalone());
        saveKeypairButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                getContainer().updateButtons();
            }
        });

        // fill in old data
        if (data.getA() != null) {
            pfield.setText(data.getModulus().toString());
            gfield.setText(data.getGenerator().toString());
            afield.setText(data.getA().toString());
        }

        // finish
        setControl(composite);
    }

    private void fillP() {
        for (final Integer i : Lib.POSSBLE_PS) {
            pfield.add(i.toString());
        }
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
            data.setModulus(new BigInteger(pfield.getText()));
            data.setGenerator(new BigInteger(gfield.getText()));
            data.setA(new BigInteger(afield.getText()));
            data.setPublicA(new BigInteger(atext.getText()));
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
