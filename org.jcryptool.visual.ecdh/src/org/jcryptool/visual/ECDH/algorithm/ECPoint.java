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

public class ECPoint{
	private boolean infinite;
	public int y;
	public int x;

	/**
	 * creates a new point
	 * @param x the x value
	 * @param y the y value
	 */
	public ECPoint(int x, int y) {
		this.x = x;
		this.y = y;
		this.infinite = false;
	}

	/**
	 * creates a new point (the infinite point)
	 */
	public ECPoint() {
		this.x = 0;
		this.y = 0;
		this.infinite = true;
	}

	/**
	 * @return true if the point represents the infinite point
	 */
	public boolean isInfinite() {return infinite;}

	@Override
	public String toString() {
		if(infinite)
			return "(inf)"; //$NON-NLS-1$
		//return "(" + x + "|" + y + ")";
		return "(" + x + ", " + y + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ECPoint){
			ECPoint point = (ECPoint) obj;
			if(point.isInfinite() && this.isInfinite())
				return true;
			if(point.x == this.x && point.y == this.y)
				return true;
		}
		return false;
	}
}
