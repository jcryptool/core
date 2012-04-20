// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.rsa.ui.wizards.wizardpages;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.visual.rsa.Messages;

public class ModifySelection extends Composite implements Listener {

    private Composite alphabetGroup;
    private Combo alphabetCombo;
    private Button alphabetYESNO;
    private Composite uppercaseGroup;
    private Button uppercaseYESNO;
    private Button uppercase;
    private Button lowercase;
    private Composite umlautGroup;
    private Button umlautYESNO;
    private Composite leerGroup;
    private Button leerYESNO;

    /** The selected alphabet's name */
    private String selectedAlphabetName = ""; //$NON-NLS-1$

    /** The operation */
    private boolean doUppercase = true;

    /** The filter state */
    private boolean uppercaseTransformationOn;
    private boolean alphabetTransformationON;
    private boolean umlautTransformationON;
    private boolean leerTransformationON;

    /**
     * the alphabets to be displayed in the alphabet box
     */
    private String[] alphabets;
    /**
     * the name of the default alphabet (the selected entry in the alphabet combo box) - if the alphabet is not found,
     * the first Alphabet is used
     */
    private String defaultAlphabet;

    /**
     * @param parent the parent composite
     * @param style SWT style for the composite
     * @param alphabets the alphabets to be displayed in the alphabet box
     * @param defaultAlphabet the name of the default alphabet (the selected entry in the alphabet combo box) - if the
     *        alphabet is not found, the first Alphabet is used
     */
    public ModifySelection(Composite parent, int style) {
        this(parent, style, new TransformData());
    }

    /**
     * @param parent the parent composite
     * @param style SWT style for the composite
     * @param alphabets the alphabets to be displayed in the alphabet box
     * @param defaultAlphabet the name of the default alphabet (the selected entry in the alphabet combo box)
     * @param defaultData defines how the page's elements will be selected first
     */
    public ModifySelection(Composite parent, int style, TransformData defaultData) {
        super(parent, style);

        GridLayout layout = new GridLayout();
        this.setLayout(layout);

        this.alphabets = getAlphabetList();
        this.defaultAlphabet = getStandardAlphabetName();

        createUppercaseGroup(this);
        createUmlautGroup(this);
        createLeerGroup(this);
        createAlphabetGroup(this);

        setTransformData(defaultData);
    }

    public TransformData getTransformData() {
        return new TransformData(selectedAlphabetName, doUppercase, uppercaseTransformationOn, leerTransformationON,
                alphabetTransformationON, umlautTransformationON);
    }

    public void setTransformData(TransformData data) {
    	alphabetYESNO.setSelection(data.isAlphabetTransformationON());
        alphabetTransformationON = data.isAlphabetTransformationON();
        uppercaseYESNO.setSelection(data.isUppercaseTransformationOn());
        uppercaseTransformationOn = data.isUppercaseTransformationOn();
        uppercase.setSelection(data.isDoUppercase());
        lowercase.setSelection(!data.isDoUppercase());
        doUppercase = data.isDoUppercase();
        leerYESNO.setSelection(data.isLeerTransformationON());
        leerTransformationON = data.isLeerTransformationON();
        umlautYESNO.setSelection(data.isUmlautTransformationON());
        umlautTransformationON = data.isUmlautTransformationON();
        initAlphabetComposites(data.getSelectedAlphabetName());

        uppercase.setEnabled(uppercaseTransformationOn);
        lowercase.setEnabled(uppercaseTransformationOn);
        alphabetCombo.setEnabled(alphabetYESNO.getSelection());
    }

	/**
     * Initializes the alphabet composites. An empty string leads to the selection of the first alphabet
     */
    private void initAlphabetComposites(String selectAlphabetName) {
        alphabetCombo.setItems(new String[0]);
        boolean found = false;
        alphabetCombo.setItems(new String[0]);
        for (int i = 0; i < alphabets.length; i++) {
            alphabetCombo.add(alphabets[i]);
            if (i == 0) {
                alphabetCombo.setText(alphabets[i]);
                selectedAlphabetName = alphabetCombo.getText();
            }
            if (alphabets[i].equals(defaultAlphabet) && !found) {
                alphabetCombo.setText(alphabets[i]);
                selectedAlphabetName = alphabetCombo.getText();
            }
            if (alphabets[i].equals(selectAlphabetName)) {
                alphabetCombo.setText(alphabets[i]);
                selectedAlphabetName = alphabetCombo.getText();
                alphabetCombo.setSelection(new Point(i, i));
                found = true;
            }
        }
    }

