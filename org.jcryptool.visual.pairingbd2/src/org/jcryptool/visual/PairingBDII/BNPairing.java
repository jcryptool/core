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
package org.jcryptool.visual.PairingBDII;

/**
 * BNPairing.java
 *
 * Bilinear pairings over Barreto-Naehrig (BN) elliptic curves.
 *
 * Copyright (C) Paulo S. L. M. Barreto, Michael Naehrig and Peter Schwabe.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

import java.math.BigInteger;

public class BNPairing {

    /**
     * Convenient BigInteger constants
     */
    private static final BigInteger _0 = BigInteger.valueOf(0L), _1 = BigInteger.valueOf(1L), _3 = BigInteger
            .valueOf(3L);
    public static final String incompatibleCurves = Messages.BNPairing_0;

    public BNCurve E;
    public BNCurve2 E2;
    public BNParams bn;

    public BNField12 Fp12_0, Fp12_1;

    public BNPairing(BNCurve2 Et) {
        E2 = Et;
        E = Et.E;
        bn = E.bn;
        Fp12_0 = new BNField12(bn, _0);
        Fp12_1 = new BNField12(bn, _1);
    }

    public BNField12 ate(BNPoint2 Q, BNPoint P) {
        if (!E.contains(P) || !E2.contains(Q)) {
            throw new IllegalArgumentException(incompatibleCurves);
        }
        BNField12 f = Fp12_1;
        P = P.normalize();
        Q = Q.normalize();
        if (!P.isZero() && !Q.isZero()) {
            final BNParams bn = E.bn;
            BNPoint2 T = Q;
            final BigInteger ord = bn.t.subtract(_1);
            for (int i = ord.bitLength() - 2; i >= 0; i--) {
                final BNPoint2 A = T.twice(1);
                f = f.square().multiply(gl(T, T, A, P));
                T = A;
                if (ord.testBit(i)) {
                    f = f.multiply(gl(T, Q, null, P));
                    T = T.add(Q);
                }
            }
            f = f.finExp();
        }
        return f;
    }

    /**
     * Compute the eta (sometimes called twisted ate) pairing for points P and Q on BN curves E and E'.
     */

    public BNField12 eta(BNPoint P, BNPoint2 Q) {
        if (!E.contains(P) || !E2.contains(Q)) {
            throw new IllegalArgumentException(incompatibleCurves);
        }
        BNField12 f = Fp12_1;
        P = P.normalize();
        Q = Q.normalize();
        if (!P.isZero() && !Q.isZero()) {
            final BNParams bn = E.bn;
            BNPoint V = P;
            final BigInteger ord = bn.rho; // the Tate pairing would have order bn.n instead of bn.rho
            for (int i = ord.bitLength() - 2; i >= 0; i--) {
                final BNPoint A = V.twice(1);
                f = f.square().multiply(gl(V, V, A, Q));
                V = A;
                if (ord.testBit(i)) {
                    f = f.multiply(gl(V, P, null, Q));
                    V = V.add(P);
                }
            }
            f = f.finExp();
        }
        return f;
    }

    BNField12 gl(BNPoint V, BNPoint P, BNPoint A, BNPoint2 Q) {
        BigInteger n, d;
        final BigInteger p = bn.p;
        if (V.isZero() || P.isZero() || Q.isZero() || V.opposite(P)) {
            return Fp12_1;
        }
        final BigInteger Vz3 = V.z.multiply(V.z).multiply(V.z).mod(p);
        if (A != null) {
            n = A.m;
            d = A.z;
        } else if (V.equals(P)) {
            n = V.x.multiply(V.x).multiply(_3);// .mod(p);
            d = V.y.multiply(V.z).shiftLeft(1);// .mod(p);
        } else {
            assert (P.z.compareTo(_1) == 0);
            n = P.y.multiply(Vz3).subtract(V.y);// .mod(p);
            d = P.x.multiply(Vz3).subtract(V.x.multiply(V.z));// .mod(p);
        }
        // lambda = n/d
        final BNField2[] w = new BNField2[6];
        w[0] = new BNField2(bn, d.multiply(V.y).subtract(n.multiply(V.x).multiply(V.z)));
        w[2] = Q.x.multiply(n.multiply(Vz3));
        w[3] = Q.y.multiply(p.subtract(d).multiply(Vz3));
        w[1] = w[4] = w[5] = E2.Fp2_0;
        return new BNField12(bn, w);
    }

    BNField12 gl(BNPoint2 T, BNPoint2 Q, BNPoint2 A, BNPoint P) {
        BNField2 n, d;
        if (T.isZero() || P.isZero() || Q.isZero() || T.opposite(Q)) {
            return Fp12_1;
        }
        final BNField2 Tz3 = T.z.cube();
        if (A != null) {
            n = A.m;
            d = A.z;
        } else if (T.equals(Q)) {
            n = T.x.square().multiply(_3);
            d = T.y.multiply(T.z).twice(1);
        } else {
            assert (Q.z.isOne());
            n = Q.y.multiply(Tz3).subtract(T.y);
            d = Q.x.multiply(Tz3).subtract(T.x.multiply(T.z));
        }
        // lambda = n/d
        final BNField2[] w = new BNField2[6];
        w[0] = d.multiply(bn.p.subtract(P.y)).multiply(Tz3);
        w[1] = n.multiply(P.x).multiply(Tz3);
        w[3] = d.multiply(T.y).subtract(n.multiply(T.x).multiply(T.z));
        w[2] = w[4] = w[5] = E2.Fp2_0;
        return new BNField12(bn, w);
    }

    public BNField12 opt(BNPoint2 Q, BNPoint P) {
        if (!E.contains(P) || !E2.contains(Q)) {
            throw new IllegalArgumentException(incompatibleCurves);
        }
        BNField12 f = Fp12_1;
        P = P.normalize();
        Q = Q.normalize();
        if (!P.isZero() && !Q.isZero()) {
            final BNParams bn = E.bn;
            BNPoint2 T = Q;
            final BigInteger ord = bn.optOrd; // 6u+2
            for (int i = ord.bitLength() - 2; i >= 0; i--) {
                final BNPoint2 A = T.twice(1);
                f = f.square().multiply(gl(T, T, A, P));
                T = A;
                if (ord.testBit(i)) {
                    f = f.multiply(gl(T, Q, null, P));
                    T = T.add(Q);
                }
            }
            final BNPoint2 Q1 = Q.frobex(1);
            final BNPoint2 Q2 = Q.frobex(2).negate();
            final BNPoint2 Q3 = Q.frobex(3);
            final BNPoint2 Q4 = Q2.add(Q3);
            final BNPoint2 Q5 = Q4.add(Q1);
            f = f.multiply(gl(Q2, Q3, null, P)).multiply(gl(Q4, Q1, null, P)).multiply(gl(Q5, T, null, P));
            f = f.finExp();
        }
        return f;
    }

    /**
     * Added by Cristina Onete: Tate pairing method by modifying the eta pairing to full order.
     */

    public BNField12 tate(BNPoint P, BNPoint2 Q) {
        if (!E.contains(P) || !E2.contains(Q)) {
            throw new IllegalArgumentException(incompatibleCurves);
        }
        BNField12 f = Fp12_1;
        P = P.normalize();
        Q = Q.normalize();
        if (!P.isZero() && !Q.isZero()) {
            final BNParams bn = E.bn;
            BNPoint V = P;
            final BigInteger ord = bn.n; // the Tate pairing would have order bn.n instead of bn.rho
            for (int i = ord.bitLength() - 2; i >= 0; i--) {
                final BNPoint A = V.twice(1);
                f = f.square().multiply(gl(V, V, A, Q));
                V = A;
                if (ord.testBit(i)) {
                    f = f.multiply(gl(V, P, null, Q));
                    V = V.add(P);
                }
            }
            f = f.finExp();
        }
        return f;
    }

    @Override
    public String toString() {
        return Messages.BNPairing_1 + E + Messages.BNPairing_2 + E2;
    }
}
