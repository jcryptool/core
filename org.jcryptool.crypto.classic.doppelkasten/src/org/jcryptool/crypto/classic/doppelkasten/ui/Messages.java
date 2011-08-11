package org.jcryptool.crypto.classic.doppelkasten.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.doppelkasten.ui.messages"; //$NON-NLS-1$
	public static String DoppelkastenWizard_classic;
	public static String DoppelkastenWizardPage_doublebox;
	public static String DoppelkastenWizardPage_enterfirst;
	public static String DoppelkastenWizardPage_enterkey;
	public static String DoppelkastenWizardPage_entersecond;
	public static String DoppelkastenWizardPage_firstkey;
	public static String DoppelkastenWizardPage_keys;
	public static String DoppelkastenWizardPage_secondkey;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
