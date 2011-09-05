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
import de.flexiprovider.common.math.ellipticcurves.EllipticCurveGFP;
import de.flexiprovider.common.math.ellipticcurves.PointGFP;
import de.flexiprovider.common.math.finitefields.GFPElement;

public class PointGFP1 {
    public GFPElement xcoord;
    public GFPElement ycoord;
    public GFPElement zcoord;
    public FlexiBigInt p;
    public boolean isJacobian;

    // creates a random point with x and y = 0, z = 1;
    public PointGFP1(FlexiBigInt myp) {
        p = myp;

        xcoord = new GFPElement(FlexiBigInt.ZERO, p);
        ycoord = new GFPElement(FlexiBigInt.ZERO, p);
        zcoord = new GFPElement(FlexiBigInt.ONE, p);

        isJacobian = false;
    }

    // creates point with given coords;

    public PointGFP1(FlexiBigInt myp, boolean IsInfinity) {
        p = myp;

        if (IsInfinity) {
            xcoord = new GFPElement(FlexiBigInt.ZERO, p);
            ycoord = new GFPElement(FlexiBigInt.ONE, p);
            zcoord = new GFPElement(FlexiBigInt.ZERO, p);
        }
    }

    // Creates point at infinity;

    public PointGFP1(FlexiBigInt myp, boolean IsAffine, Vector<GFPElement> mycoord) {
        p = myp;

        xcoord = mycoord.get(0);
        ycoord = mycoord.get(1);

        if (IsAffine) {
            zcoord = new GFPElement(FlexiBigInt.ONE, p);
            isJacobian = false;
        } else {
            zcoord = mycoord.get(2);
            isJacobian = true;
        }
    }

    public PointGFP1 Add(PointGFP1 q, GFPElement a) {
        PointGFP1 result;
        result = new PointGFP1(p);

        if (IsInfinity()) {
            return q;
        } else {
            if (q.IsInfinity()) {
                return this;
            }
        }

        if (q.EqualTo(this)) {
            return Double(a);
        }

        if (q.EqualTo(Negate())) {
            return new PointGFP1(p, true);
        }

        GFPElement z2z2, z2z2z2, z1z1, u1, u2, s1, s2, h, i, j, r, v;
        final FlexiBigInt TWO = new FlexiBigInt("2"); //$NON-NLS-1$
        final GFPElement TWOmodP = new GFPElement(TWO, p);

        z2z2 = new GFPElement(q.zcoord.multiply(q.zcoord).toFlexiBigInt(), p);
        z2z2z2 = new GFPElement(q.zcoord.multiply(z2z2).toFlexiBigInt(), p);

        z1z1 = new GFPElement(zcoord.multiply(zcoord).toFlexiBigInt(), p);
        u1 = new GFPElement(xcoord.multiply(z2z2).toFlexiBigInt(), p);
        u2 = new GFPElement(q.xcoord.multiply(z1z1).toFlexiBigInt(), p);

        s1 = new GFPElement(ycoord.multiply(z2z2z2).toFlexiBigInt(), p);
        s2 = new GFPElement(q.ycoord.multiply(zcoord.multiply(z1z1)).toFlexiBigInt(), p);

        h = new GFPElement(u2.subtract(u1).toFlexiBigInt(), p);
        // i = new GFPElement(intermed.multiply(intermed), this.p);
        i = new GFPElement(TWOmodP.multiply(h).multiply(TWOmodP.multiply(h)).toFlexiBigInt(), p);
        j = new GFPElement(h.multiply(i).toFlexiBigInt(), p);
        r = new GFPElement(TWO.multiply(s2.subtract(s1).toFlexiBigInt()), p);
        v = new GFPElement(u1.multiply(i).toFlexiBigInt(), p);

        result.xcoord = new GFPElement(r.multiply(r).subtract(j).subtract(TWOmodP.multiply(v)).toFlexiBigInt(), p);
        result.ycoord = new GFPElement(r.multiply(v.subtract(result.xcoord)).subtract(TWOmodP.multiply(s1).multiply(j))
                .toFlexiBigInt(), p);
        final GFPElement z1plusz2 = new GFPElement(zcoord.add(q.zcoord).toFlexiBigInt(), p);

        result.zcoord = new GFPElement((z1plusz2.multiply(z1plusz2).subtract(z1z1).subtract(z2z2)).multiply(h)
                .toFlexiBigInt(), p);

        if (result.zcoord.isOne()) {
            isJacobian = false;
        } else {
            isJacobian = true;
        }

        result.p = p;

        return result;
    }

