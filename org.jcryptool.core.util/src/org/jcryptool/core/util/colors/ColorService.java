//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.util.colors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * Utility class for all colors used in JCrypTool. Plug-ins should use this class for all color purposes rather
 * than instantiating a color themselves. You do not have to dispose any of these colors after usage.
 *
 * @author Dominik Schadow
 * @version 1.0.0
 */
public class ColorService {
	/**
	 * White <br>
	 * Same as <code>SWT.COLOR_WHITE</code>
	 */
    public static final Color WHITE;
    
	/**
	 * Green <br>
	 * Same as <code>SWT.COLOR_GREEN</code>
	 */
    public static final Color GREEN;
    
	/**
	 * Red <br>
	 * Same as <code>SWT.COLOR_RED</code>
	 */
    public static final Color RED;
    
	/**
	 * Lightgray (normal background gray> <br>
	 * Same as <code>SWT.COLOR_WIDGET_BACKGROUND</code>
	 */
    public static final Color LIGHTGRAY;
    
	/**
	 * Dark Gray <br>
	 * Same as <code>SWT.COLOR_DARK_GRAY</code>
	 */
    public static final Color GRAY;
    
	/**
	 * Black <br>
	 * Same as <code>SWT.COLOR_BLACK</code>
	 */
    public static final Color BLACK;
    
	/**
	 * Yellow <br>
	 * Same as <code>SWT.COLOR_YELLOW</code>
	 */
    public static final Color YELLOW;

    static {
        WHITE = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
        GRAY = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
        RED = Display.getDefault().getSystemColor(SWT.COLOR_RED);
        GREEN = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
        LIGHTGRAY = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
        BLACK = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
        YELLOW = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
    }
    
    /**
     * Gets you a color.<br>Example: getColor(SWT.COLOR_BLUE)
     * @param colorID An SWT.COLOR_...
     * @return a color
     */
    public static Color getColor(int colorID) {
    	return Display.getDefault().getSystemColor(colorID);
    }
}
