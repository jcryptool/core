package org.jcryptool.visual.sphincsplus.algorithm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Class with useful static functions
 * 
 * @author Lukas Stelzer
 *
 */
public class Utils {
    /**
     * Casts a byte[] to a hexadecimal String
     * 
     * @param byte[]array
     * @return hexadecimal String
     */
    public static String bytesToHex(byte[] array) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            String hex = Integer.toHexString(0xff & array[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Calculates the XOR-Operation over two byte[]
     * 
     * @param array1
     * @param array2
     * @param lenght of both arrays
     * @return array1 xor array2
     */
    public static byte[] xorTwoByteArrays(byte[] array1, byte[] array2, int lenght) {
        byte[] result = new byte[lenght];
        int i = 0;
        for (byte b : array1) {
            result[i] = (byte) (b ^ array2[i++]);
        }
        return result;
    }

    /**
     * this method concatenates an Array of bytearrays
     * 
     * @param bytearrays
     * @param numberOfArrays
     * @return all bytearrays in one
     */
    // public static byte[] ConcatenateByteArrays(byte[][] bytearrays, int numberOfArrays) {
    public static byte[] concatenateByteArrays(byte[][] bytearrays) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < bytearrays.length; i++) {
            try {
                outputStream.write(bytearrays[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outputStream.toByteArray();
    }

    /**
     * This method appends b to a.
     * 
     * @param a the first byte array
     * @param b the second byte array
     * @return a||b
     */
    public static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        // This method is faster than calling it with byte[][], because memory is only malloc'd
        // inside and not when calling concat(new byte[]{a, b}).
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < a.length; i++) {
            outputStream.write(a[i]);
        }
        for (int i = 0; i < b.length; i++) {
            outputStream.write(b[i]);
        }
        return outputStream.toByteArray();
    }

    /**
     * this method converts an integer value to a byte array
     * 
     * @param value
     * @return the value as a byte array
     */
    public static byte[] intToByteArray(int value) {
        return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
    }

    /***
     * For a non-negative real number returns the logarithm to base 2 of x.
     * 
     * @param x number to apply log(x)
     * @return
     */
    public static double ld(double x) {
        if (x < 0)
            return 0;
        return (Math.log(x) / Math.log(2.0));
    }

    /***
     * See NIST paper 2.4
     * 
     * @param littleEndian the number to be converted to big-endian
     * @param y number of elements in the output
     * @return y-length byte array containing the big-endian-representation of "littleEndian"
     */
    public static byte[] toByte(long littleEndian, int y) {
        byte[] result = new byte[y];
        for (; y > 0; y--) {
            result[y - 1] = (byte) (littleEndian & 0xFF);
            littleEndian = littleEndian >> 8;
        }

        return result;
    }

    /**
     * Provides a simple way to get the first n bits of value in
     * 
     * @param in the value
     * @param n count of the first bits
     * @return the first n bits of in
     */
    public static byte[] get_first_n_bits(byte in[], int n) {
        int remainder = n % 8;
        byte[] out;
        if (remainder == 0) {
            out = Arrays.copyOfRange(in, 0, n / 8);
        } else {
            // f.e. remainder=3: 1<<3=0000 1000(bin)
            // 0000 1000-1 = 0000 0111 --> 0000 0111<<5 = 1110 0000
            // logical AND to get the first N bits
            out = Arrays.copyOfRange(in, 0, n / 8 + 1);
            int mask = ((1 << remainder) - 1) << (8 - remainder);
            out[out.length - 1] &= mask;
        }
        return out;
    }

    public static int byteArrayToInt(byte[] b) {
        int value = 0;
        for (int i = 0; i < b.length; i++)
            value = (value << 8) | b[i];
        return value;
    }
}
