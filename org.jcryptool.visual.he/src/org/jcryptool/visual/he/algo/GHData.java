package org.jcryptool.visual.he.algo;

import java.math.BigInteger;

/**
 * Holds the data for the homomorphic encryption scheme by Gentry & Halevi
 * @author Coen Ramaekers
 *
 */
public class GHData {
	/** Holds a number to be encrypted */
	private int number;
	
	/** Holds the encryption of a number */
	private BigInteger encryption;
	
	/** Holds two ciphertexts and the third will hold the result of a homomorphic operation */
	private BigInteger[] bigIntArray1, bigIntArray2, bigIntArray3;

	
	public GHData() {
	}
	
	public GHData(int number) {
		this.number = number;
	}
	
	public GHData(BigInteger enc) {
		this.encryption = enc;
	}
	
	public GHData(int number, BigInteger enc) {
		this.number = number;
		this.encryption = enc;
	}
	
	public void setArray1(BigInteger[] arr) {
		this.bigIntArray1 = arr;
	}
	
	public void setArray2(BigInteger[] arr) {
		this.bigIntArray2 = arr;
	}
	
	public void setArray3(BigInteger[] arr) {
		this.bigIntArray3 = arr;
	}
	
	public void setData(int number) {
		this.number = number;
	}
	
	public void setData(BigInteger enc) {
		this.encryption = enc;
	}
	
	public void setData(int number, BigInteger enc) {
		this.number = number;
		this.encryption = enc;
	}
	
	public int getNumber() {
		return number;
	}
	
	public BigInteger getEncryption() {
		return encryption;
	}
	
	public BigInteger[] getArray1() {
		return this.bigIntArray1;
	}
	
	public BigInteger[] getArray2() {
		return this.bigIntArray2;
	}
	
	public BigInteger[] getArray3() {
		return this.bigIntArray3;
	}
}
