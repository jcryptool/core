// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3.unigenprotocol;

public class UnigenStateSuper {

	private UnigenStateContext aUnigen = null;

	public UnigenStateSuper(UnigenStateContext inUnigen){
		this.setUnigen(inUnigen);
	}

	protected UnigenStateContext getUnigen() {
		return this.aUnigen;
	}

	final private UnigenStateContext setUnigen(UnigenStateContext inUnigen) {
		this.aUnigen = inUnigen;
		return this.getUnigen();
	}
}