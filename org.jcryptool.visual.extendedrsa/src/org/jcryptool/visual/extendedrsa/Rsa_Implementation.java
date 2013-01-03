package org.jcryptool.visual.extendedrsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.math.*;

class Rsa_Implementation{
	
	public static String encrypt(String encMessage, BigInteger exponent, BigInteger modulus){
		//Some Type Conversion
		byte[] encByteMessage = encMessage.getBytes();
		BigInteger encBigMessage = new BigInteger(encByteMessage);

		//Message Encryption
		encBigMessage = encBigMessage.modPow(exponent, modulus);
		
		//Some More Type Conversion
		encMessage = new String(encBigMessage.toByteArray());
		
		return String.format("%040x", new BigInteger(encMessage.getBytes()));
	}

	
	//NOT FINISHED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public String decrypt(String decMessage, BigInteger exponent, BigInteger modulus){
		//Some Type Conversion
		byte[] byteMessage = decMessage.getBytes();
		BigInteger bigMessage = new BigInteger(byteMessage);

		//Message Decryption
		bigMessage = bigMessage.modPow(exponent, modulus);
		
		//Some More Type Conversion
		decMessage = new String(bigMessage.toByteArray());
		
		return String.format("%040x", new BigInteger(decMessage.getBytes(/*YOUR_CHARSET?*/)));
	}
}