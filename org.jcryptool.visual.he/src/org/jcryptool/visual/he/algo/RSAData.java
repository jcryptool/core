// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.algo;

import org.jcryptool.visual.he.rsa.Action;

/**
 * Data set holding data in the homomorphic visualization of the RSA crypto system,
 * extends the original one by Michael Gaber
 * @author Coen Ramaekers
 *
 */
public class RSAData extends org.jcryptool.visual.he.rsa.RSAData {
	/** Holds the operation number */
	private String homomorphOperation;

	public RSAData(Action action) {
		super(action);
	}

	public final void setOperation(String homomorphOperation) {
		this.homomorphOperation = homomorphOperation;
	}

	public final String getOperation() {
		return this.homomorphOperation;
	}
}
