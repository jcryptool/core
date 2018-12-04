//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sphincs.files;

public class MathUtils {

    /**
     * 
     * @param number Number of which ld is calculated
     * @return ld(number)
     */
    public static int log2nlz(int number) {
        if (number == 0)
            return 0; // or throw exception
        return 31 - Integer.numberOfLeadingZeros(number);
    }
}
