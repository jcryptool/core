//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.ui.textblockloader;

public enum Repr {
	DECIMAL(true), BINARY(true), HEX(true), STRING(false);
	
	public static Repr[] ALL = new Repr[]{STRING, DECIMAL, HEX, BINARY};
	public static Repr[] ALLNUM = new Repr[]{DECIMAL, HEX, BINARY};
	
	private boolean numeric;

	private Repr(boolean numeric) {
		this.numeric = numeric;
	}
	
	public boolean isNumeric() {
		return numeric;
	}
	
	public String numberToString(Integer number) {
		if(this.isNumeric()) {
			if(this == DECIMAL) {
				return number.toString(); 
			} else if(this == BINARY) {
				return Integer.toBinaryString(number);
			} else if(this == HEX) {
				return Integer.toHexString(number);
			} else {
				throw new RuntimeException("unknown numeric representation"); //$NON-NLS-1$
			}
		} else {
			throw new RuntimeException("String conversion not supported here"); //$NON-NLS-1$
		}
	}
	
	public Integer getRadix() {
		if(this.isNumeric()) {
			if(this == DECIMAL) {
				return 10; 
			} else if(this == BINARY) {
				return 2;
			} else if(this == HEX) {
				return 16;
			} else {
				throw new RuntimeException("unknown numeric representation"); //$NON-NLS-1$
			}
		} else {
			throw new RuntimeException("radix not supported here"); //$NON-NLS-1$
		}
	}
}