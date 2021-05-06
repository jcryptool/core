// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
/*
 * Copyright (c) 1998-2003 by The FlexiProvider Group, Technische Universitaet Darmstadt For conditions of usage and
 * distribution please refer to the file COPYING in the root directory of this package.
 */

package org.jcryptool.core.operations.util;

public final class ByteArrayUtils {

    public static final String IDENT = "$Id: ByteArrayUtils.java 1683 2007-04-04 13:05:07Z doering $"; //$NON-NLS-1$

    private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };

    /**
     * Default constructor (private)
     */
    private ByteArrayUtils() {

    }

    /**
     * checks if two byte-arrays contain the same bytes
     * 
     * @param a - first byte-array
     * @param b - second byte-array
     * 
     * @return true or false
     */
    public static boolean equals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts a string containing hexadecimal characters to a byte-array
     * 
     * @param s - a hexstring
     * 
     * @return a byte array with the corresponding value
     */
    public static byte[] fromHexString(String s) {
        byte[] rawChars = s.toUpperCase().getBytes();
        int hexChars = 0, pos = 0;

        for (int i = 0; i < rawChars.length; i++) {
            if ((rawChars[i] >= '0' && rawChars[i] <= '9') || (rawChars[i] >= 'A' && rawChars[i] <= 'F')) {
                hexChars++;
            }
        }

        byte[] byteString = new byte[(hexChars + 1) / 2];

        for (int i = 0; i < rawChars.length; i++) {
            if (rawChars[i] >= '0' && rawChars[i] <= '9') {
                byteString[pos / 2] <<= 4;
                byteString[pos / 2] |= rawChars[i] - '0';
            } else if (rawChars[i] >= 'A' && rawChars[i] <= 'F') {
                byteString[pos / 2] <<= 4;
                byteString[pos / 2] |= rawChars[i] - 'A' + 10;
            } else {
                continue;
            }
            pos++;
        }

        return byteString;
    }

    /**
     * Converts a byte-array to the corresponding hexstring
     * 
     * @param input - the byte-array to be converted
     * 
     * @return the corresponding hexstring
     */
    public static String toHexString(byte[] input) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < input.length; i++) {
            result.append(HEX_CHARS[(input[i] >>> 4) & 0x0f]);
            result.append(HEX_CHARS[(input[i]) & 0x0f]);
        }
        return result.toString();
    }

    /**
     * Converts a byte-array to the corresponding hexstring
     * 
     * @param input - the byte-array to be converted
     * @param seperator - a seperator string
     * 
     * @return the corresponding hexstring
     */
    public static String toHexString(byte[] input, String seperator) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < input.length; i++) {
            result.append(HEX_CHARS[(input[i] >>> 4) & 0x0f]);
            result.append(HEX_CHARS[(input[i]) & 0x0f]);
            if (i < input.length - 1) {
                result.append(seperator);
            }
        }
        return result.toString();
    }

    /**
     * Calculates the bytewise XOR of two arrays of bytes
     * 
     * @param x1 - the first array
     * @param x2 - the second array
     * @return x1 XOR x2
     */
    public static byte[] xor(byte[] x1, byte[] x2) {
        byte[] out = new byte[x1.length];

        for (int i = 0; i < x1.length; i++) {
            out[i] = (byte) (x1[i] ^ x2[i]);
        }
        return out;
    }

    /**
     * Concatenates two byte arrays. If one of the arrays is <code>null</code>, return the other array (i.e., if both
     * are <code>null</code>, <code>null</code> is returned).
     * 
     * @param x1 - the first array
     * @param x2 - the second array
     * @return x1 || x2
     */
    public static byte[] concatenate(byte[] x1, byte[] x2) {
        if (x1 == null) {
            return x2;
        }
        if (x2 == null) {
            return x1;
        }
        byte[] result = new byte[x1.length + x2.length];

        System.arraycopy(x1, 0, result, 0, x1.length);
        System.arraycopy(x2, 0, result, x1.length, x2.length);

        return result;
    }

    /**
     * Splits a byte array <code>input</code> into two arrays at <code>index</code>, i.e. the first array will have
     * <code>index</code> bytes, the second one <code>input.length - index</code> bytes.
     * 
     * @param input - the byte array to be split
     * @param index - the index where the byte array is split
     * 
     * @return the two halfs of <code>input</code> as an array of two byte arrays
     */
    public static byte[][] split(byte[] input, int index) {
        if (input == null || index > input.length) {
            return null;
        }
        byte[][] result = new byte[2][];
        result[0] = new byte[index];
        result[1] = new byte[input.length - index];
        System.arraycopy(input, 0, result[0], 0, index);
        System.arraycopy(input, index, result[1], 0, input.length - index);
        return result;
    }

    /**
     * Generates a subarray of a given byte array.
     * 
     * @param input - the input byte array
     * @param start - the start index
     * @param end - the end index
     * @return a subarray of <code>input</code>, ranging from <code>start</code> to <code>end</code>
     */
    public static byte[] subArray(byte[] input, int start, int end) {
        byte[] result = new byte[end - start];
        System.arraycopy(input, start, result, 0, end - start);
        return result;
    }

    /**
     * Generates a subarray of a given byte array.
     * 
     * @param input - the input byte array
     * @param start - the start index
     * @return a subarray of <code>input</code>, ranging from <code>start</code> to the end of the array
     */
    public static byte[] subArray(byte[] input, int start) {
        return subArray(input, start, input.length);
    }

    /**
     * Convert a byte array of length 4 into an int number, using big-endian notation
     * 
     * @param input - the byte array
     * @return the converted int or <tt>0</tt> if <tt>input.length != 4</tt>
     */
    public static int toIntBigEndian(byte[] input) {
        int result = 0;
        if (input.length != 4) {
            return 0;
        }
        result ^= (input[0] & 0xff) << 24;
        result ^= (input[1] & 0xff) << 16;
        result ^= (input[2] & 0xff) << 8;
        result ^= input[3] & 0xff;
        return result;
    }

    /**
     * Convert a byte array of length 4 into an int number, using little-endian notation
     * 
     * @param input - the byte array
     * @return the converted int or <tt>0</tt> if <tt>input.length != 4</tt> or the resulting integer would be negative
     */
    public static int toIntLittleEndian(byte[] input) {
        int result = 0;
        if (input.length != 4 || input[3] < 0) {
            return 0;
        }
        result ^= (input[3] & 0xff) << 24;
        result ^= (input[2] & 0xff) << 16;
        result ^= (input[1] & 0xff) << 8;
        result ^= input[0] & 0xff;
        return result;
    }

    /**
     * Rewrite a byte array as an int array (the array can be padded with zeros)
     * 
     * @param input - the byte array
     * @return int array
     */
    public static int[] toIntArray(byte[] input) {
        int[] result = new int[(input.length + 3) >>> 2];
        int index, shiftpos;
        for (int i = 0; i < input.length; i++) {
            index = i >>> 2;
            shiftpos = i % 4;
            result[index] ^= (input[i] & 0xff) << (8 * shiftpos);
        }
        return result;
    }

    /**
     * Rewrite a byte array as a char array
     * 
     * @param input - the byte array
     * @return char array
     */
    public static char[] toCharArray(byte[] input) {
        char[] result = new char[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = (char) input[i];
        }
        return result;
    }

    /**
     * Reverses the order of the bytes in the given byte array
     * 
     * @param input - byte array
     * @return new byte array with the property result[a.length-i] = a[i]
     * 
     */
    public static byte[] reverseOrder(byte[] input) {
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = input[input.length - i - 1];
        }
        return result;
    }

    /**
     * Generates a 16-byte bit vector.
     * 
     * @author T. Krück
     * @param i
     * @return
     */
    public static byte[] delta(int i) {
        byte[] result = new byte[16];
        int orderByte = 0, orderBit = 0;

        orderByte = (int) i / 8;
        int x = ((int) i % 8) + 1;

        switch (x) {
        case 0: {
            orderBit = 0x00;
            break;
        }
        case 1: {
            orderBit = 0x01;
            break;
        }
        case 2: {
            orderBit = 0x02;
            break;
        }
        case 3: {
            orderBit = 0x04;
            break;
        }
        case 4: {
            orderBit = 0x08;
            break;
        }
        case 5: {
            orderBit = 0x10;
            break;
        }
        case 6: {
            orderBit = 0x20;
            break;
        }
        case 7: {
            orderBit = 0x40;
            break;
        }
        case 8: {
            orderBit = 0x80;
            break;
        }
        }

        result[orderByte] = (byte) orderBit;

        return result;
    }

}
