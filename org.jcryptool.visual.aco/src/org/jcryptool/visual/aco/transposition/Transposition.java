// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.transposition;

import java.util.Arrays;
import java.util.Vector;

/**
 * Klasse mit der Transpositionen durchgefuehrt werden koennen.
 *
 * @author Philipp Blohm
 * @version 03.08.07
 *
 */
public class Transposition {

	/**
	 * Der uebergebene Text wird mit dem uebergebenen Schluessel verschluesselt.
	 *
	 * @param text
	 *            Klartext
	 * @param key
	 *            Schluessel
	 * @return t Geheimtext
	 */
	public static String encyrpt(String text, int[] key) {
		String[] s = new String[key.length];
		String t = ""; //$NON-NLS-1$
		for (int i = 0; i < key.length; i++) {
			s[i] = ""; //$NON-NLS-1$
		}
		// zufaellige Zeichen anhaengen
		while (text.length() % key.length != 0)
			text = text.concat(Transposition.randomLetter());

		// in Spalten schreiben
		while (text.length() > 0) {
			for (int i = 0; i < key.length; i++) {
				s[i] = s[i].concat(text.substring(0, 1));
				text = text.substring(1);
			}
		}

		// permutiert auslesen
		for (int i = 0; i < key.length; i++) {
			t = t.concat(s[key[i]]);
		}

		return t;
	}

	/**
	 * score Erzeugt einen zufaelligen Buchstaben.
	 *
	 * @return s Zufaelliger Buchstabe
	 */
	private static String randomLetter() {
		int rand = (int) (Math.random() * 25.999999999999) + 97;
		char c = (char) rand;
		return Character.toString(c);
	}

	/**
	 * Wandelt den uebergebenen String in ein int-Array, dass einen Schluessel
	 * darstellt.
	 *
	 * @param s
	 *            Schluessel als Text
	 * @return key Schluessel als int-Array
	 */
	@SuppressWarnings("unchecked")
	public static int[] toKey(String s) {
		// Leerzeichen entfernen
		s = s.replaceAll(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
		int[] key = null;
		boolean weiter = false;
		int[] set = null;
		// Schluessel als Schluesselwort
		if (Character.isLetter(s.charAt(0))) {
			key = new int[s.length()];
			set = new int[key.length];
			for (int i = 0; i < set.length; i++)
				set[i] = 0;
			char[] text = s.toCharArray();
			char[] text2 = text.clone();
			Arrays.sort(text2);

			for (int i = 0; i < s.length(); i++) {
				if (!Character.isLetter(text[i]))
					return null;
				key[i] = Arrays.binarySearch(text2, text[i]);
				set[key[i]]++;
				if (set[key[i]] == 2)
					weiter = true;
			}

		}
		// Schluessel als Zahlenfolge
		else if (Character.isDigit(s.charAt(0))) {
			Vector<Integer> num = new Vector<Integer>();
			while (s.length() > 0) {
				String t;
				if (s.indexOf(",") != -1) { //$NON-NLS-1$
					t = s.substring(0, s.indexOf(",")); //$NON-NLS-1$
					s = s.substring(s.indexOf(",") + 1); //$NON-NLS-1$
				} else {
					t = s;
					s = ""; //$NON-NLS-1$
				}
				try {
					num.add(Integer.parseInt(t));
				} catch (NumberFormatException e) {
					return null;
				}
			}
			key = new int[num.size()];
			Object[] num2 = ((Vector<Integer>) num.clone()).toArray();
			Arrays.sort(num2);
			set = new int[key.length];
			for (int i = 0; i < set.length; i++)
				set[i] = 0;

			for (int i = 0; i < key.length; i++) {
				key[i] = Arrays.binarySearch(num2, num.toArray()[i]);
				set[key[i]]++;
				if (set[key[i]] == 2)
					weiter = true;

			}

		}
		// doppelte eleminieren

		if (weiter) {
			int i = 0;
			while (i < set.length) {
				if (indexOf(key, i, true) < 0) {
					int j = 0;
					while (set[j] < 2)
						j++;
					key[indexOf(key, j, true)] = i;
					set[j]--;
				} else
					i++;
			}
		}

		return key;
	}

	/**
	 * Sucht von vorne oder hinten den ersten Index der uebergebenen Zahl im
	 * uebergebenen Array.
	 *
	 * @param k
	 *            Array
	 * @param j
	 *            Zahl
	 * @param vorne
	 *            von vorne oder hinten
	 * @return i Index
	 */
	private static int indexOf(int[] k, int j, boolean vorne) {
		if (vorne) {
			for (int i = 0; i < k.length; i++)
				if (k[i] == j)
					return i;
		} else {
			for (int i = k.length - 1; i >= 0; i--)
				if (k[i] == j)
					return i;
		}
		return -1;
	}
}
