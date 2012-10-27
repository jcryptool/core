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
	public void onNewIteration();
	public void onKeyLengthChange(int value);
	public void onGenerateText();
	public void onCipherTextModify(String text);
	public void onAlphaChanged(double value);
	public void onBetaChanged(double value);
	public void onPheromoneChanged(double value);
}
