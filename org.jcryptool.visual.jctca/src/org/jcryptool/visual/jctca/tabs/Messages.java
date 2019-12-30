//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca.tabs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.visual.jctca.tabs.messages"; //$NON-NLS-1$
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }

    public static String CertificationTab_tabitem_name;
    public static String RegistrationTab_tabitem_name;
    public static String SecondUserTab_tabitem_name;
    public static String UserTab_btn_get_new_cert;
    public static String UserTab_btn_manage_certs;
    public static String UserTab_btn_sign_text_or_file;
    public static String UserTab_PKI_processes;
    public static String UserTab_tabitem_name;
    public static String UserTab_User_processes;
}