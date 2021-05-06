// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.dataobject.modern.symmetric;

import org.jcryptool.core.operations.dataobject.modern.ModernDataObject;

/**
 * Data object for symmetric block ciphers.
 * 
 * @author t-kern
 * 
 */
public class SymmetricDataObject extends ModernDataObject implements ISymmetricDataObject {

    /** The initialization vector */
    private byte[] iv;

    /** The shared key */
    private byte[] symmetricKey;

    /** The cipher mode (i.e. CBC) */
    private String cipherMode;

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject#setIV(byte[])
     */
    public void setIV(byte[] iv) {
        this.iv = iv;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject#getIV()
     */
    public byte[] getIV() {
        return iv;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject#setSymmetricKey(byte[])
     */
    public void setSymmetricKey(byte[] key) {
        this.symmetricKey = key;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject#getSymmetricKey()
     */
    public byte[] getSymmetricKey() {
        return symmetricKey;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject#setCipherMode(java.lang.String)
     */
    public void setCipherMode(String cipherMode) {
        this.cipherMode = cipherMode;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.symmetric.ISymmetricDataObject#getCipherMode()
     */
    public String getCipherMode() {
        return cipherMode;
    }

}
