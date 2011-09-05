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

import org.jcryptool.analysis.kegver.layer3.CABehavior;
import org.jcryptool.analysis.kegver.layer3.U;
import org.jcryptool.analysis.kegver.layer3.UserBehavior;
import org.jcryptool.analysis.kegver.layer3.unigenprotocol.BothAreHappy;
import org.jcryptool.analysis.kegver.layer3.unigenprotocol.UnigenStateContext;


public class BothUnigen_rState extends KegverStateSuper implements KegverStateBehavior {

	public BothUnigen_rState(KegverStateContext inKegVer) {
		super(inKegVer);
	}

	public void setup() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void bothUnigen_r() {

		// Report
		U.verbose(new Throwable(), "entered");

		// Execute this state
		UserBehavior aUser = this.getKegver().getUser();
		CABehavior aCA = this.getKegver().getCA();
		UnigenStateContext aUniGenStateContext = new UnigenStateContext(aCA, aUser);
		aUniGenStateContext.bothSetup();

		// Report
		U.verbose(new Throwable(),
				"CA: " + this.getKegver().getCA().toString_() +
				", User: " + this.getKegver().getUser().toString_() +
				", KegverData: " + this.getKegver().getKegverData());

		// Trigger next state
		boolean isUnigen_rSuccessful = aUniGenStateContext.getState().getClass().equals(BothAreHappy.class);
		U.verbose(new Throwable(), "isUnigen_rSuccessful: " + isUnigen_rSuccessful);
		if (isUnigen_rSuccessful){
			this.getKegver().setState(KegverStateContext.getBothUnigen_s());
			this.getKegver().bothUnigen_s();
		} else {
			this.getKegver().setState(KegverStateContext.getCaAbortsUnigen_r());
			this.getKegver().caAbortsUnigen_r();
		}
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