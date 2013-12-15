package org.jcryptool.visual.sigVerification;

import org.eclipse.osgi.util.NLS;

/**
 * Holds the messages for the signature visualization (for the main view)
 * 
 * @author Grebe/Wilfing/Huber
 */
public class Messages {
    // Name of the files that contain the string values
    private static final String BUNDLE_NAME = "org.jcryptool.visual.sigVerification.messages"; //$NON-NLS-1$
    // Create strings for all the values
    public static String SigVerComposite_grpSignatureGeneration;
    public static String SigVerComposite_lblHash;
    public static String SigVerComposite_lblSignature;
    public static String SigVerComposite_lblProgress;
    public static String SigVerComposite_tbtmNewItem_0;
    public static String SigVerComposite_tbtmNewItem_1;
    public static String SigVerComposite_tbtmNewItem_2;
    public static String SigVerComposite_tbtmNewItem_3;
    public static String SigVerComposite_txtDescriptionOfStep1;
    public static String SigVerComposite_txtDescriptionOfStep2;
    public static String SigVerComposite_txtDescriptionOfStep3;
    public static String SigVerComposite_txtDescriptionOfStep4;
    public static String SigVerComposite_btnSignature;
    public static String SigVerComposite_btnHash;
    public static String SigVerComposite_btnResult;
    public static String SigVerComposite_btnReset;
    public static String SigVerComposite_btnReturn;
    public static String SigVerComposite_description;
    public static String SigVerComposite_btnAddInput;
    public static String SigVerComposite_lblHeader;
    public static String SigVerComposite_MessageTitleReturn;
    public static String SigVerComposite_MessageTextReturn;
    public static String SigVerComposite_txtDescriptionOfStep4_Success;
    public static String SigVerComposite_menu;
    public static String SigVerComposite_btnDecrypt;
    public static String SigVerComposite_lblPubKey;
    public static String SigVerComposite_lblTitle;
    public static String ModelComposite_description;
    public static String ModelComposite_lblHeader;
    public static String ModelComposite_lblTitle;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }

}