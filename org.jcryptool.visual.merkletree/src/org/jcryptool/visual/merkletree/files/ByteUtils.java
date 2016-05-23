package org.jcryptool.visual.merkletree.files;

import java.io.PrintWriter;
import java.io.StringWriter;

//import org.jcryptool.visual.merkletree.files.ByteUtils;

public class ByteUtils {
	/**
	 * Concatenate a byte array.
	 *
	 * @param x
	 *            byte array
	 * @param y 
	 *            byte array
	 * @return x || y
	 */
	public static byte[] concatenate(byte[] x, byte[] y) {
		if (x == null) {
			return y;
		}
		if (y == null) {
			return x;
		}
		byte[] ret = new byte[x.length + y.length];
		System.arraycopy(x, 0, ret, 0, x.length);
		System.arraycopy(y, 0, ret, x.length, y.length);
		return ret;
	}
	
	public static byte[] concatenate(byte[] x, byte y){
		byte[] ret = new byte[x.length + 1];
		System.arraycopy(x, 0, ret, 0, x.length);
		ret[x.length] = y;
		return ret;
	}
	
	public static byte[] concatenate(byte x, byte[] y){
		byte[] ret = new byte[y.length + 1];
		ret[0] = x;
		System.arraycopy(y, 0, ret, 1, y.length);
		return ret;
	}

	public static String toHexString(byte[] bytes) {
		return toHexString(bytes, "%02X", false);
	}

	public static String toHexString(byte[] bytes, int limit) {
		return toHexString(ByteUtils.copy(bytes, 0, limit), "%02X", false);
	}

	public static String toHexString(byte[][] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(toHexString(bytes[i], "%02X", false));
		}
		return sb.toString();
	}

	/**
	 * Convert a byte array to a hex string.
	 *
	 * @param bytes
	 *            Input
	 * @param formatted
	 *            If true the string is formatted to 0xXX presentation
	 * @return Hex string
	 */
	public static String toHexString(byte[] bytes, boolean formatted) {
		return toHexString(bytes, formatted, false);
	}

	/**
	 * Convert a byte array to a hex string.
	 *
	 * @param bytes
	 *            Input
	 * @param formatted
	 *            If true the string is formatted to 0xXX presentation
	 * @param addLinebreak
	 *            If true the string is formatted to 16 value per line
	 * @return Hex string
	 */
	public static String toHexString(byte[] bytes, boolean formatted, boolean addLinebreak) {
		if (formatted) {
			return toHexString(bytes, "0x%02X ", addLinebreak);
		} else {
			return toHexString(bytes, "%02X", addLinebreak);
		}
	}

	private static String toHexString(byte[] bytes, String format, boolean addLinebreak) {
		if (bytes == null) {
			return null;
		}

		StringWriter writer = new StringWriter(bytes.length * 2);
		PrintWriter out = new PrintWriter(writer);

		for (int i = 1; i <= bytes.length; i++) {
			out.printf(format, bytes[i - 1]);
			if (addLinebreak) {
				if (i % 16 == 0) {
					out.append("\n");
				}
			}
		}

		return writer.toString();
	}

	/**
	 * Cut leading null bytes of a byte array.
	 *
	 * @param input
	 * @return byte array without leading null bytes
	 */
	public static byte[] cutLeadingNullBytes(byte[] input) {
		if (input == null) {
			return null;
		}

		int i;
		for (i = 0; i < input.length - 1; i++) {
			if (input[i] != (byte) 0x00) {
				break;
			}
		}
		return copy(input, i, input.length - i);
	}

	/**
	 * Removes leading null byte from the input byte array.
	 *
	 * @param input
	 *            Byte array
	 * @return byte array without leading null bytes
	 */
	public static byte[] cutLeadingNullByte(byte[] input) {
		if (input == null) {
			return null;
		}
		if (input[0] != (byte) 0x00) {
			return ByteUtils.clone(input);
		}
		return copy(input, 1, input.length - 1);
	}

	/**
	 * Clone a byte array.
	 *
	 * @param input
	 *            the byte array to clone
	 * @return new byte array, or null if input is null
	 */
	public static byte[] clone(byte[] input) {
		if (input == null) {
			return null;
		}
		byte[] ret = new byte[input.length];
		System.arraycopy(input, 0, ret, 0, input.length);
		return ret;
	}

	/**
	 * Copy of range.
	 *
	 * @param input
	 *            the input
	 * @param offset
	 * @param length
	 *            the length
	 * @return the byte[]
	 */
	public static byte[] copy(byte[] input, int offset, int length) {
		if (input == null) {
			return null;
		}
		byte[] tmp = new byte[length];
		System.arraycopy(input, offset, tmp, 0, length);
		return tmp;
	}

	public static byte[] convert(byte[][] input) {
		if (input == null) {
			return null;
		}

		byte[] ret = new byte[input.length * input[0].length];

		for (int i = 0; i < input.length; i++) {
			System.arraycopy(input[i], 0, ret, input[i].length * i, input[i].length);
		}

		return ret;
	}
}
