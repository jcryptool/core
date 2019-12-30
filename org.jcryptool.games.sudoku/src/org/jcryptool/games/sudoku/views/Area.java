// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.sudoku.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;

public class Area {
	// final private int SUBTRACTION = 1, DIVISION = 3;
	private int operator, value;
	private List<Point> points;

	public Area() {
		this.operator = -1;
		this.value = -1;
		this.points = new ArrayList<Point>();
	}

	public Area(int operator, List<Point> points, int value) {
		this.operator = operator;
		this.points = new ArrayList<Point>();
		this.value = value;
		for (int i = 0; i < points.size(); i++)
			this.points.add(points.get(i));
	}

	public void addPoint(Point point) {
		this.points.add(point);
	}

	public List<Point> getList() {
		return this.points;
	}

	public int getOperator() {
		return operator;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean pointUsed(Point point) {
		return points.contains(point);
	}
}
