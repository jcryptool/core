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
import java.util.Stack;

/**
 * Recrypt operation of the fully homomorphic encryption scheme by Gentry & Halevi
 * Based on the C code of Gentry & Halevi, see https://researcher.ibm.com/researcher/view_project.php?id=1579
 * @author Coen Ramaekers
 *
 */
public class GHReCrypt {
	/** the lattice determinant */
	public static BigInteger det;

	/** the lattice root */
	public static BigInteger root;

	/** the scheme parameters*/
	public static FHEParams fheparams;

	/** the public key blocks */
	public static BigInteger[] pkBlocksX;

	/** the encryption of the secret vector sigma */
	public static BigInteger[] ctxts;

	/**
	 * Returns the recrypted version of the encrypted number c, this recrypted version has smaller error so that
	 * the homomorphic property is safeguarded.
	 * @param fheparams the scheme parameters
	 * @param c the ciphertext
	 * @param det the lattice determinant
	 * @param root the lattice root
	 * @param pkBlocksX the public key blocks
	 * @param ctxts the encrypted secret vector sigma
	 * @return The recrypted version of the encrypted number c.
	 */
	public static BigInteger recrypt(FHEParams fheparams, BigInteger c, BigInteger det, BigInteger root,
			BigInteger[] pkBlocksX, BigInteger[] ctxts) {
		GHReCrypt.fheparams = fheparams;
		GHReCrypt.det = det;
		GHReCrypt.root = root;
		GHReCrypt.pkBlocksX = pkBlocksX;
		GHReCrypt.ctxts = ctxts;
		BigInteger[][] vars = new BigInteger[fheparams.s][fheparams.p+1];

		for (int i = 0; i < fheparams.s; i++) {
			for (int j = 0; j < fheparams.p+1; j++) {
				vars[i][j] = new BigInteger("0");
			}
		}

		for (int i = 0; i < fheparams.s; i++) {
			vars[i] = processBlock(vars[i], c, i);
		}

		// Use the grade-school algorithm to add up these s (p+1)-bit numbers,
		// return in c the encryption of the XOR of the two left bits
		c = gradeSchoolAdd(vars, det);
		return c;
	}

	/**
	 * Returns the recrypted version of the array of encrypted numbers c, this recrypted version
	 * has smaller error so that the homomorphic property is safeguarded.
	 * @param fheparams the scheme parameters
	 * @param c the ciphertext
	 * @param det the lattice determinant
	 * @param root the lattice root
	 * @param pkBlocksX the public key blocks
	 * @param ctxts the encrypted secret vector sigma
	 * @return The recrypted version of the array of encrypted numbers c.
	 */
	public static BigInteger[] recrypt(FHEParams fheparams, BigInteger[] c, BigInteger det, BigInteger root,
			BigInteger[] pkBlocksX, BigInteger[] ctxts) {

		BigInteger[] out = new BigInteger[c.length];
		for (int i = 0; i < c.length; i++) {
			out[i] = recrypt(fheparams, c[i], det, root, pkBlocksX, ctxts);
		}
		return out;
	}

	/**
	 * Processes the public key blocks using the homomorphic property returns an
	 * array of length p+1 containing the encryptions of the corresponding bits
	 * @param vars holds the encryptions of the bits
	 * @param c the ciphertext
	 * @param i the index
	 * @return The recrypted bits.
	 */
	public static BigInteger[] processBlock(BigInteger[] vars, BigInteger c, int i) {
		int nCtxts = (int) Math.ceil(2*Math.sqrt(fheparams.S));
		int baseIdX = i*nCtxts;

		BigInteger factor = pkBlocksX[i];
		factor = factor.multiply(c).mod(det);

		BigInteger[] psums = new BigInteger[vars.length]; //partial sums
		for (int k = 0; k < vars.length; k++) vars[k]= new BigInteger("0") ; // initialize to zero

		int j,j1,j2;

		for (j = j1 =0; j1 < nCtxts-1; j1++) {       // sk-bits indexed by (j1,*) pairs
		    for (int k = 0; k < psums.length; k++) psums[k] = new BigInteger("0");  // initialize to zero

		    for (j2 = j1 + 1; j2 < nCtxts; j2++) {
		    	// get the top bits of factor/det. The code below assumes
		    	// that p+1 bits can fit in one long
		    	long binary = getBinaryRep(factor, det, vars.length);
		    	if (factor.testBit(0)) {    // "xor" the LSB to column 0
		    		binary ^= (1 << fheparams.p);
		    	}
		    	// For every 1 bit, add the current ciphertext to the partial sums
		    	for (int k = 0; k < psums.length; k++) if (((binary>>k)&1) == 1) {
	    		 	int k2 = psums.length - k - 1;
	    			psums[k2] = psums[k2].add(ctxts[baseIdX + j2]).mod(det);

		    	}
		    	j++;              // done with this element
		    	if (j < fheparams.S) { // compute next element = current * R mod det
		    		factor = factor.shiftLeft(fheparams.logR).mod(det);
		    	} else break;       // don't add more than S elements
	    	}

		    // multiply partial sums by ctxts[j1], then add to sum
		    for (int k = 0; k < vars.length; k++) {
		    	psums[k] = psums[k].multiply(ctxts[baseIdX + j1]).mod(det);
		    	vars[k] = vars[k].add(psums[k]).mod(det);
		    }

		    if (j >= fheparams.S) break;
		}
		// Sanity-check: j should be at least S, else we've missed some terms
		if (j < fheparams.S) return null;
		return vars;
	}

