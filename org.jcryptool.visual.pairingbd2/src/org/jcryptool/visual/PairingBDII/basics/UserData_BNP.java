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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Vector;

import org.jcryptool.visual.PairingBDII.BNField12;
import org.jcryptool.visual.PairingBDII.algorithm.BDIIBNP;

import de.flexiprovider.common.math.FlexiBigInt;

public class UserData_BNP {
    private PrivateKey SK;
    private PublicKey PK;
    private FlexiBigInt nonce;
    private DHECKeyPair2 DHKeyPair;
    private BNField12 Xvalue, key;
    private final int i;

    public UserData_BNP(BDIIBNP protocol, Vector<PrivateKey> SKV, Vector<PublicKey> PKV, Vector<FlexiBigInt> nonceV,
            int myi) {
        final int nmax = protocol.GetNUsers();
        i = myi;

        final BNField12 ONE = new BNField12(protocol.GetPars(), BigInteger.ONE);

        if (i <= nmax) {
            SK = SKV.get(i - 1);
            PK = PKV.get(i - 1);
            nonce = nonceV.get(i - 1);
            if (i < 4) {
                Xvalue = ONE;
            }
        }
    }

    public BNField12 GetKey() {
        return key;
    }

    public FlexiBigInt GetNonce() {
        return nonce;
    }

    public PublicKey GetPK() {
        return PK;
    }

    public PrivateKey GetSK() {
        return SK;
    }

    public DHECKeyPair2 GetStep1KeyPair() {
        return DHKeyPair;
    }

    public BNField12 GetX() {
        return Xvalue;
    }

    public void setDHECKeyPair2(DHECKeyPair2 mykeypair) {
        DHKeyPair = mykeypair;
    }

    public void setKey(BNField12 mykey) {
        key = mykey;
    }

    public void setX(BNField12 Xpers) {
        Xvalue = Xpers;
    }

    @Override
    public String toString() {
        String s = ""; //$NON-NLS-1$

        if (SK != null) {
            s += Messages.UserData_BNP_1 + SK.toString().substring(0, SK.toString().indexOf('\n')) + "\n"; //$NON-NLS-2$
        }
        if (PK != null) {
            s += Messages.UserData_BNP_3 + PK.toString().substring(0, PK.toString().indexOf('\n')) + "\n"; //$NON-NLS-2$
        }
        if (nonce != null) {
            s += Messages.UserData_BNP_5 + nonce.toString() + "\n"; //$NON-NLS-2$
        }
        if (DHKeyPair != null) {
            if (DHKeyPair.GetSK() != null) {
                s += Messages.UserData_BNP_7 + DHKeyPair.GetSK().toString() + "\n"; //$NON-NLS-2$
            }

            s += Messages.UserData_BNP_9;
            if (DHKeyPair.HasPKR() && !DHKeyPair.GetPKR().toString().equals("")) { //$NON-NLS-1$
                s += "R(i) = " + DHKeyPair.GetPKR() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                s += "R(i) = " + Messages.UserData_BNP_14 + "\n"; //$NON-NLS-1$ //$NON-NLS-3$
            }
            if (DHKeyPair.HasPKS() && !DHKeyPair.GetPKS().toString().equals("")) { //$NON-NLS-1$
                s += "S(i) = " + DHKeyPair.GetPKS() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                s += "S(i) = " + Messages.UserData_BNP_20 + "\n"; //$NON-NLS-1$ //$NON-NLS-3$
            }
        }

        if (Xvalue != null) {
            s += Messages.UserData_BNP_22 + Xvalue + "\n"; //$NON-NLS-2$
        }
        if (key != null) {
            s += Messages.UserData_BNP_24 + key + "\n"; //$NON-NLS-2$
        }

        return s;
    }
}
