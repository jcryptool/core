// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.dialogs;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class SaveRecordConfirmation {

    // instance vars
    private Shell parent;
    private boolean success;
    private String error;

    // constructor
    public SaveRecordConfirmation(Shell parent, boolean success, String error) {
        super();
        this.parent = parent;
        this.success = success;
        this.error = error;
    }

    // methods
    public int open() {
        String dialog;
        int dialogImageType;

        if (success) {
            dialog = Messages.SaveRecordConfirmation_0;
            dialogImageType = MessageDialog.INFORMATION;
        } else {
            dialog = Messages.SaveRecordConfirmation_1 + System.lineSeparator() + error;
            dialogImageType = MessageDialog.ERROR;
        }
        String[] labels = new String[] { "OK" };
        MessageDialog dialogWindow = new MessageDialog(parent, null, null, dialog, dialogImageType, labels, -1);

        return dialogWindow.open();
    }
}
