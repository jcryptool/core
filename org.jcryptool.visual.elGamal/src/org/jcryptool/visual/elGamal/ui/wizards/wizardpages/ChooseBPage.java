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

import org.eclipse.jface.wizard.WizardPage;
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
import org.jcryptool.visual.library.Lib;

/**
 * Page for choosing the unique parameter b
 *
 * @author Michael Gaber
 */
public class ChooseBPage extends WizardPage {

    /** pagename for access */
    private static final String PAGENAME = "Choose B Page"; //$NON-NLS-1$

    /** title of this page */
    private static final String TITLE = Messages.ChooseBPage_choose_b;

    /** shared data object */
    private final ElGamalData data;

    /**
     * Constructor setting {@link #data}
     *
     * @param data the {@link #data}
     */
    public ChooseBPage(final ElGamalData data) {
        super(PAGENAME, TITLE, null);
        this.data = data;
        setPageComplete(false);
    }

    public void createControl(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        final int ncol = 2;
        composite.setLayout(new GridLayout(ncol, true));
        Label l = new Label(composite, SWT.WRAP);
        l.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, ncol, 1));
        l.setText(Messages.ChooseBPage_choose_b_text);
        l = new Label(parent, SWT.NONE);
        l.setText("b"); //$NON-NLS-1$
        final Text t = new Text(composite, SWT.BORDER);
        t.addVerifyListener(Lib.getVerifyListener(Lib.DIGIT));
        t.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                setErrorMessage(null);
                if (t.getText().equals("")) { //$NON-NLS-1$
                    return;
                }
                final BigInteger b = new BigInteger(t.getText());
                if (b.compareTo(data.getModulus().subtract(BigInteger.ONE)) < 0 && b.compareTo(BigInteger.ONE) > 0) {
                    data.setB(b);
                    setPageComplete(true);
                } else {
                    data.setB(null);
                    setErrorMessage(Messages.ChooseBPage_error_invalid_b);
                    setPageComplete(false);
                }
            }
        });

        setControl(composite);
    }
}
