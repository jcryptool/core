package org.jcryptool.visual.wots.files;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
//import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

public class Converter {
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
// For Testin purpose
	
//	public static void main(String[] args) {
//		
//		byte[] seed;
//		wots.WinternitzOTS instance = new wots.WinternitzOTS(42);
//		files.PseudorandomFunction prf = new files.AESPRF.AES128();
//		int n = prf.getLength();
//	    SecureRandom sRandom = new SecureRandom();
//	    seed = new byte[n];
//	    
//	    sRandom.nextBytes(seed);
//
//	    byte[] x = new byte[n];
//		
//	    sRandom.nextBytes(x);
//	    instance.init(prf, x);
//	    
//	    instance.generatePrivateKey(seed);
//	    
//	    byte[][] key = instance.getPrivateKey();
//	    
//	    System.out.println(_2dByteToHex(key));
//	    System.out.println(_byteToHex(key[0]));
//	    System.out.println("##############################");
//	    //System.out.println(key[0]);
//	    String output = _2dByteToHex(key);
//	    //System.out.println(output);
//	    
//	    byte[][] output2 = _hexStringTo2dByte(output, instance.getLength());
//	    
//	    System.out.println(_2dByteToHex(output2));
//	    
//	    System.out.println(_byteToHex(output2[0]));
//		
//	}
	
	/**
	 * Parses byte[][] to Hex-String
	 * @param input
	 * @return
	 */
	public static String _2dByteToHex (byte[][] input) {
		
		String output = "";
		
		for (int i = 0; i < input.length; i++) {
	    	output += _byteToHex(input[i]);
	    }
		
		// TODO 
		System.out.println("§§§§" + output);
		
		return output;
	}
	
	/**
	 * Parses byte[] to Hex-String
	 * @param input
	 * @return
	 */
	public static String _byteToHex (byte[] input) {
		//return Hex.toHexString(input);
		char[] hexChars = new char[input.length * 2];
	    for ( int j = 0; j < input.length; j++ ) {
	        int v = input[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    
	    // TODO 
	 	System.out.println("§§§§" + new String(hexChars));
	 		
	    return new String(hexChars);
	}
	
	/**
	 * Parses byte[] to String
	 * @param input
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String _byteToString (byte[] input) throws UnsupportedEncodingException {
		
		// TODO 
		System.out.println("§§§§" + new String(input, "UTF-8"));
		
		return new String(input, "UTF-8");
	}
	
	/**
	 * Parses Hex-String to byte[][]
	 * @param input
	 * @param l
	 * @return
	 */
	public static byte[][] _hexStringTo2dByte (String input, int l) {
		
		byte[][] output = new byte[l][];
		
		int s = input.length()/l;
		
		for (int i = 0; i < l; i++) {
			output[i] = _hexStringToByte(input.substring(i*s,s+i*s));
		}
		
		return output;
	}
	
	/**
	 * Parses Hex-String to byte[]
	 * @param input
	 * @return
	 */
	public static byte[] _hexStringToByte (String input) {
		return DatatypeConverter.parseHexBinary(input);
	}
	
	/**
	 * Parses String to byte[]
	 * @param input
	 * @return
	 */
	public static byte[] _stringToByte (String input) {
		return input.getBytes(Charset.forName("UTF-8"));
	}
}