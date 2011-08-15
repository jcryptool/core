package org.jcryptool.core.util.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.core.util.ui.messages"; //$NON-NLS-1$
	public static String HexTextbox_0;
	public static String HexTextbox_1;
	public static String PasswordPrompt_0;
	public static String PasswordPrompt_1;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
