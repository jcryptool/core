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
package org.jcryptool.crypto.classic.autovigenere;

import java.io.InputStream;

import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.autovigenere.algorithm.AutoVigenereAlgorithm;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmCmd;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmSpecification;

/**
 * 
 * 
 * @author Simon L
 */
public class AutovigenereCmd extends ClassicAlgorithmCmd {

	/* (non-Javadoc)
	 * @see org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmCmd#keyToDataobjectFormat(java.lang.String)
	 */
	@Override
	protected char[] keyToDataobjectFormat(String key) {
		return AutoVigenereAlgorithm.specification.keyInputStringToDataobjectFormat(key);
	}

	/* (non-Javadoc)
	 * @see org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmCmd#initializeAlgorithm(int, java.io.InputStream, org.jcryptool.core.operations.alphabets.AbstractAlphabet, char[], boolean)
	 */
	@Override
	protected AbstractClassicAlgorithm initializeAlgorithm(int cryptMode,
			InputStream inputStream, AbstractAlphabet alphabet2, char[] key,
			boolean filter) {
		AutoVigenereAlgorithm algo = new AutoVigenereAlgorithm();
		algo.init(cryptMode, inputStream, alphabet2, key, null);
		return algo;
	}

	@Override
	protected ClassicAlgorithmSpecification getSpecification() {
		return AutoVigenereAlgorithm.specification;
	}
	
	

}