	/**
	 * Calculates n/d with nBits precision
	 * @param n the numerator
	 * @param d the denominator
	 * @param nBits the precision
	 * @return The nBits most significant bits right of the binary point of n/d.
	 */
	public static long getBinaryRep(BigInteger n, BigInteger d, int nBits) {
		// It is assumed that nBits fit in one long integer.
		//(n * 2^nBits)/d gives nBits bits of precision (one more than needed)
		// integer division implies truncation
		BigInteger temp  = (n.shiftLeft(nBits)).divide(d);
		long sn = temp.longValue(); // a single precision variant
		sn = (sn >> 1) + (sn & 1);
		return sn;
	}

	/**
	 * Use the grade-school algorithm to add up these s (p+1)-bit numbers.
	 * @param vars the s (p+1)-bit numbers
	 * @param det the determinant
	 * @return The sum of the s (p+1)-bit numbers modulo the determinant.
	 */
	static BigInteger gradeSchoolAdd(BigInteger[][] vars, BigInteger det) {
		int i,j;
		BigInteger out;
		// Below it is more convenient to have each column of the matrix in
		// a separate stack (since we would want to push carry bits on top)
		@SuppressWarnings("unchecked")
		Stack<BigInteger>[] stack = new Stack[vars[0].length];

		for(j = 0; j < vars[0].length; j++) {
			stack[j] = new Stack<BigInteger>();
			for (i = vars.length-1; i >= 0; i--) {
				stack[j].push(vars[i][j]);
			}
		}

		BigInteger[] sp = null;

		// add columns from right to left, upto column -1
		for (j = vars[0].length - 1; j > 0; j--) {
			int s = stack[j].size();
			int log = Functions.nextPowerOfTwo(s); // (log of) # of carry bits to compute
			if (log > j) log = j;     // no more carry than what can reach col 0
			if ((1<<log) > s) log--; // no more carry than what s bits can produce

			sp = evalSymPolys(stack[j], 1<<log, det); // evaluate symmetric polys

			// The carry bits from this column are sp[2],sp[4],sp[8]... The result
			// for that column is in sp[1] (but for most columns we don't need it)
			int k = 2;
			for (int j2=j-1; j2>=0 && k < sp.length; j2--) {
				stack[j2].push(sp[k]);   // push carry bits on top of their column
				k <<= 1;
			}
		}

		// The result from stack -1 is in sp[1], add to it all the bit in column 0
		out = stack[0].pop();
		while (!stack[0].empty()) {
			out = out.add(stack[0].pop()).mod(det);
		}
		out = out.add(sp[1]).mod(det);
		return out;
	}

	/**
	 * Evaluates symmetric polynomials to be used in the grade school addition.
	 * @param vars the stack containing the column of the matrix
	 * @param deg the degree of the polynomial
	 * @param det the lattice determinant
	 * @return The symmetric polynomial evaluation of the column of the matrix.
	 */
	private static BigInteger[] evalSymPolys(Stack<BigInteger> vars, int deg, BigInteger det) {
		int i, j;
		BigInteger[] out = new BigInteger[deg+1];
		out[0] = new BigInteger("1");
		for (i = 1; i <= deg; i++) out[i] = new BigInteger("0");

		BigInteger tmp;
		for (i=1; !vars.empty(); i++) {  // process the next variable, i=1,2,...
			for (j = Math.min(i,deg); j>0; j--) { // compute the j'th elem. sym. poly
				tmp = out[j-1].multiply(vars.peek()).mod(det);
				out[j] = out[j].add(tmp).mod(det); // out[j] += out[j-1] * vars.top() mod M

				// At the end of the inner loop, out[j] holds the
				// j'th symmetric polynomial in the first i variables
	    	}
	    	vars.pop();  // done with this variable
	  	}
		return out;
	}
}