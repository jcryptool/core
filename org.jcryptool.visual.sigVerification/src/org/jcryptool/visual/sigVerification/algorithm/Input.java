// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.algorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * This class is used to share data between classes.
 * 
 * @author Wilfing
 */
public class Input {
    /**
     * Contains the input data (signature + plaintext) (byte array)
     */
//    public byte[] data;

    /**
     * Contains the path to the input data
     */
    public String path = ""; //$NON-NLS-1$
    
    public String filename = ""; //$NON-NLS-1$
    
    public String tooltipData;

    public int h = -1; // the chosen hash (integer)

    public int s = -1; // the chosen signature method (integer)

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
     * The public key used for signature verification
     */
    public KeyStoreAlias publicKeyAlias = null;

    /**
     * This method resets all variables in this class to their initial value
     */
    public void reset(Hash hash, Hash hashNew, SigVerification sigVerification) {
//        data = null;
        path = null;
        filename = null;
        hash.hash = null;
        hash.hashHex = null;
        hashNew.hash = null;
        hashNew.hashHex = null;
        signature = null;
        signatureHex = null;
        signatureOct = null;
        sigVerification.reset();
        h = -1;
        publicKeyAlias = null;
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
     * Sets signatureSize to the size of the signature in byte.
     * 
     * @return void
     */
    public void setSignatureSizeFromInputFile(File inputFile) throws IOException {
//        switch (this.s) {
//        case 0: // DSA 368 Bit -> 46 Byte
//            this.signatureSize = 368;
//            break;
//        case 1: // RSA, RSA und MGF1 1024 Bit -> 128 Byte
//        case 3:
//            this.signatureSize = 1024;
//            //this.signatureSize = 2048;
//            break;
//        case 2: // ECDSA 560 Bit -> 70 Byte (?) // 440 Bit
//            this.signatureSize = 440;
//            break;
//        default:
//            this.signatureSize = -1;
//            break;
//        }
    	byte[] arrSigSize = getBytesFromFile(inputFile, 0, 4);
        final ByteBuffer bb = ByteBuffer.wrap(arrSigSize);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        int signatureSizeTemp = bb.getInt();
        if (signatureSizeTemp > 8192) {
        	//if signature is too big, eg because the user opened a wrong file where the first 4 bytes do not contain the correct signatire size
        	//the size is set to -1 to avoid OutOfMemoryError in setSignatureFromInputFile(file)-method and setPlainFromInputFile(file)-method
        	this.signatureSize = -1;
        } else {
        	this.signatureSize = signatureSizeTemp;
        }
    }
    
    public void setSignatureFromInputFile(File file) throws IOException {  	
    	if (signatureSize != -1) {
    		this.signature = getBytesFromFile(file, 4, signatureSize/8); //4 bytes contain the length of the signature as an integer value
    	} else {
    		this.signature = null;
    	}
    }
    
    public void setPlainFromInputFile(File file) throws IOException {
    	if (signatureSize != -1) {
    		this.plain = getBytesFromFile(file, signatureSize/8 + 4, file.length() - signatureSize/8 - 4);
    	} else {
    		this.plain = null;
    	}
    }
    
    /**
     * Converts the input file to a byte array
     * 
     * @param file The file elected by the user
     * @return The byte array
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
    	return getBytesFromFile(file, 0, file.length());
    }
    
    /**
     * Converts the input file to a byte array
     * 
     * @param file The file elected by the user
     * @param length the length of the result array. The method stops after this number of bytes
     * @return The byte array
     */
    public static byte[] getBytesFromFile(File file,long start, long length) throws IOException {
        InputStream is = new FileInputStream(file);
        
        // Check if the file isn't 0
        if (length <= 0) {
            is.close();
            return null;
        }
     
        //skip bytes until start position in the file is reached
        for (int i = 0; i < start; i++) {
            int curr = is.read();
            if (curr == -1) {
            	is.close();
            	return null;
            }
        }
        
        int offset = 0;
        int numRead = 0;  

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];
        
        //read and save the rest of the file bytes into the byte array
        while ((offset < bytes.length) && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
            offset += numRead;
        }

        is.close();
        return bytes;
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
    	if (this.signature != null) {
    		this.signatureHex = bytesToHex(this.signature);
    	}
    }

    /**
     * Sets the variable signatureOct (a String). Uses the funktion bytesToOct(byte[]) to convert
     * the signature.
     */
    public void setSignatureOct() {
    	if (this.signature != null) {
    		this.signatureOct = bytesToOct(this.signature, "");
    	}
    }

    /**
     * Returns the signature in hexadecimal form.
     * 
     * @return signaturHex (a String)
     */
    public String getSignatureHex() {
    	if (this.signatureHex != null) {
            return this.signatureHex;
    	} else {
    		return "";
    	}

    }

    /**
     * Returns the signature in octal form.
     * 
     * @return signaturOct (a String)
     */
    public String getSignatureOct() {
    	if (this.signatureHex != null) {
            return this.signatureOct;
		} else {
			return "";
		}
    }
}
