/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.dialogs;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.xml.security.core.verify.VerificationResult;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;

/**
 * <p>Special dialog type (based on MessageDialog) to show the verification result of a
 * signature with an image indicating the signature status on the left side.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class VerificationDialog extends MessageDialog {
    /** The status of the signature. */
    private String status = "";

    /**
     * Constructor for the VerificationDialog.
     *
     * @param shell The parent shell
     * @param title The dialog title
     * @param text The dialog info text
     * @param verificationStatus The status of the signature
     */
    public VerificationDialog(final Shell shell, final String title,
        final String text, final String verificationStatus) {
        super(shell, title, null, text, 0, new String[] {"Close"}, 0);
        status = verificationStatus;
    }

    /**
     * Returns the image for this dialog based on the status of the verified signature.
     *
     * @return The image to display
     */
    public Image getImage() {
        if (status.equals(VerificationResult.VALID)) {
            return XSTUIPlugin.getDefault().getImageRegistry().get("sig_valid_large");
        } else if (status.equals(VerificationResult.INVALID)) {
            return XSTUIPlugin.getDefault().getImageRegistry().get("sig_invalid_large");
        } else {
            return XSTUIPlugin.getDefault().getImageRegistry().get("sig_unknown_large");
        }
    }
}
