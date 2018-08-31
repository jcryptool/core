package org.jcryptool.visual.sigVerification.ui.wizards;

import org.eclipse.osgi.util.NLS;

/**
 * Holds the messages for the signature visualization (for the 3 wizards and the SignaturResult
 * shell)
 * 
 * @author Wilfing
 */
public class Messages {
    // Name of the files that contain the string values
    private static final String BUNDLE_NAME = "org.jcryptool.visual.sigVerification.ui.wizards.messages"; //$NON-NLS-1$
    
    public static String Copy;
    public static String CopyLine;
    public static String CopyAll;
    
    // Create strings for all the values
    // Group box 1 text
    public static String HashWizard_grpHashes;
    // Group box 2 text
    public static String HashWizard_grpDescription;
    public static String HashWizard_FurtherInfoInOnlineHelp;
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
    // Group box 1 text
    public static String SignatureWizard_grpSignatures;
    public static String SignatureWizard_FurtherInfoInOnlineHelp;
    public static String SignatureWizard_Usage;
    public static String SignatureWizard_Usage2;
    public static String SignatureWizard_noKeysHint;
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
    public static String InputFileWizard_btnBrowse;
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
    public static String InputFileWizard_title;
    public static String InputFileWizard_header;

    // Show generated signature
    public static String SignaturResult_title;
    public static String SignaturResult_keyTitle;
    public static String SignaturResult_methodTitle;

    public static String SignaturResult_grpSignature;
    public static String SignaturResult_lengthSig;

    public static String SignaturResult_grpOption;
    public static String SignaturResult_octal;
    public static String SignaturResult_decimal;
    public static String SignaturResult_hex;
    public static String SignaturResult_hexDump;

    public static String SignaturResult_grpMessage;
    public static String SignaturResult_lengthMessage;

    public static String SignaturResult_btnClose;
    public static String SignaturResult_btnOpen;

    public static String SignaturResult_tblAdr;
    public static String SignaturResult_tblHex;
    public static String SignaturResult_tblAscii;

    public static String SignaturResult_editorDescripton;

    public static String SignaturResult_btnVerificationModels;

    // Input Key Wizard
    public static String InputKeyWizard_title;
    public static String InputKeyWizard_header;
    public static String InputKeyWizard_FileOpenDialog;
    public static String InputKeyWizard_WarningTitle;
    public static String InputKeyWizard_WarningMessageTooLarge;
    public static String InputKeyWizard_WarningMessageEmpty;
    public static String InputKeyWizard_rdoFromFile;
    public static String InputKeyWizard_rdoFromEditor;
    public static String InputKeyWizard_rdoFromKeyStore;

    // Input Key File Wizard
    public static String InputKeyFileWizard_btnBrowse;
    public static String InputKeyFileWizard_title;
    public static String InputKeyFileWizard_header;

    // Input Key Editor Wizard
    public static String InputKeyEditorWizard_title;
    public static String InputKeyEditorWizard_header;
    public static String InputKeyEditorWizard_Label;
    public static String EnterText;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }

}