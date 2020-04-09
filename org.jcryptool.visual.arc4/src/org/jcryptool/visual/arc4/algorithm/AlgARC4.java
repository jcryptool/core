//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
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
 * This class implements the normal ARC4 algorithm (Most details that are not 
 * specific to arc4 are implemented in the ARC4Algorithm class
 * 
 * @author Luca Rupp
 * @author Thorben Groos (switchable keylength)
 */
public class AlgARC4 extends ARC4Algorithm {
    
	/**
	 * Default constructor</br>
	 * Creates a new object with key length 16 and a random key and plain text.
	 */
    public AlgARC4() {
		super();
	}

    /**
     * Constructor that is used when switching between ARC4 and Spritz.</br>
     * It creates an object with the given key and plain text.
     * @param key The key of the new object.
     * @param plain The plaintext of the new object.
     */
	public AlgARC4(int[] key, int[] plain) {
		super(key, plain);
	}



	/**
     * Create pseudorandom numbers and encrypt the plaintext.
     * 
     * @param n number of this createRandomAndEncrypt step (index without the initialization steps
     *            that came before)
     */
    @Override
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
    @Override
	public void reset() {
        i = 0;
        j = 0;
        currentStep = 0;
        random = new int[plain.length];
        enc = new int[plain.length];
        finish = false;
        for (int a = 0; a < ARC4Con.TWO_FIFE_SIX; a++) {
            this.vector[a] = a;
        }
    }

}
