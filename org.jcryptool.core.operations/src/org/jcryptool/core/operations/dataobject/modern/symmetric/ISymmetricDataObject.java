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

import org.jcryptool.core.operations.dataobject.modern.IModernDataObject;

/**
 * Provides a top level frame for the data objects used for symmetric algorithms.
 * 
 * @author t-kern
 * 
 */
public interface ISymmetricDataObject extends IModernDataObject {

    /**
     * Sets the IV.
     * 
     * @param iv
     */
    public void setIV(byte[] iv);

    /**
     * Returns the IV.
     * 
     * @return The IV
     */
    public byte[] getIV();

    /**
     * Sets the key.
     * 
     * @param key
     */
    public void setSymmetricKey(byte[] key);

    /**
     * Returns the key.
     * 
     * @return The key
     */
    public byte[] getSymmetricKey();

    /**
     * Sets the cipher mode.
     * 
     * @param cipherMode
     */
    public void setCipherMode(String cipherMode);

    /**
     * Returns the cipher mode.
     * 
     * @return The cipher mode
     */
    public String getCipherMode();

}
