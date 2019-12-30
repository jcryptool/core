// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.transposition.algorithm;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.alphabets.AlphaConverter;
import org.jcryptool.core.operations.dataobject.IDataObject;

/**
 * The CaesarAlgorithm extends the AbstractClassicAlgorithm.
 * 
 * @see 
 *      org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm.
 * 
 * @author SLeischnig
 * @version 0.1
 */
public class TranspositionAlgorithm extends AbstractClassicAlgorithm {

	public static final TranspositionAlgorithmSpecification specification = new TranspositionAlgorithmSpecification();

	/**
	 * Constructor The specific engine of the algorithm is assigned.
	 * 
	 */
	public TranspositionAlgorithm() {
		engine = new TranspositionEngine();

	}

	/**
	 * This method takes the key data as a char array and generates from it the
	 * algorithm key as int array
	 * 
	 * @param keyData
	 *            the key data
	 * @return the generated key as int array
	 */
	@Override
	protected int[] generateKey(char[] keyData) {
		return alphaConv.charArrayToIntArray(keyData);
	}

	@Override
	public String getAlgorithmName() {
		return "Transposition"; //$NON-NLS-1$
	}

	@Override
	public IDataObject execute() {
		if(! isNeglectAlpha()) {
			return super.execute();
		}
		// 1st prepare alphabet table, every char is associated has an int value
		this.alphaConv = new AlphaConverter(this.dataObject.getAlphabet().getCharacterSet());
		// 2nd key
		this.key = generateKey(this.dataObject.getKey());
		this.keyChars = this.dataObject.getKey();
		if (this.dataObject.getInputStream() instanceof BufferedInputStream) {
			this.is = (BufferedInputStream) this.dataObject.getInputStream();
		} else {
			this.is = new BufferedInputStream(this.dataObject.getInputStream());
		}

		// read from inputstream and call decryt/encryt methods
		String inputString = null;
		char[] charInput;
		char[] cipherInput = null;
		char[] cipherOutput = null;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		this.dataObject.setOutputStream(bout);
		inputString = InputStreamToString(is);

		charInput = inputString.toCharArray();

		// process en-/decryption cipher as char representation remove non-alpha
		// chars
		if (!isNeglectAlpha()) {
			try {
				cipherInput = this.alphaConv.filterNonAlphaChars(charInput);
			} catch (Exception e) {
				LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while setting up the cipher input", e, true); //$NON-NLS-1$
			}
		} else {
			cipherInput = charInput;
		}

		// encrypt
		if (dataObject.getOpmode() == 0) {
			cipherOutput = encrypt(cipherInput, 0);
		} else if (dataObject.getOpmode() == 1) {
			cipherOutput = decrypt(cipherInput, 0);
		}

		char[] out2 = new char[cipherOutput.length];
		for (int i = 0; i < cipherOutput.length; i++) {
			out2[i] = cipherOutput[i];
		}

		if (filter) {
			try {
				bout.write(toByteArray(cipherOutput));
			} catch (IOException ex) {
				LogUtil.logError(OperationsPlugin.PLUGIN_ID, ex);
			}
		} else {
			char[] finalOutput = mergeToFinalOutput(charInput, cipherOutput);
			try {
				bout.write(toByteArray(finalOutput));
			} catch (IOException ex) {
				LogUtil.logError(OperationsPlugin.PLUGIN_ID, ex);
			}
		}

		return dataObject;
	}

	private boolean isNeglectAlpha() {
		return false;
//		return dataObject.getAlphabet().getName().toLowerCase().contains("printable");
	}

