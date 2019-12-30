// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sphincsplus.interfaces;

import org.jcryptool.visual.sphincsplus.algorithm.ADRS;

public interface IHashFunction {
    public byte[] F(byte[] Pkseed, ADRS adrs, byte[] m1);

    public byte[] H(byte[] Pkseed, ADRS adrs, byte[] m1, byte[] m2);

    public byte[] Hmsg(byte[] R, byte[] Pkseed, byte[] Pkroot, byte[] m);

    public byte[] PRF(byte[] seed, ADRS adrs);

    public byte[] PRFmsg(byte[] Skprf, byte[] optRand, byte[] m);
}
