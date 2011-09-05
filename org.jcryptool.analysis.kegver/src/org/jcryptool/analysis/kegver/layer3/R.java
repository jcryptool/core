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
import java.security.SecureRandom;
import java.util.Random;

/**
 * R = Random
 *
 * Rumors on the street have it, that java.util.Random is not cryptographically secure.
 * While kegver is based on java.util.Random for now, this might change in the future.
 * This is basically a Wrapper for that case.
 *
 * Dammit! SecureRandom does exist in java.security AND flexiprovider. But they can't be cast into
 * each other. At least, the changes have to be made only in here.
 *
 * @author hkh
 *
 */
public class R {

	/*
	 * Class variables
	 */

	// t in BigInteger.probablyPrime(bitlength, Random): t^(-100)
	private static final double T = Math.pow(2, -100);
	private static Random random = null;

	/*
	 * Wrappers
	 */

	public static BigInteger probablePrime(int bitLength){
		return BigInteger.probablePrime(bitLength, R.getRandom());
	}

	public static double T(){
		return R.T;
	}

	public static boolean isLessProbable(double in_t){
		return R.T < in_t;
	}

	/*
	 * might be useful.
	 */
	public static Random resetRandom(){
		return R.setRandom(new SecureRandom());
	}

	/*
	 * Getter and Setter
	 */

	/**
	 * KEEP PRIVATE! it's supposed to wrap java.util.Random();
	 */
	private static Random getRandom() {
		if(R.random == null){
			R.resetRandom();
		}
		return R.random;
	}

	private static Random setRandom(Random inRandom) {
		R.random = inRandom;
		return R.getRandom();
	}

	public static int nextInt(int i) {
		return R.getRandom().nextInt(i);
	}

	public static BigInteger newBigInteger(int bitLength) {
		return new BigInteger(bitLength, R.getRandom());
	}
}