    public Vector<FlexiBigInt> BinaryDecomposition(FlexiBigInt l) {
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

    public Vector<FlexiBigInt> BinaryDecompositionPrime(FlexiBigInt l) {
        final int length = l.bitLength();
        FlexiBigInt currl;
        FlexiBigInt currpos;
        final Vector<FlexiBigInt> dec = new Vector<FlexiBigInt>(length);
        dec.setSize(length);
        final FlexiBigInt TWO = new FlexiBigInt("2"); //$NON-NLS-1$
        currl = l;

        for (int i = 0; i < length; i++) {
            currpos = currl.remainder(TWO);
            currl = currl.divide(TWO);
            dec.set(i, currpos);
        }

        return dec;
    }

    public PointGFP2 Distort(PolynomialMod XShift, PolynomialMod YShift, PolynomialMod pol) {
        final PointGFP1 p1 = ToAffine();
        PolynomialMod zres;
        final Vector<PolynomialMod> coeff = new Vector<PolynomialMod>(3);
        coeff.setSize(3);

        PolynomialMod xres = new PolynomialMod(0, p1.p, p1.xcoord);
        PolynomialMod yres = new PolynomialMod(0, p1.p, p1.ycoord);

        xres = xres.MultiplyMod(XShift, pol);
        yres = yres.MultiplyMod(YShift, pol);
        zres = new PolynomialMod(0, p1.p, new GFPElement(FlexiBigInt.ONE, p1.p));

        coeff.set(0, xres);
        coeff.set(1, yres);
        coeff.set(2, zres);

        final PointGFP2 result = new PointGFP2(p, true, coeff, pol);

        return result;
    }

    public PointGFP1 Double(GFPElement a) {
        final PointGFP1 result = new PointGFP1(p);

        if (IsInfinity()) {
            return new PointGFP1(p, true);
        }

        GFPElement xx, yy, yyyy, zz, s, m, t, x1plusyy;
        GFPElement y1plusz1;
        FlexiBigInt TWO, THREE, EIGHT;
        GFPElement TWOmodP, THREEmodP, EIGHTmodP;

        TWO = new FlexiBigInt("2"); //$NON-NLS-1$
        THREE = new FlexiBigInt("3"); //$NON-NLS-1$
        EIGHT = new FlexiBigInt("8"); //$NON-NLS-1$

        TWOmodP = new GFPElement(TWO, p);
        THREEmodP = new GFPElement(THREE, p);
        EIGHTmodP = new GFPElement(EIGHT, p);

        xx = new GFPElement(xcoord.multiply(xcoord).toFlexiBigInt(), p);
        yy = new GFPElement(ycoord.multiply(ycoord).toFlexiBigInt(), p);
        yyyy = new GFPElement(yy.multiply(yy).toFlexiBigInt(), p);
        zz = new GFPElement(zcoord.multiply(zcoord).toFlexiBigInt(), p);
        x1plusyy = new GFPElement(xcoord.add(yy).toFlexiBigInt(), p);

        s = new GFPElement(TWOmodP.multiply(x1plusyy.multiply(x1plusyy).subtract(xx).subtract(yyyy)).toFlexiBigInt(), p);
        m = new GFPElement(THREEmodP.multiply(xx).add(a.multiply(zz.multiply(zz))).toFlexiBigInt(), p);
        t = new GFPElement(m.multiply(m).subtract(TWOmodP.multiply(s)).toFlexiBigInt(), p);
        y1plusz1 = new GFPElement(ycoord.add(zcoord).toFlexiBigInt(), p);

        result.xcoord = t;
        result.ycoord = new GFPElement(m.multiply(s.subtract(t)).subtract(EIGHTmodP.multiply(yyyy)).toFlexiBigInt(), p);
        result.zcoord = new GFPElement(y1plusz1.multiply(y1plusz1).subtract(yy).subtract(zz).toFlexiBigInt(), p);

        if (result.zcoord.isOne()) {
            isJacobian = false;
        } else {
            isJacobian = true;
        }

        result.p = p;

        return result;
    }

    public boolean EqualTo(PointGFP1 q) {
        boolean isEqual = false;

        if (IsInfinity()) {
            if (q.IsInfinity()) {
                return true;
            } else {
                return false;
            }
        }

        if ((GetXAffine().toFlexiBigInt().compareTo(q.GetXAffine().toFlexiBigInt()) == 0)
                && (GetYAffine().toFlexiBigInt().compareTo(q.GetYAffine().toFlexiBigInt()) == 0)) {
            isEqual = true;
        }

        return isEqual;
    }

    public String GetPrintP() {
        return Messages.PointGFP1_6 + xcoord.toString() + Messages.PointGFP1_0 + ycoord.toString() + Messages.PointGFP1_8
                + zcoord.toString() + Messages.PointGFP1_9 + p;
    }

    public GFPElement GetX() {
        return xcoord;
    }

    public GFPElement GetXAffine() {
        GFPElement res = GFPElement.ZERO(p);

        if (IsInfinity()) {
            return GFPElement.ZERO(p);
        }

        res = new GFPElement(xcoord.toFlexiBigInt().multiply(zcoord.multiply(zcoord).invert().toFlexiBigInt()), p);

        return res;
    }

    public GFPElement GetY() {
        return ycoord;
    }

    public GFPElement GetYAffine() {
        GFPElement res = GFPElement.ZERO(p);

        if (IsInfinity()) {
            return GFPElement.ZERO(p);
        }

        res = new GFPElement(ycoord.toFlexiBigInt().multiply(
                zcoord.multiply(zcoord).multiply(zcoord).invert().toFlexiBigInt()), p);

        return res;
    }

    public GFPElement GetZ() {
        return zcoord;
    }

    public boolean IsInfinity() {
        boolean isinfinity = false;

        if (zcoord.toFlexiBigInt().compareTo(FlexiBigInt.ZERO) == 0) {
            isinfinity = true;
        }

        return isinfinity;
    }

    public PointGFP1 Negate() {
        PointGFP1 result;
        result = new PointGFP1(p);

        result.xcoord = xcoord;
        result.ycoord = ycoord.negate();
        result.zcoord = zcoord;

        return result;
    }

    public Vector<PointGFP1> PreList(PointGFP1 theP, GFPElement a, int windowsize) {
        // has size 2^(k-1)
        final int size = (int) (Math.pow(2, windowsize - 1));

        final Vector<PointGFP1> result = new Vector<PointGFP1>(size);
        result.setSize(size);

        PointGFP1 P = theP;
        final PointGFP1 DoubleP = P.Double(a);

        for (int i = 0; i < size; i++) {
            result.set(i, P);
            P = P.Add(DoubleP, a);
        }

        return result;
    }

    // begin = 0; end = 2; kdesc = 10100;
    // res = 101 = 5

    public void PrintMultTableTo(int k, GFPElement a) {
        PointGFP1 T = this;

        for (int i = 1; i < k + 1; i++) {
            T = T.Add(this, a).ToAffine();
        }
    }

    public Vector<FlexiBigInt> PrintP() {
        final Vector<FlexiBigInt> coords = new Vector<FlexiBigInt>(3);
        coords.setSize(3);

        coords.set(0, GetX().toFlexiBigInt());
        coords.set(1, GetY().toFlexiBigInt());
        coords.set(2, GetZ().toFlexiBigInt());

        return coords;
    }

    public FlexiBigInt Reconstruct(Vector<FlexiBigInt> desc) {
        FlexiBigInt result = FlexiBigInt.ZERO;
        final FlexiBigInt TWO = new FlexiBigInt("2"); //$NON-NLS-1$
        FlexiBigInt currentpower = FlexiBigInt.ONE;
        for (int i = desc.size() - 1; i >= 0; i--) {
            result = result.add(currentpower.multiply(desc.get(i)));
            currentpower = currentpower.multiply(TWO);
        }

        return result;
    }

    public PointGFP1 ScalarMultiplication(GFPElement a, FlexiBigInt k, int windowsize, Vector<PointGFP1> list) {
        PointGFP1 Q = new PointGFP1(p, true);
        final Vector<FlexiBigInt> kdesc = BinaryDecomposition(k);
        int i = kdesc.size() - 1;
        int s;
        int uint;

        final int ndigits = kdesc.size() - 1;

        if (k.compareTo(FlexiBigInt.ONE) == 0) {
            return this;
        }

        if (k.compareTo(FlexiBigInt.ZERO) == 0) {
            return Q;
        }

        // k = 20 = 00101; i = 4; s = 2; h = 1; i - s + 1 = 3; Q = O;
        // Q = 5P;

        while (i >= 0) {
            if (kdesc.get(ndigits - i).compareTo(FlexiBigInt.ZERO) == 0) {
                Q = Q.Double(a);
                i--;
            } else {
                s = Math.max(i - windowsize + 1, 0);
                while (kdesc.get(ndigits - s).compareTo(FlexiBigInt.ZERO) == 0) {
                    s++;
                }
                for (int h = 1; h <= i - s + 1; h++) {
                    Q = Q.Double(a);
                }
                final FlexiBigInt u = Reconstruct(SelectFromTo(ndigits - i, ndigits - s, kdesc));
                uint = u.intValue();
                Q = Q.Add(list.get(((uint - 1) / 2)), a);
                i = s - 1;
            }
        }

        return Q;
    }

    // reduces the point to affine coordinates;

    public Vector<FlexiBigInt> SelectFromTo(int begin, int end, Vector<FlexiBigInt> v) {
        final int size = end - begin + 1;
        final Vector<FlexiBigInt> result = new Vector<FlexiBigInt>(size);
        result.setSize(size);
        // int ndigits = v.size()-1;

        for (int i = 0; i < size; i++) {
            result.set(i, v.get(i + begin));
        }

        return result;
    }

    public PointGFP1 Subtract(PointGFP1 q, GFPElement a) {
        return Add(q.Negate(), a);
    }

    public PointGFP1 ToAffine() {
        final PointGFP1 result = new PointGFP1(p);

        if (IsInfinity()) {
            return this;
        }

        result.xcoord = GetXAffine();
        result.ycoord = GetYAffine();
        result.zcoord = new GFPElement(FlexiBigInt.ONE, p);
        result.p = p;

        result.isJacobian = false;

        return result;
    }

    public PointGFP toPointGFP(EllipticCurveGFP E) {
        PointGFP result;

        if (IsInfinity()) {
            return new PointGFP(E);
        }

        result = new PointGFP(GetXAffine(), GetYAffine(), E);

        return result;
    }

}
