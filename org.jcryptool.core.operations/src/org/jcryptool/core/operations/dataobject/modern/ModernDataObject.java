// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
/**
 * 
 */
package org.jcryptool.core.operations.dataobject.modern;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The implementation of the IDataObject interface for modern algorithms.
 * 
 * @author amro
 * @author t-kern
 * @version 0.1
 * 
 */
public class ModernDataObject implements IModernDataObject {

    /**
     * the initial input of the algorithm's operation in byte representation
     */
    private byte[] plain;

    /** The output produced by a modern algorithm is always binary */
    private byte[] output;

    /** The name of the algorithm */
    private String algorithmName;

    /** The padding scheme of the algorithm */
    private String paddingName;

    /** The opmode */
    private int opmode = -1;

    private InputStream inputStream;

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    private OutputStream outputStream;

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    private InputStream outputIS;

    public void setOutputIS(InputStream inputStream) {
        outputIS = inputStream;
    }

    public InputStream getOutputIS() {
        return outputIS;
    }

    /**
     * Sets the algorithm's name
     * 
     * @param algorithmName The algorithm name
     */
    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    /**
     * Returns the algorithm's name
     * 
     * @return The algorithm's name
     */
    public String getAlgorithmName() {
        return algorithmName;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.IModernDataObject#setPaddingName(java.lang.String)
     */
    public void setPaddingName(String paddingName) {
        this.paddingName = paddingName;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.IModernDataObject#getPaddingName()
     */
    public String getPaddingName() {
        return paddingName;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.IModernDataObject#setOpmode(int)
     */
    public void setOpmode(int opmode) {
        this.opmode = opmode;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.IModernDataObject#getOpmode()
     */
    public int getOpmode() {
        return opmode;
    }

    /**
     * Sets the ouptut.
     * 
     * @param output The output as a byte []
     */
    public void setOutput(byte[] output) {
        this.output = output;
    }

    /**
     * Returns the plaintext as a byte array
     * 
     * @return The plaintext as a byte array
     */
    public byte[] getInput() {
        return plain;
    }

    public void setInput(byte[] input) {
        plain = input;
    }

    public byte[] getOutput() {
        return output;
    }

}
