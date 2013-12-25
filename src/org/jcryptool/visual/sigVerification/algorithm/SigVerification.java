package org.jcryptool.visual.sigVerification.algorithm;

import java.security.KeyPairGenerator;
import java.security.PublicKey;
import javax.crypto.Cipher;
import java.security.KeyPair;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * Verifies a signature for the input with the selected signature methods.
 * 
 * @author Wilfing
 */
public class SigVerification {
    

    /**
     * 
     */
    public SigVerification(String signaturemethod, byte[] signature, byte[] pubKey) throws Exception {
    	// KeyPair erzeugen
    	KeyPairGenerator generator = KeyPairGenerator.getInstance(signaturemethod); //sigmethod so ändernd, dass RSA, DES,.. drinnen steht.
        generator.initialize(1024);
        KeyPair kp = generator.generateKeyPair();
        PublicKey publicKey = kp.getPublic();
        Input.publicKey = (KeyStoreAlias) publicKey;
        
        verifyInput(signaturemethod, signature, publicKey);        
    }
    
    public static void verifyInput(String signaturemethod, byte[] signature, PublicKey pubKey) throws Exception{
        Input.hashNew  = decrypt(signature, pubKey, signaturemethod);              
    }
    
    private static byte[] decrypt(byte[] inpBytes, PublicKey key, String algorithm) throws Exception{ 
    	Cipher cipher = Cipher.getInstance(algorithm); 
    	cipher.init(Cipher.DECRYPT_MODE, key); 
    	return cipher.doFinal(inpBytes); 
    }
}
