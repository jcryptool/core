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
package org.jcryptool.crypto.classic.playfair.algorithm;

import java.util.Vector;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.IClassicAlgorithmEngine;
import org.jcryptool.core.operations.alphabets.AlphaConverter;
import org.jcryptool.crypto.classic.playfair.PlayfairPlugin;

public class PlayfairEngine implements IClassicAlgorithmEngine {
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

        if (alphaLength != 25) {
            // throw new AlgoException("Wrong currentAlphabet specified."
            // +" Alphabet must exactly have 25 characters");
            LogUtil.logInfo("Wrong currentAlphabet specified. Alphabet must exactly have 25 characters");
            return null;
        }
        for (int i = 0; i < keyChars.length; i++) {
            if (Character.isLetter(keyChars[i])) {
                keyChars[i] = Character.toUpperCase(keyChars[i]);
            }
        }

        int pairs = 0;
        for (int i = 0; i < input.length; i++) {
            if (Character.isLetter(input[i]))
                input[i] = Character.toUpperCase(input[i]);
            if (input[i] == 'J')
                input[i] = 'I';
            if (input[i] == '\u00c4')
                input[i] = 'A';
            if (input[i] == '\u00d6')
                input[i] = 'O';
            if (input[i] == '\u00dc')
                input[i] = 'U';

        }
        for (int i = 0; i < inputNoNonAlphaChar.length; i++) {
            if (i < inputNoNonAlphaChar.length - 1) {
                if (inputNoNonAlphaChar[i] == inputNoNonAlphaChar[i + 1]) {
                    pairs++;
                }
            }
        }

        char[] inputEven;
        // check if input length is even, if not append null character
        if ((inputNoNonAlphaChar.length + pairs) % 2 != 0) {
            inputEven = new char[inputNoNonAlphaChar.length + 1 + pairs];
            int counter = 0;
            for (int i = 0; i < inputNoNonAlphaChar.length; i++) {
                if (i < inputNoNonAlphaChar.length - 1) {
                    if (inputNoNonAlphaChar[i] == inputNoNonAlphaChar[i + 1]) {
                        inputEven[counter] = inputNoNonAlphaChar[i];
                        counter++;
                        inputEven[counter] = nullChar;
                        counter++;
                    } else {
                        inputEven[counter] = inputNoNonAlphaChar[i];
                        counter++;
                    }
                } else {
                    inputEven[counter] = inputNoNonAlphaChar[i];
                    counter++;
                }
            }
            inputEven[inputEven.length - 1] = nullChar;
        } else {
            inputEven = new char[inputNoNonAlphaChar.length + pairs];
            int counter = 0;
            for (int i = 0; i < inputNoNonAlphaChar.length; i++) {
                if (i < inputNoNonAlphaChar.length - 1) {
                    if (inputNoNonAlphaChar[i] == inputNoNonAlphaChar[i + 1]) {
                        inputEven[counter] = inputNoNonAlphaChar[i];
                        counter++;
                        inputEven[counter] = nullChar;
                        counter++;
                    } else {
                        inputEven[counter] = inputNoNonAlphaChar[i];
                        counter++;
                    }
                } else {
                    inputEven[counter] = inputNoNonAlphaChar[i];
                    counter++;
                }
            }

        }

