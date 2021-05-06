// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.keystore;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.SecureRandom;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;

/**
 * The activator class controls the plug-in life cycle
 */
public class FlexiProviderKeystorePlugin extends AbstractUIPlugin {
    /** The plug-in ID. */
    public static final String PLUGIN_ID = "org.jcryptool.crypto.flexiprovider.keystore"; //$NON-NLS-1$

    private static SecureRandom sha1prng;

    public static SecureRandom getSecureRandom() {
        try {
            if (sha1prng == null)
                sha1prng = Registry.getSecureRandom("SHA1PRNG"); //$NON-NLS-1$
            return sha1prng;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}