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
 * Dataset for the Paillier cryptosysem
 * @author Coen Ramaekers
 *
 */
public class PaillierData {
	/** field for the owner of this keypair. */
	private String owner;

	/** field for the password. */
	private String password;

	/** the security parameter*/
	private int s;

	/** part of public key */
	private BigInteger n;

	/** part of public key */
	private BigInteger g;

	/** part of secret key */
	private BigInteger l;

	/** part of secret key */
	private BigInteger mu;

	/** holds the encryption of plain */
	private BigInteger cipher;

	/** holds the operation number for homomorphic evaluations */
	private BigInteger operation;

	/** holds the encryption of the operation number */
	private BigInteger operationCipher;

	/** holds the initial plain number */
	private BigInteger plain;

	/** holds the encrypted result of an homomorphic evaluation */
	private BigInteger resultCipher;

	/** holds the plain result of an homomorphic evaluation */
	private BigInteger resultPlain;

	/** check whether the key is set */
	public boolean set;

	public PaillierData() {

	}

	public void setS(int s) {
		this.s  = s;
	}

	public void setPubKey(BigInteger n, BigInteger g) {
		this.n = n;
		this.g = g;
	}

	public void setPrivKey(BigInteger l, BigInteger m) {
		this.l = l;
		this.mu = m;
	}

	public void setCipher(BigInteger cipher) {
		this.cipher = cipher;
	}

	public void setOperation(BigInteger operation) {
		this.operation = operation;
	}

	public void setOperationCipher(BigInteger operationCipher) {
		this.operationCipher = operationCipher;
	}

	public void setPlain(BigInteger plain) {
		this.plain = plain;
	}

	public void setResultPlain(BigInteger resultPlain) {
		this.resultPlain = resultPlain;
	}

	public void setResultCipher(BigInteger resultCipher) {
		this.resultCipher = resultCipher;
	}

	public BigInteger[] getPubKey() {
		return new BigInteger[]{n,g};
	}

	public BigInteger[] getPrivKey() {
		return new BigInteger[]{l,mu};
	}

	public BigInteger getCipher() {
		return cipher;
	}

	public BigInteger getOperation() {
		return operation;
	}

	public BigInteger getOperationCipher() {
		return operationCipher;
	}

	public BigInteger getPlain() {
		return plain;
	}

	public BigInteger getResultCipher() {
		return resultCipher;
	}

	public BigInteger getResultPlain() {
		return resultPlain;
	}

	public int getS() {
		return s;
	}

	public void setContactName(String owner) {
		this.owner = owner;
	}

	public String getContactName() {
		return owner;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
}
