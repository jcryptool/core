package org.jcryptool.visual.aco.controller;

import org.jcryptool.visual.aco.model.CommonModel;
import org.jcryptool.visual.aco.view.AntColPherMatrixComposite;
import org.jcryptool.visual.aco.view.AntColView;

public class AntColPherMatrixController implements AntColEvents{

	private CommonModel model;
	private AntColPherMatrixComposite matrixComp;

	public AntColPherMatrixController(CommonModel model, AntColView view, AntColEventController antColControllerRegistry) {
		this.model = model;
		this.matrixComp = view.getVisualComp().getMatrixComp();
	}

	public void afterGraphDrawn(){
		if(model.isGraphDisplayed()){
			return;
		}
		matrixComp.redraw();
	}
	public void onSelectShowMatrix() {
		matrixComp.redraw();
	}

	@Override
	public void onSelectShowGraph() {
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
