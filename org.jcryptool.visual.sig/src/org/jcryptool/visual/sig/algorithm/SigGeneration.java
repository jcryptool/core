package org.jcryptool.visual.sig.algorithm;

import java.security.*;
import javax.crypto.*;

public class SigGeneration {
	public static String signature;
	
	/**
	 * This method signed a hash stored in Hash.jave with a given signature
	 * method.
	 * 
	 * @param signaturemethod
	 *            Chosen signature method to sign the hash.
	 * @param input
	 *            hash form Hash.java
	 * @return
	 * @throws Exception
	 */
	public static byte[] SignInput(String signaturemethod, byte[] input)
			throws Exception {
		
		// get String to generate the key
		String keySig = null;
		if (signaturemethod == "SHA1withDSA") {
			keySig = "DSA";
		} else {
			if (signaturemethod == "MD5withRSA"
					|| signaturemethod == "SHA1withRSA"
					|| signaturemethod == "SHA256withRSA"
					|| signaturemethod == "SHA384withRSA"
					|| signaturemethod == "SHA512withRSA"
					|| signaturemethod == "SHA1withRSAandMGF1"
					|| signaturemethod == "SHA256withRSAandMGF1"
					|| signaturemethod == "SHA384withRSAandMGF1"
					|| signaturemethod == "SHA512withRSAandMGF1") {
				keySig = "RSA";
			} else {
				keySig = "EC";
			}
		}

		// Generate keypair
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keySig);
		keyGen.initialize(1024);
		KeyPair key = keyGen.generateKeyPair();

		// Get a signature object using the MD5 and RSA combo
		// and sign the input with the private key
		Signature sig = Signature.getInstance(signaturemethod);
		sig.initSign(key.getPrivate());
		sig.update(input);
		byte[] signatureArray = sig.sign();
		
		signature = new String (Hash.bytesToHex(signatureArray)); //Hex String
		org.jcryptool.visual.sig.algorithm.Input.signature = signatureArray;
		
		return signatureArray;
	}

}
