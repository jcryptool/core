// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.dsa;

import java.math.BigInteger;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * Shared data object for everything related to the ElGamal encryption.
 *
 * @author Michael Gaber
 */
public class DSAData {

	private BigInteger k;
	private BigInteger r;
	/** whether this data is connected to a standalone key-generation wizard. */
	private boolean standalone;

	/** keyparts */
	private BigInteger generator, p, q, x, y;

	private String plainText;
	private String cipherText;
	private String signature;
	private boolean simpleHash;
	private KeyStoreAlias privateAlias;
	private String password;
	private KeyStoreAlias publicAlias;
	private String contactName;
	private Action action;

	/**
	 * @return the standalone
	 */
	public final boolean isStandalone() {
		return this.standalone;
	}

	/**
	 * setter for the standalone property of this data-object.
	 *
	 * @param standalone
	 *            the standalone property
	 */
	public final void setStandalone(final boolean standalone) {
		this.standalone = standalone;
	}

	public BigInteger getGenerator() {
		return generator;
	}

	public String getPlainText() {
		if (plainText == null) {
			return "";
		} else {
			return plainText;
		}
	}

	public String getCipherText() {
		if (cipherText == null) {
			return "";
		} else {
			return cipherText;
		}
	}

	public String getSignature() {
		if (signature == null) {
			return "";
		} else {
			return signature;
		}
	}

	public boolean getSimpleHash() {
		return simpleHash;
	}

	public KeyStoreAlias getPrivateAlias() {
		return privateAlias;
	}

	public String getPassword() {
		return password;
	}

	public void setGenerator(final BigInteger generator) {
		this.generator = generator;
	}

	public BigInteger getP() {
		return p;
	}

	public void setP(final BigInteger p) {
		this.p = p;
	}

	public BigInteger getQ() {
		return q;
	}

	public void setQ(final BigInteger q) {
		this.q = q;
	}

	public BigInteger getX() {
		return x;
	}

	public void setX(final BigInteger x) {
		this.x = x;
	}

	public BigInteger getY() {
		return y;
	}

	public void setY(final BigInteger y) {
		this.y = y;
	}

	public KeyStoreAlias getPublicAlias() {
		return publicAlias;
	}

	public String getContactName() {
		return contactName;
	}

	public void setPlainText(final String plainText) {
		this.plainText = plainText;
	}

	public void setCipherText(final String cipherText) {
		this.cipherText = cipherText;
	}

	public void setSignature(final String signature) {
		this.signature = signature;
	}

	public void setSimpleHash(final boolean simplehash) {
		this.simpleHash = simplehash;
	}

	public void setPrivateAlias(final KeyStoreAlias privateAlias) {
		this.privateAlias = privateAlias;
	}

	public void setPublicAlias(final KeyStoreAlias publicAlias) {
		this.publicAlias = publicAlias;
	}

	public void setContactName(final String contactName) {
		this.contactName = contactName;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setAction(final Action action) {
		this.action = action;
	}

	public Action getAction() {
		return action;
	}

	public BigInteger getK() {
		return k;
	}

	public void setK(final BigInteger k) {
		this.k = k;
	}

	public BigInteger getR() {
		return r;
	}

	public void setR(final BigInteger r) {
		this.r = r;
	}

	public BigInteger getModulus() {
		// TODO implement
		return null;
	}

}
