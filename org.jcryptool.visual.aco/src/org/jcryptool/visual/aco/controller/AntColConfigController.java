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

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.jcryptool.visual.aco.model.CommonModel;
import org.jcryptool.visual.aco.view.AntColConfigComposite;
import org.jcryptool.visual.aco.view.AntColView;
import org.jcryptool.visual.aco.view.Messages;
import org.jcryptool.visual.aco.wizard.TranspTextWizard;
import org.jcryptool.visual.aco.wizard.TranspTextWizardPage.PageConfiguration;

public class AntColConfigController implements AntColEvents {

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
		configComp.setAllChildsEnabled(false);
		configComp.setEnabledStartAnalyseButton(true);
	}


	public void onStopAnalyseButtonClick() {
		configComp.setAllChildsEnabled(true);
	}
	public void onChangeLanguage() {
		if (configComp.getLanguageComboSelection().equals(
				Messages.Control_language2)) {
			model.setLanguage(Messages.Control_language2_short);
		} else if (configComp.getLanguageComboSelection().equals(
				Messages.Control_language_own)) {

			FileDialog fd = new FileDialog(configComp.getShell(), SWT.OPEN);
			// fd.setText(Messages.TranspTextWizardPage_windowsfiledialogtitle);
			fd.setFilterPath(null);
			String[] filterExt = { "*" }; //$NON-NLS-1$
			fd.setFilterExtensions(filterExt);
			String selected = fd.open();
			if (selected != null) {
				model.setLanguage(selected);
			} else {
				configComp
						.setLanguageComboSelection(Messages.Control_language1_short);
			}

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

	public void onKeyLengthChange(int value, int oldValue) {
		checkTextInput();
		if (oldValue == 5 && value == 6) {
			configComp.showNoVisualToolTip();
		}
	}

	public void onModifyConfiguration() {
		configComp.setEnabled(true);
	}

	public void onGenerateText() {
		TranspTextWizard textWizard = new TranspTextWizard(
				configComp.getKeyLengthSliderValue());
		WizardDialog dialog = new WizardDialog(configComp.getShell(),
				textWizard);

		int result = dialog.open();

		if (result == Window.OK) {
			PageConfiguration textPageConfiguration = textWizard
					.getTextPageConfig();

			configComp.setCipherTextFieldValue(textPageConfiguration
					.getReoderedText());
			configComp.setKeyLengthSliderValue(textPageConfiguration
					.getColumnCount());
			commonController.onConfigurationChange();
		}
	}

	@Override
	public void onSelectShowGraph() {

	}

	@Override
	public void onSelectShowMatrix() {

	}

	@Override
	public void onNextKnot() {
		configComp.setEnabledStartAnalyseButton(false);
	}

	@Override
	public void onFinishCycle() {
		configComp.setEnabledStartAnalyseButton(false);
	}

	@Override
	public void afterGraphDrawn() {
		configComp.setEnabledStartAnalyseButton(true);
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
	public void onConfigurationChange() {

	}

	@Override
	public void onPlaceNewAnt() {

	}

	@Override
	public void onNewIteration() {

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

}
