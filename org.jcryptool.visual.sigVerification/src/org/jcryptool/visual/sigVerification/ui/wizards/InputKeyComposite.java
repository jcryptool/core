// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.visual.sigVerification.algorithm.Hash;
import org.jcryptool.visual.sigVerification.algorithm.Input;
import org.jcryptool.visual.sigVerification.algorithm.SigVerification;

/**
 * This class contains the GUI elements for first page of the key from file input wizard.
 * 
 * @author Wilfing
 */
public class InputKeyComposite extends Composite implements SelectionListener {
    private Button rdoFromFile;
    private Button rdoFromEditor;
    private Button rdoFromKeyStore;
    SigVerification sigVerification;
    Hash hash;
    Input input;
    InputKeyWizardPage page;
    InputKeyWizard inputKeyWizard;

    public InputKeyComposite(Composite parent, int style, InputKeyWizardPage page) {
        super(parent, style);
        this.sigVerification = page.sigVerifiaction;
        this.hash = page.hash;
        this.input = page.input;
        this.page = page;
        this.inputKeyWizard = page.inputKeyWizard;

        rdoFromFile = new Button(this, SWT.RADIO);
        // Radiobutton rdoFromFile is disabled because
        // function publicKeyFile in class sigVerification is not implemented !!!
        rdoFromFile.setEnabled(false);
        rdoFromFile.setBounds(10, 10, 430, 18);
        rdoFromFile.setText(Messages.InputKeyWizard_rdoFromFile);

        rdoFromEditor = new Button(this, SWT.RADIO);
        // Radiobutton rdoFromFile is disabled because
        // function publicKeyFile in class sigVerification is not implemented !!!
        rdoFromEditor.setEnabled(false);
        rdoFromEditor.setBounds(10, 34, 333, 18);
        rdoFromEditor.setText(Messages.InputKeyWizard_rdoFromEditor);
        parent.setSize(600, 400);

        rdoFromKeyStore = new Button(this, SWT.RADIO);
        rdoFromKeyStore.setBounds(10, 58, 430, 18);
        rdoFromKeyStore.setText(Messages.InputKeyWizard_rdoFromKeyStore);
        parent.setSize(600, 400);
        
        rdoFromKeyStore.addSelectionListener(this);
        rdoFromKeyStore.setSelection(true);
        
        // ToDo: Remove the following lines after function 
        // publicKeyFile in class sigVerification is implemented!
        // *** start removing ***
        inputKeyWizard.enableFinish = true;
        this.page.enableNext = false;
        this.page.setPageComplete(true); 
        // *** end removing ***
        
    }

    @Override
	public void widgetSelected(SelectionEvent e) {
        if (rdoFromKeyStore.getSelection()) {
            inputKeyWizard.enableFinish = true;
            page.enableNext = false;
            page.setPageComplete(true);
            page.canFlipToNextPage();
            page.getWizard().getContainer().updateButtons();
        } else if (rdoFromEditor.getSelection()) {
            page.enableNext = true;
            inputKeyWizard.enableFinish = false;
            page.canFlipToNextPage();
            page.getWizard().getContainer().updateButtons();
        } else if (rdoFromFile.getSelection()) {
            page.enableNext = true;
            inputKeyWizard.enableFinish = false;
            page.canFlipToNextPage();
            page.getWizard().getContainer().updateButtons();
        }
    }

    /**
     * @return the rdoFromFile
     */
    public Button getRdoFromFile() {
        return rdoFromFile;
    }

    /**
     * @return the rdoFromEditor
     */
    public Button getRdoFromEditor() {
        return rdoFromEditor;
    }

    /**
     * @return the rdoFromKeyStore
     */
    public Button getRdoFromKeyStore() {
        return rdoFromKeyStore;
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {       

    }
}
