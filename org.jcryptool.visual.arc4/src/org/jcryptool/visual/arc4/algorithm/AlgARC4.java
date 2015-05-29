package org.jcryptool.visual.arc4.algorithm;

import org.jcryptool.visual.arc4.ARC4Con;

/**
 * This class implements the normal ARC4 algorithm (Most details that are not 
 * specific to arc4 are implemented in the ARC4Algorithm class
 * 
 * @author Luca Rupp
 */
public class AlgARC4 extends ARC4Algorithm {
    
    /**
     * Create pseudorandom numbers and encrypt the plaintext
     * 
     * @param n number of this createRandomAndEncrypt step (index without the initialization steps
     *            that came before)
     */
    protected void createRandomAndEncrypt(int n) {
        int temp;
        this.i = (i + 1) % ARC4Con.TWO_FIFE_SIX;
        this.j = (j + vector[i]) % ARC4Con.TWO_FIFE_SIX;
        temp = vector[i];
        vector[i] = vector[j];
        vector[j] = temp;
        this.random[n] = vector[(vector[i] + vector[j]) % ARC4Con.TWO_FIFE_SIX];
        this.enc[n] = random[n] ^ plain[n];
        this.currentStep++;
    }

    /**
     * Reset all internal variables
     */
    public void reset() {
        i = 0;
        j = 0;
        currentStep = 0;
        random = new int[ARC4Con.DATAVECTOR_VISUAL_LENGTH];
        enc = new int[ARC4Con.DATAVECTOR_VISUAL_LENGTH];
        finish = false;
        for (int a = 0; a < ARC4Con.TWO_FIFE_SIX; a++) {
            this.vector[a] = a;
        }
    }

}
