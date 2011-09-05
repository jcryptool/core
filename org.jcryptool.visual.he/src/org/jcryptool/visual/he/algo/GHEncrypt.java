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
 * Encryption of the fully homomorphic encryption scheme by Gentry & Halevi
 * Based on the C code of Gentry & Halevi, see https://researcher.ibm.com/researcher/view_project.php?id=1579
 * @author Coen Ramaekers
 *
 */
public class GHEncrypt {

	/**
	 * Encrypts the plain bit b
	 * @param fheparams the scheme parameters
	 * @param key the key
	 * @param b the plain bit
	 * @return Encryption of b
	 */
	public static BigInteger encrypt(FHEParams fheparams, GHKeyGen key, int b) {
		int i, num = 1;
		int[] bit = new int[1];
		bit[0] = b;
		long n = 1<<(fheparams.logn); // the dimension
		double p = ((double)fheparams.noise)/n;  // # of expected nonzero coefficients
		if (p>0.5) p = 0.5;

		// Evaluate all the polynomials together at root mod det
		BigInteger[] vals = evalRandPoly(num, n, p, key.root, key.det);
		BigInteger[] out = new BigInteger[num];
		for (i = 0; i < num; i++) {
			out[i] = vals[i+1];
		}
		// Set c[i] = 2*c[i] + b[i]
		for (i=0; i<num; i++) {
			out[i] = out[i].shiftLeft(1);
			out[i] = out[i].add(new BigInteger(Integer.toString(bit[i])));
			if (out[i].compareTo(key.det) >= 0) out[i] = out[i].subtract(key.det);
		}
		return out[0];
	}

	/**
	 * Encrypts a bit array
	 * @param fheparams the scheme parameters
	 * @param key the key
	 * @param b the bit array
	 * @return An array containing the elementwise encryption of b
	 */
	public static BigInteger[] encrypt(FHEParams fheparams, GHKeyGen key, int[] b) {
		int i, num = b.length;
		long n = 1<<(fheparams.logn); // the dimension
		double p = ((double)fheparams.noise)/n;  // # of expected nonzero coefficients
		if (p>0.5) p = 0.5;

		// Evaluate all the polynomials together at root mod det
		BigInteger[] vals = evalRandPoly(num, n, p, key.root, key.det);
		BigInteger[] out = new BigInteger[num];
		for (i = 0; i < num; i++) {
			out[i] = vals[i+1];
		}
		// Set out[i] = 2*out[i] + b[i]
		for (i=0; i<num; i++) {
			out[i] = out[i].shiftLeft(1);
			out[i] = out[i].add(new BigInteger(Integer.toString(b[i])));
			if (out[i].compareTo(key.det) >= 0) out[i] = out[i].subtract(key.det);
		}
		return out;
	}

	/**
	 * Encrypts the plain bit b
	 * @param fheparams the scheme parameters
	 * @param key the keypair
	 * @param b the plain bit
	 * @return Encryption of b
	 */
	public static BigInteger encrypt(FHEParams fheparams, GHKeyPair key, int b) {
		int i, num = 1;
		int[] bit = new int[1];
		bit[0] = b;
		long n = 1<<(fheparams.logn); // the dimension
		double p = ((double)fheparams.noise)/n;  // # of expected nonzero coefficients
		if (p>0.5) p = 0.5;

		// Evaluate all the polynomials together at root mod det
		BigInteger[] vals = evalRandPoly(num, n, p, key.root, key.det);
		BigInteger[] out = new BigInteger[num];
		for (i = 0; i < num; i++) {
			out[i] = vals[i+1];
		}
		// Set c[i] = 2*c[i] + b[i]
		for (i=0; i<num; i++) {
			out[i] = out[i].shiftLeft(1);
			out[i] = out[i].add(new BigInteger(Integer.toString(bit[i])));
			if (out[i].compareTo(key.det) >= 0) out[i] = out[i].subtract(key.det);
		}
		return out[0];
	}

	/**
	 * Encrypts a bit array
	 * @param fheparams the scheme parameters
	 * @param key the keypair
	 * @param b the bit array
	 * @return An array containing the elementwise encryption of b
	 */
	public static BigInteger[] encrypt(FHEParams fheparams, GHKeyPair key, int[] b) {
		int i, num = b.length;
		long n = 1<<(fheparams.logn); // the dimension
		double p = ((double)fheparams.noise)/n;  // # of expected nonzero coefficients
		if (p>0.5) p = 0.5;

		// Evaluate all the polynomials together at root mod det
		BigInteger[] vals = evalRandPoly(num, n, p, key.root, key.det);
		BigInteger[] out = new BigInteger[num];
		for (i = 0; i < num; i++) {
			out[i] = vals[i+1];
		}
		// Set out[i] = 2*out[i] + b[i]
		for (i=0; i<num; i++) {
			out[i] = out[i].shiftLeft(1);
			out[i] = out[i].add(new BigInteger(Integer.toString(b[i])));
			if (out[i].compareTo(key.det) >= 0) out[i] = out[i].subtract(key.det);
		}
		return out;
	}

