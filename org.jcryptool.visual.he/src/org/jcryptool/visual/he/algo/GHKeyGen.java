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
import java.math.BigInteger;
import java.util.Random;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

/**
 * Key generation for the fully homomorphic encryption scheme by Gentry & Halevi
 * Based on the C code of Gentry & Halevi, see https://researcher.ibm.com/researcher/view_project.php?id=1579
 * @author Coen Ramaekers
 *
 */
public class GHKeyGen {

	/** bitsize of coefficients in generating polynomial */
	public int t;

	/** dimension */
	public int n;

	/** lattice determinant */
	public BigInteger det;

	/** the root */
	public BigInteger root;

	/** secret key w, one odd coefficient of w(x) */
	public BigInteger w;

	/** the polynomial w(x)*/
	public BigInteger[] W;

	/** the encryption of the secret vector sigma */
	public BigInteger[] ctxts;

	/** the public key blocks */
	public BigInteger[] pkBlocksX;

	/** random generator */
	public Random r = new Random(System.currentTimeMillis());

	/** the polynomial which generats the lattice */
	public Polynomial v;

	/**
	 * Generates a new key pair given the parameters in fheparams, stores the key locally and in the keypair parameter
	 * @param fheparams the scheme parameters
	 * @param keyPair holds the keypair
	 */
	public GHKeyGen(FHEParams fheparams, GHKeyPair keyPair, IProgressMonitor monitor, int work) {
		t = fheparams.t;
		n = 1 << fheparams.logn;

		SubProgressMonitor sm = new SubProgressMonitor(monitor, work/3);
		sm.beginTask("", work/3);
		do { //try until HNF has the desired form, i.e. determinant is odd and lattice contains the vector (-r,1,0,...,0)

			//generate random polynomial with coefficients uniformly random in [-2^t,2^t]
			v = Polynomial.randomPolynomial(n-1,t);

			//verify whether the coefficient sum is odd, otherwise add 1
			int parity = 0;
			for (int i = 0; i < n; i++) {
				parity ^= (v.coeffs[i].testBit(0) ? 1 : 0);
			}
			if (parity == 0) v.coeffs[0].add(new BigInteger("1"));
			if (sm.isCanceled()) return;

		} while (!invModFx(v,fheparams.logn));
		sm.done();
		sm.beginTask("", work/3);
		BigInteger sum = new BigInteger("0");
		BigInteger factor;
		//the public key blocks that squash the decryption scheme
		pkBlocksX = new BigInteger[fheparams.s];
		 //the correct power such that \sum_pkBlocksX[i]*R^pkBlocksIdX[i] = w mod d
		int[] pkBlocksIdX = new int[fheparams.s];
		//make sure the sum is correct
		boolean sumtest = false;

		while (!sumtest) {
			sum = new BigInteger("0");
			//generate the first s-1 randomly
			for (int i = 0; i < fheparams.s-1; i++) {
				byte[] temp = new byte[det.bitLength()/8];
				r.nextBytes(temp);
				pkBlocksX[i] = (new BigInteger(temp)).abs().mod(det);
				pkBlocksIdX[i] = r.nextInt(fheparams.S);
				factor = (new BigInteger("2")).modPow((new BigInteger(Integer.toString(pkBlocksIdX[i]))).multiply(new BigInteger(Integer.toString(fheparams.logR))),det);
				factor = (factor.multiply(pkBlocksX[i])).mod(det);
				sum = (sum.add(factor)).mod(det);
			}
			sum = w.subtract(sum).mod(det);
			//calculate the last x_i from the first s-1, try until the sum is invertible
			while (pkBlocksX[fheparams.s-1] == null) {
				try {
					//if(nTrials2%100==0) System.out.println("trials: " + nTrials2);
					pkBlocksIdX[fheparams.s-1] = r.nextInt(fheparams.S);
					factor = new BigInteger("2").modPow((new BigInteger(Integer.toString(pkBlocksIdX[fheparams.s-1]))).multiply(new BigInteger(Integer.toString(fheparams.logR))),det);
					factor = factor.modInverse(det);
					pkBlocksX[fheparams.s-1] = sum.multiply(factor).mod(det);
				} catch (ArithmeticException e) {

				}
				if (sm.isCanceled()) return;
			}
			// check whether \sum_pkBlocksX[i]*R^pkBlocksIdX[i] = w mod d
			sum = new BigInteger("0");
			for (int i = 0; i < fheparams.s; i++) {
				factor = new BigInteger("2").modPow(new BigInteger(Integer.toString(pkBlocksIdX[i])).multiply(new BigInteger(Integer.toString(fheparams.logR))),det);
				factor = factor.multiply(pkBlocksX[i]).mod(det);
				sum = sum.add(factor).mod(det);
			}
			if (sum.compareTo(w) == 0) {
				sumtest = true;
			}
			if (sm.isCanceled()) return;
		}
		sm.done();
		// Compute the number of ciphertext for each progression,
		// i.e., an integer N such that N(N-1)/2 > S
		sm.beginTask("", work/3);
		int nCtxts = (int) Math.ceil(2*Math.sqrt(fheparams.S));
		int[] bits = new int[nCtxts*fheparams.s];
		for (int i = 0; i < fheparams.s; i++) {
		    // let j1,j2 be the idx'th pair in {nCtxts choose 2}
			int j1, j2;
		    int[] temp = encodeIndex(pkBlocksIdX[i], nCtxts);
		    j1 = temp[0];
		    j2 = temp[1];
		    bits[i*nCtxts + j1] = bits[i*nCtxts + j2] = 1; // set these two bits to one
		    if (sm.isCanceled()) return;
		}
		sm.done();
		ctxts = GHEncrypt.encrypt(fheparams, this, bits);
		keyPair.setKeyPair(det, root, w, ctxts, pkBlocksX);
	}

