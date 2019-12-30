//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.huffmanCoding.algorithm;

/**
 * 
 * @author Miray Inel
 * 
 */
public class BitString implements Cloneable {
	private byte[] string;
	private int length;

	/**
	 * Creates a new BitString filled with the length bits from data.
	 * 
	 * @param data
	 *            input data
	 * @param length
	 *            the length of the new BitString
	 */
	public BitString(byte[] data, int length) {
		if (length < 1)
			throw new IllegalArgumentException("length must be bigger than 0");
		if (data == null)
			throw new IllegalArgumentException("data may not be null");
		if ((length - 1) / 8 + 1 > data.length)
			throw new IllegalArgumentException("data contains not enough elements for length: " + length);

		string = new byte[(length - 1) / 8 + 1];
		for (int i = 0; i < string.length - 1; i++)
			string[i] = data[i];
		this.length = length;
		// set unused bits in last byte to zero!
		if (length % 8 == 0) {
			// no unused bits
			string[string.length - 1] = data[string.length - 1];
		} else {
			// apply bitmask
			byte mask = (byte) (255 << (8 - length % 8));
			string[string.length - 1] = (byte) (data[string.length - 1] & mask);
		}
	}

	/**
	 * Creates an empty BitString
	 */
	public BitString() {
		this.length = 0;
	}

	/**
	 * Append a bit to the BitString
	 * 
	 * @param the
	 *            bit to append to string
	 * @return returns the complete BitString (this)
	 */
	public BitString append(boolean bit) {
		if (this.length % 8 == 0) {
			this.grow(1);
		}
		if (bit) {
			// set bit
			string[length / 8] = (byte) (string[length / 8] | (1 << (7 - length % 8)));
		}
		length++;
		return this;
	}

	/**
	 * Clones the BitString
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public BitString clone() {
		return string == null ? new BitString() : new BitString(string.clone(), length);
	}

	/**
	 * Returns the BitString as byte array
	 * 
	 * @return byte array or null if no bits are in BitString
	 */
	public byte[] getByteArray() {
		return (this.string == null) ? null : this.string.clone();
	}

	/**
	 * Returns the BitString as an boolean array
	 * 
	 * @return boolean array or null if no bits are in BitString
	 */
	public boolean[] getBitArray() {
		// return "empty array"
		if (this.length == 0)
			return null;

		// build array
		boolean[] result = new boolean[this.length];
		int bitShift = 7;
		int arrayPos = 0;
		int resultPos = 0;
		for (int i = 0; i < this.length; i++) {
			result[resultPos++] = (string[arrayPos] & 1 << bitShift) != 0;
			if (--bitShift < 0) {
				bitShift = 7;
				arrayPos++;
			}
		}
		return result;
	}

	/**
	 * Returns the current length of the BitString (in bits)
	 * 
	 * @return the length of the BitString in bits
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * Allocates more space for bits
	 * 
	 * @param size
	 */
	private void grow(int size) {
		if (this.string == null) {
			this.string = new byte[size];
		} else {
			// create new array
			byte[] newString = new byte[this.string.length + size];
			// copy old content
			for (int i = 0; i < this.string.length; i++)
				newString[i] = this.string[i];
			// replace array
			this.string = newString;
		}
	}

	/**
	 * Returns a '0', '1' string
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		int bitShift = 7;
		int arrayPos = 0;
		for (int i = 0; i < this.length; i++) {
			buffer.append((string[arrayPos] & 1 << bitShift) != 0 ? "1" : "0");
			if (--bitShift < 0) {
				bitShift = 7;
				arrayPos++;
			}
		}
		return buffer.toString();
	}
}