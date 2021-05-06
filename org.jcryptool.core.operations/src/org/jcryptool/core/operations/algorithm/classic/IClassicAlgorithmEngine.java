// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm.classic;

import org.jcryptool.core.operations.alphabets.AlphaConverter;

/**
 * This interface represents the design for of an classic algorithm's engine.
 * 
 * @author amro
 * @version 0.2.1
 */
public interface IClassicAlgorithmEngine {

    /**
     * Encryption
     * 
     * @param input data to be encrypted.
     * @param key which the encryption uses.
     * @param alphaLength the length of the alphabet.
     * @return the encrypted data as an int array.
     */
    public int[] doEncryption(int[] input, int[] key, int alphaLength, int[] alphabet, char nullchar,
            char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2,
            int pastChars);

    /**
     * Decryption
     * 
     * @param input data to be decrypted.
     * @param key which the decryption uses.
     * @param alphaLength the length of the alphabet.
     * @return the decrypted data as an int array.
     */
    public int[] doDecryption(int[] input, int[] key, int alphaLength, int[] alphabet, char nullchar,
            char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2,
            int pastChars);
}
