//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----

package de.uni_siegen.hiek11;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class is a java-implementation of: [Essl97] Esslinger, Bernhard et al.:
 * Esslinger, Bernhard (Eds.): Das CrypTool-Skript Kryptographie, Mathematik und
 * mehr. (10th edition), www.cryptool.org, Frankfurt am Main 2010.
 * 
 * This class represents a RSA-object able to encrypt and decrypt messages if
 * setup correctly. See hiek11.test.TEST_RSA_JCT for how to use it.
 * 
 * Note: If message M is larger than n = p*q, this class takes a very simple
 * approach to truncation: Every n-th part of M is truncated recusively until
 * the remainder of M is less than n.
 * 
 * @author hhiekmann
 * 
 */
public class RSA_JCT extends RSA42 {

	private BigInteger phiN = null;

	public RSA_JCT() {
		this.setK(RSA42.theK);
		this.setSecureRandom(new SecureRandom());
	}

	public RSA_JCT(int inK) {
		this.setK(inK);
		this.setSecureRandom(new SecureRandom());
	}

	public RSA_JCT(int inK, SecureRandom inSecureRandom) {
		this.setK(inK);
		this.setSecureRandom(inSecureRandom);
	}

	public RSA_JCT(SecureRandom inSecureRandom) {
		this.setK(RSA42.theK );
		this.setSecureRandom(new SecureRandom());
	}
	
	public BigInteger calcD() {
		// d = e⁻1 mod n
		return this.setD(this.getE().modInverse(this.getPhiN()));
	}

	private void calcPhiN() {
		// phi(n) = (p-1)*(q-1)
		this.setPhiN((this.getP().subtract(BigInteger.ONE).multiply(this
				.getQ().subtract(BigInteger.ONE))));
	}

	public BigInteger chooseE() {
		this.calcPhiN();
		BigInteger i = this.getN();
		/*
		 * i will become e, if it is coprime to phi(n). A high e is better than
		 * a low e, hence we iterate down from n-1 to find a coprime. gcd(i,
		 * phi(n) == 1 ? true: e = i; false: i--;
		 */
		for (i = this.getN().subtract(BigInteger.ONE); (i.compareTo(BigInteger
				.valueOf(2)) > 0); i = i.subtract(BigInteger.ONE)) {
			if ((this.gcdEqual1(this.getPhiN(), i))) {
				break;
			}
		}
		return this.setE(i);
	}

	public BigInteger findPrimeP() {
		// do-while set up to not have p == q. See setters.
		do {
			this.setP(this.getPrime());
		} while (this.getP() == null);
		return this.getP();
	}

	public BigInteger findPrimeQ() {
		do {
			this.setQ(this.getPrime());
		} while (this.getQ() == null);
		return this.getQ();
	}

	private BigInteger getPhiN() {
		return this.phiN;
	}

	private BigInteger setPhiN(BigInteger inPhiN) {
		this.phiN = inPhiN;
		return this.getPhiN();
	}
}