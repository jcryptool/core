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
package org.jcryptool.actions.ui.preferences;

import org.eclipse.osgi.util.NLS;

/**
 * Externalized strings for the <code>org.jcryptool.actions.ui.preferences</code> package.
 * 
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.actions.ui.preferences.messages"; //$NON-NLS-1$
    public static String GeneralPage_0;
    public static String GeneralPage_1;
    public static String GeneralPage_2;
    public static String GeneralPage_3;
    public static String GeneralPage_4;
	public static String GeneralPage_5;
	public static String GeneralPage_6;
	public static String GeneralPage_7;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
