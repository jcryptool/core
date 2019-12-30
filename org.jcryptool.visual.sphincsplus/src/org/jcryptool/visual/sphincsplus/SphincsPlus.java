// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sphincsplus;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sphincsplus.algorithm.ADRS;
import org.jcryptool.visual.sphincsplus.algorithm.ADRSTypes;
import org.jcryptool.visual.sphincsplus.algorithm.FORS;
import org.jcryptool.visual.sphincsplus.algorithm.HashFunction;
import org.jcryptool.visual.sphincsplus.algorithm.Hypertree;
import org.jcryptool.visual.sphincsplus.algorithm.Key;
import org.jcryptool.visual.sphincsplus.algorithm.SIG_FORS;
import org.jcryptool.visual.sphincsplus.algorithm.SIG_HT;
import org.jcryptool.visual.sphincsplus.algorithm.Utils;
import org.jcryptool.visual.sphincsplus.interfaces.IFORS;
import org.jcryptool.visual.sphincsplus.interfaces.IHypertree;
import org.jcryptool.visual.sphincsplus.interfaces.ISphincsPlus;

public class SphincsPlus implements ISphincsPlus {

    private Key key;
    private HashFunction f;
    private byte[] tempmessage = {};

    public SphincsPlus() {
        if (key == null) {
            init();
        }
    }

    private void init() {
        key = Key.getInstance();
        this.f = HashFunction.getInstance(EnvironmentParameters.function);
    }

    /**
     * See NIST paper 6.2 This function sets the parameters in the Key-Object The secret key
     * consists of: (SKseed, SKprf, PKseed, PKroot) The public key constis of: (PKseed, PKroot)
     * Every one of this keys has n bytes
     */
    @Override
    public Key spx_keygen() {
        SecureRandom random = new SecureRandom();
        Key k = Key.getInstance();
        byte[] SKseed = new byte[EnvironmentParameters.n];
        byte[] SKprf = new byte[EnvironmentParameters.n];
        byte[] PKseed = new byte[EnvironmentParameters.n];
        byte[] PKroot = (new Hypertree()).ht_PKgen(SKseed, PKseed);
        // byte[] PKroot = new byte[EnvironmentParameters.n];
        random.nextBytes(SKseed);
        random.nextBytes(SKprf);
        random.nextBytes(PKseed);
        random.nextBytes(PKroot);
        k.setSkseed(SKseed);
        k.setSkprf(SKprf);
        k.setPkseed(PKseed);
        k.setPkroot(PKroot);
        return k;
    }

    public Key spx_keygen(byte[] seed) {
        SecureRandom random = new SecureRandom(seed);
        Key k = Key.getInstance();
        byte[] SKseed = new byte[EnvironmentParameters.n];
        byte[] SKprf = new byte[EnvironmentParameters.n];
        byte[] PKseed = new byte[EnvironmentParameters.n];
        byte[] PKroot = new byte[EnvironmentParameters.n];
        random.nextBytes(SKseed);
        random.nextBytes(SKprf);
        random.nextBytes(PKseed);
        random.nextBytes(PKroot);
        k.setSkseed(SKseed);
        k.setSkprf(SKprf);
        k.setPkseed(PKseed);
        k.setPkroot(PKroot);
        return k;
    }

    /*
    *//**
        * See NIST 6.4, calculation of md
        * 
        * @param digest H_msg(R, PK.seed, PK.root, M);
        * @return the first k*a bits of tmp_md
        *//*
           * private byte[] get_md(byte[] digest) { byte[] md; int ka = EnvironmentParameters.k *
           * EnvironmentParameters.a; int quotient = ka / 8; // integer division ! byte remainder =
           * (byte) (ka % 8); if (remainder == 0) { md = Arrays.copyOfRange(digest, 0, quotient); }
           * else { md = Arrays.copyOfRange(digest, 0, quotient + 1); md[quotient] =
           * get_first_n_bits(md[quotient], remainder); } return md; }
           */

