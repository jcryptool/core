package org.jcryptool.visual.jctca;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.jctca.messages"; //$NON-NLS-1$
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}

	public static String JCTCA_Visual_archpic_create_text;
	public static String JCTCA_Visual_btn_continue_to_plugin;
	public static String JCTCA_Visual_btn_show_archpic_check;
	public static String JCTCA_Visual_btn_show_archpic_create;
	public static String JCTCA_Visual_btn_show_archpic_revoke;
	public static String JCTCA_Visual_grp_explain_headline;
	public static String JCTCA_Visual_Plugin_Headline;
	public static String Util_CSR_Tree_Head;
	public static String Util_RR_Tree_Head;
}
