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
package org.jcryptool.visual.PairingBDII.algorithm;

import java.util.Vector;

import org.jcryptool.visual.PairingBDII.basics.DHECKeyPair;
import org.jcryptool.visual.PairingBDII.basics.GFPtoGFP2Pairing;
import org.jcryptool.visual.PairingBDII.basics.PointGFP1;
import org.jcryptool.visual.PairingBDII.basics.PolynomialMod;
import org.jcryptool.visual.PairingBDII.basics.WeilGFPtoGFP2;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.finitefields.GFPElement;
import de.flexiprovider.ec.ECPRNG;

public class ECBDII {
    private final int nusers;
    private final Vector<DHECKeyPair> UserKeys;
    private final int keysize;
    private final GFPElement a;
    private static final int windowsize = 3;
    private FlexiBigInt order;
    private FlexiBigInt p;
    private PointGFP1 P;
    private PolynomialMod XShift;
    private PolynomialMod YShift;
    private PolynomialMod pol;
    private boolean withelim;
    private int firstleaf;
    private int norows;

    Vector<PointGFP1> prelist;
    Vector<FlexiBigInt> desc;
    private boolean Tatepairing;
    private final Vector<PolynomialMod> X; // stores the X values in the protocol; each user except the leaves have 2
                                           // spaces,
    // one for the left children X, one for the right children X
    private final Vector<PolynomialMod> Xpers; // stores the "personal" values of the X. Users 1,2,3 don't have these X
    // values;
    private final Vector<PolynomialMod> remember;
    private final Vector<PolynomialMod> keys; // stores the conference key computed by each user.

    // generates an ECBD II object with the given parameters
    // also precomputes the list of points necessary for scalar multiplication
    // initialises the necessary vectors;
    // finds the number of rows and the index of the first user on the last row.

    public ECBDII(boolean WithTatePairing, int mynusers, FlexiBigInt myp, FlexiBigInt myorder, PointGFP1 myP,
            GFPElement mya, PolynomialMod myXShift, PolynomialMod myYShift, PolynomialMod mypol, boolean elimtrue,
            int mykeysize) {
        nusers = mynusers;
        keysize = mykeysize;
        a = mya;
        p = myp;
        order = myorder;
        P = myP;
        XShift = myXShift;
        YShift = myYShift;
        pol = mypol;
        withelim = elimtrue;

        desc = BinaryDecomposition(myorder);
        prelist = P.PreList(P, a, windowsize);
        norows = FindLastRow();
        firstleaf = FirstLastRow(norows);

        Tatepairing = WithTatePairing;
        UserKeys = new Vector<DHECKeyPair>(nusers);
        UserKeys.setSize(nusers);

        keys = new Vector<PolynomialMod>(nusers);
        keys.setSize(nusers);

        X = new Vector<PolynomialMod>(2 * firstleaf - 2);
        X.setSize(2 * firstleaf - 2);

        remember = new Vector<PolynomialMod>(firstleaf - 1);
        remember.setSize(firstleaf - 1);

        Xpers = new Vector<PolynomialMod>(nusers - 3);
        Xpers.setSize(nusers - 3);

    }

