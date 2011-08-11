// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.antcolony;

import java.util.Vector;

/**
 * Hauptklasse. Erzeugt die GUI, beinhaltet Methoden zum praeparieren des
 * Textes, damit er in der notwendigen Darstellung vorliegt und zum
 * entschlueseln von Geheimtexten mit Hilfe eines Ameisenalgorithmus. Ausserdem
 * werden die Parameter des Algorithmus in dieser Klasse verwaltet.
 *
 * @author Philipp Blohm
 * @version 03.08.07
 *
 */
public class ACO {

	public static double evaporation = 0.9;
	public static double alpha = 1;
	public static double beta = 1;
	public static int population = 10;
	public static int runden = 70;
	public static boolean abbruch = true;
	public static boolean deutsch = true;

	/**
	 * Methode zum Entschluesseln des Geheimtextes. Fuehrt Ameisenalgorithmus
	 * durch, indem entsprechend der eingestellten Population und Generationen
	 * Ameisen erzeugt und durch den Anagramming-Graph geschickt werden. Die
	 * beste Loesung wird gemerkt und ausgegeben.
	 *
	 * @param s
	 *            Geheimtext
	 * @param len
	 *            Schluessellaenge
	 * @return best beste Entschluesselung
	 */
	public static String decodeString(String s, int len) {
		Graph g = new Graph(s, len);
		Vector<Integer> best = new Vector<Integer>();
		double bestscore = 0;
		Ant[] a = new Ant[population];
		// Fuer die angegebene Anzahl an Generationen
		for (int k = 0; k < runden; k++) {
			// Fuer die Anzahl einer Population
			for (int i = 0; i < population; i++) {
				// neue Ameise erzeugen und auf Graph platzieren
				a[i] = new Ant(g);
				a[i].set();
			}
			for (int j = 0; j < population; j++) {
				// durchlaufen lassen
				for (int i = 0; i < g.size - 1; i++) {
					a[j].step();
				}
				a[j].calcScore();
			}
			for (int i = 0; i < population; i++) {
				// beste Loesung suchen
				if (a[i].getScore() >= bestscore) {
					bestscore = a[i].getScore();
					best = a[i].getTrail();
					if (abbruch && a[i].getScore() > 5) { // Abbruch bei
						return g.toText(best); // guter Loesung
					}
				}
				// Pheromon ablegen
				a[i].dropPheromon();
			}
		}
		// beste Entschluesselung zurueckgeben
		return g.toText(best);
	}

	/**
	 * Praepariert den uebergebenen String so, dass er nur kleine Buchstaben von
	 * a bis z enthaelt. Ist notwendig, da die Operationen des
	 * Ameisenalgorithmus auf diese Repraesentation zugeschnitten sind.
	 *
	 * @param s
	 *            zu praeparierender Text
	 * @return t praeparierter Text
	 */
	public static String prepareString(String s) {
		String t = ""; //$NON-NLS-1$
		while (s.length() > 0) {
			if (ACO.isLetter(s.charAt(0))) { // pruefen ob zulaessiger Buchstabe
				t = t.concat(s.charAt(0) + ""); //$NON-NLS-1$
			}
			s = s.substring(1); // eine Stelle weiter im String
		}
		t = t.toLowerCase(); // kleine Buchstaben
		return t;
	}

	/**
	 * Setzt alle notwendigen Parameter des Algorithmus.
	 *
	 * @param pop
	 *            Groesse der Populationen
	 * @param rou
	 *            Anzahl der Generationen
	 * @param eva
	 *            Staerke der Verdunstung
	 * @param alp
	 *            Wert von Alpha
	 * @param bet
	 *            Wert von Beta
	 */
	public static void setParams(int pop, int rou, double eva, double alp,
			double bet) {
		population = pop;
		runden = rou;
		evaporation = eva;
		alpha = alp;
		beta = bet;
	}

	/**
	 * Setzt den Abbruchwert.
	 *
	 * @param b
	 *            neuer Abbruchwert
	 */
	public static void setAbb(boolean b) {
		abbruch = b;
	}

	/**
	 * Setzt den Alpha-Wert.
	 *
	 * @param alph
	 *            Wert von Alpha
	 */
	public static void setAlpha(double alph) {
		alpha = alph;
	}

	/**
	 * Setzt den Beta-Wert.
	 *
	 * @param b
	 *            Wert von Beta
	 */
	public static void setBeta(double b) {
		beta = b;
	}

	/**
	 * Setzt die Verdunstung
	 *
	 * @param d
	 *            Staerke der Verdunstung
	 */
	public static void setVerd(double d) {
		evaporation = d;
	}

	/**
	 * Bringt einen uebergebenen Text in die Form eines anderen uebergebenen
	 * Textes, d.h. dort, wo die Vorlage Grossbuchstaben, Satz- oder
	 * Sonderzeichen hat, erhaelt auch der andere Text diese.
	 *
	 * @param form
	 *            Vorlage
	 * @param text
	 *            zu bearbeitender Text
	 * @return s bearbeiteter Text
	 */
	public static String reconditionString(String form, String text) {
		String s = ""; //$NON-NLS-1$
		char c;
		while (form.length() > 0 || text.length() > 0) { // alle Buchstaben
			if (form.length() > 0) {
				if (ACO.isLetter(form.charAt(0))) { // Buchstabe?
					c = text.charAt(0);
					if (Character.isUpperCase(form.charAt(0))) { // Grossbuchstabe?
						c = Character.toUpperCase(c);
					}
					form = form.substring(1); // weitergehen
					text = text.substring(1);
				} else { // anderes Zeichen
					c = form.charAt(0);
					form = form.substring(1);
				}
			} else { // keine zu beachtenden Zeichen mehr in der Vorlage
				c = text.charAt(0);
				text = text.substring(1);
			}
			s = s.concat(c + ""); // Zeichen anhaengen //$NON-NLS-1$
		}
		return s;
	}

	/**
	 * Prueft ob uebergebener ASCII-Wert einem Buchstaben zwischen A und Z
	 * entsprcht
	 *
	 * @param d
	 *            Wert des Characters
	 * @return true wenn Wert einem Buchstaben entspricht
	 */
	public static boolean isLetter(int d) {
		if ((d < 123 && d > 96) || (d < 91 && d > 64))
			return true;
		return false;
	}
}
