// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.test;

import java.lang.reflect.Method;
import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer3.POK;
import org.jcryptool.analysis.kegver.layer3.U;


public class POKInternalProof{

	public static boolean evaluate(
			BigInteger g,
			BigInteger x,
			BigInteger h,
			BigInteger r,
			BigInteger C,
			BigInteger a,
			BigInteger b) {
		// Setup
		final POK aPOK = new POK();

		// Add first method
		final Class<?>[] parameterTypes1 = {BigInteger.class, BigInteger.class, BigInteger.class, BigInteger.class, BigInteger.class};
		final Method Method1 = POK.getMethod(POKInternalProof.class, "pok_gxhr", parameterTypes1);
		final Object[] proofArgument1 = {g, x, h, r, C};
		aPOK.add(parameterTypes1, proofArgument1, Method1);

		// Add second method
		final Class<?>[] parameterTypes2 = {BigInteger.class, BigInteger.class, BigInteger.class};
		final Method Method2 = POK.getMethod(POKInternalProof.class, "pok_xeab", parameterTypes2);
		final Object[] proofArgument2 = {a, x, b};
		aPOK.add(parameterTypes2, proofArgument2, Method2);

		// Return
		return aPOK.evaluate();
	}

	/**
	 * C = g^x * h^r
	 * @param g
	 * @param x
	 * @param h
	 * @param r
	 * @param C
	 * @return
	 */
	public static boolean pok_gxhr(BigInteger g, BigInteger x, BigInteger h, BigInteger r, BigInteger C){
		return
			C == (U.pow(g, x)).multiply(
					U.pow(h, r));
	}

	/**
	 * x element [a,b] and b > a
	 * @param a
	 * @param x
	 * @param b
	 * @return
	 */
	public static boolean pok_xeab(BigInteger a, BigInteger x, BigInteger b){
		return
			(a.compareTo(x) == -1) &&
			(x.compareTo(b) == -1);
	}
}
