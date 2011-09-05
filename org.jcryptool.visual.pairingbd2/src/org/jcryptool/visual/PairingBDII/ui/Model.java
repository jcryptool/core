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
package org.jcryptool.visual.PairingBDII.ui;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignedObject;
import java.util.Vector;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.PairingBDII.BNField12;
import org.jcryptool.visual.PairingBDII.algorithm.BDIIBNP;
import org.jcryptool.visual.PairingBDII.algorithm.ECBDII;
import org.jcryptool.visual.PairingBDII.basics.AuthMessage;
import org.jcryptool.visual.PairingBDII.basics.DHECKeyPair;
import org.jcryptool.visual.PairingBDII.basics.DHECKeyPair2;
import org.jcryptool.visual.PairingBDII.basics.PointGFP1;
import org.jcryptool.visual.PairingBDII.basics.PolynomialMod;
import org.jcryptool.visual.PairingBDII.basics.UserData_BNP;
import org.jcryptool.visual.PairingBDII.basics.UserData_ECBDII;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.finitefields.GFPElement;
import de.flexiprovider.common.util.DefaultPRNG;

public class Model {

    public static final int DEGREE_LARGE = 0;
    public static final int DEGREE_SMALL = 1;
    public static final int PENANDPAPER = 2;
    public static final int INDUSTRIALSECURITY = 3;
    public static final int TATEPAIRING = 4;
    public static final int WEILPARING = 5;

    public int keysize;
    public int numberOfUsers;
    public int currentStep = 0;
    public int laststep;
    public int infoUserIndex;
    public boolean parsgenerated;
    public boolean isTatePairing;
    public boolean haselimtrue;
    public boolean clearPairing;
    public boolean isOnBNCurve;
    public boolean situationerrorfilled;
    public boolean clearStep3;
    public boolean clearverify;
    public boolean keysareequal;
    public boolean clearStep1;
    public boolean clearStep0;
    public boolean checkmessage;
    public boolean clearStep2;
    public long stopstep1;
    public long startstep1;
    public long startstep2;
    public long stopstep2;
    public long startstep3;
    public long stopstep3;
    public long startstep0;
    public long stopstep0;
    public long bduser;
    public long unbduser;
    public GFPElement a;
    public PolynomialMod xShift;
    public PolynomialMod yShift;
    public PolynomialMod pol;
    public FlexiBigInt q;
    public FlexiBigInt l;
    public PointGFP1 P;
    public BDIIBNP algorithmBN;
    public ECBDII algorithm;
    public Vector<FlexiBigInt> nonces;
    public Vector<PrivateKey> sk;
    public Vector<PublicKey> pk;
    public Vector<UserData_ECBDII> usersData;
    public Vector<UserData_BNP> bnpuData;
    public Vector<DHECKeyPair> keysstep1;
    public Vector<PolynomialMod> keysstep2;
    public Vector<PolynomialMod> keysstep2a;
    public Vector<PolynomialMod> keysstep2b;
    public Vector<PolynomialMod> keysstep3;
    public Vector<DHECKeyPair2> keysstep1BN;
    public Vector<BNField12> keysstep2BN;
    public Vector<BNField12> keysstep3BN;
    public String message;
    public SignedObject signedmessage;
    public String non = "";

    private static Model model;

    public static Model getDefault() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    private IntroductionAndParameters situation;
    private Illustration illustration;
    private DefinitionAndDetails protocol;

    private Logging tryagain;

    public void reset() {
        model = new Model();
        model.setLinks(situation, illustration, protocol, tryagain);
        model.numberOfUsers = situation.getText().getSelection();
        if (situation.getEmbeddedDegreeLarge().getSelection()) {
            model.setParameter(Model.DEGREE_LARGE);
        } else {
            model.setParameter(Model.DEGREE_SMALL);
        }

        if (situation.getIndustrialSecurity().getSelection()) {
            model.setParameter(Model.INDUSTRIALSECURITY);
        } else {
            model.setParameter(Model.PENANDPAPER);
        }

        if (situation.getTatePairing().getSelection()) {
            model.setParameter(Model.TATEPAIRING);
        } else {
            model.setParameter(Model.WEILPARING);
        }

        protocol.step1ExplVisible(false);
        illustration.getCanvas().redraw();
    }

