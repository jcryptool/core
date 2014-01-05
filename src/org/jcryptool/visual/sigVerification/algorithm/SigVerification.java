
package org.jcryptool.visual.sigVerification.algorithm;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

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
	/**
     * Contains the result of the comparison between the hashes.
     */
	boolean result;
    public Hash hashNew = new Hash();
	
	public void verifySignature(Input input, Hash hash){
    	if (input.signaturemethod == "RSA"){
    		if (input.privateKey != null){
    			verifyRSA(input, hash);
    		}else{
    			setKeyRSA(input);
    			verifyRSA(input, hash);
    		}
    	}else if (input.signaturemethod == "DSA"){
    		if (input.publicKey != null){
    			verifyDSA(input, hash);
    		}else{
    			setKeyDSA(input);
    			verifyDSA(input, hash);
    		}
    	}else if (input.signaturemethod == "ECDSA"){
    		if (input.publicKey != null){
    			verifyECDSA(input, hash);
    		}else{
    			setKeyECDSA(input);
    			verifyECDSA(input, hash);
    		}
    	}else{
    		;
    	}
    }
	
	public static void setKeyRSA(Input input){
		try{
			// KeyPair erzeugen
    		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA"); //signaturemethod -> RSA, DES,..    		
    		
    		kpg.initialize(input.signatureSize);			// signatureSize -> 1024 (bit)
    		KeyPair keyPair = kpg.generateKeyPair();
    		PrivateKey privKey = keyPair.getPrivate();
    		PublicKey pubKey = keyPair.getPublic();
    		input.privateKey = privKey;
    		input.publicKey = pubKey;   		
		}catch (Exception ex){
			LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
		}
	}
	
    public void verifyRSA(Input input, Hash hash){
    	try{
    		Cipher cipher = Cipher.getInstance("RSA");
       		cipher.init(Cipher.DECRYPT_MODE, input.privateKey);
    		hashNew.setHash(cipher.doFinal(input.signature));    		
    		hashNew.setHashHex();
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    	verifyInput(hash.hash, hashNew.hash);
    }
    
    public static void setKeyECDSA(Input input){
    	try{
    	KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC"); //$NON-NLS-1$
		keyGen.initialize(256, SecureRandom.getInstance("SHA1PRNG")); //$NON-NLS-1$
		KeyPair pair = keyGen.generateKeyPair();
		input.publicKey = pair.getPublic();
    	}catch (Exception ex){
			LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    }
    
    public void verifyECDSA(Input input, Hash hash){
    	try{   		
    		Signature sig = Signature.getInstance(input.signaturemethod);
    		sig.initVerify(input.publicKey);
    		sig.update(hash.hash);
    		this.result = sig.verify(input.signature);
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    }
    
    public static void setKeyDSA(Input input){
    	try {
    		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");		
			keyGen.initialize(1024);
			KeyPair keypair = keyGen.genKeyPair();
			PublicKey publicKey = keypair.getPublic();			
			input.publicKey = publicKey;
		} catch (Exception ex) {
			LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
		}
    }
    
    public void verifyDSA(Input input, Hash hash){
    	try{    		    	
    		Signature sig = Signature.getInstance(input.signaturemethod);
    		sig.initVerify(input.publicKey);
    		sig.update(hash.hash);
    		this.result = sig.verify(input.signature);
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    }
    
    public boolean getResult(){
    	return this.result;
    }
    
    /**
     * Compares the hashed plaintext with the decrypted signature
     * 
     * @return true or false if the two hashes are equal or not
     */
    public void verifyInput(byte[] hash, byte[] hashNew){
        // Vergleicht die Hashes.
    	this.result = java.util.Arrays.equals(hash, hashNew);               
    }
    
    public void DSAPublicKeyFile(byte[] pubKeyBytes){
        try{
        	X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
        	KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        	PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
        }catch(Exception ex){
        	LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
        }
    }
    

}
