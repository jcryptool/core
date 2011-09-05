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

public class PolynomialMod {
    public Vector<GFPElement> coeff;
    public int size;
    public int deg;
    public FlexiBigInt p;

    // creates the 0 polynomial modulo myp.

    public PolynomialMod(boolean IsOne, FlexiBigInt myp) {
        size = 1;
        p = myp;
        deg = 0;
        if (IsOne) {
            final Polynomial init = new Polynomial(IsOne);

            coeff = init.TurnToMod(p);
        } else {
            final Polynomial init = new Polynomial();

            coeff = init.TurnToMod(p);
        }

    }

    // creates the polynomial x^2 + 1.

    public PolynomialMod(boolean isGFP, Vector<GFPElement> mycoeff, FlexiBigInt myp) {
        size = mycoeff.size();
        deg = size - 1;
        p = myp;
        coeff = new Vector<GFPElement>(size);
        coeff.setSize(size);

        if (isGFP) {
            for (int i = 0; i < size; i++) {
                coeff.set(i, mycoeff.get(i));
            }
        } else {
            for (int i = 0; i < size; i++) {
                coeff.set(i, new GFPElement(FlexiBigInt.ZERO, p));
            }
        }

    }

    // creates the polynomial lc x^n.

    public PolynomialMod(FlexiBigInt myp) {
        size = 1;
        deg = 0;
        p = myp;

        final Polynomial init = new Polynomial(deg);

        coeff = init.TurnToMod(p);
    }

    // creates a polynomial of degree mydeg with coefficients 0 mod myp.

    public PolynomialMod(FlexiBigInt myp, boolean is101) {

        if (!is101) {
            size = 1;
            deg = 0;
            p = myp;

            final Polynomial init = new Polynomial(deg);

            coeff = init.TurnToMod(p);
        } else {
            final Vector<GFPElement> mycoords = new Vector<GFPElement>(3);
            mycoords.setSize(3);

            mycoords.set(0, new GFPElement(FlexiBigInt.ONE, myp));
            mycoords.set(1, new GFPElement(FlexiBigInt.ZERO, myp));
            mycoords.set(2, new GFPElement(FlexiBigInt.ONE, myp));

            coeff = mycoords;
            size = 3;
            deg = 2;

            p = myp;
        }
    }

    // creates polynomial of deg mydeg with coeffs 1 mod myp.

    public PolynomialMod(int mydeg, FlexiBigInt myp) {
        size = mydeg + 1;
        deg = mydeg;
        p = myp;

        final Polynomial init = new Polynomial(deg);
        coeff = init.TurnToMod(p);

    }

    // creates a polynomial of degree mydeg with coefficients mycoeff mod myp

    public PolynomialMod(int mydeg, FlexiBigInt myp, boolean coeffsone) {
        size = mydeg + 1;
        deg = mydeg;
        p = myp;

        coeff = new Vector<GFPElement>(size);
        coeff.setSize(size);

        if (coeffsone) {
            for (int i = 0; i < size; i++) {
                coeff.set(i, new GFPElement(FlexiBigInt.ONE, p));
            }
        } else {
            for (int i = 0; i < size; i++) {
                coeff.set(i, new GFPElement(FlexiBigInt.ZERO, p));
            }
        }
    }

    public PolynomialMod(int n, FlexiBigInt myp, GFPElement lc) {
        size = n + 1;
        deg = n;
        p = myp;

        coeff = new Vector<GFPElement>(size);
        coeff.setSize(size);

        coeff.set(0, lc);
        for (int i = 1; i < size; i++) {
            coeff.set(i, GFPElement.ZERO(p));
        }
    }

    // Creates the polynomial 1 mod myp.

    public PolynomialMod(Vector<FlexiBigInt> mycoeff, FlexiBigInt myp) {
        size = mycoeff.size();
        deg = size - 1;
        p = myp;

        final Polynomial init = new Polynomial(mycoeff, deg);
        coeff = init.TurnToMod(p);

    }

    // Turns the coefficients of this to FlexiBigInts

