// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer4;

import org.jcryptool.analysis.kegver.layer3.L3;
import org.jcryptool.analysis.kegver.layer5.L5;

/**
 * Separates business logic from user interface
 * @author hkh
 *
 */
public class L4 {

	public static String printString(String inString) {
		return L5.get().printString(inString);
	}

	public static void startL3() {
		L3.startKegverDummy();
	}
}
