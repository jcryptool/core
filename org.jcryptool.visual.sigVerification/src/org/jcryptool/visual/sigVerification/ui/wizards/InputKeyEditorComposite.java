// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.wizards;

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
import org.jcryptool.visual.sigVerification.algorithm.SigVerification;

/**
 * Contains all the GUI elements for the key editor input page.
 * 
 * @author Wilfing
 */
public class InputKeyEditorComposite extends Composite {
    // Limit for the length of the text that might be entered into the plaintext field
    private static final int TEXTLIMIT = 1000;
    private Text text = null;
    private InputKeyEditorWizardPage page;
    Input input;
    SigVerification sigVerification;

    public InputKeyEditorComposite(Composite parent, int style, InputKeyEditorWizardPage p) {
        super(parent, style);
        this.input = p.input;
        this.sigVerification = p.sigVerification;
        text = new Text(this, SWT.BORDER | SWT.WRAP);
        text.setBounds(10, 10, 430, 215);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        text.setTextLimit(TEXTLIMIT);
        text.setFocus();

        Label lblToSaveThe = new Label(this, SWT.NONE);
        lblToSaveThe.setBounds(10, 231, 430, 59);
        lblToSaveThe.setText(Messages.InputKeyEditorWizard_Label);

        page = p;

        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (text.getText().length() > 0) {
                    page.setPageComplete(true);
                    page.inputKeyWizard.enableFinish = true;
                    page.getWizard().getContainer().updateButtons();
                    // ToDo: Remove comment after function publicKeyFile is implemented !!!
                    // byte[] pubKey = text.getText().getBytes();
                    try {
                        // Byte Array in Typ PublicKey umwandeln.
                        // ToDo: Function publicKeyFile in class sigVerification is not implemented !!!
                        // sigVerification.publicKeyFile(pubKey, input);
                    } catch (Exception ex) {
                        LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                    }
                } else {
                    page.setPageComplete(false);
                    page.setErrorMessage(Messages.EnterText);
                }
            }
        });
    }

    public String getText() {
        return text.getText();
    }
}