    public PolynomialMod Add(PolynomialMod q) {
        PolynomialMod result;

        final int size1 = size;
        final int size2 = q.size;

        int sizer, degr;
        PolynomialMod MaxPol, MinPol;

        if (size1 <= size2) {
            MaxPol = q;
            MinPol = this;
        } else {
            MaxPol = this;
            MinPol = q;
        }

        sizer = Math.max(size1, size2);
        degr = sizer - 1;

        result = new PolynomialMod(degr, p);

        if (p.compareTo(q.p) != 0) {
            return result;
        }

        final int diff = sizer - Math.min(size1, size2);

        for (int i = sizer - 1; i >= 0; i--) {

            if (i > diff - 1) {
                result.coeff.set(i, new GFPElement(MaxPol.coeff.get(i).toFlexiBigInt().add(
                        MinPol.coeff.get(i - diff).toFlexiBigInt()), p));
            } else {
                result.coeff.set(i, MaxPol.coeff.get(i));
            }
        }

        return result.ReduceSize();
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

    public Vector<FlexiBigInt> CoeffToSize(int n) {
        final int extra = n - size;
        final PolynomialMod changed = ExtendLeftBy(extra);

        return changed.getCoeff();
    }

    public PolynomialMod DivideAllCoeffsBy(GFPElement a) {
        final PolynomialMod result = this;
        for (int i = 0; i < result.size; i++) {
            result.coeff.set(i, new GFPElement(result.coeff.get(i).multiply(a.invert()).toFlexiBigInt(), p));
        }

        return result;
    }

    // checks if this is the zero polynomial.

    public Vector<PolynomialMod> Division(PolynomialMod q) {
        final Vector<PolynomialMod> result = new Vector<PolynomialMod>(2);
        result.setSize(2);

        PolynomialMod quot, rem, intermed, rest;
        int currpos, degrem, sizei;
        FlexiBigInt inverse;

        currpos = 0;

        final int deg1 = deg;
        final int deg2 = q.deg;

        final int size1 = size;

        sizei = size1;

        result.set(0, new PolynomialMod(0, p));
        result.set(1, new PolynomialMod(0, p));

        if (p.compareTo(q.p) != 0) {
            return result;
        }

        if (q.IsZero()) {
            return result;
        }

        if (deg1 < deg2) {
            result.set(0, new PolynomialMod(0, p));
            result.set(1, this);
        } else {
            degrem = deg1;
            intermed = this;

            quot = new PolynomialMod(deg1 - deg2, p, true);
            inverse = q.coeff.get(0).invert().toFlexiBigInt();

            if (deg2 == 0) {
                for (int i = 0; i < quot.size; i++) {
                    quot.coeff.set(i, new GFPElement(coeff.get(i).toFlexiBigInt().multiply(inverse), p));
                }

                rest = new PolynomialMod(p);

                result.set(0, quot);
                result.set(1, rest);

                return result;
            }

            // currpos = 0; [1 0 1]; [57]; deg1 = 2; deg2 = 0; size1 = 3; size2 = 1
            // sizei = 3. result = [0 0]; intermed2 = 0;
            // degrem = 2; intermed = [1 0 1]; quot = [0 0 0]; inverse = 1/17 = 108.
            // quot = [108 0 0], rest = [0]
            // quot = [0 0 1]

            while (degrem >= deg2) {
                quot.coeff.set(currpos, new GFPElement(intermed.coeff.get(0).toFlexiBigInt().multiply(inverse), p));
                rest = new PolynomialMod(quot.deg - currpos, p, quot.coeff.get(currpos));
                intermed = intermed.Subtract(q.Multiply(rest)).ReduceSize();

                sizei--;
                degrem = intermed.deg;
                if (intermed.deg >= sizei - intermed.size) {
                    intermed = intermed.ExtendSizeBy(sizei - intermed.size);
                }

                currpos++;
            }

            rem = intermed.ReduceSize();
            result.set(0, quot);
            result.set(1, rem);
        }

        return result;
    }

    public boolean Equals(PolynomialMod q) {
        boolean result = true;

        if (deg != q.deg) {
            return false;
        }

        if (size != q.size) {
            return false;
        }

        if (p.compareTo(q.p) != 0) {
            return false;
        }

        int i = 0;
        while (result && i < coeff.size()) {
            if (coeff.get(i).toFlexiBigInt().compareTo(q.coeff.get(i).toFlexiBigInt()) != 0) {
                result = false;
            }
            i++;
        }

        return result;
    }

    public PolynomialMod ExtendLeftBy(int n) {
        PolynomialMod result;
        int sizer, degr;

        sizer = size + n;
        degr = sizer - 1;

        result = this;

        result.size = sizer;
        result.deg = degr;

        if (n == 0) {
            return this;
        }

        for (int i = 0; i < n; i++) {
            result.coeff.add(0, GFPElement.ZERO(p));
        }

        return result;

    }

    // public PolynomialMod Negate(){
    //
    // }

    public PolynomialMod ExtendSizeBy(int n) {
        PolynomialMod result;
        int sizer, degr;

        if (n == 0) {
            return this;
        }

        sizer = size + n;
        degr = sizer - 1;

        result = this;

        for (int i = size; i < sizer; i++) {
            result.coeff.add(i, GFPElement.ZERO(p));
        }

        result.size = sizer;
        result.deg = degr;

        return result;
    }

    // reduces the size of the polynomial to the first non-zero coefficients.

    public Vector<PolynomialMod> ExtEuclid(PolynomialMod q) {
        final Vector<PolynomialMod> result = new Vector<PolynomialMod>(3);
        result.setSize(3);

        Vector<PolynomialMod> div = new Vector<PolynomialMod>(2);
        div.setSize(2);

        if (q.IsZero()) {
            result.set(0, this);
            result.set(1, new PolynomialMod(true, p));
            result.set(2, new PolynomialMod(p));

            return result;
        }

        PolynomialMod u, v, s, g, t;

        u = new PolynomialMod(0, p, true);
        v = new PolynomialMod(0, p);
        s = q;
        g = this;

        while (!s.IsZero()) {
            div = g.Division(s);
            t = u.Subtract(v.Multiply(div.get(0)));
            u = v;
            g = s;
            v = t;
            s = div.get(1);
        }

        div = (g.Subtract(Multiply(u))).Division(q);
        v = div.get(0);

        result.set(0, u);
        result.set(1, v);
        result.set(2, g);
        return result;
    }

    // adds this to q.

    public Vector<FlexiBigInt> getCoeff() {
        final Vector<FlexiBigInt> mycoeff = new Vector<FlexiBigInt>(size);
        mycoeff.setSize(size);

        for (int i = 0; i < size; i++) {
            mycoeff.set(i, coeff.get(i).toFlexiBigInt());
        }

        return mycoeff;
    }

    // adds zeroes mod myp to the left of the polynomial.

    public PolynomialMod InvertMod(PolynomialMod pol) {
        PolynomialMod result;
        result = new PolynomialMod(p);

        if (IsZero()) {
            return result;
        }
        Vector<PolynomialMod> r = new Vector<PolynomialMod>(3);
        r.setSize(3);

        final int degpol = pol.deg;

        final PolynomialMod thispol = ReduceSize();
        final int degthis = thispol.deg;
        final PolynomialMod compterm = new PolynomialMod(thispol.deg, p, thispol.coeff.get(0)); // this polynomial
                                                                                                // stores
        // the polynomial lc x^n
        // for n the degree of
        // thispol

        final GFPElement minone = new GFPElement(FlexiBigInt.ONE.negate(), p);
        final GFPElement invcoeff = new GFPElement(thispol.coeff.get(0).multiply(minone).invert().toFlexiBigInt(), p);

        if (thispol.Equals(compterm)) {
            return new PolynomialMod(degpol - degthis, p, invcoeff);
        }

        r = ExtEuclid(pol);

        result = r.get(0);

        final GFPElement lc = r.get(2).ReduceSize().coeff.get(0); // here I changed 2 to 1.
        result = result.DivideAllCoeffsBy(lc);

        return result;
    }

    // Subtracts q from this.

    public boolean IsEqualTo(PolynomialMod q) {
        boolean areEqual = true;
        int i = size - 1;

        while (i >= 0 && areEqual) {
            if (coeff.get(i).toFlexiBigInt().compareTo(q.coeff.get(i).toFlexiBigInt()) != 0) {
                areEqual = false;
            }

            i--;
        }

        return areEqual;
    }

    // multiplies this by A x^n and reduces the coefficients mod p.

    public boolean IsOne() {
        boolean isOne = true;
        int i = size - 1;

        while (i >= 0 && isOne) {
            if (!coeff.get(i).isOne()) {
                isOne = false;
            }

            i--;
        }

        return isOne;
    }

    // multiplies q by this.

    public boolean IsZero() {
        boolean isZero = true;
        int i = size - 1;

        while (i >= 0 && isZero) {
            if (!coeff.get(i).isZero()) {
                isZero = false;
            }

            i--;
        }

        return isZero;
    }

    // extends the size of the polynomial to the right by adding zero coefficients.

    public PolynomialMod MultByAXn(GFPElement A, int n) {
        PolynomialMod result;
        int sizer, degr;
        sizer = size + n;
        degr = sizer - 1;

        result = new PolynomialMod(degr, p);

        for (int i = 0; i < sizer; i++) {
            if (i < size) {
                result.coeff.set(i, new GFPElement(A.toFlexiBigInt().multiply(coeff.get(i).toFlexiBigInt()), p));
            } else {
                result.coeff.set(i, GFPElement.ZERO(p));
            }
        }

        return result.ReduceSize();
    }

    // returns a vector [q,r] with q being the quotient and r being the remainder
    // of this divided by q.

    public PolynomialMod Multiply(PolynomialMod q) {
        PolynomialMod result, intermed, MinPol, MaxPol;
        int degr;
        int minsize;

        degr = deg + q.deg;

        result = new PolynomialMod(degr, p);

        if (p.compareTo(q.p) != 0) {
            return result;
        }

        if (size <= q.size) {
            minsize = size;
            MinPol = this;
            MaxPol = q;
        } else {
            minsize = q.size;
            MinPol = q;
            MaxPol = this;
        }

        // [1,2] * [3,1,1,0] = (x+2)(3x^3+x^2+x)
        // minsize = 2; maxsize = 4; MinPol = [1,2]; MaxPol = [3,1,1,0]
        // i = 0: intermed = [3,1,1,0] * 2 = [6,2,2,0]; result = [6,2,2,0]
        // i = 1; intermed = [3,1,1,0] * x = [3,1,1,0,0]; result = [3,7,3,2,0]

        for (int i = 0; i < minsize; i++) {
            intermed = MaxPol.MultByAXn(MinPol.coeff.get(MinPol.deg - i), i);
            result = result.Add(intermed);
        }

        return result.ReduceSize();
    }

    public PolynomialMod MultiplyMod(PolynomialMod q, PolynomialMod pol) {
        PolynomialMod p1, p2;

        p1 = ReduceModPol(pol);
        p2 = q.ReduceModPol(pol);

        return p1.Multiply(p2).ReduceModPol(pol);
    }

    public PolynomialMod Negate() {
        final Vector<GFPElement> result = new Vector<GFPElement>(size);
        result.setSize(size);

        for (int i = 0; i < size; i++) {
            result.set(i, coeff.get(i).negate());
        }

        return new PolynomialMod(true, result, p);
    }

    public PolynomialMod PowerMod(int power, PolynomialMod pol) {
        final FlexiBigInt pow = new FlexiBigInt(Integer.toString(power));
        PolynomialMod currentpos;

        Vector<FlexiBigInt> powdec;
        powdec = BinaryDecomposition(pow);

        PolynomialMod result;
        result = new PolynomialMod(0, p, GFPElement.ONE(p));

        if (power == 0) {
            return result;
        }

        currentpos = this;

        if (powdec.get(powdec.size() - 1).compareTo(FlexiBigInt.ONE) == 0) {
            result = this;
        }

        for (int j = powdec.size() - 2; j >= 0; j--) {
            currentpos = currentpos.MultiplyMod(currentpos, pol);
            if (powdec.get(j).compareTo(FlexiBigInt.ONE) == 0) {
                result = currentpos.MultiplyMod(result, pol);
            }
        }

        return result;
    }

    public PolynomialMod PowerModFBI(FlexiBigInt power, PolynomialMod pol) {
        PolynomialMod currentpos;

        Vector<FlexiBigInt> powdec;
        powdec = BinaryDecomposition(power);

        PolynomialMod result;
        result = new PolynomialMod(0, p, true);

        if (power.compareTo(FlexiBigInt.ZERO) == 0) {
            return result;
        }

        currentpos = this;

        if (powdec.get(powdec.size() - 1).compareTo(FlexiBigInt.ONE) == 0) {
            result = this;
        }

        for (int j = powdec.size() - 2; j >= 0; j--) {
            currentpos = currentpos.MultiplyMod(currentpos, pol);

            if (powdec.get(j).compareTo(FlexiBigInt.ONE) == 0) {
                result = currentpos.MultiplyMod(result, pol);
            }
        }

        return result;

    }

    public String PrintP() {
        String s = ""; //$NON-NLS-1$
        Vector<FlexiBigInt> supdate;

        supdate = getCoeff();

        if (size > 0) {
            s += supdate.get(0).toString();
            for (int i = 1; i < size; i++) {
                s = s + "|" + supdate.get(i).toString(); //$NON-NLS-1$
            }
        }

        return s;
    }

    public PolynomialMod ReduceModPol(PolynomialMod pol) {
        PolynomialMod T, result, lc, intermed;
        T = this;
        Vector<GFPElement> coeff;
        final Vector<GFPElement> mycoeff = new Vector<GFPElement>(pol.deg);

        mycoeff.setSize(pol.deg);

        final int size = pol.deg;

        if (T.deg < size) {
            return T;
        }

        else {
            for (int i = 0; i < size; i++) {
                mycoeff.set(i, T.coeff.get(i + T.size - pol.deg));
            }
            result = new PolynomialMod(true, mycoeff, p);

            for (int i = 0; i < size; i++) {
                mycoeff.set(i, pol.coeff.get(i + 1).negate());
            }
            lc = new PolynomialMod(true, mycoeff, p);

            while (T.deg > size - 1) {
                coeff = new Vector<GFPElement>(T.size - size);
                coeff.setSize(T.size - size);

                for (int i = 0; i < T.size - size; i++) {
                    coeff.set(i, T.coeff.get(i));
                }

                T = new PolynomialMod(true, coeff, p);
                T = T.Multiply(lc);

                if (T.size <= size) {
                    for (int i = 0; i < size - T.size; i++) {
                        mycoeff.set(i, GFPElement.ZERO(p));
                    }
                    for (int j = size - T.size; j < size; j++) {
                        mycoeff.set(j, T.coeff.get(j - size + T.size));
                    }
                } else {
                    for (int i = 0; i < size; i++) {
                        mycoeff.set(i, T.coeff.get(i + T.size - pol.deg));
                    }
                }

                intermed = new PolynomialMod(true, mycoeff, p);
                result = result.Add(intermed);
            }
        }
        return result;
    }

    public PolynomialMod ReduceSize() {
        PolynomialMod result;
        int sizer;
        int degr;
        int i = 0;
        boolean posiszero = true;

        if (IsZero()) {
            return new PolynomialMod(0, p);
        } else {
            while (i < size && posiszero) {
                if (!coeff.get(i).isZero()) {
                    posiszero = false;
                }
                i++;
            }
            sizer = size - i + 1;
        }

        degr = sizer - 1;
        result = new PolynomialMod(degr, p);

        for (int j = 0; j < sizer; j++) {
            result.coeff.set(j, coeff.get(j + i - 1));
        }

        return result;
    }

    // 13 = 1101. result = 1; currentpos = P; result = P;
    // j = 2; currentpos = P^2 mod pol;
    // j = 1; currentpos = P^4 mod pol; result = P^5 mod pol.
    // j = 0; currentpos = p^8 mod pol; result = P^13 mod pol.

    public PolynomialMod Subtract(PolynomialMod q) {
        return Add(q.Negate());
    }

    public Polynomial TurnToPol() {
        final Vector<FlexiBigInt> result = new Vector<FlexiBigInt>(size);
        result.setSize(size);
        Polynomial pol;

        pol = new Polynomial(deg);

        pol.size = size;
        pol.deg = deg;

        for (int i = 0; i < size; i++) {
            result.set(i, coeff.get(i).toFlexiBigInt());
        }

        pol.coeff = result;

        return pol;
    }

}