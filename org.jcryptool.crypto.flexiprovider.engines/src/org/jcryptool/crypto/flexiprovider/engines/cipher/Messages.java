package org.jcryptool.crypto.flexiprovider.engines.cipher;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME =
            "org.jcryptool.crypto.flexiprovider.engines.cipher.messages"; //$NON-NLS-1$
    public static String AsymmetricBlockCipherEngine_1;
    public static String AsymmetricHybridCipherEngine_1;
    public static String BlockCipherEngine_5;
    public static String CipherEngine_2;
    public static String ExAccessKeystorePassword;
    public static String ExBadPadding;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
