package org.jcryptool.visual.aco.controller;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.jcryptool.visual.aco.model.CommonModel;
import org.jcryptool.visual.aco.view.AntColConfigComposite;
import org.jcryptool.visual.aco.view.AntColView;
import org.jcryptool.visual.aco.view.Messages;
import org.jcryptool.visual.aco.wizard.TranspTextWizard;
import org.jcryptool.visual.aco.wizard.TranspTextWizardPage.PageConfiguration;

public class AntColConfigController implements AntColEvents{

	private AntColEventController commonController;
	private CommonModel model;
	private AntColConfigComposite configComp;

	public AntColConfigController(CommonModel model, AntColView view,
			AntColEventController antColControllerRegistry) {
		this.model = model;
		this.configComp = view.getConfigComp();
		this.commonController = antColControllerRegistry;
	}

	public void onStartAnalyseButtonClick() {
		configComp.setEnabled(false);
	}

	public void onChangeLanguage() {
		if (configComp.getLanguageComboSelection().equals(
				Messages.Control_language2)) {
			model.setLanguage(Messages.Control_language2_short);
		} else {
			model.setLanguage(Messages.Control_language1_short);
		}

	};

	private void checkTextInput() {
		if (configComp.getCipherTextFieldValue().length() < model.getSize()) {
			configComp.setEnabledStartAnalyseButton(false);
		} else {
			configComp.setEnabledStartAnalyseButton(true);
		}
	}

	public void onCipherTextModify(String text) {
		checkTextInput();
	}
	
	public void onKeyLengthChange(int value){ 
		checkTextInput();
	}


	public void onModifyConfiguration(){
		configComp.setEnabled(true);
	}
	
	public void onGenerateText() {
		TranspTextWizard textWizard = new TranspTextWizard(configComp.getKeyLengthSliderValue());
		WizardDialog dialog = new WizardDialog(configComp.getShell(),
				textWizard);

		int result = dialog.open();

		if (result == Window.OK) {
			PageConfiguration textPageConfiguration = textWizard
					.getTextPageConfig();

			configComp.setCipherTextFieldValue(textPageConfiguration
					.getReoderedText());
			commonController.onConfigurationChange();
		}
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
