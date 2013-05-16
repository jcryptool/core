package org.jcryptool.visual.sig.algorithm;

import java.security.*;
import javax.crypto.*;

public class SigGeneration {
	
	/**
	 * This method signed a hash stored in Hash.jave with a given signature method.
	 * 
	 * @param signaturemethod Chosen signature method to sign the hash.
	 * @param input hash form Hash.java
	 * @return
	 * @throws Exception
	 */
	public static byte[] SignInput (String signaturemethod, byte[] input) throws Exception {
		
		//Generate keypair 
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(signaturemethod);
		keyGen.initialize(1024);
        KeyPair key = keyGen.generateKeyPair();
        
        //Encrypt the md to create Signature
        Cipher cipher = Cipher.getInstance(signaturemethod);
        cipher.init(Cipher.ENCRYPT_MODE, key.getPrivate());
        
        byte[] cipherText = cipher.doFinal(input);
        org.jcryptool.visual.sig.algorithm.Input.signature = cipherText;
        
		return cipherText;
	}

}
