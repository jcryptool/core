// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
/**
 *
 */
package org.jcryptool.core.operations.providers;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;

/**
 * The ProvidersManager class represents a global point of access to extensions of the extension point <i>providers</i>.
 * The extensions are loaded conform to the <i>lazy loading</i> rule: Firstly only the declarative manifest is
 * retrieved, the appropriate business logic is only loaded if required. The first step is performed by the method
 * <i>retrieveProvidersExtensions</i> and <i>performCreateExecutableExtension</i> serves the second one.
 * 
 * The class has only instance (singleton class).
 * 
 * @author amro
 * @author t-kern
 * 
 */
public class ProvidersManager {
    /** The factory default Provider ID (FlexiProvider's Plug-in ID) */
    public static final String factoryDefaultProviderID = "de.flexiprovider"; //$NON-NLS-1$

    /** The default provider instance */
    private Provider defaultProvider;

    /**
     * The singleton object
     */
    private static ProvidersManager instance = null;

    /**
     * No-args constructor.
     */
    private ProvidersManager() {
        loadAvailableProviders();
    }

    /**
     * Getter for the only EditorManager object
     * 
     * @return the EditorManager object
     */
    public synchronized static ProvidersManager getInstance() {
        if (instance == null)
            instance = new ProvidersManager();
        return instance;
    }

    /** All available providers Mapping: (name, MetaProvider[id, name, info]) */
    private HashMap<String, ProviderDescriptor> availableProviders = new HashMap<String, ProviderDescriptor>();

    /** The names of all available providers in the correct order */
    private ArrayList<String> availableProviderNames = new ArrayList<String>();

    /**
     * Loads all available providers and puts them in the correct order.
     */
    private void loadAvailableProviders() {
        loadPluginProviders();
        // loadPlatformProviders();

        // from preferences
        if (OperationsPlugin.getDefault().getProviderHierarchy() != null /* false */) {
            LogUtil.logInfo("loading preferences"); //$NON-NLS-1$
            availableProviderNames = OperationsPlugin.getDefault().getProviderHierarchy();
            availableProviderNames = makeConsistent(availableProviderNames);
        } else {
            // since no preferences could be loaded, the factory default is set
            // as the first element
            availableProviderNames = getAvailableProviderNames();
            enforceFactoryDefault();

            // saving the initial preferences
            savePreferences();
        }
    }

    /**
     * Returns a list containing all the info values of all Providers.
     * 
     * @return A list containing all the info values of all Providers
     */
    public ArrayList<String> getAvailableProviderInfos() {
        ArrayList<String> infos = new ArrayList<String>();
        Iterator<String> it = availableProviderNames.iterator();
        while (it.hasNext()) {
            infos.add(availableProviders.get(it.next()).getInfo());
        }
        return infos;
    }

    /**
     * Takes the order of the infos received from the PreferencePage and puts the provider hierarchy in the correct
     * order.
     * 
     * @param providerHierarchy Contains the info values received from the PreferencePage
     */
    public void setProviderHierarchy(ArrayList<String> providerHierarchy) {
        // providerHierarchy contains the infos; get the names
        ArrayList<String> newProviderHierarchy = new ArrayList<String>();

        Iterator<String> infos = providerHierarchy.iterator();
        Entry<String, ProviderDescriptor> entry;
        String info;
        while (infos.hasNext()) {
            Iterator<Entry<String, ProviderDescriptor>> entries = availableProviders.entrySet().iterator();
            info = infos.next();
            while (entries.hasNext()) {
                entry = entries.next();
                if (entry.getValue().getInfo().equals(info)) {
                    newProviderHierarchy.add(entry.getValue().getName());
                }
            }
        }
        availableProviderNames = newProviderHierarchy;
    }

    /**
     * Moves the FlexiProvider name to the front of the list.
     */
    private void enforceFactoryDefault() {
        if (availableProviderNames.contains(getMetaFactoryDefault().getName())) {
            LogUtil.logInfo("enforcing factory default"); //$NON-NLS-1$
            int index = availableProviderNames.indexOf(getMetaFactoryDefault().getName());
            String delta = availableProviderNames.get(index);
            availableProviderNames.remove(index);
            availableProviderNames.add(0, delta);
        }
    }

