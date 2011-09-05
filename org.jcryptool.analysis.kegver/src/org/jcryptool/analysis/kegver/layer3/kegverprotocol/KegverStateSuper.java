// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3.kegverprotocol;


public abstract class KegverStateSuper {

	private KegverStateContext aKegVer = null;

	public KegverStateSuper(KegverStateContext inKegVer){
		this.setKegVer(inKegVer);
	}

	protected KegverStateContext getKegver() {
		return this.aKegVer;
	}

	final private KegverStateContext setKegVer(KegverStateContext inKegVer) {
		this.aKegVer = inKegVer;
		return this.getKegver();
	}
}
