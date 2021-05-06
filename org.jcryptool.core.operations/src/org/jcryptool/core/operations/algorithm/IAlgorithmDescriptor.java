// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm;

import org.jcryptool.core.operations.CommandInfo;

/**
 * The specifications for descriptors.
 * 
 * @author t-kern
 * @author Holger Friedrich (support for Commands)
 * 
 */
public interface IAlgorithmDescriptor {

    /**
     * Returns the name of the algorithm.
     * 
     * @return The name of the algorithm
     */
    public String getName();

    /**
     * Returns the type (i.e. symmetric, asymmetric, etc.) of the algorithm
     * 
     * @return The type of the algorithm
     */
    public String getType();

    /**
     * Returns the algorithm plug-in ID
     * 
     * @return The algorithm plug-in ID
     */
    public String getAlgorithmID();

    /**
     * Returns the Extension's ID
     * 
     * @return The Extension's ID
     */
    public String getExtensionUID();

    /**
     * Returns the action that will activate the plug-in and perform the algorithm.
     * 
     * @return The action that will activate the plug-in and perform the algorithm
     */
    public CommandInfo getCommand();

    /**
     * Returns an int array containing all supported key lengths of this algorithm.
     * 
     * @return An int array containing all supported key lengths of this algorithm
     */
    public int[] getSupportedKeyLengths();

    /**
     * Returns an int array containing all supported block lengths of this algorithm.
     * 
     * @return An int array containing all supported block lengths of this algorithm
     */
    public int[] getSupportedBlockLengths();

    /**
     * Returns the text associated with this algorithm's action.
     * 
     * @return The text associated with this algorithm's action
     */
    public String getToolTipText();

    /**
     * Returns true for FlexiProvider algortihms
     * 
     * @return true for FlexiProvider algortihms
     */
    public boolean isFlexiProviderAlgorithm();
}
