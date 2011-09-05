// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.test;

import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer3.Commitment;
import org.junit.Test;


public class TestBuildingCommitment {

	@Test
	public void first(){
		int m = 1;
		BigInteger N = BigInteger.TEN; // Comming from CA
		BigInteger g = BigInteger.TEN; // Comming from CA
		BigInteger x = BigInteger.ONE; // Value to commit to
		BigInteger h = BigInteger.TEN; // Comming from CA
		Commitment.FujisajiOkamoto(m, N, g, x, h);
	}


}
