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

import java.util.StringTokenizer;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.CommandInfo;

/**
 * Descriptor for algorithms.<br>
 * Contains all vital meta-information about the algorithm and methods to access and "lazy-load" the algorithm.
 * 
 * @author t-kern
 * @author Holger Friedrich (support for Commands)
 * 
 */
public class AlgorithmDescriptor implements IAlgorithmDescriptor {
    /** The algorithm name */
    private String name;

    /** The algorithm's type */
    private String type;

    /** The ID assigned to the algorithm in the extending plug-in */
    private String algorithmID;

    /** The unique identifier of the extension associated with this algorithm */
    private String extensionUID;

    /** The menu action associated with this algorithm */
    private CommandInfo menuCommand;

    /** The supported key lengths of this algorithm */
    private int[] supportedKeyLengths;

    /** The supported block lengths of this algorithm */
    private int[] supportedBlockLengths;

    /** The tooltip text associated with this algorithm's action */
    private String tooltipText;

    /** The flag for FlexiProvider algorihms */
    private boolean isFlexiProviderAlgorithm;

    /**
     * Creates a new instance of AlgorithmDescriptor.
     * 
     * @param name The name of the algorithm
     * @param type The type of the algorithm
     * @param algorithmID The ID of the algorithm plug-in
     * @param extensionUID The ID of the extension
     * @param keylengths The keylength(s) of the algorithm
     * @param blocklengths The blocklength(s) of the algorithm
     * @param tooltipText The tooltip text
     */
    public AlgorithmDescriptor(String name, String type, String algorithmID, String extensionUID, String keylengths,
            String blocklengths, String tooltipText, boolean isFlexiProviderAlgorithm) {
        this.name = name;
        this.type = type;
        this.algorithmID = algorithmID;
        this.extensionUID = extensionUID;
        setKeyLengths(keylengths);
        setBlockLengths(blocklengths);
        this.tooltipText = tooltipText;
        this.isFlexiProviderAlgorithm = isFlexiProviderAlgorithm;
        menuCommand = new CommandInfo(algorithmID, new ShadowAlgorithmHandler(this));
        menuCommand.setText(name);
    }

    /**
     * Sets the available block lenghts for the algorithm.
     * 
     * @param blocklengths The blocklengths (as a concatenated String)
     */
    private void setBlockLengths(String blocklengths) {
        if (blocklengths != null) {
            if (blocklengths.indexOf("|") == -1) { //$NON-NLS-1$
                // no separator; implies just one given blocklength
                supportedBlockLengths = new int[1];
                supportedBlockLengths[0] = Integer.valueOf(blocklengths);
            } else {
                LogUtil.logInfo("tokenizing blocklengths"); //$NON-NLS-1$
                supportedBlockLengths = tokenizeLengths(blocklengths);
            }
        } else {
            // no block lengths specified
            LogUtil.logInfo(name + " blocklengths is null"); //$NON-NLS-1$
            supportedBlockLengths = new int[0];
        }
    }

    /**
     * Tokenizes the given concatenated String and returns the included numbers.
     * 
     * @param input The concatenated String (Number1|Number2|...)
     * @return An int array containing the numbers
     */
    private int[] tokenizeLengths(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input, "|"); //$NON-NLS-1$
        int[] result = new int[tokenizer.countTokens()];
        LogUtil.logInfo("new token array length: " + result.length); //$NON-NLS-1$
        int counter = 0;
        while (tokenizer.hasMoreTokens()) {
            result[counter] = Integer.valueOf(tokenizer.nextToken());
            counter++;
        }
        return result;
    }

    /**
     * Sets the available key lenghts for the algorithm.
     * 
     * @param keylengths The keylengths (as a concatenated String)
     */
    private void setKeyLengths(String keylengths) {
        LogUtil.logInfo("name: " + name); //$NON-NLS-1$
        if (keylengths != null) {
            LogUtil.logInfo("keylengths: " + keylengths); //$NON-NLS-1$
            if (keylengths.indexOf("|") == -1) { //$NON-NLS-1$
                LogUtil.logInfo("no separator found"); //$NON-NLS-1$
                // no separator; implies just one given keylength
                supportedKeyLengths = new int[1];
                supportedKeyLengths[0] = Integer.valueOf(keylengths);
                LogUtil.logInfo("tokenizing keylengths"); //$NON-NLS-1$
            } else {
                supportedKeyLengths = tokenizeLengths(keylengths);
            }
        } else {
            // no key lengths specified
            LogUtil.logInfo(name + " keylengths is null"); //$NON-NLS-1$
            supportedKeyLengths = new int[0];
        }
    }

    /**
     * @see org.jcryptool.core.operations.algorithm.IAlgorithmDescriptor#getSupportedKeyLengths()
     */
    public int[] getSupportedKeyLengths() {
        return supportedKeyLengths;
    }

    /**
     * @see org.jcryptool.core.operations.algorithm.IAlgorithmDescriptor#getSupportedBlockLengths()
     */
    public int[] getSupportedBlockLengths() {
        return supportedBlockLengths;
    }

    /**
     * @see org.jcryptool.core.operations.algorithm.IAlgorithmDescriptor#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * @see org.jcryptool.core.operations.algorithm.IAlgorithmDescriptor#getType()
     */
    public String getType() {
        return type;
    }

    /**
     * @see org.jcryptool.core.operations.algorithm.IAlgorithmDescriptor#getAlgorithmID()
     */
    public String getAlgorithmID() {
        return algorithmID;
    }

    /**
     * @see org.jcryptool.core.operations.algorithm.IAlgorithmDescriptor#getExtensionUID()
     */
    public String getExtensionUID() {
        return extensionUID;
    }

    /**
     * @see org.jcryptool.core.operations.algorithm.IAlgorithmDescriptor#getCommand()
     */
    public CommandInfo getCommand() {
        return menuCommand;
    }

    /**
     * @see org.jcryptool.core.operations.algorithm.IAlgorithmDescriptor#getToolTipText()
     */
    public String getToolTipText() {
        return tooltipText;
    }

    /**
     * returns true for FlexiProvider algorithms
     * 
     * @return true for FlexiProvider algorithms
     */
    public boolean isFlexiProviderAlgorithm() {
        return isFlexiProviderAlgorithm;
    }
}
