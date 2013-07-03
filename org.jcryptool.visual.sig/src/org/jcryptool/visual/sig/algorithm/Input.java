package org.jcryptool.visual.sig.algorithm;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

// import org.jcryptool.visual.jctca.listeners.SignatureNotifier;

/**
 * This class is used to share data between classes.
 * 
 * @author Grebe
 * 
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
    public static String path = "";

    public static int s = -1; // the chosen string (integer)

    public static int h = -1; // the chosen hash (integer)

    /**
     * Contains the hash of the input data (byte array)
     */
    public static byte[] hash;

    /**
     * Contains the hash of the input data (hex representation)
     */
    public static String hashHex;

    /**
     * Contains the signature of the input data (byte array)
     */
    public static byte[] signature;

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
     * Contains the private key used to sign the data (given by JCTCA plugin)
     */
    public static KeyStoreAlias privateKey;

    /**
     * Contains the public key used to verify the data in the JCTCA plugin
     */
    public static KeyStoreAlias publicKey;

    /**
     * Contains the private key used to sign the data (chosen in our plugin)
     */
    public static KeyStoreAlias key;

    /**
     * This method resets all variables in this class to their initial value
     */
    public static void reset() {
        data = null;
        path = null;
        hash = null;
        hashHex = null;
        signature = null;
        signatureHex = null;
        signatureOct = null;
        privateKey = null;
        publicKey = null;
        h = -1;
        s = -1;
    }

    // public static SignatureNotifier signNotifier; //contains the notifier of the JCTCA plugin who should be called
    // when something is signed from their plugin

    /**
     * 
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
        // if (prefix == null) {
        // for (int i=0; i< bytes.length; i++) {
        // appendOctalDigits(sb, bytes[i]);
        // }
        // } else {
        for (int i = 0; i < bytes.length; i++) {
            sb.append(prefix);
            appendOctalDigits(sb, bytes[i]);
            // }
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
