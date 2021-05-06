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
package org.jcryptool.core.cryptosystem.core.text;

import java.util.List;

import org.jcryptool.core.cryptosystem.core.Alphabet;
import org.jcryptool.core.cryptosystem.core.Cryptosystem;
import org.jcryptool.core.cryptosystem.core.Key;
import org.jcryptool.core.cryptosystem.core.exception.ElementNotInAlphabetException;

/**
 * Models a cryptosystem on lists, which can parse strings by using TextCompatibleAlphabet objects as alphabets.
 * 
 * @see Cryptosystem
 * 
 * @author Simon L
 */
public abstract class TextCompatibleCryptosystem<P, C, K extends Key> extends Cryptosystem<P, C, K> {

    private TextCompatibleAlphabet<P> textCompatiblePlaintextAlphabet;
    private TextCompatibleAlphabet<C> textCompatibleCiphertextAlphabet;

    /**
     * @see Cryptosystem#Cryptosystem(Alphabet, Alphabet)
     */
    public TextCompatibleCryptosystem(TextCompatibleAlphabet<P> plainTextAlphabet,
            TextCompatibleAlphabet<C> cipherTextAlphabet) {
        super(plainTextAlphabet, cipherTextAlphabet);
        this.textCompatiblePlaintextAlphabet = plainTextAlphabet;
        this.textCompatibleCiphertextAlphabet = cipherTextAlphabet;
    }

    /**
     * Encrypts a string, by parsing it with the TextCompatibleAlphabet given at object creation.
     * 
     * @throws ElementNotInAlphabetException when unexpected contents (e. g. non-alphabet-elements/not compliant content
     *             to the parse method in the {@link TextCompatibleAlphabet}) were encountered at String parsing.
     * @see #encrypt(List, Key)
     * @see TextCompatibleAlphabet#parseString(String)
     */
    public String encryptTextToText(String plainText, K key) throws ElementNotInAlphabetException {
        List<P> plainList = textCompatiblePlaintextAlphabet.parseString(plainText);
        return listToString(encrypt(plainList, key));
    }

    /**
     * Decrypts a string, by parsing it with the TextCompatibleAlphabet given at object creation.
     * 
     * @throws ElementNotInAlphabetException when unexpected contents (e. g. non-alphabet-elements/not compliant content
     *             to the parse method in the {@link TextCompatibleAlphabet}) were encountered at String parsing.
     * @see #decrypt(List, Key)
     * @see TextCompatibleAlphabet#parseString(String)
     */
    public String decryptTextToText(String ciphertext, K key) throws ElementNotInAlphabetException {
        List<C> cipherList = textCompatibleCiphertextAlphabet.parseString(ciphertext);
        return listToString(decrypt(cipherList, key));
    }

    /**
     * Converts a list of object into a String by concatenating the toString()-representations
     * 
     * @param list the list to make into a sString
     * @return the concatenation of String representations
     */
    public static String listToString(List<?> list) {
        StringBuilder b = new StringBuilder();
        for (Object o : list)
            b.append(o.toString());
        return b.toString();
    }

}
