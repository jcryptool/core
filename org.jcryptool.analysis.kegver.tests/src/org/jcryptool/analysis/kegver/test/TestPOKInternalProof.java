package org.jcryptool.analysis.kegver.test;

import java.math.BigInteger;

import org.junit.Test;


public class TestPOKInternalProof {
	
	@Test
	public void first(){
		this.testInternalProof();		
	}

	private void testInternalProof() {
		BigInteger g = BigInteger.ONE; 
		BigInteger x = BigInteger.ONE; 
		BigInteger h = BigInteger.ONE;
		BigInteger r = BigInteger.ONE;
		BigInteger C = BigInteger.ONE;
		
		BigInteger a = BigInteger.ONE;
		BigInteger b = BigInteger.ONE;
		
		System.out.println(POKInternalProof.evaluate(g,x,h,r,C,a,b));
	}
	

}