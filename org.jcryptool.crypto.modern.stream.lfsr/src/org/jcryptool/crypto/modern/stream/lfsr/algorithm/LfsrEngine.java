//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.modern.stream.lfsr.algorithm;

/**
 * The LfsrEngine class represents the engine of the Lfsr algorithm class.
 * @see org.jcryptool.crypto.modern.stream.lfsr.algorithm.LfsrAlgorithm
 *
 * It provides Lfsr-based encryption and decryption on a binary basis.
 *
 * @author Tahir Kacak
 * @version 0.1
 */
public class LfsrEngine{
	private boolean[] keyStream;

	/**
	 * Provides encryption/ decryption functionality.  Generates required keystream and xor's that keystream against the input.
	 *
	 * @param cipherInput data to be encrypted/ decrypted.
	 * @param keyStreamGenerator used to generate the keystream for the encryption/ decryption process.
	 * @return the encrypted/ decrypted data as a byte array.
	 */
	public byte[] encryptDecrypt(byte[] cipherInput, LfsrKeyStreamGenerator keyStreamGenerator){
		boolean[] inputOutput = byteArrayToBooleanArray(cipherInput);
		keyStream = new boolean[inputOutput.length];

		for (int i = 0; i < keyStream.length; i++){
			keyStream[i] = keyStreamGenerator.doRound();
			inputOutput[i] ^= keyStream[i];
		}

		return booleanArrayToByteArray(inputOutput);
	}

	/**
	 * Returns the current state of the keystream.
	 *
	 * @return the last generated keystream
	 */
	public byte[] getKeyStream() {
		return booleanArrayToByteArray(keyStream);
	}

	/**
	 * Converts an array of bytes to an array of booleans.
	 *
	 * @param arrayToConvert the input byte array
	 * @return a boolean array which represents the individual bits of the bytes
	 */
	public static boolean[] byteArrayToBooleanArray(byte[] arrayToConvert) {
		boolean[] booleanArray = new boolean[arrayToConvert.length * 8];

		// for each byte in the input array
		for (int i = 0; i < arrayToConvert.length; i++) {

			// for each bit (boolean) in the current byte, assign the booleans value to the boolean output array
			for (int j = 7; j >= 0; j--)
				booleanArray[i * 8 + 7 - j] = ((arrayToConvert[i] >>> j) & 1) == 1;
		}

		return booleanArray;
	}

	/**
	 * Converts an array of booleans to an array of bytes.
	 * If the number of booleans does not divide equally by 8, then the right-most bits of the final byte will be 0's (ie, falses).
	 *
	 * @param arrayToConvert the input boolean array
	 * @return a byte array which represents the bytes formed by each group of 8 bits (each group of 8 booleans).
	 */
	public static byte[] booleanArrayToByteArray(boolean[] arrayToConvert) {
		byte[] byteArray = new byte[arrayToConvert.length / 8];

		// the 8 bit (boolean) array which represents the current byte being converted
		boolean[] currentByteBooleanArray = new boolean[8];
		byte currentByte;
		byte bitSetter;

		// for each byte in the output byte array
		for (int i = 0; i < byteArray.length; i++){

			// for each bit (boolean) in the current byte's 8 bit array, get the corresponding bits from the input boolean array
			for (int j = 0; j < currentByteBooleanArray.length; j++) {
				if (((i * 8) + j) < arrayToConvert.length)
					currentByteBooleanArray[j] = arrayToConvert[(i * 8) + j];
				else
					currentByteBooleanArray[j] = false;
			}

			bitSetter = 64;
			currentByte = 0;

			// if the most significant bit (the left-most bit) of the current byte is a 1, set that bit to 1
			if (currentByteBooleanArray[0])
	            currentByte ^= -64 ^ 64;

			//// for each remaining bit (boolean) in the current byte's 8 bit array, assign the bits of the current byte
			for (int j = 1; j < currentByteBooleanArray.length; j++){
			    if (currentByteBooleanArray[j]){
			        currentByte += bitSetter;
			    }
			    bitSetter /= 2;
			}
			byteArray[i] = currentByte;
		}

		return byteArray;
	}
}
