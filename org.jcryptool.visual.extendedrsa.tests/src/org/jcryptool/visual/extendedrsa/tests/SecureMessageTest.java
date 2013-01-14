//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----

package org.jcryptool.visual.extendedrsa.tests;

import org.jcryptool.visual.extendedrsa.SecureMessage;
import org.junit.Test;

/**
 * test for the class "SecureMessage.java"
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class SecureMessageTest {

	@Test(expected = IllegalArgumentException.class)
	public void testSecureMessage_null1() {
		SecureMessage message = new SecureMessage(null, 0, null, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSecureMessage_null2() {
		SecureMessage message = new SecureMessage("asdf", 0, null, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSecureMessage_null3() {
		SecureMessage message = new SecureMessage("asdf", 1, null, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSecureMessage_null4() {
		SecureMessage message = new SecureMessage("asdf", 1, "sender", null, null);
	}
}