    public void setLinks(IntroductionAndParameters situation, Illustration illustration, DefinitionAndDetails protocol,
            Logging tryagain) {
        this.situation = situation;
        this.illustration = illustration;
        this.protocol = protocol;
        this.tryagain = tryagain;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        reset();
        this.numberOfUsers = numberOfUsers;
        protocol.setMaximumNumberOfUsers(numberOfUsers);
    }

    public void setParameter(int parameter) {
        if (parameter == DEGREE_LARGE) {
            isOnBNCurve = false;
            illustration.changeToKis2(true);
            illustration.setStep1Enabled(false);
            protocol.changetoKis2(true);

        }
        if (parameter == DEGREE_SMALL) {
            isOnBNCurve = true;
            illustration.changeToKis2(false);
            illustration.setStep1Enabled(false);
            protocol.changetoKis2(false);
            situation.setBNCurves();

        }
        if (parameter == PENANDPAPER) {
            situation.setLowSecurity();

            q = new FlexiBigInt("103"); //$NON-NLS-1$
            l = new FlexiBigInt("13"); //$NON-NLS-1$

            final Vector<GFPElement> Pcoords = new Vector<GFPElement>(2);
            Pcoords.setSize(2);

            Pcoords.set(0, new GFPElement(new FlexiBigInt("26"), q)); //$NON-NLS-1$
            Pcoords.set(1, new GFPElement(new FlexiBigInt("35"), q)); //$NON-NLS-1$

            P = new PointGFP1(q, true, Pcoords);
            keysize = 5;
            a = new GFPElement(FlexiBigInt.ONE, q);

            Vector<GFPElement> p3, p4, p5;
            p3 = new Vector<GFPElement>(2);
            p4 = new Vector<GFPElement>(3);
            p5 = new Vector<GFPElement>(2);
            p3.setSize(2);
            p4.setSize(3);
            p5.setSize(2);
            p3.set(1, GFPElement.ONE(q).negate());
            p3.set(0, GFPElement.ZERO(q));
            p4.set(0, GFPElement.ONE(q));
            p4.set(1, GFPElement.ZERO(q));
            p4.set(2, GFPElement.ONE(q));
            p5.set(0, GFPElement.ONE(q));
            p5.set(1, GFPElement.ZERO(q));

            PolynomialMod pol3, pol4, pol5;
            pol3 = new PolynomialMod(true, p3, q);
            pol4 = new PolynomialMod(true, p4, q);
            pol5 = new PolynomialMod(true, p5, q);

            xShift = pol3;
            yShift = pol5;
            pol = pol4;

            parsgenerated = true;
        }
        if (parameter == INDUSTRIALSECURITY) {
            situation.setHighSecurity();

            q = new FlexiBigInt(
                    "13407807929942597099574024998205846127479365820656611338971283446046867015142005411144178117863615299181622743868603093972561776274533618051815316861876223"); //$NON-NLS-1$
            l = new FlexiBigInt("1461501637330902918203684832716283019655932542983"); //$NON-NLS-1$

            final Vector<GFPElement> Pcoords = new Vector<GFPElement>(2);
            Pcoords.setSize(2);

            Pcoords
                    .set(
                            0,
                            new GFPElement(
                                    new FlexiBigInt(
                                            "9537989506416121095013645232844302091864464324522274801469622143083463242058243968851316630179778495427481368972301592029924677781896780819879883466109076"), //$NON-NLS-1$
                                    q));
            Pcoords
                    .set(
                            1,
                            new GFPElement(
                                    new FlexiBigInt(
                                            "8774011707516965344315296795300650636428511623440304200728507178506743028554747280637296913461842283569539440038315214430606251757242375714832950517160787"), //$NON-NLS-1$
                                    q));

            P = new PointGFP1(q, true, Pcoords);
            keysize = 160;
            a = new GFPElement(FlexiBigInt.ONE, q);

            final Vector<GFPElement> p3 = new Vector<GFPElement>(2);
            final Vector<GFPElement> p4 = new Vector<GFPElement>(3);
            final Vector<GFPElement> p5 = new Vector<GFPElement>(2);
            p3.setSize(2);
            p4.setSize(3);
            p5.setSize(2);
            p3.set(1, GFPElement.ONE(q).negate());
            p3.set(0, GFPElement.ZERO(q));
            p4.set(0, GFPElement.ONE(q));
            p4.set(1, GFPElement.ZERO(q));
            p4.set(2, GFPElement.ONE(q));
            p5.set(0, GFPElement.ONE(q));
            p5.set(1, GFPElement.ZERO(q));

            PolynomialMod pol3, pol4, pol5;
            pol3 = new PolynomialMod(true, p3, q);
            pol4 = new PolynomialMod(true, p4, q);
            pol5 = new PolynomialMod(true, p5, q);

            xShift = pol3;
            yShift = pol5;
            pol = pol4;
            parsgenerated = true;
        }
        if (parameter == TATEPAIRING) {
            isTatePairing = true;
            haselimtrue = true;
            clearPairing = true;
        }
        if (parameter == WEILPARING) {
            isTatePairing = false;
            haselimtrue = false;
            clearPairing = true;
        }
        protocol.reset();
        tryagain.reset();
    }

