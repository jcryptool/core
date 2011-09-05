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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

/**
 * Page for entering a signature. Optionally the plaintext can be entered , too and
 *
 * @author Michael Gaber
 */
public class EnterSignaturePage extends TextWizardPage {

    /** unique pagename to get this page from inside a wizard. */
    private static final String PAGENAME = "Enter Plaintext and Signature Page"; //$NON-NLS-1$

    /** title of this page, displayed in the head of the wizard. */
    private static final String TITLE = Messages.EnterSignaturePage_enter_signature;

    /** field for entering the plaintext. */
    private Text plaintext;

    /** Common data object fore storing the entries. */
    private final ElGamalData data;

    /**
     * Constructor storing the data-object for further usage, setting title, description, and page complete status.
     *
     * @param data the data-object
     */
    public EnterSignaturePage(final ElGamalData data) {
        super(PAGENAME, TITLE, null);
        this.setDescription(Messages.EnterSignaturePage_enter_signature_text);
        setPageComplete(false);
        this.data = data;
    }

    /**
     * Set up the UI stuff.
     *
     * @param parent the parent composite
     */
    public final void createControl(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        // do stuff like layout et al
        composite.setLayout(new GridLayout());
        Label label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText(Messages.EnterSignaturePage_textentry);
        plaintext = new Text(composite, SWT.BORDER);
        plaintext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        plaintext.addVerifyListener(Lib.getVerifyListener(Lib.CHARACTERS));

        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText(Messages.EnterSignaturePage_signatureen);
        text = new Text(composite, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        text.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                final String trimmed = text.getText().replaceAll(Lib.WHITESPACE, ""); //$NON-NLS-1$
                final boolean leer = trimmed.length() == 0;
                if (!leer) {
                    final String[] rs = trimmed.split(","); //$NON-NLS-1$
                    rs[0] = rs[0].substring(1);
                    rs[1] = rs[1].replace(')', ' ').trim();
                    final BigInteger r = new BigInteger(rs[0], Constants.HEXBASE);
                    final BigInteger s = new BigInteger(rs[1], Constants.HEXBASE);
                    data.setR(r);
                    if (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(data.getModulus()) >= 0) {
                        setErrorMessage(Messages.EnterSignaturePage_error_invalid_r);
                        setPageComplete(false);
                    } else if (s.compareTo(BigInteger.ZERO) <= 0
                            || s.compareTo(data.getModulus().subtract(BigInteger.ONE)) >= 0) {
                        setErrorMessage(Messages.EnterSignaturePage_error_invalid_s);
                        setPageComplete(false);
                    } else {
                        setErrorMessage(null);
                        setPageComplete(!leer);
                    }
                }
            }
        });
        text.addVerifyListener(Lib.getVerifyListener("[" + Lib.WHITESPACE + Lib.HEXDIGIT + "\\(\\),]*")); //$NON-NLS-1$ //$NON-NLS-2$

        final Button SHA1Checkbox = new Button(composite, SWT.CHECK);
        SHA1Checkbox.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        SHA1Checkbox.setText(Messages.EnterSignaturePage_use_sha1);
        SHA1Checkbox.setToolTipText(Messages.EnterSignaturePage_use_sha1_popup);
        SHA1Checkbox.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                data.setSimpleHash(!SHA1Checkbox.getSelection());
            }
        });

        // fill in old data
        text.setText(data.getSignature());
        plaintext.setText(data.getPlainText());
        SHA1Checkbox.setSelection(!data.getSimpleHash());

        // finish
        setControl(composite);
    }

    /**
     * getter for the pagename.
     *
     * @return the pagename
     */
    public static String getPagename() {
        return PAGENAME;
    }

    /**
     * getter for the contents of the plaintextfield.
     *
     * @return the content of the plaintextfield as string or the empty string if not set
     */
    public final String getPlaintext() {
        return plaintext.getText();
    }
}
