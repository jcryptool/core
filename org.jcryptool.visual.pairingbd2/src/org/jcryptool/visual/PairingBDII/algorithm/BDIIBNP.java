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

import java.math.BigInteger;
import java.util.Random;
import java.util.Vector;

import org.jcryptool.visual.PairingBDII.BNCurve;
import org.jcryptool.visual.PairingBDII.BNCurve2;
import org.jcryptool.visual.PairingBDII.BNField12;
import org.jcryptool.visual.PairingBDII.BNPairing;
import org.jcryptool.visual.PairingBDII.BNParams;
import org.jcryptool.visual.PairingBDII.BNPoint;
import org.jcryptool.visual.PairingBDII.BNPoint2;
import org.jcryptool.visual.PairingBDII.basics.DHECKeyPair2;

import de.flexiprovider.common.math.FlexiBigInt;

public class BDIIBNP {
    private static final int numBits = 160;
    private final BNCurve E;
    private final BNPoint P;
    private final BNPoint2 Q;
    private final BNCurve2 Eprime;
    private final BNParams pars;

    private final int nusers;
    private Vector<DHECKeyPair2> userData;
    private Vector<BNField12> userKeys;
    private Vector<BNField12> userX;
    private Vector<BNField12> remember;

    private BNPairing pair;
    private BNField12 pairres;

    private Vector<Integer> userPkState;
    private int nUsers2Keys; // number of users with two keys, i.e. with pkstate = 2
    private final int norows; // norows = number of rows of the arrangement.
    private int firstleaf;

    // firstleaf = first leaf on last row.

    public BDIIBNP(int mynusers) {
        pars = new BNParams(numBits);
        E = new BNCurve(pars);
        P = E.getG();
        Eprime = new BNCurve2(E);
        Q = Eprime.getGt();

        nusers = mynusers;

        norows = FindLastRow();

        // Step1();
        // Step2();
        // Step3();
        // AllEqual();

    }

    // in Step1, each user generates keys.

    public boolean AllEqual() {
        final BNField12 ConfKey = userKeys.get(0);
        boolean areEqual = true;

        for (int i = 1; i < nusers; i++) {
            if (!userKeys.get(i).equals(ConfKey)) {
                areEqual = false;
            }
        }

        return areEqual;
    }

    // in step 2, each user apart from the leaves computes and multicasts to
    // its descendants X lchildren and X rchildren.

    public BNField12 ComputeKeyOf(int userindex) {
        BNField12 result = pairres; // this choice is just comfortable, as pairres will by this time have some value
        pair = new BNPairing(Eprime);

        int currPos = userindex; // currPos stores the current position in the product of X's.
        // indexed 1 to N

        // the pairing;
        // non-leaves do not have to recompute it.

        if (userindex < firstleaf) {
            result = remember.get(userindex - 1).exp(userData.get(userindex - 1).GetSK());
        } else {
            if (HasSibling(userindex)) {

                if (userData.get(GetParentOf(userindex) - 1).HasPKR()
                        && userData.get(GetSiblingOf(userindex) - 1).HasPKS()) {
                    pairres = pair.tate(GetKey1from(GetParentOf(userindex)), GetKey2from(GetSiblingOf(userindex)));
                    result = pairres.exp(userData.get(userindex - 1).GetSK());
                } else {
                    pairres = pair.tate(GetKey1from(GetSiblingOf(userindex)), GetKey2from(GetParentOf(userindex)));
                    result = pairres.exp(userData.get(userindex - 1).GetSK());
                }
            } else {
                if (userData.get(GetParentOf(userindex) - 1).HasPKR() && userData.get(userindex - 1).HasPKS()) {
                    pairres = pair.tate(GetKey1from(GetParentOf(userindex)), GetKey2from(userindex));
                    result = pairres.exp(userData.get(userindex - 1).GetSK());
                } else {
                    pairres = pair.tate(GetKey1from(userindex), GetKey2from(GetParentOf(userindex)));
                    result = pairres.exp(userData.get(userindex - 1).GetSK());
                }
            }

            // now the product of X's.

            while (currPos > 0) {
                result = result.multiply(userX.get(currPos - 1));
                if (currPos == 1) {
                    currPos = 0;
                } else {
                    currPos = GetParentOf(currPos);
                }
            }
        }
        return result;
    }

    // returns the key of user userindex.
    // userindex takes integer values between 1 and N

    // finds the index of the last row
    public int FindLastRow() {

        int currentnrow = 1;

        while (nusers > NUsersForNRows(currentnrow)) {
            currentnrow++;
        }

        return currentnrow;
    }

    // In Step 3, each user computes its key.

    // return the index of the first user of the last leaf, when indexed 1 to N.
    public int FirstLastRow(int nrows) {
        int result = 3;
        int lastrow = 3;
        for (int i = 2; i < nrows; i++) {
            lastrow = 4 * lastrow;
            result = result + lastrow;
        }

        return result + 1;
    }

