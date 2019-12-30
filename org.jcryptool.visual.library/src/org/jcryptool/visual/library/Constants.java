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
package org.jcryptool.visual.library;

import java.math.BigInteger;

public class Constants {
    public static final int HORIZONTAL_SPACING = 50;

    public static final int MARGIN_WIDTH = 100;

    /** vertical spacing between the big buttons. */
    public static final int BIGBUTTONVERTICALSPACE = 45;

    /** height of the big buttons */
    public static final int BIGBUTTONHEIGHT = 60;

    /** width of the big buttons */
    public static final int BIGBUTTONWIDTH = 150;

    /** Basis (16) for Hex Strings. */
    public static final int HEXBASE = 16;

    /** constant -1. */
    public static final BigInteger MINUS_ONE = new BigInteger("-1"); //$NON-NLS-1$

    /** constant 2. */
    public static final BigInteger TWO = new BigInteger("2"); //$NON-NLS-1$

    /** constant 256. */
    public static final BigInteger TWOFIVESIX = new BigInteger("256"); //$NON-NLS-1$
}
