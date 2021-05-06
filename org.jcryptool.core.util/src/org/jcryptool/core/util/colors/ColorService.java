//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
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
import org.eclipse.swt.graphics.RGB;
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
    
	/**
	 * Blue <br>
	 * Same as <code>SWT.COLOR_BLUE</code>
	 */
    public static final Color BLUE;

    /**
	 * Light Red - best fitting as decent background color for buttons or other areas.
	 * Best used with a black font. White font possible but weak.
	 * <br><br>
	 * RGB: 255, 160, 200<br>HEX: #FFB4C8
	 */
	public static final Color LIGHT_AREA_RED;

	/**
	 * Light Blue - best fitting as decent background color for buttons or other areas.
	 * Best used with a black font. White font possible but weak.
	 * <br><br>
	 * RGB: 190, 220, 255<br>HEX: #BEDCFF
	 */
	public static final Color LIGHT_AREA_BLUE;

	/**
	 * Light Green - best fitting as decent background color for buttons or other areas.
	 * Best used with a black font. White font not possible.
	 * <br><br>
	 * RGB: 203, 255, 100<br>HEX: #CBFF63
	 */
	public static final Color LIGHT_AREA_GREEN;

    static {
        WHITE = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
        GRAY = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
        RED = Display.getDefault().getSystemColor(SWT.COLOR_RED);
        GREEN = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
        LIGHTGRAY = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
        BLACK = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
        YELLOW = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
        BLUE = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);

        LIGHT_AREA_RED = createColor(new RGB(255, 180, 200));
        LIGHT_AREA_BLUE = createColor(new RGB(190, 220, 255));
        LIGHT_AREA_GREEN = createColor(new RGB(203, 255, 100));
    }
    
    /**
     * Gets you a color.<br>Example: getColor(SWT.COLOR_BLUE)
     * @param colorID An SWT.COLOR_...
     * @return a color
     */
    public static Color getColor(int colorID) {
    	return Display.getDefault().getSystemColor(colorID);
    }


    /**
     * Creates a color from the given RGB values. Display is default.
     *
     * @param rgb the RGB values as RGB Object.
     * @return a color
     */
    private static Color createColor(RGB rgb) {
    	return new Color(Display.getDefault(), rgb);
    }
}
