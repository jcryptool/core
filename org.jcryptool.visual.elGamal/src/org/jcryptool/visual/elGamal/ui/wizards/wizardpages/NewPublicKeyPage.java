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
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

/**
 * Wizardpage for entering the parameters of a new Public Key.
 *
 * @author Michael Gaber
 */
public class NewPublicKeyPage extends WizardPage {

    /** unique pagename to get this page from inside a wizard. */
    private static final String PAGENAME = "New Public Key Page"; //$NON-NLS-1$

    /** title of this page, displayed in the head of the wizard. */
    private static final String TITLE = Messages.NewPublicKeyPage_select_params;

    /**
     * getter for the pagename constant for easy access.
     *
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
    private final ElGamalData data;

    /** selection whether this key should be saved. */
    private Button saveButton;

    /** field for entering the value for p */
    private Text ptext;

    /** field for entering the value for g */
    private Text gtext;

    /** field for entering the value for a */
    private Text atext;

    /** basic composite of this page */
    private Composite composite;

    /** field for suggestions for g */
    private Combo gfield;

    /**
     * Constructor for a new wizardpage getting the data object.
     *
     * @param data the data object
     */
    public NewPublicKeyPage(final ElGamalData data) {
        super(PAGENAME, TITLE, null);
        this.data = data;
        this.setDescription(Messages.NewPublicKeyPage_select_params_text);
        setPageComplete(false);
    }

    /**
     * Set up the UI stuff.
     *
     * @param parent the parent composite
     */
    public final void createControl(final Composite parent) {
        composite = new Composite(parent, SWT.NONE);
        // do stuff like layout et al
        final int ncol = 4;
        final GridLayout gl = new GridLayout(ncol, false);
        composite.setLayout(gl);
        // final GridData gd2 = new GridData(SWT.FILL, SWT.CENTER, true, false, ncol / 2, 1);

        Label label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1));
        label.setText(Messages.NewPublicKeyPage_select_p);
        new Label(composite, SWT.NONE).setText("p"); //$NON-NLS-1$
        ptext = new Text(composite, SWT.BORDER);
        ptext.addVerifyListener(VL);
        ptext.addModifyListener(ml);
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);

        label = new Label(composite, SWT.NONE);
        label.setText(Messages.NewPublicKeyPage_select_g);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1));
        new Label(composite, SWT.NONE).setText("g"); //$NON-NLS-1$
        gtext = new Text(composite, SWT.BORDER);
        gtext.addVerifyListener(VL);
        gtext.addModifyListener(ml);
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);

        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1));
        label.setText(Messages.NewPublicKeyPage_select_A);
        new Label(composite, SWT.NONE).setText("A"); //$NON-NLS-1$
        atext = new Text(composite, SWT.BORDER);
        atext.addVerifyListener(VL);
        atext.addModifyListener(ml);
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);

        // Separator
        new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1));

        // should this key be saved?
        saveButton = new Button(composite, SWT.CHECK);
        saveButton.setText(Messages.NewPublicKeyPage_save_pubkey);
        saveButton.setToolTipText(Messages.NewPublicKeyPage_save_pubkey_popup);
        saveButton.setSelection(data.isStandalone());
        saveButton.setEnabled(!data.isStandalone());
        saveButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, ncol, 1));
        saveButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(final SelectionEvent e) {
                // won't be called
            }

            public void widgetSelected(final SelectionEvent e) {
                getContainer().updateButtons();
            }
        });

        // fill in old data
        if (data.getPublicA() != null) {
            ptext.setText(data.getModulus().toString());
            gtext.setText(data.getGenerator().toString());
            atext.setText(data.getPublicA().toString());
        }

        // finishing touch
        setControl(composite);
    }

    /**
     * checks whether this page is completed and sets the status accordingly
     */
    private void setPageComplete() {
        setErrorMessage(null);
        if (!ptext.getText().equals("")) { //$NON-NLS-1$
            final BigInteger p = new BigInteger(ptext.getText());
            if (p.compareTo(Constants.TWOFIVESIX) < 0) {
                setErrorMessage(Messages.NewPublicKeyPage_error_p_lt_256);
            }
            if (!Lib.isPrime(p)) {
                setErrorMessage(Messages.NewPublicKeyPage_error_p_not_prime);
            }
            if (getErrorMessage() == null) {
                gfield = new Combo(composite, SWT.SINGLE | SWT.READ_ONLY);
                gfield.setItems(Lib.calcG(p).toArray(new String[0]));
            }
            if (!gtext.getText().equals("")) { //$NON-NLS-1$
                final BigInteger g = new BigInteger(gtext.getText());
                if (!Lib.checkGenerator(g, p)) {
                    setErrorMessage(Messages.NewPublicKeyPage_error_g_not_generator);
                }
            }
            if (!atext.getText().equals("")) { //$NON-NLS-1$
                final BigInteger a = new BigInteger(atext.getText());
                if (a.compareTo(p) > 0) {
                    setErrorMessage(Messages.NewPublicKeyPage_error_A_gt_p);
                }
            }
        }
        setPageComplete(getErrorMessage() == null);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.WizardPage#setPageComplete(boolean)
     */
    @Override
    public void setPageComplete(boolean complete) {
        if (complete) {
            data.setGenerator(new BigInteger(gtext.getText()));
            data.setModulus(new BigInteger(ptext.getText()));
            data.setPublicA(new BigInteger(atext.getText()));
        }
        super.setPageComplete(complete);
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
        if (wantSave()) {
            return super.getNextPage();
        } else {
            return null;
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