	@Override
	protected char[] encrypt(char[] cipherInput, int cipherCount) {
		// second key, used by some algorithms, e.g. adfgvx
		if(! isNeglectAlpha()) {
			return super.encrypt(cipherInput, cipherCount);
		}
		char[] key2 = dataObject.getKey2();
		char[] alphaChars = null;
		if (!isNeglectAlpha()) {
			try {
				alphaChars = alphaConv.filterNonAlphaChars(dataObject.getAlphabet().getCharacterSet());
			} catch (Exception e) {
				LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while filtering an alphabet", e, true); //$NON-NLS-1$
			}
		} else {
			alphaChars = dataObject.getAlphabet().getCharacterSet();
		}
		try {
		} catch (Exception e) {
			LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while filtering an alphabet", e, true); //$NON-NLS-1$
		}
		this.keyChars = alphaConv.intArrayToCharArray(key);

		// -------------------------------

		StringBuilder key1String = new StringBuilder();
		StringBuilder key2String = new StringBuilder();
		TranspositionKey tKey1, tKey2;

		boolean firstReadInOrder = TranspositionEngine.getReadOrderFromChar(alphaChars[key[0]], alphaChars);
		boolean firstReadOutOrder = TranspositionEngine.getReadOrderFromChar(alphaChars[key[1]], alphaChars);
		// Reading the first key (first two chars are order instructions)
		for (int i = 2; i < key.length; i++)
			key1String.append(alphaChars[key[i]]);
		tKey1 = new TranspositionKey(key1String.toString(), alphaChars);

		boolean secondReadInOrder = TranspositionEngine.getReadOrderFromChar(alphaChars[key[0]], alphaChars);
		boolean secondReadOutOrder = TranspositionEngine.getReadOrderFromChar(alphaChars[key[1]], alphaChars);
		// Reading the second key (first two chars are order instructions)
		for (int i = 2; i < key2.length; i++)
			key2String.append(String.valueOf((key2[i])));
		tKey2 = new TranspositionKey(key2String.toString(), alphaChars);

		char[] text;
		if(! isNeglectAlpha()) {
			// convert cipher input to an int array by using the alphabet
	        int[] input = alphaConv.charArrayToIntArray(cipherInput); 
			text = new char[input.length];
		        for (int i = 0; i < input.length; i++) text[i] = alphaChars[input[i]];
		} else {
			text = cipherInput;
		}

		// CRYPTOGRAPHIC OPERATION FINALLY

		// dont predict padding by default at encryption
		if (tKey1.getLength() > 0) {
			text = TranspositionEngine.transpose(text, tKey1, firstReadInOrder, firstReadOutOrder, false);
		}
		if (tKey2.getLength() > 0) {
			text = TranspositionEngine.transpose(text, tKey2, secondReadInOrder, secondReadOutOrder, false);
		}

		return text;
	}

	@Override
	protected char[] decrypt(char[] cipherInput, int cipherCount) {
		if(!isNeglectAlpha()) {
			return super.decrypt(cipherInput, cipherCount);
		}
		
		char[] key2 = dataObject.getKey2();
		// alphabet as char array
		char[] alphaChars = null;
		if (!isNeglectAlpha()) {
			try {
				alphaChars = alphaConv.filterNonAlphaChars(dataObject.getAlphabet().getCharacterSet());
			} catch (Exception e) {
				LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while filtering an alphabet", e, true); //$NON-NLS-1$
			}
		} else {
			alphaChars = dataObject.getAlphabet().getCharacterSet();
		}

		// ---------------

		StringBuilder key1String = new StringBuilder();
		StringBuilder key2String = new StringBuilder();
		TranspositionKey tKey1, tKey2;

		boolean firstReadInOrder = TranspositionEngine.getReadOrderFromChar(alphaChars[key[0]], alphaChars);
		boolean firstReadOutOrder = TranspositionEngine.getReadOrderFromChar(alphaChars[key[1]], alphaChars);
		// Reading the first key (first two chars are order instructions)
		for (int i = 2; i < key.length; i++)
			key1String.append(alphaChars[key[i]]);
		tKey1 = new TranspositionKey(key1String.toString(), alphaChars).getReverseKey();

		boolean secondReadInOrder = TranspositionEngine.getReadOrderFromChar(alphaChars[key[0]], alphaChars);
		boolean secondReadOutOrder = TranspositionEngine.getReadOrderFromChar(alphaChars[key[1]], alphaChars);
		// Reading the second key (first two chars are order instructions)
		for (int i = 2; i < key2.length; i++)
			key2String.append(String.valueOf((key2[i])));
		tKey2 = new TranspositionKey(key2String.toString(), alphaChars).getReverseKey();

		char[] text;
		if(! isNeglectAlpha()) {
			// convert cipher input to an int array by using the alphabet
	        int[] input = alphaConv.charArrayToIntArray(cipherInput); 
			text = new char[input.length];
		        for (int i = 0; i < input.length; i++) text[i] = alphaChars[input[i]];
		} else {
			text = cipherInput;
		}

		// CRYPTOGRAPHIC OPERATION FINALLY

		// predict padding by default at decryption
		if (tKey2.getLength() > 0) {
			text = TranspositionEngine.transpose(text, tKey2, secondReadOutOrder, secondReadInOrder, true);
		}
		if (tKey1.getLength() > 0) {
			text = TranspositionEngine.transpose(text, tKey1, firstReadOutOrder, firstReadInOrder, true);
		}

		return text;
	}
}
