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

public interface IWOTSplus {
    public byte[] chain(byte[] X, int i, int s, byte[] PKseed, ADRS adrs);

    public byte[][] wots_SKgen(byte[] SKseed, ADRS adrs);

    public byte[] wots_PKgen(byte[] SKseed, byte[] PKseed, ADRS adrs);

    public byte[][] wots_sign(byte[] message, byte[] SKseed, byte[] PKseed, ADRS adrs);

    public byte[] wots_pkFromSig(byte[][] sig, byte[] message, byte[] PKseed, ADRS adrs);

}
