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

import org.jcryptool.crypto.keystore.descriptors.interfaces.INewEntryDescriptor;
import org.jcryptool.crypto.keystore.keys.KeyType;

public class NewEntryDescriptor implements INewEntryDescriptor {

    private String contactName;
    private String algoName;
    private String displayedName;
    private String password;
    private int keyLength = -1;
    private String provider;
    private KeyType type;

    public NewEntryDescriptor(String contactName, String algoName, String displayedName, int keyLength,
            String password, String provider, KeyType type) {
        this.contactName = contactName;
        this.algoName = algoName;
        this.displayedName = displayedName;
        this.keyLength = keyLength;
        this.password = password;
        this.provider = provider;
        this.type = type;
    }

    public String getAlgorithmName() {
        return algoName;
    }

    public String getContactName() {
        return contactName;
    }

    public int getKeyLength() {
        if (keyLength == -1) {
            return -1;
        } else {
            return keyLength;
        }
    }

    public String getPassword() {
        return password;
    }

    public String getProvider() {
        return provider;
    }

    public KeyType getKeyStoreEntryType() {
        return type;
    }

    public String getDisplayedName() {
        return displayedName;
    }

}
