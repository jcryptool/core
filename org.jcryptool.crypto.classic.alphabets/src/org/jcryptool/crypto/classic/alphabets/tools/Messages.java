package org.jcryptool.crypto.classic.alphabets.tools;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.alphabets.tools.messages"; //$NON-NLS-1$
	public static String AlphabetStore_extrachars;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
