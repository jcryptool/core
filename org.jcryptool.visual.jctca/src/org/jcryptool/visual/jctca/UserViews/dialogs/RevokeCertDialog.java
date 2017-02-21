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
package org.jcryptool.visual.jctca.UserViews.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.visual.jctca.Util;

/**
 * @author sho
 * 
 */
public class RevokeCertDialog extends Dialog {
    private String txt_reason;
    private Combo reason;

    /**
     * @param parentShell
     */
    public RevokeCertDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout gl = new GridLayout(1, false);
        container.setLayout(gl);
        GridData gd = new GridData(SWT.FILL, SWT.NONE, false, true);

        container.setLayoutData(gd);

        Label lbl_explain = new Label(container, SWT.WRAP);
        gd.widthHint = 600;
        lbl_explain.setLayoutData(gd);
        lbl_explain.setText(Messages.RevokeCertDialog_explain_text_revoke_cert);

        reason = new Combo(container, SWT.DROP_DOWN);
        GridData reason_gd = new GridData(SWT.LEFT, SWT.NONE, false, false);
        reason_gd.widthHint = 530;
        reason.setLayoutData(reason_gd);
        reason.add(Messages.RevokeCertDialog_reason_privkey_compromised);
        reason.add(Messages.RevokeCertDialog_reason_privkey_lost);
        reason.select(0);
        reason.pack(true);

        container.layout();
        return container;

    }

    @Override
    protected void okPressed() {
        txt_reason = reason.getText();
        Util.showMessageBox(Messages.RevokeCertDialog_msgbox_title_rr_to_ca,
                Messages.RevokeCertDialog_msgbox_text_rr_to_ca, SWT.ICON_INFORMATION);
        super.okPressed();
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, Messages.RevokeCertDialog_btn_send_rr_to_ca, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    public String getReason() {
        return txt_reason;
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.RevokeCertDialog_headline);
    }
}
