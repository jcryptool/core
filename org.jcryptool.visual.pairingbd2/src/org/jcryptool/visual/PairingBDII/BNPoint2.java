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
 * BNPoint2.java
 *
 * Arithmetic in the group of points on the sextic twist a BN elliptic curve over GF(p^2).
 *
 * A point of an elliptic curve is only meaningful when suitably attached to some curve. Hence, there must be no public
 * means to create a point by itself (i.e. concrete subclasses of BNPoint2 shall have no public constructor); the proper
 * way to do this is to invoke the factory method pointFactory() of the desired BNCurve subclass.
 *
 * Copyright (C) Paulo S. L. M. Barreto.
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
import java.security.SecureRandom;

public class BNPoint2 {

    /**
     * Convenient BigInteger constants
     */
    private static final BigInteger _3 = BigInteger.valueOf(3L);

    public static final String differentCurves = Messages.BNPoint2_0;
    public static final String invalidCPSyntax = Messages.BNPoint2_1;
    public static final String pointNotOnCurve = Messages.BNPoint2_2;

    /**
     * The underlying elliptic curve, given by its parameters
     */
    BNCurve2 E;

    /**
     * The projective x-coordinate
     */
    BNField2 x;

    /**
     * The projective y-coordinate
     */
    BNField2 y;

    /**
     * The projective z-coordinate
     */
    BNField2 z;

    /**
     * Numerator of the line slope if this point was the result of a group operation
     */
    BNField2 m;

    /**
     * Create an instance of the BNCurve point at infinity on curve E.
     *
     * @param E the elliptic curve where the created point is located.
     */
    BNPoint2(BNCurve2 E) {
        this.E = E;
        /*
         * the point at infinity is represented as (1, 1, 0) after IEEE Std 1363:2000 (notice that this triple satisfies
         * the projective curve equation y^2 = x^3 + b.z^6)
         */
        x = E.Fp2_1;
        y = E.Fp2_1;
        z = E.Fp2_0;
        m = E.Fp2_0;
    }

    /**
     * Create a normalized twist point from given affine coordinates and a curve
     *
     * @param E the underlying elliptic curve.
     * @param x the affine x-coordinate.
     * @param y the affine y-coordinate.
     */
    BNPoint2(BNCurve2 E, BNField2 x, BNField2 y) {
        this.E = E;
        this.x = x;
        this.y = y;
        z = E.Fp2_1; // normalized
        m = E.Fp2_0;
        if (!E.contains(this)) {
            throw new IllegalArgumentException(pointNotOnCurve);
        }
    }

    /**
     * Create an BNCurve point from given projective coordinates and a curve.
     *
     * @param E the underlying elliptic curve.
     * @param x the affine x-coordinate.
     * @param y the affine y-coordinate.
     * @param z the affine z-coordinate.
     */
    private BNPoint2(BNCurve2 E, BNField2 x, BNField2 y, BNField2 z) {
        this.E = E;
        this.x = x;
        this.y = y;
        this.z = z;
        m = E.Fp2_0;
    }

    private BNPoint2(BNCurve2 E, BNField2 x, BNField2 y, BNField2 z, BNField2 m) {
        this.E = E;
        this.x = x;
        this.y = y;
        this.z = z;
        this.m = m;
    }

    /**
     * Create an BNCurve point from a given affine x-coordinate, a y-bit and a curve
     *
     * @param E the underlying elliptic curve.
     * @param x the affine x-coordinate.
     * @param yBit the least significant bit of the y-coordinate.
     */
    BNPoint2(BNCurve2 E, BNField2 x, int yBit) {
        this.E = E;
        this.x = x;
        if (x.isZero()) {
            throw new IllegalArgumentException(pointNotOnCurve);
        } else {
            y = x.cube().add(E.bt).sqrt();
            if (y == null) {
                throw new IllegalArgumentException(pointNotOnCurve);
            }
            if (y.re.testBit(0) != ((yBit & 1) == 1)) {
                y = y.negate();
            }
            // throw new RuntimeException("TODO: implement y-bit handling");
        }
        z = E.Fp2_1; // normalized
        m = E.Fp2_0;
        assert (!E.contains(this));
    }

