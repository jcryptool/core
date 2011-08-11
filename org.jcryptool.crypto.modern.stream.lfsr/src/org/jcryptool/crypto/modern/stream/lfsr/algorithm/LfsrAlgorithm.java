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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jcryptool.core.operations.algorithm.modern.AbstractModernAlgorithm;
import org.jcryptool.core.operations.dataobject.modern.IModernDataObject;
import org.jcryptool.core.operations.dataobject.modern.symmetric.LfsrDataObject;

/**
 * The LfsrAlgorithm extends the AbstractModernAlgorithm.
 * @see org.jcryptool.core.operations.algorithm.modern.AbstractModernAlgorithm.
 *
 * @author Tahir Kacak
 * @version 0.1
 */
public class LfsrAlgorithm extends AbstractModernAlgorithm{

	/**
	 * The InputStream
	 */
	protected BufferedInputStream fileInputStream;

	/**
	 * data object implementation must be the Lfsr implementation
	 */
	protected LfsrDataObject dataObject;

	/**
	 * operation's engine
	 */
	protected LfsrEngine engine;

	/**
	 * The LfsrKeyStreamGenerator's seed (initial state).
	 * @see org.jcryptool.crypto.modern.stream.lfsr.algorithm.LfsrKeyStreamGenerator
	 */
	protected boolean[] seed;

	/**
	 * The LfsrKeyStreamGenerator's tap settings.
	 * @see org.jcryptool.crypto.modern.stream.lfsr.algorithm.LfsrKeyStreamGenerator
	 */
	protected boolean[] tapSettings;

	/**
	 * Constructor
	 * The specific engine of the algorithm is assigned.
	 *
	 */
	public LfsrAlgorithm(){
		engine = new LfsrEngine();
	}

	@Override
	public IModernDataObject execute() {
		seed = dataObject.getSeed().clone();
		tapSettings = dataObject.getTapSettings();

		LfsrKeyStreamGenerator keyStreamGenerator = new LfsrKeyStreamGenerator(seed, tapSettings);

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
	 * Initializes the data object.
	 *
	 * @param input the InputStream of the input file editor
	 * @param seed the LfsrKeyStreamGenerator's seed (initial state)
	 * @param tapSettings the LfsrKeyStreamGenerator's tap settings
	 */
	public void init(InputStream input, boolean[] seed, boolean[] tapSettings) {
		this.dataObject = new LfsrDataObject();
		this.dataObject.setInputStream(input);
		this.dataObject.setSeed(seed);
		this.dataObject.setTapSettings(tapSettings);
	}

    @Override
    public String getAlgorithmName() {
        return "LFSR"; //$NON-NLS-1$
    }
}
