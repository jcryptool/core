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

import org.jcryptool.analysis.kegver.layer3.unigenprotocol.UnigenData;

public interface CABehavior {

	public KegverData getKegverData();

	public UnigenData getUniGenData();

	public KegverGroup getKegVerGroup();

	public String toString_();

	public UnigenData getUnigenData();

	public BigInteger calc_u();

	public boolean verifyPOK_o();

	public boolean caVerifiesPOK_z();

	public boolean verifyPOK_Cp();

	public boolean verifyPOK_Cq();

	public boolean verifyPOK_N();

	public boolean executeBlum_n();
}
