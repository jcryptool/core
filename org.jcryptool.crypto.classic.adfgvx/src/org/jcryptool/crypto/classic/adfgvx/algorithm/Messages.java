// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.adfgvx.algorithm;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.adfgvx.algorithm.messages"; //$NON-NLS-1$
	public static String AdfgvxAlgorithmAction_0;
	public static String AdfgvxAlgorithmAction_1;
	public static String AdfgvxAlgorithmAction_2;
	public static String AdfgvxCmd_specifySubstKey;
	public static String AdfgvxCmd_specifyTranspKey;
	public static String AdfgvxCmd_substKeyDescription;
	public static String AdfgvxCmd_transpKeyDescription;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
