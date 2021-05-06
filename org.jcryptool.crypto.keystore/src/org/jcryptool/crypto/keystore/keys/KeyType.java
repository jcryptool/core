// -----BEGIN DISCLAIMER-----
/**************************************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore.keys;

/**
 * Enum of possible entry-types in the key store.
 * 
 * @author t-kern
 * 
 */
public enum KeyType {

    SECRETKEY("secret"), //$NON-NLS-1$
    PUBLICKEY("public"), //$NON-NLS-1$
    KEYPAIR("keypair"), //$NON-NLS-1$
    KEYPAIR_PUBLIC_KEY("keypair.public"), //$NON-NLS-1$
    KEYPAIR_PRIVATE_KEY("keypair.private"), //$NON-NLS-1$
    UNKNOWN("unknown"); //$NON-NLS-1$

    /** Type of this entry */
    private String type;

    /**
     * 
     * @param type The type of this entry
     */
    private KeyType(String type) {
        this.type = type;
    }

    /**
     * Returns the type of this entry.
     * 
     * @return The type of this entry
     */
    public String getType() {
        return type;
    }

}