    /**
     * Create an BNCurve point from a given x-trit, an affine y-coordinate, and a curve
     *
     * @param E the underlying elliptic curve.
     * @param xTrit the least significant trit of the x-coordinate.
     * @param y the affine y-coordinate.
     */
    BNPoint2(BNCurve2 E, int xTrit, BNField2 y) {
        this.E = E;
        this.y = y;
        if (y.isZero()) {
            throw new IllegalArgumentException(pointNotOnCurve); // otherwise the curve order would not be prime
        } else {
            x = y.square().subtract(E.bt).cbrt();
            if (x == null) {
                throw new IllegalArgumentException(pointNotOnCurve);
            }
            // either x, zeta*x, or zeta^2*x is the desired x-coordinate:
            if (x.re.mod(_3).intValue() != xTrit) {
                final BigInteger zeta = E.E.bn.zeta; // shorthand
                x = x.multiply(zeta);
                if (x.re.mod(_3).intValue() != xTrit) {
                    x = x.multiply(zeta);
                    if (x.re.mod(_3).intValue() != xTrit) {
                        throw new IllegalArgumentException(pointNotOnCurve);
                    }
                }
            }
            // throw new RuntimeException("TODO: implement x-trit handling");
        }
        z = E.Fp2_1; // normalized
        m = E.Fp2_0;
        assert (!E.contains(this));
    }

    /*
     * performing arithmetic operations on elliptic curve points generally implies knowing the nature of these points
     * (more precisely, the nature of the finite field to which their coordinates belong), hence they are done by the
     * underlying elliptic curve.
     */

    /**
     * Compute this + Q.
     *
     * @return this + Q.
     *
     * @param Q an elliptic curve point.
     */
    public BNPoint2 add(BNPoint2 Q) {
        /*
         * if (!this.isOnSameCurve(Q)) { throw new IllegalArgumentException(differentCurves); }
         */
        if (isZero()) {
            return Q;
        }
        if (Q.isZero()) {
            return this;
        }
        // P1363 section A.10.5
        BNField2 t1, t2, t3, t4, t5, t6, t7, t8;
        t1 = x;
        t2 = y;
        t3 = z;
        t4 = Q.x;
        t5 = Q.y;
        t6 = Q.z;
        if (!t6.isOne()) {
            t7 = t6.square(); // t7 = z1^2
            // u0 = x0.z1^2
            t1 = t1.multiply(t7);
            // s0 = y0.z1^3 = y0.z1^2.z1
            t2 = t2.multiply(t7).multiply(t6);
        }
        if (!t3.isOne()) {
            t7 = t3.square(); // t7 = z0^2
            // u1 = x1.z0^2
            t4 = t4.multiply(t7);
            // s1 = y1.z0^3 = y1.z0^2.z0
            t5 = t5.multiply(t7).multiply(t3);
        }
        // W = u0 - u1
        t7 = t1.subtract(t4);
        // R = s0 - s1
        t8 = t2.subtract(t5);
        if (t7.isZero()) {
            return t8.isZero() ? Q.twice(1) : E.infinity;
        }
        // T = u0 + u1
        t1 = t1.add(t4);
        // M = s0 + s1
        t2 = t2.add(t5);
        // z2 = z0.z1.W
        if (!t6.isOne()) {
            t3 = t3.multiply(t6);
        }
        t3 = t3.multiply(t7);
        // x2 = R^2 - T.W^2
        t5 = t7.square(); // t5 = W^2
        t6 = t1.multiply(t5); // t6 = T.W^2
        t1 = t8.square().subtract(t6);
        // 2.y2 = (T.W^2 - 2.x2).R - M.W^2.W
        t2 = t6.subtract(t1.twice(1)).multiply(t8).subtract(t2.multiply(t5).multiply(t7)).halve();
        return new BNPoint2(E, t1, t2, t3);
    }

