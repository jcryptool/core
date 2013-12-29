
package org.jcryptool.visual.sigVerification.algorithm;

import java.security.KeyPairGenerator;
import java.security.PublicKey;

import javax.crypto.Cipher;

import java.security.KeyPair;

import org.jcryptool.core.logging.utils.LogUtil;
//import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;

/**
 * Verifies a signature for the input with the selected signature methods.
 * 
 * @author Wilfing
 */
public class SigVerification {
    
    public static void setPublicKey(String signaturemethod, byte[] signature){
    	try{
    		// KeyPair erzeugen
    		KeyPairGenerator generator = KeyPairGenerator.getInstance(signaturemethod); //sigmethod so ändernd, dass RSA, DES,.. drinnen steht.
    		generator.initialize(Input.signatureSize);
    		KeyPair kp = generator.generateKeyPair();
    		PublicKey publicKey = kp.getPublic();
    		Input.publicKey = publicKey;
    		
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    }
    
    public static void verifyInput(String signaturemethod, byte[] signature, PublicKey pubKey) throws Exception{
        Input.hashNew  = decrypt(signature, pubKey, signaturemethod);
        System.out.println(Input.hashNew);		// ToDo Löschen -> ist nur zu Testzwecken
    }
    
    private static byte[] decrypt(byte[] inpBytes, PublicKey key, String algorithm) throws Exception{ 
    	Cipher cipher = Cipher.getInstance(algorithm); 
    	cipher.init(Cipher.DECRYPT_MODE, key);
    	return cipher.doFinal(inpBytes); 
    }
}
