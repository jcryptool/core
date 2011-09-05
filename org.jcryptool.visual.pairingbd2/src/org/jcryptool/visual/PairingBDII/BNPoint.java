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
 * BNPoint.java
 *
 * Arithmetic in the group of points on a BN elliptic curve over GF(p).
 *
 * A point of an elliptic curve is only meaningful when suitably attached to some curve. Hence, there must be no public
 * means to create a point by itself (i.e. concrete subclasses of BNPoint shall have no public constructor); the proper
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

public class BNPoint {

    /**
     * Convenient BigInteger constants
     */
    private static final BigInteger _0 = BigInteger.valueOf(0L), _1 = BigInteger.valueOf(1L), _3 = BigInteger
            .valueOf(3L);

    public static final String differentCurves = Messages.BNPoint_0;
    public static final String invalidCPSyntax = Messages.BNPoint_1;
    public static final String pointNotOnCurve = Messages.BNPoint_2;

    /**
     * The underlying elliptic curve, given by its parameters
     */
    BNCurve E;

    /**
     * The projective x-coordinate
     */
    BigInteger x;

    /**
     * The projective y-coordinate
     */
    BigInteger y;

    /**
     * The projective z-coordinate
     */
    BigInteger z;

    /**
     * Numerator of the line slope if this point was the result of a group operation
     */
    BigInteger m;

    /**
     * Flag/mask for compressed, expanded, or hybrid point representation
     */
    public static final int COMPRESSED = 2, EXPANDED = 4, HYBRID = COMPRESSED | EXPANDED;

    /**
     * Create an instance of the BNCurve point at infinity on curve E.
     *
     * @param E the elliptic curve where the created point is located.
     */
    BNPoint(BNCurve E) {
        this.E = E;
        /*
         * the point at infinity is represented as (1, 1, 0) after IEEE Std 1363:2000 (notice that this triple satisfies
         * the projective curve equation y^2 = x^3 + b.z^6)
         */
        x = _1;
        y = _1;
        z = _0;
        m = _0;
    }

    /**
     * Create a normalized BNCurve point from given affine coordinates and a curve
     *
     * @param E the underlying elliptic curve.
     * @param x the affine x-coordinate (mod p).
     * @param y the affine y-coordinate (mod p).
     */
    BNPoint(BNCurve E, BigInteger x, BigInteger y) {
        this.E = E;
        final BigInteger p = E.bn.p; // shorthand
        this.x = x.mod(p);
        this.y = y.mod(p);
        z = _1; // normalized
        m = _0;
        if (!E.contains(this)) {
            throw new IllegalArgumentException(pointNotOnCurve);
        }
    }

    /**
     * Create an BNCurve point from given projective coordinates and a curve.
     *
     * @param E the underlying elliptic curve.
     * @param x the affine x-coordinate (mod p).
     * @param y the affine y-coordinate (mod p).
     * @param z the affine z-coordinate (mod p).
     */
    private BNPoint(BNCurve E, BigInteger x, BigInteger y, BigInteger z) {
        this.E = E;
        this.x = x;
        this.y = y;
        this.z = z;
        m = _0;
    }

    private BNPoint(BNCurve E, BigInteger x, BigInteger y, BigInteger z, BigInteger m) {
        this.E = E;
        this.x = x;
        this.y = y;
        this.z = z;
        this.m = m;
    }

    /**
     * Create an BNCurve point from a given affine x-coordinate, a y-bit, and a curve
     *
     * @param E the underlying elliptic curve.
     * @param x the affine x-coordinate (mod p).
     * @param yBit the least significant bit of the y-coordinate.
     */
    BNPoint(BNCurve E, BigInteger x, int yBit) {
        this.E = E;
        final BigInteger p = E.bn.p; // shorthand
        this.x = x.mod(p);
        if (x.signum() == 0) {
            throw new IllegalArgumentException(pointNotOnCurve); // otherwise the curve order would not be prime
            // this.y = E.sqrt(E.b);
        } else {
            y = E.bn.sqrt(x.multiply(x).multiply(x).add(E.b).mod(p));
            if (y == null) {
                throw new IllegalArgumentException(pointNotOnCurve);
            }
            if (y.testBit(0) != ((yBit & 1) == 1)) {
                y = p.subtract(y);
            }
        }
        z = _1; // normalized
        m = _0;
        assert (!E.contains(this));
    }

