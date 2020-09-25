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
package org.jcryptool.analysis.transpositionanalysis.ui.wizards.autoanalysiswizard;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysis;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisInput;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public abstract class SingleAnalysisPage extends WizardPage implements TranspositionAnalysisInput {

	Composite pageComposite;
	private Composite compMainContent;
	private Combo comboDefineOwnWeight;
	private Label labelDefineOwnWeight;
	private Group grpUserEstimatedRating;
	private TranspositionAnalysis analysis;

	public SingleAnalysisPage(TranspositionAnalysis analysis) {
		super(analysis.getAnalysisName());
		this.setDescription(analysis.getAnalysisDescription());
		this.setTitle(analysis.getAnalysisName());
		this.analysis = analysis;
	}

	@Override
	public void createControl(Composite parent) {

		{
			GridData pageCompositeLayoutData = new GridData();
			GridLayout pageCompositeLayout = new GridLayout();
			pageCompositeLayoutData.grabExcessHorizontalSpace = true;
			pageCompositeLayoutData.grabExcessVerticalSpace = false;
			pageCompositeLayoutData.horizontalAlignment = SWT.FILL;
			pageCompositeLayoutData.verticalAlignment = SWT.TOP;
			pageComposite = new Composite(parent, SWT.NONE);
			pageComposite.setLayout(pageCompositeLayout);
			pageComposite.setLayoutData(pageCompositeLayoutData);
			{
				compMainContent = new Composite(pageComposite, SWT.NONE);
				GridLayout composite1Layout = new GridLayout();
				composite1Layout.makeColumnsEqualWidth = true;
				GridData compMainContentLData = new GridData();
				compMainContentLData.grabExcessHorizontalSpace = true;
				compMainContentLData.horizontalAlignment = GridData.FILL;
				compMainContentLData.verticalAlignment = GridData.FILL;
				compMainContent.setLayoutData(compMainContentLData);
				compMainContent.setLayout(composite1Layout);

				{
					createMainControls(compMainContent);
				}
			}

			{
				if (analysis.allowUserEstimatedRating())
					createDefineOwnWeightControl(pageComposite);
			}
		}
		calcPageComplete();
		setControl(pageComposite);

	}

	protected abstract void calcPageComplete();

	protected abstract void createMainControls(Composite pageComposite2);

	protected void createDefineOwnWeightControl(Composite parent) {
		// {
		// separator = new Label(pageComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		// GridData separatorLData = new GridData();
		// separatorLData.grabExcessHorizontalSpace = true;
		// separatorLData.horizontalAlignment = SWT.FILL;
		// separator.setLayoutData(separatorLData);
		// }
		{
			grpUserEstimatedRating = new Group(parent, SWT.NONE);
			GridLayout grpUserEstimatedRatingLayout = new GridLayout();
			grpUserEstimatedRatingLayout.numColumns = 2;
			grpUserEstimatedRating.setLayout(grpUserEstimatedRatingLayout);
			GridData grpUserEstimatedRatingLData = new GridData();
			grpUserEstimatedRatingLData.verticalIndent = 10;
			grpUserEstimatedRatingLData.grabExcessHorizontalSpace = true;
			grpUserEstimatedRatingLData.horizontalAlignment = GridData.FILL;
			grpUserEstimatedRating.setLayoutData(grpUserEstimatedRatingLData);
			grpUserEstimatedRating.setText("Define own impact (weight) of this analysis");
			{
				labelDefineOwnWeight = new Label(grpUserEstimatedRating, SWT.WRAP);
				GridData labelDefineOwnWeightLData = new GridData();
				labelDefineOwnWeightLData.horizontalAlignment = GridData.FILL;
				labelDefineOwnWeightLData.grabExcessHorizontalSpace = true;
				labelDefineOwnWeightLData.widthHint = 350;
				labelDefineOwnWeight.setLayoutData(labelDefineOwnWeightLData);
				labelDefineOwnWeight.setText(
						"If you have the feeling that this analysis should be weighted less or more than normal (e. g. you are not very sure about the correctness of your input), specify a multiplier here:");
			}
			{
				comboDefineOwnWeight = new Combo(grpUserEstimatedRating, SWT.NONE);
				GridData comboDefineOwnWeightLData = new GridData();
				comboDefineOwnWeight.setLayoutData(comboDefineOwnWeightLData);
				for (int i = 1; i < 10; i++)
					comboDefineOwnWeight.add("0." + i);
				comboDefineOwnWeight.add("1.0");
				for (int i = 1; i < 10; i++)
					comboDefineOwnWeight.add("1." + i);
				comboDefineOwnWeight.add("2.0");

				comboDefineOwnWeight.select(9);
			}
		}
	}

	@Override
	public String getCiphertext() {
		return ((TranspositionAnalysisInput) getWizard()).getCiphertext();
	}

	@Override
	public double getUserEstimatedAnalysisWeight() {
		return Double.parseDouble(comboDefineOwnWeight.getText());
	}

	@Override
	public boolean isUserEstimatedAnalysisWeight() {
		return true;
	}

	@Override
	public IWizardPage getNextPage() {
		IWizardPage nextPage = ((AnalysisWizard) getWizard()).nextPageFrom(this);
		if (nextPage != null)
			return nextPage;

		return ((AnalysisWizard) getWizard()).getCalcPage();
	}

}
