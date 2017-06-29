// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crtverification.verification;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.visual.crtverification.verification.messages";


    public static String CrtVerifciation_sigDateAfterVerDate;
    public static String CrtVericiation_verDateNotWithinClient;
    public static String CrtVericiation_sigDateNotWithinClient;
    public static String CrtVericiation_clientNotBeforeAfterSigDate;
    public static String CrtVericiation_clientNotAfterBeforeSigDate;
    public static String CrtVericiation_caNotBeforeAfterClientNotBefore;
    public static String CrtVericiation_caNotAfterBeforeClientNotAfter;
    public static String CrtVericiation_rootNotBeforeAfterCaNotBefore;
    public static String CrtVericiation_rootNotAfterBeforeCANotAfter;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
