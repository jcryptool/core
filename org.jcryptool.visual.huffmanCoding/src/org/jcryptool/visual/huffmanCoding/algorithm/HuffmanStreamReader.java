//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.huffmanCoding.algorithm;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Miray Inel
 *
 */
public class HuffmanStreamReader extends InputStream {

	private BitString[] codeTable;
	private InputStream stream;
	private int buffer;
	private int bitsInBuffer;

	/**
	 * Construts a new HuffmanStreamReader upon a given InputStream
	 * 
	 * This Reader tries to read a code table from the Stream.
	 * 
	 * @param stream
	 *            the InputStream
	 * @throws IOException
	 */
	public HuffmanStreamReader(InputStream stream) throws IOException {
		super();
		this.stream = stream;
		this.readCodeTable();
	}

	/**
	 * Reads the code table
	 * 
	 * @throws IOException
	 */
	private void readCodeTable() throws IOException {
		codeTable = new BitString[256];
		int character, codeLength, byteLength;
		character = this.read();
		codeLength = this.read();
		// read table entry by entry

		do {
			byteLength = (codeLength - 1) / 8 + 1;
			byte[] bytes = new byte[byteLength];

			for (int i = 0; i < byteLength; i++)
				bytes[i] = (byte) this.read();

			codeTable[character] = new BitString(bytes, codeLength);

			character = this.read();
			codeLength = this.read();
		} while (character != -1 && codeLength != 0);

	}

	/**
	 * Returns the code table
	 * 
	 * @return the code table
	 */
	public BitString[] getCodeTable() {
		return this.codeTable;
	}

	/**
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		return this.stream.read();
	}

	/**
	 * Reads and returns a bit (as integer) from the stream
	 * 
	 * @return the int value of the current bit or -1 if end of stream is
	 *         reached.
	 * @throws IOException
	 */
	public int readBit() throws IOException {
		if (this.bitsInBuffer == 0) {
			this.buffer = this.read();
			if (this.buffer == -1)
				return -1;
			this.bitsInBuffer = 8;
		}
		return (this.buffer >> (this.bitsInBuffer-- - 1)) & 1;
	}

}
