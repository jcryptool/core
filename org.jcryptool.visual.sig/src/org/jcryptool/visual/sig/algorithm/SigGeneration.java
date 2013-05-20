package org.jcryptool.visual.sig.algorithm;

import java.security.*;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sig.SigPlugin;

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
		// Generators are currently available for RSA and DSA.
		if (signaturemethod == "SHA1withDSA"
				|| signaturemethod == "SHA1withECDSA"
				|| signaturemethod == "SHA256withECDSA"
				|| signaturemethod == "SHA384withECDSA"
				|| signaturemethod == "SHA512withECDSA") {
			keySig = "DSA";
		} else {
			keySig = "RSA";
		}

		byte[] signatureArray = null;

		try {
			// Generate keypair
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keySig);
			keyGen.initialize(1024);
			KeyPair key = keyGen.generateKeyPair();

			// Get a signature object using the MD5 and RSA combo
			// and sign the input with the private key
			//Signature sig = Signature.getInstance(signaturemethod);
			Signature sig = Signature.getInstance(signaturemethod);
			sig.initSign(key.getPrivate());
			sig.update(input);
			signatureArray = sig.sign();

			signature = new String(Hash.bytesToHex(signatureArray)); // Hex
																		// String
			org.jcryptool.visual.sig.algorithm.Input.signature = signatureArray;

			return signatureArray;
		
		} catch (Exception ex) {
			LogUtil.logError(SigPlugin.PLUGIN_ID, ex);
		}
		return signatureArray;
	}

}
