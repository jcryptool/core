//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.substitution.algorithm;

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.keys.KeyVerificator;
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
	
	public SubstitutionKey.PasswordToKeyMethod getDefaultKeyCreationMethod() {
		return new SubstitutionKey.PasswordToKeyMethod(true, true, true);
	}
	
	public char[] keyInputStringToDataobjectFormat(String keyInput) {
		return keyInput.toCharArray();
	}
	
	@Override
	public List<KeyVerificator> getKeyVerificators() {
		List<KeyVerificator> result = new LinkedList<KeyVerificator>(super.getKeyVerificators());
		result.add(NO_DOUBLETS);
		return result;
	}
	
}
