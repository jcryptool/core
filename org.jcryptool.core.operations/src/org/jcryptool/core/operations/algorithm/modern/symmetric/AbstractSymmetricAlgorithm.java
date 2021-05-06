// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm.modern.symmetric;

import org.jcryptool.core.operations.algorithm.modern.AbstractModernAlgorithm;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.modern.IModernDataObject;
import org.jcryptool.core.operations.dataobject.modern.symmetric.SymmetricDataObject;

/**
 * Abstract superclass for symmetric block ciphers.
 * 
 * @author t-kern
 * 
 */
public abstract class AbstractSymmetricAlgorithm extends AbstractModernAlgorithm {

    /** The data object */
    protected SymmetricDataObject dataObject;

    /**
     * @see org.jcryptool.core.operations.algorithm.AbstractAlgorithm#getDataObject()
     */
    @Override
    public IDataObject getDataObject() {
        return dataObject;
    }

    /**
     * Initializes the algorithm.
     * 
     * @param input The input that will be processed
     * @param dataObject The data object containing the details
     */
    public void init(byte[] input, IModernDataObject dataObject) {
        this.dataObject = (SymmetricDataObject) dataObject;
        dataObject.setInput(input);
    }

}