        int[] codingAlphabet = null;
        try {
            codingAlphabet = constructCipherAlph(removeDuplicateChars(key), alphabet);
        } catch (Exception e) {
            LogUtil.logError(PlayfairPlugin.PLUGIN_ID, "Exception while constructing a cipher currentAlphabet", e, true);
        }
        int[] inputInt = null;
        try {
            inputInt = alphaConv.charArrayToIntArray(inputEven);
        } catch (Exception e) {
            LogUtil.logError(PlayfairPlugin.PLUGIN_ID, "Exception while filtering an currentAlphabet", e, true);
        }
        int[] output = new int[inputInt.length];
        for (int i = 0; i < inputInt.length - 1; i++) {
            int ch1 = inputInt[i];
            i++;
            int ch2 = inputInt[i];
            int indexCh1 = getIndex(ch1, codingAlphabet); // index holen
            int indexCh2 = getIndex(ch2, codingAlphabet); // index holen
            int rowCh1 = -1;
            int rowCh2 = -2;
            int colCh1 = -3;
            int colCh2 = -4;
            int newCh1;
            int newCh2;
            // durchsuchen des Geheimtextalph. Zeilen und Spalten der zu
            // verschlï¿½sselnden Char finden
            for (int k = 0; k < codingAlphabet.length; k++) {
                if (codingAlphabet[k] == ch1) {
                    rowCh1 = k / 5;
                    colCh1 = k % 5;
                }
                if (codingAlphabet[k] == ch2) {
                    rowCh2 = k / 5;
                    colCh2 = k % 5;
                }
                // LogUtil.logInfo("Durchlauf " + k + " ch1 " + ch1
                // + " ch2 " + ch2 + " codealph "
                // + codingAlphabet[k]);
            }
            if (rowCh1 < 0 || rowCh2 < 0 || colCh1 < 0 || colCh2 < 0) {
                // throw new AlgoException("Row or column <0");
                LogUtil.logInfo("Row or column < 0");
                break;
            }
            if (rowCh1 == rowCh2) {
                newCh1 = getRightNeighbour(indexCh1, codingAlphabet);
                newCh2 = getRightNeighbour(indexCh2, codingAlphabet);
            } else if (colCh1 == colCh2) {
                newCh1 = getLowerNeighbour(indexCh1, codingAlphabet);
                newCh2 = getLowerNeighbour(indexCh2, codingAlphabet);
            } else {
                newCh1 = getSubstitute(rowCh1, colCh2, codingAlphabet);
                newCh2 = getSubstitute(rowCh2, colCh1, codingAlphabet);
            }
            output[i - 1] = newCh1;
            output[i] = newCh2;
        }

        // if (filter) {
        // finalOutput = mergeToFinalOutput(o.getCypherChar(),
        // alphaConverter.intArrayToCharArray(output));
        // o.setCypherString(new String(finalOutput));
        // } else
        return output;
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

        if (alphaLength != 25) {
            // throw new AlgoException("Wrong currentAlphabet specified."
            // +" Alphabet must exactly have 25 characters");
            LogUtil.logInfo("Wrong currentAlphabet specified." + " Alphabet must exactly have 25 characters");
            return null;
        }
        for (int i = 0; i < keyChars.length; i++) {
            if (Character.isLetter(keyChars[i])) {
                keyChars[i] = Character.toUpperCase(keyChars[i]);
            }
        }

        int[] codingAlphabet = null;
        try {
            codingAlphabet = constructCipherAlph(removeDuplicateChars(key), alphabet);
        } catch (Exception e) {
            LogUtil.logError(PlayfairPlugin.PLUGIN_ID, "Exception while constructing a cipher currentAlphabet", e, true);
        }
        int[] output = new int[input.length];
        for (int i = 0; i < input.length - 1; i++) {
            int ch1 = input[i];
            i++;
            int ch2 = input[i];
            int indexCh1 = getIndex(ch1, codingAlphabet); // index holen
            int indexCh2 = getIndex(ch2, codingAlphabet); // index holen
            int rowCh1 = -1;
            int rowCh2 = -2;
            int colCh1 = -3;
            int colCh2 = -4;
            int newCh1;
            int newCh2;
            // durchsuchen des Geheimtextalph. Zeilen und Spalten der zu
            // verschlï¿½sselnden Char finden
            for (int k = 0; k < codingAlphabet.length; k++) {
                if (codingAlphabet[k] == ch1) {
                    rowCh1 = k / 5;
                    colCh1 = k % 5;
                }
                if (codingAlphabet[k] == ch2) {
                    rowCh2 = k / 5;
                    colCh2 = k % 5;
                }
                // LogUtil.logInfo("Durchlauf " + k + " ch1 " + ch1
                // + " ch2 " + ch2 + " codealph "
                // + codingAlphabet[k]);
            }
            if (rowCh1 < 0 || rowCh2 < 0 || colCh1 < 0 || colCh2 < 0) {
                // throw new AlgoException("Row or column <0");
                LogUtil.logInfo("Row or column < 0");
                break;
            }
            if (rowCh1 == rowCh2) {
                newCh1 = getLeftNeighbour(indexCh1, codingAlphabet);
                newCh2 = getLeftNeighbour(indexCh2, codingAlphabet);
            } else if (colCh1 == colCh2) {
                newCh1 = getUpperNeighbour(indexCh1, codingAlphabet);
                newCh2 = getUpperNeighbour(indexCh2, codingAlphabet);
            } else {
                newCh1 = getSubstitute(rowCh1, colCh2, codingAlphabet);
                newCh2 = getSubstitute(rowCh2, colCh1, codingAlphabet);
            }
            output[i - 1] = newCh1;
            output[i] = newCh2;
        }

