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

import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;

/**
 * This class is used to compute the multiplication of "Double and Add" algorithm as Q = kP

 * @author  Biqiang Jiang

 * @version 1.0, 01/09/09

 * @since   JDK1.5.7

 */
public class ECCMultiply {

	public ECPoint eccMultiply(ECPoint p, int k, int a, ECFieldFp ecf){

		String kInBinary = Integer.toBinaryString(k);

		int klength = kInBinary.length();
		ECCAdd eccAdd = new ECCAdd();
		ECCDouble eccDouble = new ECCDouble();

		int counter = 2;

		ECPoint ecPoint = p;

		while(klength-1>0){

			if(Character.valueOf(kInBinary.charAt(counter-1)).equals('0')){

				ecPoint = eccDouble.eccDouble(ecPoint, a, ecf);

			}
			else if(Character.valueOf(kInBinary.charAt(counter-1)).equals('1')) {

				ecPoint = eccDouble.eccDouble(ecPoint, a, ecf);

				ecPoint = eccAdd.ecAddition(p, ecPoint, ecf);

			}
			klength--;
			counter++;
		}
		return ecPoint;
	}
}
