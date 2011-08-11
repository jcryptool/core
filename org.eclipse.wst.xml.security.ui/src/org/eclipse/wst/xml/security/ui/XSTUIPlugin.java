/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

/**
 * <p>The <b>XML Security Tools</b> allows users to experiment with the complex W3C
 * recommendations on signatures and encryption with the Extensible Markup Language and to gather
 * information about the theoretical background of these recommendations.</p>
 *
 * <p>Arbitrary XML documents can be canonicalized, signed, verified as well as encrypted and decrypted inside most of
 * the users' famous Eclipse XML editor plug-in. The plug-ins main intention is to let users experiment with all
 * possible features of XML security in an easy and interesting way and to provide a tool to learn all about the
 * extensive W3C recommendations.</p>
 *
 * <p>This is the main plug-in class for the <b>XML Security Tools</b> UI plug-in to be used in the workbench.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class XSTUIPlugin extends AbstractUIPlugin {
    /** The main instance of the XML Security Tools UI plug-in. */
    private static XSTUIPlugin plugin;
    /** Storage for the user preferences of the XML Security Tools. */
    private ScopedPreferenceStore preferenceStore;

    public XSTUIPlugin() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        preferenceStore = null;
        super.stop(context);
    }

    /**
     * Gets the XML Security Tools UI plug-in id from the OSGi-Bundle.
     *
     * @return The Plug-In id
     */
    public static String getId() {
        return getDefault().getBundle().getSymbolicName();
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path.
     *
     * @param path The path
     * @return The image descriptor
     */
    public static ImageDescriptor getImageDescriptor(final String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin(getId(), path);
    }

    /**
     * Returns the shared instance of the XML Security Tools UI plug-in.
     *
     * @return The XML Security Tools UI plug-in instance
     */
    public static XSTUIPlugin getDefault() {
        return plugin;
    }

    @Override
    protected void initializeImageRegistry(ImageRegistry registry) {
        registry.put("sig_valid_small", getImageDescriptor("icons/sig_valid_small.gif"));
        registry.put("sig_invalid_small", getImageDescriptor("icons/sig_invalid_small.gif"));
        registry.put("sig_unknown_small", getImageDescriptor("icons/sig_unknown_small.gif"));
        registry.put("sig_valid_large", getImageDescriptor("icons/sig_valid_large.gif"));
        registry.put("sig_invalid_large", getImageDescriptor("icons/sig_invalid_large.gif"));
        registry.put("sig_unknown_large", getImageDescriptor("icons/sig_unknown_large.gif"));
        registry.put("echo_password", getImageDescriptor("icons/but_echo_password.gif"));
    }

    /**
     * <p>Returns exactly one preference store for the XML Security Tools. This preference store is always lazily
     * created.<br>
     * This preference store is used to hold persistent settings for this plug-in in the context of a workbench.</p>
     *
     * <p>If an error occurs reading the preference store, an empty preference store is quietly created, initialized
     * with defaults, and returned.</p>
     *
     * @return The preference store
     */
    public IPreferenceStore getPreferenceStore() {
        if (preferenceStore == null) {
            preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, getBundle().getSymbolicName());
        }
        return preferenceStore;
    }

    /**
     * Returns the active workbench window.
     *
     * @return The active workbench window
     */
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        return getDefault().getWorkbench().getActiveWorkbenchWindow();
    }
}
