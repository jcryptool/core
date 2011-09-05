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

public class GFPtoGFP2Pairing {

    public PolynomialMod value;
    public PolynomialMod xShift;
    public PolynomialMod yShift;

    private final PolynomialMod pol; // this is the polynomial that defines the GFP2 points.

    public GFPtoGFP2Pairing(boolean withexp, PolynomialMod myX, PolynomialMod myY, PointGFP1 A, PointGFP1 B,
            Vector<FlexiBigInt> l, boolean elimtrue, PolynomialMod mypol, FlexiBigInt lwhole, GFPElement ECa) {
        PointGFP1 p1, T;
        PointGFP2 p2;
        PolynomialMod f1, den, term;
        final Vector<GFPElement> coords = new Vector<GFPElement>(2);
        coords.setSize(2);
        FlexiBigInt power;

        xShift = myX;
        yShift = myY;
        pol = mypol;

        p1 = A.ToAffine();
        p2 = distort(B.ToAffine());

        power = p1.p.multiply(p1.p).subtract(FlexiBigInt.ONE).divide(lwhole);

        f1 = new PolynomialMod(true, p1.p);
        den = new PolynomialMod(true, p1.p);

        T = p1;
        PointGFP1 updatedT;

        for (int i = 1; i < l.size(); i++) {

            term = lineFunctionIn(T, T, p2, ECa);
            f1 = f1.Multiply(f1).Multiply(term).ReduceModPol(pol); // I added here ReduceModPol

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
            f1 = f1.Multiply(den.InvertMod(pol).ReduceModPol(pol)).ReduceModPol(mypol);
        }

        if (withexp) {
            f1 = f1.PowerModFBI(power, pol);
        }

        value = f1;
    }

    public GFPtoGFP2Pairing(FlexiBigInt myp) {
        value = new PolynomialMod(myp);
        xShift = new PolynomialMod(true, myp);
        yShift = new PolynomialMod(true, myp);

        pol = new PolynomialMod(myp, true);
    }

    public GFPtoGFP2Pairing(FlexiBigInt myp, PolynomialMod mypol, PolynomialMod myX, PolynomialMod myY) {
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

        xres = xres.MultiplyMod(xShift, pol);
        yres = yres.MultiplyMod(yShift, pol);
        zres = new PolynomialMod(0, p1.p, new GFPElement(FlexiBigInt.ONE, p1.p));

        coeff.set(0, xres);
        coeff.set(1, yres);
        coeff.set(2, zres);

        final PointGFP2 result = new PointGFP2(A.p, true, coeff, pol);

        return result;
    }

    // 13 = 1 1 0 1 or in this writing 1 0 1 1

    public PolynomialMod lineFunctionIn(PointGFP1 A, PointGFP1 B, PointGFP2 Q, GFPElement ECa) {
        PolynomialMod result;

        final PointGFP1 AffA = A.ToAffine();
        final PointGFP1 AffB = B.ToAffine();

        final PointGFP2 AffQ = Q.ToAffine();
        final FlexiBigInt p = A.p;
        final FlexiBigInt THREE = new FlexiBigInt("3"); //$NON-NLS-1$
        final GFPElement threemodP = new GFPElement(THREE, p);

        final PolynomialMod polAx = new PolynomialMod(0, p, AffA.GetX());
        final PolynomialMod polAy = new PolynomialMod(0, p, AffA.GetY());

        FlexiBigInt grad; // gradient of line
        PolynomialMod polgrad; // gradient of line as polynomial
        GFPElement gradmodp; // gradient mod p;
        final PolynomialMod mypol = Q.GetPol();

        if (A.IsInfinity()) {
            return new PolynomialMod(true, p);
        }

        if (Q.IsInfinity()) {
            return new PolynomialMod(true, p);
        }

        if (B.IsInfinity()) {
            return new PolynomialMod(true, p);
        }

        if (AffA.EqualTo(AffB.Negate())) {
            return AffQ.GetX().Subtract(polAx);
        }

        if (AffA.EqualTo(AffB)) {
            grad = (threemodP.multiply(AffA.GetX()).multiply(AffA.GetX()).add(ECa)).toFlexiBigInt();
            grad = grad.multiply(AffA.GetY().add(AffA.GetY()).invert().toFlexiBigInt());
            gradmodp = new GFPElement(grad, p);

        } else {
            grad = (AffB.GetY().subtract(AffA.GetY())).toFlexiBigInt();
            grad = grad.multiply(AffB.GetX().subtract(AffA.GetX()).invert().toFlexiBigInt());
            gradmodp = new GFPElement(grad, p);

        }

        polgrad = new PolynomialMod(0, p, gradmodp);

        result = polgrad.Multiply(AffQ.GetX().Subtract(polAx)).ReduceModPol(mypol).Add(polAy).Subtract(AffQ.GetY())
                .ReduceModPol(mypol);
        return result;
    }

}
