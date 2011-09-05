// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3;

import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer4.L4;

/**
 * U = Utilities
 *
 * @author hkh
 *
 */
public class U {

	/*
	 * BigInteger
	 */
	public static BigInteger ZERO = BigInteger.ZERO;
	final public static BigInteger ONE = BigInteger.ONE;
	final public static BigInteger TWO = BigInteger.valueOf(2);
	final public static BigInteger THREE =  BigInteger.valueOf(3);
	final public static BigInteger FOUR =   BigInteger.valueOf(4);
	final public static BigInteger SEVEN =  BigInteger.valueOf(7);
	final public static BigInteger EIGHT =  BigInteger.valueOf(8);
	final public static BigInteger NEGONE = ONE.negate();

	public static BigInteger bi(long inValueOf){
		return BigInteger.valueOf(inValueOf);
	}

	/*
	 * Keygen
	 */

	/**
	 * Used by Keygen.
	 *
	 * @param inDividend1 BigInteger dividend
	 * @param inDividend2 int dividend
	 * @param inDivisor int divisor
	 * @return true if inDividend1 === inDividend2 mod Divisor (inDividend1 mod inDivisor == inDividend2 mod inDivisor)
	 */
	public static boolean isCongruent(BigInteger inDividend1, int inDividend2, int inDivisor) {

		// Test
		if (
				inDividend1.compareTo(ZERO) <= 0
				|| inDividend2 <= 0) {
			throw new IllegalArgumentException("only positives allowed");
		}

		// Setup

		boolean isCongruent = false;
		BigInteger remainder1 = inDividend1.mod(BigInteger.valueOf(inDivisor));
		BigInteger remainder2 = BigInteger.valueOf(inDividend2 % inDivisor);
		if(remainder1.equals(remainder2)){
			isCongruent = true;
		}
		return isCongruent;
	}

	/**
	 * Used by Keygen.
	 *
	 * @param inNumerator1
	 * @param inNumerator2
	 * @return true if gcd(inNumerator1, inNumerator2) > 1
	 */
	public static boolean isGcdGreaterThanOne(BigInteger inNumerator1, BigInteger inNumerator2) {
		boolean isGreater = false;
		if(inNumerator1.gcd(inNumerator2).compareTo(BigInteger.ONE) > 0){
			isGreater = true;
		}
		return isGreater;
	}

	/*
	 * Element of U
	 */

	/**
	 * This method returns a evenly-random selected primw BigInteger from the interval
	 * [ 0 - inRange]
	 *
	 * @param inRange from 0 up until including inRange is the pool of which a number is selected
	 * @return selected positive, prime BigInteger
	 */
	public static BigInteger selectUniformlyFrom0To(BigInteger inRange) {
		BigInteger selected = BigInteger.ZERO;
		do {
			selected = U.newBigInteger(inRange.bitLength());
		} while (selected.compareTo(inRange) > 0);
		return selected;
	}

	private static BigInteger newBigInteger(int bitLength) {
		return R.newBigInteger(bitLength);
	}

	/**
	 * As in ElementOfU.selectFromU(BigInteger inRange) a evenly-random BigInteger is selected. The pool
	 * of selection goes from [0 - inRange] afterwards offset is substracted from that BigInteger. This
	 * was negative BigIntegers can be produced.
	 *
	 * @param inRange The size of the selection pool starting from 0.
	 * @param offset The negative offset / substraction to get negative BigIntegers also. left-shift
	 * @return a randomly selected prime BigInteger
	 */
	public static BigInteger selectUniformlyFromXTo(BigInteger inRange, BigInteger offset) {
		BigInteger a = (U.selectUniformlyFrom0To(inRange)).subtract(offset);
		return a;
	}

	/*
	 * Verbose
	 */

	private static boolean isVerbose = false;

	public static String verbose(Throwable inThrowable, String inMessage){
		StringBuilder aStringBuilder = new StringBuilder();
		aStringBuilder.append(
				inThrowable.getStackTrace()[0].getClassName() + "." +
				inThrowable.getStackTrace()[0].getMethodName() + ":" +
				System.getProperty("line.separator") + inMessage + ".");
		U.print(U.verbose(aStringBuilder.toString()));
		return aStringBuilder.toString();
	}

	public static String verbose(String inMessage){
		if (U.isVerbose()){
			System.err.println(inMessage);
		}
		return inMessage;
	}

	/*
	 * Verbose Switches
	 */

	public static boolean isVerbose(boolean inIsVerbose) {
		U.isVerbose = inIsVerbose;
		return U.isVerbose();
	}

	public static boolean isVerbose() {
		return U.isVerbose;
	}

