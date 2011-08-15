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
package org.jcryptool.crypto.classic.caesar.algorithm;

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmSpecification;

/**
 * 
 * 
 * @author Simon L
 */
public class CaesarAlgorithmSpecification extends
		ClassicAlgorithmSpecification {
	
	/**
	 * Validates, that the key is not blank. although, the key is not rejected, if blank, just a warning is put.
	 */
	public static final KeyVerificator KEYLENGTH = new KeyVerificator() {
		@Override
		protected boolean verifyKeyInput(String key, AbstractAlphabet alphabet) {
			return key.length() < 2;
		}
		@Override
		protected InputVerificationResult getFailResult(String key, AbstractAlphabet alphabet) {
			return new InputVerificationResult() {
				public boolean isStandaloneMessage() {
					return false;
				}
				public int getMessageType() {
					return WARNING;
				}
				public String getMessage() {
					return org.jcryptool.crypto.classic.caesar.ui.Messages.CaesarWizardPage_keyTooLong;
				}
				public boolean isValid() {
					return false;
				}
				public String toString() {return "TOO_BIG_KEY_FAIL";} //$NON-NLS-1$
			};
		}
	};
	
	public char[] keyInputStringToDataobjectFormat(String keyInput) {
		return keyInput.toCharArray();
	}

	@Override
	public List<KeyVerificator> getKeyVerificators() {
		List<KeyVerificator> verificators = new LinkedList<KeyVerificator>();
		verificators.add(NOKEY(org.jcryptool.crypto.classic.caesar.ui.Messages.CaesarWizardPage_key_instruction));
		verificators.add(KEYLENGTH);
		verificators.add(KEY_IN_ALPHABET);
		
		return verificators;
	}
	
	
	
}
