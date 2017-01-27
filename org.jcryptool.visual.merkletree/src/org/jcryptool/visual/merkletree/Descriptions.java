package org.jcryptool.visual.merkletree;

import org.eclipse.osgi.util.NLS;

/**
 * This class defines names of string constants that are the texts of the
 * plug-in; the actual texts are found in "messages_de.properties" and
 * "messages_en.properties"
 * 
 * @author Maximilian Lindpointner
 */
public class Descriptions extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.merkletree";

	public static String Platzhalter;

	// <-dropdown
	public static String CompositeDescriptionMerkleTree;
	public static String CompositeDescriptionXMSS;
	public static String CompositeDescriptionXMSS_MT;
	// >

	// <- Tabs
	public static String MerkleTreeTab_0;
	public static String MerkleTreeTab_1;
	public static String MerkleTreeTab_2;
	public static String MerkleTreeTab_3;
	public static String MerkleTreeTab_4;
	// >

	// <- MSG Box - wenn kein Key vorhanden
	public static String MerkleTree_Generation_Info;
	public static String UnsavedChanges;
	// >

	// <- BaumBeschriftung
	public static String ZestLabelProvider_0;
	public static String ZestLabelProvider_1;
	public static String ZestLabelProvider_2;
	public static String ZestLabelProvider_3;

	public static String ZestLabelProvider_7;
	public static String ZestLabelProvider_8;
	// >

	// <- Mouse over
	public static String ZestLabelProvider_4;
	// >

	// <- Ausgewählter Knoten TXT
	public static String ZestLabelProvider_5;
	public static String ZestLabelProvider_6;
	// >

	// <- KeyPair Descriptions
	public static String MerkleTreeKeyTab_0;
	public static String MerkleTreeKeyTab_1;
	public static String MerkleTreeKeyTab_2;
	public static String MerkleTreeKeyTab_3;
	public static String MerkleTreeKeyTab_4;
	public static String MerkleTreeKeyTab_5;
	public static String MerkleTreeKeyTab_6;

	public static String MerkleTreeKey_column_0;
	public static String MerkleTreeKey_column_1;
	public static String MerkleTreeKey_column_2;
	public static String MerkleTreeKey_Message;

	public static String MerkleTreeKey_1;
	public static String MerkleTreeKey_2;
	public static String MerkleTreeKey_3;
	public static String MerkleTreeKey_4;
	public static String MerkleTreeKey_5;

	public static String MerkleTreeSign_0;
	public static String MerkleTreeSign_1;
	public static String MerkleTreeSign_2;
	public static String MerkleTreeSign_4;
	public static String MerkleTreeSign_5;
	public static String MerkleTreeSign_6;
	public static String MerkleTreeSign_7;
	public static String MerkleTreeSign_8;

	public static String MerkleTreeVerify_0;
	public static String MerkleTreeVerify_1;
	public static String MerkleTreeVerify_2;
	public static String MerkleTreeVerify_3;
	public static String MerkleTreeVerify_4;
	public static String MerkleTreeVerify_5;
	public static String MerkleTreeVerify_6;
	public static String MerkleTreeVerify_7;
	public static String MerkleTreeVerify_8;
	public static String MerkleTreeVerify_9;
	public static String MerkleTreeVerify_10;
	public static String MerkleTreeVerify_11;

	public static String MerkleTree_Signature_Generation_Info;

	public static String Tab0_Head1;
	public static String Tab0_Head2;
	public static String Tab0_Head3;
	public static String Tab0_Head4;
	public static String Tab0_Head5;
	public static String Tab0_Button1;
	public static String Tab0_Button2;
	public static String Tab0_Button3;
	public static String Tab0_Lable1;
	public static String Tab0_Txt3;
	public static String Tab0_MT_1;
	public static String Tab0_MT_2;
	public static String Tab0_MT_3;
	public static String Tab0_MT_4;
	public static String Tab0_MT_5;
	
	public static String Tab1_Button_1;
	public static String Tab1_Button_2;

	public static String InteractiveSignature_Button_0;
	public static String InteractiveSignature_Button_1;
	public static String InteractiveSignature_Button_2;
	public static String InteractiveSignature_Button_3;
	public static String InteractiveSignature_Button_4;
	public static String InteractiveSignature_Button_5;
	public static String InteractiveSignature_1;
	public static String InteractiveSignature_2;
	public static String InteractiveSignature_3_1;
	public static String InteractiveSignature_3_2;
	public static String InteractiveSignature_4_1;
	public static String InteractiveSignature_4_2;
	public static String InteractiveSignature_5;
	public static String InteractiveSignature_6;
	public static String InteractiveSignature_7;
	public static String InteractiveSignature_8;
	public static String InteractiveSignature_9;
	public static String InteractiveSignature_10;

	public static class MSS {

		public static String Tab0_Head0;
		public static String Tab0_Txt0;
		public static String Tab0_Txt1;
		public static String Tab0_Txt2;

		public static String Tab1_Head0;
		public static String Tab1_Txt0;
		public static String Tab1_Txt1;

		public static String Tab2_Txt0;
	}

	public static class XMSS {

		public static String Tab0_Head0;
		public static String Tab0_Txt0;
		public static String Tab0_Txt1;
		public static String Tab0_Txt2;

		public static String Tab1_Head0;
		public static String Tab1_Txt0;
		public static String Tab1_Txt1;

		public static String Tab2_Txt0;
	}

	public static class XMSS_MT {

		public static String Tab0_Head0;
		public static String Tab0_Txt0;
		public static String Tab0_Txt1;
		public static String Tab0_Txt2;
		public static String Tab0_Txt3;

		public static String Tab1_Head0;
		public static String Tab1_Txt0;
		public static String Tab1_Txt1;
		public static String Tab2_Txt0;

	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME + ".descriptions", Descriptions.class);
		NLS.initializeMessages(BUNDLE_NAME + ".descriptionsMSS", Descriptions.MSS.class);
		NLS.initializeMessages(BUNDLE_NAME + ".descriptionsXMSS", Descriptions.XMSS.class);
		NLS.initializeMessages(BUNDLE_NAME + ".descriptionsXMSS_MT", Descriptions.XMSS_MT.class);
	}

	private Descriptions() {
	}

}
