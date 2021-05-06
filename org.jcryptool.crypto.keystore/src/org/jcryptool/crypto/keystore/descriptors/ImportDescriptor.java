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

import org.jcryptool.crypto.keystore.descriptors.interfaces.IImportDescriptor;
import org.jcryptool.crypto.keystore.keys.KeyType;

public class ImportDescriptor implements IImportDescriptor {

    private String contactName;
    private String fileName;
    private String algoName;
    private String password;
    private String provider;
    private KeyType type;
    private int keylength;

    public ImportDescriptor(String contactName, String algoName, KeyType type, String fileName, String password,
            String provider, int keylength) {
        this.contactName = contactName;
        this.algoName = algoName;
        this.fileName = fileName;
        this.password = password;
        this.provider = provider;
        this.type = type;
        this.keylength = keylength;
    }

    public String getContactName() {
        return contactName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPassword() {
        return password;
    }

    public KeyType getKeyStoreEntryType() {
        return type;
    }

    public String getAlgorithmName() {
        return algoName;
    }

    public String getProvider() {
        return provider;
    }

    public int getKeyLength() {
        return keylength;
    }

    public String getDisplayedName() {
        // FIXME: displayed name for import descriptors?
        return getAlgorithmName();
    }

}
