//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.modern.stream.lfsr.algorithm;

/**
 * The LfsrKeyStreamGenerator class represents the keystream generator of the Lfsr algorithm class.
 * @see org.jcryptool.crypto.modern.stream.lfsr.algorithm.LfsrAlgorithm
 *
 * It provides Lfsr-based keystream generation on a binary basis.
 *
 * @author Tahir Kacak, Justin Kelly
 * @version 0.1
 */
public class LfsrKeyStreamGenerator {

    private boolean [] state;
    private boolean [] tap;

    public LfsrKeyStreamGenerator(boolean[] seed, boolean[] tapSettingsInput){
        // check if seed and tap are same length
        if (seed.length != tapSettingsInput.length)
            throw new Error("seed and tap length must be equal"); //$NON-NLS-1$

        // check if final bit in tap input is true
        if (!tapSettingsInput[tapSettingsInput.length - 1])
            throw new Error("final bit of tap must be true"); //$NON-NLS-1$

        state = seed;
        tap = tapSettingsInput;
    }

    /**
     * Updates the cipher's internal state, and produces 1 bit of keystream output.
     *
     * @return a bit of keystream
     */
    public boolean doRound(){
        boolean returnBit = getFinalBit();
        shift();
        return returnBit;
    }

    // transforms and shift bits into their new positions
    private void shift(){
        boolean newFirstBit = false;

        // applies xor operation against tapped bits
        for (int i = 0; i < state.length; i++){
            if (tap[i] && state[i])
                newFirstBit = !newFirstBit;
        }

        // shifts bits into their new positions
        for (int i = state.length - 1; i > 0; i--) {
            state[i] = state[i - 1];
        }

        state[0] = newFirstBit;
    }

    private boolean getFinalBit(){
        return state[state.length - 1];
    }
}
