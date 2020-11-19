// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core;

import org.eclipse.osgi.util.NLS;

/**
 * Externalized strings for the <code>org.jcryptool.core</code> package.
 * 
 * @author Dominik Schadow *
 * @version 0.5.0
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.core.messages"; //$NON-NLS-1$

    public static String ApplicationActionBarAdvisor_0;

    public static String ApplicationActionBarAdvisor_1;

    public static String ApplicationActionBarAdvisor_2;

    public static String ApplicationActionBarAdvisor_5;

	public static String ApplicationActionBarAdvisor_Keystore;

    public static String ApplicationWorkbenchAdvisor_4;

    public static String applicationActionBarAdvisor_Menu_Algorithms;
    public static String applicationActionBarAdvisor_Menu_Algorithms_Asymmetric;
    public static String applicationActionBarAdvisor_Menu_Algorithms_Classic;
    public static String applicationActionBarAdvisor_Menu_Algorithms_Hash;
    public static String applicationActionBarAdvisor_Menu_Algorithms_Hybrid;
    public static String applicationActionBarAdvisor_Menu_Algorithms_Misc;
    public static String applicationActionBarAdvisor_Menu_Algorithms_Symmetric;
    public static String applicationActionBarAdvisor_Menu_Algorithms_Mac;
    public static String applicationActionBarAdvisor_Menu_Edit;
    public static String applicationActionBarAdvisor_Menu_File;
    public static String applicationActionBarAdvisor_Menu_New_File;
    public static String applicationActionBarAdvisor_Menu_Open_Perspective;
    public static String applicationActionBarAdvisor_Menu_Show_View;
    public static String applicationActionBarAdvisor_Menu_Window;
    public static String applicationActionBarAdvisor_Menu_Algorithms_Signature;
    public static String applicationActionBarAdvisor_Menu_Algorithms_PRNG;
    public static String applicationActionBarAdvisor_Menu_Algorithms_XML_Security;
    public static String applicationActionBarAdvisor_algorithmPerspective;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