    public void setupStep1() {
        if (situationerrorfilled) {
            situationerrorfilled = false;
        }
        nonces = new Vector<FlexiBigInt>(numberOfUsers);
        nonces.setSize(numberOfUsers);

        sk = new Vector<PrivateKey>(numberOfUsers);
        sk.setSize(numberOfUsers);

        pk = new Vector<PublicKey>(numberOfUsers);
        pk.setSize(numberOfUsers);

        illustration.setStep1Enabled(true);
        protocol.reset();
        tryagain.reset();
        illustration.getCanvas().redraw();

        currentStep = 1;
    }

    public void setupStep2() {
        if (model.isOnBNCurve) {
            algorithmBN = new BDIIBNP(model.numberOfUsers);
        } else {
            algorithm = new ECBDII(model.isTatePairing, model.numberOfUsers, model.q, model.l, model.P, model.a,
                    model.xShift, model.yShift, model.pol, model.haselimtrue, model.keysize);
        }

        // here we had protocol.Step1Visible. We change to illustration.Step1Visible
        startstep0 = System.currentTimeMillis();
        for (int i = 0; i < model.numberOfUsers; i++) {
            try {

                // generate nonce of size 160 : generate 160 bit prime, then add one random number up to 160 bits long.

                model.nonces.set(i, new FlexiBigInt(160, 100, new DefaultPRNG()));
                model.nonces.set(i, model.nonces.get(i).add(new FlexiBigInt(160, new DefaultPRNG())));
                non = non + "U(" + (i + 1) + ")|" + model.nonces.get(i) + "|"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

                // Generate a 1024-bit RSA key pair
                final KeyPairGenerator keyGenA = KeyPairGenerator.getInstance("RSA"); //$NON-NLS-1$
                keyGenA.initialize(1024);
                final KeyPair keypair = keyGenA.genKeyPair();
                model.sk.set(i, keypair.getPrivate());
                model.pk.set(i, keypair.getPublic());
                // AuthPKA = keypair.getPublic();
            } catch (final java.security.NoSuchAlgorithmException e) {
                LogUtil.logError(e);
            }
            stopstep0 = System.currentTimeMillis();
        }

        if (!model.isOnBNCurve) {
            usersData = new Vector<UserData_ECBDII>(model.numberOfUsers);
            usersData.setSize(model.numberOfUsers);

            laststep = 0;
            for (int i = 0; i < model.numberOfUsers; i++) {
                usersData.set(i, new UserData_ECBDII(algorithm, model.sk, model.pk, model.nonces, i + 1));
            }
        } else {
            bnpuData = new Vector<UserData_BNP>(model.numberOfUsers);
            bnpuData.setSize(model.numberOfUsers);

            laststep = 0;
            for (int i = 0; i < model.numberOfUsers; i++) {
                bnpuData.set(i, new UserData_BNP(algorithmBN, model.sk, model.pk, model.nonces, i + 1));
            }
        }

        clearStep0 = true;
        illustration.getCanvas().redraw();
        illustration.setStep2Enabled(true);
        currentStep = 2;
    }

