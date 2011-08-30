package org.jcryptool.analysis.kegver.layer5;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.analysis.kegver"
        + ".layer5.messages"; //$NON-NLS-1$
    
    public static String X;
    public static String KegverContent_grp1;
    public static String KegverContent_grp1_t;
    public static String KegverContent_btn1;
    public static String KegverContent_btn1_tt;
	public static String KegverContent_btn2;
	public static String KegverContent_btn2_tt;
	public static String KegverContent_btn3;
	public static String KegverContent_btn3_tt;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
