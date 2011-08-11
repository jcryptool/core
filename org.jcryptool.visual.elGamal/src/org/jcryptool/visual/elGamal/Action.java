/**
 * 
 */
package org.jcryptool.visual.elGamal;

import java.math.BigInteger;

import org.jcryptool.visual.library.Constants;

/**
 * possible actions, including a run method.
 * 
 * @author Michael Gaber
 */
public enum Action {

    /** encryption. */
    EncryptAction {
        @Override
        public String run(final ElGamalData data, final String... words) {
            final StringBuilder rv = new StringBuilder(words.length * 3);
            final BigInteger modulus = data.getModulus();
            final BigInteger multiplicand = data.getPublicA().modPow(data.getB(), modulus);
            for (final String word : words) {
                rv.append(multiplicand.multiply(new BigInteger(word, Constants.HEXBASE)).mod(modulus)
                        .toString(Constants.HEXBASE));
                rv.append(" "); //$NON-NLS-1$
            }
            rv.delete(rv.length() - 1, rv.length() - 1);
            return rv.toString();
        }
    },

    /** decryption. */
    DecryptAction {
        @Override
        public String run(final ElGamalData data, final String... words) {
            final StringBuilder rv = new StringBuilder(words.length);
            final BigInteger modulus = data.getModulus();
            final BigInteger x = modulus.subtract(BigInteger.ONE).subtract(data.getA());
            final BigInteger multiplicand = data.getGPowB().modPow(x, modulus);
            for (final String word : words) {
                rv.append((char) multiplicand.multiply(new BigInteger(word, Constants.HEXBASE)).mod(modulus).intValue());
            }
            return rv.toString();
        }
    },

    /** signature. */
    SignAction {
        @Override
        public String run(final ElGamalData data, final String... words) {
            final BigInteger modulus = data.getModulus();
            final BigInteger r = data.getGenerator().modPow(data.getK(), modulus);
            data.setR(r);
            final BigInteger s = new BigInteger(words[0], Constants.HEXBASE).subtract(data.getA().multiply(r))
                    .multiply(data.getK().modInverse(modulus.subtract(BigInteger.ONE)))
                    .mod(modulus.subtract(BigInteger.ONE));
            return s.toString(Constants.HEXBASE);
        }
    },

    /** verification. */
    VerifyAction {
        @Override
        public String run(final ElGamalData data, final String... words) {
            final String[] rs = data.getSignature().substring(1, data.getSignature().length() - 1).split(","); //$NON-NLS-1$
            final BigInteger r = new BigInteger(rs[0], Constants.HEXBASE);
            data.setR(r);
            final BigInteger s = new BigInteger(rs[1].trim(), Constants.HEXBASE);
            final BigInteger modulus = data.getModulus();
            final BigInteger result = data.getPublicA().modPow(r, modulus).multiply(r.modPow(s, modulus)).mod(modulus);
            return result.toString(Constants.HEXBASE);
        }
    };

    /**
     * runs the action on the specified array of hex-values.
     * 
     * @param words the "words"
     * @return a string containing all words "translated" separated with spaces
     */
    public abstract String run(ElGamalData data, final String... words);
}
