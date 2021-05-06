// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm.modern;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;

import org.eclipse.core.runtime.CoreException;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithm;
import org.jcryptool.core.operations.providers.ProvidersManager;

/**
 * The standard implementation of a modern algorithm.
 * 
 * @author amro
 * @author t-kern
 * 
 */
public abstract class AbstractModernAlgorithm extends AbstractAlgorithm {

    /**
     * Returns the default Provider.
     * 
     * @return The default Provider
     * @throws CoreException
     */
    protected Provider getDefaultProvider() throws CoreException {
        return ProvidersManager.getInstance().getDefaultProvider();
    }

    /**
     * Returns true, if the default Provider supports the requested service.
     * 
     * @param type The type of the requested service
     * @param algorithmName The name of the requested algorithm
     * @return <i>true</i>, if the default Provider supports this service. <i>false</i> otherwise
     */
    protected boolean isServiceProvidedByDefault(String type, String algorithmName) {
        return ProvidersManager.getInstance().isServiceProvidedByDefault(type, algorithmName);
    }

    /**
     * Returns the supporting Provider for the requested algorithm and type. <br>
     * If the default Provider supports the requested service, it will be returned. Otherwise, the fall-through
     * hierarchy will be searched for a Provider that supports the requested service.<br>
     * The sequence of this search can be defined by the user in the Crypto Providers PreferencePage.
     * 
     * @param type The type of the requested service
     * @param algorithmName The name of the requested algorithm
     * @return The default Provider, if it supports the service. Otherwise it returns another one
     * @throws CoreException
     * @throws NoSuchAlgorithmException thrown, if no available Provider supports the requested service
     */
    protected Provider getSupportingProvider(String type, String algorithmName) throws CoreException,
            NoSuchAlgorithmException {
        return ProvidersManager.getInstance().getSupportingProvider(type, algorithmName);
    }
}
