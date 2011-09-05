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
import org.jcryptool.analysis.kegver.layer3.DummyCA;
import org.jcryptool.analysis.kegver.layer3.DummyUser;
import org.jcryptool.analysis.kegver.layer3.KegverData;
import org.jcryptool.analysis.kegver.layer3.UserBehavior;

public class KegverStateContext{

	/*
	 * Class variables
	 */

	private static BothSetupState bothSetup = null;
	private static BothUnigen_rState bothUnigen_r = null;
	private static CaAbortsUnigen_rState caAbortsUnigen_r = null;
	private static UserAbortsUnigen_rState userAbortsUnigen_r = null;
	private static BothUnigen_sState bothUnigen_s = null;
	private static CaAbortsUnigen_sState caAbortsUnigen_s = null;
	private static UserAbortsUnigen_sState userAbortsUnigen_s = null;
	private static UserGenerates_pState userGenerates_p = null;
	private static UserAborts_pState userAborts_p = null;
	private static UserGenerates_qState userGenerates_q = null;
	private static UserAborts_qState userAborts_q = null;
	private static UserSends_CpState userSends_Cp = null;
	private static CaVerifiesPOK_CpState caVerifiesPOK_Cp = null;
	private static CaAborts_CpState caAborts_Cp = null;
	private static UserSends_CqState userSends_Cq = null;
	private static CaVerifiesPOK_CqState caVerifiesPOK_Cq = null;
	private static CaAborts_CqState caAborts_Cq = null;
	private static UserSends_nState userSends_n = null;
	private static CaVerifiesPOK_nState caVerifiesPOK_n = null;
	private static CaAborts_CnState caAborts_Cn = null;
	private static UserExecutesBlum_nState userExecutesBlum_n = null;
	private static CaExecutesBlum_nState caExecutesBlum_n = null;
	private static CaAbortsBlum_nState caAbortsBlum_n = null;
	private static BothAreHappyState bothAreHappy = null;

	/*
	 * Class getter
	 */

	public static BothSetupState getBothSetup() {
		return bothSetup;
	}

	public static BothUnigen_rState getBothUnigen_r() {
		return bothUnigen_r;
	}

	public static CaAbortsUnigen_rState getCaAbortsUnigen_r() {
		return caAbortsUnigen_r;
	}

	public static UserAbortsUnigen_rState getUserAbortsUnigen_r() {
		return userAbortsUnigen_r;
	}

	public static BothUnigen_sState getBothUnigen_s() {
		return bothUnigen_s;
	}

	public static CaAbortsUnigen_sState getCaAbortsUnigen_s() {
		return caAbortsUnigen_s;
	}

	public static UserAbortsUnigen_sState getUserAbortsUnigen_s() {
		return userAbortsUnigen_s;
	}

	public static UserGenerates_pState getUserGenerates_p() {
		return userGenerates_p;
	}

	public static UserAborts_pState getUserAborts_p() {
		return userAborts_p;
	}

	public static UserGenerates_qState getUserGenerates_q() {
		return userGenerates_q;
	}

	public static UserAborts_qState getUserAborts_q() {
		return userAborts_q;
	}

	public static UserSends_CpState getUserSends_Cp() {
		return userSends_Cp;
	}

	public static CaVerifiesPOK_CpState getCaVerifiesPOK_Cp() {
		return caVerifiesPOK_Cp;
	}

	public static CaAborts_CpState getCaAborts_Cp() {
		return caAborts_Cp;
	}

	public static UserSends_CqState getUserSends_Cq() {
		return userSends_Cq;
	}

	public static CaVerifiesPOK_CqState getCaVerifiesPOK_Cq() {
		return caVerifiesPOK_Cq;
	}

	public static CaAborts_CqState getCaAborts_Cq() {
		return caAborts_Cq;
	}

	public static UserSends_nState getUserSends_n() {
		return userSends_n;
	}

	public static CaVerifiesPOK_nState getCaVerifiesPOK_n() {
		return caVerifiesPOK_n;
	}

	public static CaAborts_CnState getCaAborts_Cn() {
		return caAborts_Cn;
	}

	public static UserExecutesBlum_nState getUserExecutesBlum_n() {
		return userExecutesBlum_n;
	}

	public static CaExecutesBlum_nState getCaExecutesBlum_n() {
		return caExecutesBlum_n;
	}

	public static CaAbortsBlum_nState getCaAbortsBlum_n() {
		return caAbortsBlum_n;
	}

	public static BothAreHappyState getBothAreHappy() {
		return bothAreHappy;
	}

	/*
	 * Instance variables
	 */

	private CABehavior aCA = null;
	private UserBehavior aUser = null;
	private KegverData aKegVerData = null;
	private KegverStateBehavior aState = null;

	/*
	 * Constructor
	 */

	public KegverStateContext(CABehavior inCA, UserBehavior inUser){
		// Setup
		this.initStates();
		this.setCA(inCA);
		this.setUser(inUser);
		this.setState(KegverStateContext.bothSetup);
	}

