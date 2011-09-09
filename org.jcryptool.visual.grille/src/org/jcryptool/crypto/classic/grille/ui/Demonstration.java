// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.grille.ui;

import org.jcryptool.crypto.classic.grille.algorithm.Grille;
import org.jcryptool.crypto.classic.grille.algorithm.KeySchablone;
import org.jcryptool.crypto.classic.grille.algorithm.Schablone;

public class Demonstration {

	private String input;
	private KeySchablone key;
	private int currentStep;
	private Grille model;
	public String padding;
	private Schablone crypt;
	private int plaintextBlockPosition = 0;
	private String output;
	private KeySchablone saved_key;

	public Demonstration(Grille model, String input) {
		this.key = model.getKey();
		this.saved_key = key.clone();
		this.input = input;
		this.model = model;
		crypt = new Schablone(key.getSize());
	}
	public void reset() {
		currentStep = 0;
		plaintextBlockPosition = 0;
		key = saved_key.clone();
		model.setKey(key);
	}

	public void showStep1() {
		currentStep = 1;
		padding = model.check(input);
		padding = padding.substring(input.length());
	}

	public void showStep2() {
		currentStep = 2;
		key.rotateCounterClockwise();
		plaintextBlockPosition = model.encryptAndTurn(input+padding,plaintextBlockPosition,crypt);
	}

	public void showStep3() {
		currentStep = 3;
		plaintextBlockPosition = model.encryptAndTurn(input+padding,plaintextBlockPosition,crypt);
	}

	public void showStep4() {
		currentStep = 4;
		plaintextBlockPosition = model.encryptAndTurn(input+padding,plaintextBlockPosition,crypt);
	}

	public void showStep5() {
		currentStep = 5;
		plaintextBlockPosition = model.encryptAndTurn(input+padding,plaintextBlockPosition,crypt);
	}

	public void showStep6() {
		currentStep = 6;
		if (key.getSize()%2 != 0)
			crypt.set(key.getSize()/2, key.getSize()/2, model.generateRandomChar(input+padding));

		// TODO continue block encryption
		key.rotateClockwise();
		output = model.encrypt(input+padding);
	}

	public String getOutput(){return output;}
	public String getPadding(){return padding;}
	public String getInput(){return input;}
	public Schablone getSchablone(){return crypt;}
	public int getCurrentStep(){return currentStep;}
	public KeySchablone getKey(){return key;}
}
