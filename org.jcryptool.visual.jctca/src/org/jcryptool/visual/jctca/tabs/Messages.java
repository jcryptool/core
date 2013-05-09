package org.jcryptool.visual.jctca.tabs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.jctca.tabs.messages"; //$NON-NLS-1$
	public static String CertificationTab_headline;
	public static String RegistrationTab_headline;
	public static String SecondUserTab_headline;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
