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

public class PointGFP2 {
    private PolynomialMod xcoord;
    private PolynomialMod ycoord;
    private PolynomialMod zcoord;

    private FlexiBigInt p;

    private boolean isJacobian;
    private PolynomialMod pol;

    // the default here is that the dimension is 2;

    public PointGFP2(FlexiBigInt myp, boolean isAffine, Vector<PolynomialMod> coordxy, PolynomialMod mypol) {
        p = myp;
        final PolynomialMod One = new PolynomialMod(0, p, new GFPElement(FlexiBigInt.ONE, p));
        pol = mypol;

        xcoord = coordxy.get(0);
        ycoord = coordxy.get(1);

        if (isAffine) {
            zcoord = new PolynomialMod(1, p, new GFPElement(FlexiBigInt.ZERO, p)).Add(One);
            isJacobian = false;
        } else {
            zcoord = coordxy.get(2);
            isJacobian = true;
        }

        xcoord = xcoord.ReduceModPol(pol);
        ycoord = ycoord.ReduceModPol(pol);
        zcoord = zcoord.ReduceModPol(pol);

    }

    public PointGFP2(FlexiBigInt myp, PolynomialMod mypol) {

        p = myp;
        pol = mypol;

        xcoord = new PolynomialMod(p).ReduceModPol(pol);
        ycoord = new PolynomialMod(p).ReduceModPol(pol);
        zcoord = new PolynomialMod(p).ReduceModPol(pol);

        isJacobian = true;

    }

    // creates a point with the given coordinates.

    public PointGFP2(FlexiBigInt myp, PolynomialMod mypol, boolean IsInfty) {
        p = myp;
        pol = mypol;

        xcoord = new PolynomialMod(0, myp);
        ycoord = new PolynomialMod(1, myp, GFPElement.ONE(p));
        zcoord = new PolynomialMod(0, myp);

        isJacobian = true;
    }

    public PointGFP2(FlexiBigInt myp, Vector<PolynomialMod> mycoord, PolynomialMod mypol) {
        p = myp;

        pol = mypol;

        xcoord = mycoord.get(0).ReduceModPol(pol);
        ycoord = mycoord.get(1).ReduceModPol(pol);
        zcoord = mycoord.get(2).ReduceModPol(pol);

        isJacobian = true;
    }

    public PointGFP2 Add(PointGFP2 Q, GFPElement a) {
        final PointGFP2 result = new PointGFP2(p, pol);

        if (IsInfinity()) {
            return Q;
        } else {
            if (Q.IsInfinity()) {
                return this;
            }
        }

        if (Q.EqualTo(this)) {
            return Double(a);
        }

        if (Q.EqualTo(Negate())) {
            return new PointGFP2(p, pol, true);
        }

        PolynomialMod z2z2, z2z2z2, z1z1, u1, u2, s1, s2, h, i, j, r, v;
        final FlexiBigInt TWO = new FlexiBigInt("2"); //$NON-NLS-1$
        final PolynomialMod TWOmodP = new PolynomialMod(0, p, new GFPElement(TWO, p));

        z2z2 = Q.zcoord.Multiply(Q.zcoord).ReduceModPol(pol);
        z2z2z2 = Q.zcoord.Multiply(z2z2).ReduceModPol(pol);

        z1z1 = zcoord.Multiply(zcoord).ReduceModPol(pol);
        u1 = xcoord.Multiply(z2z2).ReduceModPol(pol);
        u2 = Q.xcoord.Multiply(z1z1).ReduceModPol(pol);

        s1 = ycoord.Multiply(z2z2z2).ReduceModPol(pol);
        s2 = Q.ycoord.Multiply(zcoord.Multiply(z1z1).ReduceModPol(pol)).ReduceModPol(pol);

        h = u2.Subtract(u1).ReduceModPol(pol);
        i = TWOmodP.Multiply(h).Multiply(TWOmodP.Multiply(h)).ReduceModPol(pol);
        j = h.Multiply(i).ReduceModPol(pol);
        r = TWOmodP.Multiply(s2.Subtract(s1)).ReduceModPol(pol);
        v = u1.Multiply(i).ReduceModPol(pol);

        result.xcoord = r.Multiply(r).ReduceModPol(pol).Subtract(j).Subtract(TWOmodP.Multiply(v)).ReduceModPol(pol);
        result.ycoord = r.Multiply(v.Subtract(result.xcoord)).ReduceModPol(pol).Subtract(
                TWOmodP.Multiply(s1).Multiply(j).ReduceModPol(pol)).ReduceModPol(pol);
        final PolynomialMod z1plusz2 = zcoord.Add(Q.zcoord).ReduceModPol(pol);

        result.zcoord = (z1plusz2.Multiply(z1plusz2).ReduceModPol(pol).Subtract(z1z1).Subtract(z2z2)).Multiply(h)
                .ReduceModPol(pol);

        if (result.zcoord.IsOne()) {
            isJacobian = false;
        } else {
            isJacobian = true;
        }

        result.p = p;
        result.pol = pol;
        return result;
    }

