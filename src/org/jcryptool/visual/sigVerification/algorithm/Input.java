package org.jcryptool.visual.sigVerification.algorithm;

import java.security.PublicKey;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * This class is used to share data between classes.
 * 
 * @author Wilfing/Grebe
 */
public class Input {
    /**
     * Contains the input data
     */
    public static byte[] data;
    public static String dataHex;

    /**
     * Contains the path to the input data
     */
    public static String path = ""; //$NON-NLS-1$

    public static int h = -1; // the chosen hash (integer)

    public static int s = -1; // the chosen string (integer)
    
    /**
     * Contains the hash of the plain text in input data (byte array)
     */
    public static byte[] hashNew;

    /**
     * Contains the hash of the plain text input data (hex representation)
     */
    public static String hashHexNew;

    /**
     * Contains the hash stored in the input data (byte array)
     */
    public static byte[] hash;

    /**
     * Contains the hash stored in the input data (hex representation)
     */
    public static String hashHex;
    
    /**
     * Contains the signature of the input data (byte array)
     */
    public static byte[] signature;

    /**
     * Contains the plain text of the input data (byte array)
     */
    public static byte[] plain;
    
    public static PublicKey publicKey;
    
    /**
     * Contains the signature of the input data (hex representation)
     */
    public static String signatureHex;

    /**
     * Contains the signature of the input data (octal)
     */
    public static String signatureOct;

    /**
     * The name of the chosen hash method ("SHA-256" etc.)
     */
    public static String chosenHash;

    /**
     * The name of the chosen signature method ("RSA" etc.)
     */
    public static String signaturemethod="";
    
    /**
     * The size in bit of the chosen signature method ("RSA" = 1024 etc.)
     */
    public static int signatureSize;
    
    /**
     * Contains the private key used to sign the data (given by JCTCA plugin)
     */
    public static KeyStoreAlias privateKey;

    /**
     * Contains the public key used to verify the data in the JCTCA plugin
     */
    //public static KeyStoreAlias publicKey;
    
    /**
     * Contains the private key used to sign the data (chosen in our plugin)
     */
    public static KeyStoreAlias key;
    
    /**
     * Contains the result of the comparison between the hashes.
     */
    public static boolean result;

    /**
     * This method resets all variables in this class to their initial value
     */
    public static void reset() {
        data = null;
        path = null;
        hash = null;
        hashHex = null;
        hashNew = null;
        hashHexNew = null;
        signature = null;
        signatureHex = null;
        signatureOct = null;
        privateKey = null;
        publicKey = null;
        h = -1;
    }
    
    /**
     * Sets the signaturemethod with the used method.
     * 
     * @return void
     */
    public static void setSignaturemethod(){
    	switch(Input.s){
        case 0:             
            Input.signaturemethod = "DSA";
            break;
        case 1:
        	Input.signaturemethod = "RSA";
            break;
        case 2:
        	Input.signaturemethod = "ECDSA";
            break;
        case 3:             
        	Input.signaturemethod = "RSA and MGF1"; //????
            break;
        default:
        	Input.signaturemethod = "";
            break;
    	}
    }
    
    /**
     * Sets signatureSize to the size of the signature in bit.
     * 
     * @return void
     */
    public static void setSignatureSize(){
    	switch (Input.s){
        case 1:             // DSA 368 Bit -> 46 Byte
            Input.signatureSize = 368;
            break;
        case 2:             // RSA, RSA und MGF1 1024 Bit -> 128 Byte
        case 4:
        	Input.signatureSize = 1024;
            break;
        case 3:             // ECDSA 560 Bit -> 70 Byte
        	Input.signatureSize = 560;
            break;
        default:
        	Input.signatureSize = 0;
            break;
    	}
    }
    
    
    /**
     * Takes the input data and devides it into the signature and the plaintext.
     * 
     * @return void
     */
    public static void divideSignaturePlaintext(){      
        int sigSize = Input.signatureSize/8;	// Länge der Signatur von Bit in Byte umwandeln.        
       
       // Trennt in die Inputdaten auf in Signatur und Plaintext. Der vordere Teil ist Signatur.
       Input.signature = java.util.Arrays.copyOfRange(Input.data, 0, sigSize);
       Input.plain = java.util.Arrays.copyOfRange(data, sigSize, Input.data.length);
    }
    
    
    /**
     * Compares the hashed plaintext with the decrypted signature
     * 
     * @return true or false if the two hashes are equal or not
     */
    public static boolean compareHashes(){
        // Vergleicht die Hashes.
        return java.util.Arrays.equals(Input.hash, Input.hashNew);       
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

    /**
     * Returns the octal String representation of a byte array with optional prefix. The String is formed by making
     * value[0] the leftmost three digits and value[value.length-1] the rightmost three digits.
     * 
     * @param array the byte array
     */
    public final static String toOctalString(byte[] bytes, String prefix) {
        StringBuffer sb = new StringBuffer(bytes.length * (3 + prefix.length()) + 8);

        for (int i = 0; i < bytes.length; i++) {
            sb.append(prefix);
            appendOctalDigits(sb, bytes[i]);
        }

        return sb.toString();
    }

    /**
     * Returns the octal digit String buffer representation of a byte.
     * 
     * @param byte the byte
     */
    private final static StringBuffer appendOctalDigits(StringBuffer sb, byte b) {
        // b will be promote to integer first, mask with 0x07 is a must.
        return sb.append(Character.forDigit(b >>> 6 & 0x07, 8)).append(Character.forDigit(b >>> 3 & 0x07, 8))
                .append(Character.forDigit(b & 0x07, 8));
    }
}
