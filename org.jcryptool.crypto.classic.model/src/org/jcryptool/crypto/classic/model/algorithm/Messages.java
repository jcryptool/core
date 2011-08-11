//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.model.algorithm;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.model.algorithm.messages"; //$NON-NLS-1$
	public static String AbstractClassicCryptoPage_keyBalloonTipWithInstruction;
	public static String AbstractClassicCryptoPage_reason_notinalphabet;
	public static String AbstractClassicCryptoPage_keyToolTip;
	public static String ClassicAlgorithmCmd_alphabetnotfoundMsg;
	public static String ClassicAlgorithmCmd_alphabetnotsupportedMsg;
	public static String ClassicAlgorithmCmd_alphabetsDetails;
	public static String ClassicAlgorithmCmd_decryptMode;
	public static String ClassicAlgorithmCmd_encryptMode;
	public static String ClassicAlgorithmCmd_error;
	public static String ClassicAlgorithmCmd_filteroption;
	public static String ClassicAlgorithmCmd_ignoredargs;
	public static String ClassicAlgorithmCmd_keyDetails;
	public static String ClassicAlgorithmCmd_notwellformedMsg;
	public static String ClassicAlgorithmCmd_specifyKeyMsg;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}