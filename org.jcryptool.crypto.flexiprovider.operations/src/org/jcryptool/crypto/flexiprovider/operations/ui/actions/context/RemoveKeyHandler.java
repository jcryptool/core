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
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.ISelectedOperationListener;

public class RemoveKeyHandler extends AbstractHandler {
    private IFlexiProviderOperation entryNode;
    private ISelectedOperationListener listener;

    public RemoveKeyHandler(ISelectedOperationListener listener) {
        // this.setText(Messages.RemoveKeyAction_0);
        // this.setToolTipText(Messages.RemoveKeyAction_1);
        // this.setImageDescriptor(FlexiProviderOperationsPlugin.getImageDescriptor("icons/16x16/cancel.png")); //$NON-NLS-1$
        this.listener = listener;
    }

    public Object execute(ExecutionEvent event) {
        this.entryNode = listener.getFlexiProviderOperation();
        entryNode.setKeyStoreAlias(null);
        return(null);
    }
}