    /**
     * Returns the names of all available providers.
     * 
     * @return The names of all available providers
     */
    private ArrayList<String> getAvailableProviderNames() {
        ArrayList<String> names = new ArrayList<String>();
        Iterator<ProviderDescriptor> it = availableProviders.values().iterator();
        while (it.hasNext()) {
            names.add(it.next().getName());
        }
        return names;
    }

    public boolean isPluginProvider(String provider) {
        Iterator<ProviderDescriptor> it = availableProviders.values().iterator();
        while (it.hasNext()) {
            if (it.next().getName().equals(provider)) {
                LogUtil.logInfo("Provider: " + provider + " is present"); //$NON-NLS-1$ //$NON-NLS-2$
                return true;
            }
        }
        return false;
    }

    public void addProvider(String provider) {
        if (Security.getProvider(provider) == null) {
            try {
                LogUtil.logInfo("provider not yet loaded. adding it to the security registry"); //$NON-NLS-1$
                getProvider(provider);
            } catch (CoreException e) {
                LogUtil.logError(OperationsPlugin.PLUGIN_ID, "CoreException while adding a provider", e, false); //$NON-NLS-1$
            }
        }
    }

    public boolean isProviderAvailable(String provider) {
        // check security
        LogUtil.logInfo("Looking for provider: " + provider); //$NON-NLS-1$
        if (Security.getProvider(provider) != null) {
            // provider is present
            // it's either a platform provider or has already been initialized
            LogUtil.logInfo("provider is present"); //$NON-NLS-1$
            return true;
        } else {
            if (isPluginProvider(provider)) {
                // check provider plug-ins
                LogUtil.logInfo("provider is available"); //$NON-NLS-1$
                return true;
            }
        }
        return false;
    }

    /**
     * Makes the given list consistent.<br>
     * Entries in the list that
     * 
     * @param fromPreferences The list that will be mande consistent
     * @return The consistent list
     */
    private ArrayList<String> makeConsistent(ArrayList<String> fromPreferences) {
        // entries from the preferences not available will be removed
        for (int i = 0; i < fromPreferences.size(); i++) {
            if (!getAvailableProviderNames().contains(fromPreferences.get(i))) {
                fromPreferences.remove(i);
            }
        }

        // if the (factory) default provider is not in the preferences, it will
        // be added to the beginning
        // (if it is available)
        if (getMetaFactoryDefault() != null) {
            if (!fromPreferences.contains(getMetaFactoryDefault().getName())) {
                fromPreferences.add(0, getMetaFactoryDefault().getName());
            }
        }

        // entries from the avaiable providers not in the preferences will be
        // added
        for (int i = 0; i < getAvailableProviderNames().size(); i++) {
            if (!fromPreferences.contains(getAvailableProviderNames().get(i))) {
                fromPreferences.add(getAvailableProviderNames().get(i));
            }
        }

        return fromPreferences;
    }

    /**
     * Returns the FlexiProvider's MetaProvider.
     * 
     * @return FlexiProvider's MetaProvider
     */
    public ProviderDescriptor getMetaFactoryDefault() {
        Iterator<ProviderDescriptor> it = availableProviders.values().iterator();
        ProviderDescriptor provider;
        while (it.hasNext()) {
            provider = it.next();
            if (provider.getID().equals(factoryDefaultProviderID)) {
                return provider;
            }
        }
        return null;
    }

