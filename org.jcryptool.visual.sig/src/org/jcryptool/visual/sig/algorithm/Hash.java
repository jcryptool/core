package org.jcryptool.visual.sig.algorithm;

import java.security.*;
//import javax.crypto.*;

/**
 * Hashes the given input with one of the selected hash methods.
 * @author Grebe
 *
 */
public class Hash {
	public static String hash;
	//public static String hashmethod;

	/**
	 * This method hashes an input stored in Input.java with a given hash method
	 * @throws Exception
	 * @param hashmethod The name of the method (a string)
	 */
	public static byte[] hashInput (String hashmethod) throws Exception {
		//Get input: 
		byte[] input = org.jcryptool.visual.sig.algorithm.Input.data; //Contains the input (a file or plain text)
		//Get an MD5 message digest object and compute the plaintext digest
	    MessageDigest messageDigest = MessageDigest.getInstance(hashmethod); //Argument is a string!
	    messageDigest.update(input);
	    //Output:
	    byte[] md = messageDigest.digest();
	    //hash = new String(md, "UTF8"); //UTF8 String
	    hash = new String (bytesToHex(md)); //Hex String
	    org.jcryptool.visual.sig.algorithm.Input.hash = md; //Store the generated hash
	    return md;
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
