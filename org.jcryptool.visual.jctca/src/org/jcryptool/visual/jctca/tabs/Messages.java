package org.jcryptool.visual.jctca.tabs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.jctca.tabs.messages"; //$NON-NLS-1$
	public static String CertificationTab_headline;
	public static String RegistrationTab_headline;
	public static String SecondUserTab_headline;
	public static String UserTab_create_csr_btn;
	public static String UserTab_headline;
	public static String UserTab_manage_cert_btn;
	public static String UserTab_sign_data_btn;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
