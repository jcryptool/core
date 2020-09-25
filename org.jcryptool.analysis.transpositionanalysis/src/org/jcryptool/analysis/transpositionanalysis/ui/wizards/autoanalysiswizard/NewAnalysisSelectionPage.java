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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisInitialization;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisResumee;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysis;

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
public class NewAnalysisSelectionPage extends WizardPage implements Listener {

	Composite pageComposite;
	private List<TranspositionAnalysis> analysesList;
	private Map<TranspositionAnalysis, AnalysisSelectItem> analyses;

	private String normalMessage = "Select the analysis steps you want to execute.";

	public NewAnalysisSelectionPage(List<TranspositionAnalysis> analyses) {
		super("Analysis steps");
		this.setDescription(normalMessage);
		this.setTitle("Analysis steps");
		analysesList = analyses;
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

			analyses = new HashMap<TranspositionAnalysis, AnalysisSelectItem>();
			for (TranspositionAnalysis analysis : analysesList) {
				if (!(analysis instanceof TranspositionAnalysisResumee)) {
					AnalysisSelectItem newItem = new AnalysisSelectItem(pageComposite, analysis);
					GridData newItemLData = new GridData();
					newItemLData.grabExcessHorizontalSpace = true;
					newItemLData.horizontalAlignment = SWT.FILL;
					newItemLData.verticalAlignment = SWT.TOP;
					newItem.setLayoutData(newItemLData);

					newItem.setSelection(true);
					if (!analysis.isObligatory()) {
						newItem.setSelection(true);
						newItem.setEnabled(false);
					}
					newItem.addSelectionListener(this);

					analyses.put(analysis, newItem);
				}
			}

		}

		calcPageComplete();
		setControl(pageComposite);

	}

	public TranspositionAnalysis getNextAnalysisAfter(TranspositionAnalysis analysisFrom) {
		boolean found = analysisFrom == null;
		for (TranspositionAnalysis analysis : analysesList) {
			if (!found && analysis == analysisFrom) {
				found = true;
				continue;
			}
			if (found && analyses.containsKey(analysis) && analyses.get(analysis).getSelection()) {
				return analysis;
			}
		}
		return null;
	}

	@Override
	public void handleEvent(Event event) {
		calcPageComplete();
	}

	@Override
	public IWizardPage getNextPage() {
		IWizardPage nextPage = ((AnalysisWizard) getWizard()).nextPageFrom(null);
		if (nextPage != null)
			return nextPage;

		return null;
		// return super.getNextPage();
	}

	public boolean isAnalysisSelected(TranspositionAnalysis analysis) {
		for (TranspositionAnalysis analysisInPage : analyses.keySet()) {
			if (analysisInPage.getClass().equals(analysis.getClass()) && analyses.get(analysisInPage).getSelection())
				return true;
		}
		return false;
	}

	public int getSelectionCount() {
		int counter = 0;
		for (Entry<TranspositionAnalysis, AnalysisSelectItem> entry : analyses.entrySet()) {
			if (entry.getValue().getSelection())
				counter++;
		}
		return counter;
	}

	private void calcPageComplete() {
		setNormalMessage();
		setPageComplete(true);

		boolean oneSelected = false;
		boolean onePlusIniSelected = false;
		for (Entry<TranspositionAnalysis, AnalysisSelectItem> entry : analyses.entrySet()) {
			if (entry.getValue().getSelection())
				oneSelected = true;
			if (!(entry.getKey() instanceof TranspositionAnalysisInitialization) && entry.getValue().getSelection())
				onePlusIniSelected = true;
		}

		if (!onePlusIniSelected) {
			setMessage("Just the initialization of the analysis is selected.", IMessageProvider.WARNING);
		}
		if (!oneSelected) {
			setPageComplete(false);
			setMessage("Select at least one analysis.", IMessageProvider.ERROR);
		}

	}

	private void setNormalMessage() {
		setMessage(normalMessage, IMessageProvider.NONE);
	}

}
