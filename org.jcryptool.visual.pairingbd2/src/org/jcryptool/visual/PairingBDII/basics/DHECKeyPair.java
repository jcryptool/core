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

import java.util.Vector;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.finitefields.GFPElement;

public class DHECKeyPair {
    private final PointGFP1 PK;
    private final GFPElement SK;
    private final FlexiBigInt p;
    int windowsize = 3;

    // constructor with SK = 1; PK = P;

    public DHECKeyPair(FlexiBigInt myp, PointGFP1 P, GFPElement a) {
        p = myp;
        SK = new GFPElement(FlexiBigInt.ONE, p);

        PK = P;
    }

    // constructor with SK = mysk; PK = mysk * P;

    public DHECKeyPair(FlexiBigInt myp, PointGFP1 P, GFPElement a, GFPElement mysk) {
        SK = mysk;
        p = myp;

        final Vector<PointGFP1> list = P.PreList(P, a, windowsize);

        PK = P.ScalarMultiplication(a, SK.toFlexiBigInt(), windowsize, list);
    }

    // constructor with SK = mysk, and the scalar list included.

    public DHECKeyPair(FlexiBigInt myp, PointGFP1 P, GFPElement a, GFPElement mysk, Vector<PointGFP1> mylist) {
        SK = mysk;
        p = myp;

        PK = P.ScalarMultiplication(a, SK.toFlexiBigInt(), windowsize, mylist);
    }

    public PointGFP1 getPK() {
        return PK;
    }

    public GFPElement getSK() {
        return SK;
    }

    public String PKtoString() {
        String s = ""; //$NON-NLS-1$

        s = "(" + printPK().get(0).toString() + "|" + printPK().get(1).toString() + "|" + printPK().get(2).toString() //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ")"; //$NON-NLS-1$

        return s;
    }

    public Vector<FlexiBigInt> printPK() {
        return PK.PrintP();
    }

    public FlexiBigInt printSK() {
        return SK.toFlexiBigInt();
    }

    public String SKtoString() {
        return printSK().toString();
    }

}
