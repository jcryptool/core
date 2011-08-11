//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.caesar.algorithm;

import org.jcryptool.core.operations.algorithm.classic.IClassicAlgorithmEngine;
import org.jcryptool.core.operations.alphabets.AlphaConverter;

/**
 * The CaesarEngine class represents the engine of the Caesar algorithm class. 
 * @see org.jcryptool.classic.caesar.algorithm.CaesarAlgorithm
 * 
 * It provides Caesar-based encryption and decryption on integer basis.
 * 
 * @author Amro Al-Akkad
 * @version 0.1
 */
public class CaesarEngine implements IClassicAlgorithmEngine{

	/**
	 * Decryption
	 * @param input data to be decrypted.
	 * @param key which the decryption uses.
	 * @param alphaLength the length of the currentAlphabet.
	 * @return the decrypted data as an int array.
	 */
	public int[] doDecryption(int[] input, int[] key, int alphaLength, 
			int[] alphabet, char nullchar, char[] alphaChars, char[] keyChars, 
			char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2, int pastChars){
		
		int [] plaintext = new int[input.length];
		
		for(int i=0; i<input.length; i++){
			
			//Calculate plain value of cipher value.
			int provValue = input[i] - key[0];
			
			//If value is out of range do modification. 
			if(provValue < 0)
				provValue += alphaLength;
			
			//assign (modified) plain value
			plaintext[i] = provValue;
			
		}
		
		return plaintext;
	}

	/**
	 * Encryption
	 * @param input data to be encrypted.
	 * @param key which the encryption uses.
	 * @param alphaLength the length of the currentAlphabet.
	 * @return the encrypted data as an int array.
	 */
	public int [] doEncryption(int[] input, int[] key, int alphaLength, 
			int[] alphabet, char nullchar, char[] alphaChars, char[] keyChars, 
			char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2, int pastChars){
		
		int [] ciphertext = new int[input.length];
		
		for(int i=0; i<input.length; i++){
			
			//Calculate cipher value of the plaintext value.
			ciphertext[i] = (input[i] + key[0]) % alphaLength;
		}
		
		return ciphertext;		
	}

}
