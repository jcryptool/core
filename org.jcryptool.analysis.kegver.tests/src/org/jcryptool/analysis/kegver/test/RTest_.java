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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Random;

import org.jcryptool.analysis.kegver.layer3.R;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RTest_ {

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
	public void testProbablePrime() {
		int k = 4;
		for (int i = 0 ; i < Math.pow(2, k) ; i++){
			BigInteger p = R.probablePrime(k);
			assertTrue(k == p.bitLength());
		}
	}

	@Test
	public void testT() {
		assertTrue(Math.pow(2, -100) == R.T());
	}

	@Test
	public void testIsLessProbable(){
		assertTrue(R.isLessProbable(Math.pow(2, -99)));
		assertFalse(R.isLessProbable(Math.pow(2, -101)));
	}

	@Test
	public void testResetRandom() {
		Random R1 = R.resetRandom();
		Random R2 = R.resetRandom();
		assertNotSame(R1,R2);
	}

	@Test
	public void testNextInt() {
		int n = 5;
		int temp = -1;
		boolean allGood = true;
		for ( int i = 0 ; i < 1000 ; i++ ){
			temp = R.nextInt(n);
			if( temp < 0 || temp >= 5){
				allGood = false;
				break;
			}
		}
		assertTrue(allGood);
	}

}
