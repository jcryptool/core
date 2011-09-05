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

public class GFP2Element {
    // private FlexiBigInt Xcoord;
    // private FlexiBigInt Const;
    //
    // private Vector<FlexiBigInt> El = new Vector<FlexiBigInt>(2);

    public FlexiBigInt p;
    public FlexiBigInt a; // we create GF(p^2) by GF(p) mod x^2 + ax + b => x^2 = - ax - b;
    public FlexiBigInt b;

    public GFPElement X;
    public GFPElement c;

    private final Vector<GFPElement> pars = new Vector<GFPElement>(2);

    // we make a second constructor for the one element;
    public GFP2Element(boolean isOne, FlexiBigInt p, FlexiBigInt mya, FlexiBigInt myb) {
        final FlexiBigInt Xcoord = FlexiBigInt.ZERO;
        FlexiBigInt Const;

        if (isOne) {
            Const = FlexiBigInt.ONE;
        } else {
            Const = FlexiBigInt.ZERO;
        }
        pars.setSize(2);

        a = mya;
        b = mya;

        X = new GFPElement(Xcoord, p);
        c = new GFPElement(Const, p);

        pars.set(0, X);
        pars.set(1, c);
    }

    // the default here is the zero element;
    public GFP2Element(FlexiBigInt myp, FlexiBigInt mya, FlexiBigInt myb) {

        final FlexiBigInt Xcoord = FlexiBigInt.ZERO;
        final FlexiBigInt Const = FlexiBigInt.ZERO;
        p = myp;

        X = new GFPElement(Xcoord, p);
        c = new GFPElement(Const, p);

        pars.setSize(2);
        pars.set(0, X);
        pars.set(1, c);

        a = mya;
        b = mya;
    }

    // a third construction for general element;

    public GFP2Element(FlexiBigInt myX, FlexiBigInt myConst, FlexiBigInt myp, FlexiBigInt mya, FlexiBigInt myb) {
        p = myp;
        final FlexiBigInt Xcoord = myX.mod(p);
        final FlexiBigInt Const = myConst.mod(p);

        pars.setSize(2);

        X = new GFPElement(Xcoord, p);
        c = new GFPElement(Const, p);

        pars.set(0, X);
        pars.set(1, c);

        a = mya;
        b = myb;
    }

    public GFP2Element Add(GFP2Element other) {
        FlexiBigInt Xcoord, Const;

        final GFP2Element r = new GFP2Element(p, a, b);
        if (p.compareTo(other.p) == 0) {
            Xcoord = X.toFlexiBigInt().add(other.X.toFlexiBigInt()).mod(p);
            Const = c.toFlexiBigInt().add(other.c.toFlexiBigInt()).mod(p);
            r.p = p;
            r.X = new GFPElement(Xcoord, p);
            r.c = new GFPElement(Const, p);
        }

        r.a = a;
        r.b = b;

        return r;
    }

    public int Degree() {
        int d = 0;

        if (!X.isZero()) {
            d = 1;
        }

        return d;
    }

    // returns: position 0 is q -- the quotient, position 1 is r -- the remainder
    public Vector<GFP2Element> DivWithRem(GFP2Element other) {
        FlexiBigInt Xcoord, Const;

        final Vector<GFP2Element> result = new Vector<GFP2Element>(2);
        result.setSize(2);
        GFP2Element r1 = new GFP2Element(p, a, b);
        GFP2Element r2;

        final GFP2Element roundq = new GFP2Element(p, a, b);
        // GFP2Element roundr = new GFP2Element(this.p, this.a, this.b);

        GFP2Element pol1 = this;
        // GFP2Element pol2 = other;

        if (other.IsOne()) {
            result.set(0, this);
            result.set(1, new GFP2Element(p, a, b));
        }

        int deg1 = Degree();
        final int deg2 = other.Degree();

        if (deg1 < deg2) {
            result.set(0, new GFP2Element(p, a, b));
            result.set(1, this);
        }

        // 36 X + 44 / 0x + 2
        // r1 = [0,0]
        // roundq = [18,0]
        // r1 = [18,0]
        // pol1 = [0,44]
        // deg1 = 0
        // roundq = [0,22]

        while (deg1 >= deg2 && !pol1.IsZero()) {
            if (deg1 > deg2) {
                Xcoord = pol1.X.toFlexiBigInt().multiply(other.c.invert().toFlexiBigInt()).mod(p);
                Const = FlexiBigInt.ZERO;
            } else {
                if (deg1 == 1) {
                    Xcoord = FlexiBigInt.ZERO;
                    Const = pol1.X.toFlexiBigInt().multiply(other.X.invert().toFlexiBigInt()).mod(p);
                } else {
                    Xcoord = FlexiBigInt.ZERO;
                    Const = pol1.c.toFlexiBigInt().multiply(other.c.invert().toFlexiBigInt()).mod(p);
                }
            }

            roundq.X = new GFPElement(Xcoord, p);
            roundq.c = new GFPElement(Const, p);
            roundq.p = p;
            roundq.a = a;
            roundq.b = b;

            r1 = r1.Add(roundq);

            pol1 = pol1.Subtract(r1.Multiply(other));
            deg1 = pol1.Degree();
        }

        r2 = pol1;

        result.set(0, r1);
        result.set(1, r2);

        return result;
    }