    /**
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public final void handleEvent(final Event event) {
        if (event.widget == uppercase) {
            doUppercase = uppercase.getSelection();
        } else if (event.widget == lowercase) {
            doUppercase = uppercase.getSelection();
        } else if (event.widget == uppercaseYESNO) {
            uppercaseTransformationOn = uppercaseYESNO.getSelection();
            uppercase.setEnabled(uppercaseTransformationOn);
            lowercase.setEnabled(uppercaseTransformationOn);
        } else if (event.widget == alphabetCombo) {
            selectedAlphabetName = alphabetCombo.getText();
        } else if (event.widget == alphabetYESNO) {
            alphabetTransformationON = alphabetYESNO.getSelection();
            alphabetCombo.setEnabled(alphabetYESNO.getSelection());
        } else if (event.widget == umlautYESNO) {
            umlautTransformationON = umlautYESNO.getSelection();
        } else if (event.widget == leerYESNO) {
            leerTransformationON = leerYESNO.getSelection();
        } 
    }

    /**
     * This method initializes operationGroup
     *
     */
    private void createUppercaseGroup(final Composite parent) {
        GridData uppercaseButtonGridData = new GridData();
        uppercaseButtonGridData.horizontalAlignment = SWT.FILL;
        uppercaseButtonGridData.grabExcessHorizontalSpace = true;

        GridData lowercaseButtonGridData = new GridData();
        lowercaseButtonGridData.horizontalAlignment = SWT.FILL;
        lowercaseButtonGridData.grabExcessHorizontalSpace = true;

        GridLayout singleTransformationBoxLayout = new GridLayout();
        singleTransformationBoxLayout.numColumns = 1;

        GridData singleTransformationBoxGData = new GridData();
        singleTransformationBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationBoxGData.verticalAlignment = SWT.BEGINNING;
        singleTransformationBoxGData.grabExcessVerticalSpace = false;
        singleTransformationBoxGData.verticalIndent = 5;

        GridData singleTransformationCheckboxGData = new GridData();
        singleTransformationCheckboxGData.grabExcessHorizontalSpace = true;
        singleTransformationCheckboxGData.horizontalAlignment = SWT.FILL;
        singleTransformationCheckboxGData.grabExcessVerticalSpace = false;
        singleTransformationCheckboxGData.verticalAlignment = SWT.BEGINNING;

        GridData singleTransformationInnerBoxGData = new GridData();
        singleTransformationInnerBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationInnerBoxGData.verticalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessVerticalSpace = false;
        singleTransformationInnerBoxGData.horizontalIndent = 15;

        uppercaseGroup = new Composite(parent, SWT.NONE);
        uppercaseGroup.setLayoutData(singleTransformationBoxGData);
        uppercaseGroup.setLayout(singleTransformationBoxLayout);

        uppercaseYESNO = new Button(uppercaseGroup, SWT.CHECK);
        uppercaseYESNO.setText(Messages.ModifyWizardPage_upperLower);
        uppercaseYESNO.setLayoutData(singleTransformationCheckboxGData);
        uppercaseYESNO.addListener(SWT.Selection, this);

        Composite innerGroup = new Composite(uppercaseGroup, SWT.NONE);
        innerGroup.setLayoutData(singleTransformationInnerBoxGData);
        innerGroup.setLayout(new GridLayout(2, true));

        uppercase = new Button(innerGroup, SWT.RADIO);
        uppercase.setText(Messages.ModifyWizardPage_alltoupper);
        uppercase.setLayoutData(uppercaseButtonGridData);
        uppercase.addListener(SWT.Selection, this);

        lowercase = new Button(innerGroup, SWT.RADIO);
        lowercase.setText(Messages.ModifyWizardPage_alltolower);
        lowercase.setLayoutData(lowercaseButtonGridData);
        lowercase.addListener(SWT.Selection, this);
    }

    /**
     * This method initializes LeerGroup
     *
     */
    private void createLeerGroup(final Composite parent) {
        GridLayout singleTransformationBoxLayout = new GridLayout();
        singleTransformationBoxLayout.numColumns = 1;

        GridData singleTransformationBoxGData = new GridData();
        singleTransformationBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationBoxGData.verticalAlignment = SWT.BEGINNING;
        singleTransformationBoxGData.grabExcessVerticalSpace = false;
        singleTransformationBoxGData.verticalIndent = 5;

        GridData singleTransformationCheckboxGData = new GridData();
        singleTransformationCheckboxGData.grabExcessHorizontalSpace = true;
        singleTransformationCheckboxGData.horizontalAlignment = SWT.FILL;
        singleTransformationCheckboxGData.grabExcessVerticalSpace = false;
        singleTransformationCheckboxGData.verticalAlignment = SWT.BEGINNING;

        GridData singleTransformationInnerBoxGData = new GridData();
        singleTransformationInnerBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationInnerBoxGData.verticalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessVerticalSpace = false;
        singleTransformationInnerBoxGData.horizontalIndent = 15;

        leerGroup = new Composite(parent, SWT.NONE);
        leerGroup.setLayoutData(singleTransformationBoxGData);
        leerGroup.setLayout(singleTransformationBoxLayout);

        leerYESNO = new Button(leerGroup, SWT.CHECK);
        leerYESNO.setText(Messages.ModifyWizardPage_replaceblanks);
        leerYESNO.setLayoutData(singleTransformationCheckboxGData);
        leerYESNO.addListener(SWT.Selection, this);
    }

