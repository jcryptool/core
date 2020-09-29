// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.algo;
import java.math.BigInteger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

/**
 * Functions necessary for the fully homomorphic encryption scheme by Gentry & Halevi
 * @author Coen Ramaekers
 *
 */
public class Functions {

	/**
	 * returns the power of two greater than the parameter
	 * @param m
	 * @return the least k >= 0 such that 2^k >= m
	 */
	public static int nextPowerOfTwo(int m) {
	   int k;
	   int n, um;

	   if (m < 0) return 0;

	   um = m;
	   n = 1;
	   k = 0;

	   while (n < um) {
	      n = n << 1;
	      k++;
	   }

	   return k;
	}

	/**
	 * negation
	 * @param n
	 * @return the parameter negated
	 */
	public static long negate(long n) {
		return -n;
	}

	/**
	 * negation
	 * @param n
	 * @return the parameter negated
	 */
	public static double negate(double n) {
		return -n;
	}

	/**
	 * Tries to find the multiplicative inverse of p mod q
	 * @param p
	 * @param q
	 * @return p^{-1} if p is invertible, null otherwise
	 */
	public static BigInteger xgcd(BigInteger p, BigInteger q) {
		BigInteger temp = new BigInteger(p.toString());
		BigInteger temp2 = new BigInteger(q.toString());
		try {
			return new BigInteger(temp.modInverse(temp2).toString());
		} catch (ArithmeticException e) {
			return null;
		}
	}

	/**
	 * Converts a string to an array of binary integers
	 * @param s
	 * @return binary representation of s
	 */
	public static int[][] stringToInt(String s) {
		int[][] temp1 = new int[s.length()][];
		for (int i = 0; i < s.length(); i++) {
			byte[] temp2 = s.getBytes();
			String temp3 = Integer.toBinaryString(temp2[i]);
			int[] temp4 = new int[temp3.length()];
			for (int j = 0; j < temp4.length; j++) {
				temp4[j] = Integer.valueOf(Character.toString(temp3.charAt(j)));
			}
			temp1[i] = temp4;
		}
		return temp1;
	}

	/**
	 * Odd checker
	 * @param a
	 * @return True if the parameter is odd, false otherwise
	 */
	public static boolean isOdd(BigInteger a) {
		return (Math.abs(a.intValue())%2) == 1;
	}

	/**
	 * Even checker
	 * @param a
	 * @return True if the parameter is even, false otherwise
	 */
	public static boolean isEven(BigInteger a) {
		return !isOdd(a);
	}

	/**
	 * Zero checker
	 * @param a
	 * @return True if the parameter is zero, false otherwise
	 */
	public static boolean isZero(long a) {
		return (a == 0);
	}

	/**
	 * Zero checker
	 * @param a
	 * @return True if the parameter is zero, false otherwise
	 */
	public static boolean isZero(double a) {
		return (a == 0);
	}

	/**
	 * Converts an integer to a polynomial
	 * @param num the integer to generate the coefficients
	 * @param mod modulo for the number
	 * @return A polynomial with coefficients 1 or 0, matching the binary representation of the paramter num
	 */
	public static Polynomial numberToPoly(int num, int mod) {
		String temp = Integer.toBinaryString(num);
		BigInteger[] coeff = new BigInteger[mod];
		for (int i = mod-1; i >= 0; i--) {
			if (i >= temp.length()) {
				coeff[i] = BigInteger.ZERO;
			} else {
				if (Character.toString(temp.charAt(temp.length()-i-1)).equals("1")) {
					coeff[i] = BigInteger.ONE;
				} else {
					coeff[i] = BigInteger.ZERO;
				}
			}
		}
		return new Polynomial(coeff);
	}

	/**
	 * Convert an integer to a binary integer stored in an integer array
	 * @param num
	 * @param mod
	 * @return The binary representation of num
	 */
	public static int[] numToBits(int num, int mod) {
		String temp = Integer.toBinaryString(num);
		int[] coeff = new int[mod];
		for (int i = mod-1; i >= 0; i--) {
			if (i >= temp.length()) {
				coeff[i] = 0;
			} else {
				if (Character.toString(temp.charAt(temp.length()-i-1)).equals("1")) {
					coeff[i] = 1;
				} else {
					coeff[i] = 0;
				}
			}
		}
		return coeff;
	}

	/**
	 * Convert an integer array to a string
	 * @param bits
	 * @return The string representation of the array bits
	 */
	public static String bitArrayToString(int[] bits) {
		String temp = "";
		for (int i = bits.length-1; i >= 0; i--) {
			temp = temp + Integer.toString(bits[i]);
			if (i != 0) temp = temp + ",";
		}
		return temp;
	}