    public PointGFP2 Double(GFPElement a) {
        final PointGFP2 result = new PointGFP2(p, pol);
        // FlexiBigInt mya;

        if (IsInfinity()) {
            return new PointGFP2(p, pol, true);
        }

        PolynomialMod xx, yy, yyyy, zz, s, m, t, x1plusyy;
        PolynomialMod y1plusz1;
        FlexiBigInt TWO, THREE, EIGHT;
        PolynomialMod TWOmodP, THREEmodP, EIGHTmodP, myamodP;

        TWO = new FlexiBigInt("2"); //$NON-NLS-1$
        THREE = new FlexiBigInt("3"); //$NON-NLS-1$
        EIGHT = new FlexiBigInt("8"); //$NON-NLS-1$
        // mya = a.toFlexiBigInt();

        TWOmodP = new PolynomialMod(0, p, new GFPElement(TWO, p));
        THREEmodP = new PolynomialMod(0, p, new GFPElement(THREE, p));
        EIGHTmodP = new PolynomialMod(0, p, new GFPElement(EIGHT, p));
        myamodP = new PolynomialMod(0, p, a);

        xx = xcoord.Multiply(xcoord).ReduceModPol(pol);
        yy = ycoord.Multiply(ycoord).ReduceModPol(pol);
        yyyy = yy.Multiply(yy).ReduceModPol(pol);
        zz = zcoord.Multiply(zcoord).ReduceModPol(pol);
        x1plusyy = xcoord.Add(yy).ReduceModPol(pol);

        s = TWOmodP.Multiply(
                x1plusyy.Multiply(x1plusyy).ReduceModPol(pol).Subtract(xx).ReduceModPol(pol).Subtract(yyyy)
                        .ReduceModPol(pol)).ReduceModPol(pol);
        m = THREEmodP.Multiply(xx).Add(myamodP.Multiply(zz.Multiply(zz).ReduceModPol(pol)).ReduceModPol(pol))
                .ReduceModPol(pol);
        t = m.Multiply(m).ReduceModPol(pol).Subtract(TWOmodP.Multiply(s)).ReduceModPol(pol);
        y1plusz1 = ycoord.Add(zcoord).ReduceModPol(pol);

        result.xcoord = t;
        result.ycoord = m.Multiply(s.Subtract(t).ReduceModPol(pol)).ReduceModPol(pol).Subtract(
                EIGHTmodP.Multiply(yyyy).ReduceModPol(pol)).ReduceModPol(pol);
        result.zcoord = y1plusz1.Multiply(y1plusz1).ReduceModPol(pol).Subtract(yy).Subtract(zz).ReduceModPol(pol);

        if (result.zcoord.IsOne()) {
            isJacobian = false;
        } else {
            isJacobian = true;
        }

        result.p = p;
        result.pol = pol;
        return result;
    }

    public boolean EqualTo(PointGFP2 q) {
        boolean areequal = true;
        PointGFP2 copyp, copyq;

        copyp = ToAffine();
        copyq = q.ToAffine();

        if (Getp().compareTo(q.Getp()) != 0) {
            areequal = false;
        }

        if (!GetPol().IsEqualTo(q.GetPol())) {
            areequal = false;
        }

        if (!copyp.GetX().IsEqualTo(copyq.GetX()) || !copyp.GetY().IsEqualTo(copyq.GetY())) {
            areequal = false;
        }

        return areequal;
    }

    public FlexiBigInt Getp() {
        return p;
    }

    public PolynomialMod GetPol() {
        return pol;
    }

    public PolynomialMod GetX() {
        return xcoord;
    }

    public PolynomialMod GetXAffine() {
        if (zcoord.IsZero()) {
            return xcoord;
        }

        if (isJacobian) {

            return xcoord.Multiply(zcoord.Multiply(zcoord).ReduceModPol(pol).InvertMod(pol)).ReduceModPol(pol);
            // Division(zcoord.Multiply(zcoord)).get(0).ReduceModPol(pol);
        } else {
            return xcoord;
        }
    }

    public PolynomialMod GetY() {
        return ycoord;
    }

    public PolynomialMod GetYAffine() {
        if (zcoord.IsZero()) {
            return ycoord;
        }

        if (isJacobian) {

            return ycoord.Multiply(
                    zcoord.Multiply(zcoord).ReduceModPol(pol).Multiply(zcoord).ReduceModPol(pol).InvertMod(pol))
                    .ReduceModPol(pol);
        } else {
            return ycoord;
        }
    }

    public PolynomialMod GetZ() {
        return zcoord;
    }

    public boolean IsInfinity() {
        boolean isinfinity = false;

        if (zcoord.IsZero()) {
            isinfinity = true;
        }

        return isinfinity;
    }

    // need to add the doubling method, need to add the generation of a point at infinity

    public PointGFP2 Negate() {
        PointGFP2 result;

        final Vector<PolynomialMod> rescoords = new Vector<PolynomialMod>(3);
        rescoords.setSize(3);

        rescoords.set(0, GetX());
        rescoords.set(1, GetY().Negate());
        rescoords.set(2, GetZ());

        result = new PointGFP2(p, rescoords, pol);
        return result;
    }

    public PointGFP2 ToAffine() {
        PointGFP2 result;

        result = this;

        result.xcoord = GetXAffine().ReduceModPol(result.pol);
        result.ycoord = GetYAffine().ReduceModPol(result.pol);

        if (zcoord.IsZero()) {
            result.zcoord = zcoord;
        } else {
            result.zcoord = new PolynomialMod(0, p, new GFPElement(FlexiBigInt.ONE, p));
        }

        result.isJacobian = false;

        return result;
    }

}
