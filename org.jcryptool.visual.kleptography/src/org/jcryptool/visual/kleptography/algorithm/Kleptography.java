// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.kleptography.algorithm;

/**
 * A parents class for kleptographic attacks. Currently only available
 * for a basic RSAMain implementation, although an improved RSAMain version,
 * a Diffie-Hellman, and possibly other key generation algorithms
 * could also be implemented in the future.
 * @author Patrick Vacek, Deutsche Bank, ex-Webster University and FH-Hannover
 */
public class Kleptography {

	public RSAMain rsa;
	public RSAAttack attack;
	public RSAFunctions functions;

	/**
	 * The constructor.
	 */
	public Kleptography() {
		functions = new RSAFunctions();
		rsa = new RSAMain(this);
		attack = new RSAAttack(this);
	}

	/**
	 * The disposer.
	 */
	public void dispose() {
	}

//	public void saveCipherText1() {
//		attack.setE1(rsa.getE());
//		attack.setN1(rsa.getN());
//		attack.setSavedCipherBytes1(rsa.getCipherBytes());
//		attack.setSavedCipherHex1(rsa.getCipherHex());
//	}

//	public void saveCipherText2() {
//		attack.setE2(rsa.getE());
//		attack.setN2(rsa.getN());
//		attack.setSavedCipherBytes2(rsa.getCipherBytes());
//		attack.setSavedCipherHex2(rsa.getCipherHex());
//	}
}