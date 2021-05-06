// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.descriptors;

import javax.crypto.SecretKey;

import org.jcryptool.crypto.keystore.descriptors.interfaces.INewEntryDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewSecretKeyDescriptor;

public class NewSecretKeyDescriptor extends NewEntryDescriptor implements INewSecretKeyDescriptor {

    private SecretKey secretKey;

    public NewSecretKeyDescriptor(INewEntryDescriptor descriptor, SecretKey secretKey) {
        super(descriptor.getContactName(), descriptor.getAlgorithmName(), descriptor.getDisplayedName(), descriptor
                .getKeyLength(), descriptor.getPassword(), descriptor.getProvider(), descriptor.getKeyStoreEntryType());
        this.secretKey = secretKey;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

}
