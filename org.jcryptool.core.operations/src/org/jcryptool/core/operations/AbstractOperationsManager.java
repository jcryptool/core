// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations;


/**
 * Abstract base class for plug-ins that integrate with an operations manager.
 * 
 * @see org.jcryptool.core.operations.AlgorithmsManager
 * 
 * @author amro
 * @author Holger Friedrich (support for Commands)
 * @version 0.2
 */
public abstract class AbstractOperationsManager {

    /**
     * Subclasses must provide a mechanism to load all appropriate plug-ins.
     * 
     */
    public abstract void loadAlgorithmsExtensions();

    /**
     * Subclasses must provide a setter for enabling the operations.
     * 
     * @param active if the parameter is <i>true</i> the actions will be enabled and if it is <i>false</i> the actions
     *            will be disabled
     */
    public abstract void setCommandsEnabled(boolean active);

    /**
     * Subclasses must provide a getter for the algorithm actions.
     * 
     * @return an CommandOrAction array of the algorithm actions
     */
    public abstract CommandInfo[] getShadowAlgorithmCommands();

    /**
     * Subclasses must provide a getter for a specific algorithm type. The type is retrieved via a passed action object.
     * 
     * @param action
     * @return
     */
    public abstract String getAlgorithmType(CommandInfo command);
}
