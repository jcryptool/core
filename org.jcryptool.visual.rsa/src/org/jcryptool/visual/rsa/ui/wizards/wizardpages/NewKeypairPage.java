// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
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
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;
import org.jcryptool.visual.rsa.Messages;
import org.jcryptool.visual.rsa.RSAData;

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

    /** a {@link VerifyListener} instance that makes sure only digits are entered. */
    private static final VerifyListener VL = Lib.getVerifyListener(Lib.DIGIT);

    /** a {@link ModifyListener} instance that calls {@link #calcParams()} whenever a value is changed. */
    private final ModifyListener ml = new ModifyListener() {
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

    /**
     * Constructor, setting description completeness-status and data-object.
     *
     * @param data the data object to store the entered values
     */
    public NewKeypairPage(RSAData data) {
        super(PAGENAME, TITLE, null);
        this.setDescription(Messages.NewKeypairPage_choose_params_text + "\n"
                + Messages.NewKeypairPage_hard_calculations_text);
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
            n = new BigInteger(modulfield.getText());
        } catch (NumberFormatException e) {
        }
        try {
            p = new BigInteger(plist.getText());
        } catch (NumberFormatException e) {
        }
        try {
            q = new BigInteger(qlist.getText());
        } catch (NumberFormatException e) {
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
        if (!n.equals(Constants.MINUS_ONE) && n.compareTo(Constants.TWOFIVESIX) < 0) {
            String error = getErrorMessage();
            if (error == null) {
                error = ""; //$NON-NLS-1$
            } else {
                error += "\n"; //$NON-NLS-1$
            }
            setErrorMessage(error + Messages.NewKeypairPage_error_n_lt_256);
        }
        return p.compareTo(ZERO) > 0 && q.compareTo(ZERO) > 0 && n.compareTo(Constants.TWOFIVESIX) >= 0;
    }

    /**
     * set up the UI stuff.
     *
     * @param parent the parent composite
     */
    public final void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        // do stuff like layout et al
        // set layout
        int ncol = 4;
        GridLayout gl = new GridLayout(ncol, false);
        composite.setLayout(gl);
        GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
        GridData gd1 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
        GridData gd2 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
        GridData gd3 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
        GridData gd4 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
        GridData gd5 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);
        GridData gd6 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1);

        // "Wähle p und q"-text
        Label choosePrimes = new Label(composite, SWT.NONE);
        choosePrimes.setText(Messages.NewKeypairPage_eror_p_eq_q);
        choosePrimes.setLayoutData(gd);
        // p
        new Label(composite, SWT.NONE).setText("p"); //$NON-NLS-1$
        plist = new Combo(composite, SWT.NONE);
        fillPrimesTo(plist);
        plist.addModifyListener(ml);
        plist.addVerifyListener(VL);
        // q
        Label qtext = new Label(composite, SWT.NONE);
        qtext.setText("q"); //$NON-NLS-1$
        qtext.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        qlist = new Combo(composite, SWT.NONE);
        fillPrimesTo(qlist);
        qlist.addModifyListener(ml);
        qlist.addVerifyListener(VL);

        // Trennlinie
        new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd1);

        // RSA-Modul Text
        Label rsaModul = new Label(composite, SWT.NONE);
        rsaModul.setText(Messages.NewKeypairPage_n_result);
        rsaModul.setLayoutData(gd2);
        // N
        new Label(composite, SWT.NONE).setText("N"); //$NON-NLS-1$
        modulfield = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
        modulfield.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                checkParams();
            }
        });
        // phi(N)
        new Label(composite, SWT.NONE).setText("φ(N)"); //$NON-NLS-1$
        phinfield = new Text(composite, SWT.BORDER | SWT.READ_ONLY);

        // Trennline
        new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd3);

        // e wählen text
        Label selectetext = new Label(composite, SWT.NONE);
        selectetext.setText(Messages.NewKeypairPage_select_e);
        selectetext.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, ncol, 2));
        new Label(composite, SWT.NONE).setText("e"); //$NON-NLS-1$
        // e
        elist = new Combo(composite, SWT.READ_ONLY | SWT.SIMPLE);
        elist.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                dfield.setText(calcd().toString());
            }
        });
        // elist.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

        elistUpdate = new Button(composite, SWT.PUSH);
        elistUpdate.setText(Messages.NewKeypairPage_whole_list);
        elistUpdate.setToolTipText(Messages.NewKeypairPage_whole_list_popup);
        elistUpdate.setEnabled(false);
        elistUpdate.setVisible(false);
        elistUpdate.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                elist.setItems((String[]) elistUpdate.getData());
                elistUpdate.setEnabled(false);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // won't be called
            }
        });

        // d Text
        Label selectdtext = new Label(composite, SWT.NONE);
        selectdtext.setText(Messages.NewKeypairPage_e_text);
        selectdtext.setLayoutData(gd4);
        // d
        new Label(composite, SWT.NONE).setText("d"); //$NON-NLS-1$
        dfield = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
        dfield.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setPageComplete(!dfield.getText().equals("")); //$NON-NLS-1$
            }
        });

        // Trennlinie
        new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd5);

        // speichern?
        saveKeypairButton = new Button(composite, SWT.CHECK);
        saveKeypairButton.setText(Messages.NewKeypairPage_save_keypair);
        saveKeypairButton.setToolTipText(Messages.NewKeypairPage_save_keypair_popup);
        saveKeypairButton.setLayoutData(gd6);
        saveKeypairButton.setSelection(data.isStandalone());
        saveKeypairButton.setEnabled(!data.isStandalone());
        saveKeypairButton.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                // won't be called
            }

            public void widgetSelected(SelectionEvent e) {
                getContainer().updateButtons();
            }
        });

        // fill in old data
        fillIn();

        // finish
        setControl(composite);
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
                 * @param list the list of items to add to elist
                 * @param intermediate whether this result is an intermediate one, so enable the transfer-button
                 */
                private KeyRunnable(String[] list, boolean intermediate) {
                    this.newList = list;
                    this.intermediate = intermediate;
                }

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
             * transfers the given list of items to an array of strings, creates a new Keyrunnable and starts it using
             * the {@link Display#asyncExec(Runnable)} Method.
             *
             * @param list the list of items to set
             * @param intermediate whether this result is an intermediate one, so enable the transfer-button
             */
            private void fillToE(Set<BigInteger> list, boolean intermediate) {
                List<String> newList = new LinkedList<String>();
                for (BigInteger integer : list) {
                    newList.add(integer.toString());
                }
                Display.getDefault().asyncExec(
                        new KeyRunnable(newList.toArray(new String[newList.size()]), intermediate));
            }
        }.start();
    }

    /**
     * fills in the already entered data from the last wizard run.
     */
    private void fillIn() {
        if (data.getN() != null) {
            plist.setText(data.getP().toString());
            qlist.setText(data.getQ().toString());
            elist.setText(data.getE().toString());
        }
    }

    /**
     * enters all primes into the given combo item.
     *
     * @param combo the list from which a prime can be selected
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

    // @Override
    // public final void setVisible(boolean visible) {
    // super.setVisible(visible);
    // if (visible) {
    // // display a warning Box regarding calculation time for all possible e's
    // MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING | SWT.OK);
    // box.setText(Messages.NewKeypairPage_hard_calculations);
    // box
    // .setMessage(Messages.NewKeypairPage_hard_calculations_text);
    // box.open();
    // }
    // }

    /**
     * getter for the selection-status of the save-button.
     *
     * @return the selection-status
     */
    public final boolean wantSave() {
        return saveKeypairButton.getSelection();
    }
}
