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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.IAlgorithmParameterInputArea;

public class RangeIntInputArea implements IAlgorithmParameterInputArea {

	private Text valueText;
	private int max = -1;

	private void setMax(int max) {
		this.max = max;
	}

	private int getMax() {
		return max;
	}

	public RangeIntInputArea(Composite parent, String description) {
		String inBrackets = description.substring(description.indexOf("(")+1, description.indexOf(")")); //$NON-NLS-1$ //$NON-NLS-2$
		int min = Integer.valueOf(inBrackets.substring(0, inBrackets.indexOf("."))); //$NON-NLS-1$
		int max = Integer.valueOf(inBrackets.substring(inBrackets.lastIndexOf(".")+1, inBrackets.length())); //$NON-NLS-1$
		setMax(max);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		Label descriptionLabel = new Label(parent, SWT.NONE);
		descriptionLabel.setText(NLS.bind(Messages.RangeIntInputArea_0, description));
		descriptionLabel.setLayoutData(gridData);

		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.BEGINNING;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.widthHint = 150;
		gridData1.verticalAlignment = GridData.CENTER;
		valueText = new Text(parent, SWT.BORDER);
		valueText.setLayoutData(gridData1);
		valueText.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				if (e.character != SWT.BS && e.character != SWT.DEL) {
					String origInput = valueText.getText();
					if (!origInput.equals("")) { //$NON-NLS-1$
						//FIXME: min and max verify listeners
						if (origInput.length() > 1) {
							origInput = origInput.substring(0, origInput.length()-1);

							try {
								int input = Integer.valueOf(origInput);
								if (input > getMax()) {
									e.doit = false;
								}
							} catch (NumberFormatException exc) {
								e.doit = false;
							}
						}
					}
					if (page != null) {
						page.setPageComplete(true);
					}
				}
			}

		});

		parent.setLayout(new GridLayout());

		valueText.setText(String.valueOf(min));

		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData separatorGridData = new GridData();
		separatorGridData.horizontalAlignment = GridData.FILL;
		separatorGridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(separatorGridData);
	}

	@Override
	public Object getValue() {
		if (valueText != null) {
			return Integer.valueOf(valueText.getText()).intValue();
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

}
