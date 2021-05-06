// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.pkcs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;

/**
 * 
 * 
 * @author t-kern
 * 
 */
public class PKCSManager {
    /** Singleton instance */
    private static PKCSManager instance;

    /** PKCS#7 factory */
    private AbstractPKCS7Factory pkcs7;

    /**
     * Private singleton constructor.
     */
    private PKCSManager() {
        load();
    }

    /**
     * Returns the singleton instance.
     * 
     * @return The singleton instance
     */
    public synchronized static PKCSManager getInstance() {
        if (instance == null)
            instance = new PKCSManager();
        return instance;
    }

    /**
     * Loads all pkcs handler plug-ins.
     */
    private void load() {
        LogUtil.logInfo("Loading pkcs"); //$NON-NLS-1$
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = registry.getExtensionPoint(OperationsPlugin.PLUGIN_ID, "pkcsFactories"); //$NON-NLS-1$

        IExtension[] extensions = extensionPoint.getExtensions();
        for (int i = 0; i < extensions.length; i++) {

            if (extensions[i].getNamespaceIdentifier().equals("org.jcryptool.crypto.modern.pkcs7")) { //$NON-NLS-1$
                IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
                for (int j = 0; j < configElements.length; j++) {
                    if (configElements[j].getName().equals("pkcsFactory")) { //$NON-NLS-1$
                        try {
                            // jctDefaultHandler =
                            // (AbstractKeyStoreHandler)configElements[j].createExecutableExtension("keyStoreHandler");
                            pkcs7 = (AbstractPKCS7Factory) configElements[j].createExecutableExtension("pkcsFactory"); //$NON-NLS-1$
                            LogUtil.logInfo("pkcs7 factory loaded"); //$NON-NLS-1$
                        } catch (CoreException e) {
                            LogUtil.logError(OperationsPlugin.PLUGIN_ID,
                                    "Exception while loading a keystore handler", e, false); //$NON-NLS-1$
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns a PKCS#7 factory.
     * 
     * @return A PKCS#7 factory
     */
    public AbstractPKCS7Factory getPKCS7Factory() {
        return pkcs7;
    }

}
