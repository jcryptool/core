//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.arc4.algorithm;

import org.jcryptool.visual.arc4.ARC4Con;

/**
 * This class implements the Spritz algorithm. Most of the functionality is not specific to Spritz 
 * and thereby inherited from the abstract ARC4Algorithm
 * 
 * @author Luca Rupp
 *
 */
public class AlgSpritz extends ARC4Algorithm {

    // the variables of the algorithm
    private int w = 1, k = 0, z = 0;
    
    public AlgSpritz(int w) {
        super();
        this.w = w;
    }

    /**
     * Reset the step to zero; reset all internal variables accordingly
     */
    public void reset() {
        i = 0;
        j = 0;
        // default value for w is three; this is not part of the algorithm and has just been chosen arbitrarily by me
        w = ARC4Con.DEFAULT_W_VALUE;
        currentStep = 0;
        random = new int[ARC4Con.DATAVECTOR_VISUAL_LENGTH];
        enc = new int[ARC4Con.DATAVECTOR_VISUAL_LENGTH];
        finish = false;
        // reset the byte vector
        for (int a = 0; a < ARC4Con.TWO_FIFE_SIX; a++) {
            this.vector[a] = a;
        }
    }
    
    /**
     * Generate pseudorandom numbers according to Spritz specification
     */
    protected void createRandomAndEncrypt(int n) {
        int temp;
        i = (i + w) % ARC4Con.TWO_FIFE_SIX;
        j = (k + this.vector[(j + this.vector[i]) % ARC4Con.TWO_FIFE_SIX]) % ARC4Con.TWO_FIFE_SIX;
        k = (k + i + (this.vector[j])) % ARC4Con.TWO_FIFE_SIX;
        temp = this.vector[i];
        this.vector[i] = this.vector[j];
        this.vector[j] = temp;
        // just to make the next line more readable
        temp = this.vector[(i + this.vector[(z + k) % ARC4Con.TWO_FIFE_SIX]) % ARC4Con.TWO_FIFE_SIX];
        z = this.vector[(j + temp) % ARC4Con.TWO_FIFE_SIX];
        this.random[n] = z;
        this.enc[n] = random[n] ^ plain[n];
        this.currentStep++;
    }
    
    /**
     * Set the parameter w
     * @param num the new value for the parameter w
     */
    public void setW(int num) {
        if ((this.currentStep == 0) && (num % 2 != 0)) {
            this.w = num;
        }
    }
    
    /**
     * 
     * @return w
     */
    public int getW() {
        return w;
    }

}