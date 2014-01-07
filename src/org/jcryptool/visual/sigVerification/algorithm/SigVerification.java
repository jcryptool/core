
package org.jcryptool.visual.sigVerification.algorithm;

import java.io.FileInputStream;
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
import javax.security.cert.Certificate;

import org.jcryptool.core.logging.utils.LogUtil;
//import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;

/**
 * Verifies the signature of the input with the selected signature method.
 * 
 * @author Wilfing
 */
public class SigVerification {
	boolean result;		    //Contains the result of the comparison between the hashes.
    public Hash hashNew = new Hash();
    private PublicKey publicKey;
    private PrivateKey privateKey;
	
	/**
	 * Chooses the correct function to verify the signature for the input with the selected signature method.
	 * 
	 * @param input A instance of Input
	 * @param hash A intance of Hash
	 */
	public void verifySignature(Input input, Hash hash){
    	if (input.signaturemethod == "RSA"){
    		if (this.privateKey != null){
    			verifyRSA(input, hash);
    		}else{
    			setKeyRSA(input);
    			verifyRSA(input, hash);
    		}
    	}else if (input.signaturemethod == "DSA"){
    		if (this.publicKey != null){
    			verifyDSA(input, hash);
    		}else{
    			setKeyDSA(input);
    			verifyDSA(input);
    		}
    	}else if (input.signaturemethod == "ECDSA"){
    		if (this.publicKey != null){
    			verifyECDSA(input, hash);
    		}else{
    			setKeyECDSA(input);
    			verifyECDSA(input, hash);
    		}
    	}else{
    		;
    	}
    }
	
	/**
	 * Sets the RSA keys.
	 * 
	 * @param input A instance of Input (contains the signature size)
	 */
	public void setKeyRSA(Input input){
		try{
			// KeyPair erzeugen
    		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA"); //signaturemethod -> RSA, DES,..    		
    		
    		kpg.initialize(input.signatureSize);			// signatureSize -> 1024 (bit)
    		KeyPair keyPair = kpg.generateKeyPair();
    		PrivateKey privKey = keyPair.getPrivate();
    		PublicKey pubKey = keyPair.getPublic();
    		this.privateKey = privKey;
    		this.publicKey = pubKey;   		
		}catch (Exception ex){
			LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
		}
	}
	
    /*public void verifyRSA(Input input, Hash hash){
    	try{
    		Cipher cipher = Cipher.getInstance("RSA");
       		cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
    		hashNew.setHash(cipher.doFinal(input.signature));    		
    		hashNew.setHashHex();
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    	verifyInput(hash.hash, hashNew.hash);
    }*/
    
    /**
     * Verifies a RSA signature. Sets the variable result (boolean) TRUE if the signature is correct.
     * 
     * @param input A instance of Input (contains the signature)
     * @param hash A instance of Hash (contains the hash)
     */
    public void verifyRSA(Input input, Hash hash){
    	try{
    		Signature signature = Signature.getInstance("RSA");
            signature.initVerify(this.publicKey);

            //Signatur updaten
            signature.update(hash.hash);

            //Signatur ausgeben
            this.result = signature.verify(input.signature);
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    }
    
	/**
	 * Sets the ECDSA keys.
	 * 
	 * @param input A instance of Input.
	 */
    public void setKeyECDSA(Input input){
    	try{
    	KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC"); //$NON-NLS-1$
		keyGen.initialize(256, SecureRandom.getInstance("SHA1PRNG")); //$NON-NLS-1$
		KeyPair pair = keyGen.generateKeyPair();
		this.publicKey = pair.getPublic();
    	}catch (Exception ex){
			LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    }
    
    /**
     * Verifies a ECDSA signature. Sets the variable result (boolean) TRUE if the signature is correct.
     * 
     * @param input A instance of Input (contains the signature)
     * @param hash A instance of Hash (contains the hash)
     */
    public void verifyECDSA(Input input, Hash hash){
    	try{   		
    		Signature sig = Signature.getInstance(input.signaturemethod);
    		sig.initVerify(this.publicKey);
    		sig.update(hash.hash);
    		this.result = sig.verify(input.signature);
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    }
    
	/**
	 * Sets the DSA keys.
	 * 
	 * @param input A instance of Input.
	 */
    public void setKeyDSA(Input input){
    	try {
    		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");		
			keyGen.initialize(1024);
			KeyPair keypair = keyGen.genKeyPair();
			PublicKey publicKey = keypair.getPublic();			
			this.publicKey = publicKey;
		} catch (Exception ex) {
			LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
		}
    }
    
    /**
     * Verifies a DSA signature. Sets the variable result (boolean) TRUE if the signature is correct.
     * 
     * input A instance of Input (contains the signature)
     * @param hash A instance of Hash (contains the hash)
     */
    public void verifyDSA(Input input, Hash hash){
    	try{    		    	
    		Signature sig = Signature.getInstance(input.signaturemethod);
    		sig.initVerify(this.publicKey);
    		sig.update(hash.hash);
    		this.result = sig.verify(input.signature);
    	}catch(Exception ex){
    		LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
    	}
    }
    
    /**
     * Verifies a DSA signature. Sets the variable result (boolean) TRUE if the signature is correct.
     * 
     * @param input A instance of Input (contains the plaintext and the signature)
     */
    public void verifyDSA(Input input){
        //Signature-Objekt erstellen
        try {
        	Signature signature = Signature.getInstance("SHA1withDSA"); // zum Testen ob es mit SHA1 funktioniert
            signature.initVerify(this.publicKey);
            //Eingabedatei lesen
//            FileInputStream in = new FileInputStream(new String(input.plain));
//            int len;
//            byte[] data = new byte[1024];
//            while ((len = in.read(data)) > 0) {
              //Signatur updaten
            signature.update(input.plain);
//            }
//            in.close();
            //Signaturdatei einlesen
//            in = new FileInputStream(new String(input.signature));
//            len = in.read(data);
//            in.close();
            //Signatur ausgeben
            this.result = signature.verify(input.signature);
        }catch(Exception ex){
        	LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
        }
    	
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
    
    /**
     * Converts the imported key (byte array) in a DSA public key.
     * 
     * @param pubKeyBytes A byte array
     */
    public void DSAPublicKeyFile(byte[] pubKeyBytes){
        try{
        	X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
        	KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        	this.publicKey = keyFactory.generatePublic(pubKeySpec);
        }catch(Exception ex){
        	LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
        }
    }

    /**
     * Returns the result (boolean).
     * 
     * @return result A boolean
     */
    public boolean getResult(){
    	return this.result;
    }
    
    /**
     * Sets the private key.
     * 
     * @param privKey A PrivateKey
     */
    public void setPrivateKey(PrivateKey privKey){
    	this.privateKey = privKey;
    }

    /**
     * Sets the public Key.
     * 
     * @param pubKey PublicKey
     */
    public void setPublicKey(PublicKey pubKey){
    	this.publicKey = pubKey;
    }
}
