// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer5;

import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.kegver.layer4.L4;

public class L5 {

	/*
	 * Class variable
	 */

	private static L5 SINGLETON = null;

	/*
	 * Class methods
	 */

	public static L5 set(Text inText){
		L5.SINGLETON = new L5(inText);
		return L5.get();
	}

	public static L5 get() {
		if (L5.SINGLETON == null){

		}
		return L5.SINGLETON;
	}

	//TODO: Model View Control implementation
	public static void nextStep(Text grp1_t) {
		L5 singleL5 = L5.get();
		if (singleL5 == null){
			L5.set(grp1_t);
		} else if (! singleL5.getText().equals(grp1_t)){
			throw new IllegalArgumentException("the text you want to print is different");
		}
		L4.startL3();
	}

	/*
	 * Constructor
	 */

	public L5(Text inText) {
		this.setText(inText);
	}

	/*
	 * Instance variables
	 */

	private Text aText = null;

	/*
	 * Getter and setter
	 */

	private Text setText(Text inText) {
		this.aText = inText;
		return this.getText();
	}

	private Text getText() {
		return this.aText;
	}

	private static int i = 0;
	public String printString(String inString) {
		String s = null;
		if(this.aText != null){
			s =
				this.aText.getText() +
				System.getProperty("line.separator") +
				System.getProperty("line.separator") +
				++i + " " + inString;
			this.aText.setText(s);
		} else {
			throw new IllegalArgumentException("aText has not been set yet");
		}
		return s;
	}
}