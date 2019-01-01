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
    /** Colors for backgrounds. */
    public static final Color WHITE, GREEN, RED, LIGHTGRAY, GRAY, BLACK, YELLOW;

    static {
        WHITE = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
        GRAY = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
        RED = Display.getDefault().getSystemColor(SWT.COLOR_RED);
        GREEN = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
        LIGHTGRAY = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
        BLACK = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
        YELLOW = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
    }
}
