//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----

package org.jcryptool.crypto.modern.stream.lfsr.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.modern.stream.lfsr.LfsrPlugin;

public class LfsrWizardPage extends WizardPage implements Listener {

	public enum DisplayOption {
		OUTPUT_ONLY, OUTPUT_AND_KEYSTREAM, KEYSTREAM_ONLY
	}

	/** Used to override a listener, since the change is performed on purpose! */
	private boolean CLEARING_FLAG = false;

	private Composite pageComposite;

	private Composite lfsrLengthGroupHolderComposite;
	private Group lfsrLengthGroup;
	private Label lfsrLengthLabel;
	private Spinner lfsrLengthSpinner;

	private Composite noteLabelComposite;
	private Label noteLabel;

	private Label lfsrLengthNoteLabel;
	private Button resetButton;

	private Group tapSettingsGroup;
	private ArrayList<Button> tapSettingsCheckBoxes = new ArrayList<Button>();
	private ArrayList<Label> tapSettingsPlaceHolderLabels = new ArrayList<Label>();
	private Text tapSettings01StringText;

	private Group seedValueGroup;
	private ArrayList<Button> seedValueTapSettingsDisplayCheckBoxes = new ArrayList<Button>();
	private ArrayList<Spinner> seedValueSpinners = new ArrayList<Spinner>();
	private Text seedValue01StringText;

	private Group displayOptionsGroup;
	private Button displayOutputOnlyButton;
	private Button displayOutputAndKeystreamButton;
	private Button displayKeystreamOnlyButton;
	private Text keystreamLengthText;

	/** The length of the LFSR */
	private int lfsrLength = LfsrWizard.MAX_LFSR_LENGTH;

	/** The position of the LFSR taps */
	private boolean tapSettings[] = new boolean[LfsrWizard.MAX_LFSR_LENGTH];

	/** The initial value of the LFSR */
	private boolean seed[] = new boolean[LfsrWizard.MAX_LFSR_LENGTH];

	private DisplayOption displayOption = DisplayOption.OUTPUT_ONLY;
	private String keystreamLengthValue = ""; //$NON-NLS-1$

	/** Contains all decimal digits for convenience */
	private ArrayList<String> decValues = new ArrayList<String>(10);
//	private static final int KEY_MAX_VALUE = 1024;

	private boolean[] List2Array(List<Boolean> lst) {
		boolean[] result = new boolean[lst.size()];
		for (int i = 0; i < lst.size(); i++) {
			boolean b = lst.get(i);
			result[i] = b;
		}
		return result;
	}

	private List<Boolean> Array2List(boolean[] arr) {
		List<Boolean> result = new LinkedList<Boolean>();
		for (Boolean b : arr) {
			result.add(b);
		}
		return result;
	}

	private boolean[] reverseBoolArray(boolean[] input) {
		List<Boolean> temp = Array2List(input);
		Collections.reverse(temp);
		return List2Array(temp);
	}

	private void setSeedArray(boolean s[]) {
		this.seed = reverseBoolArray(s);
	}

	private boolean[] getSeedArray() {
		return reverseBoolArray(this.seed);
	}

	private void setSeedPosition(int idx, boolean val) {
		boolean[] temp = getSeedArray();
		temp[idx] = val;
		this.setSeedArray(temp);
	}

	/**
	 * Creates a new instance of LfsrWizardPage.
	 */
	public LfsrWizardPage() {
		super(".", "LFSR", null); //$NON-NLS-1$ //$NON-NLS-2
		setTitle(Messages.LfsrWizardPage_0);
		setMessage(Messages.LfsrWizardPage_1);
		setupDecimalValues();
	}

