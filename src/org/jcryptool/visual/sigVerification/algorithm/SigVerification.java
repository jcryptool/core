
package org.jcryptool.visual.sigVerification.algorithm;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.X509EncodedKeySpec;

import java.util.Enumeration;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.core.logging.utils.LogUtil;
//import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;

import de.flexiprovider.core.dsa.DSAPrivateKey;
import de.flexiprovider.core.rsa.RSAPrivateCrtKey;

/**
 * Verifies the signature of the input with the selected signature method.
 * 
 * @author Wilfing
 */
public class SigVerification {	
	boolean result;		    //Contains the result of the comparison between the hashes.
    public Hash hashNew = new Hash();
    private PublicKey publicKey = null;
    private KeyStoreAlias privateKey = null;
	
	/**
	 * Chooses the correct function to verify the signature for the input with the selected signature method.
	 * 
	 * @param input A instance of Input
	 * @param hash A instance of Hash
	 */
	public void verifySignature(Input input, Hash hash){
    	if (input.signaturemethod == "RSA" || input.signaturemethod == "RSA and MGF1" || input.signaturemethod == "DSA"){
    		if (this.privateKey != null){
    			verifySig(input, hash);
    		}else{
    			setPublicKey(input);
    			verifySig(input, hash);
    		}    	
    	}else if (input.signaturemethod == "ECDSA"){
    		if (this.publicKey != null){
    			verifyECDSA(input, hash);
    		}else{
    			setKeyECDSA(input);
    			verifyECDSA(input, hash);
    		}
    	}
    }
	
	/**
	 * Sets the public keys.
	 * 
	 * @param input A instance of Input (contains the signature size)
	 */
	public void setPublicKey(Input input){
		try{
			KeyStoreManager ksm = KeyStoreManager.getInstance();
            //System.out.println(ksm.getAllPublicKeys());
            KeyStoreAlias alias;
            Enumeration<String> aliases = ksm.getAliases();
            while (aliases != null && aliases.hasMoreElements()) {
                alias = new KeyStoreAlias(aliases.nextElement());
                alias.getAliasString();
                if (input.signaturemethod == "RSA" || input.signaturemethod == "RSA and MGF1") { // RSA
                    if (alias.getClassName().equals(RSAPrivateCrtKey.class.getName())) {
                    	System.out.println("RSA PrivateCrtKey gefunden");
                        Certificate cert = ksm.getCertificate(alias);
                        // input.signatureSize = alias.getKeyLength();
                        this.publicKey = cert.getPublicKey();                      
                    }                   
                }else if(input.signaturemethod == "DSA"){ // DSA
                	if (alias.getClassName().equals(DSAPrivateKey.class.getName())) {
                        // Fill in keys
                		System.out.println("DSA PrivateKey gefunden");
                		Certificate cert = ksm.getCertificate(alias);
                        this.publicKey = cert.getPublicKey();
                    } // end if
                }
            }
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
    public void verifySig(Input input, Hash hash){
    	try{
    		Signature signature = Signature.getInstance(hash.hashmethod + "with" + input.signaturemethod, "FlexiCore");
            signature.initVerify(this.publicKey);

            //Signatur updaten
            signature.update(input.plain);

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
     * Verifies a DSA signature. Sets the variable result (boolean) TRUE if the signature is correct.
     * 
     * input A instance of Input (contains the signature)
     * @param hash A instance of Hash (contains the hash)
     */
    public void verifyDsa(Input input, Hash hash){
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
     * Selects the right function to convert the input key (RSA, DSA, ECDSA).
     * 
     * @param pubKeyBytes
     * @param input A instance of Input (contains signaturemethod)
     */
    public void publicKeyFile(byte[] pubKeyBytes, Input input){
    	if (input.signaturemethod == "RSA" || input.signaturemethod == "DSA"){
    		setDSARSAPublicKeyFile(pubKeyBytes, input);
    	}else{
    		;//ECDSA noch keine Methode zum Einlesen von ECDSA keys gefunden
    	}
    }
    
    /**
     * Converts the imported key (byte array) in a DSA/RSA public key.
     * 
     * @param pubKeyBytes A byte array
     * @param input A instance of Input
     */
    public void setDSARSAPublicKeyFile(byte[] pubKeyBytes, Input input){
        try{
        	X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
        	KeyFactory keyFactory = KeyFactory.getInstance(input.signaturemethod);
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
    public void setPrivateKey(KeyStoreAlias privKey){
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
    
    /**
     * Resets this Object.
     */
    public void reset(){
    	this.result = false;
    	this.hashNew = null;
    	this.privateKey = null;
    	this.publicKey = null;    	
    }
}
