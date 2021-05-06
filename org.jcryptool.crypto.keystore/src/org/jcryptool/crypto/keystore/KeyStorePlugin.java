// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore;

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

}
