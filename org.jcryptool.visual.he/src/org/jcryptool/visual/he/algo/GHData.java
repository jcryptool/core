// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.algo;

import java.math.BigInteger;

/**
 * Holds the data for the homomorphic encryption scheme by Gentry & Halevi
 * @author Coen Ramaekers
 *
 */
public class GHData {
	/** Holds log-2 of the modulus */
	private int logMod;
	
	/** Holds a number to be encrypted */
	private int number;
	private int maxMult;
	
	/** Holds the encryption of a number */
	private BigInteger encryption;

	/** Holds two ciphertexts and the third will hold the result of a homomorphic operation */
	private BigInteger[] bigIntArray1, bigIntArray2, bigIntArray3;
	
	/** Holds the amount of additions / multiplications applied on the ciphertext */
	private int[] additions, multiplications;


	public GHData() {
		this.maxMult = 1;
	}

	public GHData(int number) {
		this.number = number;
		this.maxMult =1 ;
	}

	public GHData(BigInteger enc) {
		this.encryption = enc;
		this.maxMult =1 ;
	}

	public GHData(int number, BigInteger enc) {
		this.number = number;
		this.encryption = enc;
		this.maxMult =1 ;
	}
	
	public void setModulus(int logMod) {
		this.logMod = logMod;
	}
	
	public void setArray1(BigInteger[] arr) {
		this.bigIntArray1 = arr;
	}

	public void setArray2(BigInteger[] arr) {
		this.bigIntArray2 = arr;
	}

	public void setArray3(BigInteger[] arr) {
		this.bigIntArray3 = arr;
	}

	public void setData(int number) {
		this.number = number;
	}

	public void setData(BigInteger enc) {
		this.encryption = enc;
	}

	public void setData(int number, BigInteger enc) {
		this.number = number;
		this.encryption = enc;
	}
	
	public void setMaxMult(int maxMult) {
		this.maxMult = maxMult;
	}
	
	public void initCount(int n) {
		this.additions = new int[n];
		this.multiplications = new int[n];
	}
	
	public int getModulus() {
		return logMod;
	}
	
	public int getNumber() {
		return number;
	}

	public BigInteger getEncryption() {
		return encryption;
	}

	public BigInteger[] getArray1() {
		return this.bigIntArray1;
	}

	public BigInteger[] getArray2() {
		return this.bigIntArray2;
	}

	public BigInteger[] getArray3() {
		return this.bigIntArray3;
	}
	
	public void updateAddition(int i) {
		this.additions[i]++;
	}
	
	public void updateMultiplication(int i) {
		this.multiplications[i]++;
	}
	
	public int getAddition(int i) {
		return this.additions[i];
	}
	
	public int getMultiplication(int i){
		return this.multiplications[i];
	}
	
	public int getMaxMult() {
		return this.maxMult;
	}
}
