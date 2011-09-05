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

import org.eclipse.swt.graphics.Point;

public class EC {
	public static final int EC = 0;
	protected ECPoint[] points;
	protected int A;
	protected int B;
	private int gridSize;
	protected int numPoints;
	protected double[] pointsX;
	protected double[] pointsY;
	private Point canvasSize;

	/**
	 * Standard constructor which sets A to 1 and B to 0
	 */
	public EC() {
		A = 1;
		B = 0;
	}

	/**
	 * Constructor
	 * @param a- A
	 * @param b- B
	 */
	public EC(int a, int b) {
		A = a;
		B = b;
	}

	public EC(int a, int b, Point cs) {
		A = a;
		B = b;
		canvasSize = cs;
		updateCurve();
	}

	public int getA() {
		return A;
	}

	public int getB() {
		return B;
	}

	public int getType() {
		return EC;
	}

	public void setA(int a) {
		A = a;
		updateCurve();
	}

	public void setB(int b) {
		B = b;
		updateCurve();
	}

	public void updateCurve(int a, int b, int g, Point size) {
		A = a;
		B = b;
		gridSize = g;
		canvasSize = size;
		updateCurve();
	}

	protected void setPoints(ECPoint[] p) {
		points = p;
	}

	protected void updateCurve() {
		if(-16 * (4 * Math.pow(A, 3) + 27 * Math.pow(B, 2)) == 0) {
			points = null;
			pointsX = null;
			pointsY = null;
			return;
		}
		ArrayList<Double> listX = new ArrayList<Double>();
		ArrayList<Double> listY = new ArrayList<Double>();
		numPoints = 1;
		double xStep = Math.pow((double)gridSize, -1);
		double xVal;
		double yVal;
		boolean lastPoint = false;
		for(int x = -(canvasSize.x / 2); x < canvasSize.x / 2; x++) {
			xVal = x * xStep;
			double ans = Math.pow(xVal, 3) + A * xVal + B;
			if(ans >= 0) {//found point
				yVal = Math.sqrt(ans);

				if(numPoints == 1 || (numPoints > 1 && !lastPoint)) {
					double xV = (x - 1) * xStep;
					listX.add(xV);
					listY.add(0.0);
					numPoints++;
				}

				if (yVal != 0) {
					listX.add(xVal);
					listY.add(yVal);
					listX.add(xVal);
					listY.add(-yVal);
					numPoints += 2;
				} else {
					listX.add(xVal);
					listY.add(0.0);
					numPoints++;
				}
				lastPoint = true;
			} else if (lastPoint) {
				listX.add(xVal);
				listY.add(0.0);
				numPoints++;
				listX.add(null);
				listY.add(null);
				lastPoint = false;
			}
		}

		pointsX = new double[listX.size()];
		pointsY = new double[listY.size()];
		points =  new ECPoint[listX.size()];
		for(int i = 0; i < points.length; i++) {
			if(listX.get(i) == null)
				points[i] = null;
			else {
				pointsX[i] = listX.get(i);
				pointsY[i] = listY.get(i);
				points[i] = new ECPoint((int)(listX.get(i) * 100), (int)(listY.get(i) * 100));
			}
		}
	}

	public ECPoint[] getPoints() {
		return points;
	}

	public ECPoint addPoints(ECPoint p, ECPoint q) {
		if(p == null)
			return q;
		if(q == null)
			return p;
		if((p.x == q.x) || points == null)
			return null;

		double pX = -1;
		double pY = -1;
		double qX = -1;
		double qY = -1;
		int indexP = -1;
		for (int i =0; i < points.length; i++) {
			if(points[i] == p) {
				indexP = i;
				i = points.length;
			}
		}
		if (indexP >= 0) {
			pX = pointsX[indexP];
			pY = pointsY[indexP];
		} else {
			pX = (double) p.x / 100;
			pY = (double) p.y / 100;
		}

		int indexQ = -1;
		for (int i =0; i < points.length; i++) {
			if(points[i] == q) {
				indexQ = i;
				i = points.length;
			}
		}
		if (indexQ >= 0) {
			qX = pointsX[indexQ];
			qY = pointsY[indexQ];
		} else  {
			qX = (double) q.x / 100;
			qY = (double) q.y / 100;
		}

		double i;
		if(p.equals(q))
			i = (3 * Math.pow(pX, 2) + A) / (2 * pY);
		else
			i = (qY - pY) / (qX - pX);
		double x = Math.pow(i, 2) - pX - qX;
		double y = (-pY) + i * (pX - x);
		if(y != 0.0) {
			y = Math.sqrt(Math.pow(x, 3) + A * x + B) * (y < 0 ? -1 : 1);
		}

		return new ECPoint((int)(x * 100), (int)(y * 100));
	}

	public ECPoint multiplyPoint(ECPoint p, int m) {
		if(p == null || m < 1 || points == null)
			return null;

		ECPoint r = p;
		for(int i = 1; i < m; i++)
			r = addPoints(p, r);
		return r;
	}

	public String toString() {
		String s = "y^2 = x^3"; //$NON-NLS-1$
		if(A == 1)
			s += "+ x"; //$NON-NLS-1$
		else if(A == -1)
			s += "- x"; //$NON-NLS-1$
		else if(A != 0)
			s += (A < 0 ? " - " + -A : " + " + A) + "*x"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if(B != 0)
			s += (B < 0 ? " - " + -B : " + " + B); //$NON-NLS-1$ //$NON-NLS-2$
		return s;
	}
}
