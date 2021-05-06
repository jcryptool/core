// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.dataobject.modern.asymmetric;

import java.security.Key;

import org.jcryptool.core.operations.dataobject.modern.ModernDataObject;

/**
 * Data object for asymmetric ciphers.
 * 
 * @author t-kern
 * 
 */
public class AsymmetricDataObject extends ModernDataObject implements IAsymmetricDataObject {

    /** The asymmetric key */
    private Key key;

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.asymmetric.IAsymmetricDataObject#setAsymmetricKey(java.security.Key)
     */
    public void setAsymmetricKey(Key key) {
        this.key = key;
    }

    /**
     * @see org.jcryptool.core.operations.dataobject.modern.asymmetric.IAsymmetricDataObject#getAsymmetricKey()
     */
    public Key getAsymmetricKey() {
        return key;
    }

}
