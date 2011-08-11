/**
 * 
 */
package org.jcryptool.crypto.flexiprovider.operations.ui.handlers;

import org.eclipse.osgi.util.NLS;

/**
 * @author Anatoli Barski
 *
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.crypto.flexiprovider.operations.ui.handlers.messages"; //$NON-NLS-1$
    public static String SelectKeyHandler_WrongKey;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