    /**
     * Loads the meta information about providers provided by plug-ins.
     */
    private void loadPluginProviders() {
        LogUtil.logInfo("Loading Providers provided by a plug-in"); //$NON-NLS-1$

        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(OperationsPlugin.PLUGIN_ID,
                IOperationsConstants.PL_PROVIDERS);

        IExtension[] extensions = extensionPoint.getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
            for (int j = 0; j < configElements.length; j++) {
                String name = configElements[j].getAttribute(IOperationsConstants.ATT_PROVIDER_NAME);
                String info = configElements[j].getAttribute(IOperationsConstants.ATT_PROVIDER_INFO);
                ProviderDescriptor installedProvider = new ProviderDescriptor(extensions[i].getUniqueIdentifier(),
                        name, info);
                availableProviders.put(name, installedProvider);
            }
        }
    }

    /**
     * @deprecated This method loades the platform providers. Currently, they are not supported, since we rely on
     *             additional information provided via plug-in manifest. They may be included in Milestone 2
     */
    @SuppressWarnings("unused")
    private void loadPlatformProviders() {
        LogUtil.logInfo("Loading Providers provided by the platform"); //$NON-NLS-1$

        Provider[] providers = Security.getProviders();
        for (int i = 0; i < providers.length; i++) {
            ProviderDescriptor installedProvider = new ProviderDescriptor(providers[i].getName(),
                    providers[i].getInfo());
            availableProviders.put(providers[i].getName(), installedProvider);
        }
    }

    /**
     * Saves the provider priority order to the preferences.
     */
    public void savePreferences() {
        OperationsPlugin.getDefault().setProviderHierarchy(availableProviderNames);
        OperationsPlugin.getDefault().savePreferences();
    }

    /**
     * Returns the provider object of the currently selected default provider.
     * 
     * @return The Provider object of the currently selected default provider
     * @throws CoreException Thrown, when no provider could be loaded
     */
    public Provider getDefaultProvider() throws CoreException {
        LogUtil.logInfo("getDefaultProvider()"); //$NON-NLS-1$

        if (!availableProviders.get(availableProviderNames.get(0)).getID().equals(IOperationsConstants.PLATFORM)) {
            // plugin based provider
            LogUtil.logInfo("plug-in provider"); //$NON-NLS-1$
            if (defaultProvider != null) {
                if (availableProviderNames.get(0).equals(defaultProvider.getName())) {
                    return defaultProvider;
                } else {
                    defaultProvider = getProvider(availableProviderNames.get(0));
                    return defaultProvider;
                }
            } else {
                String extensionID = availableProviders.get(availableProviderNames.get(0)).getID();
                LogUtil.logInfo("ExtensionID: " + extensionID); //$NON-NLS-1$
                defaultProvider = createExecutableExtension(extensionID);
                LogUtil.logInfo("returning a plug-in provider"); //$NON-NLS-1$
                return defaultProvider;
            }
        } else {
            // platform based provider
            LogUtil.logInfo("returning a platform provider"); //$NON-NLS-1$
            return Security.getProvider(availableProviderNames.get(0));
        }
    }

    /**
     * Returns the provider object for the provider with the given name.
     * 
     * @param name The name of the provider
     * @return The corresponding provider object
     * @throws CoreException Thrown, when no provider could be loaded
     */
    public Provider getProvider(String name) throws CoreException {
        if (Security.getProvider(name) != null) {
            return Security.getProvider(name);
        } else {
            Provider localProvider;
            if (!availableProviders.get(name).getID().equals(IOperationsConstants.PLATFORM)) {
                // plugin based provider
                String extensionID = availableProviders.get(name).getID();
                LogUtil.logInfo("extension ID: " + extensionID); //$NON-NLS-1$
                localProvider = createExecutableExtension(extensionID, name);
                LogUtil.logInfo("adding " + name + " name to the security registry"); //$NON-NLS-1$ //$NON-NLS-2$
                Security.addProvider(localProvider);
                return localProvider;
            } else {
                // platform based provider
                return Security.getProvider(name);
            }
        }
    }

    /**
     * Checks whether the default provider supports the specified service.
     * 
     * @param type The type of the service
     * @param algorithmName The algorithm name of the service
     * @return <code>true</code>, when the default provider supports the specified service
     */
    public boolean isServiceProvidedByDefault(String type, String algorithmName) {
        if (defaultProvider == null) {
            LogUtil.logInfo("getting default provider"); //$NON-NLS-1$
            try {
                defaultProvider = getDefaultProvider();
                Service service = null;

                if (defaultProvider != null) {
                    LogUtil.logInfo("defaultProvider " + defaultProvider.getName()); //$NON-NLS-1$
                    service = defaultProvider.getService(type, algorithmName);
                } else {
                    LogUtil.logInfo("dp is null"); //$NON-NLS-1$
                }

                return service != null;
            } catch (CoreException e) {
                LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Unable to create the factory default provider", e, false); //$NON-NLS-1$
            }
        } else {
            LogUtil.logInfo("defaultProvider != null"); //$NON-NLS-1$
            try {
                // precautionary measure. the default provider might not be
                // null,
                // but the preferences might have changed
                defaultProvider = getDefaultProvider();
            } catch (CoreException e) {
                LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Unable to access default provider", e, false); //$NON-NLS-1$
            }
            Service service = defaultProvider.getService(type, algorithmName);
            boolean result = (service != null);
            LogUtil.logInfo("returning " + result); //$NON-NLS-1$
            return result;
        }
        return false;

    }

    /**
     * Returns the provider with the highest priority that supports the specified service. <br>
     * The default provider is queried first and only if it does not support the service, the other available providers
     * are queried in their priority order.
     * 
     * @param type The type of the service
     * @param algorithmName The algorithm name of the service
     * @return The highest-ordered provider supporting the given service
     * @throws NoSuchAlgorithmException Thrown, when no available provider supports the specified service
     */
    public Provider getSupportingProvider(String type, String algorithmName) throws NoSuchAlgorithmException {
        LogUtil.logInfo("getting service provider"); //$NON-NLS-1$
        if (isServiceProvidedByDefault(type, algorithmName)) {
            // the default provider supports the operation
            LogUtil.logInfo("returning the default provider: " + defaultProvider.getName()); //$NON-NLS-1$
            return defaultProvider;
        } else {
            // the default provider does not support the operation
            // query the provider hierarchy
            LogUtil.logInfo("The default provider does not support the service"); //$NON-NLS-1$
            Provider provider;
            Service service;
            Iterator<String> it = availableProviderNames.iterator();
            String providerName;
            while (it.hasNext()) {
                try {
                    providerName = it.next();
                    provider = getProvider(providerName);
                    service = provider.getService(type, algorithmName);
                    if (service != null) {
                        LogUtil.logInfo(providerName + " supports the service!"); //$NON-NLS-1$
                        LogUtil.logInfo(provider.getName());
                        return provider;
                    }
                } catch (CoreException e) {
                    LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Unable to load a provider", e, false); //$NON-NLS-1$
                }
            }
        }
        throw new NoSuchAlgorithmException("No available provider supports " + algorithmName + " of type " + type); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private Provider createExecutableExtension(String extensionID) throws CoreException {
        return createExecutableExtension(extensionID, null);
    }

    /**
     * The method performs the creation of the executable extension. The executable code is loaded, only for the plug-in
     * which extension has the specific extension ID.
     * 
     * @param extensionID ID of the contribution to extension point providers
     * @return the associated crypto provider object of the type <i>java.security.Provider</i>
     * @throws CoreException
     */
    private Provider createExecutableExtension(String extensionID, String providerName) throws CoreException {
        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(OperationsPlugin.PLUGIN_ID,
                IOperationsConstants.PL_PROVIDERS);

        IExtension extension = extensionPoint.getExtension(extensionID);

        IConfigurationElement[] configElements = extension.getConfigurationElements();

        Provider provider = null;

        for (int i = 0; i < configElements.length; i++) {
            IConfigurationElement element = configElements[i];

            LogUtil.logInfo("element.getName(): " + element.getName()); //$NON-NLS-1$
            LogUtil.logInfo("attribute: " + element.getAttribute("providerName")); //$NON-NLS-1$ //$NON-NLS-2$
            if (providerName == null) {
                if (element.getName().equals(IOperationsConstants.TAG_PROVIDER)) {
                    provider = (Provider) element.createExecutableExtension(IOperationsConstants.ATT_CLASS);
                    Security.addProvider(provider);
                }
            } else {
                if (element.getName().equals(IOperationsConstants.TAG_PROVIDER)
                        && element.getAttribute("providerName").equals(providerName)) { //$NON-NLS-1$
                    provider = (Provider) element.createExecutableExtension(IOperationsConstants.ATT_CLASS);
                    Security.addProvider(provider);
                }
            }

            // if(element.getName().equals(IOperationsConstants.TAG_PROVIDER)){
            // provider = (Provider)
            // element.createExecutableExtension(IOperationsConstants.ATT_CLASS);
            // Security.addProvider(provider);
            // }
        }
        return provider;
    }

    /**
     * Returns the ExtensionID for the specified provider.
     * 
     * @param provider The provider (which is implicitly provided by a plug-in)
     * @return The ID of the plug-in supplying the given provider
     */
    private String getExtensionNameForProvider(Provider provider) {
        if (!availableProviders.get(provider.getName()).getID().equals(IOperationsConstants.PLATFORM)) {
            IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
                    OperationsPlugin.PLUGIN_ID, IOperationsConstants.PL_PROVIDERS);

            IExtension extension = extensionPoint.getExtension(availableProviders.get(provider.getName()).getID());
            IConfigurationElement[] configElements = extension.getConfigurationElements();

            for (int i = 0; i < configElements.length; i++) {
                LogUtil.logInfo("ATT_PROVIDER_NAME: " //$NON-NLS-1$
                        + configElements[i].getAttribute(IOperationsConstants.ATT_PROVIDER_NAME));
                LogUtil.logInfo("Provider Name: " + provider.getName()); //$NON-NLS-1$
                if (configElements[i].getAttribute(IOperationsConstants.ATT_PROVIDER_NAME).equals(provider.getName())) {
                    return availableProviders.get(provider.getName()).getID();
                }
            }
        }
        return ""; //$NON-NLS-1$
    }

    /**
     * Returns the value of the given attribute of the plug-in with the given id.
     * 
     * @param id The plug-in ID that will be checked for the attribute value
     * @param attributeName The identifier of the attribute
     * @return The value of the attribute
     */
    private String getAttribute(String id, String attributeName) {
        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(OperationsPlugin.PLUGIN_ID,
                IOperationsConstants.PL_PROVIDERS);

        IExtension extension = extensionPoint.getExtension(id);

        IConfigurationElement[] elements = extension.getConfigurationElements();
        LogUtil.logInfo("elements.length: " + elements.length); //$NON-NLS-1$
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].getAttribute(attributeName) != null) {
                return elements[i].getAttribute(attributeName);
            }
        }
        return ""; //$NON-NLS-1$
    }

    /**
     * Returns an array containing all cipher modes associated with the given service.
     * 
     * @param type The type of the service
     * @param name The name of the algorithm of the service
     * @return An array containing all cipher modes associated with the given service
     */
    public String[] getCipherModes(String type, String name) {
        String[] result = new String[0];
        try {
            Provider provider = getSupportingProvider(type, name);
            String pluginID = getExtensionNameForProvider(provider);
            result = tokenizeAttribute(getAttribute(pluginID, IOperationsConstants.ATT_CIPHER_MODES));
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while asking a service provider", e, false); //$NON-NLS-1$
        }
        return result;
    }

    /**
     * Returns an array containing all paddings associated with the given service.
     * 
     * @param type The type of the service
     * @param name The name of the algorithm of the service
     * @return An array containing all paddings associated with the given service
     */
    public String[] getPaddings(String type, String name) {
        String[] result = new String[0];
        try {
            Provider provider = getSupportingProvider(type, name);
            String pluginID = getExtensionNameForProvider(provider);
            result = tokenizeAttribute(getAttribute(pluginID, IOperationsConstants.ATT_PADDINGS));
        } catch (NoSuchAlgorithmException e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while asking a service provider", e, false); //$NON-NLS-1$
        }
        return result;
    }

    /**
     * Takes a String and tokenizes it into a String array.<br>
     * The expected delimiter is an '|'.
     * 
     * @param attribute Contains a list of attributes delimited by a '|'
     * @return A String array containing all tokens
     */
    private String[] tokenizeAttribute(String attribute) {
        StringTokenizer tokenizer = new StringTokenizer(attribute, "|"); //$NON-NLS-1$
        String[] tokens = new String[tokenizer.countTokens()];
        int counter = 0;
        while (tokenizer.hasMoreElements()) {
            tokens[counter] = tokenizer.nextToken();
            counter++;
        }
        return tokens;
    }

}
