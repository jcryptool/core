// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.ui;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 *
 * Original source: http://www.java2s.com/Tutorial/Java/0490__Security/PBEFileEncrypt.htm
 * Last viewed on March 30th, 2011
 *
 */

public class FileCrypto {

	public FileCrypto(String inFile, String outFile, String passwd, int mode) throws Exception {
		Cipher cipher = createCipher(passwd, mode);
		applyCipher(inFile, outFile, cipher);
	}

	static Cipher createCipher(String passwd, int mode) throws Exception {
	    PBEKeySpec keySpec = new PBEKeySpec(passwd.toCharArray());
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
	    SecretKey key = keyFactory.generateSecret(keySpec);
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update("input".getBytes());
	    byte[] digest = md.digest();
	    byte[] salt = new byte[8];
	    for (int i = 0; i < 8; ++i)
	      salt[i] = digest[i];
	    PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 20);
	    Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
	    cipher.init(mode, key, paramSpec);
	    return cipher;
	}

	static void applyCipher(String inFile, String outFile, Cipher cipher) throws Exception {
	    CipherInputStream in = new CipherInputStream(new FileInputStream(inFile), cipher);
	    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
	    int BUFFER_SIZE = 8;
	    byte[] buffer = new byte[BUFFER_SIZE];
	    int numRead = 0;
	    do {
	    	numRead = in.read(buffer);
	    	if (numRead > 0)
	    		out.write(buffer, 0, numRead);
	    } while (numRead == 8);
	    out.close();
	}
}
