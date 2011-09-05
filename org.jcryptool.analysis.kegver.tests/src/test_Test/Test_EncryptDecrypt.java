// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package test_Test;

import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer2.IO;
import org.junit.Test;

import de.uni_siegen.hiek11.RSA42;
import de.uni_siegen.hiek11.RSA_JuGu02;


public class Test_EncryptDecrypt {

	public static RSA42 aAlice = null;
	@Test
	public void test(){
		int k = 32;
		BigInteger e = BigInteger.valueOf(65537);
		aAlice = new RSA_JuGu02(k, e);
		aAlice.create();

		int mK = 32;
		BigInteger message = aAlice.getPrime(mK);
		BigInteger[] publicKey = aAlice.getPublicKey();

		System.out.println("message: " + message);
		BigInteger aCipher = this.encrypt(message, publicKey);
		System.out.println("cipher: " + aCipher);
		System.out.println("message: " + this.decrypt(aCipher));

		System.out.println("message: " + message);
		aCipher = this.encrypt_old(message, publicKey);
		System.out.println("cipher: " + aCipher);
		System.out.println("message: " + this.decrypt_old(aCipher));

	}



	private BigInteger encrypt(BigInteger inM, BigInteger[] inPK) {
//		System.out.println("inN: " + inPK[0]);
//		System.out.println("inE: " + inPK[1]);
		return inM.modPow(aAlice.getE(), aAlice.getN());
	}

	private BigInteger decrypt(BigInteger inC) {
//		System.out.println("d: " + this.getD());
//		System.out.println("N: " + this.getN());
		return inC.modPow(aAlice.getD(), aAlice.getN());
	}

	private static final IO mIO = IO.useFactory("TESTTET");

	public BigInteger encrypt_old(BigInteger inMessage, BigInteger[] inE) {
		BigInteger bi = null;
		mIO.writeln(System.currentTimeMillis() + " inMessage.compareTo(inE[0]) > 0" + (inMessage.compareTo(inE[0]) > 0));
		if (inMessage.compareTo(inE[0]) > 0) {
			bi = inE[0].add(this.encrypt(inMessage.subtract(inE[0]), inE));

		} else {
			bi = inMessage.modPow(inE[1], inE[0]);
		}
		return bi;
	}

	/**
	 * Note: Iteration in use.
	 */
	public BigInteger decrypt_old(BigInteger inCipher) {
		if (inCipher.compareTo(this.getN()) > 0) {
			return this.getN()
					.add(this.decrypt(inCipher.subtract(this.getN())));
		} else {
			// hook
			return inCipher.modPow(this.getD(), this.getN());
		}
	}

	private BigInteger getD() {
		return aAlice.getD();
	}

	private BigInteger getN() {
		return aAlice.getN();
	}
}