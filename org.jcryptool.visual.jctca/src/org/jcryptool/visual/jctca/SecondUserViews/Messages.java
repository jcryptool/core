package org.jcryptool.visual.jctca.SecondUserViews;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.jctca.SecondUserViews.messages"; //$NON-NLS-1$
	public static String ShowSigData_btn_check_sig;
	public static String ShowSigData_btn_delete_entry;
	public static String ShowSigData_checkbox_check_revoke_status;
	public static String ShowSigData_show_request_headline;
	public static String ShowSigData_signed_files_tree_headline;
	public static String ShowSigData_signed_texts_tree_headline;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
