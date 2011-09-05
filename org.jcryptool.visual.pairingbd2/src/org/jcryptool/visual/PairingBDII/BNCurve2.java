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
 * BNCurve2.java
 *
 * Sextic twist of Barreto-Naehrig (BN) pairing-friendly elliptic curves.
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

public class BNCurve2 {

    /**
     * Convenient BigInteger constants
     */
    static final BigInteger _0 = BigInteger.valueOf(0L), _1 = BigInteger.valueOf(1L);

    BNField2 Fp2_0;
    BNField2 Fp2_1;

    /**
     * Underlying curve E/GF(p) of which this curve is a sextic twist
     */
    BNCurve E;

    /**
     * Coefficient of the twist equation
     */
    BNField2 bt;

    /**
     * The base point of the cryptographic subgroup
     */
    BNPoint2 Gt;

    /**
     * The point at infinity
     */
    BNPoint2 infinity;

    /**
     * Multiples of the base point Gt by simple multiples of powers of 16.
     */
    protected BNPoint2[][] pp16Gt;

    /**
     * Build the standard sextic twist E': y'^2 = x'^3 + 3*(1+i) of the standard BN curve E: y^2 = x^3 + 3.
     *
     * @param E given BN curve
     *
     * @return the desired curve, or null if E does not have a suitable twist of the above form.
     */
    public BNCurve2(BNCurve E) {
        this.E = E;
        Fp2_0 = new BNField2(E.bn, _0);
        Fp2_1 = new BNField2(E.bn, _1);
        bt = new BNField2(E.bn, E.b).multiplyV(); // v = 1 + i, standard non-square non-cube
        infinity = new BNPoint2(this);
        final BNField2 xt = Fp2_1; // standard x-coord
        final BNField2 yt = xt.multiply(xt).multiply(xt).add(bt).sqrt();
        assert (yt != null);
        Gt = new BNPoint2(this, xt, yt);
        assert (Gt != null);
        Gt = Gt.multiply(E.bn.ht).normalize();
        pp16Gt = new BNPoint2[(E.bn.n.bitLength() + 3) / 4][16];
        BNPoint2[] pp16Gi = pp16Gt[0];
        pp16Gi[0] = infinity;
        pp16Gi[1] = Gt;
        for (int i = 1, j = 2; i <= 7; i++, j += 2) {
            pp16Gi[j] = pp16Gi[i].twice(1);
            pp16Gi[j + 1] = pp16Gi[j].add(Gt);
        }
        for (int i = 1; i < pp16Gt.length; i++) {
            final BNPoint2[] pp16Gh = pp16Gi;
            pp16Gi = pp16Gt[i];
            pp16Gi[0] = pp16Gh[0];
            for (int j = 1; j < 16; j++) {
                pp16Gi[j] = pp16Gh[j].twice(4);
            }
        }
    }

    /**
     * Check whether this curve contains a given point (i.e. whether that point satisfies the curve equation)
     *
     * @param P the point whose pertinence or not to this curve is to be determined
     *
     * @return true if this curve contains P, otherwise false
     */
    public boolean contains(BNPoint2 P) {
        if (P.E != this) {
            return false;
        }
        // check the projective equation y^2 = x^3 + bt*z^6,
        // i.e. x^3 + bt*(z^2)^3 - y^2 = 0
        // (the computation below never uses intermediate values larger than 3p^2)
        final BNField2 x = P.x, y = P.y, z = P.z;
        return x.cube().add(bt.multiply(z.square().cube())).subtract(y.square()).isZero();
    }

    public BNCurve getE() {
        return E;
    }

    public BNPoint2 getGt() {
        return Gt;
    }

    public BNPoint2 getInfinity() {
        return infinity;
    }

    // Cristina's methods: get Gt and E

    /**
     * Compute k*G
     *
     * @param k scalar by which the base point G is to be multiplied
     *
     * @return k*G
     *
     *         References:
     *
     *         Alfred J. Menezes, Paul C. van Oorschot, Scott A. Vanstone, "Handbook of Applied Cryptography", CRC Press
     *         (1997), section 14.6 (Exponentiation)
     */
    public BNPoint2 kG(BigInteger k) {
        k = k.mod(E.bn.n); // reduce k mod n
        BNPoint2 A = infinity;
        for (int i = 0, w = 0; i < pp16Gt.length; i++, w >>>= 4) {
            if ((i & 7) == 0) {
                w = k.intValue();
                k = k.shiftRight(32);
            }
            A = A.add(pp16Gt[i][w & 0xf]);
        }
        return A;
    }

    /**
     * Get a random nonzero point on this curve, given a fixed base point.
     *
     * @param rand a cryptographically strong PRNG
     *
     * @return a random nonzero point on this curve
     */
    public BNPoint2 pointFactory(SecureRandom rand) {
        BigInteger k;
        do {
            k = new BigInteger(E.bn.n.bitLength(), rand).mod(E.bn.n);
        } while (k.signum() == 0);
        return Gt.multiply(k);
    }

    @Override
    public String toString() {
        return "BN'(" + E.bn.u + "): y'^2 = x'^3 + " + bt; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
