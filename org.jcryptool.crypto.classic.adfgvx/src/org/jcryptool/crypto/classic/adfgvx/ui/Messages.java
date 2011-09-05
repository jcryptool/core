// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.adfgvx.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.crypto.classic.adfgvx.ui.messages"; //$NON-NLS-1$
	public static String AdfgvxWizard_adfgvx;
	public static String AdfgvxWizardPage_adfgvx;
	public static String AdfgvxWizardPage_adfgvxmatrix;
	public static String AdfgvxWizardPage_adfgvxorder;
	public static String AdfgvxWizardPage_enterkeyword;
	public static String AdfgvxWizardPage_enterTranspKey;
	public static String AdfgvxWizardPage_inputNameSubstitution;
	public static String AdfgvxWizardPage_inputNameTransposition;
	public static String AdfgvxWizardPage_onlyoccuronce;
	public static String AdfgvxWizardPage_step1;
	public static String AdfgvxWizardPage_step2;
	public static String AdfgvxWizardPage_substitutionHint;
	public static String AdfgvxWizardPage_transpositionHint;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
