package org.jcryptool.visual.hashing.algorithm;

import java.math.BigInteger;
import java.util.Random;

/**
 * 
 * @author Ferit Dogan
 * 
 */
public class Hashing {
	protected int dim; /* number elements */
	protected BigInteger[] privateKey; /* private key set */
	protected BigInteger[] publicKey; /* public key set */
	private BigInteger M; /* modulus */
	private BigInteger W; /* weight */
	private BigInteger U; /* U * W = 1 (mod M) */

	public Hashing(int n) {
		this.dim = n;
		this.privateKey = new BigInteger[n];
		this.publicKey = new BigInteger[n];

		for (int i = 0; i < this.dim; i++) {
			this.privateKey[i] = BigInteger.ZERO;
			this.publicKey[i] = BigInteger.ZERO;
		}
	}

	public BigInteger getM() {
		return this.M;
	}

	public BigInteger getW() {
		return this.W;
	}

	public BigInteger getU() {
		return this.U;
	}

	public void setM() {
		/* t is the bit length of last element in super increasing sequence */
		BigInteger t = BigInteger.valueOf(this.privateKey[dim - 1].bitLength());
		t = BigInteger.ONE.shiftLeft(t.intValue());

		/* choose M to be a 2^t bit number with M > SUM(Ai) */
		this.M = randomNumber(getSum().add(BigInteger.ONE).max(t), (t.shiftLeft(1).subtract(BigInteger.ONE)));
	}

	/* get sum of all elements */
	public BigInteger getSum() {
		BigInteger sum = BigInteger.ZERO;

		for (int i = 0; i < dim; i++)
			sum = sum.add(this.privateKey[i]);

		return sum;
	}

	public void setW() {
		if (this.M.compareTo(BigInteger.ZERO) == 0)
			setM();

		BigInteger w = randomNumber(new BigInteger("2"), this.M.subtract(new BigInteger("2")));
		BigInteger d = w.gcd(this.M);

		while (d.compareTo(BigInteger.ONE) != 0) {
			w = w.divide(d);
			d = w.gcd(this.M);
		}

		this.W = w.divide(w.gcd(this.M));
		this.U = w.modInverse(this.M);
	}

	public void setW(BigInteger w) {
		this.W = w;
		this.U = W.modInverse(M);
	}

	public void setM(BigInteger m) {
		this.M = m;
	}

	public void createPrivateKeys(BigInteger startVal) {
		this.getSuperInc(startVal);
		this.setM();
		this.setW();

	}

	public void getSuperInc(BigInteger start) {
		if (start.compareTo(BigInteger.ZERO) <= 0)
			return;

		/* t is the bit length of start */
		BigInteger t = BigInteger.valueOf(start.bitLength());
		t = BigInteger.ONE.shiftLeft(t.intValue());

		this.privateKey[0] = start;
		BigInteger sum = getPrivateKeyElement(0);

		/* choose set[i] to be a (t+i)-bit number so that set[i] > sum */
		for (int i = 1; i < dim; i++) {
			this.privateKey[i] = randomNumber(sum.add(BigInteger.ONE).max(t),
					(t.shiftLeft(1).subtract(BigInteger.ONE)));
			sum = sum.add(this.getPrivateKeyElement(i));

			t = t.shiftLeft(1);
		}
	}

	public void createPublicKeys() {
		for (int i = 0; i < dim; i++) {
			setPublicKeyElement(i, this.W.multiply(this.privateKey[i]).mod(this.M));
		}
	}

	public BigInteger getPrivateKeyElement(int i) {
		return this.privateKey[i];
	}

	public BigInteger getPublicKeyElement(int i) {
		return this.publicKey[i];
	}

	private void setPublicKeyElement(int i, BigInteger m) {
		this.publicKey[i] = m;

	}

	public BigInteger encrypt(BigInteger plain) {
		BigInteger chiffre = BigInteger.ZERO;
		int n = this.dim - 1;
		BigInteger s = BigInteger.ONE;
		s = s.shiftLeft(n);

		boolean b;
		for (int i = 0; i < this.dim; i++) {
			b = false;

			if (plain.and(s).compareTo(BigInteger.ZERO) != 0)
				b = true;

			if (b) {
				chiffre = chiffre.add(this.getPublicKeyElement(i));
			}

			s = s.shiftRight(1);
		}

		return chiffre;
	}

	public static BigInteger randomNumber(BigInteger min, BigInteger max) {
		if (max.compareTo(min) < 0) {
			BigInteger tmp = min;
			min = max;
			max = tmp;
		} else if (max.compareTo(min) == 0) {
			return min;
		}
		max = max.add(BigInteger.ONE);
		BigInteger range = max.subtract(min);
		int length = range.bitLength();
		BigInteger result = new BigInteger(length, new Random());
		while (result.compareTo(range) >= 0) {
			result = new BigInteger(length, new Random());
		}
		result = result.add(min);
		return result;
	}

	public void updatePrivateKey(BigInteger[] keys) {
		this.privateKey = keys;
		this.setM();
		this.setW();
	}

	public void updatePrivateKeyOnly(BigInteger[] keys) {
		this.privateKey = keys;
	}
		
}