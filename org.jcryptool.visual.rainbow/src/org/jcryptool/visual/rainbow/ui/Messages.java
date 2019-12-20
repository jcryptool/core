package org.jcryptool.visual.rainbow.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.visual.rainbow.ui.messages"; //$NON-NLS-1$
    public static String RainbowSignatureView_btnSign;
    public static String RainbowSignatureView_btnVerify;
    public static String RainbowSignatureView_grpInfoBox;
    public static String RainbowSignatureView_grpInput;
    public static String RainbowSignatureView_grpOutput;
    public static String RainbowSignatureView_grpRainbowLES;
    public static String RainbowSignatureView_grpViParams;
    public static String RainbowSignatureView_lblHeader;
    public static String RainbowSignatureView_textInfoHead;
    public static String RainbowSignatureView_textMessage;
    public static String RainbowSignatureView_varCoeffQ;
    public static String RainbowSignatureView_varCoeffScalar;
    public static String RainbowSignatureView_varCoeffSinglar;
    public static String RainbowSignatureView_varDim;
    public static String RainbowSignatureView_varDocLength;
    public static String RainbowSignatureView_varLayers;
    public static String RainbowSignatureView_varPrivKey;
    public static String RainbowSignatureView_varPubkey;
    public static String RainbowSignatureView_varVi;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
