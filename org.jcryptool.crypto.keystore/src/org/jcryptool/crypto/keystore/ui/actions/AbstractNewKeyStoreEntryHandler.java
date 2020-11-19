// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.ui.actions;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.providers.ProviderManager2;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.descriptors.NewKeyPairDescriptor;
import org.jcryptool.crypto.keystore.descriptors.NewSecretKeyDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewEntryDescriptor;

public abstract class AbstractNewKeyStoreEntryHandler extends AbstractKeyStoreHandler {
    protected KeyStoreAlias performNewKeyAction(INewEntryDescriptor descriptor) {
    	try {
     		ProviderManager2.getInstance().pushFlexiProviderPromotion();
			if (descriptor instanceof NewSecretKeyDescriptor) {
				return addSecretKey(descriptor, ((NewSecretKeyDescriptor) descriptor).getSecretKey());
			} else if (descriptor instanceof NewKeyPairDescriptor) {
				return addKeyPair(descriptor, ((NewKeyPairDescriptor) descriptor).getPrivateKey(),
						((NewKeyPairDescriptor) descriptor).getPublicKey());
			} else {
				LogUtil.logError(KeyStorePlugin.PLUGIN_ID, "INewEntryDescriptor instance " + INewEntryDescriptor.class
						+ " is invalid");
			}

			return null;
    		
    	} finally {

     		ProviderManager2.getInstance().popCryptoProviderPromotion();
    		
    	}
    }
}
