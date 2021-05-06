// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.alphabets;

import java.nio.CharBuffer;
import java.util.HashMap;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;

/**
 * The AlphaConverter class provides the function to convert char arrays to int arrays and the way
 * back, by using a specified alphabet. For indexing the input values the class uses a hash map.
 * 
 * @author amro
 * @version 0.1
 */
public class AlphaConverter {
    /**
     * hashtable for alphabet
     */
    private HashMap<Character, Integer> alphaMap;

    /**
     * buffer for alphabet
     */
    private char[] alpha;

    /**
     * Builds the alphabet by analyzing the alpha entry and after that the alpha-chars are mapped.
     * 
     * @param alphaEntry the passed alphabet entry.
     */
    public AlphaConverter(char[] alphaEntry) {

        try {
            // Trim the alphaEntry
            alpha = alphaEntry;

            // Build alpha map and assign to HashMap field
            this.alphaMap = buildMap(alpha);
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while building the hashmap for an alphabet", e, //$NON-NLS-1$
                    false);
        }

    }

    /**
     * Converts a char array to an int array by using the specified alphabet.
     * 
     * @param input the chars to be converted.
     * @return the converted indexes of the char array.
     */
    public int[] charArrayToIntArray(char[] input) {
        int[] output = new int[input.length];

        try {

            for (int i = 0; i < input.length; i++) {
                output[i] = getAlphaIndex(input[i]);
            }
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while converting an alphabet", e, false); //$NON-NLS-1$
        }

        return output;
    }

    /**
     * Reconvert the indexes to chars by using the specified alphabet (also see
     * charArrayToIntArray(char []).
     * 
     * @param input the int values to be converted back to the appropriate chars.
     * @return the reconverted
     */
    public char[] intArrayToCharArray(int[] input) {

        char[] output = new char[input.length];

        for (int i = 0; i < output.length; i++) {
            output[i] = alpha[input[i]];
        }

        return output;
    }

    /**
     * Returns the position of value in the specified alphabet.
     * 
     * @param value the char to search for in alphabet.
     * @return the index of the value in the specified alphabet.
     * @throws Exception if the alphabet does not contain the value parameter
     */
    public int getAlphaIndex(char value) throws Exception {
        // Get key value
        Integer indexObject = alphaMap.get(value);

        // If the alphabet does not contain the letter.
        if (indexObject == null)
            throw new Exception("Alphabet does not contain the letter " + String.valueOf(value) //$NON-NLS-1$
                    + ". The int value of the char value is " + ((int) value) + "."); //$NON-NLS-1$ //$NON-NLS-2$

        // Pass to (int) index by using wrapper class of int and increment.
        return indexObject.intValue();
    }

    /**
     * Retrieval-method for getting the alphabet.
     * 
     * @return the mapped alphabet.
     */
    public char[] getAlpha() {
        return alpha;
    }

    /**
     * Retrieval-method for getting the alphabet's length.
     * 
     * @return the length of the mapped alphabet
     */
    public int getAlphaLength() {
        return alpha.length;
    }

    /**
     * Returns true if the alphabet contains the specified key.
     * 
     * @param letter the specified key
     * @return the appropriate value
     */
    public boolean containsLetter(char letter) {
        return alphaMap.containsKey(letter);
    }

    /**
     * This method filters the non alpha chars. It is used to get the the cipher input for classic
     * algorithms out of the plain text.
     * 
     * @param plain the plain text
     * @return the filtered plain text (cipher input)
     * @throws Exception an exception is thrown if the final buffer has no array.
     */
    public char[] filterNonAlphaChars(char[] plain) throws Exception {

        CharBuffer tempBuf = CharBuffer.allocate(plain.length);

        for (int i = 0; i < plain.length; i++) {
            if (alphaMap.containsKey(plain[i])) {
                tempBuf.append(plain[i]);
            }
        }

        // number of alpha letters
        int len = plain.length - tempBuf.remaining();

        CharBuffer finalBuf = CharBuffer.allocate(len);

        finalBuf.put(tempBuf.array(), 0, len);

        if (finalBuf.hasArray()) {
            return finalBuf.array();
        } else {
            throw new Exception("Final Buffer has no array."); //$NON-NLS-1$
        }

    }

    /**
     * Builds the hashtable of the specified alpha.
     * 
     * @param alpha the specified alphabet.
     * @return the hashtable of the specified alpha.
     * @throws Exception if a char is passed more than once in the alphabet option's parameter
     */
    private HashMap<Character, Integer> buildMap(char[] alpha) throws Exception {

        HashMap<Character, Integer> hashMap = new HashMap<Character, Integer>(alpha.length, 1);

        for (int i = 0; i < alpha.length; i++) {
            // Check if alpha does already contain the letter
            if (hashMap.containsKey(alpha[i])) {
                throw new Exception("Check your entries: The alphabet does already contain the letter " + alpha[i] //$NON-NLS-1$
                        + ". \nUnicode of the letter is " + ((int) alpha[i]) + "." //$NON-NLS-1$ //$NON-NLS-2$
                        + "\nThe position where the char occurs twice is " + (i + 1) + "."); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                hashMap.put(alpha[i], i);
            }
        }

        return hashMap;
    }

}
