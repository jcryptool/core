package test_Test;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.junit.Test;


public class Test_BigIntegerIterationRecursion {
	
	@Test
	public void test(){
		int bitLength = 1;
		SecureRandom aSecureRandom = new SecureRandom();
		BigInteger message = BigInteger.probablePrime(bitLength, aSecureRandom);
		BigInteger e = BigInteger.probablePrime(bitLength, aSecureRandom);
		BigInteger n = BigInteger.probablePrime(bitLength, aSecureRandom);
		this.encrypt(message, new BigInteger[] { n, e });
	}
	
	public BigInteger encrypt(BigInteger inMessage, BigInteger[] inE) {
		if (inMessage.compareTo(inE[0]) > 0) {
			return inE[0].add(this.encrypt(inMessage.subtract(inE[0]), inE));
		} else {
			return inMessage.modPow(inE[1], inE[0]);
		}
	}
}
