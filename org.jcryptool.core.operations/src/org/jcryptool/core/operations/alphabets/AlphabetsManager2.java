// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.alphabets;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jcryptool.core.cryptosystem.core.Alphabet;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;

/**
 * Central point of access to the alphabets. <br>
 * Suports adding, editing and removing custom alphabets and provides access to the basic platform alphabets.
 * 
 * 
 * @author t-kern, sleischnig
 * 
 */
public class AlphabetsManager2 {
    /** Singleton instance */
    private static AlphabetsManager2 instance;

    /** The AlphabetStore (loaded on demand) */
    private AbstractAlphabetStore2 store = null;

    /** The name of the factory-default alphabet */
    public static final String FACTORY_DEFAULT_ALPHABET = "Upper and lower Latin (A-Z,a-z)"; //$NON-NLS-1$

    /**
     * Creates a new instance of AlphabetsManager and initializes the store.
     */
    private AlphabetsManager2() {
        loadAlphabetPlugin();

        if (store != null) {
            store.init();
        }
    }

    /**
     * Returns the singleton instance.
     * 
     * @return The singleton instance
     */
    public static AlphabetsManager2 getInstance() {
        if (instance == null)
            instance = new AlphabetsManager2();
        return instance;
    }

    /**
     * Loads the alphabets Plug-in
     * 
     * @return An instance of the loaded <i>AbstractAlphabetStore</i>
     */
    private void loadAlphabetPlugin() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();

        IExtensionPoint extensionPoint = registry.getExtensionPoint(OperationsPlugin.PLUGIN_ID,
                IOperationsConstants.PL_ALPHABETS);

        IExtension[] extensions = extensionPoint.getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            if (extensions[i].getNamespaceIdentifier().equals("org.jcryptool.crypto.classic.alphabets")) { //$NON-NLS-1$

                IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
                for (int j = 0; j < configElements.length; j++) {
                    if (configElements[j].getName().equals(IOperationsConstants.PL_ALPHABETS)) {
                        try {
                            store = (AbstractAlphabetStore2) configElements[j]
                                    .createExecutableExtension(IOperationsConstants.ATT_CLASS);
                        } catch (CoreException e) {
                            LogUtil.logError(OperationsPlugin.PLUGIN_ID,
                                    "Exception while loading the AlphabetStore", e, true); //$NON-NLS-1$
                        }
                    }
                }
            }
        }
    }

    // /**
    // * Returns the names of the custom alphabets.
    // *
    // * @return The names of the custom alphabets
    // */
    // public String[] getSelfCreatedAlphaList() {
    // return store.getSelfCreatedAlphaList();
    // }

    /**
     * Adds an Alphabet to the store.
     * 
     * @param alphabet The Alphabet that will be added
     */
    public void addAlphabet(Alphabet<Character> alphabet, String name, String shortName, boolean isDefault,
            boolean isIntegral) {
        store.addAlphabet(alphabet, name, shortName, isDefault, isIntegral);
    }

    /**
     * Updates an Alphabet with a new charset.
     * 
     * @param alphabetTitle The name of the Alphabet that will be updated
     * @param newCharacterSet The new charset
     */
    public void updateAlphabet(AlphabetReference alphabetReference, Alphabet<Character> alphabet) {
        store.updateAlphabet(alphabetReference, alphabet);
    }

    /**
     * Removes an Alphabet from the store.
     * 
     * @param alphabet The Alphabet that will be removed
     */
    public void removeAlphabet(AlphabetReference alphabet) {
        store.removeAlphabet(alphabet);
    }

    /**
     * Returns the available Alphabets.
     * 
     * @return The available Alphabets
     */
    public List<Alphabet<Character>> getAlphabets() {
        return store.getAlphabets();
    }

    /**
     * Sets the available Alphabets.
     * 
     * @param alphabets The new available Alphabets
     */
    public void setAlphabets(List<Alphabet<Character>> alphabets, boolean save) throws IOException {
        store.setAlphabets(alphabets);
        if (save) {
            store.storeAlphabets();
        }
    }

    /**
     * Returns the first Alphabet of the given name.
     * 
     * @param name The name of the Alphabet
     * @return The Alphabet TODO: deprecation deprecated since alphabets will soon be internationalized, unique
     *         identifiers will be used.
     */
    public Alphabet<Character> getAlphabetByName(String name) {
        return store.getAlphabetByName(name);
    }

    /**
     * Returns the Alphabet of the given name.
     * 
     * @param name The name of the Alphabet
     * @return The Alphabet
     */
    public Alphabet<Character> getAlphabetByShortName(String name) {
        return store.getAlphabetByShortName(name);
    }

    public Alphabet<Character> getAlphabet(AlphabetReference alphabetReference) {
        return store.getAlphabet(alphabetReference);
    }

    public AbstractAlphabet getDefaultAlphabet() {
        // Alphabet<Character>[] alphas = store.getAlphabets();
        // for (int i = 0; i < alphas.length; i++) {
        // if (alphas[i].isDefaultAlphabet()) {
        // return alphas[i];
        // }
        // }
        // TODO: what to do with this function
        return null;
    }

}
