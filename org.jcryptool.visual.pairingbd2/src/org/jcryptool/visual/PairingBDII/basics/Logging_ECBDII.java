//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
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
import org.jcryptool.visual.PairingBDII.ui.Model;

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

    /**
     * Creates the content of the logfile for ECBDII
     * @return a string that contains the log
     */
    public String PrintLog() {
    	StringBuilder s = new StringBuilder();
    	s.append("Log Information for BD II with pairings (embedding degree k = 2;");
    	s.append(" pairing type: " + ((Tatepairing == true) ? "Tate" : "Weil"));
    	s.append("; security level: " + ((Model.getDefault().securityLevel == Model.PENANDPAPER) ? "Pen & Paper (8 bit)" : "Industrial security (512 bit)")+ ")\n\n");
    	s.append("There were " + nusers + " users.\n");
    	s.append("The key size is " + keysize + " bits.\n");
    	s.append("Parameters of the system:\n");
    	s.append("p = " + p.toString(10) +"\n");
    	s.append("l = " + order.toString(10) + "\n");
    	s.append("P is a point on the curve, such that:\n");
    	s.append(P.GetPrintP() + "\n");
    	s.append("The distortion map is (x, y) --> (-x, [X] y), where [X]\\u00b3 + 1 = 0 pol = " + pol.PrintP() + "\n");
    	s.append("Denominator elimination is " + withelim + "\n\n\n");
    	
    	for (int i = 0; i < nusers; i++) {
    		s.append("This is the user data for user " + (i + 1) + ":\n");
    		s.append(udata.get(i).toString() + "\n\n");
    	}
    	
    	if (timepbduser != 0 || timepubduser != 0) {
    		s.append("The average computation time per user is:\n");
    		s.append("Burdened user:" + timepbduser + " ms\n");
    		s.append("Unburdened user:" + timepubduser + " ms\n");
    	}

    	return s.toString();
    }

    public void SetAsTime(long time1, long time2) {
        timepbduser = time1;
        timepubduser = time2;
    }
}
