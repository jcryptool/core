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

public interface AntColEvents{
	public void onSelectShowGraph();
	public void onSelectShowMatrix();
	public void onStartAnalyseButtonClick();
	public void onNextKnot();
	public void onFinishCycle();
	public void afterGraphDrawn();
	public void onSelectMultipleSteps();
	public void onSelectSingleStep();
	public void onChangeAnimationCheckboxValue(boolean value);
	public void onModifyConfiguration();
	public void onConfigurationChange();
	public void onPlaceNewAnt();
	public void onStopAnalyseButtonClick();
	public void onNewIteration();
	public void onKeyLengthChange(int value, int oldValue);
	public void onGenerateText();
	public void onCipherTextModify(String text);
	public void onAlphaChanged(double value);
	public void onBetaChanged(double value);
	public void onPheromoneChanged(double value);
}
