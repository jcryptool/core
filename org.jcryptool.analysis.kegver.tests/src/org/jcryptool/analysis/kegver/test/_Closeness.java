// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.test;


public class _Closeness {

	/**
	 * We refer to a probability d as overwhelming in parameter l if for any polynomial poly
	 * there is some L that d>1-(1/|poly(l)|) for l > L.
	 *
	 * @param d a probability from 0 to 1.
	 * @param l a positive parameter
	 * @param P a polynomial
	 * @return wheter d is overwhelming in l
	 */
	public static boolean isOverwhelming(double d, int l, Polynomial P){
		// Test inputs

		if (
				! (0 <= d && d <= 100) &&	// probability has to be [0,1]
				! (0 <= l)){				// the parameter mus be a positive integer, because of Polynomial.evaluate(int x)
			throw new IllegalArgumentException();
		}

		// Setup

		boolean isOverwhelming = false;

		// Calculate

		for (int L = l; L >= 0 ; L--){		// find a L between 0 and l
			if (d > _Closeness.evaluate(L, P)){	// that has a higher d when using the formula stated in the method description
				isOverwhelming = true;
				break;
			}
		}

		return isOverwhelming;
	}

	/**
	 * We refer to a probability d as overwhelming in parameter l if for any polynomial poly
	 * there is some L that d>1-(1/|poly(l)|) for l > L.
	 */
	public static double evaluate(int L, Polynomial P){
		return 1d - (1d/Math.abs(P.evaluate(L)));
	}
}
