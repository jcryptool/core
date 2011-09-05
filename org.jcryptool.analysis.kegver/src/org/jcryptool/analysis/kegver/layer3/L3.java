// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3;

import org.jcryptool.analysis.kegver.layer3.kegverprotocol.KegverStateContext;

public class L3 {

	public static String startKegverDummy(){
		CABehavior inCA = new DummyCA();
		UserBehavior inUser = new DummyUser();
		KegverStateContext aKegverStateContext = new KegverStateContext(inCA, inUser);
		aKegverStateContext.setup();
		return null;
	}

}
