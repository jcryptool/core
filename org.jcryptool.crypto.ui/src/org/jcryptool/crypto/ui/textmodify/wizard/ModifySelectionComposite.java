// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.ui.textmodify.wizard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.crypto.ui.CryptoUIPlugin;
import org.jcryptool.crypto.ui.alphabets.AlphabetSelectorComposite;
import org.jcryptool.crypto.ui.alphabets.AlphabetSelectorComposite.Mode;

/**
 * @author Simon
 *
 */

public class ModifySelectionComposite extends Composite implements Listener {

//    private Composite alphabetGroup;
    private Button alphabetYESNO;
//    private Composite uppercaseGroup;
    private Button uppercaseYESNO;
    private Button uppercase;
    private Button lowercase;
//    private Composite umlautGroup;
    private Button umlautYESNO;
//    private Composite leerGroup;
    private Button leerYESNO;
//    private Composite tryComposite;
    private Button tryButton;
    private Composite content;

    /** The operation */
    private boolean doUppercase = true;

    /** The filter state */
    private boolean uppercaseTransformationOn;
    private boolean alphabetTransformationON;
    private boolean umlautTransformationON;
    private boolean leerTransformationON;

//    private static Mode defaultMode = Mode.SINGLE_COMBO_BOX_WITH_CUSTOM_ALPHABETS;
    private static Mode defaultMode = Mode.COMBO_BOX_WITH_CUSTOM_ALPHABET_BUTTON;

    private String tryString;
    private PreviewViewer myExampleViewer;
	private Mode customAlphabetMode = defaultMode;
	private AlphabetSelectorComposite alphabetComboNew;


    /**
     * @param parent the parent composite
     * @param style SWT style for the composite
     */
    public ModifySelectionComposite(Composite parent, int style) {
        this(parent, style, new TransformData(), defaultMode);
    }

    /**
     * @param parent the parent composite
     * @param style SWT style for the composite
     * @param defaultData defines how the page's elements will be selected first
     */
    public ModifySelectionComposite(Composite parent, int style, TransformData defaultData, AlphabetSelectorComposite.Mode customAlphaMode) {
        super(parent, style);
        this.setLayout(new GridLayout());

        this.customAlphabetMode = customAlphaMode;

        content = new Composite(this, SWT.NONE);
        GridLayout contentLayout = new GridLayout();
        contentLayout.verticalSpacing = 15;
        content.setLayout(contentLayout);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        try {
            createUppercaseGroup(content);
            createUmlautGroup(content);
            createLeerGroup(content);
            createAlphabetGroup(content);
            createTryComposite(content);
        } catch (Exception e) {
            LogUtil.logError(CryptoUIPlugin.PLUGIN_ID, e);
        }

        setTransformData(defaultData);
    }

    public ModifySelectionComposite(Group transformationGroup, int style,
			TransformData firstFormSetting) {
		this(transformationGroup, style, firstFormSetting, defaultMode);
	}

	public TransformData getTransformData() {
    	//TODO: !provisory getNameForAlphabet
        return new TransformData(getSelectedFilterAlphabet(),
        		doUppercase,
        		uppercaseTransformationOn,
        		leerTransformationON,
                alphabetTransformationON,
                umlautTransformationON);
    }

    //TODO: !relocate
    /**
     * retrieves a name for a given alphabet object that is supposed to be in the JCT alphabets store.
     *
     * @param a the alphabet object
     * @return the name, or null, if not found
     */
    public static String getNameForAlphabet(AbstractAlphabet a) {
    	for(AbstractAlphabet alpha: AlphabetsManager.getInstance().getAlphabets()) {
    		if(a!=null && (a==alpha || a.equals(alpha))) {
    			return alpha.getName();
    		}
    	}
    	return null;
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
        initAlphabetComposites(data.getSelectedAlphabet());

        uppercase.setEnabled(uppercaseTransformationOn);
        lowercase.setEnabled(uppercaseTransformationOn);
        alphabetComboNew.setEnabled(alphabetYESNO.getSelection());
    }

	/**
     * Initializes the alphabet composites. An empty string leads to the selection of the first alphabet
     */
    private void initAlphabetComposites(AbstractAlphabet selectAlphabet) {
    	
        alphabetComboNew.getAlphabetInput().writeContent(selectAlphabet);
        alphabetComboNew.getAlphabetInput().synchronizeWithUserSide();
    }

    public AbstractAlphabet getSelectedFilterAlphabet() {
    	return alphabetComboNew.getAlphabetInput().getContent();
    }

