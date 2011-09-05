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

/**
 * Decryption of the fully homomorphic encryption scheme by Gentry & Halevi
 * @author Coen Ramaekers
 *
 */
public class GHDecrypt {
	/**
	 * Decrypts the ciphertext c
	 * @param fheparams the paramters of the scheme
	 * @param det the lattice determinant
	 * @param c the ciphertext
	 * @param w the secret key
	 * @return The decryption of c
	 */
	public static int decrypt(FHEParams fheparams, BigInteger det, BigInteger c, BigInteger w) {
		int temp;
		BigInteger e = c.multiply(w).mod(det);
		temp = (e.testBit(0) ? 1 : 0);
		e = e.shiftLeft(1);
		if (e.compareTo(det) > 0) temp^= 1;
		return temp;
	}

	/**
	 * Decrypts the ciphertext array c
	 * @param fheparams the paramters of the scheme
	 * @param det the lattice determinant
	 * @param c the ciphertext
	 * @param w the secret key
	 * @return The decryption array of c
	 */
	public static int[] decrypt(FHEParams fheparams, BigInteger det, BigInteger[] c, BigInteger w) {
		int[] temp = new int[c.length];
		for (int i = 0; i < c.length; i++) {
			temp[i] = decrypt(fheparams, det, c[i], w);
		}
		return temp;
	}
}
