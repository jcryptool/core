// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.ui.actions.context;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.ISelectedOperationListener;

public class RenameAction extends Action {
    private IFlexiProviderOperation entry;
    private ISelectedOperationListener listener;

    public RenameAction(ISelectedOperationListener listener) {
        this.setText(Messages.RenameAction_0);
        this.setToolTipText(Messages.RenameAction_1);
        this.listener = listener;
    }

    public void run() {
        this.entry = listener.getFlexiProviderOperation();
        LogUtil.logInfo("rename... (" + entry.getTimestamp() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        InputDialog dialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.RenameAction_2,
                Messages.RenameAction_3 + entry.getEntryName(), "", null); //$NON-NLS-1$

        int result = dialog.open();
        if (result == Window.OK) {
            LogUtil.logInfo("new name should be: " + dialog.getValue()); //$NON-NLS-1$
            entry.setEntryName(dialog.getValue());
            LogUtil.logInfo("new name is: " + entry.getEntryName()); //$NON-NLS-1$
        }
    }

}
