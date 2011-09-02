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
package org.jcryptool.analysis.graphtools;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

/**
 * provides extended Color features
 *
 * @author SLeischnig
 */
public class MColor {
    private int r, g, b, alpha;
    private int asInt;

    int[] buf = new int[4];

    private String intToHexStr(int x) {
        String out = ""; //$NON-NLS-1$

        buf[0] = (byte) ((x & 0xff000000) >>> 24) & 0xFF;
        buf[1] = (byte) ((x & 0x00ff0000) >>> 16) & 0xFF;
        buf[2] = (byte) ((x & 0x0000ff00) >>> 8) & 0xFF;
        buf[3] = (byte) ((x & 0x000000ff)) & 0xFF;

        if (Integer.toHexString(buf[0]).length() < 2)
            out += "0" + Integer.toHexString(buf[0]); //$NON-NLS-1$
        else
            out += Integer.toHexString(buf[0]); //$NON-NLS-1$
        if (Integer.toHexString(buf[1]).length() < 2)
            out += "0" + Integer.toHexString(buf[1]); //$NON-NLS-1$
        else
            out += Integer.toHexString(buf[1]); //$NON-NLS-1$
        if (Integer.toHexString(buf[2]).length() < 2)
            out += "0" + Integer.toHexString(buf[2]); //$NON-NLS-1$
        else
            out += Integer.toHexString(buf[2]); //$NON-NLS-1$
        if (Integer.toHexString(buf[3]).length() < 2)
            out += "0" + Integer.toHexString(buf[3]); //$NON-NLS-1$
        else
            out += Integer.toHexString(buf[3]); //$NON-NLS-1$

        return out;
    }

    // Specify the color as RGB values plus the alpha value.
    public MColor(int R, int G, int B) {
        r = R;
        g = G;
        b = B;
        alpha = 255;
        asInt = 65536 * r + 256 * g + b;
    }

    // Specify the color as RGB values plus the alpha value.
    public MColor(int R, int G, int B, int Alpha) {
        r = R;
        g = G;
        b = B;
        alpha = Alpha;
        asInt = 65536 * r + 256 * g + b;
    }

    // Specify the color as RGB values plus the alpha value, plus instantly setting it.
    public MColor(int R, int G, int B, int Alpha, GC gc) {
        r = R;
        g = G;
        b = B;
        alpha = Alpha;
        asInt = 65536 * r + 256 * g + b;

        setColor(gc);
    }

    // Specify the color as 6-digit hexadecimal value in a String plus the alpha value.
    public MColor(String hex) {
        asInt = Integer.parseInt(hex.toLowerCase(), 16);
        alpha = 255;

        String hexStr = hex.toLowerCase();
        r = (int) Integer.parseInt("" + hexStr.charAt(0) + hexStr.charAt(1), 16); //$NON-NLS-1$
        g = (int) Integer.parseInt("" + hexStr.charAt(2) + hexStr.charAt(3), 16); //$NON-NLS-1$
        b = (int) Integer.parseInt("" + hexStr.charAt(4) + hexStr.charAt(5), 16); //$NON-NLS-1$

    }

    // Specify the color as 6-digit hexadecimal value in a String plus the alpha value.
    public MColor(String hex, int Alpha) {
        asInt = Integer.parseInt(hex.toLowerCase(), 16);
        alpha = Alpha;

        String hexStr = hex.toLowerCase();
        r = (int) Integer.parseInt("" + hexStr.charAt(0) + hexStr.charAt(1), 16); //$NON-NLS-1$
        g = (int) Integer.parseInt("" + hexStr.charAt(2) + hexStr.charAt(3), 16); //$NON-NLS-1$
        b = (int) Integer.parseInt("" + hexStr.charAt(4) + hexStr.charAt(5), 16); //$NON-NLS-1$

    }

    // Specify the color as 6-digit hexadecimal value in a String plus the alpha value, plus instant setting it.
    public MColor(String hex, int Alpha, GC gc) {
        asInt = Integer.parseInt(hex.toLowerCase(), 16);
        alpha = Alpha;

        String hexStr = hex.toLowerCase();
        r = (int) Integer.parseInt("" + hexStr.charAt(0) + hexStr.charAt(1), 16); //$NON-NLS-1$
        g = (int) Integer.parseInt("" + hexStr.charAt(2) + hexStr.charAt(3), 16); //$NON-NLS-1$
        b = (int) Integer.parseInt("" + hexStr.charAt(4) + hexStr.charAt(5), 16); //$NON-NLS-1$

        setColor(gc);
    }

    // Specify the color as an integer value plus the alpha value.
    public MColor(int hex, int Alpha) {
        asInt = hex;
        alpha = Alpha;

        String hexStr = intToHexStr(asInt);
        r = (int) Integer.parseInt("" + hexStr.charAt(0) + hexStr.charAt(1), 16); //$NON-NLS-1$
        g = (int) Integer.parseInt("" + hexStr.charAt(2) + hexStr.charAt(3), 16); //$NON-NLS-1$
        b = (int) Integer.parseInt("" + hexStr.charAt(4) + hexStr.charAt(5), 16); //$NON-NLS-1$

    }

    // Specify the color as an integer value plus the alpha valu, plus instant setting it.
    public MColor(int hex, int Alpha, GC gc) {
        asInt = hex;
        alpha = Alpha;

        String hexStr = intToHexStr(asInt);
        r = (int) Integer.parseInt("" + hexStr.charAt(0) + hexStr.charAt(1), 16); //$NON-NLS-1$
        g = (int) Integer.parseInt("" + hexStr.charAt(2) + hexStr.charAt(3), 16); //$NON-NLS-1$
        b = (int) Integer.parseInt("" + hexStr.charAt(4) + hexStr.charAt(5), 16); //$NON-NLS-1$

        setColor(gc);
    }

    public void setAlpha(int Alpha) {
        alpha = Alpha;
    }

    public void setAlpha(int Alpha, GC gc, boolean front) {
        alpha = Alpha;
        if (front)
            setColor(gc);
        else
            setBGColor(gc);
    }

    public int getRed() {
        return r;
    }

    public int getGreen() {
        return g;
    }

    public int getBlue() {
        return b;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getIntValue() {
        return asInt;
    }

    public void setColor(GC targetGC) {
        targetGC.setForeground(new Color(Display.getDefault(), r, g, b));
        targetGC.setAlpha(alpha);
    }

    public void setBGColor(GC targetGC) {
        targetGC.setBackground(new Color(Display.getDefault(), r, g, b));
        targetGC.setAlpha(alpha);
    }

}
