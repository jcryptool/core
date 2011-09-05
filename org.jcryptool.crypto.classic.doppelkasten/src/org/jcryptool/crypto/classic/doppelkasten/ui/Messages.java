// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.doppelkasten.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.doppelkasten.ui.messages"; //$NON-NLS-1$
	public static String DoppelkastenWizard_classic;
	public static String DoppelkastenWizardPage_doublebox;
	public static String DoppelkastenWizardPage_enterfirst;
	public static String DoppelkastenWizardPage_enterkey;
	public static String DoppelkastenWizardPage_entersecond;
	public static String DoppelkastenWizardPage_firstkey;
	public static String DoppelkastenWizardPage_keys;
	public static String DoppelkastenWizardPage_secondkey;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
