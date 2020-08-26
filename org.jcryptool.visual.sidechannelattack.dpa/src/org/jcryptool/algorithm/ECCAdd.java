// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2009, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.algorithm;

import java.math.BigInteger;
import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;

/**
 * This classe is used to carry out ECAdd computations.

 * @author  Biqiang Jiang

 * @version 1.0, 01/09/09

 * @since   JDK1.5.7

 */
public class ECCAdd {

//  this class is used to carry out ECAddtion operation with the given parameter Point a, b and Prime field ecf
	public ECPoint ecAddition(ECPoint a, ECPoint b, ECFieldFp ecf){

//  fieldprime,numerator and denominator are used to save the prime field ecf, numerator and denominator
		BigInteger fieldprime = ecf.getP();
		BigInteger Numerator = (b.getAffineY().subtract(a.getAffineY())).mod(fieldprime);
		BigInteger Denominator = (b.getAffineX().subtract(a.getAffineX())).mod(fieldprime);

		if (Denominator.equals(BigInteger.ZERO)){
			return ECPoint.POINT_INFINITY;
		}

		BigInteger InverseOfDenominator = Denominator.modInverse(fieldprime);

		BigInteger tempValue = (Numerator.multiply(InverseOfDenominator)).mod(fieldprime);

		BigInteger x3 = (tempValue.pow(2).subtract(a.getAffineX()).subtract(b.getAffineX())).mod(fieldprime);

		BigInteger y3 = ((tempValue.multiply(a.getAffineX().subtract(x3))).subtract(a.getAffineY())).mod(fieldprime);

		ECPoint addSumECPoint = new ECPoint(x3,y3);

		return addSumECPoint;
	}
}