    /**
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    @Override
	public final void handleEvent(final Event event) {
        if (event.widget == uppercase) {
            doUppercase = uppercase.getSelection();
        } else if (event.widget == lowercase) {
            doUppercase = uppercase.getSelection();
        } else if (event.widget == uppercaseYESNO) {
            uppercaseTransformationOn = uppercaseYESNO.getSelection();
            uppercase.setEnabled(uppercaseTransformationOn);
            lowercase.setEnabled(uppercaseTransformationOn);
        } else if (event.widget == alphabetYESNO) {
            alphabetTransformationON = alphabetYESNO.getSelection();
            alphabetComboNew.setEnabled(alphabetYESNO.getSelection());
        } else if (event.widget == umlautYESNO) {
            umlautTransformationON = umlautYESNO.getSelection();
        } else if (event.widget == leerYESNO) {
            leerTransformationON = leerYESNO.getSelection();
        } else if (event.widget == tryButton) {
            String text = getTryString();
            if (text == null)
                JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, CryptoUIPlugin.PLUGIN_ID,
                        Messages.PreviewViewer_fileNotOpen));
            else {
                myExampleViewer = new PreviewViewer(this.getShell());
                myExampleViewer.setText(text, this.getTransformData());
                myExampleViewer.setTitle(Messages.ModifySelectionComposite_preview);
                myExampleViewer.open();
            }
        }
    }

    /**
     * This method initializes operationGroup
     *
     */
    private void createUppercaseGroup(final Composite parent) {

        uppercaseYESNO = new Button(parent, SWT.CHECK | SWT.BORDER);
        uppercaseYESNO.setText(Messages.ModifyWizardPage_upperLower);
        uppercaseYESNO.addListener(SWT.Selection, this);

        Composite innerGroup = new Composite(parent, SWT.NONE | SWT.BORDER);
        GridData singleTransformationInnerBoxGData = new GridData(SWT.FILL, SWT.FILL, true, false);
        singleTransformationInnerBoxGData.horizontalIndent = 20;
        innerGroup.setLayoutData(singleTransformationInnerBoxGData);
        innerGroup.setLayout(new GridLayout(2, true));

        uppercase = new Button(innerGroup, SWT.RADIO | SWT.BORDER);
        uppercase.setText(Messages.ModifyWizardPage_alltoupper);
        uppercase.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        uppercase.addListener(SWT.Selection, this);

        lowercase = new Button(innerGroup, SWT.RADIO);
        lowercase.setText(Messages.ModifyWizardPage_alltolower);
        lowercase.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        lowercase.addListener(SWT.Selection, this);
    }

    /**
     * This method initializes operationGroup
     *
     */
    private void createLeerGroup(final Composite parent) {

        leerYESNO = new Button(parent, SWT.CHECK);
        leerYESNO.setText(Messages.ModifyWizardPage_replaceblanks);
        leerYESNO.addListener(SWT.Selection, this);
    }

    /**
     * This method initializes operationGroup
     *
     */
    private void createUmlautGroup(final Composite parent) {
    	
        umlautYESNO = new Button(parent, SWT.CHECK);
        umlautYESNO.setText(Messages.ModifyWizardPage_umlauts);
        umlautYESNO.addListener(SWT.Selection, this);

    }

    /**
     * This method initializes alphabetGroup
     *
     */
    private void createAlphabetGroup(final Composite parent) {

        alphabetYESNO = new Button(parent, SWT.CHECK);
        alphabetYESNO.setText(Messages.ModifyWizardPage_filteralpha);
        alphabetYESNO.addListener(SWT.Selection, this);

        Composite innerGroup = new Composite(parent, SWT.NONE);
        innerGroup.setLayout(new GridLayout());
        GridData singleTransformationInnerBoxGData = new GridData(SWT.FILL, SWT.FILL, true, false);
        singleTransformationInnerBoxGData.horizontalIndent = 20;
        innerGroup.setLayoutData(singleTransformationInnerBoxGData);

        alphabetComboNew = new AlphabetSelectorComposite(innerGroup, null, customAlphabetMode);
        alphabetComboNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    }

    /**
     * This method initializes alphabetGroup
     *
     */
    private void createTryComposite(final Composite parent) {

        tryButton = new Button(parent, SWT.PUSH);
        tryButton.setText(Messages.ModifySelectionComposite_howwillitlooklike);
        tryButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
        tryButton.addListener(SWT.Selection, this);
    }

    /**
     * reads the current value from an input stream
     *
     * @param in the input stream
     */
    private String InputStreamToString(InputStream in) {
        if (in == null)
            return null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, IConstants.UTF8_ENCODING));
        } catch (UnsupportedEncodingException e1) {
            LogUtil.logError(CryptoUIPlugin.PLUGIN_ID, e1);
        }

        StringBuffer myStrBuf = new StringBuffer();
        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = reader.read()) != -1) {
                myStrBuf.append(String.valueOf((char) charOut));
            }
        } catch (IOException e) {
            LogUtil.logError(CryptoUIPlugin.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }

    /**
     * get the text from an opened editor
     */
    private String getEditorText() {
        InputStream stream = EditorsManager.getInstance().getActiveEditorContentInputStream();
        return InputStreamToString(stream);
    }

    /**
     * Sets the example String which will be used in the "Preview transformation"
     *
     * @param pTryString the example text
     */
    public void setTryString(String pTryString) {
        tryString = pTryString;
    }

    public String getTryString() {
        tryString = getEditorText();
        return tryString;
    }

}
