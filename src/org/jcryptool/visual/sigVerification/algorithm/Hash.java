package org.jcryptool.visual.sigVerification.algorithm;

import java.security.MessageDigest;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;

/**
 * Hashes the given input with the selected hash methods.
 * 
 * @author Wilfing
 */
public class Hash {
	public byte[] hash = null;
	public String hashHex = null;
	public String hashmethod = null;
	
    /**
     * This method hashes an input stored in Input.java with a given hash method
     * 
     * @throws Exception
     * @param hashmethod The name of the method (a string)
     * @return the hash of the input as byte array
     */
    public byte[] hashInput(String hashmethod, byte[] input) throws Exception {
        byte[] md = null;
        // Den namen der Hashfunktion festlegen.
        setHashmethod(hashmethod);
        
        try {
            // Get an MD5 message digest object and compute the plaintext digest
            MessageDigest messageDigest = MessageDigest.getInstance(hashmethod); // Argument is a string!
            messageDigest.update(input);
            // Output:
            md = messageDigest.digest();

            this.hash = md; // Store the generated hash
            this.hashHex = bytesToHex(md); // Hex
        } catch (Exception ex) {
            LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
        }
        
        return md;
    }
    
    
    /**
     * Changing the name of the hashmethod to be compatible with the Flexiprovider. 
     * 
     * @param hashmethod A String with the name of the hashmethod 
     */
    public void setHashmethod(String hashmethod){
    	if(hashmethod == org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha1){
    		this.hashmethod = "SHA1";
        }else if (hashmethod == org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha256){
        	this.hashmethod = "SHA256";
        }else if (hashmethod == org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha384){
        	this.hashmethod = "SHA384";
        }else if (hashmethod == org.jcryptool.visual.sigVerification.ui.wizards.Messages.HashWizard_rdosha512){
        	this.hashmethod = "SHA512";
        }else{
        	this.hashmethod = hashmethod;
        }
    }
    
    /**
     * Sets the hash (byte array)
     * 
     * @param hash (byte array)
     */
    public void setHash(byte[] hash){
    	this.hash = hash;
    }
    
    /**
     * Sets the hashHex (String) by calling the function bytesToHex(bytes[] bytes).
     * 
     */
    public void setHashHex(){
    	this.hashHex = bytesToHex(this.hash);
    }
    
    /**
     * Sets the hashHex (String) by calling the function bytesToHex(bytes[] bytes).
     * 
     * @param hash A byte array
     */
    public void setHashHex(byte[] hash){
    	this.hashHex = bytesToHex(hash);
    }
        
    /**
     * Returns the byte array hash.
     * 
     * @return hash A byte array.
     */
    public byte[] getHash(){
    	return hash;
    }
    
    /**
     * Returns the string hashHex.
     * 
     * @return hashHex A string.
     */
    public String getHashHex(){
    	return hashHex;
    }
    
    /**
     * Resets this Object.
     */
    public void reset(){
    	this.hash = null;
    	this.hashHex = null;
    	this.hashmethod = null;
    }
    
    
    /**
     * Converts a given byte array (signature, hash, ...) to it's hexadecimal representation
     * 
     * @param bytes A byte array
     * @return The hex representation of the byte array
     */
    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}