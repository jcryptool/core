package org.jcryptool.visual.aco.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ToolTip;
import org.jcryptool.visual.aco.controller.AntColEventController;
import org.jcryptool.visual.aco.model.CommonModel;

public class AntColConfigComposite extends Composite {

	private StyledText txtCipher;
	private Spinner keyLengthSlider;
	private Button startAnalysisButton;
	private Button generateTextButton;
	private Combo languageCombo;
	protected Label labelSlider;
	private AntColEventController controller;
	private Group firstStepGroup;
	private int currKeyLength = 4;
	private Label textLengthLabel;
	private SelectionAdapter startAnalysisListener;
	private SelectionAdapter stopAnalysisListener;

	/**
	 * Konstruktor. Erhaelt das Model, das die Daten des Tutorials verwaltet und
	 * das Composite an das das Func-Objekt angehaengt werden soll.
	 * 
	 * @param model
	 *            Model des Tutorials
	 * @param c
	 *            Parent
	 */
	public AntColConfigComposite(CommonModel model, Composite c) {
		super(c, SWT.NONE);
		setLayout(new GridLayout(1, false));
		initConfigurationComp();
	}

	private void initConfigurationComp() {
		firstStepGroup = new Group(this, SWT.NONE);

		firstStepGroup.setText(Messages.Func_analyseConfiguration); //$NON-NLS-1$
		GridLayout layout = new GridLayout(1, false);
		firstStepGroup.setLayout(layout);

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);

		firstStepGroup.setLayoutData(gridData);
		generateTextButton = new Button(firstStepGroup, SWT.PUSH);
		generateTextButton.setText(Messages.Control_generateText); //$NON-NLS-1$
		generateTextButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false));
		generateTextButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controller.onGenerateText();
			}
		});

		Label label = new Label(firstStepGroup, SWT.NONE);
		label.setText(Messages.Control_or); //$NON-NLS-1$
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		label = new Label(firstStepGroup, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		label.setText(Messages.Func_ciphertext); //$NON-NLS-1$
		txtCipher = new StyledText(firstStepGroup, SWT.SINGLE | SWT.BORDER);
		txtCipher.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		textLengthLabel = new Label(firstStepGroup, SWT.FILL);
		textLengthLabel.setText(Messages.Control_textLength + " 0"); //$NON-NLS-1$

		labelSlider = new Label(firstStepGroup, SWT.NONE);
		labelSlider.setText(Messages.Func_keyLength); //$NON-NLS-1$ //$NON-NLS-2$
		GridData gd_labelSlider = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_labelSlider.verticalIndent = 30;
		labelSlider.setLayoutData(gd_labelSlider);

		keyLengthSlider = new Spinner(firstStepGroup, SWT.BORDER);

		keyLengthSlider.setMinimum(3);
		keyLengthSlider.setMaximum(9);
		keyLengthSlider.setSelection(4);
		keyLengthSlider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		keyLengthSlider.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				controller.onKeyLengthChange(keyLengthSlider.getSelection(),
						currKeyLength);
				currKeyLength = keyLengthSlider.getSelection();
			}
		});

		label = new Label(firstStepGroup, SWT.FILL);
		label.setText(Messages.Func_textLanguage); //$NON-NLS-1$
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_label.verticalIndent = 30;
		label.setLayoutData(gd_label);

		languageCombo = new Combo(firstStepGroup, SWT.READ_ONLY);
		String items[] = { Messages.Control_language1,
				Messages.Control_language2, Messages.Control_language_own };
		languageCombo.setItems(items);
		languageCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false));

		languageCombo.select(0);
		languageCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controller.onChangeLanguage();
			}
		});

		// Weiter-Button

		startAnalysisButton = new Button(firstStepGroup, SWT.PUSH);
		startAnalysisButton.setText(Messages.Func_proceedToAnalysis); //$NON-NLS-1$
		startAnalysisButton.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM,
				true, true));
		startAnalysisButton.setEnabled(false);
		startAnalysisListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controller.onStartAnalyseButtonClick();
				toggleAnalyseButton();
			}
		};
		stopAnalysisListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controller.onStopAnalyseButtonClick();
				toggleAnalyseButton();
			}
		};
		startAnalysisButton.addSelectionListener(startAnalysisListener);
		addCipherTextListener();

		layout();
	}

	private void addCipherTextListener() {
		final ToolTip tip = new ToolTip(firstStepGroup.getShell(), SWT.BALLOON);

		tip.setMessage(Messages.Control_wrongInputToolTip);
		txtCipher.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				int cursor = txtCipher.getCaretOffset();
				String text = txtCipher.getText();
				String modifiedText = text.toUpperCase().replaceAll("[^A-Z]",
						"");
				txtCipher.removeModifyListener(this);
				txtCipher.setText(modifiedText);
				txtCipher.addModifyListener(this);
				txtCipher.setCaretOffset(cursor);

				textLengthLabel.setText(Messages.Control_textLength
						+ " " + modifiedText.length()); //$NON-NLS-1$
				firstStepGroup.layout();
				controller.onCipherTextModify(modifiedText);
			}

		});

		final KeyListener keyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				String key = Character.toString(e.character);
				if (key.matches("[^A-Za-z]")) {
					StyledText actionWidget = (StyledText) e.widget;
					Point loc = actionWidget.toDisplay(actionWidget
							.getLocation());
					tip.setLocation(loc.x + 150, loc.y - 60);

					tip.setAutoHide(true);
					tip.setVisible(true);
				} else {
					tip.setVisible(false);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		};
		txtCipher.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				txtCipher.addKeyListener(keyListener);
			}

			@Override
			public void focusLost(FocusEvent e) {
				txtCipher.removeKeyListener(keyListener);
				tip.setVisible(false);
			}
		});
	}

	public void setAllChildsEnabled(boolean enabled) {
		for (Control child : firstStepGroup.getChildren()) {
			child.setEnabled(enabled);
		}
	}

	public void addController(AntColEventController reg) {
		this.controller = reg;
	}

	public String getLanguageComboSelection() {
		return languageCombo.getText();
	}

	public void setLanguageComboSelection(String lang) {
		languageCombo.setText(lang);
	}

	public void setEnabledStartAnalyseButton(boolean b) {
		startAnalysisButton.setEnabled(b);
	}

	public void toggleAnalyseButton() {
		if (startAnalysisButton.getText().equals(
				Messages.Func_proceedToAnalysis)) {
			startAnalysisButton.removeSelectionListener(startAnalysisListener);
			startAnalysisButton.setText(Messages.Func_stopAnalysis);
			startAnalysisButton.addSelectionListener(stopAnalysisListener);
		} else {
			startAnalysisButton.removeSelectionListener(stopAnalysisListener);
			startAnalysisButton.setText(Messages.Func_proceedToAnalysis);
			startAnalysisButton.addSelectionListener(startAnalysisListener);
		}

	}

	public String getCipherTextFieldValue() {
		return txtCipher.getText();
	}

	public void setCipherTextFieldValue(String text) {
		txtCipher.setText(text);
	}

	public int getKeyLengthSliderValue() {
		return keyLengthSlider.getSelection();

	}

	public void setKeyLengthSliderValue(int length) {
		keyLengthSlider.setSelection(length);
		controller.onKeyLengthChange(keyLengthSlider.getSelection(), length);
		currKeyLength = length;
	}

	public void showNoVisualToolTip() {
		final ToolTip tip = new ToolTip(keyLengthSlider.getShell(), SWT.BALLOON);
		tip.setMessage(Messages.Control_noVisualAvailable);
		tip.setVisible(true);

	}

}
