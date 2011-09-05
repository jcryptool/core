// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3.unigenprotocol;

import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer3.Commitment;
import org.jcryptool.analysis.kegver.layer3.POK;
import org.jcryptool.analysis.kegver.layer3.U;

public class UnigenData {

	private Commitment aCommitment_Cv = null;
	private BigInteger u = null;
	private Commitment aCommitment_Co;;

	@Override
	public String toString(){
		return "nothing in UniGenData: empty";
	}

	public POK getPOK_1() {
		U.verbose(new Throwable(), "This would be a proof");
		return new POK();
	}

	public Commitment setCommitment_Cv(Commitment inCommitment_Cv) {
		this.aCommitment_Cv  = inCommitment_Cv;
		return this.getCommitment_Cv();
	}

	public Commitment getCommitment_Cv() {
		return this.aCommitment_Cv;
	}

	public BigInteger set_u(BigInteger in_u) {
		this.u = in_u;
		return this.get_u();
	}

	public BigInteger get_u() {
		U.verbose(new Throwable(), "UnigenData unimplemented yet.");
		return this.u;
	}

	public Commitment setCommitment_Co(Commitment inCommitment_Co) {
		this.aCommitment_Co = inCommitment_Co;
		return this.getCommitment_Co();
	}

	public Commitment getCommitment_Co() {
		return this.aCommitment_Co;
	}
}
