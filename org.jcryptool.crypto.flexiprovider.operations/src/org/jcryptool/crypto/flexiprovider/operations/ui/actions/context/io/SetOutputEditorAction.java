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

public class SetOutputEditorAction extends Action {
    private IFlexiProviderOperation entryNode;
    private ISelectedOperationListener listener;

    public SetOutputEditorAction(ISelectedOperationListener listener) {
        this.setText(Messages.SetOutputEditorAction_0);
        this.setToolTipText(Messages.SetOutputEditorAction_1);
        this.listener = listener;
    }

    public void run() {
        this.entryNode = listener.getFlexiProviderOperation();
        LogUtil.logInfo("setting output editor (" + entryNode.getTimestamp() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        entryNode.setOutput("<Editor>"); //$NON-NLS-1$
    }

}
