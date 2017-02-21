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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Vector;

import org.jcryptool.visual.PairingBDII.algorithm.ECBDII;

import de.flexiprovider.common.math.FlexiBigInt;

public class UserData_ECBDII {
    private PrivateKey SK;
    private PublicKey PK;
    private FlexiBigInt nonce;
    private DHECKeyPair DHKeyPair;
    private PolynomialMod Xvalue, key;
    private final int i;

    // i is indexed between 1 and n, NOT between 0 and n-1

    public UserData_ECBDII(ECBDII protocol, Vector<PrivateKey> SKV, Vector<PublicKey> PKV, Vector<FlexiBigInt> nonceV,
            int myi) {
        final int nmax = protocol.GetNUsers();
        i = myi;

        if (i <= nmax) {
            SK = SKV.get(i - 1);
            PK = PKV.get(i - 1);
            nonce = nonceV.get(i - 1);
            if (i < 4) {
                Xvalue = new PolynomialMod(true, protocol.Getp());
            }
        }
    }

    public PolynomialMod GetKey() {
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

    public DHECKeyPair GetStep1KeyPair() {
        return DHKeyPair;
    }

    public PolynomialMod GetX() {
        return Xvalue;
    }

    public void setDHECKeyPair(DHECKeyPair mykeypair) {
        DHKeyPair = mykeypair;
    }

    public void setKey(PolynomialMod mykey) {
        key = mykey;
    }

    public void setX(PolynomialMod Xpers) {
        Xvalue = Xpers;
    }

    @Override
    public String toString() {
        String s = ""; //$NON-NLS-1$

        // s += "This is the user data for user " +i + ": " + "\n";
        if (SK != null) {
            s += Messages.UserData_ECBDII_1 + SK.toString().substring(0, SK.toString().indexOf('\n')) + "\n"; //$NON-NLS-2$
        }
        if (PK != null) {
            s += Messages.UserData_ECBDII_3 + PK.toString().substring(0, PK.toString().indexOf('\n')) + "\n"; //$NON-NLS-2$
        }
        if (nonce != null) {
            s += Messages.UserData_ECBDII_5 + nonce.toString() + "\n"; //$NON-NLS-2$
        }
        if (DHKeyPair != null) {
            if (DHKeyPair.getSK() != null && !DHKeyPair.getSK().toString().equals("")) { //$NON-NLS-1$
                s += Messages.UserData_ECBDII_0;
                s += DHKeyPair.getSK().toString() + "\n"; //$NON-NLS-1$
            }
            if (DHKeyPair.getPK() != null && !DHKeyPair.getPK().GetPrintP().equals("")) { //$NON-NLS-1$
                s += Messages.UserData_ECBDII_11;
                s += DHKeyPair.getPK().GetPrintP() + "\n"; //$NON-NLS-1$
            }

        }
        if (Xvalue != null && !Xvalue.PrintP().equals("")) { //$NON-NLS-1$
            s += Messages.UserData_ECBDII_14 + Xvalue.PrintP() + "\n"; //$NON-NLS-2$
        }
        if (key != null && !key.PrintP().equals("")) { //$NON-NLS-1$
            s += Messages.UserData_ECBDII_17 + key.PrintP() + "\n"; //$NON-NLS-2$
        }

        return s;
    }
}
