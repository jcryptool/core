// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.perf;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.jcryptool.analysis.kegver.layer2.IO;
import org.junit.Test;

import de.uni_siegen.hiek11.RSA42;
import de.uni_siegen.hiek11.RSA_JCT;



public class Perf_RSA_JCT_JuGu02 {

	/*
	 * Performance
	 */

	private static final int messageK = 1024;
	private static final int startK = 512;
	private static final int maxK = 512;
	private static final int iteration = 10;

	@Test
	public void test_performance_JCT(){
		// Setup
		IO aIO = null;
		RSA42 aAlice = null;
		SecureRandom aSecureRandom = new SecureRandom();
		// Execute RSA_JCT
		aIO = IO.useFactory("Perf_RSA_JCT");
		aIO.writeln("Performing tests on RSA_JCT");
		aIO.writeln("k;findP;findQ;calcN;chooseE;calcD;get message; encrypt message;decrypt message");
		for ( int i = startK ; i <= maxK ; i = i*i ){
			aAlice = new RSA_JCT(i, aSecureRandom);
			System.out.println(aIO.bufferln(this.performerStats(aAlice, i, iteration)));
		}
		aIO.close();
	}

//	@Test
//	public void test_performance_JuGu02(){
//		// Setup
//		IO aIO = null;
//		RSA42 aAlice = null;
//		SecureRandom aSecureRandom = new SecureRandom();
//		// RSA_JuGu02
//		aAlice = null;
//		aIO = IO.useFactory("Perf_RSA_JuGu02");
//		aIO.writeln("Performing tests on RSA_JuGu02");
//		aIO.writeln("k;findP;findQ;calcN;chooseE;calcD;get message; encrypt message;decrypt message");
//		for ( int i = startK; i <= maxK ; i = i*i ){
//			aAlice = new RSA_JuGu02(i, aSecureRandom);
//			System.out.println(aIO.bufferln(this.performerStats(aAlice, i, iteration)));
//		}
//		aIO.close();
//	}

	private static StringBuilder theSB = null;
	private static double[] theDOUBLES = new double[18];

	private String performerStats(RSA42 aAlice, int k, int inIteration){
		// Setup
		theSB = new StringBuilder();
		theDOUBLES[0] = aAlice.getK();
		// Execute
		for ( int i = 0 ; i < inIteration ; i++ ){
			this.performer(aAlice);
			theDOUBLES[10] += theDOUBLES[2];		//Time needed for findP;
			theDOUBLES[11] += theDOUBLES[3];		//Time needed for findQ;
			theDOUBLES[12] += theDOUBLES[4];		//Time needed for calcN;
			theDOUBLES[13] += theDOUBLES[5];		//Time needed for chooseE;
			theDOUBLES[14] += theDOUBLES[6];		//Time needed for calcD;
			theDOUBLES[15] += theDOUBLES[7];		//Time needed for getting message;
			theDOUBLES[16] += theDOUBLES[8];		//Time needed for encrypting message;
			theDOUBLES[17] += theDOUBLES[9];		//Time needed for decrypting message;
		}
		theSB.append(theDOUBLES[0] + ";" );
		theSB.append(theDOUBLES[10] / inIteration + ";"  );			//Average time needed for findP;
		theSB.append(theDOUBLES[11] / inIteration + ";"  );			//Average time needed for findQ;
		theSB.append(theDOUBLES[12] / inIteration + ";"  );			//Average time needed for calcN;
		theSB.append(theDOUBLES[13] / inIteration + ";"  );			//Average time needed for chooseE;
		theSB.append(theDOUBLES[14] / inIteration + ";"  );			//Average time needed for calcD;
		theSB.append(theDOUBLES[15] / inIteration + ";"  );			//Average time needed for getting message;
		theSB.append(theDOUBLES[16] / inIteration + ";"  );			//Average time needed for encrypting message;
		theSB.append(theDOUBLES[17] / inIteration + ";"  );			//Average time needed for decrypting message;
		// Return
		return theSB.toString();
	}

	private double[] performer(RSA42 aAlice){
		// Execute		// Perform Keygen
		Perf_RSA_JCT_JuGu02.measure();									//Setup measurement;
		aAlice.findPrimeP();
		theDOUBLES[2] = Perf_RSA_JCT_JuGu02.measure();				 	//Time needed for findP;
		aAlice.findPrimeQ();
		theDOUBLES[3] = Perf_RSA_JCT_JuGu02.measure();				 	//Time needed for findQ;
		aAlice.calcN();
		theDOUBLES[4] = Perf_RSA_JCT_JuGu02.measure();				 	//Time needed for calcN;
		aAlice.chooseE();
		theDOUBLES[5] = Perf_RSA_JCT_JuGu02.measure();				 	//Time needed for chooseE;
		aAlice.calcD();
		theDOUBLES[6] = Perf_RSA_JCT_JuGu02.measure();				 	//Time needed for calcD;

		// Execute		// Perform cipher and back
		Perf_RSA_JCT_JuGu02.measure();									//Setup measurement
		BigInteger message = aAlice.getPrime(Perf_RSA_JCT_JuGu02.messageK);
		theDOUBLES[7] = Perf_RSA_JCT_JuGu02.measure();				 	//Time needed for getting message;
		BigInteger cipher = aAlice.encrypt(message, aAlice.getPublicKey());
		theDOUBLES[8] = Perf_RSA_JCT_JuGu02.measure();
		assertEquals(message, aAlice.decrypt(cipher));
		theDOUBLES[9] = Perf_RSA_JCT_JuGu02.measure();				 	//Time needed for decrypting message;
		//Return
		return theDOUBLES;
	}

	private static long theT0 = 0;
	private static long temp = 0;

	private static long measure(){
		temp = System.currentTimeMillis();
		long measure = temp - theT0;
		theT0 = System.currentTimeMillis();
		return measure;
	}
}