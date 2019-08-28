package org.jcryptool.visual.errorcorrectingcodes.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.visual.errorcorrectingcodes.ui.messages"; //$NON-NLS-1$
    public static String EccMainView_btnNextStep;
    public static String EccMainView_btnPrev;
    public static String EccMainView_btnReset;
    public static String EccMainView_grpErrorCode;
    public static String EccMainView_grpReceiver;
    public static String EccMainView_grpSenderStep;
    public static String EccMainView_grpTextInfo;
    public static String EccMainView_lblHeader;
    public static String EccMainView_lblTextDecoded;
    public static String EccMainView_lblTextEncode;
    public static String EccMainView_lblTextOriginal;
    public static String EccMainView_textInfo_step1;
    public static String EccMainView_textInfo_step2;
    public static String EccMainView_textInfo_step3;
    public static String EccMainView_textInfo_step4;
    public static String EccMainView_textInfo_step5;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
