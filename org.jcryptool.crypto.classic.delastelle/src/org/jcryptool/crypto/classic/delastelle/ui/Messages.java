package org.jcryptool.crypto.classic.delastelle.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.delastelle.ui.messages"; //$NON-NLS-1$
	public static String DelastelleWizardPage_delastelle;
	public static String DelastelleWizardPage_enterkey;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