    public BNCurve GetE() {
        return E;
    }

    public BNCurve2 GetEp() {
        return Eprime;
    }

    // returns the curves E and E'

    // returns the 1st (P-based) pk of user i indexed 1 to nusers
    public BNPoint GetKey1from(int j) {
        return userData.get(j - 1).GetPKR();
    }

    // returns the 2nd (Q-based) pk of user j indexed 1 to nusers
    public BNPoint2 GetKey2from(int j) {
        return userData.get(j - 1).GetPKS();
    }

    public int GetNonLeafs() {
        return firstleaf - 1;
    }

    public int GetNUsers() {
        return nusers;
    }

    public int GetNUsers2keys() {
        return nUsers2Keys;
    }

    // returns the parent of user i, indexed as U_1 ... U_n
    // the result itself is indexed 1 to N.
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

    public BNParams GetPars() {
        return pars;
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

    // returns the number of left children a user has.

    public BigInteger GetSKof(int j) {
        return userData.get(j - 1).GetSK();
    }

    // returns the left children of user i, indexed 1 to N
    // the results are also indexed 1 to N;

    // userindex is indexed 1 to N.
    public boolean HasSibling(int userindex) {
        boolean hasSibling = false;

        if (GetSiblingOf(userindex) != userindex) {
            hasSibling = true;
        }

        return hasSibling;
    }

    public int LeftChild1Of(int i) {
        return 4 * i + 2;
    }

    // returns the right children of user i, indexed as U_1 ... U_n
    // the results are also indexed 1 to N

    public int LeftChild2Of(int i) {
        return 4 * i + 3;
    }

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

    public int RightChild1Of(int i) {
        return 4 * i;
    }

    public int RightChild2Of(int i) {
        return 4 * i + 1;
    }

    public Vector<DHECKeyPair2> Step1() {
        userData = new Vector<DHECKeyPair2>(nusers);
        userData.setSize(nusers);
        userPkState = new Vector<Integer>(nusers);
        userPkState.setSize(nusers);

        firstleaf = GetParentOf(nusers) + 1;
        nUsers2Keys = 0;
        int currUB = firstleaf - 1; // current upper bound of layer.
        int currLB = nusers; // current lower bound of layer. (lower bound has higher index than upper bound in this
        // case)
        int currNRow = 1; // current number of rows, indexed from outside in.

        while (currNRow < norows) {
            for (int i = currLB; i > currUB; i--) {
                if (currNRow % 2 == 1) {
                    if (!HasSibling(i)) {
                        userPkState.set(i - 1, 2);
                        nUsers2Keys++;
                    } else {
                        if (i % 2 == (currUB % 2)) {
                            userPkState.set(i - 1, 1);
                        } else {
                            userPkState.set(i - 1, 0);
                        }
                    }
                } else {
                    userPkState.set(i - 1, 2);
                    nUsers2Keys++;
                }

            }

            currLB = currUB;
            currUB = GetParentOf(currLB);
            currNRow++;
        }

        for (int i = currLB; i > 0; i--) {

            userPkState.set(i - 1, 2);
            nUsers2Keys++;
        }

        BigInteger mysk;

        for (int i = 1; i < nusers + 1; i++) {
            do {
                mysk = new BigInteger(numBits, new Random());
            } while (mysk.compareTo(BigInteger.ZERO) == 0);

            userData.set(i - 1, new DHECKeyPair2(E, Eprime, P, Q, mysk.mod(pars.getN()), userPkState.get(i - 1)));
        }

        return userData;

    }

    public Vector<BNField12> Step2() {
        userX = new Vector<BNField12>(nusers); // users 1, 2, 3 do not have X's.
        userX.setSize(nusers);

        final BNField12 ONE = new BNField12(pars, BigInteger.ONE);

        remember = new Vector<BNField12>(firstleaf - 1);
        remember.setSize(firstleaf - 1);

        // initialise X_1 = X_2 = X_3
        for (int i = 0; i < 3; i++) {
            userX.set(i, ONE);
        }

        pair = new BNPairing(Eprime);

        for (int i = firstleaf - 1; i > 0; i--) {

            // numerator.

            if (userData.get(GetParentOf(i) - 1).HasPKR() && userData.get(GetSiblingOf(i) - 1).HasPKS()) {
                pairres = pair.tate(GetKey1from(GetParentOf(i)), GetKey2from(GetSiblingOf(i)));
            } else {
                pairres = pair.tate(GetKey1from(GetSiblingOf(i)), GetKey2from(GetParentOf(i)));
            }

            remember.set(i - 1, pairres);

            if (NLeftSons(i) == 1) {
                userX.set(LeftChild1Of(i) - 1, pairres);
                userX.set(RightChild1Of(i) - 1, pairres);
                userX.set(RightChild2Of(i) - 1, pairres);
            } else {
                if (NLeftSons(i) == 2) {
                    userX.set(LeftChild1Of(i) - 1, pairres);
                    userX.set(LeftChild2Of(i) - 1, pairres);
                    userX.set(RightChild1Of(i) - 1, pairres);
                    userX.set(RightChild2Of(i) - 1, pairres);
                } else { // NLeftSons == 0
                    if (NRightSons(i) == 1) {
                        userX.set(RightChild1Of(i) - 1, pairres);
                    } else {
                        userX.set(RightChild1Of(i) - 1, pairres);
                        userX.set(RightChild2Of(i) - 1, pairres);
                    }
                }
            }

            // denominator

            if (NLeftSons(i) != 0) { // user could have 1 or 2 left children

                if (NLeftSons(i) == 1) {

                    pairres = pair.tate(GetKey1from(LeftChild1Of(i)), GetKey2from(LeftChild1Of(i)));
                    userX.set(LeftChild1Of(i) - 1, userX.get(LeftChild1Of(i) - 1).multiply(pairres.inverse()));
                    userX.set(LeftChild1Of(i) - 1, userX.get(LeftChild1Of(i) - 1).exp(userData.get(i - 1).GetSK()));
                } else {
                    if (userData.get(LeftChild1Of(i) - 1).HasPKR() && userData.get(LeftChild2Of(i) - 1).HasPKS()) {
                        pairres = pair.tate(GetKey1from(LeftChild1Of(i)), GetKey2from(LeftChild2Of(i)));
                    } else {
                        pairres = pair.tate(GetKey1from(LeftChild2Of(i)), GetKey2from(LeftChild1Of(i)));
                    }
                    userX.set(LeftChild1Of(i) - 1, userX.get(LeftChild1Of(i) - 1).multiply(pairres.inverse()));
                    userX.set(LeftChild2Of(i) - 1, userX.get(LeftChild2Of(i) - 1).multiply(pairres.inverse()));
                    userX.set(LeftChild1Of(i) - 1, userX.get(LeftChild1Of(i) - 1).exp(userData.get(i - 1).GetSK()));
                    userX.set(LeftChild2Of(i) - 1, userX.get(LeftChild2Of(i) - 1).exp(userData.get(i - 1).GetSK()));
                }

                // if user i has at least 1 left child, it has both right children.

                if (userData.get(RightChild1Of(i) - 1).HasPKR() && userData.get(RightChild2Of(i) - 1).HasPKS()) {
                    pairres = pair.tate(GetKey1from(RightChild1Of(i)), GetKey2from(RightChild2Of(i)));
                } else {
                    pairres = pair.tate(GetKey1from(RightChild2Of(i)), GetKey2from(RightChild1Of(i)));
                }
                userX.set(RightChild1Of(i) - 1, userX.get(RightChild1Of(i) - 1).multiply(pairres.inverse()));
                userX.set(RightChild2Of(i) - 1, userX.get(RightChild2Of(i) - 1).multiply(pairres.inverse()));
                userX.set(RightChild1Of(i) - 1, userX.get(RightChild1Of(i) - 1).exp(userData.get(i - 1).GetSK()));
                userX.set(RightChild2Of(i) - 1, userX.get(RightChild2Of(i) - 1).exp(userData.get(i - 1).GetSK()));

            } else { // because of the definition of firstleaf, user has at least 1 RightChild
                if (NRightSons(i) == 1) {

                    pairres = pair.tate(GetKey1from(RightChild1Of(i)), GetKey2from(RightChild1Of(i)));
                    userX.set(RightChild1Of(i) - 1, userX.get(RightChild1Of(i) - 1).multiply(pairres.inverse()));
                    userX.set(RightChild1Of(i) - 1, userX.get(RightChild1Of(i) - 1).exp(userData.get(i - 1).GetSK()));
                } else {
                    if (userData.get(RightChild1Of(i) - 1).HasPKR() && userData.get(RightChild2Of(i) - 1).HasPKS()) {
                        pairres = pair.tate(GetKey1from(RightChild1Of(i)), GetKey2from(RightChild2Of(i)));
                    } else {
                        pairres = pair.tate(GetKey1from(RightChild2Of(i)), GetKey2from(RightChild1Of(i)));
                    }
                    userX.set(RightChild1Of(i) - 1, userX.get(RightChild1Of(i) - 1).multiply(pairres.inverse()));
                    userX.set(RightChild2Of(i) - 1, userX.get(RightChild2Of(i) - 1).multiply(pairres.inverse()));
                    userX.set(RightChild1Of(i) - 1, userX.get(RightChild1Of(i) - 1).exp(userData.get(i - 1).GetSK()));
                    userX.set(RightChild2Of(i) - 1, userX.get(RightChild2Of(i) - 1).exp(userData.get(i - 1).GetSK()));
                }
            }

        }

        return userX;
    }

    public Vector<BNField12> Step3() {
        userKeys = new Vector<BNField12>(nusers);
        userKeys.setSize(nusers);

        for (int i = 1; i < nusers + 1; i++) {
            userKeys.set(i - 1, ComputeKeyOf(i));
        }

        return userKeys;
    }

}
