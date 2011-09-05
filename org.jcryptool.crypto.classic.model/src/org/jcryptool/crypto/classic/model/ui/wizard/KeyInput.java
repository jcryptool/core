// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.model.ui.wizard;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.TextfieldInput;
import org.jcryptool.crypto.classic.model.algorithm.InputVerificationResultKeyNotInAlphabet;

public abstract class KeyInput<T> extends TextfieldInput<T> {

	protected boolean canAutocorrect(InputVerificationResult result) {
		// offers autocorrection for chars which are not accepted lowercase, but uppercase, or the other way round
		if(result instanceof InputVerificationResultKeyNotInAlphabet) {
			InputVerificationResultKeyNotInAlphabet resultKNIA = (InputVerificationResultKeyNotInAlphabet) result;
			if(getAlphabet().contains(Character.toUpperCase(resultKNIA.getErroneusChar()))) {
				return true;
			}
			if(getAlphabet().contains(Character.toLowerCase(resultKNIA.getErroneusChar()))) {
				return true;
			}
		}
		return false;
	}

	protected void autocorrect(InputVerificationResult result) {
		if(result instanceof InputVerificationResultKeyNotInAlphabet) {
			String correctedKey = getTextfield().getText();

			InputVerificationResultKeyNotInAlphabet resultKNIA = (InputVerificationResultKeyNotInAlphabet) result;
			correctedKey = getTextfield().getText().substring(0, ((InputVerificationResultKeyNotInAlphabet) result).getPosition());
			if(getAlphabet().contains(Character.toUpperCase(resultKNIA.getErroneusChar()))) {
				correctedKey += Character.toUpperCase(resultKNIA.getErroneusChar());
			} else if(getAlphabet().contains(Character.toLowerCase(resultKNIA.getErroneusChar()))) {
				correctedKey += Character.toLowerCase(resultKNIA.getErroneusChar());
			}
			if(resultKNIA.getPosition() < getTextfield().getText().length()-1) {
				correctedKey += getTextfield().getText().substring(resultKNIA.getPosition()+1);
			}

			setTextfieldTextExternal(correctedKey);
		}
	}


	/**
	 * @return the alphabet for the plain text
	 */
	public abstract AbstractAlphabet getAlphabet();

}
