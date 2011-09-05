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

public class UserSends_Co extends UnigenStateSuper implements
		UnigenStateBehavior {

	public UserSends_Co(UnigenStateContext inUniGen) {
		super(inUniGen);
	}

	public void bothSetup() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userChecksPOK_1() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userAbortsPOK_1() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userSends_Cv() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caSends_u() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userChecks_u() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userAborts_u() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userSends_Co() {
		// Report
		U.verbose(new Throwable(), "entered");

		// Execute this state
		this.getUnigen().getUnigenData().setCommitment_Co(
				this.getUnigen().getUser().calcCommitment_Co());

		// Report
		U.verbose(new Throwable(),
				"CA: " + this.getUnigen().getCA().toString_() +
				", User: " + this.getUnigen().getUser().toString_() +
				", UnigenData: " + this.getUnigen().getUnigenData() +
				", POK_1: " + this.getUnigen().getUnigenData().toString() +
				", Commitment_Cv: " + this.getUnigen().getUnigenData().getCommitment_Cv().toString() +
				", u: " + this.getUnigen().getUnigenData().get_u() +
				", Commitment_Co: " + this.getUnigen().getUnigenData().getCommitment_Co().toString());
		// Trigger next state
		this.getUnigen().setState(UnigenStateContext.getUserExecutesPOK_o());
		this.getUnigen().userExecutesPOK_o();
	}

	public void userExecutesPOK_o() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caVerifiesPOK_o() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caAbortsPOK_o() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userExecutesPOK_z() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caVerifiesPOK_z() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caAbortsPOK_z() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void bothAreHappy() {
		U.verbose(new Throwable(), "Wrong State");
	}
}