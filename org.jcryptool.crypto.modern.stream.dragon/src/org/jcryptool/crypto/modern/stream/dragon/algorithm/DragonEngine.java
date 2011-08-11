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
package org.jcryptool.crypto.modern.stream.dragon.algorithm;

/**
 * The DragonEngine class represents the engine of the Dragon algorithm class.
 * @see org.jcryptool.crypto.modern.stream.dragon.algorithm.DragonAlgorithm
 *
 * It provides Dragon-based encryption and decryption on a binary basis.
 *
 * @author Tahir Kacak
 * @version 0.1
 */
public class DragonEngine{
	private byte[] keyStream;

	/**
	 * Provides encryption/ decryption functionality.  Generates required keystream and xor's that keystream against the input.
	 *
	 * @param cipherInput data to be encrypted/ decrypted.
	 * @param keyStreamGenerator used to generate the keystream for the encryption/ decryption process.
	 * @return the encrypted/ decrypted data as a byte array.
	 */
	public byte[] encryptDecrypt(byte[] cipherInput, DragonKeyStreamGenerator keyStreamGenerator) {

        int[] intKeyStream;
        if (cipherInput.length % 4 == 0)
        	intKeyStream = new int[cipherInput.length / 4];
        else
        	intKeyStream = new int[(cipherInput.length / 4) + 1];

        byte[] output = new byte[cipherInput.length];

        if (intKeyStream.length % 2 == 0) {
	        for (int i = 0; i < intKeyStream.length / 2; i++) {
	        	keyStreamGenerator.dGen();
	        	intKeyStream[i * 2] = keyStreamGenerator.getA();
	        	intKeyStream[(i * 2) + 1] = keyStreamGenerator.getE();
	        }
        } else {
        	int i;
        	for (i = 0; i < (intKeyStream.length / 2); i++) {
	        	keyStreamGenerator.dGen();
	        	intKeyStream[i * 2] = keyStreamGenerator.getA();
	        	intKeyStream[(i * 2) + 1] = keyStreamGenerator.getE();
	        }
        	intKeyStream[i * 2] = keyStreamGenerator.getA();
        }

        keyStream = intArrayToByteArray(intKeyStream);

        for (int i = 0; i < output.length; i++)
        	output[i] = (byte) (cipherInput[i] ^ keyStream[i]);

		return output;
	}

	/**
	 * Returns the current state of the keystream.
	 *
	 * @return the last generated keystream
	 */
	public byte[] getKeyStream() {
		return keyStream;
	}

	/**
	 * Converts an array of ints to an array of bytes.
	 *
	 * @param arrayToConvert the input int array
	 * @return a byte array which represents 4 bytes for each int representing the bits of the int.
	 */
	public static byte[] intArrayToByteArray(int[] arrayToConvert) {
		byte[] byteArray = new byte[arrayToConvert.length * 4];
		int shiftDistance;

		// for each int in the input array
		for (int i = 0; i < arrayToConvert.length; i++) {
			shiftDistance = 24;

			// for each byte in the current int, assign the bytes value to the byte output array
			for (int j = 3; j >= 0; j--) {
				byteArray[i * 4 + 3 - j] = (byte) (arrayToConvert[i] >>> shiftDistance);
				shiftDistance -= 8;
			}
		}
		return byteArray;
	}
}
