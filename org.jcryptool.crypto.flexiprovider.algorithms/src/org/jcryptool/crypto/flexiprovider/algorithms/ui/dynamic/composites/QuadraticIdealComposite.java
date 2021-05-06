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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.IAlgorithmParameterInputArea;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.quadraticfields.QuadraticIdeal;

public class QuadraticIdealComposite implements IAlgorithmParameterInputArea{

	private Text value1;
	private Text value2;
	private WizardPage page;

	private static final List<String> hexValues = new ArrayList<String>(16);
	static {
		hexValues.add(0, "0"); hexValues.add(1, "1"); hexValues.add(2, "2"); hexValues.add(3, "3"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		hexValues.add(4, "4"); hexValues.add(5, "5"); hexValues.add(6, "6"); hexValues.add(7, "7"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		hexValues.add(8, "8"); hexValues.add(9, "9"); hexValues.add(10, "A"); hexValues.add(11, "B"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		hexValues.add(12, "C"); hexValues.add(13, "D"); hexValues.add(14, "E"); hexValues.add(15, "F"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	public QuadraticIdealComposite(Composite parent) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		Label descriptionLabel = new Label(parent, SWT.NONE);
		descriptionLabel.setText(Messages.QuadraticIdealComposite_0);
		descriptionLabel.setLayoutData(gridData);

		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.BEGINNING;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.widthHint = 150;
		gridData1.verticalAlignment = GridData.CENTER;
		value1 = new Text(parent, SWT.BORDER);
		value1.setLayoutData(gridData1);

		value1.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {			
				if (e.character != SWT.BS && e.character != SWT.DEL) {
					e.text = e.text.toUpperCase();
					if ( !hexValues.contains(e.text) ) {
						e.doit = false;							
					} else {
						if (page != null) {
							page.setPageComplete(true);	
						}
					}
				}
			}
		});

		parent.setLayout(new GridLayout());
		
		
		Label descriptionLabel2 = new Label(parent, SWT.NONE);
		descriptionLabel2.setText(Messages.QuadraticIdealComposite_1);
		descriptionLabel2.setLayoutData(gridData);

		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.BEGINNING;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.widthHint = 150;
		gridData2.verticalAlignment = GridData.CENTER;
		value2 = new Text(parent, SWT.BORDER);
		value2.setLayoutData(gridData2);

		value2.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {			
				if (e.character != SWT.BS && e.character != SWT.DEL) {
					e.text = e.text.toUpperCase();
					if ( !hexValues.contains(e.text) ) {
						e.doit = false;							
					} else {
						if (page != null) {
							page.setPageComplete(true);	
						}
					}
				}
			}
		});

		parent.setLayout(new GridLayout());

		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData separatorGridData = new GridData();
		separatorGridData.horizontalAlignment = GridData.FILL;
		separatorGridData.grabExcessHorizontalSpace = true;						
		separator.setLayoutData(separatorGridData);
	}

	@Override
	public void setWizardPage(WizardPage page) {
		this.page = page;
	}

	@Override
	public Object getValue() {
		if (value1 != null && value2 != null) {
			return new QuadraticIdeal(
					new FlexiBigInt(value1.getText(), 16), 
					new FlexiBigInt(value2.getText(), 16)
			); 
		} else {
			return null;
		}
	}

	@Override
	public void setValue(Object value) {
		// unused
	}

}
