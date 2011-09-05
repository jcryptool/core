// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package de.uni_siegen.hiek11;

import java.math.BigInteger;

public interface RSAable {

	public BigInteger calcD();

	public BigInteger calcN();

	public BigInteger chooseE();

	public BigInteger decrypt(BigInteger inCipher);

	public BigInteger encrypt(BigInteger inMessage, BigInteger[] inPublicKey);

	public BigInteger findPrimeP();

	public BigInteger findPrimeQ();

}
