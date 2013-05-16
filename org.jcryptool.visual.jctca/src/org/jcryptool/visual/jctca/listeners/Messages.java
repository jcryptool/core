package org.jcryptool.visual.jctca.listeners;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.jctca.listeners.messages"; //$NON-NLS-1$
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}

	public static String CAListener_msgbox_title_cert_revoked;
	public static String CAListener_msgbox_text_cert_created;
	public static String CAListener_msgbox_text_cert_not_created;
	public static String CAListener_msgbox_text_cert_not_revoked;
	public static String CAListener_msgbox_text_cert_revoked;
	public static String CAListener_msgbox_title_cert_created;
	public static String CAListener_msgbox_title_cert_not_created;
	public static String CAListener_msgbox_title_cert_not_revoked;
	public static String CreateCertListener_msgbox_text_csr_to_ca;
	public static String CreateCertListener_msgbox_text_not_all_fields_set;
	public static String CreateCertListener_msgbox_title_csr_to_ca;
	public static String CreateCertListener_msgbox_title_not_all_fields_set;
	public static String CSRListener_msgbox_text_csr_rejected;
	public static String CSRListener_msgbox_text_csr_to_ca_sent;
	public static String CSRListener_msgbox_text_fake_csr_rejected;
	public static String CSRListener_msgbox_text_fake_csr_to_ca_sent;
	public static String CSRListener_msgbox_title_csr_rejected;
	public static String CSRListener_msgbox_title_csr_to_ca_sent;
	public static String CSRListener_msgbox_title_fake_csr_rejected;
	public static String CSRListener_msgbox_title_fake_csr_sent_to_ca;
	public static String PluginBtnListener_archpic_check_explain;
	public static String PluginBtnListener_archpic_create_explain;
	public static String PluginBtnListener_archpic_revoke_explain;
	public static String PluginBtnListener_visual_intro_text;
	public static String TabItemListener_tab_ca_explain;
	public static String TabItemListener_tab_ra_explain;
	public static String TabItemListener_tab_secuser_explain;
	public static String TabItemListener_tab_user_explain;
	public static String UserShowCertsListener_btn_revoke_cert;
	public static String UserShowCertsListener_btn_revoke_cert_was_revoked;
	public static String UserShowCertsListener_not_part_of_cert;
}
