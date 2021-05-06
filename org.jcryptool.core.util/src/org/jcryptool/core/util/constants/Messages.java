// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.constants;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.core.util.constants.messages"; //$NON-NLS-1$
    public static String IConstants_0;
    public static String IConstants_1;
    public static String IConstants_2;
    public static String IConstants_3;
    public static String IConstants_4;
    public static String IConstants_5;
    public static String IConstants_6;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