    public void setupStep3() {
        startstep1 = System.currentTimeMillis();
        if (!model.isOnBNCurve) {
            keysstep1 = algorithm.Step1();
        } else {
            keysstep1BN = algorithmBN.Step1();
        }
        stopstep1 = System.currentTimeMillis();
        checkmessage = true;
        for (int i = 0; i < model.numberOfUsers; i++) {
            if (!model.isOnBNCurve) {
                message = "U(" + (i + 1) + ")|" + keysstep1.get(i).PKtoString() + "|" + "1" + "|" + non; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            } else {
                message = "U(" + (i + 1) + ")|" + keysstep1BN.get(i).PrintPubKeys() + "|" + "1" + "|" + non; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            }

            signedmessage = new AuthMessage(message).sign(model.sk.get(i));

            final boolean auth = new AuthMessage(message).verify(model.pk.get(i), signedmessage);
            if (!auth) {
                checkmessage = false;
            }
        }

        if (checkmessage) {
            clearStep1 = true;
            laststep = 1;
            for (int i = 0; i < model.numberOfUsers; i++) {
                if (!model.isOnBNCurve) {
                    usersData.get(i).setDHECKeyPair(keysstep1.get(i));
                } else {
                    bnpuData.get(i).setDHECKeyPair2(keysstep1BN.get(i));
                }
            }

            illustration.getCanvas().redraw();
            protocol.step1ExplVisible(true);
            illustration.setStep3Enabled(true);
        }
        currentStep = 3;
        if (!model.isOnBNCurve) {
            protocol.displayUserDetails(usersData.get(infoUserIndex));
        } else {
            protocol.displayUserDetails(bnpuData.get(infoUserIndex));
        }
        protocol.getGroupDetails().layout();
    }

    public void setupStep4() {
        if (!model.isOnBNCurve) {
            startstep2 = System.currentTimeMillis();
            algorithm.Step2();
            stopstep2 = System.currentTimeMillis();
            keysstep2 = algorithm.GetXpers();
        } else {
            startstep2 = System.currentTimeMillis();
            keysstep2BN = algorithmBN.Step2();
            stopstep2 = System.currentTimeMillis();
        }
        checkmessage = true;
        int end;
        if (!model.isOnBNCurve) {
            end = algorithm.GetParentOf(model.numberOfUsers) - 1;
        } else {
            end = algorithmBN.GetParentOf(model.numberOfUsers) - 1;
        }
        for (int i = 0; i <= end; i++) {
            if (!model.isOnBNCurve) {
                model.message = "U(" + (i + 1) + ")|" + keysstep2.get(4 * i).PrintP() + "|" + "2" + "|" + non; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            } else {
                model.message = "U(" + (i + 1) + ")|" + keysstep2BN.get(4 * i).toString() + "|" + "2" + "|" + non; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            }
            model.signedmessage = new AuthMessage(model.message).sign(model.sk.get(i));
            boolean auth = new AuthMessage(model.message).verify(model.pk.get(i), model.signedmessage);
            if (!auth) {
                checkmessage = false;
            }
            if (4 * i + 6 < model.numberOfUsers - 1) {
                if (!model.isOnBNCurve) {
                    model.message = "U(" + (i + 1) + ")|" + keysstep2.get(4 * i + 2).PrintP() + "|" + "3" + "|" + non; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                } else {
                    model.message = "U(" + (i + 1) + ")|" + keysstep2BN.get(4 * i + 2).toString() + "|" + "3" + "|" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                            + non;
                }
                model.signedmessage = new AuthMessage(model.message).sign(model.sk.get(i));
                auth = new AuthMessage(model.message).verify(model.pk.get(i), model.signedmessage);
                if (!auth) {
                    checkmessage = false;
                }
            }
        }
        if (checkmessage) {
            laststep = 2;
            for (int i = 0; i < model.numberOfUsers - 3; i++) {
                if (!model.isOnBNCurve) {
                    usersData.get(i + 3).setX(keysstep2.get(i));
                } else {
                    bnpuData.get(i + 3).setX(keysstep2BN.get(i));
                }
            }

            clearStep2 = true;
            illustration.getCanvas().redraw();
            protocol.step2ExplVisible(true);
        }
        illustration.setStep4Enabled(true);
        currentStep = 4;
        protocol.getGroupDefinitions().getParent().layout();
        if (!model.isOnBNCurve) {
            protocol.displayUserDetails(usersData.get(infoUserIndex));
        } else {
            protocol.displayUserDetails(bnpuData.get(infoUserIndex));
        }
        protocol.getGroupDetails().layout();
    }

