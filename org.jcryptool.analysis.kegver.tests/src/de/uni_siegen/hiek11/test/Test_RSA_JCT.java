//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----

package de.uni_siegen.hiek11.test;

import java.math.BigInteger;

import org.junit.Test;

import de.uni_siegen.hiek11.RSA42;
import de.uni_siegen.hiek11.RSA_JCT;

/**
 * This class puts hiek11.RSA_JCT under a test.
 * 
 * @author hhiekmann
 *
 */
public class Test_RSA_JCT extends Test_RSA{
	
	/*
	 * Assertions
	 */
		
	/*
	 * Test
	 */
		
	@Test
	/**
	 * Proof of Concept
	 */
	public void test_Alice_JCT_short_n_super(){
		// Setup
		RSA42 aAlice = new RSA_JCT();	// Setup Alice
		aAlice.create();
		// Test
		this.cipherAndBack(aAlice, aAlice);
	}
	
	@Test
	public void test_Alice_JCT_to_Bob_JCT_v2(){
		// Execute
		RSA42 aAlice = new RSA_JCT();	// Setup Alice
		aAlice.create();
		RSA42 aBob = new RSA_JCT();		// Setup Bob
		aBob.create();
		// Test
		this.cipherAndBack(aAlice, aBob);
	}
	
	/*
	 * Debugging
	 */
	
//	@Test
	/*
	 * Looking for a bug. JUnit stackoverflows encrypting a message
	 * which is longer than n. 
	 * Result: No stackOverflowError
	 */
	public void test_encrypt_iteration_fail(){
		// Execute
		RSA42 aAlice = null;
		for ( int i = 2 ; i <= 512 ; i = i*2) {
			aAlice = new RSA_JCT(i);	// Setup Alice
			aAlice.create();
			// Test
			this.cipherAndBack(aAlice, aAlice);
		}
	}	
	
//	@Test
	/*
	 * encrypt and decrypt insanely long message. 
	 * Result: No stackOverflowError
	 */
	public void test_encrypt_iteration_fail_on_messageLength(){
		// Execute
		RSA42 aAlice = null;
		for ( int i = 2 ; i <= 512 ; i = i*i) {
			aAlice = new RSA_JCT(i);	// Setup Alice
			aAlice.create();
			// Test
			for ( int j = 1 ; j < 10 ; j++ ){
				BigInteger message = this.getMessage(i*j*2);
				this.cipherAndBack(aAlice, aAlice, message);				
			}
		}
	}	
}