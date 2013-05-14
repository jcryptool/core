package org.jcryptool.visual.sig.algorithm;

import java.security.*;
//import javax.crypto.*;

/**
 * Hashes the given input with one of the selected hash methods.
 * @author Grebe
 *
 */
public class Hash {
	private String hashmethod;
	/**
	 * @return the hashmethod
	 */
	public String getHashmethod() {
		return hashmethod;
	}

	/**
	 * @param hashmethod the hashmethod to set
	 */
	public void setHashmethod(String hashmethod) {
		this.hashmethod = hashmethod;
	}

	public static String hash;
	
	/**
	 * Standard constructor that sets the default method
	 */
	public Hash () {
		hashmethod = "MD5"; //MD5
	}
	
	/**
	 * Constructor that sets hash method and the input
	 */
	public Hash (String hashmethod) {
		hashmethod = this.hashmethod;
	}

	public void hashInput () throws Exception {
		//Get input:
		byte[] input = org.jcryptool.visual.sig.algorithm.Input.data; //Contains the input (a file or plain text)
		//Get an MD5 message digest object and compute the plaintext digest
	    MessageDigest messageDigest = MessageDigest.getInstance(hashmethod); //Argument is a string!
	    messageDigest.update(input);
	    //Output:
	    byte[] md = messageDigest.digest();
	    //Test
	    hash = new String(md, "UTF8");
	    hash = new String (bytesToHex(md));
	    //Test: recreate the message digest from the plaintext
	}
    
	//To display the sting in hex
	public static String bytesToHex(byte[] bytes) {
	    final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

}
