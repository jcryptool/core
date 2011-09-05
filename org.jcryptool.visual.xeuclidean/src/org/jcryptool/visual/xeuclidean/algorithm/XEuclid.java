// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.xeuclidean.algorithm;

import java.math.BigInteger;
import java.util.Vector;

/**
 * @author Oryal Inel
 * @version 1.0.0
 */
public class XEuclid {
	private Vector<BigInteger> r;
	private Vector<BigInteger> q;
	private Vector<BigInteger> x;
	private Vector<BigInteger> y;
	private BigInteger inverseX;
	private BigInteger inverseY;

	public XEuclid() {
	}

	public BigInteger xeuclid(BigInteger a, BigInteger b) {
		r = new Vector<BigInteger>();
		q = new Vector<BigInteger>();
		x = new Vector<BigInteger>();
		y = new Vector<BigInteger>();

		BigInteger q_, r_, sign;

		/*
		 * Initialize the table
		 */

		if (b.compareTo(a) > 0) {
			BigInteger tmp = a;
			a = b;
			b = tmp;
		}
		r.add(a);
		r.add(b);
		q.add(BigInteger.ZERO);
		x.add(BigInteger.ONE);
		x.add(BigInteger.ZERO);
		y.add(BigInteger.ZERO);
		y.add(BigInteger.ONE);

		sign = BigInteger.ONE;

		int k = 1;
		while (b.compareTo(BigInteger.ZERO) != 0) {
			r_ = a.mod(b);
			r.add(r_);
			q_ = a.divide(b);
			q.add(q_);
			a = b;
			b = r_;

			x.add(q_.multiply(x.get(k)).add(x.get(k - 1)));
			y.add(q_.multiply(y.get(k)).add(y.get(k - 1)));

			sign = sign.negate();
			k++;
		}

		q.add(BigInteger.ZERO);
		// x.add(sign.negate().multiply(x.lastElement()));
		// y.add(sign.multiply(y.lastElement()));

		x.add(sign.negate().multiply(y.get(y.size() - 2)));
		y.add(sign.multiply(x.get(x.size() - 3)));

		inverseX = y.lastElement();
		inverseY = x.lastElement();

//		output();

		return a;
	}

	public BigInteger getInverseX() {
		return inverseX;
	}

	public BigInteger getInverseY() {
		return inverseY;
	}

	public Vector<BigInteger> getR() {
		return r;
	}

	public Vector<BigInteger> getQ() {
		return q;
	}

	public Vector<BigInteger> getX() {
		return x;
	}

	public Vector<BigInteger> getY() {
		return y;
	}

	public void clear() {
		r = new Vector<BigInteger>();
		q = new Vector<BigInteger>();
		x = new Vector<BigInteger>();
		y = new Vector<BigInteger>();

	}
}
