// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sphincsplus.interfaces;

import org.jcryptool.visual.sphincsplus.algorithm.ADRS;
import org.jcryptool.visual.sphincsplus.algorithm.SIG_FORS;

/**
 * <b>FORS: Forest of Random Subsets</b> The SPHINCS+ algorithm uses FORS to sign the message
 * 
 * The Hypertree signature signs the FORS public key.
 * 
 * See NIST paper 5
 * 
 * @author Lukas Stelzer
 *
 */
public interface IFORS {

    /**
     * See NIST paper 5.2
     * 
     * Computing a FORS private key value
     * 
     * @param secret Seed skseed
     * @param address adrs
     * @param secret key index idx = i*k+j
     * @return FORS private key sk
     */
    public byte[] fors_SKgen(byte[] skseed, ADRS adrs, int idx);

    /**
     * See NIST paper 5.3
     * 
     * The fors_treehash algorithm - see NIST paper Algorithm 15.
     * 
     * @param secret seed skseed
     * @param start index s
     * @param target node height z
     * @param public seed pkseed
     * @param address adrs
     * @return n-byte root node - top node on Stack
     */
    public byte[] fors_treehash(byte[] skseed, int s, int z, byte[] PKseed, ADRS adrs);

    /**
     * See NIST paper 5.4
     * 
     * Generate a FORS public key.
     * 
     * @param secret seed skseed
     * @param public seed pkseed
     * @param address adrs
     * @return FORS public key PK
     */
    //public byte[] fors_PKgen(byte[] skseed, byte[] pkseed, ADRS adrs);

    /**
     * See NIST paper 5.5
     * 
     * Generating a FORS signature on byte[] message.
     * 
     * @param Bit string message
     * @param secret seed skseed
     * @param public seed pkseed
     * @param address adrs
     * @return FORS signature SIG_FORS
     */
    public SIG_FORS fors_sign(byte[] message, byte[] skseed, byte[] pkseed, ADRS adrs);

    /**
     * See NIST paper 5.6
     * 
     * Compute a FORS public key from a FORS signature.
     * 
     * @param FORS signature SIG_FORS
     * @param (k lg t)-bit string message
     * @param public seed pkseed
     * @param address adrs
     * @return FORS public key
     */
    public byte[] fors_pkFromSig(SIG_FORS sig_fors, byte[] message, byte[] pkseed, ADRS adrs);

}
