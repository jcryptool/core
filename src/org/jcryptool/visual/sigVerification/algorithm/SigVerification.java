
package org.jcryptool.visual.sigVerification.algorithm;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
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
	
	/**
	 * Chooses the correct function to verify the signature for the input with the selected signature method.
	 * 
	 * @param input A instance of Input
	 * @param hash A instance of Hash
	 */
	public void verifySignature(Input input, Hash hash){
    	if (input.signaturemethod == "RSA" || input.signaturemethod == "DSA" || input.signaturemethod == "RSA and MGF1"){
    		if (this.publicKey != null){
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
	 * Sets the RSA and DSA public key.
	 * 
	 * @param input A instance of Input (contains the signaturemethod)
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
                        Certificate cert = ksm.getCertificate(alias);
                        // input.signatureSize = alias.getKeyLength();                        
                        this.publicKey = cert.getPublicKey();
                    	System.out.println("RSA PrivateCrtKey found");
                    }                   
                }else if(input.signaturemethod == "DSA"){ // DSA
                	if (alias.getClassName().equals(DSAPrivateKey.class.getName())) {
                        // Fill in keys
                		Certificate cert = ksm.getCertificate(alias);
                        this.publicKey = cert.getPublicKey();
                        
                		System.out.println("DSA PrivateKey found");
                    } // end if
                }
            }
		}catch (Exception ex){
			LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
		}
	}
	
    
    /**
     * Verifies RSA, DSA and RSA with MGF1 signatures. Sets the variable result (boolean) TRUE if the signature is correct.
     * 
     * @param input A instance of Input (contains the signature, the plaintext, and the signaturemethod)
     * @param hash A instance of Hash (contains the hashmethod)
     */
    public void verifySig(Input input, Hash hash){
    	try{
    		Signature signature;
    		if (input.signaturemethod == "RSA and MGF1"){
    			signature = Signature.getInstance(hash.hashmethod + "WithRSA/PSS", "BC");
    		}else{
    			signature = Signature.getInstance(hash.hashmethod + "with" + input.signaturemethod, "FlexiCore");
    		}
            signature.initVerify(this.publicKey);

            //Signatur updaten
            signature.update(input.plain);

            //Signatur ausgeben
            this.result = signature.verify(input.signature);
            
            System.out.println("Signature Verification was correct: " + this.result);
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
     * Selects the right function to convert the input key (RSA, DSA, ECDSA).
     * 
     * @param pubKeyBytes
     * @param input A instance of Input (contains signaturemethod)
     */
    public void publicKeyFile(byte[] pubKeyBytes, Input input){
    	if (input.signaturemethod == "RSA" || input.signaturemethod == "DSA"){
    		setDsaRsaPublicKeyFile(pubKeyBytes, input);
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
    public void setDsaRsaPublicKeyFile(byte[] pubKeyBytes, Input input){
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
     * Sets the public Key.
     * 
     * @param pubKey PublicKey
     */
    public void setPublicKey(PublicKey pubKey){
    	this.publicKey = pubKey;
    }

    
    /**
     * Resets this instance.
     */
    public void reset(){
    	this.result = false;
    	this.hashNew = null;
    	this.publicKey = null;    	
    }
}
