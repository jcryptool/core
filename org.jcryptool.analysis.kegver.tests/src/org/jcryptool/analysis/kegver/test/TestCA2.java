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

import org.jcryptool.analysis.kegver.layer3.CABehavior;
import org.jcryptool.analysis.kegver.layer3.KegverData;
import org.jcryptool.analysis.kegver.layer3.KegverGroup;
import org.jcryptool.analysis.kegver.layer3.RSAData;
import org.jcryptool.analysis.kegver.layer3.U;
import org.jcryptool.analysis.kegver.layer3.unigenprotocol.UnigenData;

public class TestCA2 implements CABehavior {

	private KegverData aKegVerData = null;;
	private KegverGroup aKegVerGroup = null;;

	/*
	 * Constructor
	 */

	public TestCA2(int in_k, BigInteger in_l, int in_m, int in_t, BigInteger in_e){
		final RSAData aRSAData = U.keygen(in_k, in_e, in_t);

		this.setKegVerData(this.calcKegVerData(aRSAData.get_p(), aRSAData.get_q(), in_k, in_l, in_m, in_t, in_e));

		this.setKegVerGroup(this.calcKegVerGroup(aRSAData.get_p(), aRSAData.get_q()));
	}

	/*
	 * Calculation
	 */

	private KegverGroup calcKegVerGroup(BigInteger in_p_, BigInteger in_q_) {
		// p - 1
		final BigInteger order = (U.TWO.multiply(in_p_)).multiply(in_q_);
		return new KegverGroup(order);
	}

	private KegverData calcKegVerData (BigInteger in_p_, BigInteger in_q_, int in_k, BigInteger in_l, int in_m, int in_t, BigInteger in_e) {
		//               P =  2      *        P'     +     1
		final BigInteger p = (U.TWO).multiply(in_p_).add(U.ONE);
		//               Q =  2      *        Q'     +     1
		final BigInteger q = (U.TWO).multiply(in_q_).add(U.ONE);

		final RSAData bRSAData = U.keygen(p, q, in_e);

		return new KegverData(in_k, in_l, in_m, in_t, bRSAData.get_N());
	}

	/*
	 * Implement
	 */

	public KegverData getKegverData(){
		return this.aKegVerData;
	}

	/*
	 * Getter and setter
	 */

	private KegverData setKegVerData(KegverData inKegVerData){
		this.aKegVerData = inKegVerData;
		return this.getKegverData();
	}

	private KegverGroup setKegVerGroup(KegverGroup inKegVerGroup) {
		this.aKegVerGroup = inKegVerGroup;
		return this.getKegVerGroup();
	}

	public KegverGroup getKegVerGroup() {
		return this.aKegVerGroup;
	}

	public UnigenData getUniGenData() {
		return null;
	}

	public String toString_() {
		return "not implemented yet";
	}

	public UnigenData getUnigenData() {
		return null;
	}

	public BigInteger calc_u() {
		return null;
	}

	public boolean verifyPOK_o() {
		return false;
	}

	public boolean caVerifiesPOK_z() {
		return false;
	}

	public boolean verifyPOK_Cp() {
		return false;
	}

	public boolean verifyPOK_Cq() {
		return false;
	}

	public boolean verifyPOK_N() {
		return false;
	}

	public boolean executeBlum_n() {
		return false;
	}
}