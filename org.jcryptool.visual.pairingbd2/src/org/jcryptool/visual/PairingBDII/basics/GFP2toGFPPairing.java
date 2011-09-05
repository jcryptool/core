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

public class GFP2toGFPPairing {
    public PolynomialMod value;
    public PolynomialMod xShift;
    public PolynomialMod yShift;

    private final PolynomialMod pol; // this is the polynomial that defines the GFP2 points.

    public GFP2toGFPPairing(boolean withexp, PolynomialMod myX, PolynomialMod myY, PointGFP1 A, PointGFP1 B,
            Vector<FlexiBigInt> l, boolean elimtrue, PolynomialMod mypol, FlexiBigInt lwhole, GFPElement ECa) {
        PointGFP2 p1, T;
        PointGFP1 p2;
        PolynomialMod f1, den;
        PointGFP2 updatedT;

        xShift = myX;
        yShift = myY;
        pol = mypol;

        p1 = distort(A.ToAffine());
        p2 = B.ToAffine();

        final FlexiBigInt p = p2.p;

        f1 = new PolynomialMod(true, p);
        den = new PolynomialMod(true, p);
        T = p1;

        for (int i = 1; i < l.size(); i++) {

            f1 = f1.Multiply(f1).ReduceModPol(pol).Multiply(lineFunctionIn(T, T, p2, ECa)).ReduceModPol(pol);
            updatedT = T.Double(ECa);
            T = updatedT.ToAffine();

            if (!elimtrue) {
                den = den.Multiply(den).ReduceModPol(pol).Multiply(lineFunctionIn(T, T.Negate(), p2, ECa))
                        .ReduceModPol(pol);
            }

            if (l.get(i).compareTo(FlexiBigInt.ONE) == 0) {

                f1 = f1.Multiply(lineFunctionIn(T, p1, p2, ECa)).ReduceModPol(pol);
                T = T.Add(p1, ECa).ToAffine();

                if (!elimtrue) {
                    den = den.Multiply(lineFunctionIn(T, T.Negate(), p2, ECa)).ReduceModPol(pol);
                }

            }
        }

        f1 = f1.ReduceModPol(mypol);

        if (!elimtrue) {
            f1 = f1.Multiply(den.InvertMod(mypol)).ReduceModPol(mypol);
        }

        value = f1;
    }

    public GFP2toGFPPairing(FlexiBigInt myp) {
        value = new PolynomialMod(myp);
        xShift = new PolynomialMod(true, myp);
        yShift = new PolynomialMod(true, myp);

        pol = new PolynomialMod(myp, true);
    }

    public GFP2toGFPPairing(FlexiBigInt myp, PolynomialMod mypol, PolynomialMod myX, PolynomialMod myY) {
        value = new PolynomialMod(true, myp).Add(new PolynomialMod(mypol.deg - 1, myp));

        xShift = myX;
        yShift = myY;

        pol = mypol;
    }

    public Vector<FlexiBigInt> binaryDecomposition(FlexiBigInt l) {

        final int length = l.bitLength();
        FlexiBigInt currl;
        FlexiBigInt currpos;
        final Vector<FlexiBigInt> dec = new Vector<FlexiBigInt>(length);
        dec.setSize(length);
        final FlexiBigInt TWO = new FlexiBigInt("2"); //$NON-NLS-1$
        currl = l;

        for (int i = length - 1; i >= 0; i--) {
            currpos = currl.remainder(TWO);
            currl = currl.divide(TWO);
            dec.set(i, currpos);
        }

        return dec;
    }

    public PointGFP2 distort(PointGFP1 A) {
        final PointGFP1 p1 = A.ToAffine();
        PolynomialMod zres;
        final Vector<PolynomialMod> coeff = new Vector<PolynomialMod>(3);
        coeff.setSize(3);

        PolynomialMod xres = new PolynomialMod(0, p1.p, p1.xcoord);
        PolynomialMod yres = new PolynomialMod(0, p1.p, p1.ycoord);

        xres = xres.Multiply(xShift).ReduceModPol(pol);
        yres = yres.Multiply(yShift).ReduceModPol(pol);
        zres = new PolynomialMod(0, p1.p, new GFPElement(FlexiBigInt.ONE, p1.p));

        coeff.set(0, xres);
        coeff.set(1, yres);
        coeff.set(2, zres);

        final PointGFP2 result = new PointGFP2(A.p, true, coeff, pol);

        return result;
    }

    // 13 = 1 1 0 1 or in this writing 1 0 1 1

    public PolynomialMod lineFunctionIn(PointGFP2 A, PointGFP2 B, PointGFP1 Q, GFPElement ECa) {
        PolynomialMod result;

        final PointGFP2 AffA = A.ToAffine();
        final PointGFP2 AffB = B.ToAffine();

        final PointGFP1 AffQ = Q.ToAffine();
        final FlexiBigInt p = Q.p;
        final FlexiBigInt THREE = new FlexiBigInt("3"); //$NON-NLS-1$
        final GFPElement threemodP = new GFPElement(THREE, p);
        final PolynomialMod threemodPol = new PolynomialMod(0, p, threemodP);

        final PolynomialMod polQx = new PolynomialMod(0, p, AffQ.GetX());
        final PolynomialMod polQy = new PolynomialMod(0, p, AffQ.GetY());

        PolynomialMod polgrad; // gradient of line as polynomial
        final PolynomialMod mypol = A.GetPol();

        if (A.IsInfinity()) {
            return new PolynomialMod(true, p);
        }

        if (Q.IsInfinity()) {
            return new PolynomialMod(true, p);
        }

        if (A.IsInfinity()) {
            return new PolynomialMod(true, p);
        }

        if (AffA.EqualTo(AffB.Negate())) {
            return polQx.Subtract(AffA.GetX());
        }

        if (AffA.EqualTo(AffB)) {

            polgrad = threemodPol.Multiply(AffA.GetX()).Multiply(AffA.GetX()).ReduceModPol(mypol).Add(
                    new PolynomialMod(0, p, ECa)).ReduceModPol(mypol);
            polgrad = polgrad.Multiply((AffA.GetY().Add(AffA.GetY())).ReduceModPol(mypol).InvertMod(mypol))
                    .ReduceModPol(mypol);

        } else {
            polgrad = (AffB.GetY().Subtract(AffA.GetY())).ReduceModPol(mypol);
            polgrad = polgrad.Multiply((AffB.GetX().Subtract(AffA.GetX())).ReduceModPol(mypol).InvertMod(mypol))
                    .ReduceModPol(mypol);
        }

        result = polgrad.Multiply(polQx.Subtract(AffA.GetX())).ReduceModPol(mypol).Add(AffA.GetY()).Subtract(polQy)
                .ReduceModPol(mypol);
        return result;
    }

}
