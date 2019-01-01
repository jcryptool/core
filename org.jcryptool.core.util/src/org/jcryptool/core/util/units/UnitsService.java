// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.units;

/**
 * Utility class for all unit conversion / handling used in JCrypTool.
 * 
 * @author Anatoli Barski
 * @version 1.0.0
 */
public class UnitsService {

    /**
     * converts byte count to a human readable string Example output: SI BINARY
     * 
     * 0: 0 B 0 B 27: 27 B 27 B 999: 999 B 999 B 1000: 1.0 KB 1000 B 1023: 1.0 KB 1023 B 1024: 1.0 KB 1.0 KiB 1728: 1.7
     * KB 1.7 KiB 110592: 110.6 KB 108.0 KiB 7077888: 7.1 MB 6.8 MiB 452984832: 453.0 MB 432.0 MiB 28991029248: 29.0 GB
     * 27.0 GiB 1855425871872: 1.9 TB 1.7 TiB 9223372036854775807: 9.2 EB 8.0 EiB (Long.MAX_VALUE)
     * 
     * @return byte count including units as string
     */

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B"; //$NON-NLS-1$
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre); //$NON-NLS-1$
    }

}
