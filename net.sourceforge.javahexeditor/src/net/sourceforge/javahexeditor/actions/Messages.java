//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package net.sourceforge.javahexeditor.actions;

import org.eclipse.osgi.util.NLS;

/**
 * Externalized strings for the <b>net.sourceforge.javahexeditor.actions</b> package.
 *
 * @author Dominik Schadow
 * @version 0.6.0
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "net.sourceforge.javahexeditor.actions.messages"; //$NON-NLS-1$
    public static String OpenInTextEditorAction_0;
    public static String OpenInTextEditorAction_1;
    public static String OpenInTextEditorAction_2;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
