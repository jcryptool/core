//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.cryptosystem.vigenere;

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.core.cryptosystem.core.Alphabet;
import org.jcryptool.core.cryptosystem.core.Key;

/**
 * Models Vigenère keys.
 * 
 * @param <A> the minimum alphabet class
 * @author Simon L
 */
public class VigenereKey<A> extends Key {

    private static final String KEY_VERIFICATION_RESULT_OK = "OK";
    private Alphabet<? extends A> alphabet;
    private List<? extends A> key;

    /**
     * Creates a new Vigenère key by specifying a list of elements of the alphabet.
     * 
     * @param alphabet the alphabet as reference point
     * @param key the list of elements from the alphabet
     * @throws IllegalArgumentException if the key is empty or if one element of the key list is not member of the
     *             alphabet
     */
    public VigenereKey(List<? extends A> key, Alphabet<A> alphabet) {
        String checkResult = checkKey(key, alphabet);
        if (!KEY_VERIFICATION_RESULT_OK.equals(checkResult)) {
            throw new IllegalArgumentException(checkResult);
        }
        this.alphabet = alphabet;
        this.key = key;
    }

    /**
     * Creates a new Vigenère key by specifying a list of indices of the alphabet.
     * 
     * @param alphabet the alphabet as reference point
     * @param key the index list (you could also say "shift amount list")
     * @throws IllegalArgumentException if the key is empty or if one element of the index list is out of bounds
     */
    public VigenereKey(int[] key, Alphabet<? extends A> alphabet) {
        String checkResult = checkIndexbasedKey(key, alphabet);
        if (!KEY_VERIFICATION_RESULT_OK.equals(KEY_VERIFICATION_RESULT_OK)) {
            throw new IllegalArgumentException(checkResult);
        }
        List<Integer> intKey = new LinkedList<Integer>();
        for (int keyElem : key)
            intKey.add(keyElem);

        this.key = calculateAlphabetElementKey(key, alphabet);
        this.alphabet = alphabet;
    }

    /**
     * @return the key alphabet
     */
    public Alphabet<? extends A> getAlphabet() {
        return alphabet;
    }

    /**
     * @return the key
     */
    public List<? extends A> getKey() {
        return key;
    }

    /**
     * @return the key as list of indices against the alphabet.
     */
    public List<Integer> getKeyAsNumberList() {
        return calculateNumberKey(getKey(), getAlphabet());
    }

    private static <A> List<A> calculateAlphabetElementKey(int[] key, Alphabet<A> alphabet) {
        List<A> elementKey = new LinkedList<A>();
        for (int keyElement : key) {
            elementKey.add(alphabet.getContent().get(keyElement));
        }
        return elementKey;
    }

    private static <A> List<Integer> calculateNumberKey(List<? extends A> list, Alphabet<? extends A> basicAlphabet) {
        List<Integer> intKey = new LinkedList<Integer>();
        for (A keyElement : list) {
            intKey.add(basicAlphabet.getContent().indexOf(keyElement));
        }
        return intKey;
    }

    public static String checkIndexbasedKey(int[] key, Alphabet<?> alphabet) {
        if (key.length != 0) {
            for (int keyElement : key) {
                if (keyElement >= alphabet.getContent().size()) {
                    return elementOfIndexbasedKeyOutOfBoundsVerificationResult(keyElement);
                }
            }
            return KEY_VERIFICATION_RESULT_OK;
        }
        return keyEmptyVerificationResult();
    }

    public static <A> String checkKey(List<? extends A> key, Alphabet<A> alphabet) {
        if (key.size() > 0) {
            for (A keyElement : key) {
                if (!alphabet.getContent().contains(keyElement)) {
                    return notPartOfAlphabetVerificationResult(keyElement);
                }
            }
            return KEY_VERIFICATION_RESULT_OK;
        }
        return keyEmptyVerificationResult();
    }

    private static String elementOfIndexbasedKeyOutOfBoundsVerificationResult(final int keyElement) {
        return "one of the elements of the index based key (" + keyElement + ") is out of bounds";
    }

    private static String keyEmptyVerificationResult() {
        return "the key is empty";
    }

    private static String notPartOfAlphabetVerificationResult(final Object keyElement) {
        return "one of the elements of the key (" + keyElement.toString() + ") is not part of the alphabet";
    }

}
