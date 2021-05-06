// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.ui.actions.context;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.operations.OperationsManager;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.ISelectedOperationListener;

public class RemoveHandler extends AbstractHandler {
    private IFlexiProviderOperation entry;
    private ISelectedOperationListener listener;

    public RemoveHandler(ISelectedOperationListener listener) {
        // this.setText(Messages.RemoveAction_0);
        // this.setToolTipText(Messages.RemoveAction_1);
        // this.setImageDescriptor(FlexiProviderOperationsPlugin.getImageDescriptor("icons/16x16/cancel.png")); //$NON-NLS-1$
        this.listener = listener;
    }

    public Object execute(ExecutionEvent event) {
        this.entry = listener.getFlexiProviderOperation();
        LogUtil.logInfo("remove... (" + entry.getTimestamp() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        boolean result = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                Messages.RemoveHandler_2, Messages.RemoveHandler_3);
        if (result) {
            OperationsManager.getInstance().removeOperation(entry.getTimestamp());
        }
        return(null);
    }

}
