//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.modern.stream.lfsr.ui;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.modern.stream.lfsr.LfsrPlugin;

public class LfsrWizardPage extends WizardPage implements Listener {

    public enum DisplayOption {
        OUTPUT_ONLY, OUTPUT_AND_KEYSTREAM, KEYSTREAM_ONLY
    }

    /** Used to override a listener, since the change is performed on purpose! */
    private boolean CLEARING_FLAG = false;

    private Group lfsrLengthGroup;
    private Label lfsrLengthLabel;
    private Spinner lfsrLengthSpinner;
    private Label noteLabel;

    private Group tapSettingsGroup;
    private ArrayList<Button> tapSettingsCheckBoxes = new ArrayList<Button>();
    private ArrayList<Label> tapSettingsPlaceHolderLabels = new ArrayList<Label>();

    private Group seedValueGroup;
    private ArrayList<Button> seedValueTapSettingsDisplayCheckBoxes = new ArrayList<Button>();
    private ArrayList<Spinner> seedValueSpinners = new ArrayList<Spinner>();

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
    private static final int KEY_MAX_VALUE = 1024;

    /**
     * Creates a new instance of LfsrWizardPage.
     */
    public LfsrWizardPage() {
        super(".", "LFSR", null); //$NON-NLS-1$ //$NON-NLS-2$
        setTitle(Messages.LfsrWizardPage_0);
        setMessage(Messages.LfsrWizardPage_1);
        setupDecimalValues();
    }

    /**
     * Sets up an ArrayList with decimal values for convenience.
     */
    private void setupDecimalValues() {
        decValues.add(0, "0");decValues.add(1, "1");decValues.add(2, "2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        decValues.add(3, "3");decValues.add(4, "4");decValues.add(5, "5"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        decValues.add(6, "6");decValues.add(7, "7");decValues.add(8, "8"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        decValues.add(9, "9"); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite pageComposite = new Composite(parent, SWT.NULL);

        createLfsrLengthGroup(pageComposite);
        createNoteLabel(pageComposite);
        createTapSettingsGroup(pageComposite);
        createSeedValueGroup(pageComposite);
        createDisplayOptionsGroup(pageComposite);

        lfsrLength = lfsrLengthSpinner.getSelection();
        setFinalTapSetting();
        setSeedValueTapSettingDisplayVisibility(lfsrLength - 1, true);
        displayOutputOnlyButton.setSelection(true);

        pageComposite.setLayout(new GridLayout());

        setControl(pageComposite);
        setPageComplete(mayFinish());

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), LfsrPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }

    /**
     * This method initializes lfsrLengthGroup.
     *
     * @param parent
     */
    private void createLfsrLengthGroup(Composite parent) {
        GridData lfsrLengthLabelGridData = new GridData();
        lfsrLengthLabelGridData.horizontalAlignment = GridData.FILL;
        lfsrLengthLabelGridData.grabExcessHorizontalSpace = true;
        lfsrLengthLabelGridData.grabExcessVerticalSpace = false;
        lfsrLengthLabelGridData.verticalAlignment = GridData.CENTER;

        GridData lfsrLengthSpinnerGridData = new GridData();
        lfsrLengthSpinnerGridData.horizontalAlignment = GridData.FILL;
        lfsrLengthSpinnerGridData.grabExcessHorizontalSpace = true;
        lfsrLengthSpinnerGridData.grabExcessVerticalSpace = false;
        lfsrLengthSpinnerGridData.verticalAlignment = GridData.CENTER;

        GridLayout lfsrLengthGroupGridLayout = new GridLayout();
        lfsrLengthGroupGridLayout.numColumns = 2;

        GridData lfsrLengthGroupGridData = new GridData();
        lfsrLengthGroupGridData.horizontalAlignment = GridData.BEGINNING;
        lfsrLengthGroupGridData.grabExcessHorizontalSpace = false;
        lfsrLengthGroupGridData.grabExcessVerticalSpace = false;
        lfsrLengthGroupGridData.verticalAlignment = GridData.FILL;

        lfsrLengthGroup = new Group(parent, SWT.None);
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
    }

    private void createNoteLabel(Composite parent) {
        GridData noteLabelGridData = new GridData();
        noteLabelGridData.horizontalAlignment = GridData.BEGINNING;
        noteLabelGridData.grabExcessHorizontalSpace = true;
        noteLabelGridData.grabExcessVerticalSpace = false;
        noteLabelGridData.verticalAlignment = GridData.CENTER;

        noteLabel = new Label(parent, SWT.None);
        noteLabel.setText(Messages.LfsrWizardPage_4);
        noteLabel.setLayoutData(noteLabelGridData);
    }

