//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.library;

//----------------------------------------------------------
//Compute square root of large numbers using Heron's method
//----------------------------------------------------------

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author <a href=mailto:megilleland@yahoo.com>Michael Gilleland</a><br />
 *         as found on <a href="http://www.merriampark.com/bigsqrt.htm">merriampark.com</a><br />
 *         slightly modified by Michael Gaber
 */
public class BigSquareRoot {

	private static BigDecimal TWO = new BigDecimal("2"); //$NON-NLS-1$
	public static final int DEFAULT_MAX_ITERATIONS = 50;
	public static final int DEFAULT_SCALE = 10;

	private static BigDecimal error;
	private static int iterations;
	private static boolean traceFlag;
	private static int scale = DEFAULT_SCALE;
	private static int maxIterations = DEFAULT_MAX_ITERATIONS;

	// ---------------------------------------
	// The error is the original number minus
	// (sqrt * sqrt). If the original number
	// was a perfect square, the error is 0.
	// ---------------------------------------

	public static BigDecimal getError() {
		return error;
	}

	// -------------------------------------------------------------
	// Number of iterations performed when square root was computed
	// -------------------------------------------------------------

	public static int getIterations() {
		return iterations;
	}

	// -----------
	// Trace flag
	// -----------

	public static boolean getTraceFlag() {
		return traceFlag;
	}

	public static void setTraceFlag(final boolean flag) {
		traceFlag = flag;
	}

	// ------
	// Scale
	// ------

	public static int getScale() {
		return scale;
	}

	public static void setScale(final int scal) {
		scale = scal;
	}

	// -------------------
	// Maximum iterations
	// -------------------

	public static int getMaxIterations() {
		return maxIterations;
	}

	public static void setMaxIterations(final int maxIteration) {
		maxIterations = maxIteration;
	}

	// --------------------------
	// Get initial approximation
	// --------------------------

	private static BigDecimal getInitialApproximation(final BigDecimal n) {
		final BigInteger integerPart = n.toBigInteger();
		int length = integerPart.toString().length();
		if ((length % 2) == 0) {
			length--;
		}
		length /= 2;
		final BigDecimal guess = ONE.movePointRight(length);
		return guess;
	}

	// ----------------
	// Get square root
	// ----------------

	public static BigDecimal get(final BigInteger n) {
		return get(new BigDecimal(n));
	}

	public static BigDecimal get(final BigDecimal n) {

		// Make sure n is a positive number

		if (n.compareTo(ZERO) <= 0) {
			throw new IllegalArgumentException();
		}

		final BigDecimal initialGuess = getInitialApproximation(n);
		BigDecimal lastGuess = ZERO;
		BigDecimal guess = new BigDecimal(initialGuess.toString());

		// Iterate

		iterations = 0;
		boolean more = true;
		while (more) {
			lastGuess = guess;
			guess = n.divide(guess, scale, BigDecimal.ROUND_HALF_UP);
			guess = guess.add(lastGuess);
			guess = guess.divide(TWO, scale, BigDecimal.ROUND_HALF_UP);
			error = n.subtract(guess.multiply(guess));
			if (++iterations >= maxIterations) {
				more = false;
			} else if (lastGuess.equals(guess)) {
				more = error.abs().compareTo(ONE) >= 0;
			}
		}
		return guess;

	}

	// ----------------------
	// Get random BigInteger
	// ----------------------

	public static BigInteger getRandomBigInteger(final int nDigits) {
		final StringBuffer sb = new StringBuffer();
		final java.util.Random r = new java.util.Random();
		for (int i = 0; i < nDigits; i++) {
			sb.append(r.nextInt(10));
		}
		return new BigInteger(sb.toString());
	}
}
