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

public class RSAData{

	/*
	 * Instance variables
	 */

	private BigInteger N = null;
	private BigInteger q = null;
	private BigInteger p = null;
	private BigInteger e = null;
	private BigInteger d = null;

	public RSAData(BigInteger in_p, BigInteger in_q, BigInteger in_e, BigInteger in_d) {
		this.set_p(in_p);
		this.set_q(in_q);
		this.set_N(in_p.multiply(in_q));
		this.set_e(in_e);
		this.set_d(in_d);
	}

	/*
	 * Getter and setter
	 */

	private BigInteger get_d() {
		return this.d;
	}

	private BigInteger set_d(BigInteger in_d) {
		this.d = in_d;
		return this.get_d();
	}

	private BigInteger get_e() {
		return this.e;
	}

	private BigInteger set_e(BigInteger in_e) {
		this.e = in_e;
		return this.get_e();
	}

	public BigInteger get_N() {
		return this.N;
	}

	private BigInteger set_N(BigInteger in_N) {
		if(in_N == null || in_N.compareTo(BigInteger.ZERO) <= 0){
			throw new IllegalArgumentException();
		}
		this.N = in_N;
		return this.get_N();
	}

	public BigInteger get_q() {
		return this.q;
	}

	private BigInteger set_q(BigInteger in_q) {
		if(in_q == null || in_q.compareTo(BigInteger.ZERO) < 0){
			throw new IllegalArgumentException();
		}
		this.q = in_q;
		return this.get_q();
	}

	public BigInteger get_p(){
		return this.p;
	}

	private BigInteger set_p(BigInteger in_p) {
		if(in_p == null || in_p.compareTo(BigInteger.ZERO) < 0){
			throw new IllegalArgumentException(
					p + " " + (in_p.compareTo(BigInteger.ZERO) < 0) + " \n in_p is bad");
		}
		this.p = in_p;
		return this.get_p();
	}
}
