// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3.incubation;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CyclicGroupTest {

	private static int prime = 31;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPrepareGenerators() {
//		CyclicGroup.prepareGenerators(prime);
	}

	@Test
	public void test() {
		int p = 5;
		int q = 3;
		int n = 2*p*q;
		CyclicGroup.prepareGenerators(n);
		System.out.println(CyclicGroup.getPrimeNumber());
		int i = CyclicGroup.getGenerator();
		System.out.println(i);
	}

	@Test
	public void testGetGenerator() {
		fail("Not yet implemented");
	}


	@Test
	public void testGetPrimeNumber() {
		fail("Not yet implemented");
	}

}
