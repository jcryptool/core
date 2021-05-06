// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.engines;

import org.jcryptool.crypto.flexiprovider.engines.cipher.AsymmetricBlockCipherEngine;
import org.jcryptool.crypto.flexiprovider.engines.cipher.AsymmetricHybridCipherEngine;
import org.jcryptool.crypto.flexiprovider.engines.cipher.BlockCipherEngine;
import org.jcryptool.crypto.flexiprovider.engines.cipher.CipherEngine;
import org.jcryptool.crypto.flexiprovider.engines.mac.MacEngine;
import org.jcryptool.crypto.flexiprovider.engines.messagedigest.MessageDigestEngine;
import org.jcryptool.crypto.flexiprovider.engines.securerandom.SecureRandomEngine;
import org.jcryptool.crypto.flexiprovider.engines.signature.SignatureEngine;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;

/**
 * Creates a FlexiProviderEngine for a registry type.
 *
 * @author Anatoli Barski
 */
public class FlexiProviderEngineFactory {
    /**
     * Returns the matching FlexiProviderEngine for the given registry type. Will return null in
     * case of an unknown registry type.
     *
     * @param registryType The registry type to look for
     * @return The FlexiProviderEngine, may be null
     */
    public static FlexiProviderEngine createEngine(RegistryType registryType) {
        switch (registryType) {
        case ASYMMETRIC_BLOCK_CIPHER:
            return new AsymmetricBlockCipherEngine();
        case ASYMMETRIC_HYBRID_CIPHER:
            return new AsymmetricHybridCipherEngine();
        case BLOCK_CIPHER:
            return new BlockCipherEngine();
        case CIPHER:
            return new CipherEngine();
        case MAC:
            return new MacEngine();
        case MESSAGE_DIGEST:
            return new MessageDigestEngine();
        case SECURE_RANDOM:
            return new SecureRandomEngine();
        case SIGNATURE:
            return new SignatureEngine();
        default:
            return null;
        }
    }
}
