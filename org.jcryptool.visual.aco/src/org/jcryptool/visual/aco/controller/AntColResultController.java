// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----

package org.jcryptool.visual.aco.controller;

import java.util.Vector;

import org.jcryptool.visual.aco.model.CommonModel;
import org.jcryptool.visual.aco.view.AntColResultComposite;
import org.jcryptool.visual.aco.view.AntColView;

public class AntColResultController implements AntColEvents {
	private CommonModel model;
	private AntColResultComposite resultComp;

	public AntColResultController(CommonModel model, AntColView view,
			AntColEventController antColCommonController) {

		this.model = model;
		this.resultComp = view.getResultComp();
	}

	@Override
	public void onSelectShowGraph() {
	 

	}

	@Override
	public void onSelectShowMatrix() {
	 

	}

	@Override
	public void onStartAnalyseButtonClick() {
		
	}

	@Override
	public void onNextKnot() {
	 

	}

	@Override
	public void onFinishCycle() {
	 

	}

	@Override
	public void afterGraphDrawn() {
	 
		this.refreshView();
	}

	private void refreshView() {
		resultComp.setResultContainer();
		resultComp.setResultText(model.toText(false),
				ConvertToString(model.getTrail()), model.toText(true),
				ConvertToString(model.getBestTrail()), model.getAntNr()); //$NON-NLS-1$);
	}

	@Override
	public void onSelectMultipleSteps() {
	 

	}

	@Override
	public void onSelectSingleStep() {
	 

	}

	@Override
	public void onChangeAnimationCheckboxValue(boolean value) {
	 

	}

	@Override
	public void onModifyConfiguration() {
	 

	}

	@Override
	public void onConfigurationChange() {
	 

	}

	@Override
	public void onPlaceNewAnt() {
		refreshView();
	}

	@Override
	public void onNewIteration() {
	 

	}

	@Override
	public void onKeyLengthChange(int value, int oldValue) {
	 

	}

	@Override
	public void onGenerateText() {
	 

	}

	@Override
	public void onCipherTextModify(String text) {
	 

	}

	private String ConvertToString(Vector<Integer> input) {
		if (input == null)
			return ""; //$NON-NLS-1$

		String s = ""; //$NON-NLS-1$
		for (Integer i : input)
			s += "," + (i + 1); //$NON-NLS-1$

		return "(" + s.replaceFirst(",", "") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Override
	public void onAlphaChanged(double value) {
	 
		
	}

	@Override
	public void onBetaChanged(double value) {
	 
		
	}

	@Override
	public void onPheromoneChanged(double value) {
	 
		
	}

	@Override
	public void onStopAnalyseButtonClick() {
		resultComp.setEmptyText();
	}

}
