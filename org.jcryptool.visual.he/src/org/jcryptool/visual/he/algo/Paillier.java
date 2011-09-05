// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.algo;

import java.math.BigInteger;
import java.util.Random;

/**
 * Class implementing the Paillier cryptosystem
 * @author Coen Ramaekers
 *
 */
public class Paillier {
	/** the first prime */
	private static BigInteger p;

	/** the second prime */
	private static BigInteger q;

	/** the product of the two primes, part of public key */
	private static BigInteger n;

	/** the least common multiple of p-1 and q-1, part of secret key */
	private static BigInteger l;

	/** random integer, part of public key */
	private static BigInteger g;

	/** part of secret key */
	private static BigInteger mu;

	/** random generator */
	private static Random rnd;

	/**
	 * Generates a key pair for the Paillier cryptosystem.
	 * @param data will be used to store the key
	 */
	public static void keyGen(PaillierData data) {
		rnd = new Random(System.currentTimeMillis());
		int s = data.getS();
		p = new BigInteger(s, 10, rnd);
		q = new BigInteger(s, 10, rnd);
		n = p.multiply(q);
		l = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		g = new BigInteger(2*s, rnd);
		while ((g.gcd(n.pow(2)).compareTo(BigInteger.ONE) != 0) &&
				n.gcd((g.modPow(l, n.pow(2)).subtract(BigInteger.ONE).divide(n))).compareTo(BigInteger.ONE)!= 0) {
			g = new BigInteger(2*s, rnd);
		}
		mu = (g.modPow(l, n.pow(2)).subtract(BigInteger.ONE).divide(n)).modInverse(n);
		data.setPubKey(n, g);
		data.setPrivKey(l, mu);
		data.set = true;
	}

	/**
	 * Will encrypt the plaintext given in the data.
	 * @param data holds the plaintext and keypair, will hold the encryption
	 */
	public static void encrypt(PaillierData data) {
		rnd = new Random(System.currentTimeMillis());
		BigInteger[] pubKey = data.getPubKey();
		BigInteger r = new BigInteger(pubKey[0].bitLength(), rnd);
		while (r.gcd(pubKey[0]).compareTo(BigInteger.ONE) != 0) {
			r = new BigInteger(pubKey[0].bitLength(), rnd);
		}
		data.setCipher(pubKey[1].modPow(data.getPlain(), pubKey[0].pow(2))
				.multiply(r.modPow(pubKey[0], pubKey[0].pow(2))).mod(pubKey[0].pow(2)));
	}

	/**
	 * Will encrypt the operation number given in the data
	 * @param data holds the operation number and keypair, will hold the encryption of the operation
	 */
	public static void encryptOperation(PaillierData data) {
		rnd = new Random(System.currentTimeMillis());
		BigInteger[] pubKey = data.getPubKey();
		BigInteger r = new BigInteger(pubKey[0].bitLength(), rnd);
		while (r.gcd(pubKey[0]).compareTo(BigInteger.ONE) != 0) {
			r = new BigInteger(pubKey[0].bitLength(), rnd);
		}
		data.setOperationCipher(pubKey[1].modPow(data.getOperation(), pubKey[0].pow(2))
				.multiply(r.modPow(pubKey[0], pubKey[0].pow(2))).mod(pubKey[0].pow(2)));
	}

	/**
	 * Will decrypt the cipher given in the data
	 * @param data holds the cipher and keypair, will hold the decryption
	 */
	public static void decrypt(PaillierData data) {
		BigInteger[] pubKey = data.getPubKey();
		BigInteger[] privKey = data.getPrivKey();
		data.setPlain((data.getCipher().modPow(privKey[0], pubKey[0].pow(2)).subtract(BigInteger.ONE))
				.divide(pubKey[0]).multiply(privKey[1]).mod(pubKey[0]));
	}

	/**
	 * Will decrypt the result given in the data
	 * @param data holds the cipher and keypair, will hold the decryption
	 */
	public static void decryptResult(PaillierData data) {
		BigInteger[] pubKey = data.getPubKey();
		BigInteger[] privKey = data.getPrivKey();
		data.setResultPlain((data.getResultCipher().modPow(privKey[0], pubKey[0].pow(2)).subtract(BigInteger.ONE))
				.divide(pubKey[0]).multiply(privKey[1]).mod(pubKey[0]));
	}
}
