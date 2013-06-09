package org.jcryptool.visual.sig.algorithm;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
//import java.security.cert.Certificate;
import java.util.Date;

import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.sig.listener.SignatureEvent;
import org.jcryptool.visual.sig.listener.SignatureListener;
import org.jcryptool.visual.sig.listener.SignatureListenerAdder;

public class SigGeneration {
	public String signature;
	//private final static HashMap<String, KeyStoreAlias> keystoreitems = new HashMap<String, KeyStoreAlias>();
	private static PrivateKey k = null;
	//private static Certificate pubKey = null;
	
	
	/**
	 * Old version of SignInput, calls new version of the method
	 */
	public static byte[] SignInput(String signaturemethod, byte[] input) throws Exception {
		
		return SignInput(signaturemethod, input, null);
	}
	
	/**
	 * This method signs the data stored in Input.java with the signature method selected by the user. 
	 * It either uses the user selected key or the key given by jctca (stored in Input.java).
	 * 
	 * @param signaturemethod Chosen signature method to sign the hash.
	 * @param input The Data the user selected
	 * @return The signature (byte array)
	 * @throws Exception
	 */
	public static byte[] SignInput(String signaturemethod, byte[] input, KeyStoreAlias alias) throws Exception {
		byte[] signature = null; //Stores the signature
		
		org.jcryptool.visual.sig.algorithm.Input.chosenHash = signaturemethod.replace("withRSA", "");
		
		//Eigene sach fia ecdsa....
		if (signaturemethod.contains("ECDSA")) { //Generate a key because there are no ECDSA Keys in the keystore
	        //Generate a key pair
	        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
	        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

	        keyGen.initialize(256, random);

	        KeyPair pair = keyGen.generateKeyPair();
	        k = pair.getPrivate();
		} else {
			KeyStoreManager ksm = KeyStoreManager.getInstance();
			ksm.loadKeyStore(KeyStorePlugin.getPlatformKeyStoreURI());
			//Check if called by JCT-CA
			if (org.jcryptool.visual.sig.algorithm.Input.privateKey != null) { //Use their key
				org.jcryptool.visual.sig.algorithm.Input.privateKey.getAliasString();
				k = ksm.getPrivateKey(org.jcryptool.visual.sig.algorithm.Input.privateKey, KeyStoreManager.getDefaultKeyPassword());
				
				//Get the public key (only with required with RSA and if called)
//		        pubKey = ksm.getPublicKey(alias); 
//		        pubKey.getPublicKey();
				/*
				String p = null;
				String t = null;
				if (org.jcryptool.visual.sig.algorithm.Input.data != null) {
					t = org.jcryptool.visual.sig.algorithm.Input.data.toString();
				} else {
					p = org.jcryptool.visual.sig.algorithm.Input.path;
				}
				
				for(SignatureListener lst : SignatureListenerAdder.getListeners()){      	
	        		lst.signaturePerformed(new SignatureEvent(signature, p, t, new Date(System.currentTimeMillis()), alias, alias, org.jcryptool.visual.sig.algorithm.Input.chosenHash));
				}
				*/
			} else { //Use own Key from given alias
				k = ksm.getPrivateKey(alias, KeyStoreManager.getDefaultKeyPassword());
				
				//org.jcryptool.visual.sig.algorithm.Input.privateKey = (KeyStoreAlias) k;
			}
		}//end else
		
		
		
		// Get a signature object using the specified combo and sign the data with the private key
		Signature sig = Signature.getInstance(signaturemethod);
		sig.initSign(k);
        sig.update(input);
        signature = sig.sign();    
        
        if (org.jcryptool.visual.sig.algorithm.Input.privateKey != null) { //if called
        	String p = null;
				String t = null;
				if (org.jcryptool.visual.sig.algorithm.Input.data != null) {
					t = new String(org.jcryptool.visual.sig.algorithm.Input.data);
				} else {
					p = org.jcryptool.visual.sig.algorithm.Input.path;
				}
				
				for(SignatureListener lst : SignatureListenerAdder.getListeners()){      	
	        		lst.signaturePerformed(new SignatureEvent(signature, //byte array
	        													p, //path
	        													t, //direct input
	        													new Date(System.currentTimeMillis()), //date time
	        													alias, //private key
	        													org.jcryptool.visual.sig.algorithm.Input.publicKey, //public key
	        													org.jcryptool.visual.sig.algorithm.Input.chosenHash)); //hash method string
				}
        }
        
        //Store the generated signature
        org.jcryptool.visual.sig.algorithm.Input.signature = signature; //Store the generated original signature
	    org.jcryptool.visual.sig.algorithm.Input.signatureHex = org.jcryptool.visual.sig.algorithm.Input.bytesToHex(signature); //Hex String
	    org.jcryptool.visual.sig.algorithm.Input.signatureOct = org.jcryptool.visual.sig.algorithm.Input.toOctalString(signature, "");
//	    org.jcryptool.visual.sig.algorithm.Input.signatureDec = org.jcryptool.visual.sig.algorithm.Input.toHexString(signature);

	    return signature;		
	}
	
}
