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
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
        KeyPair key = keyGen.generateKeyPair();
        
/*
        //Encrypt the md to create Signature
        Cipher cipher = Cipher.getInstance(signaturemethod);
        cipher.init(Cipher.ENCRYPT_MODE, key.getPrivate());
        
        byte[] cipherText = cipher.doFinal(input);
        org.jcryptool.visual.sig.algorithm.Input.signature = cipherText;
       
		return cipherText;
*/

// TODO!!!

        // Get a signature object using the MD5 and RSA combo
        // and sign the plaintext with the private key,
        // listing the provider along the way
//        Signature sig = Signature.getInstance("MD5WithRSA");
//        sig.initSign(key.getPrivate());
//        sig.update(plainText);
//        byte[] signature = sig.sign();
//        System.out.println( sig.getProvider().getInfo() );
//        System.out.println( "\nSignature:" );
//        System.out.println( new String(signature, "UTF8") );

        return null;
	}

}
