package org.jcryptool.analysis.kegver.test;

import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer3.Commitment;
import org.junit.Test;


public class TestBuildingCommitment {
	
	@Test
	public void first(){
		int m = 1;
		BigInteger N = BigInteger.TEN; // Comming from CA
		BigInteger g = BigInteger.TEN; // Comming from CA
		BigInteger x = BigInteger.ONE; // Value to commit to 
		BigInteger h = BigInteger.TEN; // Comming from CA
		Commitment.FujisajiOkamoto(m, N, g, x, h);
	}


}