    public ECBDII(int mynusers, FlexiBigInt myp, FlexiBigInt myorder, int mykeysize, PointGFP1 P, GFPElement mya,
            PolynomialMod myXShift, PolynomialMod myYShift, PolynomialMod mypol, boolean elimtrue) {
        nusers = mynusers;
        keysize = mykeysize;
        a = mya;
        UserKeys = new Vector<DHECKeyPair>(nusers);
        UserKeys.setSize(nusers);

        keys = new Vector<PolynomialMod>(nusers);
        keys.setSize(nusers);

        GFPElement gen;
        Vector<PointGFP1> prelist;
        final Vector<FlexiBigInt> desc = BinaryDecomposition(myorder);

        // first we need to get the keys right
        prelist = P.PreList(P, a, windowsize);

        for (int i = 0; i < nusers; i++) {
            do {
                gen = new GFPElement(new FlexiBigInt(keysize, new ECPRNG()), myorder);
            } while (gen.isZero());
            UserKeys.set(i, new DHECKeyPair(myp, P, a, gen, prelist));
        }

        final int norows = FindLastRow(); // norows = 2
        final int firstleaf = FirstLastRow(norows); // firstleaf = 4

        X = new Vector<PolynomialMod>(2 * firstleaf - 2);
        X.setSize(2 * firstleaf - 2);

        remember = new Vector<PolynomialMod>(firstleaf - 1);
        remember.setSize(firstleaf - 1);

        Xpers = new Vector<PolynomialMod>(nusers - 3);
        Xpers.setSize(nusers - 3);

        int pos;
        GFPtoGFP2Pairing intpair;

        // Sets the X and Xpers vectors

        for (int i = 1; i <= GetParentOf(nusers); i++) {
            pos = (i - 1) * 2;
            intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(GetParentOf(i) - 1),
                    GetPKof(GetSiblingOf(i) - 1), desc, elimtrue, mypol, myorder, a);
            remember.set(i - 1, intpair.value);

            if (NLeftSons(i) == 1) {
                intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(LeftChild1Of(i) - 1),
                        GetPKof(LeftChild1Of(i) - 1), desc, elimtrue, mypol, myorder, a);
            } else {
                if (NLeftSons(i) == 0) {
                    intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(GetParentOf(i) - 1),
                            GetPKof(GetSiblingOf(i) - 1), desc, elimtrue, mypol, myorder, a);
                } else {
                    intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(LeftChild1Of(i) - 1),
                            GetPKof(LeftChild2Of(i) - 1), desc, elimtrue, mypol, myorder, a);
                }
            }
            X.set(pos, remember.get(i - 1).MultiplyMod(intpair.value.InvertMod(mypol), mypol));
            X.set(pos, X.get(pos).PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), mypol));

            if (NLeftSons(i) == 1) {
                Xpers.set(LeftChild1Of(i) - 4, X.get(pos));
            } else {
                if (NLeftSons(i) == 2) {
                    Xpers.set(LeftChild1Of(i) - 4, X.get(pos));
                    Xpers.set(LeftChild2Of(i) - 4, X.get(pos));
                }
            }

            pos = pos + 1;
            if (NRightSons(i) == 1) {
                intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(RightChild1Of(i) - 1),
                        GetPKof(RightChild1Of(i) - 1), desc, elimtrue, mypol, myorder, a);
            } else {
                if (NRightSons(i) == 0) {
                    intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(GetParentOf(i) - 1),
                            GetPKof(GetSiblingOf(i) - 1), desc, elimtrue, mypol, myorder, a);
                } else {
                    intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(RightChild1Of(i) - 1),
                            GetPKof(RightChild2Of(i) - 1), desc, elimtrue, mypol, myorder, a);
                }
            }
            X.set(pos, remember.get(i - 1).MultiplyMod(intpair.value.InvertMod(mypol), mypol));
            X.set(pos, X.get(pos).PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), mypol));

            if (NRightSons(i) == 1) {
                Xpers.set(RightChild1Of(i) - 4, X.get(pos));
            } else {
                if (NRightSons(i) == 2) {
                    Xpers.set(RightChild1Of(i) - 4, X.get(pos));
                    Xpers.set(RightChild2Of(i) - 4, X.get(pos));
                }
            }

        }

        // Key computation
        int j;

        for (int i = 1; i < nusers + 1; i++) {
            if (i < firstleaf) {
                keys.set(i - 1, remember.get(i - 1).PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), mypol));
            } else {
                intpair = new GFPtoGFP2Pairing(true, myXShift, myYShift, GetPKof(GetParentOf(i) - 1),
                        GetPKof(GetSiblingOf(i) - 1), desc, elimtrue, mypol, myorder, a);
                keys.set(i - 1, intpair.value.PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), mypol));
            }
            j = i;
            while (j > 3) {
                keys.set(i - 1, keys.get(i - 1).MultiplyMod(Xpers.get(j - 4), mypol));
                j = GetParentOf(j);
            }
        }
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

    // returns ceiling(a/b)
    public int CeilDivide(int a, int b) {
        if (a % b == 0) {
            return a / b;
        } else {
            return a / b + 1;
        }
    }

    // finds the index of the last row
    public int FindLastRow() {

        int currentnrow = 1;

        while (nusers > NUsersForNRows(currentnrow)) {
            currentnrow++;
        }

        return currentnrow;
    }

    public int FirstLastRow(int nrows) {
        int result = 3;
        int lastrow = 3;
        for (int i = 2; i < nrows; i++) {
            lastrow = 4 * lastrow;
            result = result + lastrow;
        }

        return result + 1;
    }

    public GFPElement GetA() {
        return a;
    }

    public int GetFirstLeafLastRow() {
        return firstleaf;
    }

    public PolynomialMod GetKeyof(int i) {
        return keys.get(i);
    }

    public Vector<PolynomialMod> GetKeys() {
        return keys;
    }

    public int GetKeySize() {
        return keysize;
    }

    public int GetNonLeafs() {
        return firstleaf - 1;
    }

    public int GetNUsers() {
        return nusers;
    }

    public FlexiBigInt GetOrder() {
        return order;
    }

    public FlexiBigInt Getp() {
        return p;
    }

    public PointGFP1 GetP() {
        return P;
    }

    // returns the parent of user i, indexed as U_1 ... U_n
    public int GetParentOf(int i) {
        if (i == 1) {
            return 2;
        }
        if (i == 2) {
            return 3;
        }
        if (i == 3) {
            return 1;
        }
        return i / 4;
    }

    public PointGFP1 GetPKof(int i) {
        return UserKeys.get(i).getPK();
    }

    public PolynomialMod Getpol() {
        return pol;
    }

    // returns the sibling of user i, indexed as U_1 ... U_n
    public int GetSiblingOf(int i) {
        if (i == 1) {
            return 3;
        }
        if (i == 2) {
            return 1;
        }
        if (i == 3) {
            return 2;
        }

        if (i % 4 == 0) {
            if (i + 1 <= nusers) {
                return i + 1;
            } else {
                return i;
            }
            // return i+1;
        } else {
            if (i % 4 == 1) {
                return i - 1;
            } else {
                if (i % 4 == 2) {
                    if (i + 1 <= nusers) {
                        return i + 1;
                    } else {
                        return i;
                    }
                    // return i+1;
                } else {
                    return i - 1;
                }
            }
        }
    }

    // returns the secret key of user i, indexed U_0 to U_{n-1}

    public GFPElement GetSKof(int i) {
        return UserKeys.get(i).getSK();
    }

    // returns the public key of user i, indexed U_0 to U_{n-1}

    public boolean GetTateWeil() {
        return Tatepairing;
    }

    // returns the key of user i, indexed U_0 to U_{n-1}

    public Vector<DHECKeyPair> GetUserKeys() {
        return UserKeys;
    }

    public boolean GetWithElim() {
        return withelim;
    }

    public Vector<PolynomialMod> GetXpers() {
        return Xpers;
    }

    // returns the number of left children a user has.

    public PolynomialMod GetXShift() {
        return XShift;
    }

    // returns the index of the first user on the last row given by nrows.

    public PolynomialMod GetYShift() {
        return YShift;
    }

    // returns the left children of user i, indexed as U_1 ... U_n

    public int LeftChild1Of(int i) {
        return 4 * i + 2;
    }

    public int LeftChild2Of(int i) {
        return 4 * i + 3;
    }

    // returns the right children of user i, indexed as U_1 ... U_n

    public int NLeftSons(int i) {
        int nleftsons = 0;
        if (NRightSons(i) < 2) {
            return 0;
        }

        if (LeftChild1Of(i) <= nusers) {
            nleftsons = 1;
        }
        if (LeftChild2Of(i) <= nusers) {
            nleftsons = 2;
        }

        return nleftsons;
    }

    // returns 0,1, or 2 depending on how many sons user i has.
    // method based on users being numbered 1 to nusers.
    public int NRightSons(int i) {
        int nrightsons = 0;
        if (RightChild1Of(i) <= nusers) {
            nrightsons = 1;
        }
        if (RightChild2Of(i) <= nusers) {
            nrightsons = 2;
        }

        return nrightsons;
    }

    public int NUsersForNRows(int k) {
        final FlexiBigInt FOUR = new FlexiBigInt("4"); //$NON-NLS-1$
        final FlexiBigInt adj = FOUR.pow(k);
        return (adj.intValue() - 1);
    }

    // reconstructs a vector from its binary decomposition
    public FlexiBigInt Reconstruct(Vector<FlexiBigInt> desc) {
        FlexiBigInt result = FlexiBigInt.ZERO;
        final FlexiBigInt TWO = new FlexiBigInt("2"); //$NON-NLS-1$
        FlexiBigInt currentpower = FlexiBigInt.ONE;
        for (int i = desc.size() - 1; i >= 0; i--) {
            result = result.add(currentpower.multiply(desc.get(i)));
            currentpower = currentpower.multiply(TWO);
        }

        return result;
    }

    public int RightChild1Of(int i) {
        return 4 * i;
    }

    public int RightChild2Of(int i) {
        return 4 * i + 1;
    }

    public Vector<DHECKeyPair> Step1() {
        GFPElement gen;

        for (int i = 0; i < nusers; i++) {
            do {
                gen = new GFPElement(new FlexiBigInt(keysize, new ECPRNG()), order);
            } while (gen.isZero());
            UserKeys.set(i, new DHECKeyPair(p, P, a, gen, prelist));
        }

        return UserKeys;
    }

    public void Step2() {

        int pos;

        GFPtoGFP2Pairing intpair = new GFPtoGFP2Pairing(p);
        WeilGFPtoGFP2 weilpair = new WeilGFPtoGFP2(p);

        for (int i = 1; i <= GetParentOf(nusers); i++) {
            pos = (i - 1) * 2;

            if (Tatepairing) {
                intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(GetParentOf(i) - 1),
                        GetPKof(GetSiblingOf(i) - 1), desc, withelim, pol, order, a);
                remember.set(i - 1, intpair.value);
            } else {
                weilpair = new WeilGFPtoGFP2(XShift, YShift, GetPKof(GetParentOf(i) - 1), GetPKof(GetSiblingOf(i) - 1),
                        desc, withelim, pol, order, a);
                remember.set(i - 1, weilpair.value);
            }

            if (NLeftSons(i) == 1) {
                if (Tatepairing) {
                    intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(LeftChild1Of(i) - 1),
                            GetPKof(LeftChild1Of(i) - 1), desc, withelim, pol, order, a);
                } else {
                    weilpair = new WeilGFPtoGFP2(XShift, YShift, GetPKof(LeftChild1Of(i) - 1),
                            GetPKof(LeftChild1Of(i) - 1), desc, withelim, pol, order, a);
                }

            } else {
                if (NLeftSons(i) == 0) {
                    if (Tatepairing) {
                        intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(GetParentOf(i) - 1),
                                GetPKof(GetSiblingOf(i) - 1), desc, withelim, pol, order, a);
                    } else {
                        weilpair = new WeilGFPtoGFP2(XShift, YShift, GetPKof(GetParentOf(i) - 1),
                                GetPKof(GetSiblingOf(i) - 1), desc, withelim, pol, order, a);
                    }

                } else {
                    if (Tatepairing) {
                        intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(LeftChild1Of(i) - 1),
                                GetPKof(LeftChild2Of(i) - 1), desc, withelim, pol, order, a);
                    } else {
                        weilpair = new WeilGFPtoGFP2(XShift, YShift, GetPKof(LeftChild1Of(i) - 1),
                                GetPKof(LeftChild2Of(i) - 1), desc, withelim, pol, order, a);
                    }

                }
            }
            if (Tatepairing) {
                X.set(pos, remember.get(i - 1).MultiplyMod(intpair.value.InvertMod(pol), pol));
            } else {
                X.set(pos, remember.get(i - 1).MultiplyMod(weilpair.value.InvertMod(pol), pol));
            }

            X.set(pos, X.get(pos).PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), pol));

            if (NLeftSons(i) == 1) {
                Xpers.set(LeftChild1Of(i) - 4, X.get(pos));
            } else {
                if (NLeftSons(i) == 2) {
                    Xpers.set(LeftChild1Of(i) - 4, X.get(pos));
                    Xpers.set(LeftChild2Of(i) - 4, X.get(pos));
                }
            }

            pos = pos + 1;
            if (NRightSons(i) == 1) {
                if (Tatepairing) {
                    intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(RightChild1Of(i) - 1),
                            GetPKof(RightChild1Of(i) - 1), desc, withelim, pol, order, a);
                } else {
                    weilpair = new WeilGFPtoGFP2(XShift, YShift, GetPKof(RightChild1Of(i) - 1),
                            GetPKof(RightChild1Of(i) - 1), desc, withelim, pol, order, a);
                }

            } else {
                if (Tatepairing) {
                    if (NRightSons(i) == 0) {
                        intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(GetParentOf(i) - 1),
                                GetPKof(GetSiblingOf(i) - 1), desc, withelim, pol, order, a);
                    } else {
                        intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(RightChild1Of(i) - 1),
                                GetPKof(RightChild2Of(i) - 1), desc, withelim, pol, order, a);
                    }
                } else {
                    if (NRightSons(i) == 0) {
                        weilpair = new WeilGFPtoGFP2(XShift, YShift, GetPKof(GetParentOf(i) - 1),
                                GetPKof(GetSiblingOf(i) - 1), desc, withelim, pol, order, a);
                    } else {
                        weilpair = new WeilGFPtoGFP2(XShift, YShift, GetPKof(RightChild1Of(i) - 1),
                                GetPKof(RightChild2Of(i) - 1), desc, withelim, pol, order, a);
                    }
                }

            }
            if (Tatepairing) {
                X.set(pos, remember.get(i - 1).MultiplyMod(intpair.value.InvertMod(pol), pol));
            } else {
                X.set(pos, remember.get(i - 1).MultiplyMod(weilpair.value.InvertMod(pol), pol));
            }

            X.set(pos, X.get(pos).PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), pol));

            if (NRightSons(i) == 1) {
                Xpers.set(RightChild1Of(i) - 4, X.get(pos));
            } else {
                if (NRightSons(i) == 2) {
                    Xpers.set(RightChild1Of(i) - 4, X.get(pos)); // the 4 is there because the first 3 users have no
                    // Xpers, and to account for the indexing from 0 to n-1
                    // rather than 1 to n.
                    Xpers.set(RightChild2Of(i) - 4, X.get(pos));
                }
            }

        }

    }

    public void Step3() {
        int j;
        GFPtoGFP2Pairing intpair;
        WeilGFPtoGFP2 weilpair;

        for (int i = 1; i < nusers + 1; i++) {
            if (i <= GetParentOf(nusers)) {
                keys.set(i - 1, remember.get(i - 1).PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), pol));
            } else {
                if (Tatepairing) {
                    intpair = new GFPtoGFP2Pairing(true, XShift, YShift, GetPKof(GetParentOf(i) - 1),
                            GetPKof(GetSiblingOf(i) - 1), desc, withelim, pol, order, a);
                    keys.set(i - 1, intpair.value.PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), pol));
                } else {
                    weilpair = new WeilGFPtoGFP2(XShift, YShift, GetPKof(GetParentOf(i) - 1),
                            GetPKof(GetSiblingOf(i) - 1), desc, withelim, pol, order, a);
                    keys.set(i - 1, weilpair.value.PowerModFBI(GetSKof(i - 1).toFlexiBigInt(), pol));
                }

            }
            j = i;
            while (j > 3) {
                keys.set(i - 1, keys.get(i - 1).MultiplyMod(Xpers.get(j - 4), pol));
                j = GetParentOf(j);
            }
        }

    }

    public boolean Verify() {
        final PolynomialMod Key = keys.get(0);
        final boolean arethesame = true;

        for (int i = 1; i < nusers; i++) {
            if (!Key.Equals(keys.get(i))) {
                return false;
            }

        }

        return arethesame;
    }

}
