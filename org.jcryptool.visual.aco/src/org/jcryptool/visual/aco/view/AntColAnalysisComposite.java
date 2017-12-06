package org.jcryptool.visual.aco.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ToolTip;
import org.jcryptool.visual.aco.controller.AntColEventController;
import org.jcryptool.visual.aco.model.CommonModel;

public class AntColAnalysisComposite extends Composite {

	private CommonModel model;
	private Button selectSingleRadio;
	private Button newAntButton;
	private Button nextKnotButton;
	private Button finishCycleButton;
	private Button selectMultipleRadio;
	private Button newIterationButton;
	private Button animationCheckbox;
	private Group secondStepGroup;
	private Group stepByStepGroup;
	private Group allInOneStepGroup;
	private Group settingsGroup;
	private Spinner numOfIterationsSpinner;
	private Composite compAnimationController;
	private StackLayout stackLayout;
	protected Label labelSlider;

	private AntColEventController controller;

	/**
	 * Konstruktor. Erhaelt das Model, das die Daten des Tutorials verwaltet und
	 * das Composite an das das Func-Objekt angehaengt werden soll.
	 * 
	 * @param model
	 *            Model des Tutorials
	 * @param c
	 *            Parent
	 */
	public AntColAnalysisComposite(CommonModel model, Composite c) {
		super(c, SWT.NONE);
		this.model = model;
		setLayout(new GridLayout(1, false));
		initAnalysisComp();
	}

