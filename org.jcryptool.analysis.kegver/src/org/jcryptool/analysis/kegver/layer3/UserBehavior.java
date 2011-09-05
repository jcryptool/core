// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3;

import java.math.BigInteger;


public interface UserBehavior {

	public String toString_();

	public Commitment calcCommitment_Cv();

	public boolean check_u(BigInteger get_u);

	public Commitment calcCommitment_Co();

	public boolean executePOK_o();

	public boolean executePOK_z();

	public boolean generate_p();

	public boolean generate_q();

	public Commitment calcCommitment_Cp();

	public Commitment calcCommitment_Cq();

	public BigInteger calc_N();

	public boolean executeBlum_n();

	public boolean checkPOK_1();

}