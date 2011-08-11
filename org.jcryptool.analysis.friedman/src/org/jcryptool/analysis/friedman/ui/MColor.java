//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.friedman.ui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

/**
 * @author SLeischnig This class provides extended color options like transparency.
 */
public class MColor {
    private int r, g, b, alpha;
    private int asInt;
    private Color myColor;

    int[] buf = new int[4];

    private String intToHexStr(final int x) {
        String out = ""; //$NON-NLS-1$

        buf[0] = (byte) ((x & 0xff000000) >>> 24) & 0xFF;
        buf[1] = (byte) ((x & 0x00ff0000) >>> 16) & 0xFF;
        buf[2] = (byte) ((x & 0x0000ff00) >>> 8) & 0xFF;
        buf[3] = (byte) ((x & 0x000000ff)) & 0xFF;

        if (Integer.toHexString(buf[0]).length() < 2) {
            out += "0" + Integer.toHexString(buf[0]); //$NON-NLS-1$
        } else {
            out += Integer.toHexString(buf[0]);
        }
        if (Integer.toHexString(buf[1]).length() < 2) {
            out += "0" + Integer.toHexString(buf[1]); //$NON-NLS-1$
        } else {
            out += Integer.toHexString(buf[1]);
        }
        if (Integer.toHexString(buf[2]).length() < 2) {
            out += "0" + Integer.toHexString(buf[2]); //$NON-NLS-1$
        } else {
            out += Integer.toHexString(buf[2]);
        }
        if (Integer.toHexString(buf[3]).length() < 2) {
            out += "0" + Integer.toHexString(buf[3]); //$NON-NLS-1$
        } else {
            out += Integer.toHexString(buf[3]);
        }

        return out;
    }

    /**
     * Specify the color as RGB values plus the alpha value.
     */
    public MColor(final int R, final int G, final int B, final int Alpha) {
        r = R;
        g = G;
        b = B;
        alpha = Alpha;
        asInt = 65536 * r + 256 * g + b;

        myColor = new Color(Display.getDefault(), r, g, b);
    }

    /**
     * Specify the color as RGB values plus the alpha value, plus instantly setting it.
     */
    public MColor(final int R, final int G, final int B, final int Alpha, final GC gc) {
        r = R;
        g = G;
        b = B;
        alpha = Alpha;
        asInt = 65536 * r + 256 * g + b;

        myColor = new Color(Display.getDefault(), r, g, b);

        setColor(gc);
    }

    /**
     * Specify the color as 6-digit hexadecimal value in a String plus the alpha value.
     */
    public MColor(final String hex, final int Alpha) {
        asInt = Integer.parseInt(hex.toLowerCase(), 16);
        alpha = Alpha;

        String hexStr = hex.toLowerCase();
        r = (int) Integer.parseInt("" + hexStr.charAt(0) + hexStr.charAt(1), 16); //$NON-NLS-1$
        g = (int) Integer.parseInt("" + hexStr.charAt(2) + hexStr.charAt(3), 16); //$NON-NLS-1$
        b = (int) Integer.parseInt("" + hexStr.charAt(4) + hexStr.charAt(5), 16); //$NON-NLS-1$

        myColor = new Color(Display.getDefault(), r, g, b);
    }

    /**
     * Specify the color as 6-digit hexadecimal value in a String plus the alpha value, plus instant
     * setting it.
     */
    public MColor(final String hex, final int Alpha, final GC gc) {
        asInt = Integer.parseInt(hex.toLowerCase(), 16);
        alpha = Alpha;

        String hexStr = hex.toLowerCase();
        r = (int) Integer.parseInt("" + hexStr.charAt(0) + hexStr.charAt(1), 16); //$NON-NLS-1$
        g = (int) Integer.parseInt("" + hexStr.charAt(2) + hexStr.charAt(3), 16); //$NON-NLS-1$
        b = (int) Integer.parseInt("" + hexStr.charAt(4) + hexStr.charAt(5), 16); //$NON-NLS-1$

        myColor = new Color(Display.getDefault(), r, g, b);

        setColor(gc);
    }

    /**
     * Specify the color as an integer value plus the alpha value.
     */
    public MColor(final int hex, final int Alpha) {
        asInt = hex;
        alpha = Alpha;

        String hexStr = intToHexStr(asInt);
        r = (int) Integer.parseInt("" + hexStr.charAt(0) + hexStr.charAt(1), 16); //$NON-NLS-1$
        g = (int) Integer.parseInt("" + hexStr.charAt(2) + hexStr.charAt(3), 16); //$NON-NLS-1$
        b = (int) Integer.parseInt("" + hexStr.charAt(4) + hexStr.charAt(5), 16); //$NON-NLS-1$

        myColor = new Color(Display.getDefault(), r, g, b);
    }

    /**
     * Specify the color as an integer value plus the alpha valu, plus instant setting it.
     */
    public MColor(final int hex, final int Alpha, final GC gc) {
        asInt = hex;
        alpha = Alpha;

        String hexStr = intToHexStr(asInt);
        r = (int) Integer.parseInt("" + hexStr.charAt(0) + hexStr.charAt(1), 16); //$NON-NLS-1$
        g = (int) Integer.parseInt("" + hexStr.charAt(2) + hexStr.charAt(3), 16); //$NON-NLS-1$
        b = (int) Integer.parseInt("" + hexStr.charAt(4) + hexStr.charAt(5), 16); //$NON-NLS-1$

        myColor = new Color(Display.getDefault(), r, g, b);

        setColor(gc);
    }

    /**
     * sets the alpha value
     */
    public final void setAlpha(final int Alpha) {
        alpha = Alpha;
    }

    /**
     * sets the alpha value, and applies this color to a graphical context at the same time
     *
     * @param gc the graphical context
     * @param front whether the color is the fore- or background color.
     */
    public final void setAlpha(final int Alpha, final GC gc, final boolean front) {
        alpha = Alpha;
        if (front) {
            setColor(gc);
        } else {
            setBGColor(gc);
        }
    }

    public final int getRed() {
        return r;
    }

    public final int getGreen() {
        return g;
    }

    public final int getBlue() {
        return b;
    }

    public final int getAlpha() {
        return alpha;
    }

    public final int getIntValue() {
        return asInt;
    }

    /**
     * sets the Color to the specified GC
     *
     * @param targetGC the graphical context
     */
    public final void setColor(final GC targetGC) {
        targetGC.setForeground(myColor);
        targetGC.setAlpha(alpha);
    }

    /**
     * sets the Color as background color to the specified GC
     *
     * @param targetGC the graphical context
     */
    public final void setBGColor(final GC targetGC) {
        targetGC.setBackground(myColor);
        targetGC.setAlpha(alpha);
    }

}
