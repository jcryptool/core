package org.jcryptool.analysis.kegver.layer3.unigenprotocol;

import org.jcryptool.analysis.kegver.layer3.U;

public class CaAbortsPOK_z extends UnigenStateSuper implements
		UnigenStateBehavior {

	public CaAbortsPOK_z(UnigenStateContext inUniGen) {
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
		// Report
		U.verbose(new Throwable(), "entered");
		
		// Execute this state
		
		// Report
		U.verbose(new Throwable(), "CA aborts POK_z");
				
		// Trigger next state
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