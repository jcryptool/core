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

import java.math.BigInteger;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;

/**
 * Page for entering the unique parameter k
 *
 * @author Michael Gaber
 */
public class ChooseKPage extends WizardPage {

    /** name of this page for referencing */
    private static final String PAGENAME = "k entry page"; //$NON-NLS-1$

    /** title of the page */
    private static final String TITLE = Messages.ChooseKPage_enter_k;

    /** shared data object */
    private final ElGamalData data;

    /** combo for providing a list of valid k values */
    private Combo combo;

    /**
     * Constructor setting the description, title, ... and the data object
     *
     * @param data {@link #data}
     */
    public ChooseKPage(final ElGamalData data) {
        super(PAGENAME, TITLE, null);
        this.data = data;
    }

    public void createControl(final Composite parent) {
        final Composite c = new Composite(parent, SWT.NONE);
        final int ncol = 2;
        c.setLayout(new GridLayout(ncol, false));
        final GridData gd2 = new GridData(SWT.LEFT, SWT.CENTER, true, true, ncol, 1);
        Label l = new Label(c, SWT.WRAP);
        l.setText(Messages.ChooseKPage_select_k_text);
        l.setLayoutData(gd2);
        l = new Label(c, SWT.NONE);
        l.setText("k"); //$NON-NLS-1$
        combo = new Combo(c, SWT.DROP_DOWN | SWT.READ_ONLY);
        fill(combo);
        combo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                data.setK(new BigInteger(combo.getText()));
                setPageComplete(!combo.getText().equals("")); //$NON-NLS-1$
            }
        });
        setControl(parent);
    }

    /**
     * fills the provided combo with valid values
     *
     * @param combo the combo to fill
     */
    private void fill(final Combo combo) {
        final BigInteger pm1 = data.getModulus().subtract(ONE);
        for (BigInteger i = ONE; i.compareTo(pm1) < 0; i = i.add(ONE)) {
            if (i.gcd(pm1).equals(ONE)) {
                combo.add(i.toString());
                i.add(pm1.divide(new BigInteger("30"))); //$NON-NLS-1$
            }
            if (combo.getItemCount() >= 50) {
                return;
            }
        }
    }
}
