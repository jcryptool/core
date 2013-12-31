
package org.jcryptool.visual.sigVerification.algorithm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import java.security.KeyPair;
import java.util.Arrays;

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
    		KeyPairGenerator kpg = KeyPairGenerator.getInstance(signaturemethod, "FlexiCore"); //signaturemethod -> RSA, DES,.. 
    		Cipher cipher = Cipher.getInstance(signaturemethod, "FlexiCore");
    		
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
    }
    
    public static void verifyInput(String signaturemethod, byte[] signature, PublicKey pubKey) throws Exception{
        //Input.hashNew  = decrypt(signature, pubKey, signaturemethod);
        System.out.println(Input.hashNew);		// ToDo Löschen -> ist nur zu Testzwecken
    }
    
    private static byte[] decrypt(byte[] inpBytes, PublicKey key, String algorithm) throws Exception{ 
    	Cipher cipher = Cipher.getInstance(algorithm); 
    	cipher.init(Cipher.DECRYPT_MODE, key);
    	return cipher.doFinal(inpBytes); 
    }
}
