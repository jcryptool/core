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

import org.jcryptool.analysis.kegver.layer3.CABehavior;
import org.jcryptool.analysis.kegver.layer3.UserBehavior;

public class UnigenStateContext{

	/*
	 * Class variables
	 */

	private static BothSetup bothSetup = null;
	private static UserChecksPOK_1 userChecksPOK_1 = null;
	private static UserAbortsPOK_1 userAbortsPOK_1 = null;
	private static UserSends_Cv userSends_Cv = null;
	private static CaSends_u caSends_u = null;
	private static UserChecks_u userChecks_u = null;
	private static UserAborts_u userAborts_u = null;
	private static UserSends_Co userSends_Co = null;
	private static UserExecutesPOK_o userExecutesPOK_o = null;
	private static CaVerifiesPOK_o caVerifiesPOK_o = null;
	private static CaAbortsPOK_o caAbortsPOK_o = null;
	private static UserExecutesPOK_z userExecutesPOK_z = null;
	private static CaVerifiesPOK_z caVerifiesPOK_z = null;
	private static CaAbortsPOK_z caAbortsPOK_z = null;
	private static BothAreHappy bothAreHappy = null;

	/*
	 * Class getter
	 */

	public static BothSetup getBothSetup() {
		return bothSetup;
	}

	public static UserChecksPOK_1 getUserChecksPOK_1() {
		return userChecksPOK_1;
	}

	public static UserAbortsPOK_1 getUserAbortsPOK_1() {
		return userAbortsPOK_1;
	}

	public static UserSends_Cv getUserSends_Cv() {
		return userSends_Cv;
	}

	public static CaSends_u getCaSends_u() {
		return caSends_u;
	}

	public static UserChecks_u getUserChecks_u() {
		return userChecks_u;
	}

	public static UserAborts_u getUserAborts_u() {
		return userAborts_u;
	}

	public static UserSends_Co getUserSends_Co() {
		return userSends_Co;
	}

	public static UserExecutesPOK_o getUserExecutesPOK_o() {
		return userExecutesPOK_o;
	}

	public static CaVerifiesPOK_o getCaVerifiesPOK_o() {
		return caVerifiesPOK_o;
	}

	public static CaAbortsPOK_o getCaAbortsPOK_o() {
		return caAbortsPOK_o;
	}

	public static UserExecutesPOK_z getUserExecutesPOK_z() {
		return userExecutesPOK_z;
	}

	public static CaVerifiesPOK_z getCaVerifiesPOK_z() {
		return caVerifiesPOK_z;
	}

	public static CaAbortsPOK_z getCaAbortsPOK_z() {
		return caAbortsPOK_z;
	}

	public static BothAreHappy getBothAreHappy() {
		return bothAreHappy;
	}

	/*
	 * Instance variables
	 */

	private CABehavior aCA = null;
	private UserBehavior aUser = null;
	private UnigenData aUnigenData = null;
	private UnigenStateBehavior aState = null;

	/*
	 * Constructor
	 */

	public UnigenStateContext(CABehavior inCA, UserBehavior inUser){
		// Setup
		this.initStates();
		this.setCA(inCA);
		this.setUser(inUser);
		this.setState(UnigenStateContext.bothSetup);
	}

	private void initStates() {
		UnigenStateContext.bothSetup = new BothSetup(this);
		UnigenStateContext.userChecksPOK_1 = new UserChecksPOK_1(this);
		UnigenStateContext.userAbortsPOK_1 = new UserAbortsPOK_1(this);
		UnigenStateContext.userSends_Cv = new UserSends_Cv(this);
		UnigenStateContext.caSends_u = new CaSends_u(this);
		UnigenStateContext.userChecks_u = new UserChecks_u(this);
		UnigenStateContext.userAborts_u = new UserAborts_u(this);
		UnigenStateContext.userSends_Co = new UserSends_Co(this);
		UnigenStateContext.userExecutesPOK_o = new UserExecutesPOK_o(this);
		UnigenStateContext.caVerifiesPOK_o = new CaVerifiesPOK_o(this);
		UnigenStateContext.caAbortsPOK_o = new CaAbortsPOK_o(this);
		UnigenStateContext.userExecutesPOK_z = new UserExecutesPOK_z(this);
		UnigenStateContext.caVerifiesPOK_z = new CaVerifiesPOK_z(this);
		UnigenStateContext.caAbortsPOK_z = new CaAbortsPOK_z(this);
		UnigenStateContext.bothAreHappy = new BothAreHappy(this);
	}

	/*
	 * State switches
	 */

	public void bothSetup() {
		this.getState().bothSetup();
	}

	public void userChecksPOK_1() {
		this.getState().userChecksPOK_1();
	}

	public void userAbortsPOK_1() {
		this.getState().userAbortsPOK_1();
	}

	public void userSends_Cv() {
		this.getState().userSends_Cv();
	}

	public void caSends_u() {
		this.getState().caSends_u();
	}

	public void userChecks_u() {
		this.getState().userChecks_u();
	}

	public void userAborts_u() {
		this.getState().userAborts_u();
	}

	public void userSends_Co() {
		this.getState().userSends_Co();
	}

	public void userExecutesPOK_o() {
		this.getState().userExecutesPOK_o();
	}

	public void caVerifiesPOK_o() {
		this.getState().caVerifiesPOK_o();
	}

	public void caAbortsPOK_o() {
		this.getState().caAbortsPOK_o();
	}

	public void userExecutesPOK_z() {
		this.getState().userExecutesPOK_z();
	}

	public  void caVerifiesPOK_z() {
		this.getState().caVerifiesPOK_z();
	}

	public void caAbortsPOK_z() {
		this.getState().caAbortsPOK_z();
	}

	public void bothAreHappy() {
		this.getState().bothAreHappy();
	}

	/*
	 * Getter and setter
	 */

	public UserBehavior getUser() {
		return this.aUser;
	}

	private UserBehavior setUser(UserBehavior inUser) {
		if (inUser == null){
			throw new NullPointerException();
		}
		this.aUser = inUser;
		return this.getUser();
	}

	public CABehavior getCA(){
		return this.aCA;
	}

	private CABehavior setCA(CABehavior inCA) {
		if (inCA == null){
			throw new NullPointerException();
		}
		this.aCA = inCA;
		return this.getCA();
	}

	public UnigenStateBehavior getState() {
		return this.aState;
	}

	protected UnigenStateBehavior setState(UnigenStateBehavior inState) {
		this.aState = inState;
		return this.getState();
	}

	public UnigenData getUnigenData() {
		return this.aUnigenData;
	}

	public UnigenData setUnigenData(UnigenData inUnigenData) {
		this.aUnigenData = inUnigenData;
		return this.getUnigenData();
	}
}