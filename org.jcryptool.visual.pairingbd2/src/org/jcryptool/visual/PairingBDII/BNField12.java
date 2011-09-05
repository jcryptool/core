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
 * BNField12.java
 *
 * Arithmetic in the finite extension field GF(p^12) with p = 3 (mod 4) and p = 4 (mod 9).
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

public class BNField12 {

    /**
     * Convenient BigInteger constants
     */
    private static final BigInteger _1 = BigInteger.valueOf(1L), _5 = BigInteger.valueOf(5L), _6 = BigInteger
            .valueOf(6L);

    public static final String differentFields = Messages.BNField12_0;

    /**
     * BN parameters (singleton)
     */
    BNParams bn;

    /**
     * Components
     */
    BNField2[] v;

    // added by Cristina Onete: public

    public BNField12(BNParams bn, BigInteger k) {
        this.bn = bn;
        v = new BNField2[6];
        v[0] = new BNField2(bn, k);
        for (int i = 1; i < 6; i++) {
            v[i] = new BNField2(bn);
        }
    }

    BNField12(BNParams bn, BNField2[] v) {
        this.bn = bn;
        this.v = v;
    }

    public BNField12 add(BNField12 k) {
        if (!bn.equals(k.bn)) {
            throw new IllegalArgumentException(differentFields);
        }
        final BNField2[] w = new BNField2[6];
        for (int i = 0; i < 6; i++) {
            w[i] = v[i].add(k.v[i]);
        }
        return new BNField12(bn, w);
    }

