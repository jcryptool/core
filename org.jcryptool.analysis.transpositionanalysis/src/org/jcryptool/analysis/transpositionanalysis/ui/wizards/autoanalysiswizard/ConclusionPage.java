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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisDataobject;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysis;

public class ConclusionPage extends WizardPage {

	private TranspositionAnalysisDataobject dataobject;
	private Composite pageComposite;
	private Label labelExplanationFirst;
	private ConclusionItem resumee;

	protected ConclusionPage(TranspositionAnalysisDataobject dataobject, boolean justStoredPageAtBeginning) {
		super("Conclusion");
		this.setTitle("Conclusion");
		if (!justStoredPageAtBeginning) {
			this.setDescription(
					"This page shows the results of all analysis combined, and the results of each single analysis.");
		} else {
			this.setDescription(
					"This page shows the results of the last analysis. Go to the next page to start a fresh new analysis.");
		}
		this.dataobject = dataobject;
	}

	@Override
	public void createControl(Composite parent) {
		{
			pageComposite = new Composite(parent, SWT.NONE);
			GridLayout pageCompositeLayout = new GridLayout();
			GridData pageCompositeLData = new GridData();
			pageCompositeLData.horizontalAlignment = SWT.FILL;
			pageCompositeLData.verticalAlignment = SWT.FILL;
			pageCompositeLData.grabExcessHorizontalSpace = true;
			pageCompositeLData.grabExcessVerticalSpace = true;
			pageCompositeLayout.makeColumnsEqualWidth = true;
			pageComposite.setLayout(pageCompositeLayout);
			// {
			// labelExplanationFirst = new Label(pageComposite, SWT.NONE);
			// GridData labelExplanationFirstLData = new GridData();
			// labelExplanationFirstLData.grabExcessHorizontalSpace = true;
			// labelExplanationFirstLData.horizontalAlignment = SWT.FILL;
			// labelExplanationFirstLData.widthHint = 300;
			// labelExplanationFirst.setLayoutData(labelExplanationFirstLData);
			// labelExplanationFirst.setText("");
			// }
			{
				// Resumee
				resumee = new ConclusionItem(pageComposite, dataobject.resumeeAnalysis);
				GridData resumeeLData = new GridData();
				resumeeLData.horizontalAlignment = SWT.FILL;
				resumeeLData.grabExcessHorizontalSpace = true;
				resumee.setLayoutData(resumeeLData);
			}
			// {
			// Label separator = new Label(pageComposite, SWT.SEPARATOR |
			// SWT.HORIZONTAL);
			// GridData serparatorLData = new GridData();
			// serparatorLData.widthHint = 300;
			// serparatorLData.horizontalAlignment = SWT.CENTER;
			// serparatorLData.grabExcessHorizontalSpace = true;
			// separator.setLayoutData(serparatorLData);
			// }
			{
				labelExplanationFirst = new Label(pageComposite, SWT.NONE);
				GridData labelExplanationFirstLData = new GridData();
				labelExplanationFirstLData.grabExcessHorizontalSpace = true;
				labelExplanationFirstLData.horizontalAlignment = SWT.FILL;
				labelExplanationFirstLData.verticalIndent = 10;
				labelExplanationFirstLData.widthHint = 300;
				labelExplanationFirst.setLayoutData(labelExplanationFirstLData);
				labelExplanationFirst.setText("Conclusions of what each single analysis found:");
			}
			{
				for (TranspositionAnalysis analysis : dataobject.getListOfAnalyses()) { // other
																						// analysis
					if (analysis != dataobject.iniAnalysis && analysis != dataobject.resumeeAnalysis
							&& analysis.getConclusion() != null) {
						ConclusionItem analysisConclusionItem = new ConclusionItem(pageComposite, analysis);
						GridData analysisConclusionItemLData = new GridData();
						analysisConclusionItemLData.horizontalAlignment = SWT.FILL;
						analysisConclusionItemLData.grabExcessHorizontalSpace = true;
						analysisConclusionItem.setLayoutData(analysisConclusionItemLData);
					}
				}

			}
		}
		setControl(pageComposite);
		setPageComplete(true);
	}

}