    /**
     * Compare this point to a given object.
     *
     * @param Q the elliptic curve point to be compared to this.
     *
     * @return true if this point and Q are equal, otherwise false.
     */
    @Override
    public boolean equals(Object Q) {
        if (Q instanceof BNPoint2 && isOnSameCurve((BNPoint2) Q)) {
            final BNPoint2 P = (BNPoint2) Q;
            if (z.isZero() || P.isZero()) {
                return z.equals(P.z);
            } else {
                // x/z^2 = x'/z'^2 <=> x*z'^2 = x'*z^2.
                // y/z^3 = y'/z'^3 <=> y*z'^3 = y'*z^3,
                final BNField2 z2 = z.square(), z3 = z.multiply(z2), pz2 = P.z.square(), pz3 = P.z.multiply(pz2);
                return x.multiply(pz2).subtract(P.x.multiply(z2)).isZero()
                        && y.multiply(pz3).subtract(P.y.multiply(z3)).isZero();
            }
        } else {
            return false;
        }
    }

    public BNPoint2 frobex(int k) {
        if (!z.isOne()) {
            throw new RuntimeException(Messages.BNPoint2_3);
        }
        final BNParams bn = E.E.bn;
        switch (k) {
            case 1:
                return new BNPoint2(E, x.conjugate().multiplyI().multiply(bn.sigma[1]), y.multiplyV().conjugate()
                        .multiply(bn.sigma[2]));
            case 2:
                return new BNPoint2(E, x.multiply(bn.p.subtract(bn.sigma[3])), y.multiply(bn.sigma[5]));
            case 3:
                return new BNPoint2(E, x.conjugate().multiplyI().multiply(bn.sigma[5]), y.multiplyV().conjugate()
                        .multiply(bn.sigma[6]));
            default:
                return null;
        }
    }

    public BNField2 getX() {
        return x;
    }

    public BNField2 getY() {
        return y;
    }

    public BNField2 getZ() {
        return z;
    }

    /**
     * Check whether Q lays on the same curve as this point.
     *
     * @param Q an elliptic curve point.
     *
     * @return true if Q lays on the same curve as this point, otherwise false.
     */
    public boolean isOnSameCurve(BNPoint2 Q) {
        return E.E.bn.equals(Q.E.E.bn) && E.bt.equals(Q.E.bt)
        // && E.Gt.equals(Q.E.Gt) // caveat: resist the temptation to uncomment this line! :-)
        ;
    }

    /**
     * Check whether this is the point at infinity (i.e. the BNCurve group zero element).
     *
     * @return true if this is the point at infinity, otherwise false.
     */
    public boolean isZero() {
        return z.isZero();
    }

    /**
     * Compute k*this
     *
     * @param k scalar by which the base point G is to be multiplied
     *
     * @return k*this
     */
    public BNPoint2 multiply(BigInteger k) {
        /*
         * This method implements the the quaternary window multiplication algorithm. Reference: Alfred J. Menezes, Paul
         * C. van Oorschot, Scott A. Vanstone, "Handbook of Applied Cryptography", CRC Press (1997), section 14.6
         * (Exponentiation), algorithm 14.82
         */
        BNPoint2 P = normalize();
        if (k.signum() < 0) {
            k = k.negate();
            P = P.negate();
        }
        final byte[] e = k.toByteArray();
        final BNPoint2[] mP = new BNPoint2[16];
        mP[0] = E.infinity;
        mP[1] = P;
        for (int i = 1; i <= 7; i++) {
            mP[2 * i] = mP[i].twice(1);
            mP[2 * i + 1] = mP[2 * i].add(P);
        }
        BNPoint2 A = E.infinity;
        for (final byte element : e) {
            final int u = element & 0xff;
            A = A.twice(4).add(mP[u >>> 4]).twice(4).add(mP[u & 0xf]);
        }
        return A;
    }

    /**
     * Compute -this.
     *
     * @return -this.
     */
    public BNPoint2 negate() {
        return new BNPoint2(E, x, y.negate(), z);
    }

