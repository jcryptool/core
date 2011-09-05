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

public abstract class RSA42 implements RSAable {

	protected static final BigInteger theE = BigInteger.valueOf(8);
	protected static final int theK = 4;

	private SecureRandom aSecureRandom = null;
	private BigInteger d = null;
	private BigInteger e = null;
	private int k = 0;
	private BigInteger n = null;
	private BigInteger p = null;
	private BigInteger q = null;

	public abstract BigInteger calcD();

	public final BigInteger calcN() {
		// n = p * q
		return this.setN(this.getP().multiply(this.getQ()));
	}

	public abstract BigInteger chooseE();

	public final void create(){
		this.findPrimeP();
		this.findPrimeQ();
		this.calcN();
		this.chooseE();
		this.calcD();
	}

	public BigInteger decrypt(BigInteger inC) {
		return inC.modPow(this.getD(), this.getN());
	}

	/**
	 * Note: Iteration in use.
	 *
	 * @param inE
	 *            Note: If this object is Alice, this is Bob's public key:
	 *            (n,e). inE[0] is therefore Bob's n and inE[1] is Bob's e. And
	 *            yes, Alice uses them to encrypt the message.
	 */
	public final BigInteger encrypt(BigInteger inM, BigInteger[] inE) {
		return inM.modPow(inE[1], inE[0]);
	}

	@Override
	public final boolean equals(Object inObj){
		// Setup
		boolean isEqual = false;
		// Execute
		if (
				inObj instanceof RSA42 &&
				inObj.hashCode() == this.getD().hashCode()){
			isEqual = true;
		}
		// Return
		return isEqual;
	}

	public abstract BigInteger  findPrimeP();

	public abstract BigInteger findPrimeQ();

	protected final boolean gcdEqual1(BigInteger a, BigInteger b) {
		BigInteger gcd = a.gcd(b);
		return (gcd.compareTo(BigInteger.ONE) == 0);
	}

	public final BigInteger getD() {
		return this.d;
	}

	public final BigInteger getE() {
		return this.e;
	}

	public final int getK() {
		return this.k;
	}

	public final BigInteger getN() {
		return this.n;
	}

	protected final BigInteger getP() {
		return this.p;
	}

	public final BigInteger getPrime() {
		return BigInteger.probablePrime(this.getK(), this.getSecureRandom());
	}

	public final BigInteger getPrime(int inK) {
		return BigInteger.probablePrime(inK, this.getSecureRandom());
	}

	/**
	 * Note: This is Bob's public key: (n,e). inE[0] is therefore Bob's n and
	 * inE[1] is Bob's e. And yes, Alice uses them to encrypt the message.
	 *
	 */
	public final BigInteger[] getPublicKey() {
		return new BigInteger[] { this.getN(), this.getE() };
	}

	protected final BigInteger getQ() {
		return this.q;
	}


	public final SecureRandom getSecureRandom() {
		return this.aSecureRandom;
	}

	@Override
	public final int hashCode(){
		return this.getD().hashCode();
	}

	protected final BigInteger setD(BigInteger inD) {
		this.d = inD;
		return this.getD();
	}

	public final BigInteger setE(BigInteger inE) {
		this.e = inE;
		return this.getE();
	}

	public final int setK(int inK) {
		this.k = inK;
		return this.getK();
	}

	private final BigInteger setN(BigInteger inN) {
		this.n = inN;
		return this.getN();
	}

	public final BigInteger setP(BigInteger inP) {
		// for reasons of beauty copied the structure of setQ(BigInteger)
		if (inP.equals(this.getQ())) {
			this.p = null;
		} else {
			this.p = inP;
		}
		return this.getP();
	}

	public final BigInteger setQ(BigInteger inQ) {
		// if new q equals already set p, then do not set q.
		if (inQ.equals(this.getP())) {
			// null is used a level higher to know when the new q was not
			// accepted.
			this.q = null;
		} else {
			this.q = inQ;
		}
		return this.getQ();
	}

	protected final SecureRandom setSecureRandom(SecureRandom inSecureRandom) {
		this.aSecureRandom = inSecureRandom;
		return this.getSecureRandom();
	}

	@Override
	/*
	 * Note: Secret part of private key, d, is published!
	 */
	public final String toString() {
		return (
				this.getClass().getName() +
				"\nk=" + this.getK() +
				"\np=" + this.getP() +
				"\nq=" + this.getQ() +
				"\nn=" + this.getN() +
				"\ne=" + this.getE() +
				"\nd=" + this.getD()
				);
	}
}
