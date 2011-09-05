// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.xor.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.xor.ui.messages"; //$NON-NLS-1$
	public static String FilechooserComposite_browse;
	public static String FilechooserComposite_inputname_file;
	public static String XOR;
	public static String XorWizard_XOR;
	public static String XorWizardPage_inputname_keymethod;
	public static String XorWizardPage_keys;
	public static String XorWizardPage_manualkeylabel;
	public static String XorWizardPage_orders;
	public static String XorWizardPage_selectfileforvernam;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
