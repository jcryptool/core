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
package org.eclipse.wst.xml.security.core;

import org.eclipse.ui.plugin.AbstractUIPlugin;
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
 * <p>This is the main plug-in class for the <b>XML Security Tools</b> core plug-in to be used in the workbench. This class does
 * the initialization work for the <b>Apache XML Security</b> library and the resource bundle initialization. Starts and
 * stops the complete plug-in.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class XmlSecurityPlugin extends AbstractUIPlugin {
    /** The main instance of the XML Security Tools. */
    private static XmlSecurityPlugin plugin;

    /**
     * The main constructor of the XML Security Tools.
     */
    public XmlSecurityPlugin() {
    }

    /**
     * This method is called upon XML Security Tools activation. It initializes the Apache XML Security library first
     * before any other action occurs.
     *
     * @param context The bundle context
     * @throws Exception to indicate any exceptional condition
     */
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        org.apache.xml.security.Init.init();
        plugin = this;
    }

    /**
     * This method is called when the XML Security Tools is stopped.
     *
     * @param context The bundle context
     * @throws Exception to indicate any exceptional condition
     */
    public void stop(final BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance of the XML Security Tools.
     *
     * @return The XML Security Tools instance
     */
    public static XmlSecurityPlugin getDefault() {
        return plugin;
    }

    /**
     * Gets the XML Security Tools id from the OSGi-Bundle.
     *
     * @return The Plug-In id
     */
    public static String getId() {
        return getDefault().getBundle().getSymbolicName();
    }
}
