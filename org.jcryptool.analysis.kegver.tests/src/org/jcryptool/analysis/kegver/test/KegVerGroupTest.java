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

import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer3.KegverGroup;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class KegVerGroupTest {

	private static KegverGroup aKegVerGroup = null;

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
		aKegVerGroup = null;
	}

	@Test
	public void testKegVerGroup() {
		BigInteger o = BigInteger.valueOf(31);
		aKegVerGroup = new KegverGroup(o);
		System.out.println(aKegVerGroup.get_g());
	}

	@Test
	public void testGet_h() {
		fail("Not yet implemented");
	}

	@Test
	public void testGet_g() {
		fail("Not yet implemented");
	}

}
