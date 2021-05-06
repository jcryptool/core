// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm;

import org.jcryptool.core.operations.dataobject.IDataObject;

/**
 * The standard abstract implementation of an algorithm.
 * 
 * @author amro
 * @author Dominik Schadow
 * @version 0.5
 */
public abstract class AbstractAlgorithm {

    /**
     * Constant used to initialize algorithms opmode to encryption mode.
     */
    public static final int ENCRYPT_MODE = 0;

    /**
     * Constant used to initialize algorithms opmode to decryption mode.
     */
    public static final int DECRYPT_MODE = 1;

    /**
     * Getter for data object
     * 
     * @return the data object
     */
    public abstract IDataObject getDataObject();

    /**
     * Subclasses must provide an execution mechanism.
     * 
     * @return the data object, which specific fields were setted before and after the execution of the algorithm.
     */
    public abstract IDataObject execute();

    /**
     * Subclasses must provide an algorithm name. This name is used for the <b>Actions view</b> to represent the crypto
     * plug-in.
     * 
     * @return The name of the algorithm
     */
    public abstract String getAlgorithmName();
}
