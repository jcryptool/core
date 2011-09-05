// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.kleptography.algorithm;

import java.math.BigInteger;

/**
 * Features the tools necessary to analyze the outputs of a
 * kleptographically compromised RSAMain implementation.
 * @author Patrick Vacek
 */
public class RSAAttack {

	private String savedCipherHex1;
	private String savedDeciphered1;
	private byte[] savedCipherBytes1;
	private String savedCipherHex2;
	private String savedDeciphered2;
	private byte[] savedCipherBytes2;
	private BigInteger n1;
	private BigInteger e1;
	private BigInteger n2;
	private BigInteger e2;
	private BigInteger factoredP;
	private BigInteger q1;
	private BigInteger d1;
	private BigInteger q2;
	private BigInteger d2;
	private BigInteger p2;
	private Integer fixedTextsSaved;

	private Kleptography klepto;

	public void setSavedCipherHex1(String savedCipherHex1) {
		this.savedCipherHex1 = savedCipherHex1;
	}
	public String getSavedCipherHex1() {
		return savedCipherHex1;
	}
	public void setSavedDeciphered1(String savedDeciphered1) {
		this.savedDeciphered1 = savedDeciphered1;
	}
	public String getSavedDeciphered1() {
		return savedDeciphered1;
	}
	public void setSavedCipherBytes1(byte[] savedCipherBytes1) {
		this.savedCipherBytes1 = savedCipherBytes1;
	}
	public byte[] getSavedCipherBytes1() {
		return savedCipherBytes1;
	}
	public void setSavedCipherHex2(String savedCipherHex2) {
		this.savedCipherHex2 = savedCipherHex2;
	}
	public String getSavedCipherHex2() {
		return savedCipherHex2;
	}
	public void setSavedDeciphered2(String savedDeciphered2) {
		this.savedDeciphered2 = savedDeciphered2;
	}
	public String getSavedDeciphered2() {
		return savedDeciphered2;
	}
	public void setSavedCipherBytes2(byte[] savedCipherBytes2) {
		this.savedCipherBytes2 = savedCipherBytes2;
	}
	public byte[] getSavedCipherBytes2() {
		return savedCipherBytes2;
	}
	public void setN1(BigInteger n1) {
		this.n1 = n1;
	}
	public BigInteger getN1() {
		return n1;
	}
	public void setE1(BigInteger e1) {
		this.e1 = e1;
	}
	public BigInteger getE1() {
		return e1;
	}
	public void setN2(BigInteger n2) {
		this.n2 = n2;
	}
	public BigInteger getN2() {
		return n2;
	}
	public void setE2(BigInteger e2) {
		this.e2 = e2;
	}
	public BigInteger getE2() {
		return e2;
	}
	public void setFactoredP(BigInteger factoredP) {
		this.factoredP = factoredP;
	}
	public BigInteger getFactoredP() {
		return factoredP;
	}
	public void setQ1(BigInteger q1) {
		this.q1 = q1;
	}
	public BigInteger getQ1() {
		return q1;
	}
	public void setD1(BigInteger d1) {
		this.d1 = d1;
	}
	public BigInteger getD1() {
		return d1;
	}
	public void setQ2(BigInteger q2) {
		this.q2 = q2;
	}
	public BigInteger getQ2() {
		return q2;
	}
	public void setD2(BigInteger d2) {
		this.d2 = d2;
	}
	public BigInteger getD2() {
		return d2;
	}
	public void setP2(BigInteger p2) {
		this.p2 = p2;
	}
	public BigInteger getP2() {
		return p2;
	}
	/**	Called when the bit count or key generation method is changed. */
	public void resetFixedTextsSaved() {
		fixedTextsSaved = 0;
	}
	/**	Called after saving the first or second ciphertext. */
	public void incrementFixedTextsSaved() {
		fixedTextsSaved += 1;
	}
	public Integer getFixedTextsSaved() {
		return fixedTextsSaved;
	}

	/**
	 * Constructor: initialize all numbers to zero and texts to empty.
	 * @param klepto A reference to the kleptography driver class.
	 */
	public RSAAttack(Kleptography klepto) {
		this.klepto = klepto;
		setN1(BigInteger.ZERO);
		setE1(BigInteger.ZERO);
		setQ1(BigInteger.ZERO);
		setD1(BigInteger.ZERO);
		setFactoredP(BigInteger.ZERO);
		setP2(BigInteger.ZERO);
		setN2(BigInteger.ZERO);
		setE2(BigInteger.ZERO);
		setQ2(BigInteger.ZERO);
		setD2(BigInteger.ZERO);
		setSavedCipherBytes1(null);
		setSavedCipherBytes2(null);
		setSavedCipherHex1(""); //$NON-NLS-1$
		setSavedCipherHex2(""); //$NON-NLS-1$
		setSavedDeciphered1(""); //$NON-NLS-1$
		setSavedDeciphered2(""); //$NON-NLS-1$
		resetFixedTextsSaved();
	}

