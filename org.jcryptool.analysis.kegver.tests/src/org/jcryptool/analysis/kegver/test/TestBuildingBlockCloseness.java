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

import static org.junit.Assert.assertTrue;

import org.junit.Test;



public class TestBuildingBlockCloseness {

	@Test
	public void first(){
		this.testEvaluate();
		this.testIsOverwhelming();
	}

	@Test
	public void testIsOverwhelming() {
		Polynomial P = new Polynomial(1, 3);
		assertTrue(_Closeness.isOverwhelming(0.97d, 8, P));
	}

	private void testEvaluate() {
		Polynomial P = new Polynomial(1, 3);
		assertTrue(0d == _Closeness.evaluate(1, P));
		assertTrue(-1d == _Closeness.evaluate(0, P));
	}
}
