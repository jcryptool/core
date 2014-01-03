
package org.jcryptool.visual.sigVerification.algorithm;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
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
    		if (Input.privateKey != null){
    			verifyRSA(signaturemethod, Input.signature, Input.privateKey);
    		}else{
    			setKeyRSA();
    			verifyRSA(signaturemethod, Input.signature, Input.privateKey);
    		}
    	}else if (signaturemethod == "DSA"){
    		if (Input.publicKey != null){
    			verifyDSA(signaturemethod, Input.signature, (DSAPublicKey) Input.publicKey);
    		}else{
    			setKeyDSA();
    			verifyDSA(signaturemethod, Input.signature, (DSAPublicKey) Input.publicKey);
    		}
    	}else if (signaturemethod == "ECDSA"){
    		if(Input.publicKey != null){
    			verifyECDSA(signaturemethod, Input.signature, Input.publicKey);
    		}else{
    			setKeyECDSA();
    			verifyECDSA(signaturemethod, Input.signature, Input.publicKey);
    		}
    	}else{
    		;
    	}
    }
	
	public static void setKeyRSA(){
		try{
			// KeyPair erzeugen
    		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA"); //signaturemethod -> RSA, DES,.. 
    		
    		
    		kpg.initialize(Input.signatureSize);			// signatureSize -> 1024 (bit)
    		KeyPair keyPair = kpg.generateKeyPair();
    		PrivateKey privKey = keyPair.getPrivate();
    		PublicKey pubKey = keyPair.getPublic();
    		Input.privateKey = privKey;
    		Input.publicKey = pubKey;   		
		}catch (Exception ex){
			LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
		}
	}
	
    public static void verifyRSA(String signaturemethod, byte[] signature, PrivateKey privKey){
    	try{
    		Cipher cipher = Cipher.getInstance("RSA");
       		cipher.init(Cipher.DECRYPT_MODE, privKey);
    		Input.hashNew = cipher.doFinal(signature);    		
    		
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    	verifyInput(Input.hash, Input.hashNew);
    }
    
    public static void setKeyECDSA(){
    	try{
    	KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC"); //$NON-NLS-1$
		keyGen.initialize(256, SecureRandom.getInstance("SHA1PRNG")); //$NON-NLS-1$
		KeyPair pair = keyGen.generateKeyPair();
		Input.publicKey = pair.getPublic();
    	}catch (Exception ex){
			LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    }
    
    public static void verifyECDSA(String signaturemethod, byte[] signature, PublicKey publicKey){
    	try{   		
    		Signature sig = Signature.getInstance(signaturemethod);
    		sig.initVerify(publicKey);
    		sig.update(Input.hash);
    		Input.result = sig.verify(signature);
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    }
    
    public static void setKeyDSA(){
    	try {
    		KeyPairGenerator keyGen;		
			keyGen = KeyPairGenerator.getInstance("DSA");		
			keyGen.initialize(1024);
			KeyPair keypair = keyGen.genKeyPair();
			DSAPublicKey publicKey = (DSAPublicKey) keypair.getPublic();
			Input.publicKey = publicKey;
		} catch (Exception ex) {
			LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
		}
    }
    
    public static void verifyDSA(String signaturemethod, byte[] signature, DSAPublicKey publicKey){
    	try{    		    	
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
