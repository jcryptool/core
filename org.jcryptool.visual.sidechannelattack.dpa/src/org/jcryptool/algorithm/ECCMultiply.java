/**
 * This class is used to compute the multiplication of "Double and Add" algorithm as Q = kP

 * @author  Biqiang Jiang

 * @version 1.0, 01/09/09

 * @since   JDK1.5.7

 */

package org.jcryptool.algorithm;

import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;

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
