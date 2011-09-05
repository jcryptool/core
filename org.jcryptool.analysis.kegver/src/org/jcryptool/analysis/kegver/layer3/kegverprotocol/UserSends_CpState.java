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

public class UserSends_CpState extends KegverStateSuper implements KegverStateBehavior {

	public UserSends_CpState(KegverStateContext inKegVer) {
		super(inKegVer);
	}

	public void setup() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void bothUnigen_r() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caAbortsUnigen_r() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userAbortsUnigen_r() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void bothUnigen_s() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caAbortsUnigen_s() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userAbortsUnigen_s() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userGenerates_p() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userAborts_p() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userGenerates_q() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userAborts_q() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userSends_Cp() {

		// Report
		U.verbose(new Throwable(), "entered");

		// Execute this state
		this.getKegver().getKegverData().set_Cp(
				this.getKegver().getUser().calcCommitment_Cp());

		// Report
		U.verbose(new Throwable(),
				"CA: " + this.getKegver().getCA().toString_() +
				", User: " + this.getKegver().getUser().toString_() +
				", KegverData: " + this.getKegver().getKegverData() +
				", Commitment_Cp: " + this.getKegver().getKegverData().getCommitment_Cp());
		U.verbose(new Throwable(), "Assume isQGenerated was true");

		// Trigger next state
		this.getKegver().setState(KegverStateContext.getCaVerifiesPOK_Cp());
		this.getKegver().caVerifiesPOK_Cp();

	}

	public void caVerifiesPOK_Cp() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caAborts_Cp() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userSends_Cq() {

		// Report
		U.verbose(new Throwable(), "entered");

		// Execute this state
		boolean isQGenerated = this.getKegver().getUser().generate_q();

		// Report
		U.verbose(new Throwable(),
				"CA: " + this.getKegver().getCA().toString_() +
				", User: " + this.getKegver().getUser().toString_() +
				", KegverData: " + this.getKegver().getKegverData() +
				", isQGenerated: " + isQGenerated);
		U.verbose(new Throwable(), "Assume isQGenerated was true");
		isQGenerated = true;

		// Trigger next state
		if(isQGenerated){
			this.getKegver().setState(KegverStateContext.getUserSends_Cp());
			this.getKegver().userSends_Cp();
		} else {
			this.getKegver().setState(KegverStateContext.getUserAborts_q());
			this.getKegver().userAborts_q();
		}

	}

	public void caVerifiesPOK_Cq() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caAborts_Cq() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userSends_n() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caVerifiesPOK_n() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caAborts_Cn() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void userExecutesBlum_n() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caExecutesBlum_n() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void caAbortsBlum_n() {
		U.verbose(new Throwable(), "Wrong State");
	}

	public void bothAreHappy() {
		U.verbose(new Throwable(), "Wrong State");
	}
}