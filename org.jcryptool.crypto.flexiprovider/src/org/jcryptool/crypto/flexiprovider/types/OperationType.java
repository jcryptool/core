//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.types;

public enum OperationType {

	ENCRYPT("Encrypt"), //$NON-NLS-1$
	DECRYPT("Decrypt"), //$NON-NLS-1$
	SIGN("Sign"), //$NON-NLS-1$
	VERIFY("Verify"), //$NON-NLS-1$
	UNKNOWN("Unknown"); //$NON-NLS-1$

	public static OperationType getTypeForName(String name) {
		if (name.equals(ENCRYPT.getTypeName())) {
			return ENCRYPT;
		} else if (name.equals(DECRYPT.getTypeName())) {
			return DECRYPT;
		} else if (name.equals(SIGN.getTypeName())) {
			return SIGN;
		} else if (name.equals(VERIFY.getTypeName())) {
			return VERIFY;
		}
		else return UNKNOWN;
	}
	
	private String name;

	private OperationType(String name) {
		this.name = name;
	}

	public String getTypeName() {
		return name;
	}

}
