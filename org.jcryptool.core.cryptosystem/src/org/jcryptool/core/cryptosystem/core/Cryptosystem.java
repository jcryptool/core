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
package org.jcryptool.core.cryptosystem.core;

import java.util.List;

import org.jcryptool.core.cryptosystem.core.exception.ElementNotInAlphabetException;

/**
 * <p>
 * Represents a cryptosystem on list-form plain- and ciphertexts. Every encryption or decryption encrypts or decrypts a
 * List of elements. The definition of such a cryptosystem <b>S</b> is:
 * </p>
 * 
 * <p>
 * <b>S</b> = (<b>P*</b>, <b>C*</b>, <b>K</b>, <b>encrypt</b>, <b>decrypt</b>)
 * </p>
 * 
 * <p>
 * - <b>P</b> is the plain text alphabet<br />
 * - <b>C</b> ist the cipher text alphabet<br />
 * - <b>K</b> is the key space<br />
 * - <b>encrypt</b> &sube; P* &times; K &rarr; C*<br />
 * - <b>decrypt</b> &sube; C* &times; K &rarr; P*<br />
 * - &forall;m&isin;P*,k&isin;K. &exist;k'&isin;K. decrypt(encrypt(m,k),k') = m
 * </p>
 * <p>
 * The sets P and C are defined in the constructor, the functions encrypt and decrypt are abstract functions. These
 * functions must be implemented such that they throw ElementNotInAlphabetException, should an argument (plain- or
 * ciphertext) violate the above contract.<br />
 * Key classes must be implemented in a way that only well-formed keys can exist.
 * </p>
 * 
 * <p>
 * The generic type parameters &lt;P&gt; and &lt;C&gt; are introduced to ensure type boundaries for the plain- and
 * ciphertexts. Note, that these generic type parameters do <b>not</b> define the alphabets; the alphabets are defined
 * in the constructor.
 * </p>
 * <p>
 * </p>
 * 
 * 
 * @param <P> the minimum class of the plain text alphabet (the alphabet itself is given with the constructor)
 * @param <C> the minimum class of the cipher text alphabet (the alphabet itself is given with the constructor)
 * @param <K> the class which represents keys and itself defines the boundaries of the key space (no objects of this
 *            class may exist without being element of the key space)
 * @author Simon L
 */
public abstract class Cryptosystem<P, C, K extends Key> {

    private Alphabet<P> plainTextAlphabet;
    private Alphabet<C> cipherTextAlphabet;

    /**
     * Creates a list-based cryptosystem with the given plain- and ciphertext alphabets.
     * 
     * @see Cryptosystem
     * 
     * @param plainTextAlphabet the plaintext alphabet
     * @param cipherTextAlphabet the ciphertext alphabet
     */
    public Cryptosystem(Alphabet<? extends P> plainTextAlphabet, Alphabet<? extends C> cipherTextAlphabet) {
        this.plainTextAlphabet = new Alphabet<P>(plainTextAlphabet.getContent());
        this.cipherTextAlphabet = new Alphabet<C>(cipherTextAlphabet.getContent());
    }

    /**
     * <p>
     * Encrypts a given plaintext with a given key. In symmetric cryptosystems, this method should be implemented such,
     * that encrypt(decrypt(m, k), k) = m.<br />
     * <i>(even if the formal definition only demands the existence of an "inverse" k' to each k. Given the symmetry of
     * the system, there has to exist an inversion algorithm, which should be used in either the en- or the decrypt
     * method to spare the cryptosystem user the extra programming)</i>
     * </p>
     * <p>
     * This function is required to throw ElementNotInAlphabetException whenever it encounters an element in the
     * plaintext which is not part of the alphabet.
     * </p>
     * 
     * @param plainText the plain text
     * @param key the key
     * @return the cipher text
     */
    public abstract List<C> encrypt(List<? extends P> plainText, K key) throws ElementNotInAlphabetException;

    /**
     * <p>
     * Decrypts a given ciphertext with a given key. In symmetric cryptosystems, this method should be implemented such,
     * that encrypt(decrypt(m, k), k) = m.<br />
     * <i>(even if the formal definition only demands the existence of an "inverse" k' to each k. Given the symmetry of
     * the system, there has to exist an inversion algorithm, which should be used in either the en- or the decrypt
     * method to spare the cryptosystem user the extra programming)</i>
     * </p>
     * <p>
     * This function is required to throw ElementNotInAlphabetException whenever it encounters an element in the
     * ciphertext which is not part of the alphabet.
     * </p>
     * 
     * @param plainText the ciphertext
     * @param key the key
     * @return the plaintext
     */
    public abstract List<P> decrypt(List<? extends C> cipherText, K key) throws ElementNotInAlphabetException;

    public Alphabet<P> getPlainTextAlphabet() {
        return plainTextAlphabet;
    }

    public Alphabet<C> getCipherTextAlphabet() {
        return cipherTextAlphabet;
    }

}
