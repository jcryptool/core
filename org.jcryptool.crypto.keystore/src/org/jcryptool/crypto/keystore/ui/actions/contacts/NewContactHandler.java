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
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.ui.actions.del.Messages;
import org.jcryptool.crypto.keystore.ui.views.interfaces.ISelectedNodeListener;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;

/**
 * @author Anatoli Barski
 * @author Holger Friedrich (support for Commands, additional class based on NewContactAction)
 * 
 */
public class NewContactHandler extends AbstractHandler {
    private ISelectedNodeListener descriptor;

    public NewContactHandler(ISelectedNodeListener descriptor) {
        this.descriptor = descriptor;
        // this.setText(Messages.getString("Label.NewContact")); //$NON-NLS-1$
        // this.setToolTipText(Messages.getString("Label.NewContact")); //$NON-NLS-1$
        // this.setImageDescriptor(KeyStorePlugin.getImageDescriptor("icons/16x16/user-add.png")); //$NON-NLS-1$
    }

    public Object execute(ExecutionEvent event) {
        LogUtil.logInfo("creating " + descriptor.getSelectedNodeInfo()); //$NON-NLS-1$
        InputDialog dialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                Messages.getString("Label.NewContact"), Messages.getString("Label.NewContactName"), "Alice", null);
        if (dialog.open() != Window.CANCEL) {
            if (ContactManager.getInstance().contactExists(dialog.getValue())) {
                MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        Messages.getString("Label.NewContactFailed"), Messages.getString("Label.ContactExists"));
            } else {
                // create new contact
                ContactManager.getInstance().newContact(dialog.getValue());
            }
        }
        return(null);
    }

}
