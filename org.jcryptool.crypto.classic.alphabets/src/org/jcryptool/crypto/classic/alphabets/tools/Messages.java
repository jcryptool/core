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
package org.jcryptool.crypto.classic.alphabets.tools;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.alphabets.tools.messages"; //$NON-NLS-1$
	public static String AlphabetStore_alpha_adfgvx_long;
	public static String AlphabetStore_alpha_adfgvx_short;
	public static String AlphabetStore_alpha_ascii_long;
	public static String AlphabetStore_alpha_ascii_short;
	public static String AlphabetStore_alpha_lowlatin_long;
	public static String AlphabetStore_alpha_lowlatin_short;
	public static String AlphabetStore_alpha_playfair_long;
	public static String AlphabetStore_alpha_playfair_short;
	public static String AlphabetStore_alpha_uplatin_long;
	public static String AlphabetStore_alpha_uplatin_short;
	public static String AlphabetStore_alpha_uplolatin_long;
	public static String AlphabetStore_alpha_uplolatin_short;
	public static String AlphabetStore_alpha_xor32_long;
	public static String AlphabetStore_alpha_xor32_short;
	public static String AlphabetStore_alpha_xor64_long;
	public static String AlphabetStore_alpha_xor64_short;
	public static String AlphabetStore_extrachars;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
