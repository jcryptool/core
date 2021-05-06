//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.logging.dialogs;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * JCrypTool message-handling plug-in. This class offers several convenience methods to display messages. All methods
 * are static for easy access.
 * 
 * @author Nils Reimers
 * @version 0.9.5
 */
public class JCTMessageDialog {
    /**
     * Utility class without public constructor.
     */
    private JCTMessageDialog() {
    }

    /**
     * Opens an error dialog to display the given error.
     * 
     * @param error to display
     */
    public static void showErrorDialog(final Status error, final String message) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                ExceptionDetailsErrorDialog.openError(null, Messages.MessageDialog_error, message, error);
            }
        });

    }

    /**
     * Opens an info dialog to display the given info.
     * 
     * @param info to display
     */
    public static void showInfoDialog(IStatus info) {
        MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                Messages.MessageDialog_info, info.getMessage());
    }
}
