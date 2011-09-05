// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package de.uni_siegen.hiek11;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA_JuGu02 extends RSA42 {

	private static final int theT = 99/100 * Integer.MAX_VALUE;

	private int t = 0;

	public RSA_JuGu02() {
		this(RSA42.theK, RSA42.theE, RSA_JuGu02.theT, new SecureRandom());
	}

	public RSA_JuGu02(int inK, BigInteger inE) {
		this(inK, inE, RSA_JuGu02.theT, new SecureRandom());
	}

	public RSA_JuGu02(int inK, BigInteger inE, int inT, SecureRandom inSecureRandom) {
		super.setK(inK);
		super.setE(inE);
		super.setSecureRandom(inSecureRandom);
		this.setT(inT);
	}

	public RSA_JuGu02(int inK, SecureRandom inSecureRandom) {
		this(inK, RSA_JuGu02.theE, RSA_JuGu02.theT, inSecureRandom);
	}

	public BigInteger calcD() {
		BigInteger pMinus1 = this.getP().subtract(BigInteger.ONE);
		BigInteger qMinus1 = this.getQ().subtract(BigInteger.ONE);
		BigInteger totient = pMinus1.multiply(qMinus1);
		return this.setD(this.getE().modInverse(totient));
	}

	@Override
	public BigInteger chooseE() {
		return this.getE();
	}

	public BigInteger findPrimeP() {
		BigInteger r = new BigInteger(this.getK(), this.getT(),
				this.getSecureRandom());
		while (!this.isPrimeValid(r)) {
			r = r.add(BigInteger.ONE);
		}
		return this.setP(r);
	}

	public BigInteger findPrimeQ() {
		BigInteger s = new BigInteger(this.getK(), this.getT(),
				this.getSecureRandom());
		while (!this.isPrimeValid(s)) {
			s = s.add(BigInteger.ONE);
		}
		return this.setQ(s);
	}

	private int getT() {
		return this.t;
	}

	private boolean isEquivalent(BigInteger inPrime, BigInteger inRemainder,
			BigInteger inModulus) {
		return (inPrime.mod(inModulus)).equals(inRemainder);
	}

	private boolean isPrime(BigInteger inPrime, int inT) {
		return inPrime.isProbablePrime(inT);
	}

	private boolean isPrimeValid(BigInteger inPrime) {
		boolean isValid = true;
		if (!this.gcdEqual1(super.getE(), inPrime.subtract(BigInteger.ONE))
				|| !this.isEquivalent(inPrime, BigInteger.valueOf(3),
						BigInteger.valueOf(4))
				|| !this.isPrime(inPrime, this.getT())) {
			isValid = false;
		}
		return isValid;
	}

	private int setT(int inT) {
		this.t = inT;
		return this.getT();
	}
}