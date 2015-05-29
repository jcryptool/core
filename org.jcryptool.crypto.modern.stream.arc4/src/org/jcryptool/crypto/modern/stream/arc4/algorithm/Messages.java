package org.jcryptool.crypto.modern.stream.arc4.algorithm;

import org.eclipse.osgi.util.NLS;

/**
 * Message class not used but i leave it because maybe s1 else will need it in the near future. ^^
 * ;)
 *
 * @author David
 *
 */
public class Messages {

    /**
     * Bundle name.
     */
    private static final String BUNDLE_NAME = "org.jcryptool.crypto.modern.stream.arc4.algorithm.messages"; //$NON-NLS-1$
    // public static String ;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    /**
     * Constructor.
     */
    private Messages() {
    }

}
