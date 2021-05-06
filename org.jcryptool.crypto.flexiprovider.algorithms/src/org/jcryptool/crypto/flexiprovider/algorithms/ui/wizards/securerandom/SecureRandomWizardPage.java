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
package org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.securerandom;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;



public class SecureRandomWizardPage extends WizardPage {

	private SecureRandomWizard wizard;
	private Label descriptionLabel;
	private Text text;

	protected SecureRandomWizardPage(IMetaAlgorithm algorithm, SecureRandomWizard wizard) {
		super("SecureRandomWizardPage", algorithm.getName(), null); //$NON-NLS-1$
		setDescription(Messages.SecureRandomWizardPage_0);
		this.wizard = wizard;
	}

	@Override
	public void setPageComplete(boolean value) {
		super.setPageComplete(value);
		wizard.setCanFinish(value);	
	}

	@Override
	public void createControl(Composite parent) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		descriptionLabel = new Label(parent, SWT.NONE);
		descriptionLabel.setText(Messages.SecureRandomWizardPage_1);
		descriptionLabel.setLayoutData(gridData);
		text = new Text(parent, SWT.BORDER);
		text.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				if (e.character != SWT.BS && e.character != SWT.DEL) {					
					try {	
						Integer.valueOf(e.text);
						setPageComplete(true);
					} catch (NumberFormatException exc) {
						e.doit = false;
					}
				}
			}

		});
		parent.setLayout(new GridLayout());
		setControl(parent);
	}

	protected int getLength() {
		if (text.getText().length() > 0) {
			return Integer.valueOf(text.getText());	
		} else return 0;
	}

}
