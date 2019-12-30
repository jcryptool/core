//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.substitution.ui.modules.utils;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

public class StatisticsWizardPage extends WizardPage {

	private StatisticsSelector statisticsLoader;
	private AbstractAlphabet alphabet;

	/**
	 * Create the wizard.
	 */
	public StatisticsWizardPage(AbstractAlphabet alphabet) {
		super(Messages.StatisticsWizardPage_0);
		this.alphabet = alphabet;
		setTitle(Messages.StatisticsWizardPage_1);
		setDescription(Messages.StatisticsWizardPage_2);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout());
		
		statisticsLoader = new StatisticsSelector(container, container, SWT.NONE);
		statisticsLoader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		statisticsLoader.setAlphabet(alphabet);
		
		statisticsLoader.addObserver(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				StatisticsWizardPage.this.setPageComplete(statisticsLoader.getSelectedStatistic() != null);
			}
		});
		
		StatisticsWizardPage.this.setPageComplete(statisticsLoader.getSelectedStatistic() != null);
		setControl(container);
	}

	public StatisticsSelector getStatisticsLoader() {
		return statisticsLoader;
	}

}
