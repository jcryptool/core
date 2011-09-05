// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3.kegverprotocol;

import org.jcryptool.analysis.kegver.layer3.U;

public class CaVerifiesPOK_CpState extends KegverStateSuper implements KegverStateBehavior {

	public CaVerifiesPOK_CpState(KegverStateContext inKegVer) {
		super(inKegVer);
	}

	public void setup() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void bothUnigen_r() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caAbortsUnigen_r() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userAbortsUnigen_r() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void bothUnigen_s() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caAbortsUnigen_s() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userAbortsUnigen_s() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userGenerates_p() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userAborts_p() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userGenerates_q() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userAborts_q() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userSends_Cp() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caVerifiesPOK_Cp() {

		// Report
		U.verbose(new Throwable(), "entered");

		// Execute this state
		boolean isPOK_CpVerified = this.getKegver().getCA().verifyPOK_Cp();

		// Report
		U.verbose(new Throwable(),
				"CA: " + this.getKegver().getCA().toString_() +
				", User: " + this.getKegver().getUser().toString_() +
				", KegverData: " + this.getKegver().getKegverData() +
				", Commitment_Cp: " + this.getKegver().getKegverData().getCommitment_Cp() +
				", isPOK_CpVerified: " + isPOK_CpVerified);
		U.verbose(new Throwable(), "Assume isPOK_CpVerified was true");
		isPOK_CpVerified = true;

		// Trigger next state
		if (isPOK_CpVerified){
			this.getKegver().setState(KegverStateContext.getUserSends_Cq());
			this.getKegver().userSends_Cq();
		} else {
			this.getKegver().setState(KegverStateContext.getCaAborts_Cp());
			this.getKegver().caAborts_Cp();
		}

	}

	public void caAborts_Cp() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userSends_Cq() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caVerifiesPOK_Cq() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caAborts_Cq() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userSends_n() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caVerifiesPOK_n() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caAborts_Cn() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userExecutesBlum_n() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caExecutesBlum_n() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caAbortsBlum_n() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void bothAreHappy() {
		U.verbose(new Throwable(), "Wrong state");
	}
}