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
package org.jcryptool.analysis.friedman.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jcryptool.analysis.friedman.ui.messages"; //$NON-NLS-1$
	public static String FriedmanGraphUI_coincidence;
	public static String FriedmanGraphUI_graph;
	public static String FriedmanGraphUI_openandselect;
	public static String FriedmanGraphUI_showastable;
	public static String FriedmanGraphUI_start;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
