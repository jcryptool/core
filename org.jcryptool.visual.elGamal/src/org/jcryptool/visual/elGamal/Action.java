/**
 * 
 */
package org.jcryptool.visual.elGamal;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.jcryptool.core.logging.utils.LogUtil;

/**
 * possible actions, including a run method.
 * 
 * @author Michael Gaber
 */
public enum Action {

	/** encryption. */
	EncryptAction,

	/** decryption. */
	DecryptAction,

	/** signature. */
	SignAction,

	/** verification. */
	VerifyAction;


	/**
	 * Returns a single value in a list. Voraussichtlich für die Schrittweise
	 * berechnung gedacht. Nachteil: Die begrenzung der Werte auf int (BigInteger
	 * ist größer) -> keine 1024 bit keys möglich.
	 * 
	 * @param data
	 * @param words
	 *            Die Einzelnen Zeichen die verschlüsselt werden sollen
	 * @return Irgendwas auf Basis 10. If something went wrong an Exception is
	 *         thrown and the result will be empty.
	 */
	public List<Integer> run(ElGamalData data, List<Integer> words) {
		List<Integer> result = new ArrayList<>();
		int modulus, publicB, k, s;
		try {

			switch (data.getAction()) {
			case EncryptAction:

				modulus = data.getModulus().intValue();
				publicB = data.getPublicA().intValueExact();
				// Aus irgendeinem Grund wird das k nicht in k, sondern in b gespeichert. Sollte
				// mal untersucht werden.
				k = data.getB().intValue();

				for (Integer word : words) {
					result.add((int) (Math.pow(publicB, k) * word % modulus));
				}

				break;

			case DecryptAction:

				modulus = data.getModulus().intValue();
				int multiplicand = data.getGPowB().modPow(data.getModulus().subtract(BigInteger.ONE).subtract(data.getA()), data.getModulus()).intValue();

				for (Integer word : words) {
					result.add(multiplicand * word % modulus);
				}

				break;
			case SignAction:

				modulus = data.getModulus().intValue();
				BigInteger tempR = data.getGenerator().modPow(data.getK(), data.getModulus());
				data.setR(tempR);
				int inverseK = data.getK().modInverse(data.getModulus().subtract(BigInteger.ONE)).intValue();
				s = ((words.get(0) - (data.getA().intValue() * tempR.intValue())) * inverseK) % (modulus - 1);
				if (s < 0) {
					s = (modulus - 1) + s;
				}
				result.add(s);

				break;
			case VerifyAction:

				modulus = data.getModulus().intValue();
				BigInteger bi_r = BigInteger.valueOf(data.getSignatureAsNumbers().get(0));
				BigInteger bi_s = BigInteger.valueOf(data.getSignatureAsNumbers().get(1));
				data.setR(bi_r);
				BigInteger tempRes = data.getPublicA().modPow(bi_r, data.getModulus()).multiply(bi_r.modPow(bi_s, data.getModulus())).mod(data.getModulus());
				result.add(tempRes.intValue());
				break;
			default:
				break;
			}

		} catch (NumberFormatException nfe) {
			LogUtil.logError(ElGamalPlugin.PLUGIN_ID, nfe);
		} catch (Exception e) {
			LogUtil.logError(ElGamalPlugin.PLUGIN_ID, e);
		}

		return result;

	}
}
