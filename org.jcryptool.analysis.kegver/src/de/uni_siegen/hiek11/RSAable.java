package de.uni_siegen.hiek11;

import java.math.BigInteger;

public interface RSAable {
	
	public BigInteger calcD();
	
	public BigInteger calcN();
	
	public BigInteger chooseE();
	
	public BigInteger decrypt(BigInteger inCipher);
	
	public BigInteger encrypt(BigInteger inMessage, BigInteger[] inPublicKey);
	
	public BigInteger findPrimeP();
	
	public BigInteger findPrimeQ();
	
}
