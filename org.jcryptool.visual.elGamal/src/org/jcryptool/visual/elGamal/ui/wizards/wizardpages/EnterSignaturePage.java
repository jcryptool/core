// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
 * @author Thorben Groos
 */
public class EnterSignaturePage extends TextWizardPage {
    
	/** unique pagename to get this page from inside a wizard. */
    private static final String PAGENAME = "Enter Signature Page"; //$NON-NLS-1$

    /** Common data object fore storing the entries. */
    private final ElGamalData data;

    /**
     * Constructor storing the data-object for further usage, setting title, description, and page complete status.
     *
     * @param data the data-object
     */
    public EnterSignaturePage(final ElGamalData data) {
    	super(PAGENAME, Messages.EnterSignaturePage_enter_signature, null);
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
        label.setText(Messages.EnterSignaturePage_signatureen);
        
        text = new Text(composite, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        text.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				String trimmed = text.getText().replaceAll(Lib.WHITESPACE, ""); //$NON-NLS-1$

				// This checks if the input has the form of
				// 1. an open bracket ([(])
				// 2. a hex value ([0-9A-Fa-f]+?)
				// 3. a comma ([,])
				// 4. a hex value ([0-9A-Fa-f]+?)
				// 5. a closed bracket ([)])
				if (trimmed.matches("[(][0-9A-Fa-f]+?[,][0-9A-Fa-f]+?[)]")) { //$NON-NLS-1$
					//Remove the brackets
					trimmed = trimmed.replaceAll("[(]", "").replaceAll("[)]", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					String[] rs = trimmed.split(","); //$NON-NLS-1$
					BigInteger r = new BigInteger(rs[0], Constants.HEXBASE);
					BigInteger s = new BigInteger(rs[1], Constants.HEXBASE);
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
						setPageComplete(true);
					}
				}
			}
		});
        text.addVerifyListener(Lib.getVerifyListener("[" + Lib.WHITESPACE + Lib.HEXDIGIT + "\\(\\),]*")); //$NON-NLS-1$ //$NON-NLS-2$

        // fill in old data
        text.setText(data.getSignature());


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
}