    public void setupStep5() {
        if (!model.isOnBNCurve) {
            startstep3 = System.currentTimeMillis();
            algorithm.Step3();
            stopstep3 = System.currentTimeMillis();
            keysstep3 = algorithm.GetKeys();
        } else {
            startstep3 = System.currentTimeMillis();
            keysstep3BN = algorithmBN.Step3();
            stopstep3 = System.currentTimeMillis();
        }
        clearStep3 = true;
        laststep = 3;
        for (int i = 0; i < model.numberOfUsers; i++) {
            if (!model.isOnBNCurve) {
                usersData.get(i).setKey(keysstep3.get(i));
            } else {
                bnpuData.get(i).setKey(keysstep3BN.get(i));
            }
        }

        illustration.getCanvas().redraw();
        protocol.step3ExplVisible(true);
        illustration.setVerifyEnabled(true);
        currentStep = 5;
        if (!model.isOnBNCurve) {
            protocol.displayUserDetails(usersData.get(infoUserIndex));
        } else {
            protocol.displayUserDetails(bnpuData.get(infoUserIndex));
        }
        protocol.getGroupDetails().layout();
    }

    public void setupStep6() {
        if (!model.isOnBNCurve) {
            keysareequal = algorithm.Verify();
        } else {
            keysareequal = algorithmBN.AllEqual();
        }
        int divide;
        long basictime, addtime, keytime;
        long timepkey;
        addtime = 0;
        timepkey = 0;
        if (!model.isOnBNCurve) {
            divide = algorithm.GetNonLeafs();
            basictime = (model.stopstep3 - model.startstep3) + (model.stopstep1 - model.startstep1)
                    + (model.stopstep0 - model.startstep0);
            addtime = model.stopstep2 - model.startstep2;
            unbduser = basictime / model.numberOfUsers;
            bduser = basictime / model.numberOfUsers + addtime / divide;
        } else {
            divide = algorithmBN.GetNonLeafs();
            basictime = (model.stopstep3 - model.startstep3) + (model.stopstep0 - model.startstep0);
            keytime = model.stopstep1 - model.startstep1;
            timepkey = keytime / (model.numberOfUsers + algorithmBN.GetNUsers2keys());

            addtime = model.stopstep2 - model.startstep2;
            unbduser = basictime / model.numberOfUsers + timepkey;
            bduser = basictime / model.numberOfUsers + addtime / divide + timepkey * 2;
        }
        tryagain.setTimeTo(bduser, unbduser);
        clearverify = true;
        illustration.getCanvas().redraw();
        currentStep = 6;
        if (!model.isOnBNCurve) {
            protocol.displayUserDetails(usersData.get(infoUserIndex));
        } else {
            protocol.displayUserDetails(bnpuData.get(infoUserIndex));
        }
        protocol.getGroupDetails().layout();
    }
}
