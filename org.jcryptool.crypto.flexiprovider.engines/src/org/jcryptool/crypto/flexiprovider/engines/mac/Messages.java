package org.jcryptool.crypto.flexiprovider.engines.mac;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME =
            "org.jcryptool.crypto.flexiprovider.engines.mac.messages"; //$NON-NLS-1$
    public static String MacEngine_2;
    public static String ExAccessKeystorePassword;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
