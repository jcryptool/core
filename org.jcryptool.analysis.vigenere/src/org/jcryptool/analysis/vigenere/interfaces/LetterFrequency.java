/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.interfaces;

import java.util.HashMap;

public class LetterFrequency {
	private final HashMap<Integer, Float> german;
	private final HashMap<Integer, Float> english;

	public LetterFrequency() {
		this.german = fillGerman();
		this.english = fillEnglish();
	}

	private HashMap<Integer, Float> fillGerman() {
		HashMap<Integer, Float> map = new HashMap<Integer, Float>();

		map.put((int) 'a', 6.51f);
		map.put((int) 'b', 1.89f);
		map.put((int) 'c', 3.06f);
		map.put((int) 'd', 5.08f);
		map.put((int) 'e', 17.40f);
		map.put((int) 'f', 1.66f);
		map.put((int) 'g', 3.01f);
		map.put((int) 'h', 4.76f);
		map.put((int) 'i', 7.55f);
		map.put((int) 'j', 0.27f);
		map.put((int) 'k', 1.21f);
		map.put((int) 'l', 3.44f);
		map.put((int) 'm', 2.53f);
		map.put((int) 'n', 9.78f);
		map.put((int) 'o', 2.51f);
		map.put((int) 'p', 0.79f);
		map.put((int) 'q', 0.02f);
		map.put((int) 'r', 7.00f);
		map.put((int) 's', 7.27f);
		map.put((int) 't', 6.15f);
		map.put((int) 'u', 4.35f);
		map.put((int) 'v', 0.67f);
		map.put((int) 'w', 1.89f);
		map.put((int) 'x', 0.03f);
		map.put((int) 'y', 0.04f);
		map.put((int) 'z', 1.13f);

		return map;
	}

	private HashMap<Integer, Float> fillEnglish() {
		HashMap<Integer, Float> map = new HashMap<Integer, Float>();

		map.put((int) 'a', 8.167f);
		map.put((int) 'b', 1.492f);
		map.put((int) 'c', 2.782f);
		map.put((int) 'd', 4.253f);
		map.put((int) 'e', 12.702f);
		map.put((int) 'f', 2.228f);
		map.put((int) 'g', 2.015f);
		map.put((int) 'h', 6.094f);
		map.put((int) 'i', 6.966f);
		map.put((int) 'j', 0.153f);
		map.put((int) 'k', 0.772f);
		map.put((int) 'l', 4.025f);
		map.put((int) 'm', 2.406f);
		map.put((int) 'n', 6.749f);
		map.put((int) 'o', 7.507f);
		map.put((int) 'p', 1.929f);
		map.put((int) 'q', 0.095f);
		map.put((int) 'r', 5.987f);
		map.put((int) 's', 6.327f);
		map.put((int) 't', 9.056f);
		map.put((int) 'u', 2.758f);
		map.put((int) 'v', 0.978f);
		map.put((int) 'w', 2.360f);
		map.put((int) 'x', 0.150f);
		map.put((int) 'y', 1.974f);
		map.put((int) 'z', 0.074f);

		return map;
	}

	public HashMap<Integer, Float> getGerman() {
		return german;
	}

	public HashMap<Integer, Float> getEnglish() {
		return english;
	}
}
