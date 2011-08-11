/**
 * This Class is used to carry out Squaring and multiplication algorithm
 *
 * @author Biqiang Jiang
 * @version 1.0, 01/09/09
 * @since JDK1.5.7
 */

package org.jcryptool.algorithm;

public class SquareandMultiply {

	public long sqmulExcution(int basis, int expont, int mod) {

		// n = exponentiation, m= modular, x= basis, u = the new modular result
		// of the new left moved bit.
		String n_binar = Integer.toBinaryString(expont);

		long res = 1;
		int n_binar_length = n_binar.length();
		int count = 0;

		while (n_binar_length > 0) {

			res = (long) Math.pow(res, 2) % mod;
			if (n_binar.charAt(count) == '1')
				res = (res * basis) % mod;

			count++;
			n_binar_length--;

		}

		return res;

	}

}
