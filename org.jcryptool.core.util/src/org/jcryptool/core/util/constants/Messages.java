package org.jcryptool.core.util.constants;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.core.util.constants.messages"; //$NON-NLS-1$
	public static String IConstants_0;
	public static String IConstants_1;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
