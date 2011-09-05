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

public interface UnigenStateBehavior {

	public void bothSetup();

	public void userChecksPOK_1();

	public void userAbortsPOK_1();

	public void userSends_Cv();

	public void caSends_u();

	public void userChecks_u();

	public void userAborts_u();

	public void userSends_Co();

	public void userExecutesPOK_o();

	public void caVerifiesPOK_o();

	public void caAbortsPOK_o();

	public void userExecutesPOK_z();

	public void caVerifiesPOK_z();

	public void caAbortsPOK_z();

	public void bothAreHappy();

}
