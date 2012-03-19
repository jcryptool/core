//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.vernam.algorithm;

import org.jcryptool.core.operations.algorithm.classic.IClassicAlgorithmEngine;
import org.jcryptool.core.operations.alphabets.AlphaConverter;

public class VernamEngine implements IClassicAlgorithmEngine 
{

	public VernamEngine() 
	{
		
	}

	@Override
	public int[] doEncryption(int[] input, int[] key, int alphaLength,
			int[] alphabet, char nullchar, char[] alphaChars, char[] keyChars,
			char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2,
			int pastChars) {
		// TODO Auto-generated method stub
		int [] ciphertext = new int[input.length];

		for(int i=0; i<input.length; i++){

			//Calculate cipher value of plaintext value.
			ciphertext[i] = (input[i] + key[(i+pastChars)%key.length]) % alphaLength;
		}

		return ciphertext;
	}

	@Override
	public int[] doDecryption(int[] input, int[] key, int alphaLength,
			int[] alphabet, char nullchar, char[] alphaChars, char[] keyChars,
			char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2,
			int pastChars) {
		// TODO Auto-generated method stub
		int [] plaintext = new int[input.length];

		for(int i=0; i<input.length; i++){

			//Calculate plain value of cipher value.
			int provValue = input[i] - key[(i+pastChars)%key.length];

			//If value is out of range do modification.
			if(provValue < 0)
				provValue += alphaLength;

			//assign (modified) plain value
			plaintext[i] = provValue;
		}

		return plaintext;
	}

}
