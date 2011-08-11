//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.util.ui;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Password prompt: opens prompt dialog for password input.
 * 
 * @author Anatoli Barski
 */
public class PasswordPrompt {
	
	public static char[] prompt(Shell shell) {
        InputDialog dialog = new InputDialog(
                shell,
                Messages.getString("PasswordPromt.Title"), Messages.getString("PasswordPromt.Message"), //$NON-NLS-1$ //$NON-NLS-2$
                "", null) { //$NON-NLS-1$

            protected Control createDialogArea(Composite parent) {
                Control control = super.createDialogArea(parent);
                getText().setEchoChar('*');
                return control;
            }
        };
        int result = dialog.open();
        if (result == Window.OK) {
            return dialog.getValue().toCharArray();
        } else {
            return null;
        }
    }
		
}
