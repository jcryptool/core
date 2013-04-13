package org.jcryptool.visual.sig;

import org.eclipse.osgi.util.NLS;
/**
 * Holds the messages for the signature visualization (for the main view)
 *
 */
public class Messages {
	//Name of the files that contain the string values
	private static final String BUNDLE_NAME = "org.jcryptool.visual.sig.messages";
	//Create strings for all the values
	public static String SigComposite_grpSignatureGeneration;
	public static String SigComposite_grpSignedDoc;
	public static String SigComposite_lblHash;
	public static String SigComposite_lblSignature;
	public static String SigComposite_lblProgress;
	public static String SigComposite_tbtmNewItem_0;
	public static String SigComposite_tbtmNewItem_1;
	public static String SigComposite_tbtmNewItem_2;
	public static String SigComposite_tbtmNewItem_3;
	public static String SigComposite_txtDescriptionOfStep1;
	public static String SigComposite_txtDescriptionOfStep2;
	public static String SigComposite_txtDescriptionOfStep3;
	public static String SigComposite_txtDescriptionOfStep4;
	public static String SigComposite_txtSignature;
	public static String SigComposite_btnSignature;
	public static String SigComposite_btnHash;
	public static String SigComposite_btnOpenInEditor;
	public static String SigComposite_btnReset;
	public static String SigComposite_description;
	public static String SigComposite_group_box_name;
	public static String SigComposite_btnDocumentTemp;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	
	private Messages() {
	}

}