	/**
	 * Used in Fixed P attack only.
	 * Summons the extended Euclidean algorithm to factor the two saved composite Ns.
	 * The result should be P, since it was fixed.
	 */
	public void calculateCompositeGCD() {
		setFactoredP(RSAFunctions.extendedEuclidean(getN1(), getN2()));
	}

	/**
	 * Used in Fixed P attack only.
	 * Calculates the private keys corresponding to the saved public keys with the help
	 * of the factored P.
	 */
	public void calculatePrivateKeysFixed() {
		// First get Q = N / P.
		setQ1(getN1().divide(getFactoredP()));
		// Summon some modular magic to get D.
		setD1(klepto.functions.calculateD(getE1(), klepto.functions.calculatePhi(getFactoredP(), getQ1())));
		// Same process for the second Q. N and E are different but P is the same.
		setQ2(getN2().divide(getFactoredP()));
		setD2(klepto.functions.calculateD(getE2(), klepto.functions.calculatePhi(getFactoredP(), getQ2())));
	}

	/**
	 * Used in Fixed P attack only.
	 * Decrypts the saved ciphertexts with the calculated private keys.
	 */
	public void decryptFixed() {
		setSavedDeciphered1(klepto.functions.decrypt(getSavedCipherBytes1(), getD1(), getN1()));
		setSavedDeciphered2(klepto.functions.decrypt(getSavedCipherBytes2(), getD2(), getN2()));
	}

	/**
	 * Used in SETUP attack only.
	 * Calculates P and P' by decrypting the most-significant half of N with the attacker's private key.
	 * @param attackerD The attacker's private exponent D.
	 * @param attackerN The attacker's composite N.
	 */
	public void getPFromN(BigInteger attackerD, BigInteger attackerN) {
		// Strip the upper half (most signficant) bits of N to get the encrypted P.
		BigInteger encryptedP = getN1().shiftRight(klepto.functions.getBitCount() / 2);
		// Also save an adjusted P' = P + 1 in case of a carry in the original division.
		BigInteger adjustedP = encryptedP.add(BigInteger.ONE);
		// For both P and P', undo the bias removal step.
		// This is easily detected: if the encrypted P is greater than the attacker's N, it was done.
		// Take the value and flip it around the other side to get the actual encrypted P or P'.
		if(encryptedP.compareTo(attackerN) >= 0) {
			encryptedP = (new BigInteger("2").pow(klepto.functions.getBitCount() / 2). //$NON-NLS-1$
					subtract(BigInteger.ONE).subtract(encryptedP));
		}
		if(adjustedP.compareTo(attackerN) >= 0) {
			adjustedP = (new BigInteger("2").pow(klepto.functions.getBitCount() / 2). //$NON-NLS-1$
					subtract(BigInteger.ONE).subtract(adjustedP));
		}
		// Decrypt P and P' with basic modular exponentiation.
		setFactoredP(encryptedP.modPow(attackerD, attackerN));
		setP2(adjustedP.modPow(attackerD, attackerN));
	}

	/**
	 * Used in SETUP attack only.
	 * Try to calculate private keys based on P and P'.
	 */
	public void calculatePrivateKeysSETUP() {
		// Calculate Q = N / P.
		setQ1(getN1().divide(getFactoredP()));
		// Try to calculate D with modular magic. If it fails (and either this calculation or
		// the identical one for D' will with relatively high frequency), just set it to zero.
		try {
			setD1(klepto.functions.calculateD(getE1(), klepto.functions.calculatePhi(getFactoredP(), getQ1())));
		}
		catch(ArithmeticException e) {
			setD1(BigInteger.ZERO);
		}
		// Repeat for P' to get Q' and D'.
		setQ2(getN1().divide(getP2()));
		try {
			setD2(klepto.functions.calculateD(getE1(), klepto.functions.calculatePhi(getP2(), getQ2())));
		}
		catch(ArithmeticException e) {
			setD2(BigInteger.ZERO);
		}
	}

	/**
	 * Used in SETUP attack only.
	 * Decrypt the ciphertext. Try both with D and D'; it isn't really possible for the attacker
	 * to know in advance which will be the right one, but the results should make it pretty obvious.
	 */
	public void decryptSETUP() {
		// Try to decipher; if it doesn't work, leave the result blank.
		try {
			setSavedDeciphered1(klepto.functions.decrypt(getSavedCipherBytes1(), getD1(), getN1()));
		}
		catch(ArithmeticException e) {
			setSavedDeciphered1(""); //$NON-NLS-1$
		}
		try {
			setSavedDeciphered2(klepto.functions.decrypt(getSavedCipherBytes1(), getD2(), getN1()));
		}
		catch(ArithmeticException e) {
			setSavedDeciphered2(""); //$NON-NLS-1$
		}
	}
} // End RSAAttack