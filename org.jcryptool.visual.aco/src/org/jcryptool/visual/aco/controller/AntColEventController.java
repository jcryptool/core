package org.jcryptool.visual.aco.controller;

import java.util.ArrayList;

import org.jcryptool.visual.aco.model.CommonModel;
import org.jcryptool.visual.aco.view.AntColView;

public class AntColEventController implements AntColEvents {
	private AntColVisualController visualController;
	private AntColAnalysisController anaylsisController;
	private AntColConfigController configController;
	private AntColGraphController graphController;
	private AntColPherMatrixController matrixController;
	private AntColResultController resultController;
	private AntColDescriptionController descController;

	private ArrayList<AntColEvents> controllerList;
	private CommonModel model;
	private boolean finishCycle;

	public AntColEventController(CommonModel model, AntColView view) {
		this.model = model;
		visualController = new AntColVisualController(model, view, this);
		anaylsisController = new AntColAnalysisController(model, view, this);
		configController = new AntColConfigController(model, view, this);
		graphController = new AntColGraphController(model, view, this);
		matrixController = new AntColPherMatrixController(model, view, this);
		resultController = new AntColResultController(model, view, this);
		descController = new AntColDescriptionController(model, view);
		
		controllerList = new ArrayList<AntColEvents>();
		controllerList.add(visualController);
		controllerList.add(anaylsisController);
		controllerList.add(configController);
		controllerList.add(graphController);
		controllerList.add(matrixController);
		controllerList.add(resultController);
		controllerList.add(descController);

		view.getVisualComp().addController(this);
		view.getConfigComp().addController(this);
		view.getAnalysisComp().addController(this);
	}

	public AntColVisualController getVisualController() {
		return visualController;
	}

	public AntColAnalysisController getAnaylsisController() {
		return anaylsisController;
	}

	public AntColConfigController getConfigController() {
		return configController;
	}

	public AntColGraphController getGraphController() {
		return graphController;
	}

	public AntColPherMatrixController getMatrixController() {
		return matrixController;
	}

	public void onStartAnalyseButtonClick() {
		model.startAnalyse();

		for (AntColEvents con : controllerList) {
			con.onStartAnalyseButtonClick();
		}
	}

	public void onStopAnalyseButtonClick() {
		model.reset();
		for (AntColEvents con : controllerList) {
			con.onStopAnalyseButtonClick();
		}
	}

	public void onSelectShowGraph() {
		model.setDisplayGraph(true);
		model.setAnimateable(true);
		for (AntColEvents con : controllerList) {
			con.onSelectShowGraph();
		}
	 

	}

	public void onSelectShowMatrix() {
		model.setDisplayGraph(false);
		model.setAnimateable(false);

		for (AntColEvents con : controllerList) {
			con.onSelectShowMatrix();
		}
	}

	public void onNextKnot() {
		finishCycle = false;
		model.toNextKnot();

		for (AntColEvents con : controllerList) {
			con.onNextKnot();
		}

		if (!model.isGraphDisplayed() || !model.isAnimateable()) {
			afterGraphDrawn();
		}
	}

	@Override
	public void onFinishCycle() {
		finishCycle = true;
		for (AntColEvents con : controllerList) {
			con.onFinishCycle();
		}

		if (!model.isGraphDisplayed() || !model.isAnimateable()) {
			afterGraphDrawn();
		}
	}

	public void afterGraphDrawn() {
		if (finishCycle && !model.isAtLastKnot()) {
			return;
		}

		for (AntColEvents con : controllerList) {
			con.afterGraphDrawn();
		}

	}

	@Override
	public void onSelectMultipleSteps() {
		model.setSingleStep(false);

		for (AntColEvents con : controllerList) {
			con.onSelectMultipleSteps();
		}
	}

	@Override
	public void onSelectSingleStep() {
		model.setSingleStep(true);

		for (AntColEvents con : controllerList) {
			con.onSelectSingleStep();
		}

	}

	@Override
	public void onChangeAnimationCheckboxValue(boolean value) {
		model.setAnimateable(value);
		for (AntColEvents con : controllerList) {
			con.onChangeAnimationCheckboxValue(value);
		}
	}

	@Override
	public void onModifyConfiguration() {
		model.reset();
		for (AntColEvents con : controllerList) {
			con.onModifyConfiguration();
		}
	}

	@Override
	public void onConfigurationChange() {
		for (AntColEvents con : controllerList) {
			con.onConfigurationChange();
		}
	}

	@Override
	public void onPlaceNewAnt() {
		model.replaceAnt();
		for (AntColEvents con : controllerList) {
			con.onPlaceNewAnt();
		}
	}

	@Override
	public void onNewIteration() {
		for (AntColEvents con : controllerList) {
			con.onNewIteration();
		}
	}
	
	public void onChangeLanguage(){
		configController.onChangeLanguage();
	}
	

	public void onGenerateText(){
		for (AntColEvents con : controllerList) {
			con.onGenerateText();
		}
	}

	@Override
	public void onCipherTextModify(String text) {
		model.setText(text);
		for (AntColEvents con : controllerList) {
			con.onCipherTextModify(text);
			con.onConfigurationChange();
		}
	}

	@Override
	public void onAlphaChanged(double value) {
		model.setAlpha(value);
		for (AntColEvents con : controllerList) {
			con.onAlphaChanged(value);
		}
	}

	@Override
	public void onBetaChanged(double value) {
		model.setBeta(value);
		for (AntColEvents con : controllerList) {
			con.onBetaChanged(value);
		}
	}

	@Override
	public void onPheromoneChanged(double value) {
		model.setVerd(value);
		for (AntColEvents con : controllerList) {
			con.onPheromoneChanged(value);
		}
	}

	@Override
	public void onKeyLengthChange(int value, int oldValue) {
		model.setSize(value);

		for (AntColEvents con : controllerList) {
			con.onKeyLengthChange(value, oldValue);
			con.onConfigurationChange();
		}
	}
}
