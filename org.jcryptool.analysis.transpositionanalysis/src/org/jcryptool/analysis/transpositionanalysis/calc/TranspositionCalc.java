//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.calc;

import java.util.Vector;

public class TranspositionCalc {

	/**
	 * Returns possible blocklengths for the text by finding divisors. A lower and
	 * higher margin for the results can be given.
	 * 
	 * @param marginLow  the lower margin for returned results (results can be >=
	 *                   lower margin)
	 * @param marginHigh the higher margin for results (results can be <= margin),
	 *                   or -1 for no limitation
	 * @return
	 */
	public static Integer[] blocklengthsByTextlength(int textLength, int marginLow, int marginHigh) {
		if (marginHigh < 0)
			marginHigh = 99999;

		Vector<Integer> results = new Vector<Integer>(0);

		for (int i = 1; i <= textLength; i++) {
			if (textLength % i == 0) {
				if (i >= marginLow && i <= marginHigh) {
					results.add(i);
				}
			}
		}

		return results.toArray(new Integer[0]);

	}
}
