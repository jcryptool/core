package org.jcryptool.crypto.classic.xor.algorithm;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.xor.algorithm.messages"; //$NON-NLS-1$
	public static String ClassicAlgorithmCmd_specifyKeyMsg;
	public static String XorAlgorithmAction_0;
	public static String XorAlgorithmAction_1;
	public static String XorAlgorithmAction_2;
	public static String XorCmd_keyDetailsFilepath;
	public static String XorCmd_keyDetailsString;
	public static String XorCmd_onlyOneKeyMsg;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