	/**
	 * Calculates the i'th pair in (m choose 2).
	 * @param i the pair number
	 * @param m the binomial number
	 * @return The i'th pair in (m choose 2), e.g. 0,..,m-2 gives (0,1),..,(0,m-1), m-1,..,2m-4 gives (1,2),..,(1,m-1) etc.
	 */
	int[] encodeIndex(int i, int m) {
		if (i > m*(m-1)/2) { //if so then encoding is impossible
			return null;
		}
		int j1, j2;
		for (j1 = 0; j1 < m-1; j1++) {
			int pairsFor_j1 = m-j1-1;
		    if (pairsFor_j1 > i) { 		// i is one of the pairs (j1,*)
		    	j2 = i + j1+1;       	// j2 is in j1+1,...,m-1
		    	return new int[]{j1,j2};
		    }
		    else i -= pairsFor_j1; // we already counted the pairs for this j1
		}
		return null;
	}

	/**
	 * Verifies whether the polynomial q yields a lattice with the correct HNF and odd resultant,
	 * computes the determinant (or resultant), root and inverse polynomial w(x)
	 * @param q the polynomial yielding the lattice
	 * @param n the dimension
	 * @return True if the polynomial suffices, false otherwise.
	 */
	boolean invModFx(Polynomial q, int n) {
		int N = 1 << n;
		BigInteger res, wi = null;

		if (q.degree >= N) return false;

		// compute resultant and w0
		BigInteger w0, w1;
	    BigInteger rw[] = gzModZ2(q, n);
	    res = rw[0];
	    w0 = rw[1];

	    if (Functions.isEven(res)) {   // Resultant must be odd
	    	return false;
	    }

	    // repeat for the polynomial x*q(x) mod x^N+1
	    Polynomial qx = new Polynomial(N);
	    for (int i = N-1; i > 0; i--) {
	    	qx.setCoeff(i, q.coeffs[i-1]); // copy 1st N-1 coeffs
	    }
	    qx.setCoeff(0, q.coeffs[N-1].negate());  // negate last coeff
	    qx.normalize();
	    rw = gzModZ2(qx, n);
	    res = rw[0];
	    w1 = rw[1];

	  	// now that we have res, w0, w1, set root = w1/w0 mod res
	  	// make sure things are positive
	  	if (res.signum() == -1) {
	  		res = res.negate();
	    	w0 = w0.negate();
	    	w1 = w1.negate();
	  	}
	  	if (w0.signum() < 0) w0 = w0.add(res);
	  	if (w1.signum() < 0) w1 = w1.add(res);

	  	BigInteger inv = Functions.xgcd(w0, res); //returns a = w0^{-1} if w0 invertible, null otherwise
	  	if (inv == null) {
		    return false; // verify that w0^{-1} exists
	  	}
	  	root = w1.multiply(inv).divideAndRemainder(res)[1];	// root= w1 * w0^{-1} mod res

	  	BigInteger tmp = root.modPow(new BigInteger(Integer.toString(N)), res);
	  	//it should hold that root^n = -1 mod res
	  	if (!tmp.add(new BigInteger("1")).equals(res)) {
	  		return false;
	  	}
	  	W = new BigInteger[N]; //get the entire polynomial w(x), for debug purposes
	  	W[0] = w0;
	  	W[1] = w1;
	  	for (int k = 2; k < N; k++) {
	  		W[k] = W[k-1].multiply(root).mod(res);
	  	}
	  	// for decryption only a single odd coefficient of w(x) is necessary
	  	int i = 0;
	  	if ( ((w0.compareTo(res.shiftRight(1)) <= 0)&&Functions.isOdd(w0)) || ((w0.compareTo(res.shiftRight(1)) > 0)&&Functions.isEven(w0)) ) {
	  		wi = w0;
	  	} else {
	  		if ( ((w1.compareTo(res.shiftRight(1)) <= 0)&&Functions.isOdd(w1)) || ((w1.compareTo(res.shiftRight(1)) > 0)&&Functions.isEven(w1)) ) {
	  			wi = w1;
	  		} else {
	  			for (i = 2; i < N; i++) {
	  				w1 = w1.multiply(root).mod(res); //(w1.multiply(root)).divideAndRemainder(res)[1];
	  				if ( ((w1.compareTo(res.shiftRight(1)) <= 0)&&Functions.isOdd(w1)) || ((w1.compareTo(res.shiftRight(1)) > 0)&&Functions.isEven(w1)) ) {
	  					wi = w1;
	  					break;
	  				}
	  			}
	  		}
	  	}

	  	det = res;
	  	w = wi;
	  	return ((i==N) ? false : true); // We get i==N only if all the wi's are even
	}
	 /**
	  * Method based on FFT to find the determinant and the first coefficient of w(x)
	  * this suffices to find w(x) completely
	  * @param q the polynomial
	  * @param n the degree
	  * @return The determinant and the first coefficient of w(x).
	  */
	static BigInteger[] gzModZ2(Polynomial q, int n) {
		int i;
	  	int N = 1 << n;

	  	Polynomial V = new Polynomial(q.coeffs);					// V = q
	  	Polynomial U = new Polynomial(1);	// U = 1
	  	U.setCoeff(0, new BigInteger("1"));
	  	Polynomial F = new Polynomial(N);	// F(x) = x^N +1
	  	F.setCoeff(0, new BigInteger("1"));
	  	F.setCoeff(N, new BigInteger("1"));
	  	Polynomial V2 = new Polynomial(N);

	  	while (N>1) {
	  		V2 = new Polynomial(V.coeffs);
	  		for (i=1; i<=V2.degree; i+=2) {       // set V2(x) := V(-x)
	  			V2.coeffs[i] = V2.coeffs[i].negate(); // negate odd coefficients
	  		}
	  		V = Polynomial.mod(Polynomial.mult(V, V2), F);// V := V(x) * V(-x) mod f(x)
	  		U = Polynomial.mod(Polynomial.mult(U, V2), F);// U := U(x) * V(-x) mod f(x)

  			// Sanity-check: verify that the odd coefficients in V are zero
	  		for (i=1; i <= V.degree; i+=2) if (!V.coeffs[i].equals(new BigInteger("0"))) {
	  			return null;
	  		}

	  		// "Compress" the non-zero coefficients of V
	  		for (i = 1; i <= V.degree/2; i++) V.coeffs[i] = V.coeffs[2*i];
	  		for (   ;   i <= V.degree;   i++) V.coeffs[i] = new BigInteger("0");
	  		V.normalize();

	  		// Set U to the "compressed" ( U(x) + U(-x) ) /2
	  		for (i = 0; i <= U.degree/2; i++) U.coeffs[i] = U.coeffs[2*i];
	  		for (   ; i <= U.degree;   i++) U.coeffs[i] = new BigInteger("0");
	  		U.normalize();

	  		// Set N := N/2 and update F accordingly
	  		F.coeffs[N] = new BigInteger("0");
	  		N >>= 1;
	  		F.coeffs[N] = new BigInteger("1");
	  		F.normalize();
	  	}

	  	return new BigInteger[]{V.coeffs[0],U.coeffs[0]};

	}
}
