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
package org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.io;

import org.eclipse.jface.action.Action;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.ISelectedOperationListener;

public class SetInputEditorAction extends Action {
    private IFlexiProviderOperation entryNode;
    private ISelectedOperationListener listener;

    public SetInputEditorAction(ISelectedOperationListener listener) {
        this.setText(Messages.SetInputEditorAction_0);
        this.setToolTipText(Messages.SetInputEditorAction_1);
        this.listener = listener;
    }

    public void run() {
        this.entryNode = listener.getFlexiProviderOperation();
        LogUtil.logInfo("setting input editor (" + entryNode.getTimestamp() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        entryNode.setInput("<Editor>"); //$NON-NLS-1$
    }

}
