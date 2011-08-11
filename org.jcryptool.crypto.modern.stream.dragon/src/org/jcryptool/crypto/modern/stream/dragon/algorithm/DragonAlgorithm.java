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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jcryptool.core.operations.algorithm.modern.AbstractModernAlgorithm;
import org.jcryptool.core.operations.dataobject.modern.IModernDataObject;
import org.jcryptool.core.operations.dataobject.modern.symmetric.SymmetricDataObject;

/**
 * The DragonAlgorithm extends the AbstractModernAlgorithm.
 * @see org.jcryptool.core.operations.algorithm.modern.AbstractModernAlgorithm.
 *
 * @author Tahir Kacak
 * @version 0.1
 */
public class DragonAlgorithm extends AbstractModernAlgorithm{

	/**
	 * The InputStream
	 */
	protected BufferedInputStream fileInputStream;

	/**
	 * data object implementation must be the Dragon implementation
	 */
	protected SymmetricDataObject dataObject;

	/**
	 * operation's engine
	 */
	protected DragonEngine engine;

	/**
	 * The DragonKeyStreamGenerator's key
	 * @see org.jcryptool.crypto.modern.stream.dragon.algorithm.DragonKeyStreamGenerator
	 */
	protected byte[] key;

	/**
	 * The DragonKeyStreamGenerator's IV
	 * @see org.jcryptool.crypto.modern.stream.dragon.algorithm.DragonKeyStreamGenerator
	 */
	protected byte[] iv;

	/**
	 * Constructor
	 * The specific engine of the algorithm is assigned.
	 *
	 */
	public DragonAlgorithm(){
		engine = new DragonEngine();
	}

	@Override
	public IModernDataObject execute() {
		// setup keystream generator
		key = dataObject.getSymmetricKey();
		iv = dataObject.getIV();

		// check if key and IV are same length
        if (key.length != iv.length)
            throw new Error("key and IV length must be equal"); //$NON-NLS-1$

        int[] dragonKey = byteArrayToIntArray(key);
        int[] dragonIV = byteArrayToIntArray(iv);

        DragonKeyStreamGenerator keyStreamGenerator = new DragonKeyStreamGenerator();

        if (dragonKey.length == 4)
        	keyStreamGenerator.dInit128(dragonKey, dragonIV);
        else if (dragonKey.length == 8)
        	keyStreamGenerator.dInit256(dragonKey, dragonIV);
        else
        	throw new Error("key and IV must be 128 bit or 256 bit"); //$NON-NLS-1$


		if(dataObject.getInputStream() instanceof BufferedInputStream){
			fileInputStream = (BufferedInputStream) dataObject.getInputStream();
		}else{
			fileInputStream = new BufferedInputStream(dataObject.getInputStream());
		}

		//read from InputStream and call decrypt/encrypt methods
		byte[] cipherInput = new byte[1024];
		int readFromStream = 0;
		byte[] cipherOutput = null;
		byte[] keyStream = null;

		ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream();
		dataObject.setOutputStream(cipherOutputStream);

		ByteArrayOutputStream keyStreamOutputStream = new ByteArrayOutputStream();
		dataObject.setOutputStream(keyStreamOutputStream);

		try {
			readFromStream = fileInputStream.read(cipherInput);
		} catch (IOException e) {}

		while(readFromStream != -1){

			byte[] trimmedCipherInput = new byte[readFromStream];
			for (int i = 0; i < readFromStream; i++)
				trimmedCipherInput[i] = cipherInput[i];

			// process encryption/decryption cipher as byte representation
			cipherOutput = engine.encryptDecrypt(trimmedCipherInput, keyStreamGenerator);
			keyStream = engine.getKeyStream();

			try {
				cipherOutputStream.write(cipherOutput);
				dataObject.setOutputIS(new BufferedInputStream(new ByteArrayInputStream(((ByteArrayOutputStream)cipherOutputStream).toByteArray())));
				dataObject.setOutput(((ByteArrayOutputStream)cipherOutputStream).toByteArray());
				keyStreamOutputStream.write(keyStream);
				dataObject.setInputStream(new BufferedInputStream(new ByteArrayInputStream(((ByteArrayOutputStream)keyStreamOutputStream).toByteArray())));
			} catch (IOException e1) {}
			try {
				readFromStream = fileInputStream.read(cipherInput);
			} catch (IOException e) {
				readFromStream = -1;
			}
		}
		return dataObject;
	}

	/**
	 * Returns the data object of the current algorithm.
	 *
	 * @return the current algorithms data object
	 */
	@Override
	public IModernDataObject getDataObject() {
		return dataObject;
	}

	/**
	 * Initialises the data object.
	 *
	 * @param input the InputStream of the input file editor
	 * @param key the DragonKeyStreamGenerator's key
	 * @param iv the DragonKeyStreamGenerator's IV
	 */
	public void init(InputStream input, byte[] key, byte[] iv) {
		this.dataObject = new SymmetricDataObject();
		this.dataObject.setInputStream(input);
		this.dataObject.setSymmetricKey(key);
		this.dataObject.setIV(iv);
	}

	/**
	 * Converts an array of bytes to an array of ints.
	 * If the number of bytes does not divide equally by 4, then the right-most bits of the final int will be 0's.
	 *
	 * @param arrayToConvert the input byte array
	 * @return an int array which represents the concatenation of bits from each group of 4 bytes.
	 */
	public static int[] byteArrayToIntArray(byte[] arrayToConvert) {
		int[] intArray;
		if (arrayToConvert.length % 4 == 0)
			intArray = new int[arrayToConvert.length / 4];
		else
			intArray = new int[(arrayToConvert.length / 4) + 1];

		// the 4 byte array which represents the current int being converted
		byte[] currentIntByteArray = new byte[4];
		int currentInt = 0;
		int shiftDistance;

		// for each int in the output int array
		for (int i = 0; i < intArray.length; i++) {

			// for each byte in the current int's 4 byte array, get the corresponding bytes from the input byte array
			for (int j = 0; j < currentIntByteArray.length; j++) {
				if (((i * 4) + j) < arrayToConvert.length)
					currentIntByteArray[j] = arrayToConvert[(i * 4) + j];
				else
					currentIntByteArray[j] = 0;
			}

			currentInt = 0;

			// for each byte in the current int's 4 byte array, assign the bytes of the current int
			for (int j = 0; j < currentIntByteArray.length; j++) {
				shiftDistance = (4 - 1 - j) * 8;
				currentInt += (currentIntByteArray[j] & 0x000000FF) << shiftDistance;
			}
			intArray[i] = currentInt;
		}

		return intArray;
	}

    @Override
    public String getAlgorithmName() {
        return "Dragon"; //$NON-NLS-1$
    }
}
