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
package org.jcryptool.games.numbershark.dialogs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.games.numbershark.dialogs.messages"; //$NON-NLS-1$
    public static String EndOfGameDialog_0;
    public static String EndOfGameDialog_1;
    public static String EndOfGameDialog_2;
    public static String EndOfGameDialog_3;
    public static String EndOfGameDialog_4;
    public static String NewGameDialog_0;
    public static String NewGameDialog_1;
    public static String NewGameDialog_2;
    public static String NewGameDialog_4;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
