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
package org.jcryptool.crypto.classic.vigenere.algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmCmd;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmSpecification;

/**
 * The VigenereCmd class provides command line functionality for the vigenere algorithm.
 *
 * @author amro
 * @version 0.0
 *
 */
public class VigenereCmd extends ClassicAlgorithmCmd {

	
	@Override
	protected char[] keyToDataobjectFormat(String key) {
		return VigenereAlgorithm.specification.keyInputStringToDataobjectFormat(key);
	}

	@Override
	protected AbstractClassicAlgorithm initializeAlgorithm(int cryptMode,
			InputStream inputStream, AbstractAlphabet alphabet2, char[] key,
			boolean filter) {
		AbstractClassicAlgorithm algorithm = new VigenereAlgorithm();
		algorithm.init(cryptMode, inputStream, alphabet2, key, null);
		algorithm.setFilter(filter);
		
		return algorithm;
	}

	// TODO: !remove
	private static String inputStreamToString(InputStream in) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line + "\n");
		}

		return stringBuilder.toString();
	}
	
	@Override
	protected ClassicAlgorithmSpecification getSpecification() {
		return VigenereAlgorithm.specification;
	}
	
	
	
}
