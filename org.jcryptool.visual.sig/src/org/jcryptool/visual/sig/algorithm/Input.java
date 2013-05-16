package org.jcryptool.visual.sig.algorithm;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

//This class is used to share data between classes
public class Input {
	
	public static byte[] data; //Contains the input data
	
	public static String path = ""; //Contains the path to the input data
	
	public static byte[] hash; //Contains the hash of the input data
	
	public static byte[] signature; //Contains the signature of the input data
	
	public static KeyStoreAlias privateKey; //Contains the private key used to sign the data
}
