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
package org.jcryptool.crypto.classic.substitution.algorithm;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmSpecification;

/**
 * 
 * 
 * @author Simon L
 */
public class SubstitutionAlgorithmSpecification extends
		ClassicAlgorithmSpecification {

	@Override
	public boolean isValidAlphabetCombination(
			AbstractAlphabet plainTextAlphabet,
			AbstractAlphabet cipherTextAlphabet) {
		return isValidPlainTextAlphabet(plainTextAlphabet) && isValidCipherTextAlphabet(cipherTextAlphabet)
		&& plainTextAlphabet.getCharacterSet().length == cipherTextAlphabet.getCharacterSet().length;
	}

	public char[] keyInputStringToDataobjectFormat(String keyInput) {
		return keyInput.toCharArray();
	}
	
}
