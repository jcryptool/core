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

/**
 * Should interact with a KegVer.
 *
 * @author hkh
 *
 */
public class Soundness {

	public static boolean isKegverSound(double inMusound){

		// Test inputs

		// Setup

		boolean isSound = false;

		// Calculate

		if (inMusound >= Soundness.calcSoundness()){
			isSound = true;
		}

		return isSound;
	}

	/**
	 * Soundness of KEGVER is represented by the highest result of the following devision:
	 *
	 * probability distribution induced by the output of KEGVER with fixed key-sized parameter
	 * k, soundness parameters l and t and security parameter m over executions accepted by an honest CA,
	 * when the user is represented by an algorithm.
	 *
	 * over
	 *
	 * probability distribution associated with PK induced by keygen over the set[1] of public
	 * keys specified by key-sized parameter k.
	 * Note: [1]Juels and Guajardo do restrict the set to "all such possible outputs PK of keygen"
	 *
	 * @return
	 */
	public static double calcSoundness() {
		double QofA_klmt_of_PK = 1;
		double P_keygen_produces_PK = Soundness.getP_keygen_produces_PK_with_k();
		double Set_of_all_possible_PK_with_k = Soundness.getAbsolute_of_set_of_all_PK_with_k_keygen_produces();
		double P_kt_of_PK = P_keygen_produces_PK / Set_of_all_possible_PK_with_k;

		return QofA_klmt_of_PK / (P_kt_of_PK );
	}

	public static double getAbsolute_of_set_of_all_PK_with_k_keygen_produces() {
		return 0;
	}

	public static double getP_keygen_produces_PK_with_k() {
		return 0;
	}
}
