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
package org.jcryptool.crypto.modern.stream.arc4.ui;

import org.eclipse.osgi.util.NLS;

/**
 * Class for Messages.
 *
 * For what the Strings mean look into the *.properties
 * <p>
 * Javadoc would have made the code unreadable.
 *
 * @author David
 *
 */
public class Messages {

    private static final String BUNDLE_NAME = "org.jcryptool.crypto.modern.stream.arc4.ui.messages"; //$NON-NLS-1$

    public static String Arc4_name;
    public static String Arc4Page_message;
    public static String Arc4Page_key;
    public static String Arc4Page_hex;
    public static String Arc4Page_bin;
    public static String Arc4Page_only_hex;
    public static String Arc4Page_enough;
    public static String Arc4Page_only_bin;
    public static String Arc4Page_max;
    public static String Arc4Page_crypt;
    public static String Arc4Page_encrypt;
    public static String Arc4Page_decrypt;
    public static String Arc4Page_algo;
    public static String Arc4Page_arc4;
    public static String Arc4Page_spritz;
    public static String Arc4Page_paste_change;
    public static String Arc4Page_generate_null;
    public static String Arc4Page_generate_rand;
    public static String Arc4Page_generate_free;
    public static String Arc4Page_rand_key;
    public static String Arc4Page_zero_key;
    public static String Arc4Page_amount_hex;
    public static String Arc4Page_amount_bin;
    public static String Arc4Page_max2_bin;
    public static String Arc4Page_max2_hex;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    /**
     * empty constructor.
     */
    private Messages() {
    }

}
