package org.jcryptool.visual.wots;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.wots.messages"; //$NON-NLS-1$
	public static String invalidChar_txt;
	public static String headline_txt;
	public static String header_txt;
	public static String message_txt;
	public static String defaultMessage_txt;
	public static String loadMessage_txt;
	public static String winPara_txt;
	public static String hashFunction_txt;
	public static String privateKey_txt;
	public static String publicKey_txt;
	public static String signature_txt;
	public static String btnGenKeys_txt;
	public static String btnGenSig_txt;
	public static String btnVerSig_txt;
	public static String btnReset_txt;
	public static String btnRestart_txt;
	public static String error_txt;
	public static String errorShort_txt;
	public static String showDetails_txt;
	public static String hideDetails_txt;
	public static String byte_txt;
	public static String hash_txt;
	public static String outWelcome_txt;
	public static String outGenKeys_txt;
	public static String outGenSig_txt;
	public static String outVerSig_txt;
	public static String outGenKeysPlus_txt;
	public static String outGenSigPlus_txt;
	public static String outVerSigPlus_txt;
	public static String fileType_txt;
	
	public static String WotsView_Key_Generation;
	public static String WotsView_Overview2;
	public static String WotsView_Signature_Generation;
	public static String WotsView_Signature_Verification;


	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
