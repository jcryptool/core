package org.jcryptool.visual.aco.view;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.aco.model.Model;

public class AntColControlComposite extends Composite {

	private Model model;
	private Button generateTextRadio;
	private Text permutationTextField;
	private Button existingTextRadio;
	private Text plainTextField_gen;
	private Slider permutationLengthSlider_gen;
	private Text txtCipher;
	private Slider sliderAnalyse;
	private Button toAnalyseButton;
	private Button selectSingle;
	private Button newAntButton;
	private Button stepButton;
	private Button finishCycleButton;
	private Button selectMultiple;
	private Button newIterationButton;
	private Button animationCheckbox;
	private Button reset;
	private boolean singleStep = true;
	private Group generateTextGroup;
	private Group existingTextGroup;
	private Text encryptedText;
	private Group secondStepGroup;
	private Group stepByStepGroup;
	private Group allInOneStepGroup;
	private Group settingsGroup;
	protected boolean stepByStep;
	private Combo languageCombo;
	private Spinner numOfIterationsSpinner;
	protected int numberOfIterations = 1;
	private Composite compAnimationController;
	private StackLayout stackLayout;

	/**
	 * Konstruktor. Erhaelt das Model, das die Daten des Tutorials verwaltet und
	 * das Composite an das das Func-Objekt angehaengt werden soll.
	 * 
	 * @param model
	 *            Model des Tutorials
	 * @param c
	 *            Parent
	 */
	public AntColControlComposite(Model model, Composite c) {
		super(c, SWT.NONE);
		this.model = model;
		setLayout(new GridLayout(2, false));
		initConfigurationComp();
		initControlComp();
	}

