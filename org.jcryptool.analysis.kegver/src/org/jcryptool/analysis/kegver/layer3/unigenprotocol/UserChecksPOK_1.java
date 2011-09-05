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

public class UserChecksPOK_1 extends UnigenStateSuper implements
		UnigenStateBehavior {

	public UserChecksPOK_1(UnigenStateContext inUnigen) {
		super(inUnigen);
	}

	public void bothSetup() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void userChecksPOK_1() {
		// Report
		U.verbose(new Throwable(), "entered");

		// Execute this state
		boolean isPOK_1Good = this.getUnigen().getUser().checkPOK_1();

		// Report
		U.verbose(new Throwable(),
				"CA: " + this.getUnigen().getCA().toString_() +
				", User: " + this.getUnigen().getUser().toString_() +
				", UnigenData: " + this.getUnigen().getUnigenData() +
				", POK_1: " + this.getUnigen().getUnigenData().toString()); //.getPOK_1().toString());
		U.verbose(new Throwable(), "Assuming, POK_1 was good");
		isPOK_1Good = true;
		// Trigger next state
		if(isPOK_1Good){
			this.getUnigen().setState(UnigenStateContext.getUserSends_Cv());
			this.getUnigen().userSends_Cv();
		} else {
			this.getUnigen().setState(UnigenStateContext.getUserAbortsPOK_1());
			this.getUnigen().userAbortsPOK_1();
		}
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
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caAbortsPOK_z() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void bothAreHappy() {
		U.verbose(new Throwable(), "Wrong state");
	}

}
