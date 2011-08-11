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
package org.jcryptool.crypto.classic.adfgvx.algorithm;

import java.util.Vector;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.IClassicAlgorithmEngine;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphaConverter;
import org.jcryptool.crypto.classic.adfgvx.AdfgvxPlugin;

public class AdfgvxEngine implements IClassicAlgorithmEngine {
    /**
     * buffer for key value
     */
    private int[] key;

    /**
     * characters of the key
     */
    private char[] keyValue;

    /**
     * buffer for transposition key value
     */
    private int[] transpKey;

    /**
     * characters of the key
     */
    private char[] transpKeyChar;

    /**
     * Encryption
     *
     * @param input data to be encrypted.
     * @param key array which the encryption uses.
     * @param alphaLength the length of the currentAlphabet.
     * @return the encrypted data as an int array.
     */
    public int[] doEncryption(int[] input, int[] key, int alphaLength, int[] alphabet, char nullChar,
            char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2, int pastChars) {
        transpKeyChar = key2;
        keyValue = keyChars;
        try {
            generateKeys(alphaConv);
        } catch (Exception e) {
            LogUtil.logError(AdfgvxPlugin.PLUGIN_ID, "Exception while generating keys", e, true);
        }

        int[] codingAlphabet = constructCipherAlph(removeDuplicateChars(key), alphabet);
        char[] ciphertextAfterSubst = new char[input.length * 2];
        int y = 0;
        for (int i = 0; i < input.length && y < ciphertextAfterSubst.length + 1; i++) {

            for (int k = 0; k < codingAlphabet.length; k++) {
                if (codingAlphabet[k] == input[i]) {
                    int line = k / 6;// oder 5
                    int column = k % 6;
                    String pair = ""; //$NON-NLS-1$
                    switch (line) {
                        case 0:
                            pair = "A"; //$NON-NLS-1$
                            break;
                        case 1:
                            pair = "D"; //$NON-NLS-1$
                            break;
                        case 2:
                            pair = "F"; //$NON-NLS-1$
                            break;
                        case 3:
                            pair = "G"; //$NON-NLS-1$
                            break;
                        case 4:
                            pair = "V"; //$NON-NLS-1$
                            break;
                        case 5:
                            pair = "X"; //$NON-NLS-1$
                            break;
                    }
                    switch (column) {
                        case 0:
                            pair = pair.concat("A"); //$NON-NLS-1$
                            break;
                        case 1:
                            pair = pair.concat("D"); //$NON-NLS-1$
                            break;
                        case 2:
                            pair = pair.concat("F"); //$NON-NLS-1$
                            break;
                        case 3:
                            pair = pair.concat("G"); //$NON-NLS-1$
                            break;
                        case 4:
                            pair = pair.concat("V"); //$NON-NLS-1$
                            break;
                        case 5:
                            pair = pair.concat("X"); //$NON-NLS-1$
                            break;
                    }
                    char[] temp = pair.toCharArray();
                    ciphertextAfterSubst[y] = temp[0];
                    y++;
                    ciphertextAfterSubst[y] = temp[1];
                    y++;
                }

            }// end inner for
        }// end outer for
        // Jetzt muss die Transposition erfolgen
        int[] order = getOrder(alphaConv.charArrayToIntArray(transpKeyChar));
        int count = 0;
        char[] ciphertext = new char[ciphertextAfterSubst.length];
        for (int i = 0; i < order.length; i++) {
            int ord = order[i];
            for (int k = ord; k < ciphertextAfterSubst.length; k = k + order.length) {
                ciphertext[count] = ciphertextAfterSubst[k];
                count++;
            }
        }
        return alphaConv.charArrayToIntArray(ciphertext);

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
            char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2, int pastChars) {
        transpKeyChar = key2;
        keyValue = keyChars;
        try {
            generateKeys(alphaConv);
        } catch (Exception e) {
            LogUtil.logError(AdfgvxPlugin.PLUGIN_ID, "Exception while generating keys", e, true);
        }
        int[] codingAlphabet = constructCipherAlph(removeDuplicateChars(key), alphabet);
        int[] plain = new int[inputNoNonAlphaChar.length / 2];
        int[] order = getOrder(transpKey);
        int count = 0;
        char[] plainTextAfterTransp = new char[inputNoNonAlphaChar.length];
        for (int i = 0; i < order.length; i++) {
            int ord = order[i];
            for (int k = ord; k < inputNoNonAlphaChar.length; k = k + order.length) {
                plainTextAfterTransp[k] = inputNoNonAlphaChar[count];
                count++;
            }
        }

        int y = 0;
        for (int i = 0; i < plainTextAfterTransp.length; i++) {
            char[] pair = new char[2];
            int line = 0;
            int column = 0;
            pair[0] = plainTextAfterTransp[i];
            i++;
            pair[1] = plainTextAfterTransp[i];

            switch (pair[0]) {
                case 'A':
                    line = 0;
                    break;
                case 'D':
                    line = 1;
                    break;
                case 'F':
                    line = 2;
                    break;
                case 'G':
                    line = 3;
                    break;
                case 'V':
                    line = 4;
                    break;
                case 'X':
                    line = 5;
                    break;
            }
            switch (pair[1]) {
                case 'A':
                    column = 0;
                    break;
                case 'D':
                    column = 1;
                    break;
                case 'F':
                    column = 2;
                    break;
                case 'G':
                    column = 3;
                    break;
                case 'V':
                    column = 4;
                    break;
                case 'X':
                    column = 5;
                    break;
            }
            int ch = line * 6 + column;
            plain[y] = codingAlphabet[ch];
            y++;

        }// end for
        // plainText = alphaConv.intArrayToCharArray(plain);
        return plain;

    }

