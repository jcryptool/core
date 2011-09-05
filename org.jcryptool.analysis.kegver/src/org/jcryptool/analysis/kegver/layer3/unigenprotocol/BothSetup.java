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

public class BothSetup extends UnigenStateSuper implements UnigenStateBehavior {

	public BothSetup(UnigenStateContext inUnigen) {
		super(inUnigen);
	}

	public void bothSetup() {
		// Report
		U.verbose(new Throwable(), "entered");

		// Execute this state
		this.getUnigen().setUnigenData(this.getUnigen().getCA().getUnigenData());

		// Report
		U.verbose(new Throwable(),
				"CA: " + this.getUnigen().getCA().toString_() +
				", User: " + this.getUnigen().getUser().toString_() +
				", UnigenData: " + this.getUnigen().getUnigenData());

		// Trigger next state
		this.getUnigen().setState(UnigenStateContext.getUserChecksPOK_1());
		this.getUnigen().userChecksPOK_1();
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
		U.verbose(new Throwable(), "Wrong state");
	}

	public void caAbortsPOK_z() {
		U.verbose(new Throwable(), "Wrong state");
	}

	public void bothAreHappy() {
		U.verbose(new Throwable(), "Wrong state");
	}
}