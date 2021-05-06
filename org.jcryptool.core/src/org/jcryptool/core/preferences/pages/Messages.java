// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.preferences.pages;

import org.eclipse.osgi.util.NLS;

/**
 * Externalized strings for the <code>org.jcryptool.core.preferences.pages</code> package.
 * 
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.core.preferences.pages.messages"; //$NON-NLS-1$
    public static String AlgorithmsPage_0;
	public static String AlgorithmsPage_checkbox_text;
	public static String AlgorithmsPage_group_title;
    public static String AnalysisPage_0;
    public static String CryptoPage_0;
    public static String Editors_0;
    public static String Games_0;
    public static String General_0;
    public static String MessageError;
    public static String MessageLogRestart;
    public static String MessageRestart;
    public static String MessageTitleRestart;
    public static String SelectLanguage;
    public static String WorkspaceLocation;
    public static String VisualsPage_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
