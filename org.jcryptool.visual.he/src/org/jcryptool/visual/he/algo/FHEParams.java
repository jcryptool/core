// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.algo;

/**
 * Holds the parameters for the fully homomorphic encryption scheme by Gentry & Halevi
 * Based on the C code of Gentry & Halevi, see https://researcher.ibm.com/researcher/view_project.php?id=1579
 * @author Coen Ramaekers
 *
 */
public class FHEParams {
	/** the security parameter */
	public int secprm;

	/** the BDD-hardness parameter */
	public double mu;

	/** sparse-subset size */
	public int s;

	/** big-set size */
	public int S;

	/** the precision parameter */
	public int p;

	/** bitsize of coefficients in generating polynomial */
	public int t;

	/** log the dimension */
	public int logn;

	/** log the ratio between elements in a big set */
	public int logR;

	/** expected # of nonzero entries in fresh ciphertexts */
	public int noise;

	/**
	 * sets only the security parameter, the rest will be set in setPrms
	 */
	public FHEParams() {
		secprm = 72;
		t = 0;
		logn = 0;
	}

	/**
	 * @param secprm the security parameter
	 * @param t the bitsize of the coefficients
	 * @param logn the 2-log of the dimension
	 */
	public FHEParams(int secprm, int t, int logn) {
		this.secprm = secprm;
		this.t = t;
		this.logn = logn;
	}

	/**
	 * @param sec the security parameter, 0 for default
	 * @param tt the bitsize of the coefficients, 0 for default
	 * @param ln the 2-log of the dimension, 0 for default
	 */
	public void setPrms(int sec, int tt, int ln) {
		double log2 = Math.log(2.0);
		int deg; // degree of the squashed decryption polynomial
		int n;

		if (sec <= 0) sec = 64;
		secprm = sec;
		double logsec = Math.log(sec)/log2;

		// if dimension not specified, compute it later from default mu value
		if (ln <= 0) mu = 0.11;

		// Compute the parameters s (sparse subset size), p (precision), d (degree)
		// find the first power of two larger than sec/log(sec)
		int ls = Functions.nextPowerOfTwo((int)Math.ceil(sec/logsec));
		s = (1 << ls) -1; 	// s is one less than that power of two
		deg = 2*s;         	// deg = 2s
		p = ls;        		// p = log(s+1)

		// The big-set size S (first constraint)
		double logS = sec/(double)((s+1)/2); // S^{ceil{s/2}} >= 2^{secparam}
		S = (int) Math.ceil(Math.exp(logS*log2));

		// If not specified, compute t using the formulas
		if (tt > 0) {
			 t = tt;
		} else {
			double t2 = Math.ceil(deg*((logS/2) + logsec + 4)) + p + 1;
			// add log(sqrt(n)) to the bit-size
			if (ln > 0) {
				t2 += (ln+1)/2;
			} else { // estimate of same
				t2 += Math.log(Math.ceil(t2*sec/(mu*logsec)))/(2*log2)+0.5;
			}
			t = (int) Math.ceil(t2);
		}

		if (ln > 0) { // If n is specified, compute mu from n,t
		    logn = ln;
		    n = 1 << logn;
		    mu = t*sec/(n*logsec);
		} else {    // Else compute (log of) n from t, mu
		    n = (int) Math.ceil(t*sec/(mu*logsec));
		    logn = Functions.nextPowerOfTwo(n);
		    n = 1 << logn;
		}

		double S2 = Math.ceil( Math.sqrt(t*n*sec/(mu*logsec)) / s );
		if (S < S2) {
		    S = (int) S2;
		    // Update t,n if needed
		    if (tt <= 0) {
		    	logS = Math.log(S2)/log2;
		    	t = (int) Math.ceil(deg*((logS/2) +logsec +4) +(logn/2) +p+1);
		    	if (ln <= 0) {
		    		n = (int) Math.ceil(t*sec/(mu*logsec));
		    		logn = Functions.nextPowerOfTwo(n);
		    		n = 1<<logn;
		    	}
		    }
		}

		// Compute (log of) the ratio R between big-set elements
		logR = (int) Math.ceil(n*t / (double)(s*(S-1)));

		// Compute the noise level, such that {n choose noise/2} > 2^secparam
		noise = (int) Math.ceil(2*secprm/(double)(logn-2));
		if (noise < s) noise = s;      // make sure we're not over-optimistic
	}
}