	/*
	 * Random
	 */

	public static BigInteger probablePrime(int bitLength){

		// Test

		if (bitLength < 2) {
			throw new IllegalArgumentException();
		}

		// Return

		return R.probablePrime(bitLength);
	}

	/*
	 * Keygen
	 */

	/**
	 * Instances of this class are not meant to be shared. Private data is being exposed.
	 *
	 *
	 * @param in_k
	 * @param in_e
	 * @param in_t
	 * @return
	 */
	public static RSAData keygen(int in_k, BigInteger in_e, double in_t){

		// Tests

		if (in_k % 2 != 0){
			throw new IllegalArgumentException("k has to be even");
		}

		if ( in_t > R.T()){
			throw new IllegalArgumentException("Your T is bigger than mine");
		}

		// Setup

		final BigInteger _2powkminus1 = U.TWO.pow(in_k - 1); //TODO: enum & k
		final BigInteger _2powk_minus1 = ((U.TWO.pow(in_k)).subtract(U.ONE)).negate();

		// Algorithm

		BigInteger r = U.selectUniformlyFromXTo(_2powkminus1, _2powk_minus1);
		while (
				U.isGcdGreaterThanOne(in_e, r.subtract(U.ONE))
				&& ! (U.isCongruent(r, 3, 4))){ // _R.get().probablePrime(int k) obsoletes primetest
				r = r.add(U.ONE);
		}

		BigInteger s = U.selectUniformlyFromXTo(_2powkminus1, _2powk_minus1);
		while (
				U.isGcdGreaterThanOne(in_e, s.subtract(U.ONE))
				&& ! (U.isCongruent(r, 3, 4))
				&& ! (s.equals(r)) ){
			s = s.add(U.ONE);
		}

		final BigInteger p = r;
		final BigInteger q = s;
//                 phi(N)= (p -          1   ) *       (q -          1)
		BigInteger phi_N = (p.subtract(U.ONE)).multiply(q.subtract(U.ONE));
		BigInteger d = in_e.modInverse(phi_N);

		return new RSAData(p, q, in_e, d);
	}

	/**
	 * Instances of this class are not meant to be shared. Private data is being exposed.
	 *
	 *
	 * @param in_k
	 * @param in_e
	 * @param in_t
	 * @return
	 */
	public static RSAData keygen(BigInteger in_p, BigInteger in_q, BigInteger in_e){

		// Tests
		// in_p and in_q equal k size

		// Setup

		// Algorithm

		BigInteger phi_N = (in_p.subtract(U.ONE)).multiply(in_q.subtract(U.ONE));
		BigInteger d = in_e.modInverse(phi_N);
		return new RSAData(in_p, in_q, in_e, d);
	}

	/*
	 * Jacobi
	 * http://www.cs.princeton.edu/courses/archive/fall04/frs125/Toss.java
	 */

	/** Calculate the Jacobi Number of x and n **/
	public static int jacobi(BigInteger x, BigInteger n) {
		if (!x.gcd(n).equals(U.ONE))
			return 0;
		if (x.equals(U.ONE))
			return 1;
		if (x.equals(U.TWO)) {
			BigInteger nmod8 = n.mod(U.EIGHT);
			if (nmod8.equals(U.ONE) || nmod8.equals(U.SEVEN))
				return 1;
			else
				return -1;
		}
		if (x.compareTo(n) >= 0)
			return jacobi(x.mod(n), n);
		if (x.mod(U.TWO).equals(U.ZERO))
			return (jacobi(x.divide(U.TWO), n) * jacobi(U.TWO, n));
		if (n.mod(U.TWO).equals(U.ZERO))
			return (jacobi(x, n.divide(U.TWO)) * jacobi(x, U.TWO));
		if (x.mod(U.FOUR).equals(U.ONE) || n.mod(U.FOUR).equals(U.ONE))
			return jacobi(n, x);
		if (x.mod(U.FOUR).equals(U.THREE) && n.mod(U.FOUR).equals(U.THREE))
			return -jacobi(n, x);
		if (x.equals(U.NEGONE)) {
			if (n.mod(U.FOUR).equals(U.ONE))
				return 1;
			else
				return -1;
		}
		throw new RuntimeException("Error in jacobi");
	}

	public static double log(BigInteger g, BigInteger h) {
		U.verbose(new Throwable(), "not implemented yet");
		return 0;
	}

	public static BigInteger pow(BigInteger g, BigInteger x) {
		U.verbose(new Throwable(), "not implemented yet");
		return null;
	}

	/*
	 * layer 4
	 */

	private static void print(String inString){
		L4.printString(inString);
	}
}