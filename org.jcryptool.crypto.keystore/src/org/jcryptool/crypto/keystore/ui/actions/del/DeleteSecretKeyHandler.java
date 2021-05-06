// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2013, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.actions.del;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.ui.views.interfaces.ISelectedNodeListener;

/**
 * @author tkern
 * @author Holger Friedrich (supportfor Commands, additional class based on DeleteSecretKeyAction)
 * 
 */
public class DeleteSecretKeyHandler extends AbstractHandler {

    private ISelectedNodeListener descriptor;

    public DeleteSecretKeyHandler(ISelectedNodeListener descriptor) {
        this.descriptor = descriptor;
        // this.setText(Messages.getString("Label.DeleteSecretKey")); //$NON-NLS-1$
        // this.setToolTipText(Messages.getString("Label.DeleteSecretKey")); //$NON-NLS-1$
        // this.setImageDescriptor(KeyStorePlugin.getImageDescriptor("icons/16x16/cancel.png")); //$NON-NLS-1$
    }

    public Object execute(ExecutionEvent event) {
        boolean result = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                Messages.getString("Label.DeleteSecretKey"), Messages.getString("Label.AreYouSureSecretKey")); //$NON-NLS-1$ //$NON-NLS-2$
        if (result) {
            // remove from tree and from keystore
            KeyStoreManager.getInstance().deleteEntry(descriptor.getSelectedNodeAlias());
        }
        return(null);
    }

}
