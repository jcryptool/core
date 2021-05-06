// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.actions.contacts;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.ui.actions.del.Messages;
import org.jcryptool.crypto.keystore.ui.views.interfaces.ISelectedNodeListener;

/**
 * @author tkern
 * @author Holger Friedrich (support for Commands, additional class based on DeleteContactAction)
 * 
 */
public class DeleteContactHandler extends AbstractHandler {
    private ISelectedNodeListener descriptor;

    public DeleteContactHandler(ISelectedNodeListener descriptor) {
        this.descriptor = descriptor;
        // this.setText(Messages.getString("Label.DeleteContact")); //$NON-NLS-1$
        // this.setToolTipText(Messages.getString("Label.DeleteContact")); //$NON-NLS-1$
        // this.setImageDescriptor(KeyStorePlugin.getImageDescriptor("icons/16x16/user-delete-3.png")); //$NON-NLS-1$
    }

    public Object execute(ExecutionEvent event) {
        LogUtil.logInfo("deleting " + descriptor.getSelectedNodeInfo()); //$NON-NLS-1$
        boolean result = MessageDialog
                .openQuestion(
                        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        Messages.getString("Label.DeleteContact"), NLS.bind(Messages.getString("Label.AreYouSure"), new Object[] { descriptor.getSelectedNodeInfo() })); //$NON-NLS-1$ //$NON-NLS-2$
        if (result) {
            LogUtil.logInfo("deleting " + descriptor.getSelectedNodeInfo()); //$NON-NLS-1$
            // delete the contact
            KeyStoreManager.getInstance().deleteAllEntriesForContact(descriptor.getSelectedNodeInfo());
        }
        return(null);
    }

}