	private void initConfigurationComp() {
		Group firstStepGroup = new Group(this, SWT.NONE);
		
		firstStepGroup.setText(Messages.Func_analyseConfiguration); //$NON-NLS-1$
		firstStepGroup.setLayout(new GridLayout(1, false));

	    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true,
				false);
	    gridData.widthHint = 220;
		firstStepGroup.setLayoutData(gridData);
		Group groupSelect = new Group(firstStepGroup, SWT.NONE);
		groupSelect.setText(Messages.Func_possibleInputs); //$NON-NLS-1$
		groupSelect.setLayout(new GridLayout(1, false));
		groupSelect
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		final StackLayout stackLayout = new StackLayout();
		final Composite comp = new Composite(firstStepGroup, SWT.NONE);
		comp.setLayout(stackLayout);
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		generateTextRadio = new Button(groupSelect, SWT.RADIO);
		generateTextRadio.setText(Messages.Func_analyseCreated); //$NON-NLS-1$
		generateTextRadio.setSelection(true);
		generateTextRadio.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				model.reset(false);
				stackLayout.topControl = generateTextGroup;
				checkTextInput();
				model.setText(plainTextField_gen.getText());
				comp.layout();
			}
		});

		existingTextRadio = new Button(groupSelect, SWT.RADIO);
		existingTextRadio.setText(Messages.Func_analyseGiven); //$NON-NLS-1$
		existingTextRadio.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				stackLayout.topControl = existingTextGroup;
				model.reset(false);
				model.setText(txtCipher.getText());
				checkTextInput();
				comp.layout();
			}
		});

		this.initGenerateTextGroup(comp);
		this.initExistingTextGroup(comp);
		stackLayout.topControl = generateTextGroup;

		Label label = new Label(firstStepGroup, SWT.FILL);
		label.setText(Messages.Func_textLanguage); //$NON-NLS-1$
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		languageCombo = new Combo(firstStepGroup, SWT.READ_ONLY);
		languageCombo.setBounds(50, 50, 150, 65);
		String items[] = { Messages.Control_language1,
				Messages.Control_language2 };
		languageCombo.setItems(items);
		languageCombo.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true,
				false));

		languageCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (languageCombo.getText().equals(Messages.Control_language2)) {
					model.setLanguage(Messages.Control_language2_short);
				} else {
					model.setLanguage(Messages.Control_language1_short);
				}
			}
		});
		languageCombo.select(0);

		Composite filler = new Composite(firstStepGroup, SWT.NONE);
		filler.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// Weiter-Button
		toAnalyseButton = new Button(firstStepGroup, SWT.PUSH);
		toAnalyseButton.setText(Messages.Func_proceedToAnalysis); //$NON-NLS-1$
		toAnalyseButton.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true,
				false));
		toAnalyseButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (generateTextRadio.getSelection()) {
					model.setPerm(permutationTextField.getText());
					model.setSize(permutationLengthSlider_gen.getSelection());
					model.setText(plainTextField_gen.getText());
				} else {
					String perm = "1, 2"; //$NON-NLS-1$
					for (int i = 3; i < sliderAnalyse.getSelection() + 1; i++)
						perm += ", " + i; //$NON-NLS-1$
					model.setPerm(perm);
					model.setSize(sliderAnalyse.getSelection());
					model.setText(txtCipher.getText());
				}
				model.setAnalyse(existingTextRadio.getSelection());
				model.toAnalyse();
				toAnalyse();
			}
		});

		setEnabledFirstStep(true);
		layout();
	}

	public void toAnalyse() {
		setEnabledFirstStep(false);
		this.setEnabledSecondStep(true);
		String s = model.getCipher();
		encryptedText.setText(s);

		if (model.getTrail().size() == model.getSize()) {
			this.stepButton.setEnabled(false);
			this.finishCycleButton.setEnabled(false);
			this.newAntButton.setEnabled(true);
		}

		if (model.isVisualizable()) {
			animationCheckbox.setEnabled(true);
			// animationCheckbox.setSelection(true);
			selectSingle.setEnabled(true);
			if (this.numberOfIterations > 1) {
				animationCheckbox.setEnabled(false);
				animationCheckbox.setSelection(false);
			}
		} else {
			animationCheckbox.setEnabled(false);
			animationCheckbox.setSelection(false);
			selectSingle.setSelection(false);
			selectSingle.setEnabled(false);
			selectMultiple.setSelection(true);
			stackLayout.topControl = allInOneStepGroup;
			compAnimationController.layout();
		}
		model.setAnimateable(animationCheckbox.getSelection());
		layout();
	}

	private void initExistingTextGroup(Composite comp) {
		existingTextGroup = new Group(comp, SWT.NONE);
		existingTextGroup.setText(Messages.Func_analysis); //$NON-NLS-1$
		existingTextGroup.setLayout(new GridLayout(1, false));

		Label label = new Label(existingTextGroup, SWT.NONE);
		label.setText(Messages.Func_ciphertext); //$NON-NLS-1$

		txtCipher = new Text(existingTextGroup, SWT.SINGLE | SWT.BORDER);
		txtCipher.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		txtCipher.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				model.setText(txtCipher.getText());

				checkTextInput();

			}
		});

		final Label labelSlider = new Label(existingTextGroup, SWT.NONE);
		labelSlider.setText(Messages.Func_keyLength + " 4"); //$NON-NLS-1$ //$NON-NLS-2$

		sliderAnalyse = new Slider(existingTextGroup, SWT.NONE);
		sliderAnalyse.setValues(4, 3, 10, 1, 1, 1);
		sliderAnalyse.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		sliderAnalyse.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				labelSlider.setText(Messages.Func_keyLength
						+ " " + sliderAnalyse.getSelection()); //$NON-NLS-1$

				model.setSize(sliderAnalyse.getSelection());

				checkTextInput();
			}
		});
	}

	public void setEnabledFirstStep(boolean enabled) {
		for (Control child : existingTextGroup.getChildren()) {
			child.setEnabled(enabled);
		}

		for (Control child : generateTextGroup.getChildren()) {
			child.setEnabled(enabled);
		}

		languageCombo.setEnabled(enabled);
		toAnalyseButton.setEnabled(enabled);
		existingTextRadio.setEnabled(enabled);
		generateTextRadio.setEnabled(enabled);
	}

	private void initGenerateTextGroup(Composite comp) {
		generateTextGroup = new Group(comp, SWT.NONE);
		generateTextGroup.setText(Messages.Func_encryption); //$NON-NLS-1$
		generateTextGroup.setLayout(new GridLayout(1, false));
		generateTextGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		// Klartext-Label
		Label label = new Label(generateTextGroup, SWT.FILL);
		label.setText(Messages.Func_plaintext); //$NON-NLS-1$
		label.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

		// Klartext-Textfeld
		plainTextField_gen = new Text(generateTextGroup, SWT.BORDER);
		plainTextField_gen.setText(Messages.Func_initial_plaintext); //$NON-NLS-1$
		plainTextField_gen.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false));
		plainTextField_gen.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				model.setText(plainTextField_gen.getText());
				checkTextInput();
				encryptedText.setText(model.getCipher());
			}
		});

		// Schluessellaenge-Label
		final Label labelKeyLength = new Label(generateTextGroup, SWT.TOP);
		final String permKey = Messages.Func_keyLength; //$NON-NLS-1$
		labelKeyLength.setText(permKey.concat(" 4")); //$NON-NLS-1$
		labelKeyLength.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true,
				false));

		// Schluessellaenge-Slider
		permutationLengthSlider_gen = new Slider(generateTextGroup,
				SWT.HORIZONTAL);
		permutationLengthSlider_gen.setValues(4, 3, 10, 1, 1, 1);
		permutationLengthSlider_gen.setEnabled(true);
		permutationLengthSlider_gen.setLayoutData(new GridData(SWT.FILL,
				SWT.TOP, true, false));
		permutationLengthSlider_gen.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (permutationLengthSlider_gen.getSelection() != model
						.getSize()) {
					model.setSize(permutationLengthSlider_gen.getSelection());
					labelKeyLength.setText(permKey + " " + model.getSize());
					updatePermutationText();
					checkTextInput();

					encryptedText.setText(model.getCipher());
				}
			}
		});

		// Permutation-Label
		label = new Label(generateTextGroup, SWT.FILL);
		label.setText(Messages.Func_permutation); //$NON-NLS-1$
		label.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

		// Permutation-Eingabefeld
		permutationTextField = new Text(generateTextGroup, SWT.BORDER);
		permutationTextField.setTextLimit(17);
		updatePermutationText();
		permutationTextField.setLayoutData(new GridData(SWT.FILL, SWT.TOP,
				true, false));
		permutationTextField.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				String match = "[1-" + permutationLengthSlider_gen.getSelection() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
				for (int i = 1; i < permutationLengthSlider_gen.getSelection(); i++) {
					match += ",[1-" + permutationLengthSlider_gen.getSelection() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				if (permutationTextField.getText().matches(match))
					model.setPerm(permutationTextField.getText());

				checkTextInput();
				encryptedText.setText(model.getCipher());

			}
		});
		permutationTextField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				// allow arrow keys
				if (e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT) {
					return;
				}
				// not too long

				if ((permutationTextField.getText().length() >= 2 * permutationLengthSlider_gen
						.getSelection() - 1) && e.character != '\b')
					e.doit = false;
				// number already set
				if (Character.isDigit(e.character)
						&& permutationTextField.getText().contains(
								"" + e.character)) //$NON-NLS-1$
					e.doit = false;
				// number out of range
				if (Character.isDigit(e.character)
						&& (Integer.parseInt("" + e.character) > model.getSize() //$NON-NLS-1$
						|| Integer.parseInt("" + e.character) == 0)) //$NON-NLS-1$
					e.doit = false;
				// only digits and commas

				if (!((Character.isDigit(e.character) || (e.character == ',') || (e.character == '\b'))))
					e.doit = false;
			}

			public void keyReleased(KeyEvent e) {
				String match = "[1-" + permutationLengthSlider_gen.getSelection() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
				for (int i = 1; i < permutationLengthSlider_gen.getSelection(); i++) {
					match += ",[1-" + permutationLengthSlider_gen.getSelection() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				if (permutationTextField.getText().matches(match)) {
					checkTextInput();
				} else {
					toAnalyseButton.setEnabled(false);
				}
			}
		});

		label = new Label(generateTextGroup, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		label.setText(Messages.Show_encryptedText); //$NON-NLS-1$

		encryptedText = new Text(generateTextGroup, SWT.BORDER);
		encryptedText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		encryptedText.setEditable(false);
		String s = model.getCipher();
		encryptedText.setText(s);

	}

	private boolean checkTextInput() {
		boolean bool = true;
		if (model.getText().equals("")) { //$NON-NLS-1$
			bool = false;
		} else {
			if (existingTextRadio.getSelection()) {
				if (model.getCipher().length() == 0
						|| model.getCipher().length()
								% sliderAnalyse.getSelection() != 0) {
					bool = false;
				}
			}
		}

		toAnalyseButton.setEnabled(bool);
		return bool;

	}

	private void updatePermutationText() {
		String permutation = "1"; //$NON-NLS-1$
		for (int i = 2; i <= permutationLengthSlider_gen.getSelection(); i++) {
			permutation += "," + i;} //$NON-NLS-1$
		permutationTextField.setText(permutation);
	}

	private void selectSingleStep(boolean single) {
		if (model.getState() == 1 || model.getState() == 4)
			singleStep = single;
	}

	private boolean isSingleStepSelected() {
		return singleStep;
	}

	/**
	 * Setzt die Einstellungen fuer Schritt 1 des Tutorials
	 * 
	 */
	private void initControlComp() {
		// Initialisierung der Variablen, anhaengen von Listenern

		secondStepGroup = new Group(this, SWT.NONE);
		secondStepGroup.setText(Messages.Func_analyseGroupLabel); //$NON-NLS-1$
		secondStepGroup.setLayout(new GridLayout(1, false));
	    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true,
				false);
	    gridData.widthHint = 220;
	    secondStepGroup.setLayoutData(gridData);
		Group groupSteps = new Group(secondStepGroup, SWT.NONE);
		groupSteps.setText(Messages.Func_analysisType); //$NON-NLS-1$
		groupSteps.setLayout(new GridLayout(1, false));
		groupSteps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		// Animation-Checkbox
		animationCheckbox = new Button(secondStepGroup, SWT.CHECK);
		animationCheckbox.setText(Messages.Func_animation); //$NON-NLS-1$
		animationCheckbox.setSelection(true);
		animationCheckbox.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (!model.isWorking())
					model.setAnimateable(animationCheckbox.getSelection());
				else
					animationCheckbox.setSelection(!animationCheckbox
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

		selectSingle = new Button(groupSteps, SWT.RADIO);
		selectSingle.setText(Messages.Func_oneStep); //$NON-NLS-1$
		selectSingle.setSelection(true);
		selectSingle.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				if (!isSingleStepSelected()) {
					newAntButton.setEnabled(true);
					stepButton.setEnabled(false);
					finishCycleButton.setEnabled(false);
					newIterationButton.setEnabled(false);
					selectSingleStep(true);
				}

				stepByStep = true;
				stackLayout.topControl = stepByStepGroup;
				numberOfIterations = 1;
				if (model.isVisualizable()) {
					animationCheckbox.setEnabled(true);
				}
				numOfIterationsSpinner.setSelection(1);
				compAnimationController.layout();
			}
		});

		selectMultiple = new Button(groupSteps, SWT.RADIO);
		selectMultiple.setText(Messages.Func_radioCompleteRound); //$NON-NLS-1$
		selectMultiple.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				if (isSingleStepSelected()) {
					newIterationButton.setEnabled(true);
					selectSingleStep(false);
				}

				stepByStep = false;
				numberOfIterations = 1;
				if (model.isVisualizable()) {
					animationCheckbox.setEnabled(true);
				}
				numOfIterationsSpinner.setSelection(1);
				stackLayout.topControl = allInOneStepGroup;
				compAnimationController.layout();
			}
		});

		this.initAllInOneGroup();
		this.initStepByStepGroup();
		this.initSettingsGroup();

		Composite filler = new Composite(secondStepGroup, SWT.NONE);
		filler.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, true, true));
		// Weiter-Button
		reset = new Button(secondStepGroup, SWT.PUSH);
		reset.setText(Messages.Func_reset); //$NON-NLS-1$
		reset.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		reset.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (!model.isWorking()) {
					setEnabledFirstStep(true);
					setEnabledSecondStep(false);
					model.reset(true);
				}
			}
		});

		this.setEnabledSecondStep(false);
		this.layout();

	}

	private void initSettingsGroup() {
		settingsGroup = new Group(secondStepGroup, SWT.NONE);
		settingsGroup.setText(Messages.Func_settings); //$NON-NLS-1$
		settingsGroup.setLayout(new GridLayout(1, false));
		settingsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));

		// Alpha-Label
		final Label alpha = new Label(settingsGroup, SWT.TOP);
		final String str = Messages.Func_alpha; //$NON-NLS-1$
		alpha.setText(str + " " + model.getAlpha());
		alpha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// Alpha-Slider
		final Slider alphaslider = new Slider(settingsGroup, SWT.HORIZONTAL);
		alphaslider.setValues(80, 0, 101, 1, 10, 1);
		alphaslider.setEnabled(true);
		alphaslider
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		alphaslider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				model.setAlpha((double) alphaslider.getSelection() / 100);
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
		betaslider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		betaslider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				model.setBeta((double) betaslider.getSelection() / 100);
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
		verdslider.setValues(90, 0, 101, 1, 10, 1);
		verdslider.setEnabled(true);
		verdslider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		verdslider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				model.setVerd((double) verdslider.getSelection() / 100);
				verd.setText(str3 + " " + model.getVerd());
			}
		});

	}

	private void initAllInOneGroup() {

		final Display display = Display.getCurrent();
		// Setzen-Button

		// Repeat-Button
		newIterationButton = new Button(allInOneStepGroup, SWT.PUSH);
		newIterationButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		newIterationButton.setText(Messages.Func_doWithNewAnt); //$NON-NLS-1$
		newIterationButton.setEnabled(false);

		newIterationButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				if (!model.isWorking()) {
					if (model.getAntNr() != 1) {
						model.replaceAnt();
					}
					if (animationCheckbox.getSelection()) {
						setEnabledSecondStep(false);
						// emulates the button clicks at the end of the
						// animation
						Thread t = new Thread(new Runnable() {
							public void run() {
								while (model.isWorking()) {
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
									}
								}
								display.asyncExec(new Runnable() {
									public void run() {
										setEnabledSecondStep(true);
										// TODO step4();
										model.finishCycle();
									}
								});
							}
						});
						t.start();
					} else {
						model.silentViewRefresh(true);
						newIterationButton.setEnabled(false);
						int counter = numberOfIterations;
						while (counter > 0) {
							if (counter == 1) {
								model.silentViewRefresh(false);
								newIterationButton.setEnabled(true);
							}
							model.finishCycle();
							model.replaceAnt();

							counter--;
						}

					}
				}
			}
		});

		Label label = new Label(allInOneStepGroup, SWT.NONE);
		label.setText(Messages.Analysis_multipleCycles); //$NON-NLS-1$

		numOfIterationsSpinner = new Spinner(allInOneStepGroup, SWT.BORDER);
		numOfIterationsSpinner.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false));
		numOfIterationsSpinner.setMinimum(1);
		numOfIterationsSpinner.setMaximum(100);

		numOfIterationsSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean active = numOfIterationsSpinner.getSelection() == 1;
				numberOfIterations = numOfIterationsSpinner.getSelection();

				animationCheckbox.setEnabled(active);
				animationCheckbox.setSelection(active);
				model.setAnimateable(active);
			}
		});

	}

	private void initStepByStepGroup() {

		// Schritt-Button
		stepButton = new Button(stepByStepGroup, SWT.PUSH);
		stepButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		stepButton.setText(Messages.Func_step); //$NON-NLS-1$
		stepButton.setEnabled(false);
		stepButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (!model.isWorking()) {
					stepButton.setEnabled(false);
					finishCycleButton.setEnabled(false);
					// model.startAnimation();
					model.toNextKnot();
				}
			}
		});

		// Durchlauf-Button
		finishCycleButton = new Button(stepByStepGroup, SWT.PUSH);
		finishCycleButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		finishCycleButton.setText(Messages.Func_cycle); //$NON-NLS-1$
		finishCycleButton.setEnabled(false);
		finishCycleButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (!model.isWorking()) {
					stepButton.setEnabled(false);
					finishCycleButton.setEnabled(false);
					model.finishCycle();
				}
			}
		});

		newAntButton = new Button(stepByStepGroup, SWT.PUSH);
		newAntButton
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		newAntButton.setText(Messages.Func_newAnt); //$NON-NLS-1$
		newAntButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				model.replaceAnt();
				newAntButton.setEnabled(false);
				stepButton.setEnabled(true);
				finishCycleButton.setEnabled(true);
			}
		});
		newAntButton.setEnabled(false);

	}

	public void setEnabledSecondStep(boolean enabled) {
		for (Control child : secondStepGroup.getChildren()) {
			child.setEnabled(enabled);
		}

		for (Control child : settingsGroup.getChildren()) {
			child.setEnabled(enabled);
		}
		if (!enabled) {
			// newAntButton is handled only
			newAntButton.setEnabled(false);
		}
		stepButton.setEnabled(enabled);
		finishCycleButton.setEnabled(enabled);
		newIterationButton.setEnabled(enabled);
		selectSingle.setEnabled(enabled);
		selectMultiple.setEnabled(enabled);
	}
}