    /**
     * Normalize this point.
     *
     * @return a normalized point equivalent to this.
     */
    public BNPoint2 normalize() {
        if (z.isZero() || z.isOne()) {
            return this; // already normalized
        }
        BNField2 zinv = null;
        try {
            zinv = z.inverse();
        } catch (final ArithmeticException a) {
        }
        final BNField2 zinv2 = zinv.square(), zinv3 = zinv.multiply(zinv2);
        return new BNPoint2(E, x.multiply(zinv2), y.multiply(zinv3), E.Fp2_1);
    }

    /**
     * Check if a point equals -this.
     */
    public boolean opposite(BNPoint2 P) {
        return equals(P.negate());
    }

    /**
     * Compute a random point on the same curve as this.
     *
     * @param rand a cryptographically strong pseudo-random number generator.
     *
     * @return a random point on the same curve as this.
     */
    public BNPoint2 randomize(SecureRandom rand) {
        return E.pointFactory(rand);
    }

    // added here by Cristina Onete

    /**
     * Compute ks*this + kr*Y. This is useful in the verification part of several signature algorithms, and (hopely)
     * faster than two scalar multiplications.
     *
     * @param ks scalar by which this point is to be multiplied.
     * @param kr scalar by which Y is to be multiplied.
     * @param Y a curve point.
     *
     * @return ks*this + kr*Y
     */
    public BNPoint2 simultaneous(BigInteger ks, BigInteger kr, BNPoint2 Y) {
        assert (isOnSameCurve(Y));
        // return this.multiply(ks).add(Y.multiply(kr)).normalize();
        // *
        final BNPoint2[] hV = new BNPoint2[16];
        hV[0] = E.infinity;
        hV[1] = this;
        hV[2] = Y;
        hV[3] = add(Y);
        for (int i = 4; i < 16; i += 4) {
            hV[i] = hV[i >> 2].twice(1);
            hV[i + 1] = hV[i].add(hV[1]);
            hV[i + 2] = hV[i].add(hV[2]);
            hV[i + 3] = hV[i].add(hV[3]);
        }
        final int t = Math.max(kr.bitLength(), ks.bitLength());
        BNPoint2 R = E.infinity;
        for (int i = (((t + 1) >> 1) << 1) - 1; i >= 0; i -= 2) {
            final int j = (kr.testBit(i) ? 8 : 0) | (ks.testBit(i) ? 4 : 0) | (kr.testBit(i - 1) ? 2 : 0)
                    | (ks.testBit(i - 1) ? 1 : 0);
            R = R.twice(2).add(hV[j]);
        }
        return R.normalize();
        // */
    }

    @Override
    public String toString() {
        return "[" + x + " : " + y + " : " + z + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    /**
     * Left-shift this point by a given distance n, i.e. compute (2^^n)*this.
     *
     * @param n the shift amount.
     *
     * @return (2^^n)*this.
     */
    public BNPoint2 twice(int n) {
        // P1363 section A.10.4
        BNField2 t1, t2, t3, t4, t5, M = E.Fp2_0;
        t1 = x;
        t2 = y;
        t3 = z;
        while (n-- > 0) {
            if (t2.isZero() || t3.isZero()) {
                return E.infinity;
            }
            t4 = t3.square(); // t4 = z^2 (no need to reduce: z is often 1)
            // M = 3.x^2
            M = t4 = t1.square().multiply(_3);
            // z2 = 2.y.z
            t3 = t3.multiply(t2).twice(1);
            // S = 4.x.y^2
            t2 = t2.square(); // t2 = y^2
            t5 = t1.multiply(t2).twice(2);
            // x2 = M^2 - 2.S
            t1 = t4.square().subtract(t5.twice(1));
            // T = 8.(y^2)^2
            t2 = t2.square().twice(3);
            // y2 = M(S - x2) - T
            t2 = t4.multiply(t5.subtract(t1)).subtract(t2);
        }
        return new BNPoint2(E, t1, t2, t3, M);
    }

}
