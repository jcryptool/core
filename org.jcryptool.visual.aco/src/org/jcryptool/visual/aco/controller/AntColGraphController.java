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

import org.jcryptool.visual.aco.model.CommonModel;
import org.jcryptool.visual.aco.view.AntColGraphComposite;
import org.jcryptool.visual.aco.view.AntColView;

public class AntColGraphController implements AntColEvents{
	private AntColEventController registry;
	private AntColGraphComposite graphComp;

	public AntColGraphController(CommonModel model, AntColView view, AntColEventController antColControllerRegistry) {

		this.graphComp = view.getVisualComp().getGraphComp();
		// TODO Auto-generated constructor stub
		this.registry = antColControllerRegistry;
		graphComp.addController(registry);
	}

	public void onSelectShowGraph() {
		graphComp.redraw();
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
		// TODO Auto-generated method stub
		
	}
}
