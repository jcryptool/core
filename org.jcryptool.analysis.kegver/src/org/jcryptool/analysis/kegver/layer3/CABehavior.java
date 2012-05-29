// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3;

import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer3.unigenprotocol.UnigenData;

public interface CABehavior {
    KegverData getKegverData();
    UnigenData getUniGenData();
    KegverGroup getKegVerGroup();
    String toString_();
    UnigenData getUnigenData();
    BigInteger calc_u();
    boolean verifyPOK_o();
    boolean caVerifiesPOK_z();
    boolean verifyPOK_Cp();
    boolean verifyPOK_Cq();
    boolean verifyPOK_N();
    boolean executeBlum_n();
}
