// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.sigVerification.algorithm.Input;

/**
 * This class contains the GUI elements for signature wizard.
 * 
 * @author Wilfing
 */
public class SignatureComposite extends Composite implements SelectionListener {
    private Group grpSignatures;
    private Text txtDescription;
    private Button rdo1;
    private Button rdo2;
    private Button rdo3;
    private Button rdo4;
    private int method;
    private Menu menuSig;
    private MenuItem mntmSig;
    Input input;

    public SignatureComposite(Composite parent, int style, int m, SignatureWizardPage p, Input input) {
        super(parent, style);
        this.input = input;
        method = m;
        initialize();
    }

    /**
     * Draws all the controls of the composite
     */
    private void initialize() {
        grpSignatures = new Group(this, SWT.NONE);
        grpSignatures.setText(Messages.SignatureWizard_grpSignatures);
        grpSignatures.setBounds(10, 10, 406, 151);

        rdo1 = new Button(grpSignatures, SWT.RADIO);
        rdo1.setBounds(10, 19, 118, 18);
        rdo1.setText(Messages.SignatureWizard_DSA);

        rdo2 = new Button(grpSignatures, SWT.RADIO);
        rdo2.setBounds(10, 43, 118, 18);
        rdo2.setText(Messages.SignatureWizard_RSA);

        rdo3 = new Button(grpSignatures, SWT.RADIO);
        rdo3.setEnabled(true);
        rdo3.setBounds(10, 67, 118, 18);
        rdo3.setText(Messages.SignatureWizard_ECDSA);

        rdo4 = new Button(grpSignatures, SWT.RADIO);
        rdo4.setBounds(10, 91, 118, 18);
        rdo4.setText(Messages.SignatureWizard_RSAandMGF1);

        Group grpDescription = new Group(this, SWT.NONE);
        grpDescription.setText(Messages.SignatureWizard_grpDescription);
        grpDescription.setBounds(10, 220, 406, 255);

        txtDescription = new Text(grpDescription, SWT.WRAP | SWT.NO_BACKGROUND);
        txtDescription.setEditable(false);
        txtDescription.setBounds(10, 15, 382, 213);
        txtDescription.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
        txtDescription.setText(Messages.SignatureWizard_DSA_description);

        menuSig = new Menu(txtDescription);
        txtDescription.setMenu(menuSig);

        mntmSig = new MenuItem(menuSig, SWT.NONE);
        mntmSig.setText(Messages.Wizard_menu);
        // To select all text
        mntmSig.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                txtDescription.selectAll();
            }
        });

        rdo1.addSelectionListener(this);
        rdo2.addSelectionListener(this);
        rdo3.addSelectionListener(this);
        rdo4.addSelectionListener(this);

        // Enable/disable methods
        switch (method) {
        case 0: // MD5: RSA
            rdo2.setEnabled(true);
            rdo1.setEnabled(false);
            rdo3.setEnabled(false);
            rdo4.setEnabled(false);
            rdo2.setSelection(true);
            rdo1.setSelection(false);

            txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_RSA_description);
            input.s = 1;
            break;
        case 1: // SHA1: RSA, DSA, ECDSA, RSA + MGF1
            rdo1.setEnabled(true);
            rdo2.setEnabled(true);
            rdo3.setEnabled(true);
            rdo4.setEnabled(true);

            rdo1.setSelection(true);
            rdo2.setSelection(false);
            input.s = 0;
            txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_DSA_description);
            break;
        case 2:
        case 3:
        case 4: // SHA256+: RSA, ECDSA, RSA + MGF1
            rdo2.setEnabled(true);
            rdo3.setEnabled(true);
            rdo4.setEnabled(true);
            rdo1.setEnabled(false);

            rdo2.setSelection(true);
            rdo1.setSelection(false);
            input.s = 1;
            txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_RSA_description);
            break;
        }
    }

    /**
     * @return the grpSignatures
     */
    public Group getgrpSignatures() {
        return grpSignatures;
    }

    // Checks if the radio buttons have changed and updates the text and keys from the keystore
    public void widgetSelected(SelectionEvent e) {
        if (rdo1.getSelection()) {
            txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_DSA_description);
            // Store the chosen signature to keep the selected radio button for the next time the
            // wizard is opened
            input.s = 0;
        } else {
            if (rdo2.getSelection()) {
                txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_RSA_description);
                input.s = 1;
            } else {
                if (rdo3.getSelection()) {
                    txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_ECDSA_description);
                    input.s = 2;
                } else {
                    if (rdo4.getSelection()) {
                        txtDescription.setText(Messages.SignatureWizard_Usage
                                + Messages.SignatureWizard_RSAandMGF1_description);
                        input.s = 3;
                    }
                }
            }
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        switch (method) {
        case 0: // MD5: RSA
            input.s = 1;
            break;
        case 1: // SHA1: RSA, DSA, ECDSA, RSA + MGF1
            input.s = 0;
            break;
        case 2:
        case 3:
        case 4: // SHA256+: RSA, ECDSA, RSA + MGF1
            input.s = 1;
            break;
        }
    }
}
