package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.jctca.UserViews.messages"; //$NON-NLS-1$

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}

	public static String CreateCert_btn_create_keypair;
	public static String CreateCert_btn_select_file;
	public static String CreateCert_btn_select_pubkey;
	public static String CreateCert_btn_send_csr_to_ra;
	public static String CreateCert_explain_text;
	public static String CreateCert_headline;
	public static String CreateCert_lbl_city;
	public static String CreateCert_lbl_country;
	public static String CreateCert_lbl_first_name;
	public static String CreateCert_lbl_idproof;
	public static String CreateCert_lbl_last_name;
	public static String CreateCert_lbl_mail;
	public static String CreateCert_lbl_pubkey;
	public static String CreateCert_lbl_street;
	public static String CreateCert_lbl_zip;
	public static String CreateCert_sample_city;
	public static String CreateCert_sample_country;
	public static String CreateCert_sample_first_name;
	public static String CreateCert_sample_last_name;
	public static String CreateCert_sample_mail;
	public static String CreateCert_sample_street;
	public static String CreateCert_sample_zip;
	public static String ShowCert_btn_revoke_cert;
	public static String ShowCert_explain_text;
	public static String ShowCert_headline;
	public static String ShowCert_lbl_expires_on;
	public static String ShowCert_lbl_issued_by;
	public static String ShowCert_lbl_issued_on;
	public static String ShowCert_lbl_issued_to;
	public static String ShowCert_lbl_issuer_cn;
	public static String ShowCert_lbl_issuer_o;
	public static String ShowCert_lbl_issuer_ou;
	public static String ShowCert_lbl_subject_c;
	public static String ShowCert_lbl_subject_cn;
	public static String ShowCert_lbl_subject_e;
	public static String ShowCert_lbl_subject_l;
	public static String ShowCert_lbl_subject_o;
	public static String ShowCert_lbl_subject_ou;
	public static String ShowCert_lbl_validity_perios;
	public static String SignCert_btn_cancel_file_selection;
	public static String SignCert_btn_chose_file;
	public static String SignCert_btn_sign_with_key;
	public static String SignCert_checkbox_show_sigvis;
	public static String SignCert_explain_text;
	public static String SignCert_file_or_text_headline;
	public static String SignCert_headline;
	public static String SignCert_textbox_sample_text;
}
