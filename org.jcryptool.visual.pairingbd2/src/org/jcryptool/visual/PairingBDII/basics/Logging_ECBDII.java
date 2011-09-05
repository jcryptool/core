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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Vector;

import org.jcryptool.visual.PairingBDII.algorithm.ECBDII;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.finitefields.GFPElement;

public class Logging_ECBDII {
    private final int nusers;
    private final int keysize;
    private final GFPElement a;
    private final FlexiBigInt order;
    private final FlexiBigInt p;
    private final PointGFP1 P;
    private final PolynomialMod XShift;
    private final PolynomialMod YShift;
    private final PolynomialMod pol;
    private final boolean withelim;
    private final boolean Tatepairing;

    private final Vector<UserData_ECBDII> udata;
    private long timepbduser, timepubduser;

    public Logging_ECBDII(ECBDII protocol, Vector<PrivateKey> SKA, Vector<PublicKey> PKA, Vector<FlexiBigInt> Nonces,
            Vector<UserData_ECBDII> users) {
        nusers = protocol.GetNUsers();
        keysize = protocol.GetKeySize();
        a = protocol.GetA();
        order = protocol.GetOrder();
        p = protocol.Getp();
        P = protocol.GetP();
        XShift = protocol.GetXShift();
        YShift = protocol.GetYShift();
        pol = protocol.Getpol();
        withelim = protocol.GetWithElim();
        Tatepairing = protocol.GetTateWeil();

        udata = users;
    }

    public GFPElement GetA() {
        return a;
    }

    public boolean GetElimTrue() {
        return withelim;
    }

    public int GetKSize() {
        return keysize;
    }

    public int GetN() {
        return nusers;
    }

    public FlexiBigInt GetOrder() {
        return order;
    }

    public FlexiBigInt Getp() {
        return p;
    }

    public PointGFP1 GetP() {
        return P;
    }

    public PolynomialMod Getpol() {
        return pol;
    }

    public boolean GetTateWeil() {
        return Tatepairing;
    }

    public UserData_ECBDII GetUDataOf(int i) {
        return udata.get(i - 1);
    }

    public Vector<UserData_ECBDII> GetUsersData() {
        return udata;
    }

    public PolynomialMod GetXShift() {
        return XShift;
    }

    public PolynomialMod GetYShift() {
        return YShift;
    }

    public String PrintLog() {
        String s = ""; //$NON-NLS-1$
        s = s + Messages.Logging_ECBDII_1 + "\n" + "\n"; //$NON-NLS-2$ //$NON-NLS-3$
        s = s + Messages.Logging_ECBDII_4 + nusers + Messages.Logging_ECBDII_5 + "\n" + Messages.Logging_ECBDII_7 + keysize + Messages.Logging_ECBDII_8 + "\n"; //$NON-NLS-3$ //$NON-NLS-6$
        s = s + Messages.Logging_ECBDII_10 + "\n" + "p = " + p.toString(10) + "\n" + "l = " + order.toString(10) //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + "\n"; //$NON-NLS-1$
        s = s + Messages.Logging_ECBDII_0 + "\n" + P.GetPrintP() + "\n" + "\n"; //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        s = s + Messages.Logging_ECBDII_20 + "pol = " + "\n"; //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        s = s + pol.PrintP() + "\n"; //$NON-NLS-1$
        s = s + Messages.Logging_ECBDII_25 + withelim + "\n" + Messages.Logging_ECBDII_27 + Tatepairing + "\n"; //$NON-NLS-2$ //$NON-NLS-4$

        for (int i = 0; i < nusers; i++) {
            s += Messages.Logging_ECBDII_29 + (i + 1) + ": " + "\n"; //$NON-NLS-2$ //$NON-NLS-3$
            s += udata.get(i).toString() + "\n" + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (timepbduser != 0 || timepubduser != 0) {
            s = s + Messages.Logging_ECBDII_34 + "\n" + Messages.Logging_ECBDII_36 + timepbduser + " ms.\n"; //$NON-NLS-2$ //$NON-NLS-4$
            s = s + Messages.Logging_ECBDII_38 + timepubduser + " ms."; //$NON-NLS-2$
        }
        return s;
    }

    // i is indexed 1 to n

    public void SetAsTime(long time1, long time2) {
        timepbduser = time1;
        timepubduser = time2;
    }
}
