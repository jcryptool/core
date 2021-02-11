package org.jcryptool.core.util.units;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.jcryptool.core.util.units.messages"; //$NON-NLS-1$
    public static String unitByte;
    public static String LocaleThousandSeparator;
    public static String LocaleCommaSeparator;

    // Base 10 byte count
    public static String unitKiloByte;
    public static String unitMegaByte;
    public static String unitGigaByte;
    public static String unitTeraByte;
    public static String unitPetaByte;
    public static String unitExaByte;
    public static String unitZettaByte;
    public static String unitYottaByte;

    // Base 2 IEC byte count
    public static String unitKibiByte;
    public static String unitMebiByte;
    public static String unitGibiByte;
    public static String unitTebiByte;
    public static String unitPebiByte;
    public static String unitExbiByte;
    public static String unitZebibyte;
    public static String unitYobiByte;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }

}
