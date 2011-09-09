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

import org.jcryptool.analysis.entropy.ui.EntropyUI;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;

/**
 * @author Matthias Mensch
 */
public class EntropyCalc {

	public static final int MATRIXCOLUMS = 10;

	private EntropyUI entropyUIpointer;

	/**
	 * Eine Instanz der Klasse EntropyData welche die Daten bereit haelt und diese analysiert.
	 */
	private EntropyData myData;

	private double[][] resultMatrix;

	/**
	 * Obergrenze für die zugrundegelegte Tupellänge.
	 */
	private int n;

	/**
	 * Hilfsvariable. Hier wird gespeichert, bei welcher Suchtiefe das Abbruchkriterium zutraf.
	 */
	private int actualN;

	private double signiveau;

	private String inputText;
	private String editorname;
	private TransformData myModifySettings;

	public int getActualN() {
		return actualN;
	}

	public int getN() {
		return n;
	}

	public EntropyData getMyData() {
		return myData;
	}

	public double[][] getResultMatrix() {
		return resultMatrix;
	}

	public double getSigniveau() {
		return signiveau;
	}

	public EntropyUI getEntropyUIpointer() {
		return entropyUIpointer;
	}

	public String getEditorname(){
		return editorname;
	}

	public void setEditorname(String s){
		editorname = s;
	}

	public EntropyCalc(String source, int depth, double significance, TransformData modifySettings, EntropyUI pointer, String textname) {
		entropyUIpointer = pointer;
		inputText = source;
		n = depth;
		signiveau = significance;
		myModifySettings = modifySettings;
		editorname =textname;
	}

	public void startCalculator() {
		myData = new EntropyData(inputText, myModifySettings);
		actualN = 1;
		calcResultMatrix(n);
	}

	/**
	 * erzeugt ein zweidimensionales Array aus double Werten, wobei jede Zeile durch das
	 * Ergebnisarray von EntropyData.calcEntropy(n) bestimmt wird. Die Anzahl der Zeilen wird durch
	 * das Abbruchkriterium bestimmt: entweder wird die eingegebene Suchtiefe erreicht, oder der
	 * Zuwachs von G_n-1 auf G_n unterschreitet das Signifikanznivaeu.
	 * @param depth Suchtiefe
	 */
	private void calcResultMatrix(int depth) {
		// Matrix initalisieren:
		resultMatrix = new double[depth][MATRIXCOLUMS];

		for (int i = 0; i < depth; i++) {
			// entropyUIpointer.getCompositeConfig().printStatus("Calculating Entropy with n = "+(i+1));
			// Aufruf der Entropieberechnung unter Beachtung von (i+1)-Tupeln:
			resultMatrix[i] = myData.calcEntropy(i + 1);
			if (i == (depth - 1)) {
				actualN = depth;
			}
			if (resultMatrix[i][9] <= signiveau && i > 1) {
				actualN = i;
				break;
			}

		}
	}

	/**
	 * rundet den Wert d auf l-viele Nachkommastellen.
	 * @param d zu rundender double Wert,
	 * @param l Anzahl der Nachkommastellen
	 * @return double Wert, auf l Nachkomaastellen gerundeter Wert von d
	 */
	public static double roundDouble(double d, int l) {
		double result = d * Math.pow(10.0, (double) l);
		result = Math.rint(result);
		result = result / Math.pow(10.0, (double) l);
		return result;
	}

}