    /**
     * Create an BNCurve point from a given x-trit, an affine y-coordinate, and a curve
     *
     * @param E the underlying elliptic curve.
     * @param xTrit the least significant trit of the x-coordinate.
     * @param y the affine y-coordinate (mod p).
     */
    BNPoint(BNCurve E, int xTrit, BigInteger y) {
        this.E = E;
        final BigInteger p = E.bn.p; // shorthand
        this.y = y.mod(p);
        if (y.signum() == 0) {
            throw new IllegalArgumentException(pointNotOnCurve); // otherwise the curve order would not be prime
            // this.x = E.cbrt(p.subtract(E.b));
        } else {
            x = E.bn.cbrt(y.multiply(y).subtract(E.b).mod(p));
            if (x == null) {
                throw new IllegalArgumentException(pointNotOnCurve);
            }
            // either x, zeta*x, or zeta^2*x is the desired x-coordinate:
            if (x.mod(_3).intValue() != xTrit) {
                final BigInteger zeta = E.bn.zeta; // shorthand
                x = zeta.multiply(x).mod(p);
                if (x.mod(_3).intValue() != xTrit) {
                    x = zeta.multiply(x).mod(p);
                    if (x.mod(_3).intValue() != xTrit) {
                        throw new IllegalArgumentException(pointNotOnCurve);
                    }
                }
            }
        }
        z = _1; // normalized
        m = _0;
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
    public BNPoint add(BNPoint Q) {
        /*
         * if (!this.isOnSameCurve(Q)) { throw new IllegalArgumentException(differentCurves); }
         */
        if (isZero()) {
            return Q;
        }
        if (Q.isZero()) {
            return this;
        }
        final BigInteger p = E.bn.p; // shorthand
        BigInteger t1, t2, t3, t4, t5, t6, t7, t8;
        t1 = x;
        t2 = y;
        t3 = z;
        t4 = Q.x;
        t5 = Q.y;
        t6 = Q.z;
        if (t6.compareTo(_1) != 0) {
            t7 = t6.multiply(t6); // t7 = z1^2
            // u0 = x0.z1^2
            t1 = t1.multiply(t7).mod(p);
            // s0 = y0.z1^3 = y0.z1^2.z1
            t2 = t2.multiply(t7).multiply(t6).mod(p);
        }
        if (t3.compareTo(_1) != 0) {
            t7 = t3.multiply(t3); // t7 = z0^2
            // u1 = x1.z0^2
            t4 = t4.multiply(t7).mod(p);
            // s1 = y1.z0^3 = y1.z0^2.z0
            t5 = t5.multiply(t7).multiply(t3).mod(p);
        }
        // W = u0 - u1
        t7 = t1.subtract(t4).mod(p);
        // R = s0 - s1
        t8 = t2.subtract(t5).mod(p);
        if (t7.signum() == 0) {
            return (t8.signum() == 0) ? Q.twice(1) : E.infinity;
        }
        // T = u0 + u1
        t1 = t1.add(t4).mod(p);
        // M = s0 + s1
        t2 = t2.add(t5).mod(p);
        // z2 = z0.z1.W
        if (!t6.equals(_1)) {
            t3 = t3.multiply(t6); // no need to reduce here
        }
        t3 = t3.multiply(t7).mod(p);
        // x2 = R^2 - T.W^2
        t5 = t7.multiply(t7).mod(p); // t5 = W^2
        t6 = t1.multiply(t5).mod(p); // t6 = T.W^2
        t1 = t8.multiply(t8).subtract(t6).mod(p);
        // 2.y2 = (T.W^2 - 2.x2).R - M.W^2.W
        t2 = t6.subtract(t1.shiftLeft(1)).multiply(t8).subtract(t2.multiply(t5).multiply(t7)).mod(p);
        t2 = (t2.testBit(0) ? t2.add(p) : t2).shiftRight(1).mod(p);
        return new BNPoint(E, t1, t2, t3);
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
        if (!(Q instanceof BNPoint && isOnSameCurve((BNPoint) Q))) {
            return false;
        }
        final BNPoint P = (BNPoint) Q;
        if (z.signum() == 0 || P.z.signum() == 0) {
            return z.equals(P.z);
        }
        final BigInteger p = E.bn.p; // shorthand
        final BigInteger z2 = z.multiply(z).mod(p), z3 = z.multiply(z2).mod(p), pz2 = P.z.multiply(P.z).mod(p), pz3 = P.z
                .multiply(pz2).mod(p);
        return x.multiply(pz2).subtract(P.x.multiply(z2)).mod(p).signum() == 0
                && y.multiply(pz3).subtract(P.y.multiply(z3)).mod(p).signum() == 0;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public BigInteger getZ() {
        return z;
    }

    public boolean isNormal() {
        return (z.signum() == 0 || z.compareTo(_1) == 0);
    }

    /**
     * Check whether Q lays on the same curve as this point.
     *
     * @param Q an elliptic curve point.
     *
     * @return true if Q lays on the same curve as this point, otherwise false.
     */
    public boolean isOnSameCurve(BNPoint Q) {
        return E.bn.equals(Q.E.bn) && E.b.equals(Q.E.b)
        // && E.G.equals(Q.E.G) // caveat: resist the temptation to uncomment this line! :-)
        ;
    }

    /**
     * Check whether this is the point at infinity (i.e. the BNCurve group zero element).
     *
     * @return true if this is the point at infinity, otherwise false.
     */
    public boolean isZero() {
        return z.signum() == 0;
    }

    /**
     * Compute k*this
     *
     * @param k scalar by which the base point G is to be multiplied
     *
     * @return k*this
     */
    public BNPoint multiply(BigInteger k) {
        /*
         * This method implements the the quaternary window multiplication algorithm. Reference: Alfred J. Menezes, Paul
         * C. van Oorschot, Scott A. Vanstone, "Handbook of Applied Cryptography", CRC Press (1997), section 14.6
         * (Exponentiation), algorithm 14.82
         */
        BNPoint P = normalize();
        if (k.signum() < 0) {
            k = k.negate();
            P = P.negate();
        }
        final byte[] e = k.toByteArray();
        final BNPoint[] mP = new BNPoint[16];
        mP[0] = E.infinity;
        mP[1] = P;
        for (int i = 1; i <= 7; i++) {
            mP[2 * i] = mP[i].twice(1);
            mP[2 * i + 1] = mP[2 * i].add(P);
        }
        BNPoint A = E.infinity;
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
    public BNPoint negate() {
        return new BNPoint(E, x, E.bn.p.subtract(y), z);
    }

    /**
     * Normalize this point.
     *
     * @return a normalized point equivalent to this.
     */
    public BNPoint normalize() {
        if (z.signum() == 0 || z.compareTo(_1) == 0) {
            return this; // already normalized
        }
        final BigInteger p = E.bn.p; // shorthand
        BigInteger zinv = null;
        try {
            zinv = z.modInverse(p);
        } catch (final ArithmeticException a) {
        }
        final BigInteger zinv2 = zinv.multiply(zinv); // mod p
        return new BNPoint(E, x.multiply(zinv2).mod(p), y.multiply(zinv).multiply(zinv2).mod(p), _1);
    }

    /**
     * Check if a point equals -this.
     */
    public boolean opposite(BNPoint P) {
        if (!isOnSameCurve(P)) {
            return false;
        }
        if (z.signum() == 0 || P.isZero()) {
            return z.compareTo(P.z) == 0;
        }
        final BigInteger p = E.bn.p; // shorthand
        final BigInteger z2 = z.multiply(z).mod(p), z3 = z.multiply(z2).mod(p), pz2 = P.z.multiply(P.z).mod(p), pz3 = P.z
                .multiply(pz2).mod(p);
        return x.multiply(pz2).subtract(P.x.multiply(z2)).mod(p).signum() == 0
                && y.multiply(pz3).add(P.y.multiply(z3)).mod(p).signum() == 0;
    }

    /**
     * Compute a random point on the same curve as this.
     *
     * @param rand a cryptographically strong pseudo-random number generator.
     *
     * @return a random point on the same curve as this.
     */
    public BNPoint randomize(SecureRandom rand) {
        return E.pointFactory(rand);
    }

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
    public BNPoint simultaneous(BigInteger ks, BigInteger kr, BNPoint Y) {
        assert (isOnSameCurve(Y));
        // return this.multiply(ks).add(Y.multiply(kr)).normalize();
        // *
        final BNPoint[] hV = new BNPoint[16];
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
        BNPoint R = E.infinity;
        for (int i = (((t + 1) >> 1) << 1) - 1; i >= 0; i -= 2) {
            final int j = (kr.testBit(i) ? 8 : 0) | (ks.testBit(i) ? 4 : 0) | (kr.testBit(i - 1) ? 2 : 0)
                    | (ks.testBit(i - 1) ? 1 : 0);
            R = R.twice(2).add(hV[j]);
        }
        return R.normalize();
        // */
    }

    // added here by Cristina Onete:

    /**
     * Convert this curve point to a byte array. This is the ANSI X9.62 Point-to-Octet-String Conversion primitive
     *
     * @param formFlags the desired form of the octet string representation (BNPoint.COMPRESSED, BNPoint.EXPANDED,
     *        BNPoint.HYBRID)
     *
     * @return this point converted to a byte array using the algorithm defined in section 4.3.6 of ANSI X9.62
     */
    public byte[] toByteArray(int formFlags) {
        byte[] result;
        if (isZero()) {
            result = new byte[1];
            result[0] = (byte) 0;
            return result;
        }
        final BNPoint P = normalize();
        byte[] osX = null, osY = null;
        osX = P.x.toByteArray();
        int pc = 0, resLen = 1 + osX.length;
        if ((formFlags & COMPRESSED) != 0) {
            pc |= COMPRESSED | (P.y.testBit(0) ? 1 : 0);
        }
        if ((formFlags & EXPANDED) != 0) {
            pc |= EXPANDED;
            osY = P.y.toByteArray();
            resLen += osY.length;
        }
        result = new byte[resLen];
        result[0] = (byte) pc;
        System.arraycopy(osX, 0, result, 1, osX.length);
        if (osY != null) {
            System.arraycopy(osY, 0, result, 1 + osX.length, osY.length);
        }
        return result;
    }

    @Override
    public String toString() {
        return isZero() ? "O" : "[" + x + " : " + y + " : " + z + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    }

    /**
     * Left-shift this point by a given distance n, i.e. compute (2^^n)*this.
     *
     * @param n the shift amount.
     *
     * @return (2^^n)*this.
     */
    public BNPoint twice(int n) {
        // P1363 section A.10.4
        BigInteger t1, t2, t3, t4, t5, M = _0;
        t1 = x;
        t2 = y;
        t3 = z;
        final BigInteger p = E.bn.p; // shorthand
        while (n-- > 0) {
            if (t2.signum() == 0 || t3.signum() == 0) {
                return E.infinity;
            }
            t4 = t3.multiply(t3); // t4 = z^2 (no need to reduce: z is often 1)
            // M = 3.x^2
            M = t4 = _3.multiply(t1.multiply(t1)).mod(p);
            // z2 = 2.y.z
            t3 = t3.multiply(t2).shiftLeft(1).mod(p);
            // S = 4.x.y^2
            t2 = t2.multiply(t2).mod(p); // t2 = y^2
            t5 = t1.multiply(t2).shiftLeft(2).mod(p);
            // x2 = M^2 - 2.S
            t1 = t4.multiply(t4).subtract(t5.shiftLeft(1)).mod(p);
            // T = 8.(y^2)^2
            t2 = t2.multiply(t2).shiftLeft(3).mod(p);
            // y2 = M(S - x2) - T
            t2 = t4.multiply(t5.subtract(t1)).subtract(t2).mod(p);
        }
        return new BNPoint(E, t1, t2, t3, M);
    }

}