    /**
     * Compute this^((p^2)^m), the m-th conjugate of this over GF(p^2).
     */
    public BNField12 conjugate(int m) {
        /*
         * z^(p^2) = -zeta*z z^(p^4) = -(zeta+1)*z = zeta^2*z z^(p^6) = -z z^(p^8) = zeta*z z^(p^10) = (zeta+1)*z =
         * -zeta^2*z v = v_0 + v_1 z + v_2 z^2 + v_3 z^3 + v_4 z^4 + v_5 z^5 => v^(p^2) = v_0 - v_1zeta z - v_2(zeta+1)
         * z^2 - v_3 z^3 + v_4zeta z^4 + v_5(zeta+1) z^5 v^(p^4) = v_0 - v_1(zeta+1) z + v_2zeta z^2 + v_3 z^3 - v_4
         * z^4(zeta+1) + v_5zeta z^5 v^(p^6) = v_0 - v_1 z + v_2 z^2 - v_3 z^3 + v_4 z^4 - v_5 z^5 v^(p^8) = v_0 +
         * v_1zeta z - v_2(zeta+1) z^2 + v_3 z^3 + v_4zeta z^4 - v_5(zeta+1) z^5 v^(p^10) = v_0 + v_1(zeta+1) z +
         * v_2zeta z^2 - v_3 z^3 - v_4 z^4(zeta+1) - v_5zeta z^5
         */
        final BigInteger zeta0 = bn.zeta;
        final BigInteger zeta1 = bn.zeta.add(_1);
        BNField2[] w;
        switch (m % 6) {
            default: // only to make the compiler happy
            case 0:
                return this;
            case 1:
                w = new BNField2[6];
                w[0] = v[0];
                w[1] = v[1].multiply(zeta0.negate());
                w[2] = v[2].multiply(zeta1.negate());
                w[3] = v[3].negate();
                w[4] = v[4].multiply(zeta0);
                w[5] = v[5].multiply(zeta1);
                return new BNField12(bn, w);
            case 2:
                w = new BNField2[6];
                w[0] = v[0];
                w[1] = v[1].multiply(zeta1.negate());
                w[2] = v[2].multiply(zeta0);
                w[3] = v[3];
                w[4] = v[4].multiply(zeta1.negate());
                w[5] = v[5].multiply(zeta0);
                return new BNField12(bn, w);
            case 3:
                w = new BNField2[6];
                w[0] = v[0];
                w[1] = v[1].negate();
                w[2] = v[2];
                w[3] = v[3].negate();
                w[4] = v[4];
                w[5] = v[5].negate();
                return new BNField12(bn, w);
            case 4:
                w = new BNField2[6];
                w[0] = v[0];
                w[1] = v[1].multiply(zeta0);
                w[2] = v[2].multiply(zeta1.negate());
                w[3] = v[3];
                w[4] = v[4].multiply(zeta0);
                w[5] = v[5].multiply(zeta1.negate());
                return new BNField12(bn, w);
            case 5:
                w = new BNField2[6];
                w[0] = v[0];
                w[1] = v[1].multiply(zeta1);
                w[2] = v[2].multiply(zeta0);
                w[3] = v[3].negate();
                w[4] = v[4].multiply(zeta1.negate());
                w[5] = v[5].multiply(zeta0.negate());
                return new BNField12(bn, w);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BNField12)) {
            return false;
        }
        final BNField12 w = (BNField12) o;
        if (!bn.equals(w.bn)) {
            return false;
        }
        for (int i = 0; i < 6; i++) {
            if (!v[i].equals(w.v[i])) {
                return false;
            }
        }
        return true;
    }

    public BNField12 exp(BigInteger k) {
        BNField12 w = this;
        for (int i = k.bitLength() - 2; i >= 0; i--) {
            w = w.square();
            if (k.testBit(i)) {
                w = w.multiply(this);
            }
        }
        return w;
    }

    public BNField12 finExp() {
        BNField12 f = this;
        try {
            // p^12 - 1 = (p^4 - 1)*(p^4 + p^2 + 1)*(p^4 - p^2 + 1)
            f = f.conjugate(2).multiply(f.inverse()); // f = f^(p^4 - 1)
        } catch (final ArithmeticException x) {
            f = this; // this can only happen when this instance is not invertible, i.e. zero
        }
        f = f.conjugate(2).multiply(f.conjugate(1)).multiply(f); // f = f^(p^4 + p^2 + 1)
        assert (f.inverse().equals(f.conjugate(3)));
        BNField12 a;
        if (bn.u.signum() >= 0) {
            a = f.exp(_6.multiply(bn.u).add(_5)).conjugate(3);
        } else {
            a = f.exp(_6.multiply(bn.u).add(_5).negate());
        }
        final BNField12 b = a.frobenius().multiply(a);
        final BNField12 c = f.frobenius();
        final BNField12 d = f.conjugate(1);
        final BNField12 e = c.multiply(f);
        f = c.conjugate(1).multiply(b.multiply(c.square()).multiply(d).exp(bn.t)).multiply(b).multiply(a).multiply(
                f.multiply(e.square()).square().square()).multiply(e);
        return f;
    }

    public BNField12 frobenius() {
        /*
         * z^p = sigma*(1+i)*z (z^2)^p = 2*sigma^2*i*z^2 (z^3)^p = -2*sigma^3*(i-1)*z^3 (z^4)^p = -4*sigma^4*z^4 (z^5)^p
         * = -4*sigma^5*(1+i)*z^5
         */
        final BNField2[] w = new BNField2[6];
        w[0] = v[0].conjugate();
        w[1] = v[1].conjugate().multiply(bn.sigma[0]).multiplyV();
        w[2] = v[2].conjugate().multiply(bn.sigma[1]).multiplyI();
        w[3] = v[3].multiplyV().conjugate().multiply(bn.sigma[2]);
        w[4] = v[4].conjugate().multiply(bn.sigma[3]);
        w[5] = v[5].conjugate().multiply(bn.sigma[4]).multiplyV();
        return new BNField12(bn, w);
    }

    public BNField2 getComponent(int index) {
        return v[index];
    }

    public BNField12 inverse() throws ArithmeticException {
        BNField12 c = conjugate(1);
        for (int i = 2; i < 6; i++) {
            c = c.multiply(conjugate(i));
        }
        final BNField12 n = c.multiply(this);
        assert (n.v[1].isZero() && n.v[2].isZero() && n.v[3].isZero() && n.v[4].isZero() && n.v[5].isZero());
        c = c.multiply(n.v[0].inverse());
        return c;
    }

    public boolean isOne() {
        if (!v[0].isOne()) {
            return false;
        }
        for (int i = 1; i < 6; i++) {
            if (!v[i].isZero()) {
                return false;
            }
        }
        return true;
    }

    public boolean isZero() {
        for (int i = 0; i < 6; i++) {
            if (!v[i].isZero()) {
                return false;
            }
        }
        return true;
    }

    public BNField12 multiply(BigInteger k) {
        final BNField2[] w = new BNField2[6];
        for (int i = 0; i < 6; i++) {
            w[i] = v[i].multiply(k);
        }
        return new BNField12(bn, w);
    }

    public BNField12 multiply(BNField12 k) {
        if (!bn.equals(k.bn)) {
            throw new IllegalArgumentException(differentFields);
        }
        if (k == this) {
            return square();
        }
        final BNField2[] w = new BNField2[6];
        if (k.v[1].isZero() && k.v[4].isZero() && k.v[5].isZero()) {
            final BNField2 d00 = v[0].multiply(k.v[0]), d22 = v[2].multiply(k.v[2]), d33 = v[3].multiply(k.v[3]), d01 = v[0]
                    .add(v[1]).multiply(k.v[0]).subtract(d00), d02 = v[0].add(v[2]).multiply(k.v[0].add(k.v[2]))
                    .subtract(d00.add(d22)), d04 = v[0].add(v[4]).multiply(k.v[0]).subtract(d00), d13 = v[1].add(v[3])
                    .multiply(k.v[3]).subtract(d33), d23 = v[2].add(v[3]).multiply(k.v[2].add(k.v[3])).subtract(
                    d22.add(d33)), d24 = v[2].add(v[4]).multiply(k.v[2]).subtract(d22), d35 = v[3].add(v[5]).multiply(
                    k.v[3]).subtract(d33), d03 = v[0].add(v[1]).add(v[2]).add(v[3]).multiply(
                    k.v[0].add(k.v[2]).add(k.v[3])).subtract(d00.add(d22).add(d33).add(d01).add(d02).add(d13).add(d23)), d05 = v[0]
                    .add(v[1]).add(v[4]).add(v[5]).multiply(k.v[0]).subtract(d00.add(d01).add(d04)), d25 = v[2].add(
                    v[3]).add(v[4]).add(v[5]).multiply(k.v[2].add(k.v[3])).subtract(
                    d22.add(d33).add(d23).add(d24).add(d35));
            w[0] = d24.add(d33).divideV().add(d00);
            w[1] = d25.divideV().add(d01);
            w[2] = d35.divideV().add(d02);
            w[3] = d03;
            w[4] = d04.add(d13).add(d22);
            w[5] = d05.add(d23);
        } else if (k.v[2].isZero() && k.v[4].isZero() && k.v[5].isZero()) {
            final BNField2 d00 = v[0].multiply(k.v[0]), d11 = v[1].multiply(k.v[1]), d33 = v[3].multiply(k.v[3]), d01 = v[0]
                    .add(v[1]).multiply(k.v[0].add(k.v[1])).subtract(d00.add(d11)), d02 = v[0].add(v[2]).multiply(
                    k.v[0]).subtract(d00), d04 = v[0].add(v[4]).multiply(k.v[0]).subtract(d00), d13 = v[1].add(v[3])
                    .multiply(k.v[1].add(k.v[3])).subtract(d11.add(d33)), d15 = v[1].add(v[5]).multiply(k.v[1])
                    .subtract(d11), d23 = v[2].add(v[3]).multiply(k.v[3]).subtract(d33), d35 = v[3].add(v[5]).multiply(
                    k.v[3]).subtract(d33), d03 = v[0].add(v[1]).add(v[2]).add(v[3]).multiply(
                    k.v[0].add(k.v[1]).add(k.v[3])).subtract(d00.add(d11).add(d33).add(d01).add(d02).add(d13).add(d23)), d05 = v[0]
                    .add(v[1]).add(v[4]).add(v[5]).multiply(k.v[0].add(k.v[1])).subtract(
                            d00.add(d11).add(d01).add(d04).add(d15)), d25 = v[2].add(v[3]).add(v[4]).add(v[5])
                    .multiply(k.v[3]).subtract(d33.add(d23).add(d35));
            w[0] = d15.add(d33).divideV().add(d00);
            w[1] = d25.divideV().add(d01);
            w[2] = d35.divideV().add(d02).add(d11);
            w[3] = d03;
            w[4] = d04.add(d13);
            w[5] = d05.add(d23);
        } else {
            final BNField2 d00 = v[0].multiply(k.v[0]), d11 = v[1].multiply(k.v[1]), d22 = v[2].multiply(k.v[2]), d33 = v[3]
                    .multiply(k.v[3]), d44 = v[4].multiply(k.v[4]), d55 = v[5].multiply(k.v[5]), d01 = v[0].add(v[1])
                    .multiply(k.v[0].add(k.v[1])).subtract(d00.add(d11)), d02 = v[0].add(v[2]).multiply(
                    k.v[0].add(k.v[2])).subtract(d00.add(d22)), d04 = v[0].add(v[4]).multiply(k.v[0].add(k.v[4]))
                    .subtract(d00.add(d44)), d13 = v[1].add(v[3]).multiply(k.v[1].add(k.v[3])).subtract(d11.add(d33)), d15 = v[1]
                    .add(v[5]).multiply(k.v[1].add(k.v[5])).subtract(d11.add(d55)), d23 = v[2].add(v[3]).multiply(
                    k.v[2].add(k.v[3])).subtract(d22.add(d33)), d24 = v[2].add(v[4]).multiply(k.v[2].add(k.v[4]))
                    .subtract(d22.add(d44)), d35 = v[3].add(v[5]).multiply(k.v[3].add(k.v[5])).subtract(d33.add(d55)), d45 = v[4]
                    .add(v[5]).multiply(k.v[4].add(k.v[5])).subtract(d44.add(d55)), d03 = v[0].add(v[1]).add(v[2]).add(
                    v[3]).multiply(k.v[0].add(k.v[1]).add(k.v[2]).add(k.v[3])).subtract(
                    d00.add(d11).add(d22).add(d33).add(d01).add(d02).add(d13).add(d23)), d05 = v[0].add(v[1]).add(v[4])
                    .add(v[5]).multiply(k.v[0].add(k.v[1]).add(k.v[4]).add(k.v[5])).subtract(
                            d00.add(d11).add(d44).add(d55).add(d01).add(d04).add(d15).add(d45)), d25 = v[2].add(v[3])
                    .add(v[4]).add(v[5]).multiply(k.v[2].add(k.v[3]).add(k.v[4]).add(k.v[5])).subtract(
                            d22.add(d33).add(d44).add(d55).add(d23).add(d24).add(d35).add(d45));
            w[0] = d15.add(d24).add(d33).divideV().add(d00);
            w[1] = d25.divideV().add(d01);
            w[2] = d35.add(d44).divideV().add(d02).add(d11);
            w[3] = d45.divideV().add(d03);
            w[4] = d55.divideV().add(d04).add(d13).add(d22);
            w[5] = d05.add(d23);
        }
        return new BNField12(bn, w);
    }

    public BNField12 multiply(BNField2 k) {
        final BNField2[] w = new BNField2[6];
        for (int i = 0; i < 6; i++) {
            w[i] = v[i].multiply(k);
        }
        return new BNField12(bn, w);
    }

    public BNField12 negate() {
        final BNField2[] w = new BNField2[6];
        for (int i = 0; i < 6; i++) {
            w[i] = v[i].negate();
        }
        return new BNField12(bn, w);
    }

    public BigInteger norm() {
        return norm2().norm();
    }

    public BNField2 norm2() {
        BNField12 c = this;
        for (int i = 1; i < 6; i++) {
            c = c.multiply(conjugate(i));
        }
        if (!c.v[1].isZero() || !c.v[2].isZero() || !c.v[3].isZero() || !c.v[4].isZero() || !c.v[5].isZero()) {
            throw new RuntimeException(Messages.BNField12_1);
        }
        return c.v[0];
    }

    /*
     * public BNField12 exp(BigInteger k) { BNField12 P = this; if (k.signum() < 0) { k = k.negate(); P =
     * P.conjugate(3); } byte[] e = k.toByteArray(); BNField12[] mP = new BNField12[16]; mP[0] = new BNField12(bn, _1);
     * mP[1] = P; for (int m = 1; m <= 7; m++) { mP[2*m ] = mP[ m].square(); mP[2*m + 1] = mP[2*m].multiply(P); }
     * BNField12 A = mP[0]; for (int i = 0; i < e.length; i++) { int u = e[i] & 0xff; A =
     * A.square().square().square().square().multiply(mP[u >>> 4]).square().square().square().square().multiply(mP[u &
     * 0xf]); } return A; } //
     */

    public BNField12 square() {
        // return this.multiply(this);
        final BNField2 d00 = v[0].square(), d11 = v[1].square(), d22 = v[2].square(), d33 = v[3].square(), d44 = v[4]
                .square(), d55 = v[5].square(), d01 = v[0].add(v[1]).square().subtract(d00.add(d11)), d02 = v[0].add(
                v[2]).square().subtract(d00.add(d22)), d04 = v[0].add(v[4]).square().subtract(d00.add(d44)), d13 = v[1]
                .add(v[3]).square().subtract(d11.add(d33)), d15 = v[1].add(v[5]).square().subtract(d11.add(d55)), d23 = v[2]
                .add(v[3]).square().subtract(d22.add(d33)), d24 = v[2].add(v[4]).square().subtract(d22.add(d44)), d35 = v[3]
                .add(v[5]).square().subtract(d33.add(d55)), d45 = v[4].add(v[5]).square().subtract(d44.add(d55)), d03 = v[0]
                .add(v[1]).add(v[2]).add(v[3]).square().subtract(
                        d00.add(d11).add(d22).add(d33).add(d01).add(d02).add(d13).add(d23)), d05 = v[0].add(v[1]).add(
                v[4]).add(v[5]).square().subtract(d00.add(d11).add(d44).add(d55).add(d01).add(d04).add(d15).add(d45)), d25 = v[2]
                .add(v[3]).add(v[4]).add(v[5]).square().subtract(
                        d22.add(d33).add(d44).add(d55).add(d23).add(d24).add(d35).add(d45));
        final BNField2[] w = new BNField2[6];
        w[0] = d15.add(d24).add(d33).divideV().add(d00);
        w[1] = d25.divideV().add(d01);
        w[2] = d35.add(d44).divideV().add(d02).add(d11);
        w[3] = d45.divideV().add(d03);
        w[4] = d55.divideV().add(d04).add(d13).add(d22);
        w[5] = d05.add(d23);
        return new BNField12(bn, w);
    }

    public BNField12 subtract(BNField12 k) {
        if (!bn.equals(k.bn)) {
            throw new IllegalArgumentException(differentFields);
        }
        final BNField2[] w = new BNField2[6];
        for (int i = 0; i < 6; i++) {
            w[i] = v[i].subtract(k.v[i]);
        }
        return new BNField12(bn, w);
    }

    // added by Cristina Onete: returns each component of the field element.
    // index takes value 0 to 5;

    @Override
    public String toString() {
        return "(" + v[0] + ", " + v[1] + ", " + v[2] + ", " + v[3] + ", " + v[4] + ", " + v[5] + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
    }

}
