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
import java.security.SecureRandom;

import org.junit.Test;

import de.uni_siegen.hiek11.RSA42;
import de.uni_siegen.hiek11.RSA_JCT;
import de.uni_siegen.hiek11.RSA_JuGu02;


public class Test_RSA42 {

	@Test
	public void test_JuGu02_JCT(){

		int k = 1024;
		SecureRandom aSecureRandom = new SecureRandom();

		// Setup Alice
		BigInteger e = BigInteger.probablePrime(k, aSecureRandom);
		int t = Integer.MAX_VALUE / 2 ;
		RSA42 aAlice = new RSA_JuGu02(k, e, t, aSecureRandom);
		aAlice.findPrimeP();
		aAlice.findPrimeQ();
		aAlice.calcD();
		aAlice.calcN();

		// Setup Bob
		RSA42 aBob = new RSA_JCT(k, aSecureRandom);
		aBob.findPrimeP();
		aBob.findPrimeQ();
		aBob.calcN();
		aBob.chooseE();
		aBob.calcD();

		// Alice generates a message
		BigInteger message = aAlice.getPrime();
		// Alice ciphers the message using Bob's public Key
		BigInteger cipher = aAlice.encrypt(message, aBob.getPublicKey());
		// The plaintext Bob decrypts must equal Alice's message
		assertEquals(message, aBob.decrypt(cipher));

		// Bob generates a message
		message = aBob.getPrime();
		// Bob ciphers the message using Bob's public Key
		cipher = aBob.encrypt(message, aAlice.getPublicKey());
		// The plaintext Alice decrypts must equal Bob's message
		assertEquals(message, aAlice.decrypt(cipher));

	}
}