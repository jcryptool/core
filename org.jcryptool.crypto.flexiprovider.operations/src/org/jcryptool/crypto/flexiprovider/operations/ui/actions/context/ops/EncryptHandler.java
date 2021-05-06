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
package org.jcryptool.crypto.flexiprovider.operations.ui.actions.context.ops;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.ISelectedOperationListener;
import org.jcryptool.crypto.flexiprovider.types.OperationType;

public class EncryptHandler extends AbstractHandler {
    private IFlexiProviderOperation descriptor;
    private ISelectedOperationListener listener;

    public EncryptHandler(ISelectedOperationListener listener) {
        // this.setText(Messages.EncryptAction_0);
        // this.setToolTipText(Messages.EncryptAction_1);
        this.listener = listener;
    }

    public Object execute(ExecutionEvent event) {
        descriptor = listener.getFlexiProviderOperation();
        LogUtil.logInfo("Encrypt (" + descriptor.getTimestamp() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        descriptor.setOperation(OperationType.ENCRYPT);
        return(null);
    }

}
