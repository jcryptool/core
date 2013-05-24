package org.jcryptool.visual.sig.algorithm;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
//import org.jcryptool.visual.jctca.listeners.SignatureNotifier;

//This class is used to share data between classes
public class Input {
	
	public static byte[] data; //Contains the input data
	
	public static String path = ""; //Contains the path to the input data
	
	public static byte[] hash; //Contains the hash of the input data
	public static String hashHex;
	
	public static byte[] signature; //Contains the signature of the input data
	public static String signatureHex;
	
	public static String chosenHash; 
	
	public static KeyStoreAlias privateKey; //Contains the private key used to sign the data
	public static KeyStoreAlias publicKey; //Contains the public key used to verify the data in the JCTCA plugin
	
	//public static SignatureNotifier signNotifier; //contains the notifier of the JCTCA plugin who should be called when something is signed from their plugin
	
	/**
	 * 
	 * Converts a given byte array (signature, hash, ...) to it's hexadecimal representation
	 * 
	 * @param bytes A byte array
	 * @return The hex representation of the byte array 
	 */
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
