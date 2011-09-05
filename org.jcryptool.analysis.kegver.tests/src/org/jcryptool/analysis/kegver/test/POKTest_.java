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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.jcryptool.analysis.kegver.layer3.POK;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class POKTest_ {

	private static POK aPOK = null;
	private static Method m = null;
	private static String s = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		aPOK = new POK();
	}

	@After
	public void tearDown() throws Exception {
		aPOK = null;
		m = null;
		s = null;
	}

	/*
	 * Test
	 */

	@Test
	public void testPOK() {
	}

	@Test
	public void testGetMethod1() {
		m = POK.getMethod(this.getClass(), "returnTrue", null);
		s = "public static boolean org.jcryptool.analysis.kegver.test.POKTest.returnTrue()";
		assertTrue(m.toString().equals(s));
	}

	public static boolean returnTrue() {
		return true;
	}

	@Test
	public void testGetMethod2 () {
		Class<?>[] parameterTypes = {Boolean.TYPE};
		m = POK.getMethod(this.getClass(), "returnSomething", parameterTypes);
		s = "public static boolean org.jcryptool.analysis.kegver.test.POKTest.returnSomething(boolean)";
		assertTrue(m.toString().equals(s));
	}

	public static boolean returnSomething(boolean inBoolean) {
		return inBoolean;
	}

	@Test
	public void testGetMethod3 () {
		Class<?>[] parameterTypes = {Boolean.TYPE, Integer.TYPE};
		m = POK.getMethod(this.getClass(), "returnSomething2", parameterTypes);
		s = "public static boolean org.jcryptool.analysis.kegver.test.POKTest.returnSomething2(boolean,int)";
		assertTrue(m.toString().equals(s));
	}

	public static boolean returnSomething2(boolean inBoolean, int in_i) {
		return inBoolean;
	}

	@Test
	public void testAdd1() throws SecurityException, NoSuchMethodException {
		Class<?>[] parameterTypes = {};
		m = POK.getMethod(POKTest_.class, "returnTrue", parameterTypes);
		Class<?>[] classes = {};
		Object[] objects = {};
		assertTrue(aPOK.add(classes, objects, m));
	}

	@Test
	public void testAdd2() throws SecurityException, NoSuchMethodException {
		Class<?>[] parameterTypes = {Boolean.TYPE};
		m = POK.getMethod(this.getClass(), "returnSomething", parameterTypes);
		Class<?>[] classes = {};
		Object[] objects = {};
		assertTrue(aPOK.add(classes, objects, m));
	}

	@Test
	public void testAdd3() throws SecurityException, NoSuchMethodException {
		Class<?>[] parameterTypes = {Boolean.TYPE, Integer.TYPE};
		m = POK.getMethod(this.getClass(), "returnSomething2", parameterTypes);
		Class<?>[] classes = {};
		Object[] objects = {};
		assertTrue(aPOK.add(classes, objects, m));
	}

	@Test
	public void testEvaluate1() {
		Class<?>[] parameterTypes = {};
		m = POK.getMethod(POKTest_.class, "returnTrue", parameterTypes);
		Class<?>[] classes = {};
		Object[] objects = {};
		assertTrue(aPOK.add(classes, objects, m));
		assertTrue(aPOK.evaluate());
	}

	@Test
	public void testEvaluate21() {
		Class<?>[] parameterTypes = {Boolean.TYPE};
		m = POK.getMethod(this.getClass(), "returnSomething", parameterTypes);
		Class<?>[] classes = {Boolean.class};
		Object[] objects = {true};
		assertTrue(aPOK.add(classes, objects, m));
		assertTrue(aPOK.evaluate());
	}

	@Test
	public void testEvaluate22() {
		Class<?>[] parameterTypes = {Boolean.TYPE};
		m = POK.getMethod(this.getClass(), "returnSomething", parameterTypes);
		Class<?>[] classes = {Boolean.class};
		Object[] objects = {false};
		assertTrue(aPOK.add(classes, objects, m));
		assertFalse(aPOK.evaluate());
	}

	@Test
	public void testEvaluate3() {
		Class<?>[] parameterTypes = {Boolean.TYPE, Integer.TYPE};
		m = POK.getMethod(this.getClass(), "returnSomething2", parameterTypes);
		Class<?>[] classes = {Boolean.class, Integer.class};
		Object[] objects = {true, 100};
		assertTrue(aPOK.add(classes, objects, m));
		assertTrue(aPOK.evaluate());
	}

	@Test
	public void testEvaluate4() {
		Class<?>[] parameterTypes1 = {};
		m = POK.getMethod(POKTest_.class, "returnTrue", parameterTypes1);
		Class<?>[] classes1 = {};
		Object[] objects1 = {};
		assertTrue(aPOK.add(classes1, objects1, m));

		Class<?>[] parameterTypes2 = {Boolean.TYPE};
		m = POK.getMethod(this.getClass(), "returnSomething", parameterTypes2);
		Class<?>[] classes2 = {Boolean.class};
		Object[] objects2 = {true};
		assertTrue(aPOK.add(classes2, objects2, m));

		Class<?>[] parameterTypes3 = {Boolean.TYPE, Integer.TYPE};
		m = POK.getMethod(this.getClass(), "returnSomething2", parameterTypes3);
		Class<?>[] classes3 = {Boolean.class, Integer.class};
		Object[] objects3 = {true, 100};
		assertTrue(aPOK.add(classes3, objects3, m));

		assertTrue(aPOK.evaluate());
	}
}
