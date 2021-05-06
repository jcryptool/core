// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.fonts;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * Utility class for all font services used in JCrypTool. Plug-ins should use this class for all font purposes rather
 * than determining a font themselves.
 * 
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class FontService {
    private static final String SYSTEM_FONT_NAME = Display.getDefault().getSystemFont().getFontData()[0].getName();
    private static Font headerFont = null;
    private static Font tinyFont = null;
    private static Font smallFont = null;
    private static Font normalFont = null;
    private static Font normalMonospacedFont = null;
    private static Font largeFont = null;
    private static Font hugeFont = null;
    private static Font tinyBoldFont = null;
    private static Font smallBoldFont = null;
    private static Font normalBoldFont = null;
    private static Font largeBoldFont = null;
    private static Font hugeBoldFont = null;

    /**
     * Returns the default system font name.
     * 
     * @return The default system font name
     */
    public static String getSystemFontName() {
        return SYSTEM_FONT_NAME;
    }

    /**
     * Returns the default header font, used e.g. for the headline of a view like analysis or visualization. Font name
     * is the system default, font size is 12, font weight is bold.
     * 
     * <b>Do not dispose this font after usage!</b>
     * 
     * @return The header font
     */
    public static Font getHeaderFont() {
        if (headerFont == null || headerFont.isDisposed()) {
            headerFont = new Font(Display.getDefault(), SYSTEM_FONT_NAME, 12, SWT.BOLD);
        }

        return headerFont;
    }

    /**
     * Returns the default huge font, used e.g. for normal text in a view like analysis or visualization. Font name is
     * the system default, font size is 20, font weight is normal.
     * 
     * <b>Do not dispose this font after usage!</b>
     * 
     * @return The large default font
     */
    public static Font getHugeFont() {
        if (hugeFont == null || hugeFont.isDisposed()) {
            hugeFont = new Font(Display.getDefault(), SYSTEM_FONT_NAME, 20, SWT.NORMAL);
        }

        return hugeFont;
    }

    /**
     * Returns the default large font, used e.g. for normal text in a view like analysis or visualization. Font name is
     * the system default, font size is 12, font weight is normal.
     * 
     * <b>Do not dispose this font after usage!</b>
     * 
     * @return The large default font
     */
    public static Font getLargeFont() {
        if (largeFont == null || largeFont.isDisposed()) {
            largeFont = new Font(Display.getDefault(), SYSTEM_FONT_NAME, 12, SWT.NORMAL);
        }

        return largeFont;
    }

    /**
     * Returns the default normal font, used e.g. for normal text in a view like analysis or visualization. Font name is
     * the system default, font size is 10, font weight is normal.
     * 
     * <b>Do not dispose this font after usage!</b>
     * 
     * @return The normal default font
     */
    public static Font getNormalFont() {
        return Display.getCurrent().getSystemFont();
    }

    /**
     * Returns the default small font, used e.g. for small text in a view like analysis or visualization. Font name is
     * the system default, font size is 8, font weight is normal.
     * 
     * <b>Do not dispose this font after usage!</b>
     * 
     * @return The small default font
     */
    public static Font getSmallFont() {
        if (smallFont == null || smallFont.isDisposed()) {
            smallFont = new Font(Display.getDefault(), SYSTEM_FONT_NAME, 8, SWT.NORMAL);
        }

        return smallFont;
    }

    /**
     * Returns the default tiny font, used e.g. for small text in a view like analysis or visualization. Font name is
     * the system default, font size is 6, font weight is normal.
     * 
     * <b>Do not dispose this font after usage!</b>
     * 
     * @return The small default font
     */
    public static Font getTinyFont() {
        if (tinyFont == null || tinyFont.isDisposed()) {
            tinyFont = new Font(Display.getDefault(), SYSTEM_FONT_NAME, 6, SWT.NORMAL);
        }

        return tinyFont;
    }

    /**
     * Returns the default bold huge font, used e.g. for normal text in a view like analysis or visualization. Font name
     * is the system default, font size is 20, font weight is bold.
     * 
     * <b>Do not dispose this font after usage!</b>
     * 
     * @return The large default font
     */
    public static Font getHugeBoldFont() {
        if (hugeBoldFont == null || hugeBoldFont.isDisposed()) {
            hugeBoldFont = new Font(Display.getDefault(), SYSTEM_FONT_NAME, 20, SWT.BOLD);
        }

        return hugeBoldFont;
    }

    /**
     * Returns the default bold large font, used e.g. for normal text in a view like analysis or visualization. Font
     * name is the system default, font size is 12, font weight is bold.
     * 
     * <b>Do not dispose this font after usage!</b>
     * 
     * @return The large default font
     */
    public static Font getLargeBoldFont() {
        if (largeBoldFont == null || largeBoldFont.isDisposed()) {
            largeBoldFont = new Font(Display.getDefault(), SYSTEM_FONT_NAME, 12, SWT.BOLD);
        }

        return largeBoldFont;
    }

    /**
     * Returns the default bold normal font, used e.g. for normal text in a view like analysis or visualization. Font
     * name is the system default, font size is 10, font weight is bold.
     * 
     * <b>Do not dispose this font after usage!</b>
     * 
     * @return The normal default font
     */
    public static Font getNormalBoldFont() {
        Font systemFont = Display.getCurrent().getSystemFont();
        FontDescriptor systemFontDesriptor = FontDescriptor.createFrom(systemFont).setStyle(SWT.BOLD);
        Font boldFont = systemFontDesriptor.createFont(Display.getDefault());
        return boldFont;
    }

    /**
     * Returns the default bold small font, used e.g. for small text in a view like analysis or visualization. Font name
     * is the system default, font size is 8, font weight is bold.
     * 
     * <b>Do not dispose this font after usage!</b>
     * 
     * @return The small default font
     */
    public static Font getSmallBoldFont() {
        if (smallBoldFont == null || smallBoldFont.isDisposed()) {
            smallBoldFont = new Font(Display.getDefault(), SYSTEM_FONT_NAME, 8, SWT.BOLD);
        }

        return smallBoldFont;
    }

    /**
     * Returns the default bold tiny font, used e.g. for small text in a view like analysis or visualization. Font name
     * is the system default, font size is 6, font weight is bold.
     * 
     * <b>Do not dispose this font after usage!</b>
     * 
     * @return The small default font
     */
    public static Font getTinyBoldFont() {
        if (tinyBoldFont == null || tinyBoldFont.isDisposed()) {
            tinyBoldFont = new Font(Display.getDefault(), SYSTEM_FONT_NAME, 6, SWT.BOLD);
        }

        return tinyBoldFont;
    }
    
    
    /**
     * Returns a monospaced font, used e.g. for hex strings or similar texts which require
     * fonts with equal width. Font name is JFaceResources.TEXT_FONT (org.eclipse.jface.textfont).
     * Font size is 10, font weight is normal.
     * 
     * <b>Do not dispose this font after usage!</b>
     * 
     * @return A normal monospaced (fixed-width) font
     */
    public static Font getNormalMonospacedFont() {
        if (normalMonospacedFont == null || normalMonospacedFont.isDisposed()) {
        	normalMonospacedFont = JFaceResources.getFont(JFaceResources.TEXT_FONT);
        }

        return normalMonospacedFont;
    }
    

    /**
     * Disposes all fonts at the end of the plug-ins lifecycle. Plug-ins should never call this method.
     */
    public static void dispose() {
        if (headerFont != null && !headerFont.isDisposed()) {
            headerFont.dispose();
        }
        if (tinyFont != null && !tinyFont.isDisposed()) {
            tinyFont.dispose();
        }
        if (smallFont != null && !smallFont.isDisposed()) {
            smallFont.dispose();
        }
        if (normalFont != null && !normalFont.isDisposed()) {
            normalFont.dispose();
        }
        if (normalMonospacedFont != null && !normalMonospacedFont.isDisposed()) {
        	normalMonospacedFont.dispose();
        }
        if (largeFont != null && !largeFont.isDisposed()) {
            largeFont.dispose();
        }
        if (hugeFont != null && !hugeFont.isDisposed()) {
            hugeFont.dispose();
        }
        if (tinyBoldFont != null && !tinyBoldFont.isDisposed()) {
            tinyBoldFont.dispose();
        }
        if (smallBoldFont != null && !smallBoldFont.isDisposed()) {
            smallBoldFont.dispose();
        }
        if (normalBoldFont != null && !normalBoldFont.isDisposed()) {
            normalBoldFont.dispose();
        }
        if (largeBoldFont != null && !largeBoldFont.isDisposed()) {
            largeBoldFont.dispose();
        }
        if (hugeBoldFont != null && !hugeBoldFont.isDisposed()) {
            hugeBoldFont.dispose();
        }
    }
}
