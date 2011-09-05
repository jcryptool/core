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

import org.jcryptool.analysis.kegver.layer3.U;

public class CaVerifiesPOK_z extends UnigenStateSuper implements
		UnigenStateBehavior {

	public CaVerifiesPOK_z(UnigenStateContext inUniGen) {
		super(inUniGen);
	}

	public void bothSetup() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userChecksPOK_1() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userAbortsPOK_1() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userSends_Cv() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caSends_u() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userChecks_u() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userAborts_u() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userSends_Co() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userExecutesPOK_o() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caVerifiesPOK_o() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caAbortsPOK_o() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userExecutesPOK_z() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caVerifiesPOK_z() {
		// Report
		U.verbose(new Throwable(), "entered");

		// Execute this state
		boolean isPOK_zVerified = this.getUnigen().getCA().caVerifiesPOK_z();

		// Report
		U.verbose(new Throwable(),
				"CA: " + this.getUnigen().getCA().toString_() +
				", User: " + this.getUnigen().getUser().toString_() +
				", UnigenData: " + this.getUnigen().getUnigenData() +
				", POK_1: " + this.getUnigen().getUnigenData().toString() +
				", Commitment_Cv: " + this.getUnigen().getUnigenData().getCommitment_Cv().toString() +
				", u: " + this.getUnigen().getUnigenData().get_u() +
				", Commitment_Co: " + this.getUnigen().getUnigenData().getCommitment_Co().toString() +
				", isPOK_zVerified: " + isPOK_zVerified);

		U.verbose(new Throwable(), "Assume that worked...");
		isPOK_zVerified = true;

		// Trigger next state
		if(isPOK_zVerified){
			this.getUnigen().setState(UnigenStateContext.getBothAreHappy());
			this.getUnigen().bothAreHappy();
		} else {
			this.getUnigen().setState(UnigenStateContext.getCaAbortsPOK_z());
			this.getUnigen().caAbortsPOK_z();
		}
	}

	public void caAbortsPOK_z() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void bothAreHappy() {
		U.verbose(new Throwable(), "Wrong state");
	}
}