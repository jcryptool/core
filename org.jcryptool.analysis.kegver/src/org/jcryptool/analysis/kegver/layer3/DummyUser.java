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

public class DummyUser implements UserBehavior {

	public String toString_() {
		return "User_" + this.hashCode();
	}

	public Commitment calcCommitment_Cv() {
		U.verbose(new Throwable(), "User not implemented yet");
		return new Commitment();
	}

	public boolean check_u(BigInteger get_u) {
		U.verbose(new Throwable(), "User not implemented yet");
		return false;
	}

	public Commitment calcCommitment_Co() {
		U.verbose(new Throwable(), "User not implemented yet");
		return new Commitment();
	}

	public boolean executePOK_o() {
		U.verbose(new Throwable(), "User not implemented yet");
		return false;
	}

	public boolean executePOK_z() {
		U.verbose(new Throwable(), "User not implemented yet");
		return false;
	}

	public boolean generate_p() {
		U.verbose(new Throwable(), "User not implemented yet");
		return false;
	}

	public boolean generate_q() {
		U.verbose(new Throwable(), "User not implemented yet");
		return false;
	}

	public Commitment calcCommitment_Cp() {
		U.verbose(new Throwable(), "User not implemented yet");
		return new Commitment();
	}

	public Commitment calcCommitment_Cq() {
		U.verbose(new Throwable(), "User not implemented yet");
		return new Commitment();
	}

	public BigInteger calc_N() {
		U.verbose(new Throwable(), "User not implemented yet");
		return BigInteger.ONE;
	}

	public boolean executeBlum_n() {
		U.verbose(new Throwable(), "User not implemented yet");
		return false;
	}

	public boolean checkPOK_1() {
		U.verbose(new Throwable(), "User not implemented yet");
		return false;
	}
}
