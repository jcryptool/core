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
 * Holds a keypair for the fully homomorphic encryption scheme by Gentry & Halevi
 * @author Coen Ramaekers
 *
 */
public class GHKeyPair {
	/** Boolean to check wheter or not the pair is set*/
	public boolean set;

	/** Lattice determinant */
	public BigInteger det;

	/** Root */
	public BigInteger root;

	/** secret value w, one odd coefficient of w(x) */
	public BigInteger w;

	/** encryption of secret vector sigma */
	public BigInteger[] ctxts;

	/** public key blocks */
	public BigInteger[] pkBlocksX;

	/** field for the owner of this keypair. */
	private String owner;

	/** field for the password. */
	private String password;

	public GHKeyPair() {
		this.set = false;
		this.det = BigInteger.ONE;
		this.root = BigInteger.ONE;
		this.w = BigInteger.ONE;
		this.ctxts = new BigInteger[]{BigInteger.ONE};
		this.pkBlocksX = new BigInteger[]{BigInteger.ONE};
	}

	public GHKeyPair(BigInteger det, BigInteger root, BigInteger w, BigInteger[] ctxts, BigInteger[] pkBlocksX) {
		this.set = true;
		this.det = det;
		this.root = root;
		this.w = w;
		this.ctxts = new BigInteger[ctxts.length];
		this.pkBlocksX = new BigInteger[pkBlocksX.length];
		for (int i = 0; i < ctxts.length; i++) {
			this.ctxts[i] = ctxts[i];
		}
		for (int i = 0; i < pkBlocksX.length; i++) {
			this.pkBlocksX[i] = pkBlocksX[i];
		}
	}

	public void setKeyPair(BigInteger det, BigInteger root, BigInteger w, BigInteger[] ctxts, BigInteger[] pkBlocksX) {
		this.set = true;
		this.det = det;
		this.root = root;
		this.w = w;
		this.ctxts = new BigInteger[ctxts.length];
		this.pkBlocksX = new BigInteger[pkBlocksX.length];
		for (int i = 0; i < ctxts.length; i++) {
			this.ctxts[i] = ctxts[i];
		}
		for (int i = 0; i < pkBlocksX.length; i++) {
			this.pkBlocksX[i] = pkBlocksX[i];
		}
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
