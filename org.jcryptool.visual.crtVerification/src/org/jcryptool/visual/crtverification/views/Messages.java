package org.jcryptool.visual.crtverification.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.visual.crtVerification.views.messages";

    public static String crtVerification_status_chainFailChain;
    public static String crtVerification_status_chainFailModShell;
    public static String crtVerification_status_chainFailShell;
    public static String crtVerification_status_chainSucChain;
    public static String crtVerification_status_chainSucModShell;
    public static String crtVerification_status_chainSucShell;
    public static String crtVerification_status_dateFailChain;
    public static String crtVerification_status_dateFailModShell;
    public static String crtVerification_status_dateFailShell;
    public static String crtVerification_status_dateSucChain;
    public static String crtVerification_status_dateSucModShell;
    public static String crtVerification_status_dateSucShell;
    public static String crtVerification_status_missingRootCert;
    public static String crtVerification_status_missingClientCert;
    public static String crtVerification_status_missingCACert;
    public static String crtVerification_status_invalidDate;
    

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
