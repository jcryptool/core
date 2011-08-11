package org.jcryptool.crypto.classic.doppelkasten.algorithm;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.doppelkasten.algorithm.messages"; //$NON-NLS-1$
	public static String DoppelkastenAlgorithmAction_0;
	public static String DoppelkastenAlgorithmAction_1;
	public static String DoppelkastenAlgorithmAction_2;
	public static String DoppelkastenCmd_firstKeyDetails;
	public static String DoppelkastenCmd_secondKeyDetails;
	public static String DoppelkastenCmd_specifyKeys;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
