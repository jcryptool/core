// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.util;

import java.util.List;

public class DividerGameUtil {
    private DividerGameUtil() {
    }

    public static String createStringFromIntArray(List<Integer> listOfNumbers) {
        String ret = ("[");
        if (listOfNumbers != null) {
            for (int i = 0; i < listOfNumbers.size(); i++) {
                if (i != listOfNumbers.size() - 1) {
                    ret += (listOfNumbers.get(i));
                    ret += ", ";
                } else {
                    ret += listOfNumbers.get(i);
                }
            }
            ret += "]";
        }
        return ret;
    }
}
