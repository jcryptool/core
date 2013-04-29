package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.osgi.util.NLS;

/**
 * Holds the messages for the signature visualization (for the 2 wizards)
 *
 */
public class Messages {
	//Name of the files that contain the string values
	private static final String BUNDLE_NAME = "org.jcryptool.visual.sig.ui.wizards.messages";
	//Create strings for all the values
	//Group box 1 text
	public static String HashWizard_grpHashes;
	//Group box 2 text
	public static String HashWizard_grpDescription;
	//The Texts for the descriptions of the methods
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
	//SignatureWizard
	//Group box 1 text
	public static String SignatureWizard_grpSignatures;
	//Group box 2 text
	public static String SignatureWizard_grpDescription;
	public static String SignatureWizard_rdomd5_description;
	public static String SignatureWizard_rdosha1_description;
	public static String SignatureWizard_rdosha256_description;
	public static String SignatureWizard_rdosha384_description;
	public static String SignatureWizard_rdosha512_description;
	public static String SignatureWizard_rdomd5;
	public static String SignatureWizard_rdosha1;
	public static String SignatureWizard_rdosha256;
	public static String SignatureWizard_rdosha384;
	public static String SignatureWizard_rdosha512;
	public static String SignatureWizard_header;
	public static String HashWizard_header;
	public static String HashWizard_WindowTitle;
	public static String SignatureWizard_WindowTitle;
	public static String InputWizard_WindowTitle;
	public static String InputEditorWizard_WindowTitle;
	public static String InputFileWizard_WindowTitle;
	
	//InputWizard
	//Page 1
	public static String InputWizard_title;
	public static String InputWizard_header;
	public static String InputWizard_rdoFromFile;
	public static String InputWizard_rdoFromEditor;
	//Page 2
	public static String InputFileWizard_title;
	public static String InputFileWizard_header;
	//Page 3
	public static String InputEditorWizard_title;
	public static String InputEditorWizard_header;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	
	private Messages() {
	}

}
