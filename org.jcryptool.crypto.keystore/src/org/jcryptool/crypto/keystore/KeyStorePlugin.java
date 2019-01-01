// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;

/**
 * The activator class controls the plug-in life cycle and initializes the singleton JCrypTool
 * KeyStoreManager.
 * 
 * @see org.jcryptool.crypto.keystore.backend.KeyStoreManager
 * 
 * @author Tobias Kern, Dominik Schadow
 */
public class KeyStorePlugin extends AbstractUIPlugin {
    /** The plug-in ID, value is {@value} . */
    public static final String PLUGIN_ID = "org.jcryptool.crypto.keystore"; //$NON-NLS-1$

    /**
     * The constructor, initializes the singleton KeyStoreManager instance.
     */
    public KeyStorePlugin() {
        KeyStoreManager.getInstance();
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path.
     * 
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    /**
     * Returns an image descriptor for the image file at the provided plug-in relative path.
     * 
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String pluginID, String path) {
        return imageDescriptorFromPlugin(pluginID, path);
    }
}
