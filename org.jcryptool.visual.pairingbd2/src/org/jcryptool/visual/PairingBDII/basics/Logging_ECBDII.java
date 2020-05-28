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
    	s.append(Messages.Logging_ECBDII_0);
    	s.append(Messages.Logging_ECBDII_1 + ((Tatepairing == true) ? "Tate" : "Weil")); //$NON-NLS-2$ //$NON-NLS-3$
    	s.append(Messages.Logging_ECBDII_4 + ((Model.getDefault().securityLevel == Model.PENANDPAPER) ? Messages.Logging_ECBDII_5 : Messages.Logging_ECBDII_6)+ ")\n\n"); //$NON-NLS-4$
    	s.append(Messages.Logging_ECBDII_8 + nusers + Messages.Logging_ECBDII_9);
    	s.append(Messages.Logging_ECBDII_10 + keysize + Messages.Logging_ECBDII_11);
    	s.append(Messages.Logging_ECBDII_12);
    	s.append("p = " + p.toString(10) +"\n"); //$NON-NLS-1$ //$NON-NLS-2$
    	s.append("l = " + order.toString(10) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
    	s.append(Messages.Logging_ECBDII_17);
    	s.append(P.GetPrintP() + "\n"); //$NON-NLS-1$
    	s.append(Messages.Logging_ECBDII_19 + pol.PrintP() + "\n"); //$NON-NLS-2$
    	s.append(Messages.Logging_ECBDII_21 + withelim + Messages.Logging_ECBDII_22);
    	
    	for (int i = 0; i < nusers; i++) {
    		s.append(Messages.Logging_ECBDII_23 + (i + 1) + ":\n"); //$NON-NLS-2$
    		s.append(udata.get(i).toString() + "\n\n"); //$NON-NLS-1$
    	}
    	
    	if (timepbduser != 0 || timepubduser != 0) {
    		s.append(Messages.Logging_ECBDII_26);
    		s.append(Messages.Logging_ECBDII_27 + timepbduser + " ms\n"); //$NON-NLS-2$
    		s.append(Messages.Logging_ECBDII_29 + timepubduser + " ms\n"); //$NON-NLS-2$
    	}

    	return s.toString();
    }

    public void SetAsTime(long time1, long time2) {
        timepbduser = time1;
        timepubduser = time2;
    }
}
