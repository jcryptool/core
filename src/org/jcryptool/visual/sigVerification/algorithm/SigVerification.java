
package org.jcryptool.visual.sigVerification.algorithm;

import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.DSAPublicKey;


import javax.crypto.Cipher;

import org.jcryptool.core.logging.utils.LogUtil;
//import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;

/**
 * Verifies a signature for the input with the selected signature methods.
 * 
 * @author Wilfing
 */
public class SigVerification {
    
	public static void verifySignature(String signaturemethod){
    	if (signaturemethod == "RSA"){
    		verifyRSA(signaturemethod, Input.signature);
    	}else if (signaturemethod == "DSA"){
    		verifyDSA(signaturemethod, Input.signature);
    	}else if (signaturemethod == "ECDSA"){
    		verifyECDSA(signaturemethod, Input.signature);
    	}else{
    		;
    	}
    }
	
    public static void verifyRSA(String signaturemethod, byte[] signature){
    	try{
    		// KeyPair erzeugen
    		KeyPairGenerator kpg = KeyPairGenerator.getInstance(signaturemethod); //signaturemethod -> RSA, DES,.. 
    		Cipher cipher = Cipher.getInstance(signaturemethod);
    		
    		kpg.initialize(Input.signatureSize);			// signatureSize -> 1024 (bit)
    		KeyPair keyPair = kpg.generateKeyPair();
    		PrivateKey privKey = keyPair.getPrivate();
    		PublicKey pubKey = keyPair.getPublic();
    		Input.publicKey = pubKey;
    		
    		cipher.init(Cipher.DECRYPT_MODE, privKey);
    		Input.hashNew = cipher.doFinal(signature);    		
    		
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    	verifyInput(Input.hash, Input.hashNew);
    }
    
    
    public static void verifyECDSA(String signaturemethod, byte[] signature){
    	try{
    		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC"); //$NON-NLS-1$
    		keyGen.initialize(256, SecureRandom.getInstance("SHA1PRNG")); //$NON-NLS-1$
    		KeyPair pair = keyGen.generateKeyPair();
        
    		Signature sig = Signature.getInstance(signaturemethod);
    		sig.initVerify(pair.getPublic());
    		sig.update(Input.hash);
    		Input.result = sig.verify(signature);
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    }
    
    
    public static void verifyDSA(String signaturemethod, byte[] signature){
    	try{
    		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
    		keyGen.initialize(1024);
    		KeyPair keypair = keyGen.genKeyPair();
    		DSAPublicKey publicKey = (DSAPublicKey) keypair.getPublic();
    	
    		Signature sig = Signature.getInstance(signaturemethod);
    		sig.initVerify(publicKey);
    		sig.update(Input.hash);
    		Input.result = sig.verify(signature);
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    }
    
    /**
     * Compares the hashed plaintext with the decrypted signature
     * 
     * @return true or false if the two hashes are equal or not
     */
    public static void verifyInput(byte[] hash, byte[] hashNew){
        // Vergleicht die Hashes.
    	Input.result = java.util.Arrays.equals(hash, hashNew);               
    }

}
