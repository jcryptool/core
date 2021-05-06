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

import java.security.PrivateKey;
import java.security.PublicKey;

import org.jcryptool.crypto.keystore.descriptors.interfaces.INewEntryDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewKeyPairDescriptor;

public class NewKeyPairDescriptor extends NewEntryDescriptor implements INewKeyPairDescriptor {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public NewKeyPairDescriptor(INewEntryDescriptor descriptor, PrivateKey privateKey, PublicKey publicKey) {
        super(descriptor.getContactName(), descriptor.getAlgorithmName(), descriptor.getDisplayedName(), descriptor
                .getKeyLength(), descriptor.getPassword(), descriptor.getProvider(), descriptor.getKeyStoreEntryType());
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public java.security.PrivateKey getPrivateKey() {
        return privateKey;
    }

    public java.security.PublicKey getPublicKey() {
        return publicKey;
    }

}
