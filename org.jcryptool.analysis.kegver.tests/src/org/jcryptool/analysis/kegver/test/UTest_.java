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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer3.RSAData;
import org.jcryptool.analysis.kegver.layer3.U;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UTest_ {

	private static int inDividend2 = 3;
	private static int inDivisor = 4;
	private static BigInteger SEVEN = null;
	private static BigInteger EIGHT = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		inDividend2 = 3;
		inDivisor = 4;
		SEVEN = U.bi(7);
		EIGHT = U.bi(8);
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	 * Test
	 */

	@Test
	public void testConstants(){
		assertSame(BigInteger.ZERO, U.ZERO);
		assertSame(BigInteger.ONE, U.ONE);
		assertSame(BigInteger.valueOf(3), U.THREE);
		assertSame(BigInteger.valueOf(4), U.FOUR);
		assertSame(BigInteger.valueOf(7), U.SEVEN);
		assertSame(BigInteger.valueOf(8), U.EIGHT);
		assertTrue(BigInteger.valueOf(-1).equals(U.NEGONE));
	}

	@Test
	public void testIsModuleCongruent() {
		assertTrue(U.isCongruent(U.bi(3), inDividend2, inDivisor));
		assertFalse(U.isCongruent(U.bi(4), inDividend2, inDivisor));
		assertFalse(U.isCongruent(U.bi(1), inDividend2, inDivisor));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIsCongruent2(){
		assertFalse(U.isCongruent(U.bi(0), inDividend2, inDivisor));
	}

	@Test
	public void testIsGcdGreaterThanOne() {
		assertFalse(U.isGcdGreaterThanOne(EIGHT, SEVEN));
		assertFalse(U.isGcdGreaterThanOne(SEVEN, EIGHT));
		assertTrue(U.isGcdGreaterThanOne(SEVEN, SEVEN));
		assertTrue(U.isGcdGreaterThanOne(EIGHT, EIGHT));
		assertTrue(U.isGcdGreaterThanOne(U.bi(12), U.bi(2)));
	}

	@Test
	public void testSelectBigInteger1() {
		BigInteger max = U.probablePrime(2);
		BigInteger b = U.selectUniformlyFrom0To(max);
		boolean allGood = true;
		for ( int i = 0 ; i < 1000 ; i ++ ) {
			b = U.selectUniformlyFrom0To(max);
			if(
					b.compareTo(U.ZERO) < 0
					|| b.compareTo(max) > 0){
				allGood = false;
				break;
			}
		}
		assertTrue(allGood);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testSelectBigInteger2() {
		U.probablePrime(1);
	}

	@Test
	public void testSelectUniformlyFromXTo() {
		BigInteger inRange = BigInteger.valueOf(0);
		BigInteger offset = BigInteger.valueOf(1);
		assertTrue(U.NEGONE.equals(U.selectUniformlyFromXTo(inRange, offset)));
		offset = BigInteger.valueOf(-1);
		assertTrue(U.ONE.equals(U.selectUniformlyFromXTo(inRange, offset)));
	}

	@Test
	public void testVerboseThrowableString() {
		String s = U.verbose(new Throwable(), "hello");
		String t = "org.jcryptool.analysis.kegver.layer3.UTest:testVerboseThrowableString says: hello.";
		assertTrue(s.equals(t));
	}

	@Test
	public void testVerboseString() {
		String s = U.verbose(new Throwable(), "hello");
		String t = "org.jcryptool.analysis.kegver.layer3.UTest:testVerboseString says: hello.";
		assertTrue(s.equals(t));
	}

	@Test
	public void testIsVerbose() {
		assertFalse(U.isVerbose());
		assertTrue(U.isVerbose(true));
		assertTrue(U.isVerbose());
		assertFalse(U.isVerbose(false));
		assertFalse(U.isVerbose());
	}

	@Test
	public void testProbablePrime() {
		int k = 512;
		BigInteger p = U.probablePrime(k);
		assertTrue(k == p.bitLength());
	}

	@Test
	public void testKeygenIntBigIntegerInt() {
		BigInteger in_p = U.probablePrime(512);
		BigInteger in_q = U.probablePrime(512);
		BigInteger in_e = U.probablePrime(512);
		RSAData aRSAData = U.keygen(in_p, in_q, in_e);

		assertSame(in_p, aRSAData.get_p());
		assertSame(in_q, aRSAData.get_q());
		assertTrue(in_p.multiply(in_q).equals(aRSAData.get_N()));
	}

	@Test (expected=IllegalArgumentException.class)
	public void testKeygenBigIntegerBigIntegerBigInteger1() {
		int k = 512;
		BigInteger e = U.probablePrime(k);
		double t = 1;
		RSAData aRSAData = U.keygen(k, e, t);
		assertTrue(aRSAData.get_p().bitLength() == k);
	}

	@Test
	public void testKeygenBigIntegerBigIntegerBigInteger2() {
		int k = 512;
		BigInteger e = U.probablePrime(k);
		double t = Math.pow(2, -101);
		RSAData aRSAData = U.keygen(k, e, t);
		assertTrue(aRSAData.get_p().bitLength() == k+1); // TODO: muss p.bitlength = k sein? oder reicht >k
	}

	@Test
	public void testOffset(){
		int k = 1;
		final BigInteger _2powkminus1 = U.TWO.pow(k - 1);
		final BigInteger _2powk_minus1 = (((U.TWO).pow(k)).subtract(U.ONE)).negate();

		BigInteger r = U.selectUniformlyFromXTo(_2powkminus1, _2powk_minus1);
		for ( int i = 0 ; i < 100; i++){
			r = U.selectUniformlyFromXTo(_2powkminus1, _2powk_minus1);
			assertTrue(r.bitLength() <= 2); // TODO Offset falsch, Range 2^k-1 -1, offset 2^k-1
		}
	}

	@Test
	public void testJacobi() {
		assertTrue(U.jacobi(EIGHT, SEVEN) == 1);
		assertTrue(U.jacobi(U.bi(127), U.bi(703)) == -1);
	}
}
