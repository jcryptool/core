// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3.kegverprotocol;

public interface KegverStateBehavior {

	/**
	 * Add user and ca to kegver-protocol.
	 * Test ca and user for required variables (i.e. not null).
	 */
	public void setup();

	/**
	 * (Cr, r) <- unigen[2^k-1, 2^k -1](m,t)
	 *  Cr being the public output, r being known to user
	 */
	public void bothUnigen_r();

	public void caAbortsUnigen_r();

	public void userAbortsUnigen_r();

	/**
	 * (Cs, s) <- unigen[2^k-1, 2^k -1](m,t)
	 *  Cs being the public output, s being known to user
	 *  Note: step 1 in unigen can be omitted here.
	 */
	public void bothUnigen_s();

	public void caAbortsUnigen_s();

	public void userAbortsUnigen_s();

	/**
	 * p is prime >= r meeting the conditions:
	 * 1.) gcd(e, p-1) = 1
	 * 2.) p = 3 mod 4
	 * 3.) p-r is minimal, if p-r > l abort! See userAborts_p()
	 */
	public void userGenerates_p();

	/**
	 * User aborts, the protocol outputs null;
	 * Protocol has to be re-instantiated.
	 */
	public void userAborts_p();

	/**
	 * q is prime >= r meeting the conditions:
	 * 1.) gcd(e, q-1) = 1
	 * 2.) q = 3 mod 4
	 * 3.) q - s is minimal, if q-s > l abort! See userAborts_q()
	 */
	public void userGenerates_q();

	/**
	 * User aborts, the protocol outputs null;
	 * Protocol has to be re-instantiated.
	 */
	public void userAborts_q();

	/**
	 * Before he does:
	 * 1.) user selects wp element of U [-2^m *N, 2^m *N]
	 * 2.) user computes Cp = C(p,wp)
	 */
	public void userSends_Cp();

	/**
	 * TODO: POK_Cp {a,b: (Cp/Cr = g^a * g^h) && (a e [0,l])}
	 */
	public void caVerifiesPOK_Cp();

	/**
	 * Protocol output is null
	 */
	public void caAborts_Cp();

	/**
	 * Before he does:
	 * 1.) user selects wq element of U [-2^m *N, 2^m *N]
	 * 2.) user computes Cq = C(q,wq)
	 */
	public void userSends_Cq();

	/**
	 * TODO: POK_Cq {a,b: (Cq/Cr = g^a * g^h) && (a e [0,l])}
	 */
	public void caVerifiesPOK_Cq();

	/**
	 * Protocol output is null
	 */
	public void caAborts_Cq();

	/**
	 * Before he does:
	 * 1.) user calculates n = pq
	 */
	public void userSends_n();

	/**
	 * TODO: POK_n {a,b,c,d : (Cp = g^a * h^d) && (Cq = g^c * h^d) && (g^n = g^ac)}
	 * hereby the user proves that Cp and Cq are commitments to factors of n
	 */
	public void caVerifiesPOK_n();

	/**
	 * Protocol output is null
	 */
	public void caAborts_Cn();

	/**
	 *
	 */
	public void userExecutesBlum_n();

	/**
	 *
	 */
	public void caExecutesBlum_n();

	/**
	 * Protocol output is null
	 */
	public void caAbortsBlum_n();

	/**
	 * Protocol output is n and published.
	 * User holds (q,p);
	 */
	public void bothAreHappy();
}