    public Vector<GFP2Element> ExtGCD(GFP2Element p1, GFP2Element p2) {
        final Vector<GFP2Element> result = new Vector<GFP2Element>(2);
        result.setSize(2);
        Vector<GFP2Element> div = new Vector<GFP2Element>(2);
        div.setSize(2);

        GFP2Element x, y, u, v, q, repx, repy, repu;
        Vector<FlexiBigInt> pol1, pol2;

        pol1 = new Vector<FlexiBigInt>(2);
        pol2 = new Vector<FlexiBigInt>(2);

        pol1.setSize(2);
        pol2.setSize(2);

        pol2 = p1.Turn();

        pol2.set(0, p2.ReturnFBI(X));
        pol2.set(1, p2.ReturnFBI(c));

        x = new GFP2Element(true, p1.p, p1.a, p1.b);
        v = x;
        y = new GFP2Element(p1.p, p1.a, p1.b);
        u = y;

        while (!(pol2.get(0).compareTo(FlexiBigInt.ZERO) == 0 && pol2.get(1).compareTo(FlexiBigInt.ZERO) == 0)) {
            div = p1.DivWithRem(p2);
            p1 = p2;
            p2 = div.get(1);
            q = div.get(0);
            repx = x;
            repy = y;
            x = u;
            y = v;
            repu = u;
            u = repx.Subtract(q.Multiply(v));
            v = repy.Subtract(q.Multiply(repu));

            pol2 = p2.Turn();
        }

        result.set(0, x);
        result.set(1, y);

        return result;
    }

    public boolean IsOne() {
        boolean isone = false;

        if (X.isZero() && c.isOne()) {
            isone = true;
        }

        return isone;
    }

    public boolean IsZero() {
        boolean iszero = false;

        if (X.isZero() && c.isZero()) {
            iszero = true;
        }

        return iszero;
    }

    public GFP2Element Multiply(GFP2Element other) {
        FlexiBigInt Xcoord, Const;

        final GFP2Element r = new GFP2Element(p, a, b);
        if (IsZero() || other.IsZero()) {
            return r;
        }

        if (IsOne()) {
            return other;
        }

        if (other.IsOne()) {
            return this;
        }

        if (p.compareTo(other.p) == 0) {
            Xcoord = ((c.toFlexiBigInt().multiply(other.X.toFlexiBigInt()).mod(p)).add((X.toFlexiBigInt()
                    .multiply(other.c.toFlexiBigInt())).mod(p))).subtract(
                    X.toFlexiBigInt().multiply(other.X.toFlexiBigInt()).mod(p).multiply(a)).mod(p);
            Const = c.toFlexiBigInt().multiply(other.c.toFlexiBigInt()).mod(p).subtract(
                    X.toFlexiBigInt().multiply(other.X.toFlexiBigInt()).mod(p).multiply(b)).mod(p);
            r.p = p;

            r.X = new GFPElement(Xcoord, r.p);
            r.c = new GFPElement(Const, r.p);
        }

        r.a = a;
        r.b = b;

        return r;
    }

    public GFP2Element Negate() {
        final GFP2Element r = new GFP2Element(p, a, b);
        FlexiBigInt Xcoord, Const;

        if (IsZero()) {
            return r;
        }

        Xcoord = X.toFlexiBigInt().negate().mod(p);
        Const = c.toFlexiBigInt().negate().mod(p);
        r.p = p;

        r.X = new GFPElement(Xcoord, r.p);
        r.c = new GFPElement(Const, r.p);

        r.a = a;
        r.b = b;

        return r;
    }

    public FlexiBigInt ReturnFBI(GFPElement m) {
        FlexiBigInt r = FlexiBigInt.ZERO;

        if (m.isZero()) {
            r = p;
        } else {
            r = m.toFlexiBigInt();
        }

        return r;
    }

    public GFP2Element Subtract(GFP2Element other) {
        GFP2Element r;

        r = Add(other.Negate());

        return r;
    }

    public Vector<FlexiBigInt> Turn() {
        final Vector<FlexiBigInt> p1 = new Vector<FlexiBigInt>(2);
        p1.setSize(2);

        p1.set(0, ReturnFBI(X));
        p1.set(1, ReturnFBI(c));

        return p1;
    }

    // test case: x^2+1 binary. 1/x: p1 = X, p2 = X^2 + 1; I need to write a particular Invert() function.
    // need special method for: P[x]/p1.
    // x = [0,1]; y = [0,0]
    // p1 = X^2 + 1; div = [q,r] with q = 0 and r = X; p2 = X; x = 1; y=0; repu=0; u=
}
