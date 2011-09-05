// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.dsa.ui.wizards.wizardpages;

import static java.math.BigInteger.ONE;

import java.math.BigInteger;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.dsa.DSAData;

public class ChooseKPage extends WizardPage {

	private static final String PAGENAME = "k entry page";
	private static final String TITLE = "Enter parameter k";
	private final DSAData data;
	private Combo combo;

	public ChooseKPage(final DSAData data) {
		super(PAGENAME, TITLE, null);
		this.data = data;
	}

	public void createControl(final Composite parent) {
		final Composite c = new Composite(parent, SWT.NONE);
		final int ncol = 2;
		c.setLayout(new GridLayout(ncol, false));
		final GridData gd2 = new GridData(SWT.LEFT, SWT.CENTER, true, true, ncol, 1);
		Label l = new Label(c, SWT.WRAP);
		l
				.setText("Bitte wählen Sie aus der Liste einen Parameter k zwischen 0 und p-1.\n Für diesen Parameter muss außerdem gelten, dass ggT(k, p-1)=1.");
		l.setLayoutData(gd2);
		l = new Label(c, SWT.NONE);
		l.setText("k");
		combo = new Combo(c, SWT.DROP_DOWN | SWT.READ_ONLY);
		fill(combo);
		combo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(final SelectionEvent e) {
				data.setK(new BigInteger(combo.getText()));
				setPageComplete(!combo.getText().equals(""));
			}

			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
			}
		});
		setControl(parent);
	}

	private void fill(final Combo combo) {
		final BigInteger pm1 = data.getModulus().subtract(ONE);
		for (BigInteger i = ONE; i.compareTo(pm1) < 0; i = i.add(ONE)) {
			if (i.gcd(pm1).equals(ONE)) {
				combo.add(i.toString());
				i.add(pm1.divide(new BigInteger("30")));
			}
			if (combo.getItemCount() >= 50) {
				return;
			}
		}
	}
}
