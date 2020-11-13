//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.friedman.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.analysis.friedman.ui.messages"; //$NON-NLS-1$
	public static String FriedmanGraphUI_0;
	public static String FriedmanGraphUI_1;
	public static String FriedmanGraphUI_2;
	public static String FriedmanGraphUI_coincidence;
	public static String FriedmanGraphUI_graph;
	public static String FriedmanGraphUI_openandselect;
	public static String FriedmanGraphUI_showastable;
	public static String FriedmanGraphUI_start;
	public static String FriedmanGraphUI_warning;
	public static String FriedmanGraphUI_warning_text;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