	private void initStates() {
		KegverStateContext.bothSetup = new BothSetupState (this);
		KegverStateContext.bothUnigen_r = new BothUnigen_rState (this);
		KegverStateContext.caAbortsUnigen_r = new CaAbortsUnigen_rState (this);
		KegverStateContext.userAbortsUnigen_r = new UserAbortsUnigen_rState (this);
		KegverStateContext.bothUnigen_s = new BothUnigen_sState (this);
		KegverStateContext.caAbortsUnigen_s = new CaAbortsUnigen_sState (this);
		KegverStateContext.userAbortsUnigen_s = new UserAbortsUnigen_sState (this);
		KegverStateContext.userGenerates_p = new UserGenerates_pState (this);
		KegverStateContext.userAborts_p = new UserAborts_pState (this);
		KegverStateContext.userGenerates_q = new UserGenerates_qState (this);
		KegverStateContext.userAborts_q = new UserAborts_qState (this);
		KegverStateContext.userSends_Cp = new UserSends_CpState (this);
		KegverStateContext.caVerifiesPOK_Cp = new CaVerifiesPOK_CpState (this);
		KegverStateContext.caAborts_Cp = new CaAborts_CpState (this);
		KegverStateContext.userSends_Cq = new UserSends_CqState (this);
		KegverStateContext.caVerifiesPOK_Cq = new CaVerifiesPOK_CqState (this);
		KegverStateContext.caAborts_Cq = new CaAborts_CqState (this);
		KegverStateContext.userSends_n = new UserSends_nState (this);
		KegverStateContext.caVerifiesPOK_n = new CaVerifiesPOK_nState (this);
		KegverStateContext.caAborts_Cn = new CaAborts_CnState (this);
		KegverStateContext.userExecutesBlum_n = new UserExecutesBlum_nState (this);
		KegverStateContext.caExecutesBlum_n = new CaExecutesBlum_nState (this);
		KegverStateContext.caAbortsBlum_n = new CaAbortsBlum_nState (this);
		KegverStateContext.bothAreHappy = new BothAreHappyState (this);
	}

	/*
	 * State switches
	 */

	public void setup() {
		this.getState().setup();
	}

	public void bothUnigen_r() {
		this.getState().bothUnigen_r();
	}

	public void caAbortsUnigen_r() {
		this.getState().caAbortsUnigen_r();
	}

	public void userAbortsUnigen_r() {
		this.getState().userAbortsUnigen_r();
	}

	public void bothUnigen_s() {
		this.getState().bothUnigen_s();
	}

	public void caAbortsUnigen_s() {
		this.getState().caAbortsUnigen_s();
	}

	public void userAbortsUnigen_s() {
		this.getState().userAbortsUnigen_s();
	}

	public void userGenerates_p() {
		this.getState().userGenerates_p();
	}

	public void userAborts_p() {
		this.getState().userAborts_p();
	}

	public void userGenerates_q() {
		this.getState().userGenerates_q();
	}

	public void userAborts_q() {
		this.getState().userAborts_q();
	}

	public void userSends_Cp() {
		this.getState().userSends_Cp();
	}

	public void caVerifiesPOK_Cp() {
		this.getState().caVerifiesPOK_Cp();
	}

	public void caAborts_Cp() {
		this.getState().caAborts_Cp();
	}

	public void userSends_Cq() {
		this.getState().userSends_Cq();
	}

	public void caVerifiesPOK_Cq() {
		this.getState().caVerifiesPOK_Cq();
	}

	public void caAborts_Cq() {
		this.getState().caAborts_Cq();
	}

	public void userSends_n() {
		this.getState().userSends_n();
	}

	public void caVerifiesPOK_n() {
		this.getState().caVerifiesPOK_n();
	}

	public void caAborts_Cn() {
		this.getState().caAborts_Cn();
	}

	public void userExecutesBlum_n() {
		this.getState().userExecutesBlum_n();
	}

	public void caExecutesBlum_n() {
		this.getState().caExecutesBlum_n();
	}

	public void caAbortsBlum_n() {
		this.getState().caAbortsBlum_n();
	}

	public void bothAreHappy() {
		this.getState().bothAreHappy();
	}

	/*
	 * Getters and Setters
	 */

	public UserBehavior getUser() {
		return this.aUser;
	}

	private UserBehavior setUser(UserBehavior inUser) {
		if (inUser == null){
			inUser = new DummyUser();
		}
		this.aUser = inUser;
		return this.getUser();
	}

	public CABehavior getCA(){
		return this.aCA;
	}

	private CABehavior setCA(CABehavior inCA) {
		if (inCA == null){
			inCA = new DummyCA();
		}
		this.aCA = inCA;
		return this.getCA();
	}

	private KegverStateBehavior getState() {
		return this.aState;
	}

	/**
	 * Protected so only callable from inside this package.
	 *
	 */
	protected KegverStateBehavior setState(KegverStateBehavior inState) {
		this.aState = inState;
		return this.getState();
	}

	public KegverData getKegverData() {
		return this.aKegVerData;
	}

	public KegverData setKegverData(KegverData inKegVerData) {
		this.aKegVerData = inKegVerData;
		return this.getKegverData();
	}
}