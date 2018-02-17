//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.sig.algorithm.Input;

/**
 * Contains all the GUI elements for the editor input page.
 * 
 * @author Grebe
 */
public class InputEditorComposite extends Composite {
    // Limit for the length of the text that might be entered into the plaintext field
    private static final int TEXTLIMIT = 1000;
    private Text text = null;
    private InputEditorWizardPage page;

    public InputEditorComposite(Composite parent, int style, InputEditorWizardPage p) {
        super(parent, style);
        setLayout(new GridLayout());
        
        text = new Text(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        text.setTextLimit(TEXTLIMIT);
        text.setFocus();
        
        //Restore the former text, if existing 
        if (Input.dataPlain != null && Input.filename == null) {
        	text.setText(Input.dataPlain);
        }
        
        Label lblToSaveThe = new Label(this, SWT.NONE);
        GridData gd_lblToSaveThe = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_lblToSaveThe.widthHint = 400;
        lblToSaveThe.setText(Messages.InputEditorWizard_Label);

        page = p;

        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (text.getText().length() > 0) {
                    page.setPageComplete(true);
                    page.canFlipToNextPage();
                    Input.dataPlain = text.getText();
                    Input.data = text.getText().getBytes();
                    Input.filename = null;
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
    
    public void setInitialFocus() {
    	text.setFocus();
    }
}
