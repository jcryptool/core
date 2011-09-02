package org.jcryptool.core.logging.dialogs;

import org.eclipse.osgi.util.NLS;

public class JFaceResources extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.core.logging.dialogs.messages"; //$NON-NLS-1$

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, JFaceResources.class);
    }

    private JFaceResources() {
    }
}
