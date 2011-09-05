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

public class Polynomial {
    public int size;
    public Vector<FlexiBigInt> coeff;
    public int deg;

    // creates the 0 polynomial;

    public Polynomial() {
        size = 1;
        coeff = new Vector<FlexiBigInt>(size);
        coeff.setSize(size);
        deg = 0;

        coeff.set(0, FlexiBigInt.ZERO);
    }

    // creates a polynomial of degree mydeg with all coefficients 0.

    public Polynomial(boolean IsOne) {
        size = 1;
        coeff = new Vector<FlexiBigInt>(size);
        coeff.setSize(size);
        deg = 0;

        coeff.set(0, FlexiBigInt.ONE);
    }

    // creates a polynomial of degree mydeg with the coefficients in mycoeff.

    public Polynomial(int mydeg) {
        size = mydeg + 1;
        deg = mydeg;

        coeff = new Vector<FlexiBigInt>(size);
        coeff.setSize(size);

        for (int i = 0; i < size; i++) {
            coeff.set(i, FlexiBigInt.ZERO);
        }
    }

    // creates the One polynomial.

    public Polynomial(Vector<FlexiBigInt> mycoeff, int mydeg) {
        size = mydeg + 1;
        coeff = new Vector<FlexiBigInt>(size);
        coeff.setSize(size);

        deg = mydeg;

        coeff = mycoeff;
    }

    // turns the coefficients of a polynomial into GFPElements mod p.

    public Polynomial Add(Polynomial q) {
        final int size1 = size;
        final int size2 = q.size;

        int sizer, degr;
        Polynomial result, MaxPol, MinPol;

        if (size1 <= size2) {
            MaxPol = q;
            MinPol = this;
        } else {
            MaxPol = this;
            MinPol = q;
        }

        sizer = Math.max(size1, size2);
        degr = sizer - 1;

        result = new Polynomial(degr);

        final int diff = sizer - Math.min(size1, size2);

        for (int i = sizer - 1; i >= 0; i--) {

            if (i > diff - 1) {
                result.coeff.set(i, MaxPol.coeff.get(i).add(MinPol.coeff.get(i - diff)));
            } else {
                result.coeff.set(i, MaxPol.coeff.get(i));
            }
        }

        return result.ReduceSize();
    }

    // reduces the size by reducing the degree to the first non-zero

    public Polynomial ExtendLeftBy(int n) {
        Polynomial result;
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
            result.coeff.add(0, FlexiBigInt.ZERO);
        }

        return result;

    }

    // adds two polynomials

    public boolean IsZero() {
        boolean isZero = true;
        int i = size - 1;

        while (i >= 0 && isZero) {
            if (coeff.get(i).compareTo(FlexiBigInt.ZERO) != 0) {
                isZero = false;
            }

            i--;
        }

        return isZero;

    }

    // adds a few zeroes to the left of the polynomial coefficient.

    public Polynomial MultByAXn(FlexiBigInt A, int n) {
        Polynomial result;
        int sizer, degr;
        sizer = size + n;
        degr = sizer - 1;

        result = new Polynomial(degr);

        for (int i = 0; i < sizer; i++) {
            if (i < size) {
                result.coeff.set(i, A.multiply(coeff.get(i)));
            } else {
                result.coeff.set(i, FlexiBigInt.ZERO);
            }
        }

        return result.ReduceSize();
    }

    // subtracts q from this

    public Polynomial Multiply(Polynomial q) {
        Polynomial result, intermed, MinPol, MaxPol;
        int degr;
        int minsize;

        degr = deg + q.deg;

        result = new Polynomial(degr);

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

    // checks if the polynomial is zero.

    public Polynomial ReduceSize() {
        Polynomial result;
        int sizer;
        int degr;
        int i = 0;
        boolean posiszero = true;

        if (IsZero()) {
            return this;
        } else {
            while (i < size && posiszero) {
                if (coeff.get(i).compareTo(FlexiBigInt.ZERO) != 0) {
                    posiszero = false;
                }
                i++;
            }
            sizer = size - i + 1;
        }

        degr = sizer - 1;
        result = new Polynomial(degr);

        for (int j = 0; j < sizer; j++) {
            result.coeff.set(j, coeff.get(j + i - 1));
        }

        return result;
    }

    // [2] * [3,1,1,0]
    // [2,0] * [3,1,1,0]

    // performs a multiplication of this by A x^n.

    public Polynomial Subtract(Polynomial q) {
        Polynomial thisprime, qprime;
        // boolean qisgreatest = false;

        final int size1 = size;
        final int size2 = q.size;

        int sizer, degr;
        Polynomial result;

        if (size1 <= size2) {
            thisprime = ExtendLeftBy(size2 - size1);
            qprime = q;
            // qisgreatest = true;
        } else {
            thisprime = this;
            qprime = q.ExtendLeftBy(size1 - size2);
            // MaxPol = this;
        }

        sizer = Math.max(size1, size2);
        degr = sizer - 1;

        result = new Polynomial(degr);

        for (int i = 0; i < sizer; i++) {
            result.coeff.set(i, thisprime.coeff.get(i).subtract(qprime.coeff.get(i)));
        }

        return result.ReduceSize();
    }

    // multiplies this by q.

    public Vector<GFPElement> TurnToMod(FlexiBigInt p) {
        final Vector<GFPElement> result = new Vector<GFPElement>(size);
        result.setSize(size);

        for (int i = 0; i < size; i++) {
            result.set(i, new GFPElement(coeff.get(i), p));
        }

        return result;
    }

    // public Vector<Polynomial> Divide(Polynomial q){
    // Vector<Polynomial> result = new Vector<Polynomial>(2);
    // result.setSize(2);
    // Polynomial quot, rem, intermed;
    // int currpos, degrem;
    //
    // int deg1 = this.deg;
    // int deg2 = q.deg;
    //
    // int size1 = this.size;
    // int size2 = q.size;
    //
    // if (deg1 < deg2){
    // result.set(0, new Polynomial(0));
    // result.set(1, this);
    // }
    // else{
    // degrem = deg1;
    // intermed = this;
    //
    // quot = new Polynomial(deg1 - deg2);
    //
    // while(degrem>=deg2){
    //
    // }
    // }
    //
    //
    // return result;
    // }

    // [3,7,3,2,0] : [1,2] = [3,1,1,0]

}
