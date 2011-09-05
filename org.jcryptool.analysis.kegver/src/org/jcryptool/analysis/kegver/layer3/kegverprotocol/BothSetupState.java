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

public class BothSetupState extends KegverStateSuper implements KegverStateBehavior {

	/**
	 * Constructor
	 */
	public BothSetupState(KegverStateContext inKegver){
		super(inKegver);
	}

	public void setup() {

		// Report
		U.verbose(new Throwable(), "entered");

		// Execute this state
		this.getKegver().setKegverData(this.getKegver().getCA().getKegverData());

		// Report
		U.verbose(new Throwable(),
				"CA: " + this.getKegver().getCA().toString_() +
				", User: " + this.getKegver().getUser().toString_() +
				", KegverData: " + this.getKegver().getKegverData());

		// Trigger next state
		this.getKegver().setState(KegverStateContext.getBothUnigen_r());
		this.getKegver().bothUnigen_r();
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
		U.verbose(new Throwable(), "Wrong state");

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
