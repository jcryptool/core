//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysis;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.autoanalysiswizard.SingleAnalysisPage;

public class InitializationPage extends SingleAnalysisPage implements TranspositionAnalysisInitializationInput {

	private Group compMaxLengthSelect;
	private Label labelMaxlengthSelect;
	private Combo comboMaxLength;
	private Label labelMaxlengthAdvice;
	private Label labelExplanation;

	public InitializationPage(TranspositionAnalysis analysis) {
		super(analysis);
	}

	@Override
	protected void calcPageComplete() {
		setPageComplete(true);
	}

	@Override
	protected void createMainControls(Composite parent) {
		{
			labelExplanation = new Label(parent, SWT.WRAP);
			GridData labelExplanationLData = new GridData();
			labelExplanationLData.horizontalAlignment = SWT.FILL;
			labelExplanationLData.grabExcessHorizontalSpace = true;
			labelExplanationLData.widthHint = 300;
			labelExplanation.setLayoutData(labelExplanationLData);
			labelExplanation.setText(
					"For the initialization, it is important to know, up to which block lengths (key lengths) the different analyses may search:");
		}
		{
			compMaxLengthSelect = new Group(parent, SWT.NONE);
			GridLayout compMaxLengthSelectLayout = new GridLayout();
			GridData compMaxLengthSelectLData = new GridData();
			compMaxLengthSelectLayout.numColumns = 2;
			compMaxLengthSelectLayout.makeColumnsEqualWidth = false;
			compMaxLengthSelectLData.grabExcessHorizontalSpace = true;
			compMaxLengthSelectLData.horizontalAlignment = SWT.FILL;
			compMaxLengthSelectLData.verticalIndent = 5;
			compMaxLengthSelect.setLayout(compMaxLengthSelectLayout);
			compMaxLengthSelect.setLayoutData(compMaxLengthSelectLData);
			compMaxLengthSelect.setText("Maximum block length selection");
			{
				labelMaxlengthSelect = new Label(compMaxLengthSelect, SWT.WRAP);
				GridData labelMaxlengthSelectLData = new GridData();
				labelMaxlengthSelectLData.widthHint = 150;
				labelMaxlengthSelectLData.horizontalAlignment = SWT.FILL;
				labelMaxlengthSelectLData.grabExcessHorizontalSpace = true;
				labelMaxlengthSelect.setLayoutData(labelMaxlengthSelectLData);
				labelMaxlengthSelect.setText("Select the maximum block length to analyse:");
			}
			{
				comboMaxLength = new Combo(compMaxLengthSelect, SWT.NONE);
				GridData comboMaxLengthLData = new GridData();
				comboMaxLength.setLayoutData(comboMaxLengthLData);
				for (int i = 3; i < 30; i++)
					comboMaxLength.add("" + i);

				comboMaxLength.select(18 - 3);
			}

		}
		{
			labelMaxlengthAdvice = new Label(parent, SWT.WRAP);
			GridData labelMaxlengthAdviceLData = new GridData();
			labelMaxlengthAdviceLData.horizontalAlignment = SWT.FILL;
			labelMaxlengthAdviceLData.grabExcessHorizontalSpace = true;
			labelMaxlengthAdviceLData.widthHint = 300;
			labelMaxlengthAdvice.setLayoutData(labelMaxlengthAdviceLData);
			labelMaxlengthAdvice.setText(
					"Please keep in mind, that too big keylengths can lead to very time-consumptive algorithm behaviour.");
		}
	}

	@Override
	public int getMaxKeylength() {
		return Integer.parseInt(comboMaxLength.getText());
	}

}
