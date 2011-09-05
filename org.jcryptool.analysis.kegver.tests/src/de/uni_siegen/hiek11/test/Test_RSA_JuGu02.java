// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package de.uni_siegen.hiek11.test;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;

import de.uni_siegen.hiek11.RSA42;
import de.uni_siegen.hiek11.RSA_JuGu02;


public class Test_RSA_JuGu02 extends Test_RSA{

	/*
	 * Assertions
	 */

	/*
	 * Test
	 */

	/**
	 * Proof of Concept
	 */
	@Test
	public void test_Alice_JuGu02_to_itself_old(){
		// Setup
		RSA42 aAlice = new RSA_JuGu02(); 	// Setup Alice
		aAlice.create();
		// Test
		super.cipherAndBack(aAlice, aAlice);
	}

//	@Test
	public void test_Alice_JuGu02_to_Bob_JuGu02_v2(){
		// Setup
		RSA42 aAlice = new RSA_JuGu02();	// Setup Alice
		aAlice.create();
		RSA42 aBob = new RSA_JuGu02();		// Setup Bob
		aBob.create();
		// Test
		super.cipherAndBack(aAlice, aBob);
	}

//	@Test
	public void test1(){
		// Setup
		RSA42 aAlice = new RSA_JuGu02();	// Setup Alice
		aAlice.setP(BigInteger.valueOf(47));
		aAlice.setQ(BigInteger.valueOf(71));
		aAlice.calcN();
		aAlice.setE(BigInteger.valueOf(79));
		aAlice.calcD();
		BigInteger m = BigInteger.valueOf(688);
		BigInteger cipher = aAlice.encrypt(m, aAlice.getPublicKey());
		assertEquals(m, aAlice.decrypt(cipher));
	}
}