//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.library.Constants;

/**
 * this class contains the implementation of the rsa-functionality
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class Rsa_Implementation{
	
	public Rsa_Implementation(){
		
	}
	/**
	 * encrypt a message
	 * @param cleartext the cleartext
	 * @param exponent is the RSA-exponent of the recipients public key
	 * @param modulus is the RSA-modulus of the recipients public key
	 * @return the encrypted message in the hexadecimal format
	 */
	public String encrypt(String cleartext, BigInteger exponent, BigInteger modulus){
		
		//transform the given cleartext into hex-text
		final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cleartext.length(); ++i) {
            sb.append(Integer.toHexString(cleartext.charAt(i)));
            if (i != cleartext.length() - 1) {
                sb.append(' ');
            }
        }
        
        String hexText = sb.toString();
        System.out.println("hex-text: "+hexText);
        
        StringBuilder enc_sb = new StringBuilder();
        String[] splittedHexText = hexText.split(" ");
        System.out.println("splittet-text entries: "+splittedHexText.length);
        BigInteger number;
        for (String s : splittedHexText){
        	number = new BigInteger(s, Constants.HEXBASE);
        	enc_sb.append(number.modPow(exponent, modulus).toString(Constants.HEXBASE));
        	enc_sb.append(' ');
        }
		
        return enc_sb.toString();
	}

	/**
	 * method to decrypt an encypted message
	 * @param encryptedMessage is the encrypted message
	 * @param d the inverse RSA-exponent of the private Key
	 * @param modulus the RSA-modulus of the private Key
	 * @return the decrypted message 
	 */
	public String decrypt(String encryptedMessage, BigInteger d, BigInteger modulus){
		StringBuilder sb = new StringBuilder();
		String[]splitted_enc = encryptedMessage.split(" ");
		
		BigInteger number;
        for (String s : splitted_enc){
        	number = new BigInteger(s, Constants.HEXBASE);
        	int value = number.modPow(d, modulus).intValue(); 
        	sb.append((char) value);
        }
		
		return new String(sb.toString());
	}
}