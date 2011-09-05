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


/**
 * TODO: Insert acknowledgement to FujisajiOkamoto
 * TODO: Redo everything and test
 * @author hkh
 *
 */
public class Commitment {

	/**
	 *
	 *  w = {-2^m *N+1, 2^m *N-1}
	 *  <=> w = {0, 2* (2^m *N-1)} - 2^m *N-1
	 *  => range = 2* (2^m *N-1);
	 *  => offset = 2^m *N-1
	 *
	 *  w = 2* offset - offset
	 *
	 *  Note: 2^m *N can be retrieved statically from ElementOfU._2_pow_m_times_N(int, BigInteger)
	 *
	 * @param m
	 * @return
	 */
	public static BigInteger FujisajiOkamoto(
			int m,
			BigInteger N,
			BigInteger g,
			BigInteger x,
			BigInteger h
			) {

		// Setup

		// Calculation
		BigInteger offset = Commitment._2_pow_m_times_N(m, N).subtract(BigInteger.ONE);
		BigInteger w = U.selectUniformlyFromXTo((offset.multiply(BigInteger.valueOf(2))), offset);
		//g^x * h^w mod N =  g^x mod N f * h^w mod N
		BigInteger C = 	(g.modPow(x, N)).multiply(h.modPow(w, N));

		// TODO: <g> = <h>
		// TODO: proof (?) user can't factorize N (Strong RSA Assumption... 1024 bit?)

		return C;
	}

	private static BigInteger _2_pow_m_times_N(int m, BigInteger N) {
		return ((U.TWO).pow(m)).multiply(N);
	}

	@Override
	public String toString(){
		U.verbose(new Throwable(), "Commitment not implemented yet");
		return this.hashCode()+"";
	}
}