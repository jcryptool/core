package org.jcryptool.visual.merkletree;

import org.eclipse.osgi.util.NLS;

/**
 * This class defines names of string constants that are the texts of the
 * plug-in; the actual texts are found in "messages_de.properties" and
 * "messages_en.properties"
 * 
 * @author Kevin M�hlb�ck
 */
public class Descriptions extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.merkletree.descriptions";

	// public static String WizardPageDescriptionKey;

	// public static String WizardPageDescriptionPlain;

	public static String CompositeDescriptionMerkleTree;

	public static String CompositeDescriptionXMSS;
	
	public static String CompositeDescriptionXMSS_MT;


	public static String MerkleTreeView_0;
	public static String MerkleTreeView_1; 
	public static String MerkleTreeView_2; 
	public static String MerkleTreeView_3;

	public static String MerkleTree_Generation_Info;
	public static String ZestLabelProvider_0;
	public static String ZestLabelProvider_1;
	public static String ZestLabelProvider_2;
	public static String ZestLabelProvider_3;
	public static String ZestLabelProvider_4;
	public static String ZestLabelProvider_5;
	public static String ZestLabelProvider_6;
	public static String ZestLabelProvider_7;
	public static String TestLabelProvider_5;
	public static String TestLabelProvider_6;

	public static String MerkleTreeKey_Label_1;
	public static String MerkleTreeKey_column_0;
	public static String MerkleTreeKey_column_1;
	public static String MerkleTreeKey_column_2;
	public static String MerkleTreeKey_Message;
	
	public static String MerkleTreeSign_0;
	public static String MerkleTreeSign_1;
	public static String MerkleTreeSign_2;
	public static String MerkleTreeSign_3;
	public static String MerkleTreeSign_4;
	public static String MerkleTreeSign_5;
	public static String MerkleTreeSign_6;
	public static String MerkleTreeSign_7;
	
	public static String MerkleTreeVerify_0;
	public static String MerkleTreeVerify_1;
	public static String MerkleTreeVerify_2;
	public static String MerkleTreeVerify_3;
	public static String MerkleTreeVerify_4;
	
	public static String MerkleTree_Signature_Generation_Info;

	public static class MSS{

		public static String Tab0_Head0;
		public static String Tab0_Head1;
		public static String Tab0_Head2;
		
		public static String Tab0_Txt0;
		public static String Tab0_Txt2;
		
		public static String Tab0_Button1;
		public static String Tab0_Button2;

	}
	
	public static class XMSS{
		

		public static String Tab0_Head0;
		public static String Tab0_Head1;
		public static String Tab0_Head2;
		
		public static String Tab0_Txt0;
		public static String Tab0_Txt1;
		public static String Tab0_Txt2;
		
		public static String Tab0_Button0;
		public static String Tab0_Button1;
		public static String Tab0_Button2;

	}
	
	public static class XMSS_MT{
		

		public static String Tab0_Head0;
		public static String Tab0_Head1;
		public static String Tab0_Head2;
		
		public static String Tab0_Txt0;
		public static String Tab0_Txt1;
		public static String Tab0_Txt2;
		
		public static String Tab0_Button0;
		public static String Tab0_Button1;
		public static String Tab0_Button2;

	}


	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Descriptions.class);
		NLS.initializeMessages("org.jcryptool.visual.merkletree.descriptionsMSS", Descriptions.MSS.class);
		NLS.initializeMessages("org.jcryptool.visual.merkletree.descriptionsXMSS", Descriptions.XMSS.class);
	}

	private Descriptions() {
	}

}
