// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.listeners.INewOperationListener;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;

public class NewOperationListener implements INewOperationListener {
    public void newOperation(AlgorithmDescriptor descriptor) {
        LogUtil.logInfo("Plugin interface - newOperation(AlgorithmDescriptor descriptor) triggered"); //$NON-NLS-1$
        OperationsManager.getInstance().addOperation(descriptor);
    }

}
