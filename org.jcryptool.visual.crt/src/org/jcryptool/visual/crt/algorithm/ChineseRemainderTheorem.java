// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crt.algorithm;

import java.math.BigInteger;

import org.jcryptool.visual.xeuclidean.algorithm.XEuclid;

/**
 *
 * @author Oryal Inel
 * @version 1.0.0
 */
public class ChineseRemainderTheorem {
	private XEuclid xeuclid;
	private BigInteger modulus;
	private BigInteger multipliers[];
	private BigInteger a[];
	private BigInteger bigM[];
	private BigInteger inverse[];
	private BigInteger result[];
	private BigInteger finalResult;
	private BigInteger moduli[];

	/**
	 * constructor
	 */
	public ChineseRemainderTheorem() {
		xeuclid = new XEuclid();
	}

	/**
	 * the chinese remainder theorem algorithm x = a mod m
	 * @param moduli an array of moduli m
	 * @param a an array of arguments a
	 * @return the x for all simultaneous congruence
	 */
	public BigInteger crt(BigInteger moduli[], BigInteger a[]) {
		this.a = a;
		this.moduli = moduli;
		BigInteger m;
		BigInteger inverseValue;

		int numberOfModuli = moduli.length;
		multipliers = new BigInteger[numberOfModuli];
		bigM = new BigInteger[numberOfModuli];
		inverse = new BigInteger[numberOfModuli];
		result = new BigInteger[numberOfModuli];
		result[0] = BigInteger.ZERO;
		modulus = BigInteger.ONE;

		finalResult = BigInteger.ZERO;

		for (int i = 0; i < moduli.length; i++) {
			modulus = modulus.multiply(moduli[i]);
		}

		for (int i = 0; i < moduli.length; i++) {
			m = moduli[i];
			bigM[i] = modulus.divide(m);
			xeuclid.xeuclid(bigM[i], m);
			if (bigM[i].compareTo(m) <= 0)
				inverseValue = xeuclid.getInverseY();
			else
				inverseValue = xeuclid.getInverseX();
			inverse[i] = inverseValue;

			multipliers[i] = inverseValue.multiply(bigM[i]).mod(modulus);
		}

		for (int i = 0; i < numberOfModuli; i++) {
			if (i != 0) {
				result[i] = result[i - 1].add(multipliers[i].multiply(a[i])).mod(modulus);
			} else {
				result[i] = multipliers[i].multiply(a[i]).mod(modulus);
			}

			finalResult = finalResult.add(multipliers[i].multiply(a[i])).mod(modulus);
		}

		return result[numberOfModuli - 1];
	}

	/**
	 * the inverse
	 * @return the inverse
	 */
	public BigInteger[] getInverse() {
		return inverse;
	}

	/**
	 * the array of the parameter a
	 * @return array a
	 */
	public BigInteger[] getA() {
		return a;
	}

	/**
	 * the big M's
	 * @return the big M's
	 */
	public BigInteger[] getBigM() {
		return bigM;
	}

	/**
	 * the modulus computes by sum( m_i )
	 * @return the modulus
	 */
	public BigInteger getModulus() {
		return modulus;
	}

	/**
	 * the moduli array
	 * @return moduli array
	 */
	public BigInteger[] getModuli() {
		return moduli;
	}

	/**
	 * the result of the chinese remainder theorem
	 * @return the solution of the crt
	 */
	public BigInteger getFinalResult() {
		return finalResult;
	}



}
