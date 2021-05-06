// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for all date conversion / handling used in JCrypTool.
 * 
 * @author Anatoli Barski
 * @version 1.0.0
 */
public class CalendarService {

    /**
     * @return date, e.g.: 23. February 1983
     */
    public static String getDate(long milliseconds) {
        Date d = new Date(milliseconds);
        return new SimpleDateFormat("dd. MMMM yyyy").format(d); //$NON-NLS-1$
    }

    /**
     * @return time, e.g.: 16:17:33
     */
    public static String getTime(long milliseconds) {
        Date d = new Date(milliseconds);
        return new SimpleDateFormat("HH:mm:ss").format(d); //$NON-NLS-1$
    }

    /**
     * @return date and time, e.g.: 23. February 1983 16:17:33
     */
    public static String getDateAndTime(long milliseconds) {
        return CalendarService.getDate(milliseconds) + " " + CalendarService.getTime(milliseconds); //$NON-NLS-1$
    }

}
