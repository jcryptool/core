// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crtverification.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.visual.crtverification.views.messages";

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
    public static String crtVerification_status_flushCerts;
    public static String crtVerification_status_UserCertLoaded;
    public static String crtVerification_status_CaLoaded;
    public static String crtVerification_status_RootCaLoaded;
    public static String CrtVerViewComposite_title;
    public static String CrtVerViewComposite_description;
    public static String CrtVerViewComposite_notValidBefore;
    public static String CrtVerViewComposite_notValidAfter;
    public static String CrtVerViewComposite_loadRootCa;
    public static String CrtVerViewComposite_loadCa;
    public static String CrtVerViewComposite_loadUserCert;
    public static String CrtVerViewComposite_UserCertificate;
    public static String CrtVerViewComposite_Ca;
    public static String CrtVerViewComposite_RootCa;
    public static String CrtVerViewComposite_signatureDate;
    public static String CrtVerViewComposite_verificationDate;
    public static String CrtVerViewComposite_details;
    public static String CrtVerViewComposite_validFrom;
    public static String CrtVerViewComposite_validThru;
    public static String CrtVerViewComposite_rootCaFromDay;
    public static String CrtVerViewComposite_rootCaThruDay;
    public static String CrtVerViewComposite_caFromDay;
    public static String CrtVerViewComposite_caThruDay;
    public static String CrtVerViewComposite_userCertificateFromDay;
    public static String CrtVerViewComposite_userCertificateThruDay;
    public static String CrtVerViewComposite_signatureDateDay;
    public static String CrtVerViewComposite_verificationDateDay;
    public static String CrtVerViewComposite_shellModel;
    public static String CrtVerViewComposite_modifiedshellModel;
    public static String CrtVerViewComposite_chainModel;
    public static String CrtVerViewComposite_reset;
    public static String CrtVerViewComposite_validate;
    public static String CrtVerViewComposite_pki_plugin;
    public static String CrtVerViewComposite_signatureVerification;
    public static String CrtVerViewComposite_validateSuccessful;
    public static String CrtVerViewComposite_validateUnSuccessful;
    public static String ChooseCertPage_description;
    public static String CrtVerViewComposite_lblLog_text;
    public static String CrtVerViewComposite_dateSet;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
