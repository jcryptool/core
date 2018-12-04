//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sphincs.ui;

/**
 * Holds constants for the plug-in to allow to change them in a systematical
 * fashion
 * 
 * @author Philipp Guggenberger
 */
public abstract class SphincsConstant {

	// height of the plug-in as a whole (this is important for the scrolling of
	// the plug-in)
	public static final int PLUGIN_HEIGTH = 680;

	// width of the plug-in as a whole (this is important for the scrolling of
	// the plug-in)
	public static final int PLUGIN_WIDTH = 900;

	// HSPANLEFT / 2
	public static final int H_SPAN_LEFT = 6;

	// the layout of the plug-in is 3 to one -> 6 / 3
	public static final int H_SPAN_RIGHT = 2;

	// the span of the whole plug-in: left + right
	public static final int H_SPAN_MAIN = H_SPAN_LEFT + H_SPAN_RIGHT;

	// Height of the description of the plug-in in rows
	public static final int DESC_HEIGHT = 4;

}