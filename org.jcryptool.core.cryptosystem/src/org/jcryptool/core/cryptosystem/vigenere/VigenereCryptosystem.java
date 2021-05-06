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
import org.jcryptool.core.cryptosystem.core.Cryptosystem;
import org.jcryptool.core.cryptosystem.core.exception.ElementNotInAlphabetException;

/**
 * Implementation of the Vigenère cryptosystem.
 * 
 * @param <A> the minimum class for the plain- and ciphertext alphabets.
 * @author Simon L
 */
public class VigenereCryptosystem<A> extends Cryptosystem<A, A, VigenereKey<A>> {

    public VigenereCryptosystem(Alphabet<A> plainAndCipherTextAlphabet) {
        super(plainAndCipherTextAlphabet, plainAndCipherTextAlphabet);
    }

    @Override
    public List<A> encrypt(List<? extends A> plainText, VigenereKey<A> key) throws ElementNotInAlphabetException {
        List<A> chiffre = new LinkedList<A>();
        List<Integer> intKey = key.getKeyAsNumberList();
        int counter = 0;
        for (A plainTextElement : plainText) {
            if (!getPlainTextAlphabet().getContent().contains(plainTextElement)) {
                throw new ElementNotInAlphabetException();
            }
            chiffre.add(rotateElementAgainstAlphabet(plainTextElement, intKey.get(counter % intKey.size()),
                    this.getPlainTextAlphabet()));
            counter++;
        }

        return chiffre;
    }

    @Override
    public List<A> decrypt(List<? extends A> cipherText, VigenereKey<A> key) throws ElementNotInAlphabetException {
        VigenereKey<A> invertedKey = invertKey(key, this.getPlainTextAlphabet());
        return encrypt(cipherText, invertedKey);
    }

    public static <A> VigenereKey<A> invertKey(VigenereKey<A> key, Alphabet<? extends A> basicAlphabet) {
        List<Integer> numberKey = key.getKeyAsNumberList();
        int[] invKey = new int[numberKey.size()];
        int alphaSize = basicAlphabet.getContent().size();
        int counter = 0;
        for (Integer keyElement : numberKey) {
            invKey[counter] = (alphaSize - keyElement) % alphaSize;
            counter++;
        }

        return new VigenereKey<A>(invKey, basicAlphabet);
    }

    public static <A> A rotateElementAgainstAlphabet(A plainTextElement, int rotAmount,
            Alphabet<? extends A> basicAlphabet) {
        return basicAlphabet.getContent().get(
                (basicAlphabet.getContent().indexOf(plainTextElement) + rotAmount) % basicAlphabet.getContent().size());
    }

}
