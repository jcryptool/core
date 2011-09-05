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
 * Class handling polynomial operations for the fully homomorphic encryption scheme by Gentry & Halevi
 * @author Coen Ramaekers
 *
 */
public class Polynomial  {
	/**holds the degree */
	public int degree;

	/** holds the coefficients, 0 for x^0, etc.*/
	public BigInteger[] coeffs;

	/** random generator */
	public static Random r = new Random(System.currentTimeMillis());

	/**
	 * Creates a new polynomial with all coefficients 0
	 * @param deg the degree
	 */
	public Polynomial(int deg) {
		degree = deg;
		coeffs = new BigInteger[deg+1];
		for (int i = 0; i <= deg; i++) {
			coeffs[i] = new BigInteger("0");
		}
	}

	/**
	 * Creates a polynomial with the given coefficients, the degree is deduced from the coefficient array
	 * @param coeffs the coefficients
	 */
	public Polynomial(BigInteger[] coeffs) {
		this.coeffs = new BigInteger[coeffs.length];
		this.degree = coeffs.length-1;
		for (int i = 0; i < coeffs.length; i++) {
			this.coeffs[i] = coeffs[i];
		}
	}

	/**
	 * Changes the coefficients and if necessary adapts the degree
	 * @param coeffs the new coefficients
	 */
	public void setCoeffs(BigInteger[] coeffs) {
		if (degree != coeffs.length-1) {
			this.coeffs = new BigInteger[coeffs.length];
			this.degree = coeffs.length-1;
		}
		for (int i = 0; i < coeffs.length; i++) {
			this.coeffs[i] = coeffs[i];
		}
	}

	/**
	 * Change a single coefficient
	 * @param i the coefficient index
	 * @param c the new coefficient
	 */
	public void setCoeff(int i, BigInteger c) {
		if (i <= degree+1) {
			this.coeffs[i] = c;
		}
	}

	/**
	 * Gets rid of leading zero coeffients, adapts degree
	 */
	public void normalize() {
		if (coeffs[degree].equals(new BigInteger("0"))) {
			int i = degree;
			while (coeffs[i].equals(new BigInteger("0"))) {
				i--;
			}
			BigInteger[] temp = new BigInteger[i+1];
			for (int j = 0; j <= i; j++) {
				temp[j] = coeffs[j];
			}
			coeffs = new BigInteger[i+1];
			for (int j = 0; j <= i; j++) {
				coeffs[j] = temp[j];
			}
			degree = i;
		}
	}

	/**
	 * Generate random polynomial with coefficients in the range [-2^bitsize,2^bitsize]
	 * @param deg the polynomial degree
	 * @param bitsize the coefficient bitsize
	 * @return The new polynomial
	 */
	public static Polynomial randomPolynomial(int deg, int bitsize) {
		BigInteger[] cffs = new BigInteger[deg+1];
		byte[] test;
		for (int i = 0; i < deg; i++) {
			test = new byte[bitsize/8];
			r.nextBytes(test);
			cffs[i] = new BigInteger(test);
			//if (i == 0 & !cffs[i].testBit(0)) cffs[i] = cffs[i].add(BigInteger.ONE);
			//if (i != 0 & cffs[i].testBit(0)) cffs[i] = cffs[i].add(BigInteger.ONE);
		}
		BigInteger temp = new BigInteger("0");
		do {
			test = new byte[bitsize/8];
			r.nextBytes(test);
			temp = new BigInteger((new BigInteger(test)).toString());
		} while (temp.compareTo(new BigInteger("0")) == 0);
		//if (temp.testBit(0)) temp = temp.add(BigInteger.ONE);
		cffs[deg] = temp;
		Polynomial p = new Polynomial(cffs);
		return p;
	}

