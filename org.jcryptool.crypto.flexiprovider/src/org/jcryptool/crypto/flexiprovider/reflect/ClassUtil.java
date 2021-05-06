// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.reflect;

public class ClassUtil {
    public static Class<?> getClass(final String type) {
        if (type.equals("byte[]")) { //$NON-NLS-1$
            return byte[].class;
        } else if (type.equals("int")) { //$NON-NLS-1$
            return int.class;
        } else {
            try {
                return Class.forName(type);
            } catch (final ClassNotFoundException e) {
            }
        }
        return null;
    }

}