        return output;
    }

    /**
     * @param index index of char
     * @param cipherAlph int array cipher currentAlphabet
     * @return cipher char as int
     */
    private int getRightNeighbour(int index, int[] cipherAlph) {
        if (index % 5 < 4) {// wenn <4 dann einfach "nï¿½chsthï¿½heren"
                            // Buchstaben
            // holen, PROBLEM: Array lï¿½uft ab 0, mï¿½gliche
            // Fehlerquelle
            // LogUtil.logInfo("index " + index);
            return cipherAlph[index + 1];
        } else {
            if (index % 5 == 4) {
                return cipherAlph[index - 4];
            }
        }
        return -1; // should never happen
    }

    /**
     * @param index index of a character
     * @param cipherAlph int array cipher currentAlphabet
     * @return cipher char as int
     */
    private int getLowerNeighbour(int index, int[] cipherAlph) {
        if (index + 5 < cipherAlph.length) {
            return cipherAlph[index + 5];
        } else {
            return cipherAlph[(index + 5) % cipherAlph.length];
        }
    }

    /**
     * @param index index of a character
     * @param cipherAlph int array cipher currentAlphabet
     * @return cipher char as int
     */
    private int getUpperNeighbour(int index, int[] cipherAlph) {
        if (index < 5) {
            return cipherAlph[cipherAlph.length - (5 - index)];
        } else {
            return cipherAlph[index - 5];
        }
    }

    /**
     * @param index index of a character
     * @param cipherAlph int array cipher currentAlphabet
     * @return cipher char as int
     */
    private int getLeftNeighbour(int index, int[] cipherAlph) {
        if (index % 5 > 0) {
            return cipherAlph[index - 1];
        } else {
            return cipherAlph[index + 4];
        }
    }

    /**
     * @param ch "value" of an character
     * @param cipherAlph int array cipher currentAlphabet
     * @return array index of the character ch
     */
    private int getIndex(int ch, int[] cipherAlph) {
        for (int i = 0; i < cipherAlph.length; i++) {
            if (ch == cipherAlph[i]) {
                return i;
            }
        }
        return -1; // should never happen
    }

    /**
     * @param row row of character
     * @param col column of character
     * @param cipherAlph int array cipher currentAlphabet
     * @return array index of substitute character
     */
    private int getSubstitute(int row, int col, int[] cipherAlph) {
        return cipherAlph[5 * (row) + col];
    }

    /**
     * Removes duplicate characters from the keyword.
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
        for (int i = 0; i < newKey.length; i++) {
            // kopieren des Key ins neue Array
            cipherAlph[i] = newKey[i];
        }
        for (int i = 0; i < plainAlph.length; i++) {
            boolean found = false;
            for (int k = 0; k < newKey.length && !found; k++) {
                if (plainAlph[i] == newKey[k]) {
                    found = true;
                    keyLength--;
                }
            }
            if (!found) {
                cipherAlph[keyLength + i] = plainAlph[i];
            }
        }
        return cipherAlph;
    }

}
