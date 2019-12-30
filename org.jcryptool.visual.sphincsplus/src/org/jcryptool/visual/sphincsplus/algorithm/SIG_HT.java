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

import org.jcryptool.visual.sphincsplus.EnvironmentParameters;

public class SIG_HT {
    private SIG_XMSS[] xmss_sig;
    public int length = 0;
    
    public SIG_HT(byte[] sigHT) {
        this.length = sigHT.length;
        int lenth = sigHT.length / EnvironmentParameters.d;
        byte[] xmssByteArray = new byte[lenth];
        int offset = 0;
        xmss_sig = new SIG_XMSS[EnvironmentParameters.d];
        for (int i = 0; i < EnvironmentParameters.d; i++) {
            System.arraycopy(sigHT, offset, xmssByteArray, 0, lenth);
            offset += lenth;
            this.xmss_sig[i] = new SIG_XMSS(xmssByteArray);
        }
    }
    
    public SIG_HT() {
        this.xmss_sig = new SIG_XMSS[EnvironmentParameters.d];
    }
    
    public SIG_XMSS getXMSS_SIG(int index) {
        return xmss_sig[index];
    }

    public void setXMSS_SIG(SIG_XMSS sig, int index) {
        this.xmss_sig[index] = sig;
        this.length += sig.length;
    }

    public byte[] toByteArray() {     
        byte[] tempArray = {};
        for (SIG_XMSS node : xmss_sig) {
            tempArray = Utils.concatenateByteArrays(tempArray, node.toByteArray());
        }
        return tempArray;
    }
}
