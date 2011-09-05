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


import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer3.unigenprotocol.BothAreHappy;
import org.jcryptool.analysis.kegver.layer3.unigenprotocol.UnigenStateContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TEST {

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
	public void testReturn(){
		Thing o1 = new Thing(Integer.valueOf(1));
		Thing o2 = this.setO(o1);
		assertNotSame(o1, o2);
		assertSame(o1.getI(), o2.getI());

		o1.setI(Integer.valueOf(2));
		assertNotSame(o1, o2);
		assertNotSame(o1.getI(), o2.getI());

		o2.setI(Integer.valueOf(2));
		assertNotSame(o1, o2);
		assertSame(o1.getI(), o2.getI());

	}

	private Thing o = null;

	private Thing setO(Thing ino) {
		this.o = new Thing(ino);
		return this.getO();
	}

	private Thing getO(){
		return this.o;
	}

	class Thing{
		private Integer i;

		public Thing(Thing inT){
			this.i = inT.getI();
		}

		public Thing(Integer inI) {
			this.setI(inI);
		}

		public Integer getI(){
			return this.i;
		}

		public int setI(Integer inI ){
			this.i = inI;
			return this.getI();
		}
	}

//	@Test
	public void testStates(){
		UnigenStateContext aUniGenStateContext = new UnigenStateContext(null, null) ;
		if( ! aUniGenStateContext .getState().getClass().equals(BothAreHappy.class.getClass()));
	}

//	@Test
	public void testGF(){
		int order = 12;
		int a = 2;
		for ( int j = 2; j < order ; j ++) {
			for ( int i = 1 ; i < order ; i++){
				System.out.print(this.pow(j, i) % order + " ");
			}
			System.out.println();
		}
	}

	@Test
	public void testPow(){
//		assertTrue(16807 == this.pow(7, 5));
//		System.out.println(pow(5,0)); // 0
//		System.out.println(pow(5,1)); // 5
//		System.out.println(pow(5,2)); // 25
//		System.out.println(pow(5,3)); // 125
	}

	private int pow(int a , int b){
		return (int) Math.pow(a, b);
	}

//	@Test
	public void testMemory(){
		// Get current size of heap in bytes
		long heapSize = Runtime.getRuntime().totalMemory();
		System.out.println("heapSize: " + heapSize);

		// Get maximum size of heap in bytes. The heap cannot grow beyond this size.
		// Any attempt will result in an OutOfMemoryException.
		long heapMaxSize = Runtime.getRuntime().maxMemory();
		System.out.println("heapMaxSize: " + heapMaxSize);

		// Get amount of free memory within the heap in bytes. This size will increase
		// after garbage collection and decrease as new objects are created.
		long heapFreeSize = Runtime.getRuntime().freeMemory();
		System.out.println("heapFreeSize: " + heapFreeSize);

	}

//	@Test
	public void testArrayLength(){
		BigInteger[] bi = null;
		int i = 0;
		try {
			for (i = 15288900; i < Integer.MAX_VALUE ; i = i + 1){
				bi = null;
				bi = new BigInteger[i];
				System.out.println(i);
			}
		} catch (OutOfMemoryError e) {
			bi = null;

		} finally {
			System.out.println(i);
			for (int j = 0 ; i < 10 ; j++){
				System.out.println("de");
			}
		}
	}
}
