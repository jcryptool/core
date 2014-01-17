package org.jcryptool.visual.sigVerification.algorithm;

/**
 * This class is used to share data between classes.
 * 
 * @author Wilfing
 */
public class Input {
    /**
     * Contains the input data (signature + plaintext) (byte array)
     */
    public byte[] data;

    /**
     * Contains the path to the input data
     */
    public String path = ""; //$NON-NLS-1$

    public int h = -1; // the chosen hash (integer)

    public int s = -1; // the chosen string (integer)

    /**
     * Contains the plain text of the input data (byte array)
     */
    public byte[] plain;

    /**
     * Contains the signature of the input data (byte array)
     */
    public byte[] signature;

    /**
     * Contains the signature of the input data (hex representation)
     */
    public String signatureHex;

    /**
     * Contains the signature of the input data (octal)
     */
    public String signatureOct;

    /**
     * The name of the chosen hash method ("SHA-256" etc.)
     */
    public String chosenHash;

    /**
     * The name of the chosen signature method ("RSA" etc.)
     */
    public String signaturemethod = null;

    /**
     * The size in bit of the chosen signature method ("RSA" = 1024 etc.)
     */
    public int signatureSize;

    /**
     * This method resets all variables in this class to their initial value
     */
    public void reset(Hash hash, Hash hashNew, SigVerification sigVerification) {
        data = null;
        path = null;
        hash.hash = null;
        hash.hashHex = null;
        hashNew.hash = null;
        hashNew.hashHex = null;
        signature = null;
        signatureHex = null;
        signatureOct = null;
        sigVerification.reset();
        h = -1;
    }

    /**
     * Sets the signaturemethod with the used method.
     * 
     * @return void
     */
    public void setSignaturemethod() {
        switch (this.s) {
        case 0:
            this.signaturemethod = "DSA";
            break;
        case 1:
            this.signaturemethod = "RSA";
            break;
        case 2:
            this.signaturemethod = "ECDSA";
            break;
        case 3:
            this.signaturemethod = "RSA and MGF1"; // ????
            break;
        default:
            this.signaturemethod = "";
            break;
        }
    }

    /**
     * Sets signatureSize to the size of the signature in bit.
     * 
     * @return void
     */
    public void setSignatureSize() {
        switch (this.s) {
        case 0: // DSA 368 Bit -> 46 Byte
            this.signatureSize = 368;
            break;
        case 1: // RSA, RSA und MGF1 1024 Bit -> 128 Byte
        case 3:
            this.signatureSize = 1024;
            //this.signatureSize = 2048;
            break;
        case 2: // ECDSA 560 Bit -> 70 Byte
            this.signatureSize = 560;
            break;
        default:
            this.signatureSize = -1;
            break;
        }
    }

    /**
     * Takes the input data and divides it into the signature and the plaintext.
     * 
     * @return void
     */
    public void divideSignaturePlaintext() {
        int sigSize = this.signatureSize / 8; // Länge der Signatur von Bit in Byte umwandeln.

        // Trennt in die Inputdaten auf in Signatur und Plaintext. Der vordere Teil ist Signatur.
        //this.signature = java.util.Arrays.copyOfRange(this.data, 0, sigSize + 0);
        //this.plain = java.util.Arrays.copyOfRange(this.data, sigSize + 0, this.data.length);
        
        this.signature = java.util.Arrays.copyOfRange(this.data, 4, sigSize + 4);
        this.plain = java.util.Arrays.copyOfRange(this.data, sigSize + 4, this.data.length);

    }

    /**
     * Converts a given byte array (signature, hash, ...) to it's hexadecimal representation
     * 
     * @param bytes A byte array
     * @return The hex representation of the byte array
     */
    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
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
     * Returns the octal String representation of a byte array with optional prefix. The String is
     * formed by making value[0] the leftmost three digits and value[value.length-1] the rightmost
     * three digits.
     * 
     * @param array the byte array
     */
    public final static String bytesToOct(byte[] bytes, String prefix) {
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

    /**
     * Sets the variable signatureHex (a String). Uses the funktion bytesToHex(byte[]) to convert
     * the signature.
     */
    public void setSignatureHex() {
        this.signatureHex = bytesToHex(this.signature);
    }

    /**
     * Sets the variable signatureOct (a String). Uses the funktion bytesToOct(byte[]) to convert
     * the signature.
     */
    public void setSignatureOct() {
        this.signatureOct = bytesToOct(this.signature, "");
    }

    /**
     * Returns the signature in hexadecimal form.
     * 
     * @return signaturHex (a String)
     */
    public String getSignatureHex() {
        return this.signatureHex;
    }

    /**
     * Returns the signature in octal form.
     * 
     * @return signaturOct (a String)
     */
    public String getSignatureOct() {
        return this.signatureOct;
    }
}
