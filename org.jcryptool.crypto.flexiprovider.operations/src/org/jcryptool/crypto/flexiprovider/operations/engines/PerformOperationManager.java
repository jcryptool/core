// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations.engines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.providers.ProviderManager2;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.operations.FlexiProviderOperationsPlugin;

import de.flexiprovider.FlexiProviderController;

public class PerformOperationManager {
    private static PerformOperationManager instance;

    private static List<IPerfomOperationListener> listeners = Collections
            .synchronizedList(new ArrayList<IPerfomOperationListener>());

    private PerformOperationManager() {
        loadExtensions();
    }

    public synchronized static PerformOperationManager getInstance() {
        if (instance == null)
            instance = new PerformOperationManager();
        return instance;
    }

    public void firePerformOperation(IFlexiProviderOperation operation) {
    	try {
     		ProviderManager2.getInstance().pushFlexiProviderPromotion();
			for (IPerfomOperationListener listener : listeners) {
				listener.performOperation(operation);
			}
    	} finally {
     		ProviderManager2.getInstance().popCryptoProviderPromotion();
    	}
    }

    private static void loadExtensions() {
        LogUtil.logInfo("loading extensions"); //$NON-NLS-1$
        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
                FlexiProviderOperationsPlugin.PLUGIN_ID, "performFlexiProviderOperation"); //$NON-NLS-1$
        IExtension[] extensions = extensionPoint.getExtensions();
        for (IExtension extension : extensions) {
            IConfigurationElement[] configElements = extension.getConfigurationElements();
            for (IConfigurationElement configElement : configElements) {
                try {
                    IPerfomOperationListener newListener = (IPerfomOperationListener) configElement
                            .createExecutableExtension("listenerClass"); //$NON-NLS-1$
                    listeners.add(newListener);
                } catch (CoreException e) {
                    LogUtil.logError(FlexiProviderOperationsPlugin.PLUGIN_ID,
                            "CoreException while creating a new IPerfomOperationListener", e, false); //$NON-NLS-1$
                }
            }
        }
    }

}
