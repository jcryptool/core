// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jcryptool.core.logging.utils.LogUtil;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * The activator class controls the plug-in life cycle.<br>
 * Also the central point of access to the preferences store.<br>
 * Additionally, it providers a point of access to the loaded <code>AlphabetStore</code> of the alphabets Plug-in.
 * 
 * @author amro
 * @author t-kern
 * @version 0.6.0
 */
public class OperationsPlugin extends AbstractUIPlugin {

    /**
     * The plug-in ID
     */
    public static final String PLUGIN_ID = "org.jcryptool.core.operations"; //$NON-NLS-1$

    /** The preferences store */
    private IEclipsePreferences preferences;

    /** Identifier for the storage point for the default Provider */
    private static final String DEFAULT_PROVIDER = "default-provider"; //$NON-NLS-1$

    /** Identifier for the provider hierarchy */
    private static final String FALL_THROUGH = "fall-through"; //$NON-NLS-1$

    /** The fall-through hierarchy */
    private String providerHierarchy = ""; //$NON-NLS-1$
    /** The shared instance. */
    private AbstractOperationsManager algorithmsManager;

    /**
     * The shared instance
     */
    private static OperationsPlugin plugin;

    /**
     * The constructor
     */
    public OperationsPlugin() {
        plugin = this;
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static OperationsPlugin getDefault() {
        return plugin;
    }

    /**
     * Saves the preferences.<br>
     */
    public void savePreferences() {
        preferences = ConfigurationScope.INSTANCE.getNode(PLUGIN_ID);
        Preferences provider = preferences.node(DEFAULT_PROVIDER);
        provider.put(FALL_THROUGH, providerHierarchy);
        try {
            provider.flush();
        } catch (BackingStoreException ex) {
            LogUtil.logError(PLUGIN_ID, ex);
        }
    }

    /**
     * Loads the preferences.<br>
     */
    public void loadPreferences() {
        preferences = ConfigurationScope.INSTANCE.getNode(PLUGIN_ID);
        Preferences provider = preferences.node(DEFAULT_PROVIDER);
        providerHierarchy = provider.get(FALL_THROUGH, ""); //$NON-NLS-1$
    }

    /**
     * Sets the crypto provider hierarchy.
     * 
     * @param providerHierarchy The given Vector will be transformed into a String that can be stored in the preferences
     */
    public void setProviderHierarchy(ArrayList<String> providerHierarchy) {
        String concated = ""; //$NON-NLS-1$
        for (int i = 0; i < providerHierarchy.size(); i++) {
            concated = concated.concat(providerHierarchy.get(i) + "|"); //$NON-NLS-1$
        }
        this.providerHierarchy = concated;
    }

    /**
     * Returns an ArrayList containing the provider hierarchy.<br>
     * (Loads the preferences first.)
     * 
     * @return An ArrayList containing the provider hierarchy
     */
    public ArrayList<String> getProviderHierarchy() {
        loadPreferences();
        ArrayList<String> result = new ArrayList<String>();
        if (providerHierarchy == null || providerHierarchy.equals("")) { //$NON-NLS-1$
            // no saved preferences.
            return null;
        } else {
            StringTokenizer tokenizer = new StringTokenizer(providerHierarchy, "|"); //$NON-NLS-1$
            while (tokenizer.hasMoreElements()) {
                String t = tokenizer.nextToken();
                if (t != null && !t.equals("")) { //$NON-NLS-1$
                    result.add(t);
                }
            }
        }
        return result;
    }

    /**
     * The method uses the superclass method which refreshes the plug-in actions.
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
     * The method uses the superclass method which saves this plug-in's preference and dialog stores and shuts down its
     * image registry (if they are in use).
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Retrieves the algorithms manager. The extension registry is used to fetch the specific contributor of the
     * <i>org.jcryptool.core.operations</i> plug-in.
     * 
     * @return null if there is no specific manager found and an object of AbstractOperationsManager if a manager is
     *         found
     */
    private static AbstractOperationsManager retrieveAlgorithmsManager() {

        IExtensionRegistry registry = Platform.getExtensionRegistry();

        IExtensionPoint extensionPoint = registry.getExtensionPoint(PLUGIN_ID, "operationsManager"); //$NON-NLS-1$

        IExtension[] extensions = extensionPoint.getExtensions();

        for (int i = 0; i < extensions.length; i++) {
            if ("org.jcryptool.core.operations.algorithm.AlgorithmRegistry".equals( //$NON-NLS-1$
                    extensions[i].getUniqueIdentifier())) {
                IConfigurationElement[] configElements = extensions[i].getConfigurationElements();

                for (int j = 0; j < configElements.length; j++) {
                    IConfigurationElement element = configElements[j];

                    if ("manager".equals(element.getName())) { //$NON-NLS-1$
                        try {
                            AbstractOperationsManager manager = (AbstractOperationsManager) element
                                    .createExecutableExtension(IOperationsConstants.ATT_CLASS);

                            return manager;
                        } catch (CoreException ex) {
                            LogUtil.logError(PLUGIN_ID, ex);
                        }
                    }

                }
            }
        }

        return null;
    }

    /**
     * Initialize the algorithms manager.
     */
    public void initAlgorithmsManager() {
        algorithmsManager = retrieveAlgorithmsManager();

        if (algorithmsManager != null) {
            algorithmsManager.loadAlgorithmsExtensions();
        }
    }

    /**
     * Getter for the algorithms manager.
     * 
     * @return the algorithms manager as a AbstractOperationsManager object
     */
    public AbstractOperationsManager getAlgorithmsManager() {
        return algorithmsManager;
    }

}
