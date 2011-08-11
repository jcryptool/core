package org.jcryptool.crypto.classic.playfair.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.playfair.ui.messages"; //$NON-NLS-1$
	public static String PlayfairWizard_classic;
	public static String PlayfairWizardPage_playfair;
	public static String PlayfairWizardPage_enterkey1;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
