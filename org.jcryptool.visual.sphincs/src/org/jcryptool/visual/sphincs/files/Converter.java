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
package org.jcryptool.visual.sphincs.files;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
// import java.security.SecureRandom;
import java.text.DecimalFormat;

import javax.xml.bind.DatatypeConverter;

public class Converter {

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * Parses byte[][] to Hex-String
	 * 
	 * @param input
	 * @return
	 */
	public static String _2dByteToHex(byte[][] input) {

		String output = "";

		for (int i = 0; i < input.length; i++) {
			output += _byteToHex(input[i]);
		}
		return output;
	}

	/**
	 * Parses byte[] to Hex-String
	 * 
	 * @param input
	 * @return
	 */
	public static String _byteToHex(byte[] input) {
		// return Hex.toHexString(input);
		String hex = DatatypeConverter.printHexBinary(input);
		return hex;
	}

	/**
	 * Parses byte[] to String
	 * 
	 * @param input
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String _byteToString(byte[] input) throws UnsupportedEncodingException {
		return new String(input, "UTF-8");
	}

	/**
	 * Parses Hex-String to byte[][]
	 * 
	 * @param input
	 * @param l
	 * @return
	 */
	public static byte[][] _hexStringTo2dByte(String input, int l) {

		byte[][] output = new byte[l][];

		int s = input.length() / l;

		for (int i = 0; i < l; i++) {
			output[i] = _hexStringToByte(input.substring(i * s, s + i * s));
		}

		return output;
	}

	/**
	 * Parses Hex-String to byte[]
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] _hexStringToByte(String input) {
		return DatatypeConverter.parseHexBinary(input);
	}

	/**
	 * Parses String to byte[]
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] _stringToByte(String input) {
		return input.getBytes(Charset.forName("UTF-8"));
	}

	public static String _numberToPrefix(long number) {
		if (number < 0) {
			try {
				throw new IllegalArgumentException("Number must be positive");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			}
		}
		if (number < 2000) {
			return number + " Byte";
		}

		final String[] prefixes = new String[] { "Byte", "Kilobyte", "Megabyte", "Gigabyte", "Terabyte", "Petabyte" };
		int j = 0;
		double concatNumber = number / 1000d;
		for (long i = 1000; i < number; i *= 1000) {
			++j;
			concatNumber = (double) number / (double) i;
		}

		if (j >= prefixes.length - 1) {
			try {
				throw new IllegalArgumentException("Number greater than Petabyte");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			}

		}

		return new DecimalFormat(".#").format(concatNumber) + " " + prefixes[j];
	}

}