	/**
	 * Convert a polynomial to a binary integer
	 * @param p
	 * @return The integer corresponding to the coefficients of the polynomial considered as binary number
	 */
	public static int polyToInt(Polynomial p) {
		String temp = "";
		for (int i = p.degree; i >= 0; i--) {
			if (p.coeffs[i].compareTo(BigInteger.ZERO) == 0) {
				temp = temp.concat("0");
			} else {
				temp = temp.concat("1");
			}
		}
		int i = Integer.parseInt(temp,2);
		return i;
	}

	/**
	 * Convert a binary integer stored in an integer array to an integer
	 * @param bits
	 * @return The integer corresponding to the binary number stored in the array bits
	 */
	public static int bitsToNum(int[] bits) {
		String temp = "";
		for (int i = bits.length-1; i >= 0; i--) {
			if (bits[i] == 0) {
				temp = temp.concat("0");
			} else {
				temp = temp.concat("1");
			}
		}
		int i = Integer.parseInt(temp,2);
		return i;
	}

	/**
	 * Add two ciphertexts, uses the fact that in the scheme multiplication represents an AND-gate
	 * and addition represents an XOR-gate. The AND-gate gives the carry, the XOR-gate the sum.
	 * @param a the first ciphertext
	 * @param b the second ciphertext
	 * @param fheparams the parameters for the scheme
	 * @param det the determinant of the lattice
	 * @param root the root of the lattice
	 * @param pkBlocksX the public key blocks
	 * @param ctxts the encrypted secret vector
	 * @param monitor the monitor that displays the progress
	 * @param work the amount of work to be done
	 * @return The encrypted sum of the two given ciphertexts, evaluated homomorphically
	 */
	public static BigInteger[] addCiphertexts(String step, BigInteger[] a, BigInteger[] b, FHEParams fheparams, 
								BigInteger det, BigInteger root, BigInteger[] pkBlocksX, 
								BigInteger[] ctxts, IProgressMonitor monitor, int work, GHData data) {		
		int max = Math.max(a.length, b.length);
		BigInteger[] out = new BigInteger[max];	
		//BigInteger temp1 = BigInteger.ZERO;
		//BigInteger temp2 = BigInteger.ZERO;
		//BigInteger temp3 = BigInteger.ZERO;
		BigInteger[] XOR = new BigInteger[max];
		BigInteger[] AND = new BigInteger[max];
		BigInteger[] CARRY = new BigInteger[max];
		for (int i = 0; i < max; i++) {
			
		}
		monitor.subTask(step + "Computing XOR, AND and carries");
		for (int i = 0; i < max; i++) out[i] = BigInteger.ZERO;
		XOR[0] = a[0].add(b[0]).mod(det);
		AND[0] = BigInteger.ZERO;
		CARRY[0] = BigInteger.ZERO;
		int mults = 1;
		for (int i = 1; i < max; i++) {
			XOR[i] = a[i].add(b[i]).mod(det);
			AND[i] = a[i-1].multiply(b[i-1]).mod(det);
			CARRY[i] = XOR[i-1].multiply(AND[i-1].add(CARRY[i-1])).mod(det);
			mults++;
			if (mults >= data.getMaxMult()) {
				CARRY[i] = GHReCrypt.recrypt(fheparams, CARRY[i], det, root, pkBlocksX, ctxts);
				mults = 1;
			}
			if (monitor.isCanceled()) {
				return null;
			}
		}
		monitor.internalWorked(work/(max+1));
		boolean test = (step.length() > 1);
		for (int i = 0; i < max; i++) {
			if (!test) step = "Step " + (i+1) + " of " + max + ", ";
			monitor.subTask(step + "Recrypting results");
			out[i] = XOR[i].add(AND[i]).add(CARRY[i]).mod(det);
			out[i] = GHReCrypt.recrypt(fheparams, out[i], det, root, pkBlocksX, ctxts);
			if (monitor.isCanceled()) {
				return null;
			}
			monitor.internalWorked(work/(max+1));
		}
		/*for (int i = 0; i < max; i++) {
			if (i < a.length) {
				if (i < b.length) {
					temp1 = a[i].add(b[i]).mod(det); //XOR gives the sum without carries
					temp1 = GHReCrypt.recrypt(fheparams, temp1, det, root, pkBlocksX, ctxts);
					if (i != max-1) {
						temp2 = a[i].multiply(b[i]).mod(det); //AND gives the carry
						temp2 = GHReCrypt.recrypt(fheparams, temp2, det, root, pkBlocksX, ctxts);
						if (i != 0) {
							//if the previous index resulted in a carry and the current sum is 1 this results in a new carry
							temp3 = temp1.multiply(out[i]).mod(det); 
							temp3 = GHReCrypt.recrypt(fheparams, temp3, det, root, pkBlocksX, ctxts);
							//XOR the possible current carry and the new carry, should never both be set
							temp2 = temp2.add(temp3).mod(det);
							temp2 = GHReCrypt.recrypt(fheparams, temp2, det, root, pkBlocksX, ctxts);
						}
					}
				} else {
					temp1 = a[i];
				}
			} else {
				if (i < b.length) {
					temp1 = b[i];
				}
			}
			out[i] = out[i].add(temp1).mod(det);
			out[i] = GHReCrypt.recrypt(fheparams, out[i], det, root, pkBlocksX, ctxts);
			if (i != max-1) {
				out[i+1] = out[i+1].add(temp2).mod(det);
				out[i+1] = GHReCrypt.recrypt(fheparams, out[i+1], det, root, pkBlocksX, ctxts);
			}
			if (monitor.isCanceled()) {
				return null;
			}
			monitor.internalWorked(work/max);
		}*/
		return out;
	}

