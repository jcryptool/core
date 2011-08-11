/**
 * 
 */
package org.jcryptool.visual.he.rsa;

import java.math.BigInteger;

import org.jcryptool.visual.library.Constants;

/**
 * possible actions, including a run method.
 * 
 * @author Michael Gaber
 */
public enum Action {

	/** decryption. */
	DecryptAction,

	/** encryption. */
	EncryptAction,

	/** signature. */
	SignAction,

	/** verification. */
	VerifyAction;

	/**
	 * runs the action on the specified array of hex-values.
	 * 
	 * @param words
	 *            the "words"
	 * @param exponent
	 *            the exponent to use
	 * @param n
	 *            the modul to use
	 * @return a string containing all words "translated" separated with spaces
	 */
	public String run(final String[] words, final BigInteger exponent,
			final BigInteger n) {
		final StringBuilder sb = new StringBuilder();
		BigInteger number;
		for (final String word : words) {
			number = new BigInteger(word, Constants.HEXBASE);
			if (this == DecryptAction) {
				sb.append((char) number.modPow(exponent, n).intValue());
			} else {
				sb.append(number.modPow(exponent, n).toString(Constants.HEXBASE));
				sb.append(' ');
			}
		}
		return sb.toString();
	}
}
