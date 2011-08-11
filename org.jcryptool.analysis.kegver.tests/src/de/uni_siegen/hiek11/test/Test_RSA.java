package de.uni_siegen.hiek11.test;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.security.SecureRandom;

import de.uni_siegen.hiek11.RSA42;

public class Test_RSA {
	
	private static SecureRandom theSECURERANDOM = new SecureRandom();
	
	/*
	 * Test
	 */
	
	/*
	 * Shortcuts
	 */
	
	public BigInteger getMessage(int inK){
		return BigInteger.probablePrime(inK, theSECURERANDOM);
	}
	
	public void cipherAndBack(RSA42 inAlice, RSA42 inBob){
		this.cipherAndBack(inAlice, inBob, this.getMessage(inBob.getN().bitLength() - 1));
	}
	
	public void cipherAndBack(RSA42 inAlice, RSA42 inBob, BigInteger inMessage){
		
		BigInteger cipher = inAlice.encrypt(inMessage, inBob.getPublicKey());
		assertEquals(inMessage, inBob.decrypt(cipher));
	}
}