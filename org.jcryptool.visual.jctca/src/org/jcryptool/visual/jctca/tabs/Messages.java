package org.jcryptool.visual.jctca.tabs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.visual.jctca.tabs.messages"; //$NON-NLS-1$
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }

    public static String CertificationTab_tabitem_name;
    public static String RegistrationTab_tabitem_name;
    public static String SecondUserTab_tabitem_name;
    public static String UserTab_btn_get_new_cert;
    public static String UserTab_btn_manage_certs;
    public static String UserTab_btn_sign_text_or_file;
    public static String UserTab_tabitem_name;
}
