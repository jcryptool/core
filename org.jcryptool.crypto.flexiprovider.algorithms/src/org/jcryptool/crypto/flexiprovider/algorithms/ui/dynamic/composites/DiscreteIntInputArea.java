//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.IAlgorithmParameterInputArea;

public class DiscreteIntInputArea implements IAlgorithmParameterInputArea, Listener {

	private Combo valueCombo;

	public DiscreteIntInputArea(Composite parent, String description) {
		String inBrackets = description.substring(description.indexOf("(")+1, description.indexOf(")")); //$NON-NLS-1$ //$NON-NLS-2$
		List<Integer> values = new ArrayList<Integer>(1);
		StringTokenizer tokenizer = new StringTokenizer(inBrackets, ","); //$NON-NLS-1$
		while (tokenizer.hasMoreTokens()) {
			String current = tokenizer.nextToken().trim();
			if (current.contains("or")) { //$NON-NLS-1$
				current = current.substring(current.indexOf(" ")+1, current.lastIndexOf(" ")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			values.add(Integer.valueOf(current));
		}
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		Label descriptionLabel = new Label(parent, SWT.NONE);
		descriptionLabel.setText(NLS.bind(Messages.DiscreteIntInputArea_0, description));
		descriptionLabel.setLayoutData(gridData);

		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.BEGINNING;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.widthHint = 150;
		gridData1.verticalAlignment = GridData.CENTER;
		valueCombo = new Combo(parent, SWT.NONE);
		valueCombo.setLayoutData(gridData1);
		valueCombo.addListener(SWT.Selection, this);

		parent.setLayout(new GridLayout());

		for (Integer value : values) {
			valueCombo.add(String.valueOf(value));
		}
		valueCombo.select(0);

		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData separatorGridData = new GridData();
		separatorGridData.horizontalAlignment = GridData.FILL;
		separatorGridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(separatorGridData);
	}

	@Override
	public Object getValue() {
		if (valueCombo != null) {
			return Integer.valueOf(valueCombo.getText());
		} else {
			return null;
		}
	}

	@Override
	public void setValue(Object value) {
		// unused
	}

	private WizardPage page;

	@Override
	public void setWizardPage(WizardPage page) {
		this.page = page;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.widget.equals(valueCombo)) {
			if (page != null) {
				page.setPageComplete(true);
			}
		}
	}

}
