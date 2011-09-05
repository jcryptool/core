// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.ECDH.algorithm;

import java.util.ArrayList;

public class ECFm extends EC{
	public static final int ECFm = 2;
	private int[] elements;
	private int G;
	private int M;
	private int indexA;
	private int indexB;
	private final int[][] irreduciblePolynomials = {{11}, {19}, {37, 55, 61}, {67, 103, 109}};

	public ECFm() {
		A = -1;
		B = -1;
		M = -1;
		G = -1;
	}

	public ECFm(int m) {
		A = -1;
		B = -1;
		G = -1;
		setM(m);
	}

	public ECPoint addPoints(ECPoint p, ECPoint q) {
		if(p == null)
			return q;
		if(q == null)
			return p;
		int pX = (p.x == elements.length ? 0 : elements[p.x]);
		int pY = (p.y == elements.length ? 0 : elements[p.y]);
		int qX = (q.x == elements.length ? 0 : elements[q.x]);
		int qY = (q.y == elements.length ? 0 : elements[q.y]);
		if((pX == pY && (pX ^ pY) == qY) || points == null)
			return null;

		int rX = 0;
		int rY = 0;
		ECPoint r = new ECPoint(-1, -1);
		if(p == q) {
			int m = mmi(G, pX);
			if(m == -1)
				return null;
			int lambda = pX ^ multiply(m, pY, true);
			rX = (multiply(lambda, lambda, true) ^ lambda) ^ A;
			rY = multiply(pX, pX, true) ^ multiply(lambda ^ 1, rX, true);
		} else {
			int m = mmi(G, qX ^ pX);
			if(m == -1)
				return null;
			int lambda = multiply(m, qY ^ pY, true);
			rX = (((multiply(lambda, lambda, true) ^ lambda) ^ pX) ^ qX) ^ A;
			rY = (multiply(lambda, pX ^ rX, true) ^ rX) ^ pY;
		}

		for(int i = 0; i < elements.length; i++) {
			if(rX == elements[i])
				r.x = i;
			else if(rY == elements[i])
				r.y = i;
		}
		if(r.x == -1)
			r.x = elements.length;
		if(r.y == -1)
			r.y = elements.length;
		return r;
	}

	public int[] getElements() {
		return elements;
	}

	public int getG() {
		return G;
	}

	public int[] getIrreduciblePolinomials() {
		return irreduciblePolynomials[M - 3];
	}

	public int getM() {
		return M;
	}

	@Override
	public int getType() {
		return ECFm;
	}

	public void setA(int i, boolean index) {
		if(elements == null)
			return;
		if(i == -1) {
			A = 0;
			indexA = elements.length;
		} else if(index) {
			indexA = i;
			A = (i == elements.length ? 0 : elements[i]);
		} else {
			for(int j = 0; j < elements.length; j++){
				if(elements[j] == i) {
					indexA = j;
					A = i;
					j = elements.length;
				}
			}
		}
		calculatePoints();
	}

	public void setB(int i, boolean index) {
		if(elements == null)
			return;
		if(i == -1) {
			B = 0;
			indexB = elements.length;
		} else if(index) {
			indexB = i;
			B = (i == elements.length ? 0 : elements[i]);
		} else {
			for(int j = 0; j < elements.length; j++){
				if(elements[j] == i) {
					indexB = j;
					B = i;
					j = elements.length;
				}
			}
		}
		calculatePoints();
	}

	public void setG(int g, boolean index) {
		if(index)
			G = irreduciblePolynomials[M - 3][g];
		else
			G = g;
		calculateElements();
	}

	public void setM(int m) {
		M = m;
		calculateElements();
	}

	@Override
	protected void updateCurve() {
		calculateElements();
		calculatePoints();
	}

	private void calculateElements() {
		points = null;
		if(M != -1 && G != -1) {
			elements = new int[(int) Math.pow(2, M)-1];
			elements[0] = 1;
			for(int i = 1; i < elements.length; i++) {
				elements[i] = elements[i-1] << 1;
				if(elements[i] >= (int) Math.pow(2, M))
					elements[i] = elements[i] ^ G;
				if(A == elements[i])
					indexA = i;
				if(B == elements[i])
					indexB = i;
			}
			if(A == 0 || indexA > elements.length) {
				A = 0;
				indexA = elements.length;
			}
			if(B == 0 || indexB > elements.length) {
				B = 0;
				indexB = elements.length;
			}
		}
	}

