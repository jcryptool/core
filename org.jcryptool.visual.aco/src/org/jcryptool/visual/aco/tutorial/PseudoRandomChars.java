// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.tutorial;

public class PseudoRandomChars {

	private char[] chars;
	private int pos = 0;

	public PseudoRandomChars(int numberOfChars) {
		chars = new char[numberOfChars];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) (int) (Math.random() * 25.9999 + 65);
		}
	}

	public char getRandomChar() {
		char ret = chars[pos];
		pos = (pos + 1) % chars.length;
		return ret;
	}
}
