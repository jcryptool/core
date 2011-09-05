// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.transposition.algorithm;

import org.jcryptool.core.operations.algorithm.classic.IClassicAlgorithmEngine;
import org.jcryptool.core.operations.alphabets.AlphaConverter;

/**
 * The CaesarEngine class represents the engine of the Caesar algorithm class.
 *
 * @see org.jcryptool.TranspositionAlgorithm.caesar.algorithm.CaesarAlgorithm It provides
 *      Caesar-based encryption and decryption on integer basis.
 *
 * @author simlei
 * @version 0.1
 */
public class TranspositionEngine implements IClassicAlgorithmEngine {
    public static String implode(String[] ary, String delim) {
        StringBuffer out = new StringBuffer(""); //$NON-NLS-1$
        for (int i = 0; i < ary.length; i++) {
            if (i != 0) {
                out.append(delim);
            }
            out.append(ary[i]);
        }
        return out.toString();
    }

    /**
     * Decryption
     *
     * @param input data to be decrypted.
     * @param key which the decryption uses.
     * @param alphaLength the length of the currentAlphabet.
     * @return the decrypted data as an int array.
     */
    public int[] doDecryption(int[] input, int[] key, int alphaLength, int[] alphabet,
            char nullchar, char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar,
            AlphaConverter alphaConv, char[] key2, int pastChars) {

        StringBuilder key1String = new StringBuilder();
        StringBuilder key2String = new StringBuilder();
        TranspositionKey tKey1, tKey2;

        boolean firstReadInOrder = getReadOrderFromChar(alphaChars[key[0]], alphaChars);
        boolean firstReadOutOrder = getReadOrderFromChar(alphaChars[key[1]], alphaChars);
        //Reading the first key (first two chars are order instructions)
        for (int i = 2; i < key.length; i++)
            key1String.append(alphaChars[key[i]]);
        tKey1 = new TranspositionKey(key1String.toString(), alphaChars).getReverseKey();


        boolean secondReadInOrder = getReadOrderFromChar(alphaChars[key[0]], alphaChars);
        boolean secondReadOutOrder = getReadOrderFromChar(alphaChars[key[1]], alphaChars);
        //Reading the second key (first two chars are order instructions)
        for(int i = 2; i < key2.length; i++)
            key2String.append(String.valueOf((key2[i])));
        tKey2 = new TranspositionKey(key2String.toString(), alphaChars).getReverseKey();

        char[] text = new char[input.length];
        for (int i = 0; i < input.length; i++) text[i] = alphaChars[input[i]];

        //CRYPTOGRAPHIC OPERATION FINALLY

        //predict padding by default at decryption
        if(tKey2.getLength() > 0) {
        	text = transpose(text, tKey2, secondReadOutOrder, secondReadInOrder, true);
        }
        if(tKey1.getLength() > 0) {
        	text = transpose(text, tKey1, firstReadOutOrder, firstReadInOrder, true);
        }

        // Putting it back together
        int[] result = toInternalResultRepresentation(text, alphaChars);

        return result;
    }

//    public char[] createTranspositionTableFromText(String text)

    /**
     * Does exactly the same as {@link #transpose(char[], TranspositionKey, boolean, boolean, boolean, TranspositionKey)},
     * but the key for padding prediction is already automatically guessed as the inverse of the given key, which makes sense in decryption cases.
     *
     * @see #transpose(char[], TranspositionKey, boolean, boolean, boolean, TranspositionKey)
     */
    public char[] transpose(char[] text, TranspositionKey key, boolean readInOrder, boolean readOutOrder, boolean predictPadding) {
    	return transpose(text, key, readInOrder, readOutOrder, predictPadding, key.getReverseKey());
    }

    /**
     * transposes a text, given in/out reading order, and whether to prepare the reading by foresaying were
     * the blanks (padding) were (should be done, when it is known, that a padding was omissed, which is the
     * case when decrypting, because after encryption, maybe a padding was omissed.)
     *
     * @param text the text as char-array
     * @param key the key for transformation
     * @param readInOrder the reading in order (true: column-wise; false:row-wise)
     * @param readOutOrder the reading out order (true: column-wise; false:row-wise)s
     * @param predictPadding see above
     * @param keyForPaddingPrediction the key that has to be used for padding prediction (in the case decryption of a cipher that has omissed padding, its the reverse key of the decryption key, thus, the encryption key)
     * @return the transposed text as char array
     */
    public char[] transpose(char[] text, TranspositionKey key, boolean readInOrder, boolean readOutOrder, boolean predictPadding, TranspositionKey keyForPaddingPrediction) {
    	TranspositionTable table = new TranspositionTable(key.getLength());
    	if(predictPadding) {
    		table.fillCharsIntoTable(text, readInOrder, keyForPaddingPrediction);
    	} else {
    		table.fillCharsIntoTable(text, readInOrder);
    	}

    	table.transposeColumns(key);

    	return table.readOutContent(readOutOrder);
    }

    /**
     * Reads the encoded reading order from a character and an currentAlphabet.
     *
     * @param character the character where the order is encoded
     * @param alphaChars the currentAlphabet for this encoding
     * @return the read order
     */
    public boolean getReadOrderFromChar(char character, char[] alphaChars) {
    	if(character == alphaChars[0]) return true;
    	return false;
    }

    /**
     * Encryption
     *
     * @param input data to be encrypted.
     * @param key which the encryption uses.
     * @param alphaLength the length of the currentAlphabet.
     * @return the encrypted data as an int array.
     */
    public int[] doEncryption(int[] input, int[] key, int alphaLength, int[] alphabet,
            char nullchar, char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar,
            AlphaConverter alphaConv, char[] key2, int pastChars) {

        StringBuilder key1String = new StringBuilder();
        StringBuilder key2String = new StringBuilder();
        TranspositionKey tKey1, tKey2;

        boolean firstReadInOrder = getReadOrderFromChar(alphaChars[key[0]], alphaChars);
        boolean firstReadOutOrder = getReadOrderFromChar(alphaChars[key[1]], alphaChars);
        //Reading the first key (first two chars are order instructions)
        for (int i = 2; i < key.length; i++)
            key1String.append(alphaChars[key[i]]);
        tKey1 = new TranspositionKey(key1String.toString(), alphaChars);

        boolean secondReadInOrder = getReadOrderFromChar(alphaChars[key[0]], alphaChars);
        boolean secondReadOutOrder = getReadOrderFromChar(alphaChars[key[1]], alphaChars);
        //Reading the second key (first two chars are order instructions)
        for(int i = 2; i < key2.length; i++)
            key2String.append(String.valueOf((key2[i])));
        tKey2 = new TranspositionKey(key2String.toString(), alphaChars);

        char[] text = new char[input.length];
        for (int i = 0; i < input.length; i++) text[i] = alphaChars[input[i]];

        //CRYPTOGRAPHIC OPERATION FINALLY

        //dont predict padding by default at encryption
        if(tKey1.getLength() > 0) {
        	text = transpose(text, tKey1, firstReadInOrder, firstReadOutOrder, false);
        }
        if(tKey2.getLength() > 0) {
        	text = transpose(text, tKey2, secondReadInOrder, secondReadOutOrder, false);
        }

        // Putting it back together
        int[] result = toInternalResultRepresentation(text, alphaChars);

        return result;
    }

	private int[] toInternalResultRepresentation(char[] text, char[] alphaChars) {
		int[] result = new int[text.length];
        for (int i = 0; i < text.length; i++) {
            for (int k = 0; k < alphaChars.length; k++)
                if (text[i] == alphaChars[k]) {
                	result[i] = k;
                }
        }

        return result;
	}

}
