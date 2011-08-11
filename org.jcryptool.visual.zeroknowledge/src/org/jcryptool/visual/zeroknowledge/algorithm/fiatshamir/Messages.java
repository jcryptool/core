package org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME =
            "org.jcryptool.visual.zeroknowledge.algorithm.fiatshamir.messages"; //$NON-NLS-1$
    public static String FS_Alice_0;
    public static String FS_Bob_0;
    public static String FS_Carol_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
