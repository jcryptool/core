// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.xor.algorithm;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.IClassicAlgorithmEngine;
import org.jcryptool.core.operations.alphabets.AlphaConverter;
import org.jcryptool.crypto.classic.xor.XorPlugin;

public class XorEngine implements IClassicAlgorithmEngine {
    /**
     * Encryption
     *
     * @param input data to be encrypted.
     * @param key array which the encryption uses.
     * @param alphaLength the length of the currentAlphabet.
     * @return the encrypted data as an int array.
     */
    public int[] doEncryption(int[] input, int[] key, int alphaLength, int[] alphabet, char nullchar,
            char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2,
            int pastChars) {
        boolean alphaLengthIsPowerOfTwo = false;
        if (key2.length > 3) {
            String s = readInput(new String(key2));
            char[] keyFromFile = s.toCharArray();
            try {
                key = alphaConv.charArrayToIntArray(alphaConv.filterNonAlphaChars(keyFromFile));
            } catch (Exception e) {
                LogUtil.logError(XorPlugin.PLUGIN_ID, "Exception while converting an currentAlphabet", e, true);
            }
            if (key.length < input.length) {
                LogUtil.logInfo("Key is too short for Vernam cipher, using common XOR instead"); //$NON-NLS-1$
            }
        }
        int y = -1;
        for (int i = 0; y <= alphaLength; i++) {
            y = (int) (java.lang.Math.pow(2, i));
            if (alphaLength == y) {
                alphaLengthIsPowerOfTwo = true;
            }
        }
        if (!alphaLengthIsPowerOfTwo) {
            LogUtil.logInfo("Alphabet length must be a power of 2"); //$NON-NLS-1$
            return new int[0];
            // TODO throw Exception
        }
        int[] ciphertext = new int[input.length];

        for (int i = 0; i < input.length; i++) {
            ciphertext[i] = input[i] ^ key[i % key.length];
        }
        return ciphertext;
    }

    /**
     * Decryption
     *
     * @param input data to be decrypted.
     * @param key array which the decryption uses.
     * @param alphaLength the length of the currentAlphabet.
     * @return the decrypted data as an int array.
     */
    public int[] doDecryption(int[] input, int[] key, int alphaLength, int[] alphabet, char nullchar,
            char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2,
            int pastChars) {
        if (key2.length > 3) {
            String s = readInput(new String(key2));
            char[] keyFromFile = s.toCharArray();
            try {
                key = alphaConv.charArrayToIntArray(alphaConv.filterNonAlphaChars(keyFromFile));
            } catch (Exception e) {
                LogUtil.logError(XorPlugin.PLUGIN_ID, "Exception while converting an currentAlphabet", e, true);
            }
            if (key.length < input.length) {
                LogUtil.logInfo("Key is too short for Vernam cipher, using common XOR instead"); //$NON-NLS-1$
            }
        }
        boolean alphaLengthIsPowerOfTwo = false;

        int y = -1;
        for (int i = 0; y <= alphaLength; i++) {
            y = (int) (java.lang.Math.pow(2, i));
            if (alphaLength == y) {
                alphaLengthIsPowerOfTwo = true;
            }
        }
        if (!alphaLengthIsPowerOfTwo) {
            LogUtil.logInfo("Alphabet length must be a power of 2"); //$NON-NLS-1$
            return new int[0];
            // TODO throw Exception
        }
        int[] plaintext = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            plaintext[i] = input[i] ^ key[i % key.length];
        }

        return plaintext;
    }

    private String readInput(String path) {
        FileTransferManager ftm = new FileTransferManager(path);
        char[] key = null;
        StringBuilder keyString = new StringBuilder();
        while (ftm.getEof() != -1) {
            key = ftm.readInput();
            keyString.append(String.valueOf(key));
        }
        ftm.closeInputStream();
        return keyString.toString();
    }
}
