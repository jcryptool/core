// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.logging.preferences.pages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.core.logging.preferences.pages.messages"; //$NON-NLS-1$
	public static String leglevel_error;
	public static String LoggingPage_0;
	public static String loglevel;
	public static String loglevel_error_description;
	public static String loglevel_info;
	public static String loglevel_info_description;
	public static String loglevel_warn;
	public static String loglevel_warn_description;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
