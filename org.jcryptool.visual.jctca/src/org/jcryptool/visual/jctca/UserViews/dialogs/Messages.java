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
package org.jcryptool.visual.jctca.UserViews.dialogs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.visual.jctca.UserViews.dialogs.messages"; //$NON-NLS-1$
    public static String RevokeCertDialog_btn_send_rr_to_ca;
    public static String RevokeCertDialog_explain_text_revoke_cert;
    public static String RevokeCertDialog_headline;
    public static String RevokeCertDialog_msgbox_text_rr_to_ca;
    public static String RevokeCertDialog_msgbox_title_rr_to_ca;
    public static String RevokeCertDialog_reason_privkey_compromised;
    public static String RevokeCertDialog_reason_privkey_lost;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
