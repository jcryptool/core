//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.osgi.util.NLS;

/**
 * Holds the messages for the signature visualization (for the 3 wizards and the ShowSig shell)
 * 
 * @author Grebe
 */
public class Messages {
    // Name of the files that contain the string values
    private static final String BUNDLE_NAME = "org.jcryptool.visual.sig.ui.wizards.messages"; //$NON-NLS-1$
    // Create strings for all the values
    // Group box 1 text
    public static String HashWizard_grpHashes;
    // Group box 2 text
    public static String HashWizard_grpDescription;
    // The Texts for the descriptions of the methods
    public static String HashWizard_rdomd5_description;
    public static String HashWizard_rdosha1_description;
    public static String HashWizard_rdosha256_description;
    public static String HashWizard_rdosha384_description;
    public static String HashWizard_rdosha512_description;
    public static String HashWizard_rdomd5;
    public static String HashWizard_rdosha1;
    public static String HashWizard_rdosha256;
    public static String HashWizard_rdosha384;
    public static String HashWizard_rdosha512;
    public static String InputWizard_FileOpenDialog;
    // SignatureWizard
    public static String SignatureWizard_Usage;
    public static String SignatureWizard_Usage2;
    // Group box 1 text
    public static String SignatureWizard_grpSignatures;
    // Group box 2 text
    public static String SignatureWizard_grpDescription;
    public static String SignatureWizard_DSA_description;
    public static String SignatureWizard_RSA_description;
    public static String SignatureWizard_ECDSA_description;
    public static String SignatureWizard_RSAandMGF1_description;
    public static String SignatureWizard_DSA;
    public static String SignatureWizard_RSA;
    public static String SignatureWizard_ECDSA;
    public static String SignatureWizard_RSAandMGF1;
    public static String SignatureWizard_header;
    public static String HashWizard_header;
    public static String HashWizard_WindowTitle;
    public static String SignatureWizard_WindowTitle;
    public static String InputEditorWizard_Label;
    public static String InputFileWirard_btnBrowse;
    public static String InputWizard_WarningTitle;
    public static String InputWizard_WarningMessageTooLarge;
    public static String InputWizard_WarningMessageEmpty;
    public static String InputWizard_Title;
    public static String HashWizard_Title;
    public static String SignatureWizard_Title;
    public static String Wizard_menu;
    public static String SignatureWizard_labelKey;
    public static String SignatureWizard_labelCurve;

    // InputWizard
    // Page 1
    public static String InputWizard_title;
    public static String InputWizard_header;
    public static String InputWizard_rdoFromFile;
    public static String InputWizard_rdoFromEditor;
    // Page 2
    public static String InputFileWizard_title;
    public static String InputFileWizard_header;
    // Page 3
    public static String InputEditorWizard_title;
    public static String InputEditorWizard_header;

    // Show generated signature
    public static String ShowSig_title;
    public static String ShowSig_ownerTitle;
    public static String ShowSig_keyTitle;
    public static String ShowSig_methodTitle;

    public static String ShowSig_grpSignature;
    public static String ShowSig_lengthSig;

    public static String ShowSig_grpOption;
    public static String ShowSig_octal;
    public static String ShowSig_decimal;
    public static String ShowSig_hex;
    public static String ShowSig_hexDump;

    public static String ShowSig_grpMessage;
    public static String ShowSig_grpMessage_first10kB;
    public static String ShowSig_lengthMessage;

    public static String ShowSig_btnClose;
    public static String ShowSig_btnOpen;

    public static String ShowSig_tblAdr;
    public static String ShowSig_tblHex;
    public static String ShowSig_tblAscii;

    public static String ShowSig_editorDescripton;
    public static String ShowSig_randomizedMethods;
    
    public static String EnterText;
    public static String ShowSig_btnSave;
    public static String ShowSig_MessageBoxTitle;
    public static String ShowSig_MessageBoxText;
    public static String ShowSig_ContextCopy;
    public static String ShowSig_ContextCopySelection;
    public static String ShowSig_ContexstCopyAll;
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }

}
