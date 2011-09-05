//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.PairingBDII.basics;

import java.math.BigInteger;
import java.util.Vector;

import org.jcryptool.visual.PairingBDII.algorithm.BDIIBNP;

public class Logging_BNP {
    private final int nusers;
    public BigInteger p, n, u, t;

    private final Vector<UserData_BNP> udata;
    private long timepbduser;
    private long timepubduser;
    private final BDIIBNP protocol;

    public Logging_BNP(int myn, int myksize, BDIIBNP myprotocol, Vector<UserData_BNP> userdata) {
        nusers = myn;
        protocol = myprotocol;
        udata = userdata;

        u = protocol.GetPars().getU();
        p = protocol.GetPars().getP();
        n = protocol.GetPars().getN();
        t = protocol.GetPars().getT();

    }

    public String printLog() {
        String s = ""; //$NON-NLS-1$
        s = s + Messages.Logging_BNP_1 + "\n" + "\n"; //$NON-NLS-2$ //$NON-NLS-3$
        s = s + Messages.Logging_BNP_4 + nusers + Messages.Logging_BNP_5 + "\n" + Messages.Logging_BNP_7 + "\n"; //$NON-NLS-3$ //$NON-NLS-5$
        s = s + Messages.Logging_BNP_9 + "\n"; //$NON-NLS-2$
        s = s + "u = " + u + Messages.Logging_BNP_12 + "\n"; //$NON-NLS-1$ //$NON-NLS-3$
        s = s + "p = 36u^4 + 36u\u00b3 + 24u\u00b2 + 6u + 1" + p + Messages.Logging_BNP_15 + "\n"; //$NON-NLS-1$ //$NON-NLS-3$
        s = s + "l = 36u^4 + 36u\u00b3 + 18u\u00b2 + 6u + 1" + n + " = #E(GF(p));" + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        s = s + "t = 6u + 1" + t + Messages.Logging_BNP_21 + "\n"; //$NON-NLS-1$ //$NON-NLS-3$
        s = s + Messages.Logging_BNP_23 + "\n"; //$NON-NLS-2$
        s = s + Messages.Logging_BNP_25;
        s = s + "P = " + protocol.GetE().getG() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        s = s + "Q'= " + protocol.GetEp().getGt() + "\n" + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        s = s + Messages.Logging_BNP_31 + "\n"; //$NON-NLS-2$

        for (int i = 0; i < nusers; i++) {
            s += Messages.Logging_BNP_33 + (i + 1) + ": " + "\n"; //$NON-NLS-2$ //$NON-NLS-3$
            s += udata.get(i).toString() + "\n" + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (timepbduser != 0 || timepubduser != 0) {
            s = s + Messages.Logging_BNP_38 + "\n" + Messages.Logging_BNP_40 + timepbduser + " ms."; //$NON-NLS-2$ //$NON-NLS-4$
            s = s + "\n" + Messages.Logging_BNP_43 + timepubduser + " ms."; //$NON-NLS-1$ //$NON-NLS-3$
        }
        return s;

    }

    public void setAsTime(long time1, long time2) {
        timepbduser = time1;
        timepubduser = time2;
    }
}
