package org.jcryptool.visual.extendedrsa;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;

public interface RSAKey {
	public final BigInteger one = new BigInteger("1");
	public final BigInteger two = new BigInteger("2");
	
	public final BigInteger[] possiblePrimesE = new BigInteger[] {
			new BigInteger("3"),
			new BigInteger("5"),
			new BigInteger("17"),
			new BigInteger("257"),
			new BigInteger("65537")
	};
	
	public enum Prime {P, Q, R, S ,T};
	
	public void setPrime(Prime prime, BigInteger value);
	public BigInteger getPrime(Prime prime);
	public boolean isPrimeSet(Prime prime);

	public void calculateD();
	public BigInteger getD();
	public boolean isSetD();
	
	public void setE(BigInteger e);
	public BigInteger getE();
	public boolean isESet();

	public boolean allPrimesSet();
	
	public void calculateN();
	public BigInteger getN();
	public BigInteger getPhiN();
	public boolean isNSet();
	
	public boolean isValid();
	public void resetN();
	public void completeReset();
	
	public BigInteger getRandomE();
	public TreeSet<BigInteger> getPossibleEs();
	public LinkedList<Prime> getEnabledPrimes();
}
