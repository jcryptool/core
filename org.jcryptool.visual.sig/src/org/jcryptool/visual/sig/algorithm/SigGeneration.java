package org.jcryptool.visual.sig.algorithm;

import java.security.*;
import javax.crypto.*;

public class SigGeneration {

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
		String keySig = null;

		// get String to generate the key
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

		/*
		 * //Encrypt the md to create Signature Cipher cipher =
		 * Cipher.getInstance(signaturemethod); cipher.init(Cipher.ENCRYPT_MODE,
		 * key.getPrivate());
		 * 
		 * byte[] cipherText = cipher.doFinal(input);
		 * org.jcryptool.visual.sig.algorithm.Input.signature = cipherText;
		 * 
		 * return cipherText;
		 */

		// TODO!!!

		// Get a signature object using the MD5 and RSA combo
		// and sign the input with the private key,ay
		Signature sig = Signature.getInstance(signaturemethod);
		sig.initSign(key.getPrivate());
		sig.update(input);
		byte[] signature = sig.sign();
		org.jcryptool.visual.sig.algorithm.Input.signature = signature;
		
		return signature;
	}

}
