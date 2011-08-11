//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.operations.dataobject.modern;

import java.io.InputStream;
import java.io.OutputStream;

import org.jcryptool.core.operations.dataobject.IDataObject;

/**
 * Provides a top level frame for the data objects used for modern algorithms. 
 * 
 * @author t-kern
 *
 */
public interface IModernDataObject extends IDataObject {

	/** Transparent wrapper for the encryption mode */
	public static final int ENCRYPT_MODE = 1;//Cipher.ENCRYPT_MODE;

	/** Transparent wrapper for the decryption mode */
	public static final int DECRYPT_MODE = 2;//Cipher.DECRYPT_MODE;	

	public void setInputStream(InputStream inputStream);

	public InputStream getInputStream();

	public void setOutputStream(OutputStream outputStream);

	public OutputStream getOutputStream();

	public void setOutputIS(InputStream inputStream);
	
	public InputStream getOutputIS();
	/**
	 * Returns the input.
	 * 
	 * @return The input
	 */
	public byte[] getInput();

	/**
	 * Returns the opmode
	 * 
	 * @return	The opmode
	 */
	public int getOpmode();

	/**
	 * Returns the output.
	 * 
	 * @return	The output
	 */
	public byte[] getOutput();

	/**
	 * Returns the padding scheme.
	 * 
	 * @return	The padding scheme
	 */
	public String getPaddingName();

	/**
	 * Sets the input.
	 * 
	 * @param input
	 */
	public void setInput(byte[] input);

	/**
	 * Sets the opmode
	 * 
	 * @param opmode
	 */
	public void setOpmode(int opmode);

	/**
	 * Sets the output.
	 * 
	 * @param output
	 */
	public void setOutput(byte[] output);

	/**
	 * Sets the padding scheme.
	 * 
	 * @param paddingName	The padding scheme
	 */
	public void setPaddingName(String paddingName);

}
