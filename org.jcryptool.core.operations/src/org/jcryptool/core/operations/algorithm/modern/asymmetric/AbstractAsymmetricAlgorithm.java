// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm.modern.asymmetric;

import org.jcryptool.core.operations.algorithm.modern.AbstractModernAlgorithm;
import org.jcryptool.core.operations.dataobject.modern.IModernDataObject;
import org.jcryptool.core.operations.dataobject.modern.asymmetric.AsymmetricDataObject;

/**
 * Abstract superclass for asymmetric ciphers.
 * 
 * @author t-kern
 * 
 */
public abstract class AbstractAsymmetricAlgorithm extends AbstractModernAlgorithm {

    /** The data object */
    protected AsymmetricDataObject dataObject;

    /**
     * @see org.jcryptool.core.operations.algorithm.AbstractAlgorithm#getDataObject()
     */
    @Override
    public IModernDataObject getDataObject() {
        return dataObject;
    }

    /**
     * Initializies the algorithm.
     * 
     * @param input The input that will be processed
     * @param dataObject The data object containing the details
     */
    public void init(byte[] input, IModernDataObject dataObject) {
        this.dataObject = (AsymmetricDataObject) dataObject;
        dataObject.setInput(input);
    }

}
