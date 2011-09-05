// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3;

import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer3.unigenprotocol.UnigenData;

public class DummyCA implements CABehavior {

	public KegverData getKegverData() {
		U.verbose(new Throwable(), "CA not implemented yet");
		return new KegverData(1, BigInteger.TEN, 2, 3, BigInteger.ONE);
	}

	public UnigenData getUniGenData() {
		U.verbose(new Throwable(), "CA not implemented yet");
		return null;
	}

	public KegverGroup getKegVerGroup() {
		U.verbose(new Throwable(), "CA not implemented yet");
		return null;
	}

	public String toString_() {
		return "CA_" + this.hashCode();
	}

	public UnigenData getUnigenData() {
		U.verbose(new Throwable(), "CA not implemented yet");
		return new UnigenData();
	}

	public BigInteger calc_u() {
		U.verbose(new Throwable(), "CA not implemented yet");
		return BigInteger.ZERO;
	}

	public boolean verifyPOK_o() {
		U.verbose(new Throwable(), "CA not implemented yet");
		return false;
	}

	public boolean caVerifiesPOK_z() {
		U.verbose(new Throwable(), "Ca not implemented yet");
		return false;
	}

	public boolean verifyPOK_Cp() {
		U.verbose(new Throwable(), "CA not implemented yet");
		return false;
	}

	public boolean verifyPOK_Cq() {
		U.verbose(new Throwable(), "CA not implemented yet");
		return false;
	}

	public boolean verifyPOK_N() {
		U.verbose(new Throwable(), "CA not implemented yet");
		return false;
	}

	public boolean executeBlum_n() {
		U.verbose(new Throwable(), "CA not implemented yet");
		return false;
	}
}