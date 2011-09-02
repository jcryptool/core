// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.textmodify.wizard;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.jcryptool.analysis.textmodify.wizard.messages"; //$NON-NLS-1$
    public static String ModifySelectionComposite_howwillitlooklike;
    public static String ModifySelectionComposite_preview;
    public static String ModifyWizard_textmodifyoptions;
    public static String ModifyWizardPage_alltolower;
    public static String ModifyWizardPage_alltoupper;
    public static String ModifyWizardPage_filteralpha;
    public static String ModifyWizardPage_hint1;
    public static String ModifyWizardPage_replaceblanks;
    public static String ModifyWizardPage_texttransformations;
    public static String ModifyWizardPage_umlauts;
    public static String ModifyWizardPage_upperLower;
    public static String PreviewViewer_close;
    public static String PreviewViewer_original;
    public static String PreviewViewer_transformed;
    public static String PreviewViewer_fileNotOpen;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
