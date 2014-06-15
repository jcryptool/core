package org.jcryptool.visual.aco.controller;

import org.jcryptool.visual.aco.model.CommonModel;
import org.jcryptool.visual.aco.view.AntColDescriptionComposite;
import org.jcryptool.visual.aco.view.AntColView;

public class AntColDescriptionController implements AntColEvents {
	private AntColDescriptionComposite comp;
	private CommonModel model;

	public AntColDescriptionController(CommonModel model, AntColView view) {
		this.model = model;
		this.comp = view.getDescriptionComp();
	}

	@Override
	public void onSelectShowMatrix() {
	 
	}

	@Override
	public void onSelectShowGraph() {
	}

	@Override
	public void onStartAnalyseButtonClick() {
		int value = 1;
		if (!model.isVisualizable()) {
			value = 2;
		}
		comp.setDescriptionText(value);
	}

	@Override
	public void onNextKnot() {
		comp.setDescriptionText(1);
	}

	@Override
	public void onFinishCycle() {
		comp.setDescriptionText(1);
	}

	@Override
	public void afterGraphDrawn() {
	 

	}

	@Override
	public void onSelectMultipleSteps() {
	 
		comp.setDescriptionText(2);

	}

	@Override
	public void onSelectSingleStep() {
	 
		comp.setDescriptionText(1);

	}

	@Override
	public void onChangeAnimationCheckboxValue(boolean value) {
		comp.setDescriptionText(1);
	}

	@Override
	public void onModifyConfiguration() {
	 

	}

	@Override
	public void onConfigurationChange() {
	 

	}

	@Override
	public void onPlaceNewAnt() {
		comp.setDescriptionText(1);

	}

	@Override
	public void onNewIteration() {
		comp.setDescriptionText(2);

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
		comp.setDescriptionText(3);
	}

	@Override
	public void onBetaChanged(double value) {
		comp.setDescriptionText(3);
	}

	@Override
	public void onPheromoneChanged(double value) {
		comp.setDescriptionText(3);
	}

	@Override
	public void onStopAnalyseButtonClick() {
		comp.setDescriptionText(0);
	}

}
