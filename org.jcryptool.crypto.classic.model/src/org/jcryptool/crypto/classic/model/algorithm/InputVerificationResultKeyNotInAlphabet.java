// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.model.algorithm;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.util.input.InputVerificationResult;

public class InputVerificationResultKeyNotInAlphabet extends
		InputVerificationResult {

	public static final String RESULT_TYPE_KEY_NOT_IN_ALPHABET = "KEY_NOT_IN_ALPHABET";
	private Character erroneusChar;
	private AbstractAlphabet alphabet;
	private int position;

	public InputVerificationResultKeyNotInAlphabet(Character erroneusChar, AbstractAlphabet alphabet, int position) {
		super();
		this.erroneusChar = erroneusChar;
		this.alphabet = alphabet;
		this.position = position;
	}

	@Override
	public boolean isStandaloneMessage() {
		return false;
	}
	@Override
	public MessageType getMessageType() {
		return InputVerificationResult.MessageType.WARNING;
	}
	@Override
	public String getMessage() {
		return String.format(Messages.AbstractClassicCryptoPage_reason_notinalphabet, erroneusChar.toString());
	}
	@Override
	public boolean isValid() {
		return false;
	}
	@Override
	public String toString() {return "NOALPHABETKEY_FAIL";} //$NON-NLS-1$
	@Override
	public Object getResultType() {
		return RESULT_TYPE_KEY_NOT_IN_ALPHABET;
	}

	public int getPosition() {
		return position;
	}

	public Character getErroneusChar() {
		return erroneusChar;
	}

	public AbstractAlphabet getAlphabet() {
		return alphabet;
	};

}
