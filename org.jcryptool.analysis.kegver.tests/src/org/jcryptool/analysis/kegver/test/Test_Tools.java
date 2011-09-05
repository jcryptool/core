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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.jcryptool.analysis.kegver.layer2.IO;
import org.jcryptool.analysis.kegver.layer2.Tools;
import org.junit.Test;


public class Test_Tools {

//	@Test
	public void test(){
		Throwable t = new Throwable("This is a test");
		assertEquals("layer.test.Test_Tools.test: Line 18: This is a test", Tools.catchThis(t));
	}

//	@Test
	public void test_get2powKMinus1_(){
		int ec = 0; 	//Exception counter
		try{
			Tools.get2powKMinus1(-1);
		} catch (ArithmeticException e){
			ec++;
		}
		assertTrue(ec==1);
		try {
			Tools.get2powKMinus1(0);

		} catch (ArithmeticException e){
			ec++;
		}
		assertTrue(ec==2);
		assertEquals(BigInteger.valueOf(1), Tools.get2powKMinus1(1));
		assertEquals(BigInteger.valueOf(2), Tools.get2powKMinus1(2));
		assertEquals(BigInteger.valueOf(4), Tools.get2powKMinus1(3));
		assertEquals(BigInteger.valueOf(1073741824), Tools.get2powKMinus1(31));
		assertEquals(new BigInteger("85070591730234615865843651857942052864"), Tools.get2powKMinus1(127));
	}

//	@Test
	public void test_get2powK_Minus1(){
		int ec = 0; 	//Exception counter
		try{
			Tools.get2powK_Minus1(-1);
		} catch (ArithmeticException e){
			ec++;
		}
		assertTrue(ec==1);
		assertEquals(BigInteger.valueOf(0), Tools.get2powK_Minus1(0));
		assertEquals(BigInteger.valueOf(1), Tools.get2powK_Minus1(1));
		assertEquals(BigInteger.valueOf(3), Tools.get2powK_Minus1(2));
		assertEquals(BigInteger.valueOf(7), Tools.get2powK_Minus1(3));
		assertEquals(BigInteger.valueOf(2147483647), Tools.get2powK_Minus1(31));
		assertEquals(new BigInteger("170141183460469231731687303715884105727"), Tools.get2powK_Minus1(127));
	}

//	@Test
	public void test_2powk(){
		IO aIO = IO.useFactory();
		aIO.bufferln("bitLength;length of 2^(k-1);length of (2^k)-1;time passed for 2^(k-1);time passed (2^k)-1;2^(k-1);(2^k)-1;");
		long t0 = 0;
		long ta = 0;
		long tb = 0;
		for ( int i = 10000; i < 10010; i++ ) {
			t0 = System.currentTimeMillis();
			BigInteger a = Tools.get2powKMinus1(2^i);
			ta = System.currentTimeMillis() - t0;

			t0 = System.currentTimeMillis();
			BigInteger b = Tools.get2powK_Minus1(2^i);
			tb = System.currentTimeMillis() - t0;
			aIO.bufferln((2^i) + ";" + a.bitLength() + ";" + b.bitLength() + ";" + ta + ";" + tb + ";" + a + ";" + b + ";");
		}
		aIO.close();
	}

//	@Test
	public void test3(){
		BigInteger bi = new BigInteger(4, 0, new SecureRandom());
		System.out.println(bi);
	}

//	@Test
	public void test_T(){
		IO aIO = IO.useFactory();
		aIO.bufferln( "inT;b;min;rounds;" );
		for ( int i = 0 ; i < Integer.MAX_VALUE ; i = i + 100000 ) {
			aIO.bufferln(wtf(i));
		}
		aIO.close();
	}

	public String wtf(int inT){
		// Execute
		int b = Integer.MAX_VALUE-1;
		int min = (Math.min(inT, b));
		int rounds = ( min + 1 ) / 2;
		// Return
		return new String (	inT + ";" + b + ";" + min + ";" + rounds + ";" );
	}

//	@Test
	public void test5(){
		BigInteger a = BigInteger.TEN;
		int ct = a.compareTo(BigInteger.ONE);
		System.out.println(ct);
		boolean b = ct > 0;
		System.out.println(b);
	}

	@Test
	public void test_convertT2Percent(){

	}
}