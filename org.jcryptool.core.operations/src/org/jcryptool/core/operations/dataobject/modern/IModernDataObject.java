// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.dataobject.modern;

import java.io.InputStream;
import java.io.OutputStream;

import org.jcryptool.core.operations.dataobject.IDataObject;

/**
 * Provides a top level frame for the data objects used for modern algorithms.
 * 
 * @author t-kern
 * 
 */
public interface IModernDataObject extends IDataObject {

    /** Transparent wrapper for the encryption mode */
    int ENCRYPT_MODE = 1; // Cipher.ENCRYPT_MODE;

    /** Transparent wrapper for the decryption mode */
    int DECRYPT_MODE = 2; // Cipher.DECRYPT_MODE;

    void setInputStream(InputStream inputStream);

    InputStream getInputStream();

    void setOutputStream(OutputStream outputStream);

    OutputStream getOutputStream();

    void setOutputIS(InputStream inputStream);

    InputStream getOutputIS();

    /**
     * Returns the input.
     * 
     * @return The input
     */
    byte[] getInput();

    /**
     * Returns the opmode
     * 
     * @return The opmode
     */
    int getOpmode();

    /**
     * Returns the output.
     * 
     * @return The output
     */
    byte[] getOutput();

    /**
     * Returns the padding scheme.
     * 
     * @return The padding scheme
     */
    String getPaddingName();

    /**
     * Sets the input.
     * 
     * @param input
     */
    void setInput(byte[] input);

    /**
     * Sets the opmode
     * 
     * @param opmode
     */
    void setOpmode(int opmode);

    /**
     * Sets the output.
     * 
     * @param output
     */
    void setOutput(byte[] output);

    /**
     * Sets the padding scheme.
     * 
     * @param paddingName The padding scheme
     */
    void setPaddingName(String paddingName);
}
