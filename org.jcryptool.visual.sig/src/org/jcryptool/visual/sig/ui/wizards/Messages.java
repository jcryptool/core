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
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	
	private Messages() {
	}

}