    /**
     * This method initializes umlautGroup
     *
     */
    private void createUmlautGroup(final Composite parent) {

        GridLayout singleTransformationBoxLayout = new GridLayout();
        singleTransformationBoxLayout.numColumns = 1;

        GridData singleTransformationBoxGData = new GridData();
        singleTransformationBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationBoxGData.verticalAlignment = SWT.BEGINNING;
        singleTransformationBoxGData.grabExcessVerticalSpace = false;
        singleTransformationBoxGData.verticalIndent = 5;

        GridData singleTransformationCheckboxGData = new GridData();
        singleTransformationCheckboxGData.grabExcessHorizontalSpace = true;
        singleTransformationCheckboxGData.horizontalAlignment = SWT.FILL;
        singleTransformationCheckboxGData.grabExcessVerticalSpace = false;
        singleTransformationCheckboxGData.verticalAlignment = SWT.BEGINNING;

        GridData singleTransformationInnerBoxGData = new GridData();
        singleTransformationInnerBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationInnerBoxGData.verticalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessVerticalSpace = false;
        singleTransformationInnerBoxGData.horizontalIndent = 15;
        umlautGroup = new Composite(parent, SWT.NONE);
        umlautGroup.setLayoutData(singleTransformationBoxGData);
        umlautGroup.setLayout(singleTransformationBoxLayout);

        umlautYESNO = new Button(umlautGroup, SWT.CHECK);
        umlautYESNO.setText(Messages.ModifyWizardPage_umlauts);
        umlautYESNO.setLayoutData(singleTransformationCheckboxGData);
        umlautYESNO.addListener(SWT.Selection, this);

    }

    /**
     * This method initializes alphabetGroup
     *
     */
    private void createAlphabetGroup(final Composite parent) {
        GridData filterComboGridData = new GridData();
        filterComboGridData.horizontalAlignment = SWT.FILL;
        filterComboGridData.grabExcessHorizontalSpace = true;

        GridLayout singleTransformationBoxLayout = new GridLayout();
        singleTransformationBoxLayout.numColumns = 1;

        GridData singleTransformationBoxGData = new GridData();
        singleTransformationBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationBoxGData.verticalAlignment = SWT.BEGINNING;
        singleTransformationBoxGData.grabExcessVerticalSpace = false;
        singleTransformationBoxGData.verticalIndent = 5;

        GridData singleTransformationCheckboxGData = new GridData();
        singleTransformationCheckboxGData.grabExcessHorizontalSpace = true;
        singleTransformationCheckboxGData.horizontalAlignment = SWT.FILL;
        singleTransformationCheckboxGData.grabExcessVerticalSpace = false;
        singleTransformationCheckboxGData.verticalAlignment = SWT.BEGINNING;

        GridData singleTransformationInnerBoxGData = new GridData();
        singleTransformationInnerBoxGData.horizontalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessHorizontalSpace = true;
        singleTransformationInnerBoxGData.verticalAlignment = SWT.FILL;
        singleTransformationInnerBoxGData.grabExcessVerticalSpace = false;
        singleTransformationInnerBoxGData.horizontalIndent = 15;

        alphabetGroup = new Composite(parent, SWT.NONE);
        alphabetGroup.setLayoutData(singleTransformationBoxGData);
        alphabetGroup.setLayout(singleTransformationBoxLayout);

        alphabetYESNO = new Button(alphabetGroup, SWT.CHECK);
        alphabetYESNO.setText(Messages.ModifyWizardPage_filteralpha);
        alphabetYESNO.setLayoutData(singleTransformationCheckboxGData);
        alphabetYESNO.addListener(SWT.Selection, this);

        Composite innerGroup = new Composite(alphabetGroup, SWT.NONE);
        innerGroup.setLayoutData(singleTransformationInnerBoxGData);
        innerGroup.setLayout(new GridLayout(1, true));

        alphabetCombo = new Combo(innerGroup, SWT.NONE | SWT.READ_ONLY);
        alphabetCombo.setLayoutData(filterComboGridData);
        alphabetCombo.addListener(SWT.Selection, this);
    }

    private String getStandardAlphabetName() {
        return AlphabetsManager.getInstance().getDefaultAlphabet().getName();
    }

    private String[] getAlphabetList() {
        Vector<String> myV = new Vector<String>();
        AbstractAlphabet[] alphas = AlphabetsManager.getInstance().getAlphabets();
        for (int i = 0; i < alphas.length; i++) {
            myV.addElement(alphas[i].getName());
        }
        return myV.toArray(new String[0]);
    }

}
