package org.jcryptool.crypto.classic.model.algorithm;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.util.input.InputVerificationResult;

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
	
	public boolean isStandaloneMessage() {
		return false;
	}
	public int getMessageType() {
		return InputVerificationResult.WARNING;
	}
	public String getMessage() {
		return String.format(Messages.AbstractClassicCryptoPage_reason_notinalphabet, erroneusChar.toString());
	}
	public boolean isValid() {
		return false;
	}
	public String toString() {return "NOALPHABETKEY_FAIL";} //$NON-NLS-1$
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
