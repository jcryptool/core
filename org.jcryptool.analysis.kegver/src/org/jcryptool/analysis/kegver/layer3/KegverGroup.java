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


public class KegverGroup {

	private BigInteger g = null;
	private BigInteger h = null;

	public KegverGroup(BigInteger in_o) {
		final BigInteger G = in_o.add(U.ONE);
		final BigInteger gth = U.selectUniformlyFrom0To(in_o);
		this.set_g(this.getXthElementOfG(gth, G));

		BigInteger hth = null;
		do {
			hth = U.selectUniformlyFrom0To(in_o);
		} while (hth.equals(gth));
		this.set_h(this.getXthElementOfG(hth, G));
	}

	private BigInteger getXthElementOfG(BigInteger in_x, BigInteger in_G){
		int jacobi = -1337;
		BigInteger i = null;

		for ( i = U.ZERO ; i.compareTo(in_G.subtract(U.ONE)) < 0 ; /*!*/){
			jacobi = U.jacobi(i, in_G);
			if (jacobi != 1) {
				 i.add(U.ONE);
				 if (i == in_x){
					 break;
				 }
			}
		}

		return i;
	}

	/*
	 * Getter and setter
	 */

	private BigInteger set_h(BigInteger in_h) {
		this.h = in_h;
		return this.get_h();
	}

	public BigInteger get_h() {
		return this.h;
	}

	private BigInteger set_g(BigInteger in_g) {
		this.g = in_g;
		return this.get_g();
	}

	public BigInteger get_g() {
		return this.g;
	}
}