	/**
	 * Sets up an ArrayList with decimal values for convenience.
	 */
	private void setupDecimalValues() {
		decValues.add(0, "0"); //$NON-NLS-1$
		decValues.add(1, "1"); //$NON-NLS-1$
		decValues.add(2, "2"); //$NON-NLS-1$
		decValues.add(3, "3"); //$NON-NLS-1$
		decValues.add(4, "4"); //$NON-NLS-1$
		decValues.add(5, "5"); //$NON-NLS-1$
		decValues.add(6, "6"); //$NON-NLS-1$
		decValues.add(7, "7"); //$NON-NLS-1$
		decValues.add(8, "8"); //$NON-NLS-1$
		decValues.add(9, "9"); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		pageComposite = new Composite(parent, SWT.NONE);
		pageComposite.setLayout(new GridLayout());

		createLfsrLengthGroup(pageComposite);
		createNoteLabel(pageComposite);
		createTapSettingsGroup(pageComposite);
		createSeedValueGroup(pageComposite);
		createDisplayOptionsGroup(pageComposite);

		lfsrLength = lfsrLengthSpinner.getSelection();
		setFinalTapSetting();
		setSeedValueTapSettingDisplayVisibility(lfsrLength - 1, true);
		displayOutputOnlyButton.setSelection(true);

		setControl(pageComposite);
		setPageComplete(mayFinish());

		loadSavedSettings();

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), LfsrPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
	}

	private boolean loadSavedSettings() {
		// Check if s savefile exists.
		// If one exists continue, else return with false.
		File file = new File(System.getProperty("user.home") + "/Documents/.jcryptool/LFSR/savedSettings.txt"); //$NON-NLS-1$ //$NON-NLS-2$
		if (!file.exists()) {
			return false;
		}
		String seedAsString = "", tapAsString = "", loadedLfsrLengthAsString = "", outputOptionAsString = "",
				keystreamLengthAsString = "";
		// Load the tap and seed from the savefile into two different strings.
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			// Not a beautiful solution but it works.
			// The goal is to read the third and fifth line.
			br.readLine();
			br.readLine();
			loadedLfsrLengthAsString = br.readLine();
			br.readLine();
			seedAsString = br.readLine();
			br.readLine();
			tapAsString = br.readLine();
			br.readLine();
			outputOptionAsString = br.readLine();
			if (outputOptionAsString.equals(DisplayOption.KEYSTREAM_ONLY.toString())) {
				br.readLine();
				keystreamLengthAsString = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			LogUtil.logError(LfsrPlugin.PLUGIN_ID, e);
		}

		lfsrLength = Integer.parseInt(loadedLfsrLengthAsString);
		char[] seedAsCharArray = seedAsString.toCharArray();
		char[] tapAsCharArray = tapAsString.toCharArray();

		for (int i = 0; i < seedAsCharArray.length; i++) {
			if (seedAsCharArray[i] == '1') {
// 				seed[i] = true;
				setSeedPosition(i, true);
			} else {
// 				seed[i] = false;
				setSeedPosition(i, false);
			}
		}
		for (int i = 0; i < tapAsCharArray.length; i++) {
			if (tapAsCharArray[i] == '1') {
				tapSettings[i] = true;
			} else {
				tapSettings[i] = false;
			}
		}

		int counter = 0;
		Iterator<Button> tapSettingsCheckBoxesIterator = tapSettingsCheckBoxes.iterator();
		while (tapSettingsCheckBoxesIterator.hasNext()) {
			tapSettingsCheckBoxesIterator.next().setSelection(tapSettings[counter]);
			counter++;
		}

		counter = 0;
		Iterator<Button> seedValueTapSettingsDisplayCheckBoxesIterator = seedValueTapSettingsDisplayCheckBoxes
				.iterator();
		while (seedValueTapSettingsDisplayCheckBoxesIterator.hasNext()) {
			seedValueTapSettingsDisplayCheckBoxesIterator.next().setVisible(tapSettings[counter]);
			counter++;
		}

		counter = 0;
		Iterator<Spinner> seedValueSpinnersIterator = seedValueSpinners.iterator();
		while (seedValueSpinnersIterator.hasNext()) {
// 			if (seed[counter]) {
			if (getSeedArray()[counter]) {
				seedValueSpinnersIterator.next().setSelection(1);
			} else {
				seedValueSpinnersIterator.next().setSelection(0);
			}
			counter++;
		}

		// Set the LFSR length
		lfsrLengthSpinner.setSelection(lfsrLength);
		setSelectableTapSettingVisibilities();
		setEditableSeedValueVisibility();
		setFinalTapSetting();

		updateTapSetting01StringText();
		updateSeedValue01StringText();

		// Enable and disable the output options button
		// when needed set the keystream length.
		if (outputOptionAsString.equals(DisplayOption.OUTPUT_ONLY.toString())) {
			displayOutputOnlyButton.setSelection(true);
			displayOutputAndKeystreamButton.setSelection(false);
			displayKeystreamOnlyButton.setSelection(false);
			displayOutputOnlyButton.notifyListeners(SWT.Selection, new Event());
		} else if (outputOptionAsString.equals(DisplayOption.OUTPUT_AND_KEYSTREAM.toString())) {
			displayOutputOnlyButton.setSelection(false);
			displayOutputAndKeystreamButton.setSelection(true);
			displayKeystreamOnlyButton.setSelection(false);
			displayOutputAndKeystreamButton.notifyListeners(SWT.Selection, new Event());
		} else if (outputOptionAsString.equals(DisplayOption.KEYSTREAM_ONLY.toString())) {
			displayOutputOnlyButton.setSelection(false);
			displayOutputAndKeystreamButton.setSelection(false);
			displayKeystreamOnlyButton.setSelection(true);
			displayKeystreamOnlyButton.notifyListeners(SWT.Selection, new Event());
			// Set the keystream length to the textfield. The "FLAGS" are necessasry to
			// switch of the verfifyListener on keystreamLengthText. Without setting them to
			// TRUE before setting a text they will complain, that you should only enter
			// numbers.
			CLEARING_FLAG = true;
			keystreamLengthText.setText(keystreamLengthAsString);
			CLEARING_FLAG = false;
		}
		return true;
	}

	/**
	 * This method initializes lfsrLengthGroup.
	 *
	 * @param parent
	 */
	private void createLfsrLengthGroup(Composite parent) {
		GridData lfsrLengthGroupHolderCompositeGridData = new GridData(SWT.FILL, SWT.FILL, true, false);

		GridLayout lfsrLengthGroupHolderCompositeGridLayout = new GridLayout(2, false);
		lfsrLengthGroupHolderCompositeGridLayout.marginHeight = 0;
		lfsrLengthGroupHolderCompositeGridLayout.marginWidth = 0;

		GridData lfsrLengthLabelGridData = new GridData(SWT.FILL, SWT.CENTER, false, false);

		GridData lfsrLengthSpinnerGridData = new GridData(SWT.FILL, SWT.CENTER, false, false);

		GridLayout lfsrLengthGroupGridLayout = new GridLayout();
		lfsrLengthGroupGridLayout.numColumns = 3;

		GridData lfsrLengthGroupGridData = new GridData(SWT.FILL, SWT.FILL, false, false);

		GridData lfsrLengthNoteGridData = new GridData(SWT.FILL, SWT.CENTER, false, false);

		GridData resetButtonGridData = new GridData(SWT.RIGHT, SWT.TOP, true, false);

		lfsrLengthGroupHolderComposite = new Composite(parent, SWT.NONE);
		lfsrLengthGroupHolderComposite.setLayout(lfsrLengthGroupHolderCompositeGridLayout);
		lfsrLengthGroupHolderComposite.setLayoutData(lfsrLengthGroupHolderCompositeGridData);

		lfsrLengthGroup = new Group(lfsrLengthGroupHolderComposite, SWT.None);
		lfsrLengthGroup.setLayoutData(lfsrLengthGroupGridData);
		lfsrLengthGroup.setLayout(lfsrLengthGroupGridLayout);
		lfsrLengthGroup.setText(Messages.LfsrWizardPage_2);

		lfsrLengthLabel = new Label(lfsrLengthGroup, SWT.None);
		lfsrLengthLabel.setText(Messages.LfsrWizardPage_3);
		lfsrLengthLabel.setLayoutData(lfsrLengthLabelGridData);

		lfsrLengthSpinner = new Spinner(lfsrLengthGroup, SWT.BORDER);
		lfsrLengthSpinner.setMinimum(1);
		lfsrLengthSpinner.setMaximum(LfsrWizard.MAX_LFSR_LENGTH);
		lfsrLengthSpinner.setIncrement(1);
		lfsrLengthSpinner.setLayoutData(lfsrLengthSpinnerGridData);
		lfsrLengthSpinner.setSelection(LfsrWizard.MAX_LFSR_LENGTH);
		lfsrLengthSpinner.addListener(SWT.Modify, this);

		lfsrLengthNoteLabel = new Label(lfsrLengthGroup, SWT.NONE);
		lfsrLengthNoteLabel.setLayoutData(lfsrLengthNoteGridData);
		lfsrLengthNoteLabel.setForeground(ColorService.GRAY);
		lfsrLengthNoteLabel.setText(Messages.LfsrWizardPage_LFSRLengthNote);

		resetButton = new Button(lfsrLengthGroupHolderComposite, SWT.PUSH);
		resetButton.setLayoutData(resetButtonGridData);
		resetButton.setText(Messages.LfsrWizardPage_reset);
		resetButton.setToolTipText(Messages.LfsrWizardPage_resetTooltip);
		resetButton.setImage(ImageService.ICON_RESET);
		resetButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Delete the old savefile to enable resetting the plugin via the
				// createControl().
				File file = new File(System.getProperty("user.home") + "/Documents/.jcryptool/LFSR/savedSettings.txt"); //$NON-NLS-1$ //$NON-NLS-2$
				if (file.exists()) {
					file.delete();
				}

				// Temporarily save the parent composite of the whole wizard page
				Composite pageParent = pageComposite.getParent();

				// Dispose all elements of the old wizard page
				for (Control ctrl : pageParent.getChildren()) {
					ctrl.dispose();
				}

				// Reset all variables to its inital state
				CLEARING_FLAG = false;

				// I do not know if the lists must also be reset, but better safe than sorry.
				tapSettingsCheckBoxes = new ArrayList<Button>();
				tapSettingsPlaceHolderLabels = new ArrayList<Label>();

				seedValueTapSettingsDisplayCheckBoxes = new ArrayList<Button>();
				seedValueSpinners = new ArrayList<Spinner>();

				lfsrLength = LfsrWizard.MAX_LFSR_LENGTH;

				tapSettings = new boolean[LfsrWizard.MAX_LFSR_LENGTH];
// 				seed = new boolean[LfsrWizard.MAX_LFSR_LENGTH];
				setSeedArray(new boolean[LfsrWizard.MAX_LFSR_LENGTH]);

				displayOption = DisplayOption.OUTPUT_ONLY;
				keystreamLengthValue = ""; //$NON-NLS-1$

				// recreate the wizard page via the createControl method.
				createControl(pageParent);
				// To show the new page, lay it out.
				pageParent.layout();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	/**
	 * Label that says changing the length of the lfsr will reset the tap and seed.
	 * 
	 * @param parent Parent Composite
	 */
	private void createNoteLabel(Composite parent) {
		GridLayout noteLabelCompositeGridLayout = new GridLayout();

		noteLabelComposite = new Composite(parent, SWT.NONE);
		noteLabelComposite.setLayout(noteLabelCompositeGridLayout);

		noteLabel = new Label(noteLabelComposite, SWT.NONE);
		noteLabel.setText(Messages.LfsrWizardPage_4);
	}

	/**
	 * This method initializes tapSettingsGroup.
	 *
	 * @param parent
	 */
	private void createTapSettingsGroup(Composite parent) {
		GridData tapSettingCheckboxGridData = new GridData(SWT.CENTER, SWT.BEGINNING, true, false);

		GridData tapSettingPlaceHolderLabelGridData = new GridData(SWT.FILL, SWT.NONE, true, false);

		GridLayout tapSettingsGroupGridLayout = new GridLayout();
		tapSettingsGroupGridLayout.numColumns = LfsrWizard.MAX_LFSR_LENGTH;
		tapSettingsGroupGridLayout.makeColumnsEqualWidth = true;

		GridData tapSettingsGroupGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		tapSettingsGroupGridData.verticalIndent = 15;

		GridData tapSettings01StringTextGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		tapSettings01StringTextGridData.horizontalSpan = LfsrWizard.MAX_LFSR_LENGTH;

		tapSettingsGroup = new Group(parent, SWT.None);
		tapSettingsGroup.setLayoutData(tapSettingsGroupGridData);
		tapSettingsGroup.setLayout(tapSettingsGroupGridLayout);
		tapSettingsGroup.setText(Messages.LfsrWizardPage_5);

		for (int i = 0; i < tapSettingsGroupGridLayout.numColumns; i++) {
			Button tempButton = new Button(tapSettingsGroup, SWT.CHECK);
			tempButton.setLayoutData(tapSettingCheckboxGridData);

			tempButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					for (int i = 0; i < tapSettingsCheckBoxes.size(); i++) {
						if (event.widget == tapSettingsCheckBoxes.get(i)) {
							tapSettings[i] = tapSettingsCheckBoxes.get(i).getSelection();
							setSeedValueTapSettingDisplayVisibility(i, tapSettings[i]);
							updateTapSetting01StringText();
							break;
						}
					}
				}
			});

			tapSettingsCheckBoxes.add(tempButton);
		}

		for (int i = 1; i <= tapSettingsGroupGridLayout.numColumns; i++) {
			Label tempLabel = new Label(tapSettingsGroup, SWT.CENTER);
			tempLabel.setText(String.valueOf(i));
			tempLabel.setLayoutData(tapSettingPlaceHolderLabelGridData);
			tempLabel.setAlignment(SWT.CENTER);
			tapSettingsPlaceHolderLabels.add(tempLabel);
		}

		tapSettings01StringText = new Text(tapSettingsGroup, SWT.NONE);
		tapSettings01StringText.setLayoutData(tapSettings01StringTextGridData);
		tapSettings01StringText.setBackground(ColorService.LIGHTGRAY);
		tapSettings01StringText.setForeground(ColorService.GRAY);
		tapSettings01StringText.setEditable(false);
		tapSettings01StringText.setText(Messages.LfsrWizardPage_tapAs01string + "000000000000001"); //$NON-NLS-1$

	}

	/**
	 * This method initializes seedValueGroup.
	 *
	 * @param parent
	 */
	private void createSeedValueGroup(Composite parent) {
		GridData tapSettingCheckboxGridData = new GridData(SWT.CENTER, SWT.BEGINNING, true, false);

		GridData seedValueSpinnerGridData = new GridData(SWT.CENTER, SWT.BEGINNING, true, false);

		GridLayout tapSettingsGroupGridLayout = new GridLayout();
		tapSettingsGroupGridLayout.numColumns = LfsrWizard.MAX_LFSR_LENGTH;

		GridData tapSettingsGroupGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		tapSettingsGroupGridData.verticalIndent = 15;

		GridData seedValue01StringTextGridData = new GridData(SWT.FILL, SWT.NONE, true, false);
		seedValue01StringTextGridData.horizontalSpan = LfsrWizard.MAX_LFSR_LENGTH;

		seedValueGroup = new Group(parent, SWT.None);
		seedValueGroup.setLayoutData(tapSettingsGroupGridData);
		seedValueGroup.setLayout(tapSettingsGroupGridLayout);
		seedValueGroup.setText(Messages.LfsrWizardPage_6);

		for (int i = 0; i < tapSettingsGroupGridLayout.numColumns; i++) {
			Button tempButton = new Button(seedValueGroup, SWT.CHECK);
			tempButton.setVisible(false);
			tempButton.setEnabled(false);
			tempButton.setSelection(true);
			tempButton.setLayoutData(tapSettingCheckboxGridData);
			seedValueTapSettingsDisplayCheckBoxes.add(tempButton);
		}

		// Set the first seed value by default to 1. With a seed full of zeros the
		// output file
		// would only contain of zeros.

		for (int i = 0; i < tapSettingsGroupGridLayout.numColumns; i++) {
			Spinner tempSpinner = new Spinner(seedValueGroup, SWT.BORDER);
			tempSpinner.setMinimum(0);
			tempSpinner.setMaximum(1);
			tempSpinner.setIncrement(1);
			// Set the first seed by default to 1.
			if (i == 0) {
				tempSpinner.setSelection(1);
// 				seed[0] = true;
				setSeedPosition(0, true);
			} else {
				tempSpinner.setSelection(0);
			}
			tempSpinner.setLayoutData(seedValueSpinnerGridData);
			tempSpinner.addListener(SWT.Modify, this);
			seedValueSpinners.add(tempSpinner);
		}

		seedValue01StringText = new Text(seedValueGroup, SWT.NONE);
		seedValue01StringText.setLayoutData(seedValue01StringTextGridData);
		seedValue01StringText.setBackground(ColorService.LIGHTGRAY);
		seedValue01StringText.setForeground(ColorService.GRAY);
		seedValue01StringText.setEditable(false);
		seedValue01StringText.setText(Messages.LfsrWizardPage_seedValueAs01String + "100000000000000"); //$NON-NLS-1$

	}

	private void createDisplayOptionsGroup(Composite parent) {
		GridData displayOutputOnlyButtonGridData = new GridData(SWT.FILL, SWT.CENTER, false, true);
		displayOutputOnlyButtonGridData.horizontalSpan = 2;

		GridData displayOutputAndKeystreamButtonGridData = new GridData(SWT.FILL, SWT.CENTER, false, true);
		displayOutputAndKeystreamButtonGridData.horizontalSpan = 2;

		GridData displayKeystreamOnlyButtonGridData = new GridData(SWT.FILL, SWT.CENTER, false, true);

		GridLayout displayOptionsGroupGridLayout = new GridLayout();
		displayOptionsGroupGridLayout.numColumns = 2;

		GridData displayOptionsGroupGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		displayOptionsGroupGridData.verticalIndent = 15;

		displayOptionsGroup = new Group(parent, SWT.NONE);
		displayOptionsGroup.setLayoutData(displayOptionsGroupGridData);
		displayOptionsGroup.setLayout(displayOptionsGroupGridLayout);
		displayOptionsGroup.setText(Messages.LfsrWizardPage_7);

		displayOutputOnlyButton = new Button(displayOptionsGroup, SWT.RADIO);
		displayOutputOnlyButton.setText(Messages.LfsrWizardPage_8);
		displayOutputOnlyButton.setLayoutData(displayOutputOnlyButtonGridData);
		displayOutputOnlyButton.addListener(SWT.Selection, this);

		displayOutputAndKeystreamButton = new Button(displayOptionsGroup, SWT.RADIO);
		displayOutputAndKeystreamButton.setText(Messages.LfsrWizardPage_9);
		displayOutputAndKeystreamButton.setLayoutData(displayOutputAndKeystreamButtonGridData);
		displayOutputAndKeystreamButton.addListener(SWT.Selection, this);

		displayKeystreamOnlyButton = new Button(displayOptionsGroup, SWT.RADIO);
		displayKeystreamOnlyButton.setText(Messages.LfsrWizardPage_10);
		displayKeystreamOnlyButton.setLayoutData(displayKeystreamOnlyButtonGridData);
		displayKeystreamOnlyButton.addListener(SWT.Selection, this);

		keystreamLengthText = new Text(displayOptionsGroup, SWT.BORDER | SWT.SINGLE);
		keystreamLengthText.setText(""); //$NON-NLS-1$
		keystreamLengthText.addListener(SWT.Modify, this);
		keystreamLengthText.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				if (CLEARING_FLAG) {
					return;
				}

				setErrorMessage(null);
				if (e.character != SWT.BS && e.character != SWT.DEL) {
					if (!decValues.contains(e.text)) {
						setErrorMessage(Messages.LfsrWizardPage_11);
						e.doit = false;
					} else if (keystreamLengthText.getText().length() == 0 && e.text.equals("0")) { //$NON-NLS-1$
						setErrorMessage(Messages.LfsrWizardPage_13);
						e.doit = false;
//					} else if ((Long.parseLong(keystreamLengthText.getText() + e.text)) > KEY_MAX_VALUE) {
//						setErrorMessage(NLS.bind(Messages.LfsrWizardPage_12, KEY_MAX_VALUE));
//						e.doit = false;
					}
				}
			}

		});
		keystreamLengthText.setEnabled(false);
	}

	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		if (event.widget == lfsrLengthSpinner) {
			setPreviousFinalTapSetting();
			lfsrLength = lfsrLengthSpinner.getSelection();
			setSelectableTapSettingVisibilities();
			setEditableSeedValueVisibility();
			setFinalTapSetting();
			updateTapSetting01StringText();
			updateSeedValue01StringText();
			setDefaultKeystreamLength();
		} else if (event.widget == displayOutputOnlyButton) {
			displayOption = DisplayOption.OUTPUT_ONLY;
//			clearKeystreamLength();
			keystreamLengthText.setEnabled(false);
			setErrorMessage(null);
		} else if (event.widget == displayOutputAndKeystreamButton) {
			displayOption = DisplayOption.OUTPUT_AND_KEYSTREAM;
//			clearKeystreamLength();
			keystreamLengthText.setEnabled(false);
			setErrorMessage(null);
		} else if (event.widget == displayKeystreamOnlyButton) {
			displayOption = DisplayOption.KEYSTREAM_ONLY;
			keystreamLengthText.setEnabled(true);
			setDefaultKeystreamLength();
		} else if (event.widget == keystreamLengthText) {
			keystreamLengthValue = keystreamLengthText.getText();
		} else {
			for (int i = 0; i < seedValueSpinners.size(); i++) {
				if (event.widget == seedValueSpinners.get(i)) {
					int tempBitValue = seedValueSpinners.get(i).getSelection();
					if (tempBitValue == 0) {
// 						seed[i] = false;
						setSeedPosition(i, false);
					} else {
// 						seed[i] = true;
						setSeedPosition(i, true);
					}
					break;
				}
			}
			updateSeedValue01StringText();
		}
		setPageComplete(mayFinish());
	}

	/**
	 * Calculates the length of one lfsr cycle.
	 * 
	 * @return The cycle length depending on the lfsr Length.
	 */
	private void setDefaultKeystreamLength() {
		int result = (int) Math.pow(2, lfsrLength) - 1;
		CLEARING_FLAG = true;
		keystreamLengthText.setText(Integer.toString(result)); // $NON-NLS-1$
		CLEARING_FLAG = false;
		keystreamLengthText.requestLayout();
		keystreamLengthValue = Integer.toString(result);
	}

	private void setSelectableTapSettingVisibilities() {
		setSelectableTapSettingVisibility();
		setSeedValueTapSettingDisplayVisibilityOfUnselectableTaps();
	}

	/**
	 * Updates the String of zeros and on the bottom of the tapSettingsGroup. Must
	 * be called when the LFSR length is changed or a tap setting checkbox is
	 * set/unset.
	 */
	protected void updateTapSetting01StringText() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Messages.LfsrWizardPage_tapAs01string);
		for (int i = 0; i < lfsrLength; i++) {
			if (tapSettings[i]) {
				stringBuilder.append("1"); //$NON-NLS-1$
			} else {
				stringBuilder.append("0"); //$NON-NLS-1$
			}
		}
		tapSettings01StringText.setText(stringBuilder.toString());
	}

	/**
	 * Updates the seed as 0-1-string. Method must be called when the LFSR length is
	 * changed and when a seed value spinner is changed.
	 */
	protected void updateSeedValue01StringText() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Messages.LfsrWizardPage_seedValueAs01String);
		for (int i = 0; i < lfsrLength; i++) {
// 			if (seed[i]) {
			if (getSeedArray()[i]) {
				stringBuilder.append("1"); //$NON-NLS-1$
			} else {
				stringBuilder.append("0"); //$NON-NLS-1$
			}
		}
		seedValue01StringText.setText(stringBuilder.toString());
	}