    /**
     * Removes duplicate charactters from the keyword.
     *
     * @param key The keyword
     * @return keyword w/o duplicate characters
     */
    public static int[] removeDuplicateChars(int[] key) { // REMOVES DUPLICATE
        // CHARS FROM THE
        // KEYWORD
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
            newKey[i] = ((Integer) (tempKey.get(i))).intValue();
        }
        return newKey;
    } // end removeDuplicateChars()

    /**
     * Construct cipher-currentAlphabet.
     *
     * @param key the keyword
     * @param plainAlph the plain currentAlphabet
     * @return currentAlphabet for en-/deciphering as int[]
     */
    public static int[] constructCipherAlph(int[] key, int[] plainAlph) {
        int[] cipherAlph = new int[plainAlph.length];
        int[] newKey = removeDuplicateChars(key);
        int keyLength = newKey.length;
        for (int i = 0; i < newKey.length; i++)
            // kopieren des Key ins neue Array
            cipherAlph[i] = newKey[i];
        for (int i = 0; i < plainAlph.length; i++) {
            boolean found = false;
            for (int k = 0; k < newKey.length && !found; k++) {
                if (plainAlph[i] == newKey[k]) {
                    found = true;
                    keyLength--;
                }
            }
            if (!found)
                cipherAlph[keyLength + i] = plainAlph[i];
        }
        return cipherAlph;
    }

    /**
     * Generates and initializes the private data
     *
     * @param key2 The key to be used for the permution.
     * @return an int-array which contains the read-order of the columns e.g.
     *         [0,3,1,2] would mean first read column 0, then column 3, 1, 2
     */
    public int[] getOrder(int[] key2) {
        int[] m_P = new int[key2.length];
        int[] m_IP = new int[key2.length];

        for (int idx1 = 0; idx1 < key2.length; idx1++) {
            m_P[idx1] = 0;
            for (int idx2 = 0; idx2 < key2.length; idx2++) {
                if (idx1 != idx2) {
                    if (key2[idx1] > key2[idx2]) {
                        m_P[idx1]++;
                    } else if ((key2[idx1] == key2[idx2]) & (idx2 < idx1)) {
                        m_P[idx1]++;
                    }
                }
            }
        }
        for (int idx = 0; idx < key2.length; idx++) {
            m_IP[m_P[idx]] = idx;
        }
        return m_IP;
    }

    public char[] getCipherAlphabet(char[] keyString, AbstractAlphabet a) {
        char[] set = a.getCharacterSet();
        AlphaConverter alphaConverter = null;
        try {
            alphaConverter = new AlphaConverter(set);
        } catch (Exception e) {
            LogUtil.logError(AdfgvxPlugin.PLUGIN_ID, "Exception while setting up an AlphaConverter", e, false); //$NON-NLS-1$
        }
        keyValue = keyString;
        transpKeyChar = "EMPTY".toCharArray(); //$NON-NLS-1$
        try {
            for (int i = 0; i < keyValue.length; i++) {
                if (Character.isLetter(keyValue[i]))
                    keyValue[i] = Character.toUpperCase(keyValue[i]);
            }
            for (int i = 0; i < transpKeyChar.length; i++) {
                if (Character.isLetter(transpKeyChar[i]))
                    transpKeyChar[i] = Character.toUpperCase(transpKeyChar[i]);
            }
            key = (alphaConverter.charArrayToIntArray(alphaConverter.filterNonAlphaChars(keyValue)));
            transpKey = (alphaConverter.charArrayToIntArray(alphaConverter.filterNonAlphaChars(transpKeyChar)));
        } catch (Exception e) {
            LogUtil.logError(AdfgvxPlugin.PLUGIN_ID, "Exception while filtering an currentAlphabet", e, false); //$NON-NLS-1$
        }
        int[] alphaTemp = null;
        try {
            alphaTemp = alphaConverter.charArrayToIntArray(set);
        } catch (Exception e) {
            LogUtil.logError(AdfgvxPlugin.PLUGIN_ID, "Exception while converting an currentAlphabet", e, false); //$NON-NLS-1$
        }
        int[] codingAlphabet = null;
        if (!(alphaTemp == null)) {
            codingAlphabet = constructCipherAlph(removeDuplicateChars(key), alphaTemp);
        }
        return alphaConverter.intArrayToCharArray(codingAlphabet);
    }

    /**
     * Generates the operation's key.
     *
     * @throws Exception
     */
    private void generateKeys(AlphaConverter alphaConverter) throws Exception {
        for (int i = 0; i < keyValue.length; i++) {
            if (Character.isLetter(keyValue[i]))
                keyValue[i] = Character.toUpperCase(keyValue[i]);
        }
        for (int i = 0; i < transpKeyChar.length; i++) {
            if (Character.isLetter(transpKeyChar[i]))
                transpKeyChar[i] = Character.toUpperCase(transpKeyChar[i]);
        }
        key = (alphaConverter.charArrayToIntArray(alphaConverter.filterNonAlphaChars(keyValue)));
        transpKey = (alphaConverter.charArrayToIntArray((transpKeyChar)));

    }
}
