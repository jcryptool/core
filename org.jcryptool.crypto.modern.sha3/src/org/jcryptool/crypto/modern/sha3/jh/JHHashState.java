// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.modern.sha3.jh;

/**
 * Helper class for JH.
 *
 * @author Michael Rupp
 */
public class JHHashState {

    /**
     * the message digest size
     */
    protected int hashbitlen;
    /**
     * the message size in bits
     */
    protected long databitlen;
    /**
     * the hash value H; 128 bytes
     */
    protected byte H[] = new byte[128];
    /**
     * the temporary round value; 256 4-bit elements
     */
    protected byte A[] = new byte[256];
    /**
     * round constant for one round; 64 4-bit elements
     */
    protected byte roundconstant[] = new byte[64];
    /**
     * the message block to be hashed; 64 bytes
     */
    protected byte buffer[] = new byte[64];

    /**
     * Array of the final hash value.
     */
    protected byte hash[];

    /**
     * Constructor
     *
     * @param hashbitlen sets the hash bitlength.
     * @param databitlen sets the data bitlength.
     */
    public JHHashState(int hashbitlen, long databitlen) {
        this.hashbitlen = hashbitlen;
        this.databitlen = databitlen;
        this.hash = new byte[hashbitlen / 8];
    }

    /**
     * Converts a string of the hash value in a array of the hash value.
     *
     * @param hashval
     */
    public void setHash(byte[] hashval) {
        for (int i = 0; i < hashbitlen / 8; i++) {
            hash[i] = hashval[i];
        }
    }

    /**
     * Returns the hash value.
     *
     * @return hash value
     */
    public byte[] getHash() {
        return this.hash;
    }

    /**
     * Returns the message block to hash (64 bit).
     *
     * @return 64 bit message block.
     */
    public byte[] getBuffer() {
        return this.buffer;
    }

    /**
     * Returns the hash value H (128 bit).
     *
     * @return 128 bit hash value.
     */
    public byte[] getH() {
        return this.H;
    }

    /**
     * Returns the hash bitlength of a hashstate.
     */
    public int getHashBitlen() {
        return this.hashbitlen;
    }

    /**
     * Sets a hash bitlength of a hashstate.
     *
     * @param len could be 224, 256, 384 or 512.
     */
    public void setHashBitlen(int len) {
        this.hashbitlen = len;
    }
}