package org.jcryptool.visual.aco.controller;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.visual.aco.model.CommonModel;
import org.jcryptool.visual.aco.view.AntColGraphComposite;
import org.jcryptool.visual.aco.view.AntColView;
import org.jcryptool.visual.aco.view.AntColVisualComposite;

public class AntColVisualController implements
		AntColEvents {

	private AntColEventController registry;
	private CommonModel model;
	private AntColVisualComposite visualComp;
	private AntColGraphComposite graphComp;

	public AntColVisualController(CommonModel model, AntColView view,
			AntColEventController antColControllerRegistry) {

		this.model = model;
		this.visualComp = view.getVisualComp();
		this.graphComp = view.getVisualComp().getGraphComp();
		this.registry = antColControllerRegistry;
	}

	public void onNextKnot() {
		if (model.isAnimateable() && model.isGraphDisplayed()) {
			if (!graphComp.isAnimationRunning()) {
				graphComp.animationStep();
				visualComp.setEnabledRadioGroup(false);
			}
		} else {
			graphComp.redraw();
		}
	}

	public void onPlaceNewAnt() {
		updateView();
	}

	private void animateToLastKnot() {
		final Display d = visualComp.getDisplay();
		Runnable runnable = new Runnable() {
			public void run() {
				if (model.isAtLastKnot()) {
					return;
				} else if (!graphComp.isAnimationRunning()) {
					model.toNextKnot();
					onNextKnot();
				}
				d.timerExec(200, this);
			}
		};
		d.timerExec(1, runnable); // Starten
	}

	private void toLastKnot() {
		while (!model.isAtLastKnot()) {
			model.toNextKnot();
			onNextKnot();
		}
	}

	public void onFinishCycle() {
		if (!model.isAnimateable() || !model.isGraphDisplayed()) {
			toLastKnot();
		} else {
			animateToLastKnot();
		}
	}

	public void onSelectShowGraph() {
		visualComp.setTopContolStackLayout(visualComp.getGraphComp());
		graphComp.redraw();
	}

	public void onSelectShowMatrix() {
		visualComp.setTopContolStackLayout(visualComp.getMatrixComp());
	}

	public void afterGraphDrawn() {
		if (model.isSingleStep()) {
			visualComp.setEnabledRadioGroup(true);
		}
	}

	public void onStartAnalyseButtonClick() {
		if (model.isVisualizable()) {
			visualComp.setEnabledRadioGroup(true);
		}
		updateView();
	}

	public void onSelectMultipleSteps() {

		visualComp.setEnabledRadioGroup(false);
		visualComp.setSelectionShowGraphRadio(false);
		registry.onSelectShowMatrix();
	}

	public void onSelectSingleStep() {
		if (model.isVisualizable()) {
			visualComp.setEnabledRadioGroup(true);
		}
	}

	public void onConfigurationChange() {
		model.setDisplayGraph(model.isVisualizable());
		visualComp.setSelectionShowGraphRadio(model.isVisualizable());
		Composite comp = visualComp.getMatrixComp();
		if (model.isVisualizable()) {
			comp = visualComp.getGraphComp();
		}
		comp.redraw();

		visualComp.setTopContolStackLayout(comp);
	}

	public void updateView() {
		visualComp.getCurrentDisplyedComp().redraw();
	}

	@Override
	public void onChangeAnimationCheckboxValue(boolean value) {
	 
		
	}

	@Override
	public void onModifyConfiguration() {
	 
		
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
		visualComp.setEnabledRadioGroup(false);
		updateView();
	}

}
