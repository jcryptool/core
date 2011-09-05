// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer5;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.analysis.kegver"
        + ".layer5.messages"; //$NON-NLS-1$

    public static String X;
    public static String KegverContent_grp1;
    public static String KegverContent_grp1_t;
    public static String KegverContent_btn1;
    public static String KegverContent_btn1_tt;
	public static String KegverContent_btn2;
	public static String KegverContent_btn2_tt;
	public static String KegverContent_btn3;
	public static String KegverContent_btn3_tt;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
