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

public class POK_Proof1 implements IPOKBehavior {

	private POK aPOK = null;

	public POK_Proof1(BigInteger g, double a, BigInteger h){
		//Setup
		this.setPOK(new POK());

		// Add first method
		final Class<?>[] parameterTypes1 = {BigInteger.class, Double.class, BigInteger.class};
		final Method Method1 = POK.getMethod(POK_Proof1.class, "pok_g_pow_a_equal_h", parameterTypes1);
		final Object[] proofArgument1 = {g, a, h};
		aPOK.add(parameterTypes1, proofArgument1, Method1);

		// Add second method
		final Class<?>[] parameterTypes2 = {BigInteger.class, Double.class, BigInteger.class};
		final Method Method2 = POK.getMethod(POK_Proof1.class, "pok_h_pow_b_equal_g", parameterTypes1);
		final Object[] proofArgument2 = {h, a, g};
		aPOK.add(parameterTypes2, proofArgument2, Method2);
	}

	public static boolean pok_g_pow_a_equal_h(BigInteger in_g, double in_a, BigInteger in_h ){
		return in_g.pow((int)in_a) == in_h;
	}

	public static boolean pok_h_pow_b_equal_g(BigInteger in_h, double in_b, BigInteger in_g){
		return in_h.pow((int)in_b) == in_g;
	}

	/*
	 * Getter and setter
	 */

	private void setPOK(POK inPOK) {
		this.aPOK = inPOK;
	}

	public boolean evaluate() {
		return this.aPOK.evaluate();
	}
}
