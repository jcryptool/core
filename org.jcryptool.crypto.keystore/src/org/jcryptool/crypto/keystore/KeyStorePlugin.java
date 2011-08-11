// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.keystore;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * The activator class controls the plug-in life cycle
 */
public class KeyStorePlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.jcryptool.crypto.keystore"; //$NON-NLS-1$

    public static final String KEYSTORE_PREFERENCES_SEPARATOR = ","; //$NON-NLS-1$
    private static final String KEYSTORES_SEPARATOR = ";"; //$NON-NLS-1$

    /**
     * Identifier for this plug-in's preference node. Value is <code>keystores</code>.
     */
    private static final String KEYSTORES = "keystores"; //$NON-NLS-1$

    /**
     * Identifier for the <code>AVAILABLE_KEYSTORES</code> preference node. Value is
     * <code>availableKeyStores</code>.
     */
    private static final String AVAILABLE_KEYSTORES = "availableKeyStores"; //$NON-NLS-1$

    /**
     * Identifier for the <code>CURRENT_KEYSTORE</code> preference node. Value is
     * <code>currentKeyStore</code>.
     */
    private static final String CURRENT_KEYSTORE = "currentKeyStore"; //$NON-NLS-1$

    /**
     * Identifier for the <code>PLATFORM_KEYSTORE</code> preference node. Value is
     * <code>platformKeyStore</code>.
     */
    private static final String PLATFORM_KEYSTORE = "platformKeyStore"; //$NON-NLS-1$

    /**
     * Constant for the file name of the platform keystore. Value is <code>jctKeystore.ksf</code>
     */
    private static final String PLATFORM_KEYSTORE_FILENAME = "jctKeystore.ksf"; //$NON-NLS-1$

    /**
     * Constant for the name of the platform keystore. Value is <code>JCrypTool Keystore</code>
     */
    private static final String PLATFORM_KEYSTORE_NAME = "JCrypTool Keystore"; //$NON-NLS-1$

    private static final String PLATFORM_SEPARATOR = System.getProperty("file.separator"); //$NON-NLS-1$
    private static final String FLEXIPROVIDER_FOLDER = "flexiprovider"; //$NON-NLS-1$

    /** Convenience URI object for the platform keystore */
    private static final URI PLATFORM_KEYSTORE_URI =
            URIUtil.toURI(DirectoryService.getWorkspaceDir() + PLATFORM_SEPARATOR
                    + FLEXIPROVIDER_FOLDER + PLATFORM_SEPARATOR + PLATFORM_KEYSTORE_FILENAME);

    /** The platform keystore (<name>,<URI>) */
    private static String platformKeyStore;

    /** The name of the current keystore */
    private static String currentKeyStore;

    /** All available keystores (<name>,<URI>) */
    private static List<String> availableKeyStores = new ArrayList<String>();

    /** The preferences store */
    private static IEclipsePreferences preferences;

    // The shared instance
    private static KeyStorePlugin plugin;
    
    private static boolean initialized = false;
    
    /**
     * The constructor
     */
    public KeyStorePlugin() {
    }
    

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static KeyStorePlugin getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static ImageDescriptor getImageDescriptor(String pluginID, String path) {
        return imageDescriptorFromPlugin(pluginID, path);
    }

    /**
     * Returns the name of the platform keystore.
     *
     * @return The name of the platform keystore
     */
    public static String getPlatformKeyStore() {
        return platformKeyStore;
    }

    /**
     * Returns the name of the platform keystore.
     *
     * @return The name of the platform keystore
     */
    public static String getPlatformKeyStoreName() {
        return PLATFORM_KEYSTORE_NAME;
    }

    /**
     * Returns the URI of the platform keystore.
     *
     * @return The URI of the platform keystore
     */
    public static URI getPlatformKeyStoreURI() {
        return PLATFORM_KEYSTORE_URI;
    }

    public static String getFlexiProviderFolder() {
        return FLEXIPROVIDER_FOLDER;
    }

    /**
     * Returns the name of the current keystore.
     *
     * @return The name of the current keystore
     */
    public static String getCurrentKeyStore() {
        return currentKeyStore;
    }

    /**
     * Returns the available keystores.
     *
     * @return The available keystores
     */
    public static List<String> getAvailableKeyStores() {
        return availableKeyStores;
    }

    /**
     * Setter for the name of the current keystore.
     *
     * @param current The name of the current keystore
     */
    public static void setCurrentKeyStore(String current) {
        currentKeyStore = current;
    }

    /**
     * Setter for the available keystores.
     *
     * @param list An ArrayList containing the names and URIs of the available keystores.
     */
    public static void setAvailableKeyStores(List<String> list) {
        availableKeyStores = list;
    }

    /**
     * Returns an <i>encoded</i> String representation of the available keystores.
     *
     * @return an <i>encoded</i> String representation of the available keystores
     */
    private static String concatenateAvailableKeyStores() {
        String result = ""; //$NON-NLS-1$
        Iterator<String> it = availableKeyStores.iterator();
        while (it.hasNext()) {
            result = result.concat(it.next() + KEYSTORES_SEPARATOR);
        }
        return result;
    }

    /**
     * Creates an ArrayList from an <i>encoded</i> String.
     *
     * @param encoded The <i>encoded</i> String
     */
    private static void buildAvailableKeyStores(String encoded) {
        StringTokenizer tokenizer = new StringTokenizer(encoded, KEYSTORES_SEPARATOR);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!availableKeyStores.contains(token) && keyStoreFileExists(token)) {
                availableKeyStores.add(token);
            }
        }
        // ensures, that non-existant key stores are removed immediately.
        savePreferences();
    }

    /**
     * Checks, if the given keystore actually exists.
     *
     * @param entry The keystore entry, which will be checked
     * @return <code>true</code>, if the given keystore entry has an existing corresponding file
     */
    private static boolean keyStoreFileExists(String entry) {
        try {
            URI uri =
                    new URI(entry.substring(entry.indexOf(KEYSTORE_PREFERENCES_SEPARATOR) + 1,
                            entry.length()));
            IFileStore store = EFS.getStore(uri);
            IFileInfo info = store.fetchInfo();
            if (info.exists()) {
                return true;
            }
        } catch (URISyntaxException e) {
        } catch (CoreException e) {
        }
        return false;
    }

    /**
     * Loads this plug-in's preferences.<br>
     * Loads the default preferences, if none are associated with the respective keys.
     */
    public static void loadPreferences() {
        preferences = ConfigurationScope.INSTANCE.getNode(PLUGIN_ID);
        Preferences keyStorePreferences = preferences.node(KEYSTORES);
        platformKeyStore =
                keyStorePreferences.get(PLATFORM_KEYSTORE, PLATFORM_KEYSTORE_NAME
                        + KEYSTORE_PREFERENCES_SEPARATOR + PLATFORM_KEYSTORE_URI.toString()
                        + KEYSTORES_SEPARATOR);
        currentKeyStore = keyStorePreferences.get(CURRENT_KEYSTORE, PLATFORM_KEYSTORE_NAME);
        buildAvailableKeyStores(keyStorePreferences.get(AVAILABLE_KEYSTORES, PLATFORM_KEYSTORE_NAME
                + KEYSTORE_PREFERENCES_SEPARATOR + PLATFORM_KEYSTORE_URI.toString()
                + KEYSTORES_SEPARATOR));
    }

    /**
     * Saves this plug-in's preferences.
     */
    public static void savePreferences() {
        preferences = ConfigurationScope.INSTANCE.getNode(PLUGIN_ID);
        Preferences keyStorePreferences = preferences.node(KEYSTORES);
        keyStorePreferences.put(AVAILABLE_KEYSTORES, concatenateAvailableKeyStores());
        keyStorePreferences.put(CURRENT_KEYSTORE, currentKeyStore);
        try {
            keyStorePreferences.flush();
        } catch (BackingStoreException e) {
        }
    }

    public static URI getCurrentKeyStoreURI() {
        Iterator<String> it = availableKeyStores.iterator();
        String tmp;
        while (it.hasNext()) {
            tmp = it.next();
            if (tmp.substring(0, tmp.indexOf(KEYSTORE_PREFERENCES_SEPARATOR)).equals(
                    getCurrentKeyStore())) {
                try {
                    return new URI(tmp.substring(tmp.indexOf(KEYSTORE_PREFERENCES_SEPARATOR) + 1,
                            tmp.length()));
                } catch (URISyntaxException e) {
                }
            }
        }
        return null;
    }


    public static boolean isInitialized() {
        return initialized;
    }
    
    public static void initialize()
    {
        KeyStoreManager.getInstance(); // loads keystore
        initialized = true;
    }

}
