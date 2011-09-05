// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.transposition.algorithm;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.transposition.algorithm.messages"; //$NON-NLS-1$
	public static String TranspositionAlgorithmAction_0;
	public static String TranspositionAlgorithmAction_1;
	public static String TranspositionAlgorithmAction_2;
	public static String TranspositionAlgorithmCmd_appliesFor1st;
	public static String TranspositionAlgorithmCmd_appliesFor2nd;
	public static String TranspositionAlgorithmCmd_key2details;
	public static String TranspositionAlgorithmCmd_noneedfor2ndkeyorders;
	public static String TranspositionAlgorithmCmd_notValidOrderOption;
	public static String TranspositionAlgorithmCmd_readInOrderDetails;
	public static String TranspositionAlgorithmCmd_readOutOrderDetails;
	public static String TranspositionAlgorithmCmd_rowwise_sentence_end;
	public static String TranspositionAlgorithmCmd_columnwise_sentence_end;
	public static String TranspositionAlgorithmCmd_seeabove;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
