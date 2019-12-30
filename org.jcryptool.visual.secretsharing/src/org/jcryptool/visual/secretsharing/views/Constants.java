// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.secretsharing.views;

import java.math.BigInteger;

import org.eclipse.swt.graphics.Color;

/**
 * @author Oryal Inel
 * @version 1.0.0
 */
public interface Constants {
	
	/*
	 * COLORS
	 */
	Color BLACK = new Color(null, 0, 0, 0);
	Color WHITE = new Color(null, 255, 255, 255);
	Color RED = new Color(null, 255, 0, 0);
	Color GREEN = new Color(null, 0, 255, 0);
	Color BLUE = new Color(null, 0, 0, 255);
	Color LIGHTGREY = new Color(null, 240, 240, 240);
	Color LIGHTBLUE = new Color(null, 0, 255, 255);
	Color PURPLE = new Color(null, 255, 0, 255);
	Color DARKPURPLE = new Color(null, 148, 3, 148);
	Color MAGENTA = new Color(null, 255, 0, 255);

	/*
	 * UNICODE CONSTANTS
	 */
	String sZero = ("\u2070"); //$NON-NLS-1$
	String sOne = ("\u00B9"); //$NON-NLS-1$
	String sTwo = ("\u00B2"); //$NON-NLS-1$
	String sThree = ("\u00B3"); //$NON-NLS-1$
	String sFour = ("\u2074"); //$NON-NLS-1$
	String sFive = ("\u2075"); //$NON-NLS-1$
	String sSix = ("\u2076"); //$NON-NLS-1$
	String sSeven = ("\u2077"); //$NON-NLS-1$
	String sEight = ("\u2078"); //$NON-NLS-1$
	String sNine = ("\u2079"); //$NON-NLS-1$

	String uCongruence = ("\u2261"); //$NON-NLS-1$
	String uDot = "\u2022"; //$NON-NLS-1$
	String uE = ("\u220A"); //$NON-NLS-1$
	String uI = ("\u1D62"); //$NON-NLS-1$
	String uMinus = "\u208B"; //$NON-NLS-1$
	String uMinusUp = "\u207B"; //$NON-NLS-1$
	String uNotEqual = ("\u2260"); //$NON-NLS-1$
	String uProduct = "\u03A0"; //$NON-NLS-1$
	String uR = ("\u1D63"); //$NON-NLS-1$
	String uST = ("\u2264"); //$NON-NLS-1$
	String uSum = ("\u03A3"); //$NON-NLS-1$
	String uT = ("\u2071"); //$NON-NLS-1$
	String uElem = ("\u2208"); //$NON-NLS-1$

	String uZero = ("\u2080"); //$NON-NLS-1$
	String uOne = ("\u2081"); //$NON-NLS-1$
	String uTwo = ("\u2082"); //$NON-NLS-1$
	String uThree = ("\u2083"); //$NON-NLS-1$
	String uFour = ("\u2084"); //$NON-NLS-1$
	String uFive = ("\u2085"); //$NON-NLS-1$
	String uSix = ("\u2086"); //$NON-NLS-1$
	String uSeven = ("\u2087"); //$NON-NLS-1$
	String uEight = ("\u2088"); //$NON-NLS-1$
	String uNine = ("\u2089"); //$NON-NLS-1$

	/*
	 * MESSAGE CONSTANTS
	 */

	String LAGRANGE_FORMULAR = ("\u03A3 s\u1d62 \u2022 \u03A0 ( x - x\u1d63 ) \u2022 ( x\u1d62 - x\u1d63 )\u207B\u00B9 mod p"); //$NON-NLS-1$
	String LAGRANGE_FORMULAR_RANGE = ("0  \u2264 i,r \u2264 t, i\u2260r"); //$NON-NLS-1$



	/*
	 * MESSAGE RECONSTRUCTION
	 */
	String MESSAGE_RECONSTRUCTION = ("P'(x) = \u03A3 w\u1D62 ="); //$NON-NLS-1$

	BigInteger MINUS_ONE = new BigInteger("-1"); //$NON-NLS-1$
}
