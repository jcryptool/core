/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class PasswordDialogGui extends Dialog {
    private FrequencyGui parent;
    private String passphrase;
    private Text tpass;

    public PasswordDialogGui(final FrequencyGui container, final String pass) {
        super(container.getShell());
        this.parent = container;
        this.passphrase = pass;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.PassDialogGui_dialog_title);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        GridLayout parentLayout = new GridLayout();
        parentLayout.makeColumnsEqualWidth = true;
        parent.setLayout(parentLayout);
        Composite ccontain = new Composite(parent, SWT.NONE);
        GridLayout ccontainLayout = new GridLayout();
        ccontainLayout.makeColumnsEqualWidth = true;
        GridData ccontainLData = new GridData();
        ccontainLData.widthHint = 460;
        ccontainLData.heightHint = 50;
        ccontain.setLayoutData(ccontainLData);
        ccontain.setLayout(ccontainLayout);
        {
            Composite ccontent = new Composite(ccontain, SWT.NONE);
            FormLayout ccontentLayout = new FormLayout();
            GridData ccontentLData = new GridData();
            ccontentLData.widthHint = 450;
            ccontentLData.heightHint = 50;
            ccontent.setLayoutData(ccontentLData);
            ccontent.setLayout(ccontentLayout);
            {
                FormData tpassLData = new FormData();
                tpassLData.left = new FormAttachment(0, 1000, 210);
                tpassLData.top = new FormAttachment(0, 1000, 10);
                tpassLData.width = 218;
                tpassLData.height = 19;
                tpass = new Text(ccontent, SWT.BORDER);
                tpass.setLayoutData(tpassLData);
                tpass.setText(passphrase);
                tpass.setFocus();
                tpass.setTextLimit(100);
                tpass.selectAll();
            }
            {
                Label lpass = new Label(ccontent, SWT.NONE);
                FormData lpassLData = new FormData();
                lpassLData.left = new FormAttachment(0, 1000, 10);
                lpassLData.top = new FormAttachment(0, 1000, 14);
                lpassLData.width = 180;
                lpassLData.height = 20;
                lpass.setLayoutData(lpassLData);
                lpass.setText(Messages.PassDialogGui_label_password);
                lpass.setAlignment(SWT.RIGHT);
            }
        }

        return ccontain;
    }

    @Override
    protected void okPressed() {
        String in = tpass.getText();
        parent.showCompletePass(in);
        super.okPressed();
    }

    @Override
    protected void cancelPressed() {
        super.cancelPressed();
    }
}
