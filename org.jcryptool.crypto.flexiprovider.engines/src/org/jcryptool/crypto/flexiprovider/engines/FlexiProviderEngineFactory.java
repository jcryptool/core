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
 * this is class is used to create a flexi provider engine for a type
 * @author Anatoli Barski
 */

public class FlexiProviderEngineFactory {

	public static FlexiProviderEngine createEngine(RegistryType registryType) {
		FlexiProviderEngine engine = null;
		switch (registryType) {
	        case ASYMMETRIC_BLOCK_CIPHER:
	            engine = new AsymmetricBlockCipherEngine();
	            break;
	        case ASYMMETRIC_HYBRID_CIPHER:
	            engine = new AsymmetricHybridCipherEngine();
	            break;
	        case BLOCK_CIPHER:
	            engine = new BlockCipherEngine();
	            break;
	        case CIPHER:
	            engine = new CipherEngine();
	            break;
	        case MAC:
	            engine = new MacEngine();
	            break;
	        case MESSAGE_DIGEST:
	            engine = new MessageDigestEngine();
	            break;
	        case SECURE_RANDOM:
	            engine = new SecureRandomEngine();
	            break;
	        case SIGNATURE:
	            engine = new SignatureEngine();
	            break;
			default:
				break;
		}
		return engine;
	}

}
