// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sphincsplus.algorithm;

/***
 * This class is a wrapper for the WOTS+-Node returned by WOTSplus.PK_gen
 * 
 * @author akro
 */
public class WOTSNode {

    private byte[] PKseed;
    private byte[][] tmp;
    private ADRS wotspkADRS;

    public WOTSNode(byte[] PKseed, ADRS wotspkADRS, byte[][] tmp) {
        // TODO Auto-generated constructor stub
    }

    public byte[] getPKseed() {
        return PKseed;
    }

    public byte[][] getTmp() {
        return tmp;
    }

    public ADRS getWotspkADRS() {
        return wotspkADRS;
    }

}
