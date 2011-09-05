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

public class ECFp extends EC{
	public static final int ECFp = 1;
	private int P;

	public ECFp() {
		 A = -1;
		 B = -1;
		 P = -1;
	}

	public ECFp(int a, int b, int p) {
		updateCurve(a, b, p);
	}

	public int getP() {
		return P;
	}

	@Override
	public int getType() {
		return ECFp;
	}

	public void setP(int p) {
		P = p;
		updateCurve();
	}

	public void updateCurve(int a, int b, int p) {
		A = a;
		B = b;
		P = (p > 3 ? p : 3); //P cannot be lower than 3
		updateCurve();
	}

	@Override
	protected void updateCurve() {
		if(A < 0 || A > P || B < 0 || B > P || -16 * (4 * Math.pow(A, 3) + 27 * Math.pow(B, 2)) == 0) {
			points = null;
			pointsX = null;
			pointsY = null;
			return;
		}
		ArrayList<ECPoint> list = new ArrayList<ECPoint>();
		numPoints = 1;

		//search the curve for points
		for(int x = 0; x < P; x++) {
			for(int y = 0; y < P; y++) {
				int left = (int) (Math.pow(y, 2) % P);
				int right = (int) ((Math.pow(x, 3) + A * x + B) % P);
				if(left == right) {
					list.add(new ECPoint(x, y));
					numPoints++;

					// When y != 0, the point is mirrored on the x-axis
					if(y != 0) {
						list.add(new ECPoint(x, P - y));
						numPoints++;
						x++;
						if(x == P)
							y = P;
						else
							y = -1;
					}
				}
			}
		}

		//set the points to the array
		points = new ECPoint[list.size()];
		for(int i = 0; i < points.length; i++)
			points[i] = list.get(i);
	}

	public ECPoint addPoints(ECPoint p, ECPoint q) {
		if(p == null)
			return q;
		if(q == null)
			return p;
		if(p.x == q.x && p.y == -q.y)
			return null;
		int i;
		if(p.equals(q)) {//p.x == q.x && p.y == q.y) {
			int m = mmi(P, mod(2 * p.y, P));
			if(m == -1)
				return null;
			i = mod(mod(3 * (int)Math.pow(p.x, 2) + A, P) * m, P);
		} else {
			int m = mmi(P, mod(q.x - p.x, P));
			if(m == -1)
				return null;
			i = mod(mod(q.y - p.y, P) * m, P);
		}
		int x = mod((int) Math.pow(i, 2) - p.x - q.x, P);
		int y = mod(i * (p.x - x) - p.y, P);
		return new ECPoint(x, y);
	}

	/**
	 * Calculates i%p within 0 <= i%p < p
	 * @param i - the integer
	 * @param p - the modulo
	 * @return the value of i%p there the result >= 0
	 */
	public int mod(int i, int p) {
		int ret = i % p;
		if (ret < 0)
			ret += p;
		return ret;
	}

	/**
	 * Calculates the modular multiplicative inverse
	 * @param n
	 * @param m
	 * @return the modular multiplicative inverse of <i>i</i> in the field of F(<i>p</i>)
	 */
	public int mmi(int m, int n) {
		int[] a = {1, 0, m};
		int[] b = {0, 1, n};

		while(b[2] != 1 && b[2] != 0) {
			int q = a[2] / b[2];
			int[] t = {a[0] - q * b[0], a[1] - q * b[1], a[2] - q * b[2]};
			a[0] = b[0]; a[1] = b[1]; a[2] = b[2];
			b[0] = t[0]; b[1] = t[1]; b[2] = t[2];
		}

		if(b[1] < 0)
			b[1] += m;
		if(b[2] == 0)
			return -1;
		else
			return b[1];
	}

	public String toString() {
		String s = "(y^2) mod " + P + " = (x^3"; //$NON-NLS-1$ //$NON-NLS-2$
		if(A == 1)
			s += "+ x"; //$NON-NLS-1$
		else if(A == -1)
			s += "- x"; //$NON-NLS-1$
		else if(A != 0)
			s += (A < 0 ? " - " + -A : " + " + A) + "*x"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if(B != 0)
			s += (B < 0 ? " - " + -B : " + " + B); //$NON-NLS-1$ //$NON-NLS-2$
		s += ") mod " + P; //$NON-NLS-1$
		return s;
	}

}