	/**
	 * Multiplies two cihpertexts, uses a sort of grade-school multiplication. The second ciphertext is
	 * multiplied with the least significant bit of the first ciphertext. The result is saved and the
	 * first ciphertext is shifted to the right. Again the second ciphertext is multiplied with the least
	 * significant bit of the first ciphertext, the result is added to the previous result and the first
	 * ciphertext is again shifted to the right. This is done as many times as number of bits of the first
	 * cihpertext.
	 * @param a the first ciphertext
	 * @param b the second ciphertext
	 * @param fheparams the parameters of the scheme
	 * @param det the lattice determinant
	 * @param root the lattice root
	 * @param pkBlocksX the public key blocks
	 * @param ctxts the encryption of the secret vector
	 * @param key the key, used to encrypt the initial empty result
	 * @param monitor the monitor which displays the progress
	 * @param work the amount of work to be done
	 * @return the ciphertexts multiplied, evaluated homomorphically
	 */
	public static BigInteger[] mulCiphertexts(BigInteger[] a, BigInteger[] b, FHEParams fheparams, 
						BigInteger det, BigInteger root, BigInteger[] pkBlocksX, 
						BigInteger[] ctxts, GHKeyPair key, IProgressMonitor monitor, int work, GHData data) {
		int max = Math.max(a.length, b.length);
		BigInteger[] aTemp = new BigInteger[a.length];
		BigInteger[] bTemp = new BigInteger[b.length];
		for (int i = 0; i < a.length; i++) aTemp[i] = a[i];
		for (int i = 0; i < b.length; i++) bTemp[i] = b[i];
		BigInteger[] temp = new BigInteger[max];
		BigInteger[] temp2 = new BigInteger[max];
		String step;
		for (int i = 0; i < max; i++) {
			temp[i] = BigInteger.ZERO;//GHEncrypt.encrypt(fheparams, key, 0);
		}
		for (int i = 0; i < aTemp.length; i++) {
			step = "Step " + (i+1) + " of " + aTemp.length + ", ";
			monitor.subTask(step + "Multiplying first operand with bit " + (i+1) + " of second operand.");
			for (int j = 0; j < bTemp.length; j++) temp2[j] = bTemp[j].multiply(aTemp[0]).mod(det);
			monitor.subTask(step + "Recrypting results");
			for (int j = 0; j < bTemp.length; j++) {
				//temp2[j] = GHReCrypt.recrypt(fheparams, temp2[j], det, root, pkBlocksX, ctxts);
			}
			SubMonitor sm = SubMonitor.convert(monitor, work/aTemp.length);
			sm.beginTask("", work/aTemp.length);
			if (i == 0) {
				for (int j = 0; j < temp2.length; j++) temp[j] = temp2[j];
			} else {
				temp = addCiphertexts(step, temp, temp2, fheparams, det, root, pkBlocksX, ctxts, sm, work/aTemp.length, data);
			}
			if (sm.isCanceled()) return null;
			sm.done();
			for (int k = 0; k < aTemp.length-1; k++) {
				aTemp[k] = aTemp[k+1];
			}
			aTemp[aTemp.length-1] = temp[0];
			for (int k = 0; k < temp.length-1; k++) {
				temp[k] = temp[k+1];
			}
			if (monitor.isCanceled()) {
				return null;
			}
		}
		return aTemp;
	}
}
