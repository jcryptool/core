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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartAnalyseButtonClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNextKnot() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinishCycle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterGraphDrawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSelectMultipleSteps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSelectSingleStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChangeAnimationCheckboxValue(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onModifyConfiguration() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConfigurationChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlaceNewAnt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewIteration() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyLengthChange(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGenerateText() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCipherTextModify(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAlphaChanged(double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBetaChanged(double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPheromoneChanged(double value) {
		// TODO Auto-generated method stub
		
	}
}
