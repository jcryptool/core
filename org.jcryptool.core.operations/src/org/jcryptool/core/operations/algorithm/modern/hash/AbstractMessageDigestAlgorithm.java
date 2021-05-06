// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm.modern.hash;

import java.io.InputStream;

import org.jcryptool.core.operations.algorithm.modern.AbstractModernAlgorithm;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.modern.IModernDataObject;
import org.jcryptool.core.operations.dataobject.modern.ModernDataObject;

/**
 * Abstract superclass for message digests, a.k.a. hashes.
 * 
 * @author t-kern
 * 
 */
public abstract class AbstractMessageDigestAlgorithm extends AbstractModernAlgorithm {

    /** The data object */
    protected IModernDataObject dataObject;

    /**
     * @see org.jcryptool.core.operations.algorithm.AbstractAlgorithm#getDataObject()
     */
    @Override
    public IDataObject getDataObject() {
        return dataObject;
    }

    public void init(InputStream input) {
        this.dataObject = new ModernDataObject();
        dataObject.setInputStream(input);
    }

    /**
     * Initializes the message digest with the given input
     * 
     * @deprecated
     * 
     * @param input
     */
    public void init(byte[] input) {

        this.dataObject = new ModernDataObject();

        dataObject.setInput(input);
    }

}
