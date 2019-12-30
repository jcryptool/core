// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.ecc.algorithm;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Point;

public class EC {
	public static final int EC = 0;
	protected FpPoint[] points;
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

	protected void setPoints(FpPoint[] p) {
		points = p;
	}

	/**
	 * Diese Methode berechnet Punkte auf der Elliptischen Kurve
	 * die in der GUI als Werte für P und Q mit der Maus gewählt werden können.
	 * In der Nähe von Nullstellen wird die Schrittweite auf 1/1000 der normalen
	 * Schrittweite (xStep) herabgesetzt um ein möglichst genaues Ergebnis für 
	 * y-Werte (yVal) nahe 0  zu erhalten. 
	 */
	private void updateCurve() {
		if(4 * Math.pow(A, 3) + 27 * Math.pow(B, 2) == 0) {
			points = null;
			pointsX = null;
			pointsY = null;
			return;
		}
		ArrayList<Double> listX = new ArrayList<Double>();
		ArrayList<Double> listY = new ArrayList<Double>();
		numPoints = 1;
		double xStep = Math.pow(gridSize, -1);
		double xVal;
		double yVal;
		boolean lastPoint = false;
		boolean firstPoint = false;
		for (int x = -(canvasSize.x / 2); x < canvasSize.x / 2; x++) {
			xVal = x * xStep;
			double ans = Math.pow(xVal, 3) + A * xVal + B;
			//Man kann nur Wurzeln aus positiven Zahlen ziehen.
			//Daraus folgt, wenn ans kleiner als 0 ist exisitiert 
			//für diesen xVal keine Lösung.
			//Nur für xVal für die es auch einen yVal gibt wird weiter gerechnet.
			if(ans >= 0) {
				yVal = Math.sqrt(ans);
				
				//Den ersten gefundene Punkt mit y Wert = 0 einspeichern.
				//Hier wird geprüft, ob 
				//A: Es der erste Punkt ist der eingespeichert wird (numPoints == 1).
				//Dieser muss eine Nullstelle der Funktion sein. 
				//B: Ob die Funktion eine Lücke hat (firstPoint) und es nach der
				//Lücke wieder Funktionswerte gibt.
				if (numPoints == 1 || firstPoint) {
					//smallest X Value Near Zero
					double sXVNZ = xVal;
					//Es wird ein yVal gesucht, welcher näher an 0 liegt 
					//als der yVal der sich bei einsetzen von xVal ergibt.
					do {
						sXVNZ = sXVNZ - xStep *  0.001;
					} while ((Math.pow(sXVNZ, 3) + A * sXVNZ + B) >= 0);
					
					//Werte auf zwei Nachkommastellen runden
					listX.add((double) ((Math.round(sXVNZ*100))) / 100);
					listY.add(0.0);
					firstPoint = false;
					numPoints++;
				}

				//Falls y!=0 dann füge den positiven und den negativen 
				//y-Wert zur Liste listY hinzu.
				if (yVal != 0) {
					listX.add(xVal);
					listY.add(yVal);
					listX.add(xVal);
					listY.add(-yVal);
					numPoints += 2;
				}
				lastPoint = true;
			} else if (lastPoint) {
				double sXVNZ = xVal;
				//Es wird ein yVal gesucht, welcher näher an 0 liegt 
				//als der yVal der sich bei einsetzen von xVal ergibt.
				do {
					sXVNZ = sXVNZ - xStep *  0.001;
					//Der Unterschied zur oberen Schleife zur Annäherung
					//an die exakte Nullstelle ist, dass in diesem Fall mit
					//einem xVal gestartet wird, für den es keine yVal gibt,
					//Der erste sXVNZ für den es einen yVal gibt wird genommen
					//und es wird angenommen, dass dieser die Nullstelle ist.
				} while ((Math.pow(sXVNZ, 3) + A * sXVNZ + B) <= 0);
				//Werte auf zwei Nachkommastellen runden.
				listX.add((double) ((Math.round(sXVNZ * 100))) / 100);
				listY.add(0.0);
				numPoints++;
				lastPoint = false;
				firstPoint = true;
			}
			
		}

		pointsX = new double[listX.size()];
		pointsY = new double[listY.size()];
		points =  new FpPoint[listX.size()];
		for(int i = 0; i < points.length; i++) {
			if(listX.get(i) != null){
				pointsX[i] = listX.get(i);
				
				pointsY[i] = listY.get(i);

				points[i] = new FpPoint((int)(listX.get(i) * 100), (int)(listY.get(i) * 100));
			}
		}
	}

	public FpPoint[] getPoints() {
		return points;
	}

	public FpPoint addPoints(FpPoint p, FpPoint q) {
		if(q == null || p == null)
			return null;
		if(p.isInfinite())
			return q;
		if(q.isInfinite())
			return p;
		if((p.x == q.x && p.y + q.y < 10 && p.y + q.y > -10) || points == null)
			return new FpPoint();

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
			/*for(int i = 0; tempX != null && i < tempX.size() && pX < 0; i++) {
				if(p.x == (int)(tempX.get(i) * 100) && p.y == (int)(tempY.get(i) * 100)) {
					pX = tempX.get(i);
					pY = tempY.get(i);
				}
			}
			if(pX < 0) {*/
			pX = (double) p.x / 100;
			pY = (double) p.y / 100;
				/*pY = Math.sqrt(Math.pow(pX, 3) + pX * A + B);
			}*/
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
			/*for(int i = 0; tempX != null && i < tempX.size() && qX < 0; i++) {
				if(q.x == (int)(tempX.get(i) * 100) && q.y == (int)(tempY.get(i) * 100)) {
					qX = tempX.get(i);
					qY = tempY.get(i);
				}
			}
			if(qX < 0) {*/
			qX = (double) q.x / 100;
			qY = (double) q.y / 100;
				/*qY = Math.sqrt(Math.pow(qX, 3) + qX * A + B);
			}*/
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

		/*i = -1;
		for(int j = 0; j < points.length && i < 0; j++) {
			if(pointsX[j] == x && pointsY[j] == y)
				i = 1;
		}
		if(i < 0) {
			tempX.add(x);
			tempY.add(y);
		}*/

		return new FpPoint((int)(x * 100), (int)(y * 100));
	}

	public FpPoint multiplyPoint(FpPoint p, int m) {
		if(p == null)
			return null;
		if(p.isInfinite() || m < 1 || points == null)
			return new FpPoint();

		FpPoint r = p;
		/*if(m == 1) {
			r = p;
		} else if (m == 2) {
			r = addPoints(p, p);
		} else if(m % 2 == 0) {
			r = addPoints(multiplyPoint(p, m / 2), multiplyPoint(p, m / 2));
		} else {
			r = addPoints(multiplyPoint(p, m / 2), multiplyPoint(p, (m / 2) + 1));
		}*/
		for(int i = 1; i < m; i++)
			r = addPoints(p, r);
		return r;
	}

	@Override
	public String toString() {
		String s = "y\u00b2 = x\u00b3"; //$NON-NLS-1$
		if(A == 1)
			s += " + x"; //$NON-NLS-1$
		else if(A == -1)
			s += " - x"; //$NON-NLS-1$
		else if(A != 0)
			s += (A < 0 ? " - " + -A : " + " + A) + "x"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if(B != 0)
			s += (B < 0 ? " - " + -B : " + " + B); //$NON-NLS-1$ //$NON-NLS-2$
		return s;
	}

}
