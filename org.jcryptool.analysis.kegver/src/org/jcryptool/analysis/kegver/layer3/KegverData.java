// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3;

import java.math.BigInteger;

public class KegverData {

	private int k = -1337; // key-size parameter
	private BigInteger l = null; // soundness and security parameter
	private int m = -1337; // security parameter
	private int t = -1337; // soundness parameter
	private BigInteger N = null; // RSA modulus N
	private Commitment aCommitment_Cp = null;
	private Commitment aCommitment_Cq = null;

	/*
	 * Constructor
	 */

	public KegverData(
			int in_k,
			BigInteger in_l,
			int in_m,
			int in_t,
			BigInteger in_N){
		this.set_k(in_k);
		this.set_l(in_l);
		this.set_m(in_m);
		this.set_t(in_t);
		this.set_N(in_N);
	}

	/*
	 * Getter and setter
	 */

	public BigInteger set_N(BigInteger in_N) {
		if(
				in_N == null
				|| in_N.compareTo(BigInteger.ZERO) <= 0
//				|| this.get_k() <= in_N.bitLength()
				){
			throw new IllegalArgumentException();
		}
		this.N = in_N;
		return this.get_N();
	}

	public BigInteger get_N() {
		return this.N;
	}

	private int set_t(int in_t) {
		if(in_t <= 0){
			throw new IllegalArgumentException();
		}
		this.t = in_t;
		return this.get_t();
	}

	public int get_t() {
		return this.t;
	}

	private int set_m(int in_m) {
		if(in_m <= 0){
			throw new IllegalArgumentException();
		}
		this.m = in_m;
		return this.get_m();
	}

	public int get_m() {
		return this.m;
	}

	private BigInteger set_l(BigInteger in_l) {
		if(in_l == null || in_l.compareTo(BigInteger.ZERO) <= 0) {
			throw new IllegalArgumentException();
		}
		this.l = in_l;
		return this.get_l();
	}

	public BigInteger get_l() {
		return this.l;
	}

	private int set_k(int in_k) {
		if(in_k <= 0){
			throw new IllegalArgumentException();
		}
		this.k = in_k;
		return this.get_k();
	}

	public int get_k() {
		return this.k;
	}

	/*
	 * Override
	 */

	@Override
	public String toString(){
		StringBuilder aStringBuilder = new StringBuilder();
		aStringBuilder.append(
				"k: " + this.get_k() +
				", m: " + this.get_m() +
				", t: " + this.get_t() +
				", l: " + this.get_l() +
				", N: " + this.get_N());
		return aStringBuilder.toString();
	}

	public Commitment set_Cp(Commitment inCommitment_Cp) {
		this.aCommitment_Cp = inCommitment_Cp;
		return this.getCommitment_Cp();
	}

	public Commitment getCommitment_Cp() {
		return this.aCommitment_Cp;
	}

	public Commitment set_Cq(Commitment inCommitment_Cq) {
		this.aCommitment_Cq = inCommitment_Cq;
		return this.getCommitment_Cq();
	}

	public Commitment getCommitment_Cq() {
		return this.aCommitment_Cq;
	}
}