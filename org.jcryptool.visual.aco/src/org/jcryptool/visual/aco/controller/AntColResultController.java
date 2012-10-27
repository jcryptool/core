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
		// TODO Auto-generated method stub

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
		this.refreshView();
	}

	private void refreshView() {
		resultComp.setResultText(model.toText(false),
				ConvertToString(model.getTrail()), model.toText(true),
				ConvertToString(model.getBestTrail()), model.getAntNr()); //$NON-NLS-1$);
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
		refreshView();
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
