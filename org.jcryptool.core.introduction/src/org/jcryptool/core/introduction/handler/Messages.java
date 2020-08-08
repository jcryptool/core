package org.jcryptool.core.introduction.handler;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.core.introduction.handler.messages"; //$NON-NLS-1$
	public static String AutoslideHandler_disable;
	public static String AutoslideHandler_enable;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