//	private void clearKeystreamLength() {
//		CLEARING_FLAG = true;
//		keystreamLengthText.setText(""); //$NON-NLS-1$
//		CLEARING_FLAG = false;
//		keystreamLengthValue = ""; //$NON-NLS-1$
//	}

	public void setPreviousFinalTapSetting() {
		tapSettingsCheckBoxes.get(lfsrLength - 1).setEnabled(true);
		tapSettingsCheckBoxes.get(lfsrLength - 1).setSelection(false);
		tapSettings[lfsrLength - 1] = false;
		setSeedValueTapSettingDisplayVisibility(lfsrLength - 1, false);
	}

	public void setSelectableTapSettingVisibility() {
		for (int i = 0; i < lfsrLength; i++) {
			tapSettingsCheckBoxes.get(i).setVisible(true);
			tapSettingsPlaceHolderLabels.get(i).setVisible(true);
		}
		for (int i = lfsrLength; i < tapSettingsCheckBoxes.size(); i++) {
			tapSettingsCheckBoxes.get(i).setVisible(false);
			tapSettingsPlaceHolderLabels.get(i).setVisible(false);
		}

		uncheckTapSettingsPastLfsrLength();

		setTapSettings();
	}

	private void uncheckTapSettingsPastLfsrLength() {
		for (int i = lfsrLength; i < LfsrWizard.MAX_LFSR_LENGTH; i++) {
			tapSettingsCheckBoxes.get(i).setSelection(false);
		}
	}

	private void setTapSettings() {
		tapSettings = new boolean[lfsrLength];
		for (int i = 0; i < lfsrLength; i++) {
			tapSettings[i] = tapSettingsCheckBoxes.get(i).getSelection();
		}
	}

	public void setFinalTapSetting() {
		if (tapSettingsCheckBoxes.get(lfsrLength - 1).getSelection() == false) {
			tapSettingsCheckBoxes.get(lfsrLength - 1).setSelection(true);
		}

		tapSettingsCheckBoxes.get(lfsrLength - 1).setEnabled(false);

		tapSettings[lfsrLength - 1] = true;
		setSeedValueTapSettingDisplayVisibility(lfsrLength - 1, true);
	}

	public void setSeedValueTapSettingDisplayVisibilityOfUnselectableTaps() {
		for (int i = lfsrLength; i < seedValueTapSettingsDisplayCheckBoxes.size(); i++) {
			seedValueTapSettingsDisplayCheckBoxes.get(i).setVisible(false);
		}
	}

	public void setSeedValueTapSettingDisplayVisibility(int index, boolean isChecked) {
		if (seedValueTapSettingsDisplayCheckBoxes.isEmpty())
			return;

		if (isChecked)
			seedValueTapSettingsDisplayCheckBoxes.get(index).setVisible(true);
		else
			seedValueTapSettingsDisplayCheckBoxes.get(index).setVisible(false);
	}

	public void setEditableSeedValueVisibility() {
		for (int i = 0; i < lfsrLength; i++)
			seedValueSpinners.get(i).setVisible(true);

		for (int i = lfsrLength; i < seedValueSpinners.size(); i++)
			seedValueSpinners.get(i).setVisible(false);

		resetSeedValueSpinnersPastLfsrLengthToZero();

		setSeedValue();
	}

	private void resetSeedValueSpinnersPastLfsrLengthToZero() {
		for (int i = lfsrLength; i < LfsrWizard.MAX_LFSR_LENGTH; i++) {
			if (seedValueSpinners.get(i).getSelection() != 0)
				seedValueSpinners.get(i).setSelection(0);
		}
	}

	private void setSeedValue() {
// 		seed = new boolean[lfsrLength];
		setSeedArray(new boolean[lfsrLength]);
		for (int i = 0; i < lfsrLength; i++) {

			int tempBitValue = seedValueSpinners.get(i).getSelection();

			if (tempBitValue == 0)
// 				seed[i] = false;
				setSeedPosition(i, false);
			else
// 				seed[i] = true;
				setSeedPosition(i, true);
		}
	}

	public int getLFSRLength() {
		return lfsrLength;
	}

	public boolean[] getSeed() {
		return seed;
	}

	public boolean[] getTapSettings() {
		return tapSettings;
	}

	public DisplayOption getDisplayOption() {
		return displayOption;
	}

	public String getKeystreamLengthValue() {
		return keystreamLengthValue;
	}

	/**
	 * Returns <code>true</code>, if the page is complete and the wizard may finish.
	 *
	 * @return <code>true</code>, if the page is complete and the wizard may finish
	 */
	private boolean mayFinish() {
		if (lfsrLengthSpinner.getSelection() <= LfsrWizard.MAX_LFSR_LENGTH && lfsrLengthSpinner.getSelection() > 0) {
			if (displayKeystreamOnlyButton.getSelection())
				if (keystreamLengthText.getText() != "") //$NON-NLS-1$
					return true;
				else
					return false;

			return true;
		}
		return false;
	}
}
