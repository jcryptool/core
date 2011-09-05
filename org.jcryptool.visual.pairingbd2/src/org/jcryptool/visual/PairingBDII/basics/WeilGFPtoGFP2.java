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

public class WeilGFPtoGFP2 {

    public PolynomialMod value;
    public PolynomialMod xShift;
    public PolynomialMod yShift;

    public WeilGFPtoGFP2(FlexiBigInt myp) {
        value = new PolynomialMod(myp);
        xShift = new PolynomialMod(true, myp);
        yShift = new PolynomialMod(true, myp);
    }

    public WeilGFPtoGFP2(FlexiBigInt myp, PolynomialMod mypol, PolynomialMod myX, PolynomialMod myY) {
        value = new PolynomialMod(true, myp).Add(new PolynomialMod(mypol.deg - 1, myp));

        xShift = myX;
        yShift = myY;
    }

    public WeilGFPtoGFP2(PolynomialMod myX, PolynomialMod myY, PointGFP1 A, PointGFP1 B, Vector<FlexiBigInt> l,
            boolean elimtrue, PolynomialMod mypol, FlexiBigInt lwhole, GFPElement ECa) {
        xShift = myX;
        yShift = myY;

        GFPtoGFP2Pairing AtoB;
        GFP2toGFPPairing BtoA;

        AtoB = new GFPtoGFP2Pairing(false, myX, myY, A, B, l, elimtrue, mypol, lwhole, ECa);
        BtoA = new GFP2toGFPPairing(false, myX, myY, B, A, l, elimtrue, mypol, lwhole, ECa);

        final PolynomialMod invby = BtoA.value.InvertMod(mypol);
        value = AtoB.value.Multiply(invby).Negate().ReduceModPol(mypol);
    }
}