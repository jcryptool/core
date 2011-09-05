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

import org.jcryptool.visual.PairingBDII.BNCurve;
import org.jcryptool.visual.PairingBDII.BNCurve2;
import org.jcryptool.visual.PairingBDII.BNPoint;
import org.jcryptool.visual.PairingBDII.BNPoint2;

public class DHECKeyPair2 {
    private final BigInteger sk;
    private final BNCurve E;
    private final BNCurve2 E12;
    private final BNPoint P;
    private final BNPoint2 Q;
    private BNPoint PKR; // the public key based on P: PKR = sk*P;
    private BNPoint2 PKS; // the public key based on Q: PKS = sk*Q;
    private final int pkscheme; // denotes which PKs this user has. pkscheme can take the values: 0 for PKR only,

    // 1 for PKS only; 2 for both PKR and PKS.

    public DHECKeyPair2(BNCurve myE, BNCurve2 myE2, BNPoint myP, BNPoint2 myQ, BigInteger mysk, int mypkscheme) {
        P = myP;
        Q = myQ;

        E = myE;
        E12 = myE2;

        sk = mysk;
        pkscheme = mypkscheme;

        switch (pkscheme) {
            case 0:
                PKR = P.multiply(sk);
                break;
            case 1:
                PKS = Q.multiply(sk);
                break;
            default:
                PKR = P.multiply(sk);
                PKS = Q.multiply(sk);
        }
    }

    public BNPoint GetPKR() {
        if (pkscheme == 1) {
            return E.getInfinity();
        }

        return PKR;
    }

    public BNPoint2 GetPKS() {
        if (pkscheme == 0) {
            return E12.getInfinity();
        }

        return PKS;
    }

    public BigInteger GetSK() {
        return sk;
    }

    public boolean HasPKR() {
        boolean hasPKR = true;

        if (pkscheme == 1) {
            hasPKR = false;
        }

        return hasPKR;
    }

    public boolean HasPKS() {
        boolean hasPKS = true;

        if (pkscheme == 0) {
            hasPKS = false;
        }

        return hasPKS;
    }

    public String PrintPubKeys() {
        String s = ""; //$NON-NLS-1$

        if (HasPKR()) {
            s = s + "(" + GetPKR(); //$NON-NLS-1$
            if (HasPKS()) {
                s = s + " , " + GetPKS() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                s = s + ")"; //$NON-NLS-1$
            }
        } else {
            s = s + "(" + GetPKS() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }

        return s;
    }

}
