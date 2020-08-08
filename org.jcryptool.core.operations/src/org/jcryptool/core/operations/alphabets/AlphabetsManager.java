// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.alphabets;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;

/**
 * Central point of access to the alphabets. <br>
 * Suports adding, editing and removing custom alphabets and provides access to the basic platform alphabets.
 * 
 * 
 * @author t-kern
 * 
 */
public class AlphabetsManager {
    /** Singleton instance */
    private static AlphabetsManager instance;

    /** The AlphabetStore (loaded on demand) */
    private AbstractAlphabetStore store = null;


    /** The name of the factory-default alphabet */
    public static final String FACTORY_DEFAULT_ALPHABET = "Upper Latin (A-Z)"; //$NON-NLS-1$
    public static final String FACTORY_DEFAULT_ALPHABET_DE = "Upper Latin (A-Z)"; //$NON-NLS-1$

    /**
     * Creates a new instance of AlphabetsManager and initializes the store.
     */
    private AlphabetsManager() {
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
    public synchronized static AlphabetsManager getInstance() {
        if (instance == null)
            instance = new AlphabetsManager();
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
                            store = (AbstractAlphabetStore) configElements[j]
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

    /**
     * Returns the names of the custom alphabets.
     * 
     * @return The names of the custom alphabets
     */
    public String[] getSelfCreatedAlphaList() {
        return store.getSelfCreatedAlphaList();
    }

    /**
     * Adds an Alphabet to the store.
     * 
     * @param alphabet The Alphabet that will be added
     */
    public void addAlphabet(AbstractAlphabet alphabet) {
        store.addAlphabet(alphabet);
    }

    /**
     * Updates an Alphabet with a new charset.
     * 
     * @param alphabetTitle The name of the Alphabet that will be updated
     * @param newCharacterSet The new charset
     */
    public void updateAlphabet(String alphabetTitle, char[] newCharacterSet) {
        store.updateAlphabet(alphabetTitle, newCharacterSet);
    }

    /**
     * Removes an Alphabet from the store.
     * 
     * @param alphabet The Alphabet that will be removed
     */
    public void removeAlphabet(AbstractAlphabet alphabet) {
        store.removeAlphabet(alphabet);
    }

    /**
     * Returns the available Alphabets.
     * 
     * @return The available Alphabets
     */
    public AbstractAlphabet[] getAlphabets() {
        return store.getAlphabets();
    }

    /**
     * Sets the available Alphabets.
     * 
     * @param alphabets The new available Alphabets
     */
    public void setAlphabets(AbstractAlphabet[] alphabets, boolean save) {
        store.setAlphabets(alphabets);
        if (save) {
            try {
                store.storeAlphabets();
            } catch (IOException e) {
                LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while storing the alphabets", e, true); //$NON-NLS-1$
            }
        }
    }

    /**
     * Returns the Alphabet of the given name.
     * 
     * @param name The name of the Alphabet
     * @return The Alphabet TODO: deprecation deprecated since alphabets will soon be internationalized, unique
     *         identifiers will be used.
     */
    public AbstractAlphabet getAlphabetByName(String name) {
        return store.getAlphabetByName(name);
    }

    /**
     * Returns the Alphabet of the given name.
     * 
     * @param name The name of the Alphabet
     * @return The Alphabet
     */
    public AbstractAlphabet getAlphabetByShortName(String name) {
        return store.getAlphabetByShortName(name);
    }

    public AbstractAlphabet getDefaultAlphabet() {
        AbstractAlphabet[] alphas = store.getAlphabets();
        for (int i = 0; i < alphas.length; i++) {
            if (alphas[i].isDefaultAlphabet()) {
                return alphas[i];
            }
        }
        return null;
    }

    /**
     * 
     * @return the size of various alphabets stored
     */
    public int getSize() {
        return store.getSize();
    }

	/**
	 * Retrieves an alphabet by case-sensitively looking for a substring of it. Returns null if no match is found.
	 * 
	 * @param string the substring of the alphabet name to look for
	 * @return a found alphabet (to be considered randomly chosen out of multiple matches) or null if no match was found.
	 */
	public AbstractAlphabet getAlphabetBySubName(String string) {
		Optional<AbstractAlphabet> alpha = Arrays.asList(getAlphabets()).stream().filter(a -> a.getName().contains(string)).findAny();
		return alpha.orElse(null);
	}

}
