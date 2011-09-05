// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer2;

import java.math.BigInteger;

public class Tools {

	public static String catchThis(Throwable inT){
		// Return
		return (
				inT.getStackTrace()[0].getClassName() + "." +
				inT.getStackTrace()[0].getMethodName() + ": Line " +
				inT.getStackTrace()[0].getLineNumber() + ": " +
				inT.getMessage()
				);
	}

	/**
	 * @throws ArithmeticException if inK < 3
	 */
	public static BigInteger get2powKMinus1(int inK){		//2^(k-1)
		// Return
		return (BigInteger.valueOf(2)).pow(inK-1);
	}

	/**
	 * @throws ArithmeticException if inK < 2
	 */
	public static BigInteger get2powK_Minus1(int inK){		//(2^k)-1
		// Return
		return ((BigInteger.valueOf(2)).pow(inK)).subtract(BigInteger.ONE);
	}
}