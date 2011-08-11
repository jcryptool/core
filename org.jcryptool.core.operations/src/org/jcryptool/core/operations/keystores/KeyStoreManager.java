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
package org.jcryptool.core.operations.keystores;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;

/**
 * The central manager class for all keystores.
 *
 * @author t-kern
 *
 */
public class KeyStoreManager {
    /** Singleton instance */
    private static KeyStoreManager instance;

    // private ArrayList<AbstractKeyStoreHandler> keystoreHandlers = new
    // ArrayList<AbstractKeyStoreHandler>();

    /** The default jct keystore handler */
    private AbstractKeyStoreHandler jctDefaultHandler;

    /**
     * Private singleton constructor.
     */
    private KeyStoreManager() {
        loadKeyStores();
    }

    /**
     * Returns an instance of this manager.
     *
     * @return An instance of this manager
     */
    public synchronized static KeyStoreManager getInstance() {
        if (instance == null)
            instance = new KeyStoreManager();
        return instance;
    }

    /**
     * Loads all keystore handler plug-ins.
     */
    private void loadKeyStores() {
        LogUtil.logInfo("Loading keystores"); //$NON-NLS-1$
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = registry.getExtensionPoint(OperationsPlugin.PLUGIN_ID, "keystores"); //$NON-NLS-1$

        IExtension[] extensions = extensionPoint.getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            if (extensions[i].getNamespaceIdentifier().equals("org.jcryptool.crypto.keystore")) { //$NON-NLS-1$
                IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
                for (int j = 0; j < configElements.length; j++) {
                    if (configElements[j].getName().equals("keystore")) { //$NON-NLS-1$
                        try {
                            jctDefaultHandler = (AbstractKeyStoreHandler) configElements[j]
                                    .createExecutableExtension("keyStoreHandler"); //$NON-NLS-1$
                            LogUtil.logInfo("keystore loaded"); //$NON-NLS-1$
                            // keystoreHandlers.add(handler);
                        } catch (CoreException e) {
                            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while loading a keystore handler", //$NON-NLS-1$
                                    e, false);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the default jct key store handler.
     *
     * @return The default jct key store handler.
     */
    public AbstractKeyStoreHandler getJCTKeyStoreHandler() {
        return jctDefaultHandler;
    }

}
