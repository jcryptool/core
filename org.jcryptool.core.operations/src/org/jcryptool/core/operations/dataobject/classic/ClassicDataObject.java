// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.dataobject.classic;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

/**
 * 
 * The ClassicDataObject class is the implementation of the IDataObject interface for classic algorithms.
 * 
 * @author amro
 * @version 0.1
 */
public class ClassicDataObject implements IClassicDataObject {

    /**
     * cipher as char representation
     */
    private char[] output;

    /**
     * key as char representation
     */
    private char[] key;

    /**
     * filter non-alphanumeric characters?
     */
    private boolean filterNonAlphaChars;

    /**
     * text transformations
     */
    private TransformData transformData;

    /**
     * Character used as null value in some ciphers
     */
    private char nullchar;

    /**
     * key as char representation
     */
    private char[] key2;

    private AbstractAlphabet cypherAlphabet;

    /**
     * The plain text.
     */
    private char[] plain;

    /**
     * operation mode
     */
    private int opmode;

    private InputStream inputStream;

    private OutputStream outputStream;

    private InputStream outputIS;

    /**
     * create DataObject with an initial alphabet
     * 
     * @param alpha initial alphabet
     */
    public ClassicDataObject(AbstractAlphabet alpha) {
        cypherAlphabet = alpha;
    }

    /**
     * default constructor
     */
    public ClassicDataObject() {
        super();
    }

    /**
     * method is defined by interface IDataObject used to set the editors input after operation was executed
     */
    public String getOutput() {
        return String.copyValueOf(output);
    }

    /**
     * method is defined by interface IDataObject used to provide input for the operation
     */
    public void setPlain(String input) {
        plain = input.toCharArray();
    }

    public void setPlain(byte[] input) {

    }

    /**
     * Getter for plain text
     * 
     * @return the plain text
     */
    public char[] getPlain() {
        return plain;
    }

    /**
     * specific method for this IDataObject type
     * 
     * @param finalOutput
     */
    public void setOutput(char[] finalOutput) {
        output = finalOutput;
    }

    public AbstractAlphabet getAlphabet() {
        return cypherAlphabet;
    }

    public void setAlphabet(AbstractAlphabet alphabet) {
        this.cypherAlphabet = alphabet;
    }

    /**
     * Getter for the algorithm's key
     * 
     * @return the key as a char array
     */
    public char[] getKey() {
        return key;
    }

    /**
     * Setter for the key of the algorithm.
     * 
     * @param key the key via a char array.
     */
    public void setKey(char[] key) {
        this.key = key;
    }

    /**
     * Getter for the operations mode
     * 
     * @return the operation mode as int value
     */
    public int getOpmode() {
        return opmode;
    }

    /**
     * Setter for the operation mode
     * 
     * @param opmode the operation mode via int value
     */
    public void setOpmode(int opmode) {
        this.opmode = opmode;
    }

    public char getNullchar() {
        return nullchar;
    }

    public void setNullchar(char nullchar) {
        this.nullchar = nullchar;
    }

    public char[] getKey2() {
        return key2;
    }

    public void setKey2(char[] key2) {
        this.key2 = key2;
    }

    public boolean isFilterNonAlphaChars() {
        return filterNonAlphaChars;
    }

    public void setFilterNonAlphaChars(boolean filterNonAlphaChars) {
        this.filterNonAlphaChars = filterNonAlphaChars;
    }

    public TransformData getTransformData() {
        return transformData;
    }

    public void setTransformData(TransformData transformData) {
        this.transformData = transformData;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setOutputIS(InputStream is) {
        this.outputIS = is;
    }

    public InputStream getOutputIS() {
        if (outputIS != null)
            return outputIS;
        if (this.outputStream instanceof ByteArrayOutputStream) {
            return new BufferedInputStream(new ByteArrayInputStream(
                    ((ByteArrayOutputStream) this.outputStream).toByteArray()));
        } else {
            return null;
        }
    }
}
