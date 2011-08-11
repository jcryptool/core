package org.jcryptool.crypto.classic.autovigenere.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.autovigenere.ui.messages"; //$NON-NLS-1$
	public static String AutoVigenereWizard_title;
	public static String AutoVigenereWizardPage_autokeyvigenere;
	public static String AutoVigenereWizardPage_orders;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
