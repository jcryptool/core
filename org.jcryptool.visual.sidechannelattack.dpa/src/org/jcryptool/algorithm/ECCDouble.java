// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2009, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
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
public class ECCDouble {

	public ECPoint eccDouble (ECPoint p, int a, ECFieldFp ecf) {


		BigInteger fieldprime = ecf.getP();

		BigInteger Numerator = ((((p.getAffineX().pow(2)).multiply(new BigInteger("3"))).add(BigInteger.valueOf(a)))).mod(fieldprime);

		BigInteger Denominator = (p.getAffineY().multiply(BigInteger.valueOf(2))).mod(fieldprime);

		if (Denominator.equals(BigInteger.ZERO)){
			return ECPoint.POINT_INFINITY;
		}

		BigInteger InverseOfDenominator = Denominator.modInverse(fieldprime);

		BigInteger tempValue = Numerator.multiply(InverseOfDenominator).mod(fieldprime);

		BigInteger x3 = (tempValue.pow(2).subtract(p.getAffineX().multiply(BigInteger.valueOf(2)))).mod(fieldprime);

		BigInteger y3 = (tempValue.multiply(p.getAffineX().subtract(x3)).subtract(p.getAffineY())).mod(fieldprime);

		ECPoint doubleSumECPoint = new ECPoint(x3,y3);

		return doubleSumECPoint;
	}
}
