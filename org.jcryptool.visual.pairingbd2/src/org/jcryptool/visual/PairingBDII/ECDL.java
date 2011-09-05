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

import java.util.Vector;

import org.jcryptool.visual.PairingBDII.basics.DHECKeyPair;
import org.jcryptool.visual.PairingBDII.basics.GFPtoGFP2Pairing;
import org.jcryptool.visual.PairingBDII.basics.PointGFP1;
import org.jcryptool.visual.PairingBDII.basics.PolynomialMod;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.finitefields.GFPElement;
import de.flexiprovider.ec.ECPRNG;

public class ECDL {
    private final int nusers;
    private Vector<DHECKeyPair> UserKeys;
    private final int keysize;
    private final GFPElement a;
    private static final int windowsize = 3;

    private FlexiBigInt p;
    private FlexiBigInt l;
    private PointGFP1 P;

    private PolynomialMod XShift;
    private PolynomialMod YShift;
    private PolynomialMod pol;

    private boolean haselimtrue;

    private final Vector<PointGFP1> prelist;
    private Vector<PolynomialMod> X;
    private Vector<PolynomialMod> InvX;
    private Vector<PolynomialMod> remember;
    private Vector<PolynomialMod> keys;

    private Vector<FlexiBigInt> desc;
    private GFPElement gen;
    private final int m;

    // myorder = l;
    // mykeysize = maybe size of l
    // myp = prime p
    // P is pointGFP1.
    // GFPElement mya = par. of curve.
    // distortion map pars: myXShift, myYShift in shape of polynomials;
    // mypol: polynomial which creates second group
    // elimtrue: elimination is true;
    // BNCurve: is this a BNCurve or not.

