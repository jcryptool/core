/**
 * 
 */
package org.jcryptool.visual.dsa;

import java.math.BigInteger;

import org.jcryptool.visual.library.Constants;

/**
 * possible actions, including a run method.
 * @author Michael Gaber
 */
public enum Action {

	/** signature. */
	SignAction {
		@Override
		public String run(final DSAData data, final String... words) {
			final BigInteger r = data.getGenerator().modPow(data.getK(), data.getP()).mod(data.getQ());
			data.setR(r);
			final BigInteger s = data.getK().modInverse(data.getQ()).multiply(
					new BigInteger(words[0], Constants.HEXBASE).add(data.getX().multiply(r))).mod(data.getQ());
			return s.toString(Constants.HEXBASE);
		}
	},

	/** verification. */
	VerifyAction {
		@Override
		public String run(final DSAData data, final String... words) {
			final String[] rs = data.getSignature().substring(1, data.getSignature().length() - 1).split(","); //$NON-NLS-1$
			final BigInteger r = new BigInteger(rs[0], Constants.HEXBASE);
			data.setR(r);
			final BigInteger s = new BigInteger(rs[1].trim(), Constants.HEXBASE);
			final BigInteger p = data.getP();
			final BigInteger q = data.getQ();
			final BigInteger w = s.modInverse(q);
			final BigInteger u1 = new BigInteger(words[0], Constants.HEXBASE).multiply(w).mod(q);
			final BigInteger u2 = r.multiply(w).mod(q);
			final BigInteger v = data.getGenerator().modPow(u1, p).multiply(data.getY().modPow(u2, p)).mod(p).mod(q);
			return v.toString(Constants.HEXBASE);
		}
	};

	/**
	 * runs the action on the specified array of hex-values.
	 * @param words the "words"
	 * @return a string containing all words "translated" separated with spaces
	 */
	public abstract String run(DSAData data, final String... words);
}
