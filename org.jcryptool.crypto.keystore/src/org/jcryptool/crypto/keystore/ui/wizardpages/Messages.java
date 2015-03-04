package org.jcryptool.crypto.keystore.ui.wizardpages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.keystore.ui.wizardpages.messages"; //$NON-NLS-1$
	public static String BackupRestorePage_1;
	public static String BackupRestorePage_2;
	public static String BackupRestorePage_3;
	public static String BackupRestorePage_4;
	public static String BackupRestorePage_5;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
