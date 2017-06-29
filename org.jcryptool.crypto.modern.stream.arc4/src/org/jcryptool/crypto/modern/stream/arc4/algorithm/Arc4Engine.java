//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.modern.stream.arc4.algorithm;

/**
 *
 * the arc4 algorithm.
 *
 * @author david
 *
 */
public class Arc4Engine {

    /**
     * the key.
     */
    private byte[] key;

    /**
     * the sbox.
     */
    private byte[] box;

    /**
     * type of algo. spritz or arc4
     */
    private boolean algo;

    /**
     * W for Spritz
     */
    private int W;

    /**
     *
     * method for encrypting and decrypting data; it's the same because xor with chiffre and random.
     * numberr is plaint text and plain with random numberr is chiffre
     *
     * @param input -> the cipher or plain text
     * @return -> the cipher or plain text
     */
    public byte[] encryptDecrypt(byte[] input) {

        if (algo) {
            return crypt(input, input);
        } else {
            return crypt_spritz(input, input);
        }

    }

    /**
     *
     * the crypt method for arc4.
     *
     * @param input -> byte array with the cipher/plain text
     * @param output -> a byte array with the same sice as the input array; thats the cipher or
     *            plain text
     * @return output
     */
    private byte[] crypt(byte[] input, byte[] output) {
        int a = 0;
        int b = 0;

        // The pseudo-random generator algorithm
        for (int i = 0; i < input.length; i++) {
            a = (a + 1) & 0xff;
            b = (box[a] + b) & 0xff;

            // sw -> switch
            byte sw = box[a];
            box[a] = box[b];
            box[b] = sw;

            output[i] = (byte) ((input[i] ^ box[(box[a] + box[b]) & 0xff]));
        }
        return output;
    }

    /**
     *
     * the crypt method for spritz.
     *
     * @param input -> byte array with the cipher/plain text
     * @param output -> a byte array with the same size as the input array; thats the cipher or
     *            plain text
     * @return output
     */
    private byte[] crypt_spritz(byte[] input, byte[] output) {
        int i = 0;
        int j = 0;
        int k = 0;
        byte z = 0;

        // The pseudo-random generator algorithm
        for (int d = 0; d < input.length; d++) {
            i = (i + this.W) & 0xff;
            j = (k + (box[(j + box[i]) & 0xff])) & 0xff;
            k = (i + k + box[j]) & 0xff;

            // sw -> switch
            byte sw = box[i];
            box[i] = box[j];
            box[j] = sw;

            z = (byte) ((box[(j + box[(i + box[(z + k) & 0xff]) & 0xff]) & 0xff]) & 0xff);

            output[d] = (byte) (input[d] ^ z);
        }
        return output;
    }

    /**
     * the sbox.
     */
    private void generateSBox() {
        // initialize
        for (int i = 0; i < 256; i++) {
            box[i] = (byte) i;
        }
        int j = 0;
        // swap the nr with the key input
        for (int i = 0; i < 256; i++) {
            j = (j + box[i] + key[i % key.length]) & 0xff;
            byte sw = box[i];
            box[i] = box[j];
            box[j] = sw;
        }

    }

    /**
     * key is setz here, if it is too long it will get shortened, but that will normaly never.
     * happen. because the wizard doesn't allow such input
     *
     * @param key -> your key as a byte array
     */
    public void setKey(byte[] key) {
        // create s-box
        this.box = new byte[256];

        // look if key is to long if to long it will be 256 -> shortened
        int length_min = Math.min(256, key.length);
        byte[] cop = new byte[length_min];
        System.arraycopy(key, 0, cop, 0, length_min);
        this.key = cop;
        generateSBox();

    }

    /**
     * set if Arc4 or Spritz should be performed.
     */
    public void setAlgo(boolean algorithm) {
        this.algo = algorithm;
    }

    /**
     * set the free W
     * 
     * @param w
     */
    public void setW(int w) {
        this.W = w;
    }

}