	/**
	 * Setzt die Einstellungen fuer Schritt 1 des Tutorials
	 * 
	 */
	private void initAnalysisComp() {
		// Initialisierung der Variablen, anhaengen von Listenern

		secondStepGroup = new Group(this, SWT.NONE);
		secondStepGroup.setText(Messages.Func_analyseGroupLabel); //$NON-NLS-1$
		secondStepGroup.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		secondStepGroup.setLayoutData(gridData);
		
		Group groupSteps = new Group(secondStepGroup, SWT.NONE);
		groupSteps.setText(Messages.Func_analysisType); //$NON-NLS-1$
		groupSteps.setLayout(new GridLayout(1, false));
		groupSteps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		// Animation-Checkbox
		animationCheckbox = new Button(secondStepGroup, SWT.CHECK);
		animationCheckbox.setText(Messages.Func_animation); //$NON-NLS-1$
		animationCheckbox.setSelection(true);
		animationCheckbox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controller.onChangeAnimationCheckboxValue(animationCheckbox
						.getSelection());
			}
		});

		stackLayout = new StackLayout();
		compAnimationController = new Composite(secondStepGroup, SWT.NONE);
		compAnimationController.setLayout(stackLayout);
		compAnimationController.setLayoutData(new GridData(SWT.FILL,
				SWT.BEGINNING, true, false, 1, 1));

		stepByStepGroup = new Group(compAnimationController, SWT.NONE);
		stepByStepGroup.setText(Messages.Func_antAnalysis); //$NON-NLS-1$
		stepByStepGroup.setLayout(new GridLayout(1, false));
		stepByStepGroup.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING));

		stackLayout.topControl = stepByStepGroup;

		allInOneStepGroup = new Group(compAnimationController, SWT.NONE);
		allInOneStepGroup.setText(Messages.Func_antAnalysis); //$NON-NLS-1$
		allInOneStepGroup.setLayout(new GridLayout(1, false));
		allInOneStepGroup.setLayoutData(new GridData(SWT.BEGINNING,
				SWT.BEGINNING));

		selectSingleRadio = new Button(groupSteps, SWT.RADIO);
		selectSingleRadio.setText(Messages.Func_oneStep); //$NON-NLS-1$
		selectSingleRadio.setSelection(true);
		selectSingleRadio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				stackLayout.topControl = stepByStepGroup;
				compAnimationController.layout();
				controller.onSelectSingleStep();
			}
		});

		selectMultipleRadio = new Button(groupSteps, SWT.RADIO);
		selectMultipleRadio.setText(Messages.Func_radioCompleteRound); //$NON-NLS-1$
		selectMultipleRadio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				stackLayout.topControl = allInOneStepGroup;
				compAnimationController.layout();
				controller.onSelectMultipleSteps();
			}
		});

		this.initAllInOneGroup();
		this.initStepByStepGroup();
		this.initSettingsGroup();

		/*
		 * Composite filler = new Composite(secondStepGroup, SWT.NONE);
		 * filler.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, true, true));
		 * // Weiter-Button resetButton = new Button(secondStepGroup, SWT.PUSH);
		 * resetButton.setText(Messages.Func_reset); //$NON-NLS-1$ resetButton
		 * .setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		 * resetButton.addSelectionListener(new SelectionAdapter() { public void
		 * widgetSelected(SelectionEvent e) {
		 * controller.onModifyConfiguration(); } });
		 */
		this.setEnabled(false);
		this.layout();

	}

	private void initSettingsGroup() {

		settingsGroup = new Group(secondStepGroup, SWT.NONE);
		settingsGroup.setText(Messages.Func_settings); //$NON-NLS-1$
		settingsGroup.setLayout(new GridLayout(1, false));
		GridData gd_settingsGroups = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_settingsGroups.verticalIndent = 30;
		settingsGroup.setLayoutData(gd_settingsGroups);

		// Alpha-Label
		final Label alpha = new Label(settingsGroup, SWT.TOP);
		final String str = Messages.Func_alpha; //$NON-NLS-1$
		alpha.setText(str + " " + model.getAlpha());
		alpha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// Alpha-Slider
		final Slider alphaslider = new Slider(settingsGroup, SWT.HORIZONTAL);
		alphaslider.setValues(80, 0, 101, 1, 10, 1);
		alphaslider.setEnabled(true);
		alphaslider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		alphaslider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				controller.onAlphaChanged((double) alphaslider.getSelection() / 100);
				alpha.setText(str + " " + model.getAlpha());
			}
		});

		// Beta-Label
		final Label beta = new Label(settingsGroup, SWT.TOP);
		final String str2 = Messages.Func_beta; //$NON-NLS-1$
		beta.setText(str2 + " " + model.getBeta());
		beta.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// Beta-Slider
		final Slider betaslider = new Slider(settingsGroup, SWT.HORIZONTAL);
		betaslider.setValues(80, 0, 101, 1, 10, 1);
		betaslider.setEnabled(true);
		betaslider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		betaslider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				controller.onBetaChanged((double) betaslider.getSelection() / 100);
				beta.setText(str2 + " " + model.getBeta());
			}
		});

		// Verdunstung-Label
		final Label verd = new Label(settingsGroup, SWT.TOP);
		final String str3;
		str3 = Messages.Func_evap; //$NON-NLS-1$
		verd.setText(str3 + " " + model.getVerd());
		verd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// Verdunstung-Slider
		final Slider verdslider = new Slider(settingsGroup, SWT.HORIZONTAL);
		verdslider.setValues(90, 1, 101, 1, 10, 1);
		verdslider.setEnabled(true);
		verdslider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		verdslider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				controller.onPheromoneChanged((double) verdslider
						.getSelection() / 100);
				verd.setText(str3 + " " + model.getVerd());
			}
		});

	}

	private void initAllInOneGroup() {

		// Setzen-Button

		// Repeat-Button
		newIterationButton = new Button(allInOneStepGroup, SWT.PUSH);
		newIterationButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		newIterationButton.setText(Messages.Func_doWithNewAnt); //$NON-NLS-1$
		newIterationButton.setEnabled(false);
		newIterationButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controller.onNewIteration();
			}
		});

		Label label = new Label(allInOneStepGroup, SWT.NONE);
		label.setText(Messages.Analysis_multipleCycles); //$NON-NLS-1$

		numOfIterationsSpinner = new Spinner(allInOneStepGroup, SWT.BORDER);
		numOfIterationsSpinner.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false));
		numOfIterationsSpinner.setMinimum(1);
		numOfIterationsSpinner.setMaximum(100);

	}

	private void initStepByStepGroup() {

		// Schritt-Button
		nextKnotButton = new Button(stepByStepGroup, SWT.PUSH);
		nextKnotButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		nextKnotButton.setText(Messages.Func_step); //$NON-NLS-1$
		nextKnotButton.setEnabled(false);
		nextKnotButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controller.onNextKnot();
			}
		});
		// Durchlauf-Button
		finishCycleButton = new Button(stepByStepGroup, SWT.PUSH);
		finishCycleButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		finishCycleButton.setText(Messages.Func_cycle); //$NON-NLS-1$
		finishCycleButton.setEnabled(false);
		finishCycleButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controller.onFinishCycle();
			}
		});

		newAntButton = new Button(stepByStepGroup, SWT.PUSH);
		newAntButton
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		newAntButton.setText(Messages.Func_newAnt); //$NON-NLS-1$
		newAntButton.setEnabled(false);
		newAntButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controller.onPlaceNewAnt();
			}
		});
		addToolTip();
	}

	
	private void addToolTip(){
		final ToolTip tip = new ToolTip(newAntButton.getShell(), SWT.BALLOON);
		tip.setMessage(Messages.Analysis_newAntButtonToolTip);
		newAntButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tip.setVisible(true);
			}
		});
	}
	
	
	public void disableAnimationCheckbox(boolean disable) {
		animationCheckbox.setEnabled(disable);
		animationCheckbox.setSelection(disable);
	}

	public void addController(AntColEventController reg) {
		this.controller = reg;
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (Control child : secondStepGroup.getChildren()) {
			child.setEnabled(enabled);
		}

		for (Control child : settingsGroup.getChildren()) {
			child.setEnabled(enabled);
		}

		animationCheckbox.setEnabled(enabled);
		newAntButton.setEnabled(enabled);
		nextKnotButton.setEnabled(enabled);
		finishCycleButton.setEnabled(enabled);
		newIterationButton.setEnabled(enabled);
		selectSingleRadio.setEnabled(enabled);
		selectMultipleRadio.setEnabled(enabled);
	}

	public boolean getAnimationCheckboxValue() {
		return animationCheckbox.getSelection();
	}

	public void setAnimationCheckboxValue(boolean selected) {
		animationCheckbox.setSelection(selected);
	}

	public void setEnabledAnimationCheckbox(boolean enabled) {
		animationCheckbox.setEnabled(enabled);
	}

	public void setEnabledPlaceAntButton(boolean enable) {
		newAntButton.setEnabled(enable);
	}

	public void setEnabledNextKnotButton(boolean enabled) {
		nextKnotButton.setEnabled(enabled);
	}

	public void setEnabledFinishCycleButton(boolean enabled) {
		finishCycleButton.setEnabled(enabled);
	}

	public void setEnabledNewIterationButton(boolean enabled) {
		newIterationButton.setEnabled(enabled);
	}

	public void setNumberOfIterationSpinner(int value) {
		numOfIterationsSpinner.setSelection(value);
	}

	public int getNumberOfIterationSpinnerValue() {
		return numOfIterationsSpinner.getSelection();
	}

	public void setSingleStepSelected(boolean selected) {
		selectSingleRadio.setSelection(selected);
		selectMultipleRadio.setSelection(!selected);
	}

	public void setEnabledStepRadioGroup(boolean b) {
		selectSingleRadio.setEnabled(b);
		selectMultipleRadio.setEnabled(b);
	}

	public Group getStepByStepComp() {
		return stepByStepGroup;
	}

	public Group getMultipleIterationComp() {
		return allInOneStepGroup;
	}

	public void setTopLayout(Control comp) {
		stackLayout.topControl = comp;
		compAnimationController.layout();
	}


}
