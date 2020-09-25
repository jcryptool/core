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
import java.util.Map;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.InitializationPage;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisDataobject;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisInitializationInput;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysis;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.TranspositionAnalysisInput;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.Messages;

public class AnalysisWizard extends Wizard implements TranspositionAnalysisInput {

	private TranspositionAnalysisDataobject dataobject;

	// pages
	NewAnalysisSelectionPage selectionPage;
	DividerAnalysisPage dividerPage;
	PaddingAnalysisPage paddingPage;
	InitializationPage initializationPage;
	CalculationPage calcPage;
	ConclusionPage conclusionPage;

	// mapping from analyses to analysis pages
	private Map<TranspositionAnalysis, SingleAnalysisPage> analysisPageMap;

	private String ciphertext;
	private boolean firstAnalysis = false;

	/**
	 * @param ciphertext the ciphertext to analyze.
	 * @param dataobject , if there is analysis data that should be displayed as
	 *                   first page, before starting a new analysis. null for not
	 *                   showing a cinclusion first, but starting a new analysis
	 *                   straightaway.
	 */
	public AnalysisWizard(String ciphertext, TranspositionAnalysisDataobject dataobject) {
		if (dataobject == null) {
			firstAnalysis = true;
			this.dataobject = new TranspositionAnalysisDataobject();
		} else {
			firstAnalysis = false;
			this.dataobject = dataobject;
		}

		this.ciphertext = ciphertext;

		initPages();

		setWindowTitle(Messages.AnalysisWizard_advancedanalysissteps);
	}

	private void initPages() {
		analysisPageMap = new HashMap<TranspositionAnalysis, SingleAnalysisPage>();

		// ChooserPage
		selectionPage = new NewAnalysisSelectionPage(this.dataobject.getListOfAnalyses());

		// Initialization page
		initializationPage = new InitializationPage(this.dataobject.iniAnalysis);
		this.dataobject.iniAnalysis.setInput(initializationPage);
		analysisPageMap.put(this.dataobject.iniAnalysis, initializationPage);

		// Divider page
		dividerPage = new DividerAnalysisPage(this.dataobject.dividerAnalysis);
		this.dataobject.dividerAnalysis.setInput(dividerPage);
		analysisPageMap.put(this.dataobject.dividerAnalysis, dividerPage);

		// Padding page
		paddingPage = new PaddingAnalysisPage(this.dataobject.paddingAnalysis);
		this.dataobject.paddingAnalysis.setInput(paddingPage);
		analysisPageMap.put(this.dataobject.paddingAnalysis, paddingPage);

		calcPage = new CalculationPage(this.dataobject);
	}

	private TranspositionAnalysis getAnalysisForPage(SingleAnalysisPage page) {
		for (TranspositionAnalysis analysis : analysisPageMap.keySet()) {
			if (page == analysisPageMap.get(analysis))
				return analysis;
		}
		return null;
	}

	public SingleAnalysisPage nextPageFrom(SingleAnalysisPage actual) {
		TranspositionAnalysis actualAnalysis = getAnalysisForPage(actual);

		while (selectionPage.getNextAnalysisAfter(actualAnalysis) != null) { // while
																				// there
																				// is
																				// a
																				// next
																				// analysis
			TranspositionAnalysis nextAnalysis = selectionPage.getNextAnalysisAfter(actualAnalysis);
			SingleAnalysisPage nextAnalysisPage = analysisPageMap.get(nextAnalysis);
			if (nextAnalysisPage != null) { // if there is a page for this
											// analysis
				// return this page
				return nextAnalysisPage;
			} else { // if there is no page for it, try the next analysis
				actualAnalysis = nextAnalysis;
			}
		}

		// Normally, here the calculation and resumee pages are called
		return null;
	}

	public void prepareDataobjectAnalyses() {

	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public final void addPages() {
		if (!firstAnalysis) {
			addPage(new ConclusionPage(dataobject, true));
		}
		addPage(selectionPage);
		addPage(initializationPage);
		addPage(dividerPage);
		addPage(paddingPage);
		addPage(calcPage);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public String getCiphertext() {
		return ciphertext;
	}

	@Override
	public double getUserEstimatedAnalysisWeight() {
		return 1.0;
	}

	@Override
	public boolean isUserEstimatedAnalysisWeight() {
		return false;
	}

	public TranspositionAnalysisInitializationInput getInitializationInput() {
		return initializationPage;
	}

	public IWizardPage getCalcPage() {
		return calcPage;
	}

	public void analyze() {
		// first reset conclusions
		for (TranspositionAnalysis analysis : dataobject.getListOfAnalyses()) {
			analysis.resetConclusion();
		}

		int counter = 0;
		for (TranspositionAnalysis analysis : dataobject.getListOfAnalyses()) {
			if (selectionPage.isAnalysisSelected(analysis)) {
				calcPage.setProgress((double) counter / (double) (selectionPage.getSelectionCount() + 1));
				analysis.analyze();
				analysis.combineResultsWithOutput();
				counter++;
			}
		}
		calcPage.setProgress((double) counter / (double) (selectionPage.getSelectionCount() + 1));

		dataobject.resumeeAnalysis.analyze();
		dataobject.resumeeAnalysis.combineResultsWithOutput();
		calcPage.setProgress(1.0);

		conclusionPage = new ConclusionPage(dataobject, false);
		addPage(conclusionPage);

		calcPage.analysisComplete();
	}

	public TranspositionAnalysisDataobject getDataobject() {
		return dataobject;
	}

}