	private void calculatePoints() {
		if(elements != null) {
			points = null;
			ArrayList<ECPoint> list = new ArrayList<ECPoint>();
			numPoints = 0;
			for(int x = 0; x < (int) Math.pow(2, M); x ++) {
				for(int y = 0; y < (int) Math.pow(2, M); y ++) {
					int left;
					int right;
					if(y == (int) Math.pow(2, M) - 1) // the last Y value = 0
						left = 0;
					else
						left = elements[(y * 2) % elements.length] ^ elements[(y + x) % elements.length];

					if(x == (int) Math.pow(2, M) - 1) { // the last X value = 0
						right = (indexB < elements.length ?elements[indexB] : 0);
						left = elements[(y * 2) % elements.length];
					} else {
						right = elements[(x * 3) % elements.length];// ^ elements[(indexA + x * 2) % elements.length] ^ elements[indexB];
						if(indexA < elements.length)
							right ^=  elements[(indexA + x * 2) % elements.length];
						if(indexB < elements.length)
							right ^= elements[indexB];
					}

					if(left == right && (y != (int) Math.pow(2, M) - 1 || x != (int) Math.pow(2, M) - 1)) { // (0, 0) is the point of infinity, and will be added elsewhere
						list.add(new ECPoint(x, y));
						numPoints++;
					}
				}
			}

			points = new ECPoint[numPoints];
			for(int i = 0; i < points.length; i++) {
				points[i] = list.get(i);
			}
		}
	}

	private int divide(int i, int j) {
		if(i == 0 || j == 0)
			return 0;

		int lbI;
		for(lbI = 0; i >> lbI != 0; lbI++) ;
		int lbJ;
		for(lbJ = 0; j >> lbJ != 0; lbJ++) ;

		if(lbI < lbJ)
			return 0;
		if(lbI == lbJ)
			return 1;
		else
			return (1 << (lbI - lbJ)) + divide(i ^ (j << (lbI - lbJ)), j);
	}

	private int multiply(int i, int j, boolean mod) {
		if(i == 0 || j == 0)
			return 0;

		int count = 0;
		int ans = 0;
		for(int k = j; k > 0; k = k >> 1) {
			if(k % 2 == 1 )
				ans = ans ^ (i << count);
			count++;
		}

		if(mod)
			return mod(ans, G);
		else
			return ans;
	}

	private int mod(int i, int p) {
		int lbI;
		for(lbI = 0; i >> lbI != 0; lbI++);
		int lbP;
		for(lbP = 0; p >> lbP != 0; lbP++);

		if(lbI < lbP)
			return i;
		if(lbI == lbP)
			return i ^ p;
		else
			return mod(i ^ (p << (lbI - lbP)), p);
	}

	/**
	 * Calculates the modular multiplicative inverse
	 * @param n
	 * @param m
	 * @return the modular multiplicative inverse of <i>n</i> in a field of F(2^m) with generator <i>m</i>.
	 */
	public int mmi(int m, int n) {
		int[] a = {1, 0, m};
		int[] b = {0, 1, n};

		while(b[2] != 1 && b[2] != 0) {
			int q = divide(a[2], b[2]);
			int[] t = {	a[0] ^ multiply(q, b[0], false),
						a[1] ^ multiply(q, b[1], false),
						a[2] ^ multiply(q, b[2], false)};
			a[0] = b[0]; a[1] = b[1]; a[2] = b[2];
			b[0] = t[0]; b[1] = t[1]; b[2] = t[2];
		}

		if(b[2] == 0)
			return -1;
		else
			return b[1];
	}

	public String toString() {
		String s = "y^2 + x * y = x^3 + "; //$NON-NLS-1$
		if(indexA == 0)
			s += "x^2"; //$NON-NLS-1$
		else if (indexA == 1)
			s += "g*x^2"; //$NON-NLS-1$
		else if (indexA < elements.length)
			s += "g" + indexA + "*x^2"; //$NON-NLS-1$ //$NON-NLS-2$

		if(indexB == 0)
			s += " + 1"; //$NON-NLS-1$
		else if (indexB == 1)
			s += " + g"; //$NON-NLS-1$
		else if (indexB < elements.length)
			s += " + g" + indexB; //$NON-NLS-1$

		s += " generator = " + intToBitString(G); //$NON-NLS-1$
		return s;
	}

	private String intToBitString(int i) {
		String s = ""; //$NON-NLS-1$
		int j = i;
		while(j > 1) {
			s = (j % 2) + s;
			j /= 2;
		}
		s = (j % 2) + s;
		return s;
	}
}
