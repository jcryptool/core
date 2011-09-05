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

import static org.junit.Assert.assertSame;

import org.junit.Test;


public class TestUtilsPolynomial {

	@Test
	public void first(){
		this.testPolynomial();
	}

	@Test
	public void testPolynomial() {
		Polynomial p1 = new Polynomial(1, 0);
		Polynomial p2 = new Polynomial(2, 1);
		Polynomial p3 = new Polynomial(3, 2);
		Polynomial p4 = p1.plus(p2).plus(p3);
		assertSame(86, p4.evaluate(5));
	}


}
