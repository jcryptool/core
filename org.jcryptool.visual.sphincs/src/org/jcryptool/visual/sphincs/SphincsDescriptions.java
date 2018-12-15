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
package org.jcryptool.visual.sphincs;

import org.eclipse.osgi.util.NLS;

/**
 * This class defines names of string constants that are the texts of the
 * plug-in; the actual texts are found in "messages_de.properties" and
 * "messages_en.properties"
 * 
 * @author Philipp Guggenberger
 * @author Klaus Kaiser
 */
public class SphincsDescriptions extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.visual.sphincs";

	// Tab descriptions
	public static String SphincsTab_0;
	public static String SphincsTab_1;
	public static String SphincsTab_2;

	 // Title description
	public static String SphincsDescription_titleBox;
	public static String SphincsDescription_titleDesc;
	
	// Switching Tabs messages/ state messages
	public static String SignatureMessageBoxText;
	public static String SphincsTree_Generation_Info;
	public static String NoKeyText;
	public static String Info;

	
	/**
	 * Sphincs Tab "Bitmasks and Key Generation"
	 */
	// Group Boxes
	public static String SphincsDescription_grp_seed;
	public static String SphincsDescription_grp_bitmask;
	public static String SphincsDescription_grp_key;

	// Label Information
	public static String SphincsDescription_lbl_information;
	public static String SphincsDescription_keyInfo1;
	public static String SphincsDescription_keyInfo2;
	public static String SphincsDescription_keyInfo3;

	public static String SphincsDescription_bracket;
	public static String SphincsDescription_bytes;

	public static String SphincsDescription_btn_Generation;
	public static String SphincsDescription_btn_renewKey;

	
	/**
	 * Sphincs tab "Sign and Verify"
	 */
	// Group boxes
	public static String SphincsSign_Group_0;
	public static String SphincsSign_Group_1;

	// Label
	public static String SphincsSign_Label_0;
	public static String SphincsSign_Label_1;

	// Text
	public static String SphincsSign_Text_0;

	// Button
	public static String SphincsSign_Button_0;
	public static String SphincsSign_Button_1;
	public static String SphincsSign_Button_2;
	public static String SphincsSign_Button_3;

	// Verify status/button
	public static String SphincsVerify_Success;
	public static String SphincsVerify_Fail;
	public static String SphincsVerify_Button_0;
	
	
	/**
	 * Sphincs tab "Overview"
	 */
	// Label + Text
	public static String SphincsDecription_Label_0;
	public static String SphincsDecription_Text_0;
	public static String SphincsDecription_Text_1;

	// Button
	public static String SphincsDecription_Button_1;
	public static String SphincsDecription_Button_2;

	/**
	 * Tree stuff
	 */
	public static String ZestLabelProvider_0;
	public static String ZestLabelProvider_1;
	public static String ZestLabelProvider_2;
	public static String ZestLabelProvider_3;

	public static String ZestLabelProvider_7;
	public static String ZestLabelProvider_8;
	public static String ZestLabelProvider_9;

	// Mouse over
	public static String ZestLabelProvider_4;

	// selected node
	public static String ZestLabelProvider_5;
	public static String ZestLabelProvider_6;
	

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME + ".descriptions", SphincsDescriptions.class);

	}

	private SphincsDescriptions() {
	}

}
