//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.babystepgiantstep.views;

import java.math.BigInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * @author Miray Inel
 * 
 */
public interface Constants {
/*
	 * COLORS
	 */
	public static final Color BLACK = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	public static final Color WHITE = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	public static final Color RED = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	public static final Color GREEN = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
	public static final Color BLUE = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
	public static final Color MAGENTA = Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA);

	public static final Color LIGHTGREY = new Color(null, 240, 240, 240);
	public static final Color LIGHTBLUE = new Color(null, 0, 255, 255);
	public static final Color PURPLE = new Color(null, 255, 0, 255);
	public static final Color DARKPURPLE = new Color(null, 148, 3, 148);

	/*
	 * UNICODE CONSTANTS
	 */
	public static final String uCongruence = ("\u2261"); //$NON-NLS-1$
	public static final BigInteger LIMIT = BigInteger.TEN.pow(24);

}
