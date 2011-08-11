//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.model.ui.wizard;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.model.ui.wizard.messages"; //$NON-NLS-1$
	public static String AbstractClassicCryptoPage_alphabet_input_name;
	public static String AbstractClassicCryptoPage_alphabetcontent_balloon_title;
	public static String AbstractClassicCryptoPage_applyTransformLabel;
	public static String AbstractClassicCryptoPage_clicktoclose;
	public static String AbstractClassicCryptoPage_filter_input_name;
	public static String AbstractClassicCryptoPage_genericNormalMsg;
    public static String AbstractClassicCryptoPage_key_input_name;
	public static String AbstractClassicCryptoPage_keyToolTip;
	public static String AbstractClassicCryptoPage_operation_input_name;
	public static String AbstractClassicCryptoPage_preOpTransformLabel;
	public static String AbstractClassicCryptoPage_showSelectedAlpha;
	public static String AbstractClassicCryptoPage_transform_name;
	public static String AbstractClassicTransformationPage_pageMessage;
	public static String AbstractClassicTransformationPage_pageTitle;
	public static String WidgetBubbleUIInputHandler_balloon_errorhandling_title;
	public static String WidgetBubbleUIInputHandler_inputreset_reason_balloon_message;
	public static String WizardPage_alpha;
	public static String WizardPage_decrypt;
	public static String WizardPage_encrypt;
	public static String WizardPage_enterkey;
	public static String WizardPage_filterchars;
	public static String WizardPage_key;
	public static String WizardPage_operation;
	public static String WizardPage_selectalpha;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}