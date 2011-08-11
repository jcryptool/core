package org.jcryptool.crypto.classic.substitution.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.substitution.ui.messages"; //$NON-NLS-1$
	public static String SubstitutionWizard_substitution;
	public static String SubstitutionWizardPage_enterkey1;
	public static String SubstitutionWizardPage_substitution;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
