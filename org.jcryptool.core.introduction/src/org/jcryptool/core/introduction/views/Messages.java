package org.jcryptool.core.introduction.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.core.introduction.views.messages"; //$NON-NLS-1$
	public static String AlgorithmInstruction_showAgain;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
