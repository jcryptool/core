// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.substitution.algorithm;

import java.util.Vector;

import org.jcryptool.core.operations.algorithm.classic.IClassicAlgorithmEngine;
import org.jcryptool.core.operations.alphabets.AlphaConverter;

public class SubstitutionEngine implements IClassicAlgorithmEngine {
    /**
     * Cipher-Alphabet represented by an int[].
     */
    private int[] codingAlphabet;

    /**
     * Encryption
     *
     * @param input data to be encrypted.
     * @param key array which the encryption uses.
     * @param alphaLength the length of the currentAlphabet.
     * @return the encrypted data as an int array.
     */
    public int[] doEncryption(int[] input, int[] key, int alphaLength, int[] alphabet,
            char nullchar, char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar,
            AlphaConverter alphaConv, char[] key2, int pastChars) {
        int[] ciphertext = new int[input.length];
        codingAlphabet = constructCipherAlph(removeDuplicateChars(key), alphabet);
        for (int i = 0; i < input.length; i++) {
            for (int k = 0; k < alphabet.length; k++) {
                if (alphabet[k] == input[i]) {
                    ciphertext[i] = codingAlphabet[k];
                    break;
                }

            }
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
    public int[] doDecryption(int[] input, int[] key, int alphaLength, int[] alphabet,
            char nullchar, char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar,
            AlphaConverter alphaConv, char[] key2, int pastChars) {
        int[] plaintext = new int[input.length];
        codingAlphabet = constructCipherAlph(removeDuplicateChars(key), alphabet);
        for (int i = 0; i < input.length; i++) {
            for (int k = 0; k < alphabet.length; k++) {
                if (codingAlphabet[k] == input[i]) {
                    plaintext[i] = alphabet[k];
                    break;
                }

            }
        }
        return plaintext;
    }

    /**
     * Construct cipher-currentAlphabet.
     *
     * @param key the keyword
     * @param plainAlph the plain currentAlphabet
     * @return currentAlphabet for en-/deciphering as int[]
     */
    private static int[] constructCipherAlph(int[] key, int[] plainAlph) {
        int[] cipherAlpha = new int[(plainAlph.length)];
        int count = 0;
        for (int i = 0; i < key.length; i++) {
            // kopieren des Key ins neue Array
            cipherAlpha[i] = key[i];
        }
        for (int i = 0; i < plainAlph.length; i++) {
            boolean found = false;
            for (int k = 0; k < key.length && !found; k++) {
                if (plainAlph[i] == key[k]) {
                    found = true;
                    count++;
                }
            } // end for
            if (!found) {
                cipherAlpha[i + key.length - count] = plainAlph[i];
            }
        } // end for
        return cipherAlpha;

    }

    /**
     * Removes duplicate charactters from the keyword.
     *
     * @param key The keyword
     * @return keyword w/o duplicate characters
     */
    public static int[] removeDuplicateChars(int[] key) {

        int[] newKey;
        Vector<Integer> tempKey = new Vector<Integer>();
        for (int k = 0; k < key.length; k++) { // For each letter in the key
            boolean found = false;
            for (int i = 0; i < tempKey.size(); i++) {
                if (key[k] == ((Integer) tempKey.get(i)).intValue()) {
                    found = true;
                }
            }
            if (!found) { // If it's not already in newKey
                tempKey.add(Integer.valueOf(key[k]));
            }
        } // Append it to newKey
        newKey = new int[tempKey.size()];
        for (int i = 0; i < tempKey.size(); i++) {
            newKey[i] = tempKey.get(i).intValue();
        }
        return newKey;
    } // end removeDuplicateChars()
}
