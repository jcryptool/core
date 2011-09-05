// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.entropy.calc;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Matthias Mensch
 */
public class EntropyTupelFreq {

	/**
	 * ein Array aller vorkommenden einzelnen Zeichentupel absteigend sortiert nach deren Haufigkeit
	 */
	private String[] tupels;

	/**
	 * die Haeufigkeiten der in tupels gespeicherten Zeichentupel. Dabei gehoert die an i-ter Stelle
	 * gespeicherte Haeufigkeit zum Tupel an i-ter Stelle in tupels.
	 */
	private double[] tupelVals;

	/**
	 * Laenge der in tupels gespeicherten Zeichentupel
	 */
	private int tupelLength;

	/**
	 * Standardkonstruktor. Entnimmt die in relativeFrequency gespeicherten Tupel der angeforderten
	 * Laenge und speichert diese in den entsprechenden Arrays. Anschlissend werden diese Arrays
	 * sortiert.
	 * @param arrayLength Anzahl der unterschiedlichen n-Tupeln. Entsprechend dieser Groesse werden
	 *        die Arrays deklariert.
	 * @param tupelLen Laenge n der zu speichernden Tupel.
	 * @param relativeFrequency Zeiger auf die Hashtable welche Informationen ueber die relative
	 *        Hauefigkeit von Tupeln enthaelt.
	 */
	public EntropyTupelFreq(int arrayLength, int tupelLen, Hashtable<String, Double> relativeFrequency) {
		this.tupelLength = tupelLen;
		// Arrays initialisieren:
		tupels = new String[arrayLength];
		tupelVals = new double[arrayLength];

		// Arrays mit entsprechenden Werten fuellen:
		setLetterArrays(relativeFrequency);

		// beide Arrays nach Haeufigkeit der tupel sortieren:
		bubblesortArrays();
	}

	/**
	 * GetMethode
	 * @return Laenge der in der Tabelle hinterlegten Tupel
	 */
	public int getTupelLength() {
		return tupelLength;
	}

	/**
	 * GetMethode
	 * @return liefert das Array welches die Tupel sortiert abgespeichert hat.
	 */
	public String[] getTupels() {
		return tupels;
	}

	/**
	 * GetMethode
	 * @return liefert das zu tupels gehoerige Array mit den Haeufigkeiten. Die Werte sind
	 *         entsprechend der Sortierung von tupels ebenfalls absteigend nach Haeufigkeit
	 *         sortiert.
	 */
	public double[] getTupelVals() {
		return tupelVals;
	}

	/**
	 * generiert anhand der Daten der Hashtable ein Array aller einzelnen Zeichen (beim
	 * Standardfilter also 26 Zeichen) und sortiert diese Zeichen absteigend nach deren Haeufigkeit
	 * (Stirng[] letters). Das double[] letterVals enthaelt entsprechende Haeufigkeiten der
	 * Buchstaben. Dabei gehoert die an i-ter Stelle gespeicherten Haeufigkeit zum Buchstaben an
	 * i-ter Stelle in letters.
	 * @param relativeFrequency Zeiger auf die Hashtabel die Informationen ueber die relative
	 *        Haufigkeit bereithaelt.
	 */
	private void setLetterArrays(Hashtable<String, Double> relativeFrequency) {

		String ntupel;
		int counter = 0;

		// letters und letterVals mit den entsprechenden Werten fuellen:
		Enumeration<String> e = relativeFrequency.keys();
		while (e.hasMoreElements()) {
			// waehle das naechste Element:
			ntupel = e.nextElement().toString();
			// beachte nur Tupel der Laenge n:
			if (ntupel.length() == tupelLength) {
				tupels[counter] = ntupel;
				tupelVals[counter] = Double.parseDouble(relativeFrequency.get(ntupel).toString());
				counter++;
			}
		}
	}

	/**
	 * Implementierung von BubbleSort. Sortiert die Arrays tupels und tupelVals absteigend nach der
	 * Haeufigkeit der Tupel.
	 */
	private void bubblesortArrays() {
		for (int pass = 1; pass < tupelVals.length; pass++) {
			for (int element = 0; element < tupelVals.length - 1; element++) {
				if (tupelVals[element] < tupelVals[element + 1]) {
					swap(tupels, element, element + 1);
					swap(tupelVals, element, element + 1);
				}
			}
		}
	}

	/**
	 * Hilfsmethode. Vertauscht im String[] das Element an Stelle first mit dem an Stelle second.
	 * @param Str String Array indem Elemente vertauscht werden sollen.
	 * @param first Position des ersten der beiden zu vertauschenden Elemente.
	 * @param second Position Zweites der beiden zu vertauschenden Elemente.
	 */
	public void swap(String[] Str, int first, int second) {
		String hold;
		hold = Str[first];
		Str[first] = Str[second];
		Str[second] = hold;
	}

	/**
	 * Vertauscht im double[] das Element an Stelle first mit dem an Stelle second.
	 * @param doub double Array indem zwei Werte vertauscht werden sollen.
	 * @param first Position des ersten Element welches getauscht werden soll.
	 * @param second Position zweites Element, welches mit dem Ersten getauscht wird.
	 */
	public void swap(double[] doub, int first, int second) {
		double hold;
		hold = doub[first];
		doub[first] = doub[second];
		doub[second] = hold;
	}

}
