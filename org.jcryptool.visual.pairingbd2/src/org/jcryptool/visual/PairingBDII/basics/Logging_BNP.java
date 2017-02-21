//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
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
        StringBuilder s = new StringBuilder();
        s.append(Messages.Logging_BNP_1);
        s.append("\n\n"); //$NON-NLS-1$
        s.append(Messages.Logging_BNP_4);
        s.append(nusers); //$NON-NLS-1$
        s.append(Messages.Logging_BNP_5);
        s.append("\n"); //$NON-NLS-1$
        s.append(Messages.Logging_BNP_7);
        s.append("\n"); //$NON-NLS-1$
        s.append(Messages.Logging_BNP_9);
        s.append("\n"); //$NON-NLS-1$
        s.append("u = "); //$NON-NLS-1$
        s.append(u);
        s.append(Messages.Logging_BNP_12);
        s.append("\n"); //$NON-NLS-1$
        s.append("p = 36u^4 + 36u\u00b3 + 24u\u00b2 + 6u + 1"); //$NON-NLS-1$
        s.append(p);
        s.append(Messages.Logging_BNP_15);
        s.append("\n"); //$NON-NLS-1$
        s.append("l = 36u^4 + 36u\u00b3 + 18u\u00b2 + 6u + 1"); //$NON-NLS-1$
        s.append(n);
        s.append(" = #E(GF(p));"); //$NON-NLS-1$
        s.append("\n"); //$NON-NLS-1$
        s.append("t = 6u + 1"); //$NON-NLS-1$
        s.append(t);
        s.append(Messages.Logging_BNP_21);
        s.append("\n"); //$NON-NLS-1$
        s.append(Messages.Logging_BNP_23);
        s.append("\n"); //$NON-NLS-1$
        s.append(Messages.Logging_BNP_25);
        s.append("P = "); //$NON-NLS-1$
        s.append(protocol.GetE().getG());
        s.append("\n"); //$NON-NLS-1$
        s.append("Q'= "); //$NON-NLS-1$
        s.append(protocol.GetEp().getGt());
        s.append("\n"); //$NON-NLS-1$
        s.append("\n"); //$NON-NLS-1$
        s.append(Messages.Logging_BNP_31);
        s.append("\n"); //$NON-NLS-1$

        for (int i = 0; i < nusers; i++) {
            s.append(Messages.Logging_BNP_33);
            s.append((i + 1));
            s.append(": "); //$NON-NLS-1$
            s.append("\n"); //$NON-NLS-1$
            s.append(udata.get(i).toString());
            s.append("\n\n"); //$NON-NLS-1$
        }

        if (timepbduser != 0 || timepubduser != 0) {
            s.append(Messages.Logging_BNP_38);
            s.append("\n"); //$NON-NLS-1$
            s.append(Messages.Logging_BNP_40);
            s.append(timepbduser);
            s.append(" ms."); //$NON-NLS-1$
            s.append("\n"); //$NON-NLS-1$
            s.append(Messages.Logging_BNP_43);
            s.append(timepubduser);
            s.append(" ms."); //$NON-NLS-1$
        }
        return s.toString();

    }

    public void setAsTime(long time1, long time2) {
        timepbduser = time1;
        timepubduser = time2;
    }
}
