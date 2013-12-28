//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.wizards;

import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;
import org.jcryptool.visual.sigVerification.algorithm.Input;

/**
 * Contains all the GUI elements for the editor input page.
 * 
 * @author Grebe
 */
public class InputKeyEditorComposite extends Composite {
    // Limit for the length of the text that might be entered into the plaintext field
    private static final int TEXTLIMIT = 1000;
    private Text text = null;
    private InputKeyEditorWizardPage page;

    public InputKeyEditorComposite(Composite parent, int style, InputKeyEditorWizardPage p) {
        super(parent, style);
        text = new Text(this, SWT.BORDER | SWT.WRAP);
        text.setBounds(10, 10, 430, 215);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        text.setTextLimit(TEXTLIMIT);
        text.setFocus();
        
        Label lblToSaveThe = new Label(this, SWT.NONE);
        lblToSaveThe.setBounds(10, 231, 400, 15);
        lblToSaveThe.setText(Messages.InputKeyEditorWizard_Label);

        page = p;

        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (text.getText().length() > 0) {
                    page.setPageComplete(true);
                    page.canFlipToNextPage();
                    byte[] pubKey = text.getText().getBytes();
                    try{
                    	Input.pubKey = KeyFactory.getInstance(Input.signaturemethod).generatePublic(new X509EncodedKeySpec(pubKey));
                    }catch(Exception ex){
                    	LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                    }
                    page.getWizard().getContainer().updateButtons();
                } else {
                    page.setPageComplete(false);
                    page.getWizard().getContainer().updateButtons();
                    page.setErrorMessage(Messages.EnterText);
                }
            }
        });
    }

    public String getText() {
        return text.getText();
    }
}