    /**
     * This method initializes tapSettingsGroup.
     *
     * @param parent
     */
    private void createTapSettingsGroup(Composite parent) {
        GridData tapSettingCheckboxGridData = new GridData();
        tapSettingCheckboxGridData.horizontalAlignment = GridData.CENTER;
        tapSettingCheckboxGridData.grabExcessHorizontalSpace = true;
        tapSettingCheckboxGridData.grabExcessVerticalSpace = false;
        tapSettingCheckboxGridData.verticalAlignment = GridData.BEGINNING;

        GridData tapSettingPlaceHolderLabelGridData = new GridData();
        tapSettingPlaceHolderLabelGridData.horizontalAlignment = GridData.CENTER;
        tapSettingPlaceHolderLabelGridData.grabExcessHorizontalSpace = true;
        tapSettingPlaceHolderLabelGridData.grabExcessVerticalSpace = false;
        tapSettingPlaceHolderLabelGridData.verticalAlignment = GridData.BEGINNING;

        GridLayout tapSettingsGroupGridLayout = new GridLayout();
        tapSettingsGroupGridLayout.numColumns = LfsrWizard.MAX_LFSR_LENGTH;

        GridData tapSettingsGroupGridData = new GridData();
        tapSettingsGroupGridData.horizontalAlignment = GridData.FILL;
        tapSettingsGroupGridData.grabExcessHorizontalSpace = true;
        tapSettingsGroupGridData.grabExcessVerticalSpace = false;
        tapSettingsGroupGridData.verticalAlignment = GridData.FILL;

        tapSettingsGroup = new Group(parent, SWT.None);
        tapSettingsGroup.setLayoutData(tapSettingsGroupGridData);
        tapSettingsGroup.setLayout(tapSettingsGroupGridLayout);
        tapSettingsGroup.setText(Messages.LfsrWizardPage_5);

        for (int i = 0; i < tapSettingsGroupGridLayout.numColumns; i++) {
            Button tempButton = new Button(tapSettingsGroup, SWT.CHECK);
            tempButton.setLayoutData(tapSettingCheckboxGridData);

            tempButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
                    for (int i = 0; i < tapSettingsCheckBoxes.size(); i++) {
                        if (event.widget == tapSettingsCheckBoxes.get(i)) {
                            tapSettings[i] = tapSettingsCheckBoxes.get(i).getSelection();
                            setSeedValueTapSettingDisplayVisibility(i, tapSettings[i]);
                            break;
                        }
                    }
                }
            });

            tapSettingsCheckBoxes.add(tempButton);
        }

        for (int i = 1; i <= tapSettingsGroupGridLayout.numColumns; i++) {
            Label tempLabel = new Label(tapSettingsGroup, SWT.None);
            tempLabel.setText(String.valueOf(i));
            tempLabel.setLayoutData(tapSettingPlaceHolderLabelGridData);
            tapSettingsPlaceHolderLabels.add(tempLabel);
        }
    }

    /**
     * This method initializes seedValueGroup.
     *
     * @param parent
     */
    private void createSeedValueGroup(Composite parent) {
        GridData tapSettingCheckboxGridData = new GridData();
        tapSettingCheckboxGridData.horizontalAlignment = GridData.CENTER;
        tapSettingCheckboxGridData.grabExcessHorizontalSpace = true;
        tapSettingCheckboxGridData.grabExcessVerticalSpace = false;
        tapSettingCheckboxGridData.verticalAlignment = GridData.BEGINNING;

        GridData seedValueSpinnerGridData = new GridData();
        seedValueSpinnerGridData.horizontalAlignment = GridData.CENTER;
        seedValueSpinnerGridData.grabExcessHorizontalSpace = true;
        seedValueSpinnerGridData.grabExcessVerticalSpace = false;
        tapSettingCheckboxGridData.verticalAlignment = GridData.BEGINNING;

        GridLayout tapSettingsGroupGridLayout = new GridLayout();
        tapSettingsGroupGridLayout.numColumns = LfsrWizard.MAX_LFSR_LENGTH;

        GridData tapSettingsGroupGridData = new GridData();
        tapSettingsGroupGridData.horizontalAlignment = GridData.FILL;
        tapSettingsGroupGridData.grabExcessHorizontalSpace = true;
        tapSettingsGroupGridData.grabExcessVerticalSpace = false;
        tapSettingsGroupGridData.verticalAlignment = GridData.FILL;

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

        for (int i = 0; i < tapSettingsGroupGridLayout.numColumns; i++) {
            Spinner tempSpinner = new Spinner(seedValueGroup, SWT.BORDER);
            tempSpinner.setMinimum(0);
            tempSpinner.setMaximum(1);
            tempSpinner.setIncrement(1);
            tempSpinner.setSelection(0);
            tempSpinner.setLayoutData(seedValueSpinnerGridData);
            tempSpinner.addListener(SWT.Modify, this);
            seedValueSpinners.add(tempSpinner);
        }
    }

    private void createDisplayOptionsGroup(Composite parent) {
        GridData displayOutputOnlyButtonGridData = new GridData();
        displayOutputOnlyButtonGridData.horizontalAlignment = GridData.FILL;
        displayOutputOnlyButtonGridData.grabExcessHorizontalSpace = false;
        displayOutputOnlyButtonGridData.grabExcessVerticalSpace = true;
        displayOutputOnlyButtonGridData.verticalAlignment = GridData.CENTER;

        GridData displayOutputAndKeystreamButtonGridData = new GridData();
        displayOutputAndKeystreamButtonGridData.horizontalAlignment = GridData.FILL;
        displayOutputAndKeystreamButtonGridData.grabExcessHorizontalSpace = false;
        displayOutputAndKeystreamButtonGridData.grabExcessVerticalSpace = true;
        displayOutputAndKeystreamButtonGridData.verticalAlignment = GridData.CENTER;

        GridData displayKeystreamOnlyButtonGridData = new GridData();
        displayKeystreamOnlyButtonGridData.horizontalAlignment = GridData.FILL;
        displayKeystreamOnlyButtonGridData.grabExcessHorizontalSpace = false;
        displayKeystreamOnlyButtonGridData.grabExcessVerticalSpace = true;
        displayKeystreamOnlyButtonGridData.verticalAlignment = GridData.CENTER;

        GridData keystreamLengthTextGridData = new GridData();
        keystreamLengthTextGridData.verticalAlignment = GridData.CENTER;
        keystreamLengthTextGridData.grabExcessHorizontalSpace = true;
        keystreamLengthTextGridData.horizontalAlignment = GridData.FILL;

        GridLayout displayOptionsGroupGridLayout = new GridLayout();
        displayOptionsGroupGridLayout.numColumns = 1;

        GridData displayOptionsGroupGridData = new GridData();
        displayOptionsGroupGridData.horizontalAlignment = GridData.FILL;
        displayOptionsGroupGridData.grabExcessHorizontalSpace = true;
        displayOptionsGroupGridData.grabExcessVerticalSpace = true;
        displayOptionsGroupGridData.verticalAlignment = GridData.FILL;

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

        keystreamLengthTextGridData = new GridData();
        keystreamLengthTextGridData.verticalAlignment = GridData.CENTER;
        keystreamLengthTextGridData.widthHint = 50;
        keystreamLengthTextGridData.horizontalIndent = 20;

        keystreamLengthText = new Text(displayOptionsGroup, SWT.BORDER | SWT.SINGLE);
        keystreamLengthText.setLayoutData(keystreamLengthTextGridData);
        keystreamLengthText.setText(""); //$NON-NLS-1$
        keystreamLengthText.addListener(SWT.Modify, this);
        keystreamLengthText.addVerifyListener(new VerifyListener() {
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
                    } else if ((Long.parseLong(keystreamLengthText.getText() + e.text)) > KEY_MAX_VALUE) {
                        setErrorMessage(NLS.bind(Messages.LfsrWizardPage_12, KEY_MAX_VALUE));
                        e.doit = false;
                    }
                }
            }

        });
        keystreamLengthText.setEnabled(false);
    }

    /**
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event) {
        if (event.widget == lfsrLengthSpinner) {
            setPreviousFinalTapSetting();
            lfsrLength = lfsrLengthSpinner.getSelection();
            setSelectableTapSettingVisibilities();
            setEditableSeedValueVisibility();
            setFinalTapSetting();
        } else if (event.widget == displayOutputOnlyButton) {
            displayOption = DisplayOption.OUTPUT_ONLY;
            clearKeystreamLength();
            keystreamLengthText.setEnabled(false);
            setErrorMessage(null);
        } else if (event.widget == displayOutputAndKeystreamButton) {
            displayOption = DisplayOption.OUTPUT_AND_KEYSTREAM;
            clearKeystreamLength();
            keystreamLengthText.setEnabled(false);
            setErrorMessage(null);
        } else if (event.widget == displayKeystreamOnlyButton) {
            displayOption = DisplayOption.KEYSTREAM_ONLY;
            keystreamLengthText.setEnabled(true);
        } else if (event.widget == keystreamLengthText) {
            keystreamLengthValue = keystreamLengthText.getText();
        } else {
            for (int i = 0; i < seedValueSpinners.size(); i++) {
                if (event.widget == seedValueSpinners.get(i)) {
                    int tempBitValue = seedValueSpinners.get(i).getSelection();
                    if (tempBitValue == 0)
                        seed[i] = false;
                    else
                        seed[i] = true;

                    break;
                }
            }
        }
        setPageComplete(mayFinish());
    }

    private void setSelectableTapSettingVisibilities() {
        setSelectableTapSettingVisibility();
        setSeedValueTapSettingDisplayVisibilityOfUnselectableTaps();
    }

    private void clearKeystreamLength() {
        CLEARING_FLAG = true;
        keystreamLengthText.setText(""); //$NON-NLS-1$
        CLEARING_FLAG = false;
        keystreamLengthValue = ""; //$NON-NLS-1$
    }

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
        if (tapSettingsCheckBoxes.get(lfsrLength - 1).getSelection() == false)
            tapSettingsCheckBoxes.get(lfsrLength - 1).setSelection(true);

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
        seed = new boolean[lfsrLength];
        for (int i = 0; i < lfsrLength; i++) {

            int tempBitValue = seedValueSpinners.get(i).getSelection();

            if (tempBitValue == 0)
                seed[i] = false;
            else
                seed[i] = true;
        }
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