    public ECDL(int mynusers, FlexiBigInt myp, FlexiBigInt myorder, int mykeysize, PointGFP1 P, GFPElement mya,
            PolynomialMod myXShift, PolynomialMod myYShift, PolynomialMod mypol, boolean elimtrue) {

        nusers = mynusers;
        keysize = mykeysize;
        a = mya;
        UserKeys = new Vector<DHECKeyPair>(nusers);
        UserKeys.setSize(nusers);

        keys = new Vector<PolynomialMod>(nusers);
        keys.setSize(nusers);

        final Vector<FlexiBigInt> desc = BinaryDecomposition(myorder);

        // first we need to get the keys right
        prelist = P.PreList(P, a, windowsize);

        for (int i = 0; i < nusers; i++) {
            do {
                gen = new GFPElement(new FlexiBigInt(keysize, new ECPRNG()), myorder);
            } while (gen.isZero());
            UserKeys.set(i, new DHECKeyPair(myp, P, a, gen, prelist));
        }

        // there are 2m or 2m+1 users. I need to store the X values of participants 3 to 2m-1, in steps of two.
        // That's (2m-1-3)/2 + 1 = m-1 users.

        m = nusers / 2;

        X = new Vector<PolynomialMod>(m - 1);
        X.setSize(m - 1);
        InvX = new Vector<PolynomialMod>(m - 1);
        InvX.setSize(m - 1);
        remember = new Vector<PolynomialMod>(m - 1);
        remember.setSize(m - 1);

        // the last one is a special case because of the odd/even difference.
        int pos;
        GFPtoGFP2Pairing intpair;

        for (int i = 3; i < 2 * m - 2; i = i + 2) {
            pos = ((i - 3) / 2);
            intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(i - 2), GetPKof(i - 3), desc, elimtrue,
                    mypol, myorder, a);
            remember.set(pos, intpair.value);

            intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(i), GetPKof(i + 1), desc, elimtrue, mypol,
                    myorder, a);
            X.set(pos, intpair.value.MultiplyMod(remember.get(pos).InvertMod(mypol), mypol));
            X.set(pos, X.get(pos).PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), mypol));
            InvX.set(pos, X.get(pos).InvertMod(mypol));
        }

        // position m-2 of the vector = the space for user 2m-1, which is stored in the space 2m-2.

        pos = m - 2;
        DHECKeyPair adj;
        if (nusers % 2 == 0) {
            adj = UserKeys.get(0);
        } else {
            adj = UserKeys.get(nusers - 1);
        }

        intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(2 * m - 3), GetPKof(2 * m - 4), desc,
                elimtrue, mypol, myorder, a);
        remember.set(pos, intpair.value);
        intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(2 * m - 1), adj.getPK(), desc, elimtrue,
                mypol, myorder, a);
        X.set(pos, intpair.value.MultiplyMod(remember.get(pos).InvertMod(mypol), mypol));
        X.set(pos, X.get(pos).PowerModFBI(GetSKof(2 * m - 2).toFlexiBigInt(), mypol));
        InvX.set(pos, X.get(pos).InvertMod(mypol));

        // Now we have to do step 3.

        // key of 1:
        PolynomialMod s, t;

        intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(1), GetPKof(2), desc, elimtrue, mypol,
                myorder, a);
        s = intpair.value.PowerModFBI(GetSKof(0).toFlexiBigInt(), mypol);
        t = s;

        for (int i = 0; i < m - 2; i++) {
            s = s.MultiplyMod(X.get(i), mypol);
            t = t.MultiplyMod(s, mypol);
        }

        keys.set(0, t);

        // other keys: split even from odd, leave last aside.

        // even users: 2,4,6,..., 2m-2 ; leave 2m special case;
        PolynomialMod help;
        int cpos;

        // what I need to do: I have m - j - 1 terms in the first bracket and
        // j - 1 terms in the second bracket.
        // together, that's m-j-1+j-1 = m-2 terms.
        // the key for 2m might be different if nusers is odd or even;

        // for i = 2, the first bracket is 1,
        // there are m-j-1 terms = m-2 terms.
        int begin;

        for (int i = 2; i < 2 * m + 1; i = i + 2) {
            pos = (i / 2);
            if (i == 2 * m) {
                intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(i - 2), adj.getPK(), desc, elimtrue,
                        mypol, myorder, a);
                s = intpair.value.PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), mypol)
                        .MultiplyMod(InvX.get(m - 2), mypol);
                t = s;
                help = s;
            } else {
                intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(i - 2), GetPKof(i), desc, elimtrue,
                        mypol, myorder, a);
                s = intpair.value.PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), mypol);
                t = s;
                help = s;
            }
            if (i != 2) {
                if (i != 2 * m) {
                    begin = 2 * pos - 1;
                } else {
                    begin = 2 * pos - 3;
                }
                for (int j = begin; j > 2; j = j - 2) {
                    cpos = ((j - 3) / 2);
                    s = s.MultiplyMod(InvX.get(cpos), mypol);
                    t = t.MultiplyMod(s, mypol);
                }
                keys.set(i - 1, t);
            }

            else {
                keys.set(i - 1, help);
            }

            if (i != 2 * m && i != 2 * m - 2) {
                s = help.MultiplyMod(X.get(pos - 1), mypol); // begin at X_(2*pos + 1). In the vector, this is:
                // X_((2*pos - 2)/2)
                t = s;

                for (int j = 2 * pos + 3; j < 2 * m - 2; j = j + 2) {
                    cpos = ((j - 3) / 2);
                    s = s.MultiplyMod(X.get(cpos), mypol);
                    t = t.MultiplyMod(s, mypol);
                }

                keys.set(i - 1, keys.get(i - 1).MultiplyMod(t, mypol));

            }

        }

        // odd users. If i reaches nusers in the iterator, then nusers is odd.
        // there are no exceptions here.

        for (int i = 3; i <= nusers; i = i + 2) {
            pos = ((i - 1) / 2);
            if (i == nusers) {
                intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(i - 2), GetPKof(i - 3), desc,
                        elimtrue, mypol, myorder, a);
                s = intpair.value.PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), mypol)
                        .MultiplyMod(InvX.get(m - 2), mypol);
                t = s;
                help = s;
            } else {
                s = remember.get(((i - 3) / 2)).PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), mypol);
                t = s;
                help = s;
            }
            if (i != 3) {
                if (i != nusers) {
                    begin = 2 * pos - 1;
                } else {
                    begin = 2 * pos - 3;
                }
                for (int j = begin; j > 2; j = j - 2) {
                    cpos = ((j - 3) / 2);
                    s = s.MultiplyMod(InvX.get(cpos), mypol);
                    t = t.MultiplyMod(s, mypol);
                }
                keys.set(i - 1, t);
            }

            else {
                keys.set(i - 1, help);
            }

            if (i != 2 * m - 1 && i != nusers) {
                s = help.MultiplyMod(X.get(pos - 1), mypol);
                t = s;

                for (int j = 2 * pos + 3; j < 2 * m - 2; j = j + 2) {
                    cpos = ((j - 3) / 2);
                    s = s.MultiplyMod(X.get(cpos), mypol);
                    t = t.MultiplyMod(s, mypol);
                }

                keys.set(i - 1, keys.get(i - 1).MultiplyMod(t, mypol));

            }

            // else{
            // keys.set(2*m-2, keys.get(2*m-2).MultiplyMod(help, mypol));
            // }
            // at this point, keys = (Ti^ri)^(j)()^(-1)

        }
    }

    public ECDL(int mynusers, FlexiBigInt myp, FlexiBigInt myorder, int mykeysize, PointGFP1 myP, GFPElement mya,
            PolynomialMod myXShift, PolynomialMod myYShift, PolynomialMod mypol, boolean elimtrue, boolean BNCurve) {
        nusers = mynusers;
        p = myp;
        l = myorder;
        P = myP;
        a = mya;
        m = nusers / 2;

        keysize = mykeysize;

        XShift = myXShift;
        YShift = myYShift;
        pol = mypol;

        haselimtrue = elimtrue;

        desc = BinaryDecomposition(myorder);
        prelist = P.PreList(P, a, windowsize);
    }

    public boolean AreEqual() {
        PolynomialMod Key;
        boolean arethesame = true;

        Key = GetKeyof(0);
        for (int i = 1; i < nusers; i++) {
            if (!GetKeyof(i).Equals(Key)) {
                arethesame = false;
            }
        }

        return arethesame;
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

    public PolynomialMod GetKeyof(int i) {
        return keys.get(i);
    }

    public PointGFP1 GetPKof(int i) {
        return UserKeys.get(i).getPK();
    }

    public GFPElement GetSKof(int i) {
        return UserKeys.get(i).getSK();
    }

    public Vector<PolynomialMod> ReturnInvx() {
        return InvX;
    }

    // 0 and 7 are not working.

    public Vector<PolynomialMod> ReturnX() {
        return X;
    }

    public Vector<DHECKeyPair> Step1() {
        UserKeys = new Vector<DHECKeyPair>(nusers);
        UserKeys.setSize(nusers);

        for (int i = 0; i < nusers; i++) {
            do {
                gen = new GFPElement(new FlexiBigInt(keysize, new ECPRNG()), l);

            } while (gen.isZero());
            UserKeys.set(i, new DHECKeyPair(p, P, a, gen, prelist));
        }

        return UserKeys;
    }

    public void Step2() {
        X = new Vector<PolynomialMod>(m - 1);
        X.setSize(m - 1);
        InvX = new Vector<PolynomialMod>(m - 1);
        InvX.setSize(m - 1);
        remember = new Vector<PolynomialMod>(m - 1);
        remember.setSize(m - 1);

        int pos;
        GFPtoGFP2Pairing intpair;

        for (int i = 3; i < 2 * m - 2; i = i + 2) {
            pos = ((i - 3) / 2);
            intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(i - 2), GetPKof(i - 3), desc, haselimtrue,
                    pol, l, a);
            remember.set(pos, intpair.value);

            intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(i), GetPKof(i + 1), desc, haselimtrue, pol, l,
                    a);
            X.set(pos, intpair.value.MultiplyMod(remember.get(pos).InvertMod(pol), pol));
            X.set(pos, X.get(pos).PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), pol));
            InvX.set(pos, X.get(pos).InvertMod(pol));
        }

        // position m-2 of the vector = the space for user 2m-1, which is stored in the space 2m-2.

        pos = m - 2;
        DHECKeyPair adj;
        if (nusers % 2 == 0) {
            adj = UserKeys.get(0);
        } else {
            adj = UserKeys.get(nusers - 1);
        }

        intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(2 * m - 3), GetPKof(2 * m - 4), desc, haselimtrue,
                pol, l, a);
        remember.set(pos, intpair.value);
        intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(2 * m - 1), adj.getPK(), desc, haselimtrue, pol,
                l, a);
        X.set(pos, intpair.value.MultiplyMod(remember.get(pos).InvertMod(pol), pol));
        X.set(pos, X.get(pos).PowerModFBI(GetSKof(2 * m - 2).toFlexiBigInt(), pol));
        InvX.set(pos, X.get(pos).InvertMod(pol));

    }

    public Vector<PolynomialMod> Step3() {
        keys = new Vector<PolynomialMod>(nusers);
        keys.setSize(nusers);

        // key of 1:
        PolynomialMod s, t;
        int pos;
        DHECKeyPair adj;

        GFPtoGFP2Pairing intpair;

        intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(1), GetPKof(2), desc, haselimtrue, pol, l, a);
        s = intpair.value.PowerModFBI(GetSKof(0).toFlexiBigInt(), pol);
        t = s;

        for (int i = 0; i < m - 2; i++) {
            s = s.MultiplyMod(X.get(i), pol);
            t = t.MultiplyMod(s, pol);
        }

        keys.set(0, t);

        // other keys: split even from odd, leave last aside.

        // even users: 2,4,6,..., 2m-2 ; leave 2m special case;
        PolynomialMod help;
        int cpos;

        int begin;

        if (nusers % 2 == 0) {
            adj = UserKeys.get(0);
        } else {
            adj = UserKeys.get(nusers - 1);
        }

        for (int i = 2; i < 2 * m + 1; i = i + 2) {
            pos = (i / 2);
            if (i == 2 * m) {
                intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(i - 2), adj.getPK(), desc, haselimtrue,
                        pol, l, a);
                s = intpair.value.PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), pol).MultiplyMod(InvX.get(m - 2), pol);
                t = s;
                help = s;
            } else {
                intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(i - 2), GetPKof(i), desc, haselimtrue,
                        pol, l, a);
                s = intpair.value.PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), pol);
                t = s;
                help = s;
            }
            if (i != 2) {
                if (i != 2 * m) {
                    begin = 2 * pos - 1;
                } else {
                    begin = 2 * pos - 3;
                }
                for (int j = begin; j > 2; j = j - 2) {
                    cpos = ((j - 3) / 2);
                    s = s.MultiplyMod(InvX.get(cpos), pol);
                    t = t.MultiplyMod(s, pol);
                }
                keys.set(i - 1, t);
            }

            else {
                keys.set(i - 1, help);
            }

            if (i != 2 * m && i != 2 * m - 2) {
                s = help.MultiplyMod(X.get(pos - 1), pol); // begin at X_(2*pos + 1). In the vector, this is: X_((2*pos
                // - 2)/2)
                t = s;

                for (int j = 2 * pos + 3; j < 2 * m - 2; j = j + 2) {
                    cpos = ((j - 3) / 2);
                    s = s.MultiplyMod(X.get(cpos), pol);
                    t = t.MultiplyMod(s, pol);
                }

                keys.set(i - 1, keys.get(i - 1).MultiplyMod(t, pol));

            }

        }

        // odd users. If i reaches nusers in the iterator, then nusers is odd.
        // there are no exceptions here.

        for (int i = 3; i <= nusers; i = i + 2) {
            pos = ((i - 1) / 2);
            if (i == nusers) {
                intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(i - 2), GetPKof(i - 3), desc, haselimtrue,
                        pol, l, a);
                s = intpair.value.PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), pol).MultiplyMod(InvX.get(m - 2), pol);
                t = s;
                help = s;
            } else {
                s = remember.get(((i - 3) / 2)).PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), pol);
                t = s;
                help = s;
            }
            if (i != 3) {
                if (i != nusers) {
                    begin = 2 * pos - 1;
                } else {
                    begin = 2 * pos - 3;
                }
                for (int j = begin; j > 2; j = j - 2) {
                    cpos = ((j - 3) / 2);
                    s = s.MultiplyMod(InvX.get(cpos), pol);
                    t = t.MultiplyMod(s, pol);
                }
                keys.set(i - 1, t);
            }

            else {
                keys.set(i - 1, help);
            }

            if (i != 2 * m - 1 && i != nusers) {
                s = help.MultiplyMod(X.get(pos - 1), pol);
                t = s;

                for (int j = 2 * pos + 3; j < 2 * m - 2; j = j + 2) {
                    cpos = ((j - 3) / 2);
                    s = s.MultiplyMod(X.get(cpos), pol);
                    t = t.MultiplyMod(s, pol);
                }

                keys.set(i - 1, keys.get(i - 1).MultiplyMod(t, pol));

            }

        }

        return keys;
    }
}