    @Override
    public byte[] spx_sign(byte[] message) {
        try {
            // int k = EnvironmentParameters.k;
            ADRS adrs = new ADRS();
            IFORS fors = new FORS();
            byte[] opt = Utils.toByte(0, 32);
            if (EnvironmentParameters.RANDOMIZE) {
                SecureRandom r = new SecureRandom();
                // generate 32 byte random if RANDOMIZE is set
                r.nextBytes(opt);
                r = null;
            }
            byte[] R = f.PRFmsg(key.getSkprf(), opt, message);
            byte[] SIG = R;
            // compute message digest
            byte[][] tmp = compute_digest_and_index(R, message);
            byte[] md = tmp[1];
            byte[] idx_tree = tmp[2];
            byte[] idx_leaf = tmp[3];

            // FORS sign
            adrs.setLayerAddress(0);
            adrs.setTreeAddress(idx_tree);
            adrs.setType(ADRSTypes.FORS_TREE);
            adrs.setKeyPairAddress(idx_leaf);
            // TODO inform FORS-programmer to only use the first N bits of md
            SIG_FORS SIG_FORS = fors.fors_sign(md, key.getSkseed(), key.getPkseed(), adrs);
            SIG = Utils.concatenateByteArrays(SIG, SIG_FORS.toByteArray());

            byte[] PK_FORS = fors.fors_pkFromSig(SIG_FORS, message, key.getPkseed(), adrs);
            this.tempmessage = message;

            adrs.setType(ADRSTypes.TREE);
            IHypertree ht = new Hypertree();
            SIG_HT sig_ht = ht.ht_sign(PK_FORS, key.getSkseed(), key.getPkseed(), Utils.byteArrayToInt(idx_tree),
                    Utils.byteArrayToInt(idx_leaf));
            byte[] sig_ht_byte = sig_ht.toByteArray();
            SIG = Utils.concatenateByteArrays(SIG, sig_ht_byte);

            return SIG;

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.logError("bundleID", "sign failed", ex, true);
        }
        return "".getBytes();
    }

    /**
     * See NIST paper 6.4<br>
     * This function calculates the digest from the message and extracts the specified number of
     * bits from the digest.
     * 
     * @param R random value generated by PRF_msg(SK.prf, opt, message)
     * @param message the message to calculate the digest
     * @return a byte[]{digest, md, idx_tree, idx_leaf} where every field is a byte[] itself
     */
    private byte[][] compute_digest_and_index(byte[] R, byte[] message) {
        int h = EnvironmentParameters.h;
        int a = EnvironmentParameters.a;
        int d = EnvironmentParameters.d;
        int k = EnvironmentParameters.k;
        byte[] digest = f.Hmsg(R, key.getPkseed(), key.getPkroot(), message);
        byte[] tmp_md = Arrays.copyOfRange(digest, 0, (int) Math.floor((k * a + 7) / 8));
        byte[] tmp_idx_tree = Arrays.copyOfRange(digest, tmp_md.length,
                tmp_md.length + (int) Math.floor((h - h / d + 7) / 8));
        byte[] tmp_idx_leaf = Arrays.copyOfRange(digest, tmp_idx_tree.length,
                tmp_idx_tree.length + (int) Math.floor((h / d + 7) / 8));
        byte[] md = Utils.get_first_n_bits(tmp_md, k * a);
        byte[] idx_tree = Utils.get_first_n_bits(tmp_idx_tree, h - h / d);
        byte[] idx_leaf = Utils.get_first_n_bits(tmp_idx_leaf, h / d);

        return new byte[][] { digest, md, idx_tree, idx_leaf };
    }

    @Override
    public boolean spx_verify(byte[] message, byte[] SIG) {
        // init R, SIG_FORS and SIG_HT
        ADRS adrs = new ADRS();
        Key key = Key.getInstance();
        int n = EnvironmentParameters.n;
        int k = EnvironmentParameters.k;
        int a = EnvironmentParameters.a;
        byte[] R = new byte[n];
        byte[] SIG_FORS_bytes = new byte[k * (a + 1) * n];
        // same as: (h+d*len)*n
        byte[] sig_ht_bytes = new byte[SIG.length - R.length - SIG_FORS_bytes.length];
        System.arraycopy(SIG, 0, R, 0, n);
        System.arraycopy(SIG, R.length, SIG_FORS_bytes, 0, SIG_FORS_bytes.length);
        System.arraycopy(SIG, R.length + SIG_FORS_bytes.length, sig_ht_bytes, 0, sig_ht_bytes.length);

        // compute message digest and index
        byte[][] tmp = compute_digest_and_index(R, message);
        byte[] idx_tree = tmp[2];
        byte[] idx_leaf = tmp[3];
        // compute FORS public key
        adrs.setLayerAddress(0);
        adrs.setTreeAddress(idx_tree);
        adrs.setType(ADRSTypes.FORS_TREE);
        adrs.setKeyPairAddress(idx_leaf);
        FORS fors = new FORS();
        SIG_FORS sig_fors_object = new SIG_FORS(SIG_FORS_bytes);
        SIG_HT sig_ht_object = new SIG_HT(sig_ht_bytes);
        // get FORS public key
        byte[] PK_FORS = fors.fors_pkFromSig(sig_fors_object, message, key.getPkseed(), adrs);
        adrs.setType(ADRSTypes.FORS_TREE);
        byte[] pknew = {};
        if (Arrays.equals(message, tempmessage)) {
            pknew = fors.fors_pkFromSig(sig_fors_object, this.tempmessage, key.getPkseed(), adrs);
        }
        // verify HT signature
        adrs.setType(ADRSTypes.TREE);
        Hypertree ht = new Hypertree();
        return ht.ht_verify(PK_FORS, sig_ht_object, key.getPkseed(), (new BigInteger(idx_tree)).intValue(),
                (new BigInteger(idx_leaf)).intValue(), key.getPkroot(), pknew);
    }

}