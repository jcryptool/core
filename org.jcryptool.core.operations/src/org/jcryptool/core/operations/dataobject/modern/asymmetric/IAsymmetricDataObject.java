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

import org.jcryptool.core.operations.dataobject.modern.IModernDataObject;

/**
 * Provides a top level frame for the data objects used for asymmetric algorithms.
 * 
 * @author t-kern
 * 
 */
public interface IAsymmetricDataObject extends IModernDataObject {

    /**
     * Sets the key.
     * 
     * @param key The key
     */
    public void setAsymmetricKey(Key key);

    /**
     * Returns the key.
     * 
     * @return The key
     */
    public Key getAsymmetricKey();

}