	/**
	 * Evaluates n random polynomials at root mod det, coefficients are 1, -1 with respective probability p/2
	 * and 0 with probability 1-p. Splits up the evaluation (into two parts) if the number of polynomials
	 * is large enough with respect to the degree, i.e. n+1+(m&1) < m/2.
	 * @param n the number of polynomials
	 * @param m the degree
	 * @param p the coefficient probability
	 * @param root the lattice root
	 * @param det the lattice determinant
	 * @return An array containing the n evaluations.
	 */
	public static BigInteger[] evalRandPoly(int n, long m, double p, BigInteger root, BigInteger det) {
		BigInteger[] vals;
		if ((n+1+(m&1) < m/2)) {
			double q;
			vals = evalRandPoly(2*n, m/2, p, root, det); // returns {root^{m/2},c0,c1,...}
		    for (int i = 1; i <= n; i++) { // vals[i] += root^{m/2} * vals[i+n] mod det
		    	// If m is odd then add another random 0/1 coefficient
		    	if (((m&1) == 1) && ((q = Math.random()) < p)) {
		    		vals[i+n] = ((q < p/2) ? vals[i+n].add(vals[0]).mod(det) : vals[i+n].subtract(vals[0]).mod(det));
		    	}
		    	BigInteger tmp = vals[i+n].multiply(vals[0]).mod(det); // multiply "top half" by root^{d/2}
		    	vals[i] = vals[i].add(tmp);
		    }

		    vals[0] = vals[0].modPow(new BigInteger("2"), det); // compute root^m for the next level
		    if ((m&1) == 1) vals[0] = vals[0].multiply(root).mod(det); // if m is odd, multiply by r again
		} else {
			vals = basicRandPoly(n, m, p, root, det);
		}
		return vals;
	}

	/**
	 * Evaluates n random polynomials at root mod det, coefficients are 1, -1 with respective probability p/2
	 * and 0 with probability 1-p.
	 * @param n the number of polynomials
	 * @param m the degree
	 * @param p the coefficient probability
	 * @param root the lattice root
	 * @param det the lattice determinant
	 * @return An array containing the n evaluations.
	 */
	public static BigInteger[] basicRandPoly(int n, long m, double p, BigInteger root, BigInteger det) {
		BigInteger[] vals = new BigInteger[n+1];;
		int i,j,k;
		if (m <= 0) {
			vals[0] = new BigInteger("1");
			return vals;
		}
		double q;
		for (i = 1; i <= n; i++) vals[i] = new BigInteger(Integer.toString((((q = Math.random()) < p) ? ((q < p/2) ? -1 : 1) : 0 )));
		if (m==1) {
			vals[0] = root;
			return vals;
		}

		BigInteger rSqr = root.modPow(new BigInteger("2"), det); // holds the value root^2 mod det
		BigInteger rPowm;
		// Handle the powers 1,2,4,... separately (saves maybe 1-2 mults)
		for (i = 1; i <= n; i++) {
			if ((q = Math.random()) < p) {
				// add r (no need for modular reduction) or subtract r mod det
				vals[i] = (q < p/2) ? vals[i].add(root) : vals[i].subtract(root).mod(det);
			}
			if (m > 2 && ((q = Math.random()) < p)) {
				// add or subtract root^2
				vals[i] = (q < p/2) ? vals[i].add(rSqr).mod(det) : vals[i].subtract(rSqr).mod(det);
			}
		}
		if (m>4) {
			rPowm = rSqr;
			for (j = 4; j < m; j *= 2) {
				rPowm = rPowm.modPow(new BigInteger("2"), det); // r^j mod det
				for (i = 1; i <= n; i++) {
					if ((q = Math.random()) < p) {
						// add or subtract the correct power of root mod det
						vals[i] = (q < p/2) ? vals[i].add(rPowm).mod(det) : vals[i].subtract(rPowm).mod(det);
					}
				}
			}
		} else if (m<4) { // if m==2 or 3 we're done, just return the correct r_to_m
			vals[0] = ((m == 2) ? rSqr : rSqr.multiply(root).mod(det));
			return vals;
		}

		// Handle all the other powers of r
		// compute r^j,r^{2j},r^{4j},..., and add to all values
		BigInteger rOddPow = root;
		for (j = 3; j < m; j += 2) {
			rOddPow = rOddPow.multiply(rSqr).mod(det); // next odd power of r
			rPowm = rOddPow;
			k = j;
			while (true) {
				for (i = 1; i <= n; i++)
					if ((q = Math.random()) < p) {
						// add or subtract the correct power of root mod det
						vals[i] = (q < p/2) ? vals[i].add(rPowm).mod(det) : vals[i].subtract(rPowm).mod(det);
					}
				k *= 2;
				if (k >= m) break;
				rPowm = rPowm.modPow(new BigInteger("2"), det); // r^k := (previous-r^k)^2 mod det
			}
		}

		// r_odd_power is r^{m-1} or r^{m-2}, depending  on whether m is even or odd
		vals[0] = (((m&1) == 1) ? rOddPow.multiply(rSqr).mod(det) : rOddPow.multiply(root).mod(det));
		return vals;
	}
}
