// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.transposition.ui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;
import org.jcryptool.crypto.classic.model.ui.wizard.util.WidgetBubbleUIInputHandler;
import org.jcryptool.crypto.classic.transposition.TranspositionPlugin;

/**
 * The wizard page for the Autokey-Vigenere wizard.
 *
 * @author SLeischnig
 *
 */
public class TranspositionWizardPage extends AbstractClassicCryptoPage {

    private char[] keyGenerationAlphabet;

    private TranspositionKeyInputComposite key1InputComposite;

    private TranspositionKeyInputComposite key2InputComposite;

    public TranspositionWizardPage() {
        super(Messages.TranspositionWizardPage_transposition, Messages.TranspositionWizardPage_enterkey1); //$NON-NLS-1$
        int alphaBegin = 32, alphaEnd = 254;
        keyGenerationAlphabet = new char[alphaEnd - alphaBegin + 1];
        for (int i = alphaBegin; i <= alphaEnd; i++) {
            keyGenerationAlphabet[i - alphaBegin] = (char) i;
        }
    }

    @Override
    protected void createAlphabetInputObjects() {
        super.createAlphabetInputObjects();
        key1InputComposite.setAlphabetInput(alphabetInput);
        key2InputComposite.setAlphabetInput(alphabetInput);
    }

    @Override
    protected void createKeyInputObjects() {
        // input objects were already created in the composites.
        // update them with important information now..
        key1InputComposite.setKeyInputName(Messages.TranspositionWizardPage_inputname_firstkey);
        key2InputComposite.setKeyInputName(Messages.TranspositionWizardPage_inputname_secondkey);
    }

    @Override
    protected void createInputVerificationHandler(Shell shell) {
        verificationDisplayHandler = new WidgetBubbleUIInputHandler(shell) {
            @SuppressWarnings("rawtypes")
			@Override
            public Control mapInputToWidget(AbstractUIInput input) {
                if (input.equals(operationInput)) {
                    return (Control) operationLastSelected;
                }
                return super.mapInputToWidget(input);
            }
        };
        verificationDisplayHandler.addAsObserverForInput(operationInput);
        verificationDisplayHandler.addAsObserverForInput(filterInput);
        verificationDisplayHandler.addAsObserverForInput(alphabetInput);
        verificationDisplayHandler.addAsObserverForInput(transformationInput);

        // static mappings (dynamic, like at operation, are handled above in the overridden method)
        verificationDisplayHandler.addInputWidgetMapping(alphabetInput, alphabetCombo);
        verificationDisplayHandler.addInputWidgetMapping(filterInput, filterCheckBox);
        verificationDisplayHandler.addInputWidgetMapping(transformationInput, transformCheckBox);
    }
    
    

    @Override
	protected List<KeyVerificator> getKeyVerificators() {
		List<KeyVerificator> verificators = specification.getKeyVerificators();
		
		return verificators;
	}

	@Override
    protected void addPageObserver() {
        operationInput.addObserver(pageObserver);
        alphabetInput.addObserver(pageObserver);
        filterInput.addObserver(pageObserver);
        transformationInput.addObserver(pageObserver);
        key1InputComposite.setObserverToAllInputs(pageObserver);
        key2InputComposite.setObserverToAllInputs(pageObserver);
    }

    @Override
    protected void setHelpAvailable() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), TranspositionPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }


    /**
     * Returns the entered key.
     *
     * @return The entered key
     */
    public String getKey() {
        return key1InputComposite.getKeyInput().getContent().toUnformattedChars(this.getSelectedAlphabet());
    }

    /**
     * Returns the entered 2nd key.
     *
     * @return The entered 2nd key
     */
    public String getKey2() {
        if (key2InputComposite.getIsActiveInput().getContent())
            return key2InputComposite.getKeyInput().getContent().toUnformattedChars(this.getSelectedAlphabet());
        return ""; //$NON-NLS-1$
    }

    public boolean getTransp1InOrder() {
        return key1InputComposite.getReadInInput().getContent();
    }

    public boolean getTransp1OutOrder() {
        return key1InputComposite.getReadOutInput().getContent();
    }

    public boolean getTransp2InOrder() {
        return key2InputComposite.getReadInInput().getContent();
    }

    public boolean getTransp2OutOrder() {
        return key2InputComposite.getReadOutInput().getContent();
    }

    @Override
    protected boolean mayFinish() {
        if ((encryptButton.getSelection()) || (decryptButton.getSelection())) {
            if (key1InputComposite.getKeyInput().getContent().toArray().length > 0
                    && (!key2InputComposite.getIsActiveInput().getContent() || key2InputComposite.getKeyInput()
                            .getContent().toArray().length > 0)) {

                return true;
            }
        }
        return false;
    }

    /**
     * This method initializes keyGroup
     *
     */
    protected void createKeyGroup(Composite parent) {

        keyGroup = new Group(parent, SWT.NONE);
        GridLayout keyGroupGridLayout = new GridLayout();
        keyGroupGridLayout.makeColumnsEqualWidth = true;
        keyGroupGridLayout.numColumns = 2;
        GridData keyGroupGridData = new GridData();
        keyGroupGridData.horizontalAlignment = GridData.FILL;
        keyGroupGridData.grabExcessHorizontalSpace = true;
        keyGroup.setLayoutData(keyGroupGridData);
        keyGroup.setLayout(keyGroupGridLayout);
        keyGroup.setText(Messages.TranspositionWizardPage_grouptext_keys);

        key1InputComposite = new TranspositionKeyInputComposite(keyGroup, false);
        GridData key1InputCompositeLData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        key1InputComposite.setLayoutData(key1InputCompositeLData);
        key1InputComposite.setVerificators(getKeyVerificators());

        key1InputComposite.setTitle(Messages.TranspositionWizardPage_firstkey_grouptitle);
        key1InputComposite.setActive(true);

        key2InputComposite = new TranspositionKeyInputComposite(keyGroup, true);
        GridData key2InputCompositeLData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        key2InputComposite.setLayoutData(key2InputCompositeLData);
        key2InputComposite.setVerificators(getKeyVerificators());

        key2InputComposite.setTitle(Messages.TranspositionWizardPage_secondkey_grouptitle);
        key2InputComposite.setActive(false);

    }

    @Override
    public void dispose() {
        super.dispose();
        if (key1InputComposite != null && key1InputComposite.getVerificationDisplayHandler() != null) {
            key1InputComposite.getVerificationDisplayHandler().dispose();
        }
        if (key2InputComposite != null && key2InputComposite.getVerificationDisplayHandler() != null) {
            key2InputComposite.getVerificationDisplayHandler().dispose();
        }
    }

}
