//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.arc4.algorithm;

import java.util.Random;

import org.jcryptool.visual.arc4.ARC4Con;

/**
 * Parent class for ARC4Alg and Spritz, implements everything shared between the different variations of ARC4
 * To implement a new variation of ARC4, one just creates another subclass of this class and overwrites the 
 * initStep, createRandomAndEncrypt and reset methods
 * 
 * @author Luca Rupp
 */
public abstract class ARC4Algorithm {

    // the representation of the s box that is used to do the calculation
    protected int[] vector = new int[ARC4Con.S_BOX_LEN];

    // the key used for encryption
    protected int[] key = new int[ARC4Con.DATAVECTOR_VISUAL_LENGTH];

    // the plaintext that shall be encrypted
    protected int[] plain = new int[ARC4Con.DATAVECTOR_VISUAL_LENGTH];

    // contains the generated pseudo random numbers
    protected int[] random = new int[ARC4Con.DATAVECTOR_VISUAL_LENGTH];

    // contains the ciphertext
    protected int[] enc = new int[ARC4Con.DATAVECTOR_VISUAL_LENGTH];

    // the two internal variables of ARC4 and the step counter;
    protected int i = 0, j = 0, currentStep = 0;

    // weather the algorithm is finished
    protected boolean finish = false;

    /**
     * Constructor for the ARC4Algorithm class
     */
    public ARC4Algorithm() {
        // Fill the byte vector with numbers from 0 to 255
        for (int a = 0; a < ARC4Con.S_BOX_LEN; a++) {
            this.vector[a] = a;
        }
        // initialize key and plaintext with pseudorandom numbers
        randomizeKeyPlain();
    }
    
    /**
     * Initialization step of ARC4; there are 256 of those steps for the algorithm
     * 
     * @param i is the number of the initialization step (0 - 255)
     */
    private void initStep(int i) {
        int temp;
        this.i = i;
        this.j = (j + vector[i] + key[i % (key.length)]) % ARC4Con.TWO_FIFE_SIX;
        temp = vector[i];
        vector[i] = vector[j];
        vector[j] = temp;
        currentStep++;
    }

    /**
     * return a integer array of a certain length filled with random numbers between 0 and 255
     * 
     * @param length the length of the integer array to be returned
     * @return returns a array of pseudorandom integer of length "length"
     */
    private int[] randomize(int length) {
        // As you can see the random bytes used for initialization of key and plaintext are not very
        // random after all as Math.random() is used to generate them
        Random ran = new Random();
        int[] ret = new int[length];
        for (int a = 0; a < ret.length; a++) {
            int ranNum = ran.nextInt(ARC4Con.TWO_FIFE_SIX);
            ret[a] = ranNum;
        }
        return ret;
    }
    
    /**
     * Let the algorithm perform some steps; It controls what sort of step to perform depending on
     * how far the algorithm has already proceeded. First the byte vector is initialized, then the
     * pseudorandom numbers are generated and used to encrypt the plaintext.
     * 
     * @param steps how many steps to perform; may be an arbitrary positive integer; any step after
     *            the algorithm finished execution does not do anything
     */
    public void encrypt(int steps) {
        int todo = steps;
        while (todo > 0) {
            if (this.currentStep == ARC4Con.TWO_FIFE_SIX) {
                this.i = 0;
                this.j = 0;
            }
            // first the initialization steps
            if (this.currentStep < ARC4Con.TWO_FIFE_SIX) {
                initStep(this.currentStep);
            // and then the pseudorandom numbers are generated and the plaintext is encrypted
            } else if (this.currentStep < (ARC4Con.TWO_FIFE_SIX + ARC4Con.DATAVECTOR_VISUAL_LENGTH)) {
                // createRandomAndEncrypt wants the index of the step without initialization
                createRandomAndEncrypt(this.currentStep % ARC4Con.TWO_FIFE_SIX);
            } else {
                this.finish = true;
            }
            todo--;
        }
    }
    
    /**
     * initialize key and plaintext to random values
     */
    public void randomizeKeyPlain() {
        if (this.currentStep == 0) {
            this.key = randomize(key.length);
            this.plain = randomize(plain.length);
        }
    }
    
    protected abstract void createRandomAndEncrypt(int n);

    /**
     * Set the plaintext the algorithm will encrypt. One can not change the plaintext after the
     * algorithm started execution
     * 
     * @param plain the plaintext the user wants to encrypt in form of an array of integers
     */
    public void setPlain(int[] plain) {
        // to prevent changing the plaintext after starting the algorithm
        if (this.currentStep == 0) {
            this.plain = plain;
        }
    }

    /**
     * Set the key which will be used to encrypt. One can not change the key after the algorithm
     * started execution
     * 
     * @param key the key in form of an array of integers
     */
    public void setKey(int[] key) {
        // to prevent changing the key after starting the algorithm
        if (this.currentStep == 0) {
            this.key = key;
        }
    }

    /**
     * reset the step counter to zero and change all other variables and the s box accordingly
     */
    public abstract void reset();

    // From here on just some getters

    /**
     * weather the algorithm is finished
     * 
     * @return true, if the algorithm finished execution and false if it is not finished yet
     */
    public boolean getFinish() {
        return this.finish;
    }

    /**
     * return the algorithms s box
     * 
     * @return returns an array of integer that represents the s box
     */
    public int[] getVector() {
        return this.vector;
    }

    /**
     * return the ciphertext
     * 
     * @return returns an array of integers that represents the ciphertext
     */
    public int[] getEnc() {
        return this.enc;
    }

    /**
     * return the pseudorandom numbers
     * 
     * @return returns an array of integers that represents the pseudorandom numbers generated by
     *         the algorithm
     */
    public int[] getRandom() {
        return this.random;
    }

    /**
     * return the internal variable of the algorithm i
     * 
     * @return returns a integer: the internal variable i
     */
    public int getI() {
        return this.i;
    }

    /**
     * return the internal variable of the algorithm j
     * 
     * @return returns an integer, the internal variable j
     */
    public int getJ() {
        return this.j;
    }

    /**
     * return the value of the step counter
     * 
     * @return returns the value of the step counter: an integer
     */
    public int getStep() {
        return this.currentStep;
    }

    /**
     * returns an array of integers that represent the key
     * 
     * @return returns an array of integers that represent the key
     */
    public int[] getKey() {
        return this.key;
    }

    /**
     * returns an array of integers that represent the plaintext
     * 
     * @return returns an array of integers that represent the plaintext
     */
    public int[] getPlain() {
        return this.plain;
    }

}