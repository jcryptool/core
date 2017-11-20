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
import org.eclipse.swt.widgets.Event;
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
        final Composite composite = new Composite(parent, SWT.NONE);
        
        GridLayout gl_composite = new GridLayout(2, false);
        gl_composite.marginWidth = 50;
        composite.setLayout(gl_composite);
        Label label_text = new Label(composite, SWT.WRAP);
        label_text.setText(Messages.ChooseKPage_select_k_text);
        Label prime = new Label(composite, SWT.WRAP);
        prime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        prime.setText("p = " + data.getModulus().toString());
        label_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        //Spacer label
        new Label(composite, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        Label label_k = new Label(composite, SWT.NONE);
        
        label_k.setText("k = "); //$NON-NLS-1$
        combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        fill(combo);

        combo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                data.setK(new BigInteger(combo.getText()));
                setPageComplete(!combo.getText().equals("")); //$NON-NLS-1$
            }
        });
        combo.select(15);
        combo.notifyListeners(SWT.Selection, new Event());
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
