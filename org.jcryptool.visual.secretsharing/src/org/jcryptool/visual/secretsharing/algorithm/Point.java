// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.secretsharing.algorithm;

import java.math.BigInteger;

/**
 * @author Oryal Inel
 * @version 1.0
 */
public class Point {
	private boolean infinite;
	private BigInteger y;
	private BigInteger x;

	/**
	 * creates a new point
	 * @param x the x value
	 * @param y the y value
	 */
	public Point(BigInteger x, BigInteger y) {
		this.x = x;
		this.y = y;
		this.infinite = false;
	}

	/**
	 * creates a new point, but the y value is not set
	 * @param x the x value
	 */
	public Point(BigInteger x) {
		this.x = x;
	}

	/**
	 * creates a new point (the infinite point)
	 */
	public Point() {
		this.x = BigInteger.ZERO;
		this.y = BigInteger.ZERO;
		this.infinite = true;
	}

	/**
	 * @return true if the point represents the infinite point
	 */
	public boolean isInfinite() {
		return infinite;
	}

	@Override
	public String toString() {
		if (infinite)
			return "(inf)";
		return "(" + x + "|" + y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Point) {
			Point point = (Point) obj;
			if (point.isInfinite() && this.isInfinite())
				return true;
			if (point.x == this.x && point.y == this.y)
				return true;
		}
		return false;
	}

	public BigInteger getY() {
		return y;
	}

	public void setY(BigInteger y) {
		this.y = y;
	}

	public BigInteger getX() {
		return x;
	}

	public void setX(BigInteger x) {
		this.x = x;
	}

}