	/**
	 * Add two polynomials
	 * @param p the first polynomial
	 * @param q the second polynomial
	 * @return The sum of the parameter polynomials
	 */
	public static Polynomial add(Polynomial p, Polynomial q) {
		int deg = Math.max(p.degree, q.degree);
		boolean equal = false, pmax = false;
		if (p.degree == deg) {
			if (q.degree == deg) {
				equal = true;
			} else {
				pmax = true;
			}
		}
		Polynomial r = new Polynomial(deg);
		for (int i = 0; i <= (equal ? deg : (pmax ? q.degree : p.degree)); i++) {
			r.coeffs[i] = q.coeffs[i].add(p.coeffs[i]);
		}
		if (!equal) {
			if (pmax) {
				for (int i = q.degree+1; i <= p.degree; i++) {
					r.coeffs[i] = p.coeffs[i];
				}
			} else {
				for (int i = p.degree+1; i <= q.degree; i++) {
					r.coeffs[i] = q.coeffs[i];
				}
			}
		}
		r.normalize();
		return r;
	}

	/**
	 * Subtract two polynomials
	 * @param p the positive polynomial
	 * @param q the negative polynomial
	 * @return p-q
	 */
	public static Polynomial substract(Polynomial p, Polynomial q) {
		Polynomial r = negate(q);
		Polynomial s = add(p,r);
		return s;
	}

	/**
	 * Negates a polynomial
	 * @param p
	 * @return -p
	 */
	public static Polynomial negate(Polynomial p) {
		Polynomial q = new Polynomial(p.degree);
		for (int i = 0; i <= p.degree; i++) {
			q.coeffs[i] = p.coeffs[i].negate();
		}
		return q;
	}

	/**
	 * Multiplies to polynomials
	 * @param p the first polynomial
	 * @param q the second polynomial
	 * @return p*q
	 */
	public static Polynomial mult(Polynomial p, Polynomial q) {
		int deg = p.degree + q.degree;
		BigInteger temp = new BigInteger("0");
		Polynomial r = new Polynomial(deg);

		for (int i = 0; i <= deg; i++) {
			temp = new BigInteger("0");
			for (int j = 0; j <= i; j++) {
				if (j > p.degree) break;
				if ((i-j) > q.degree) continue;
				temp = temp.add(p.coeffs[j].multiply(q.coeffs[i-j]));
			}
			r.coeffs[i] = temp;
		}
		return r;
	}

	/**
	 * Multiplies a polynomial with a factor
	 * @param p the polynomial
	 * @param q the factor
	 * @return q*p(x)
	 */
	public static Polynomial mult(Polynomial p, BigInteger q) {
		Polynomial r = new Polynomial(p.degree);
		for (int i = 0; i <= r.degree; i++) {
			r.coeffs[i] = q.multiply(p.coeffs[i]);
		}
		return r;
	}

	/**
	 * Multiplies a polynomial with a power of x
	 * @param p the polynomial
	 * @param q the power of x
	 * @return x^q * p(x)
	 */
	public static Polynomial xmult(Polynomial p, int q) {
		if (q == 0) {
			return p;
		}
		Polynomial r = new Polynomial(q);
		for (int i = 0; i < q; i++) {
			r.coeffs[i] = new BigInteger("0");
		}
		r.coeffs[q] = new BigInteger("1");
		Polynomial s = Polynomial.mult(p,r);
		return s;
	}

	/**
	 * Calculates p mod q, only allows integer modular arithmetic this is fine for our purposes,
	 * since it is always modulo x^(2^m)+1
	 * @param p the polynomial
	 * @param q the modulo
	 * @return p mod q
	 */
	public static Polynomial mod(Polynomial p, Polynomial q) {
		if (p.degree < q.degree) {
			return p;
		}
		int temp = p.degree - q.degree;
		BigInteger factor = p.coeffs[p.degree].divide(q.coeffs[q.degree]);
		Polynomial s = Polynomial.substract(p,Polynomial.mult(Polynomial.xmult(q,temp),factor));
		s.normalize();
		Polynomial r = mod(s,q);
		return r;
	}
}
