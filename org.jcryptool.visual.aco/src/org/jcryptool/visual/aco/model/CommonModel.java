// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.model;

import java.util.Vector;

/**
 * Klasse, die die Daten des Tutorials verwaltet und die Operationen des
 * Ameisenalgorithmus kontrolliert.
 * 
 * @author Philipp Blohm
 * @version 03.08.07
 * 
 */
public class CommonModel {

	private int state;
	private int size;
	private String text;
	private GraphModel graph;
	private AntModel ant;
	private boolean isAnimateable;
	private boolean displayGraph = true;
	private double best;
	private Vector<Integer> besttrail;
	private PseudoRandomChars randomChars;
	private int antNr = 0;
	private String language = Messages.Model_defaultLanguage;
	private boolean isSingleStepSelected = true;

	/**
	 * Der Konstruktor initialisiert die Variablen und setzt die Ausgangswerte.
	 * 
	 */
	public CommonModel() {

		size = 4;
		text = "";// Messages.Model_initial_plaintext
					//.replaceAll(" ", "").toUpperCase(); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		randomChars = new PseudoRandomChars(size - text.length() % size);

		graph = new GraphModel(getCipher(), size, language);
		this.reset();
	}

	/**
	 * Gibt zurueck ob die Animation zugelassen wird.
	 * 
	 * @return true wenn animiert werden soll
	 */
	public boolean isAnimateable() {
		return isAnimateable;
	}

	/**
	 * Laesst Animation zu oder verhindert Animation.
	 * 
	 * @param true wenn Animation zugelassen werden soll
	 */
	public void setAnimateable(boolean b) {
		isAnimateable = b;
	}


	/**
	 * Gibt den Wert der bisher besten Loesung zurueck.
	 * 
	 * @return best
	 */
	public double getBest() {
		return best;
	}

	/**
	 * Gibt den bisher besten Weg durch den Graphen aus.
	 * 
	 * @return besttrail Der bisher beste Weg
	 */
	public Vector<Integer> getBestTrail() {
		return besttrail;
	}

	/**
	 * Gibt die Knoten zurueck.
	 * 
	 * @return s String-Array des Graphen
	 */
	public String[] getKnots() {
		return graph.getKnots();
	}

	/**
	 * Gibt die Pheromonmatrix zurueck.
	 * 
	 * @return m Die Pheromonmatrix
	 */
	public double[][] getMatrix() {
		return graph.getMatrix();
	}

	/**
	 * Erzeugt den Geheimtext aus dem Klartext und gibt ihn zurueck.
	 * 
	 * @return t Geheimtext
	 */
	public String getCipher() {
		String s = text;
		while (s.length() % size != 0)
			s = s.concat(Character.toString((randomChars.getRandomChar())));

		return encrypt(s);
	}

	/**
	 * Gibt die Nummer des aktuellen Knoten zurueck.
	 * 
	 * @return i Knotennummer
	 */
	public int getKnot() {
		if (ant == null) {
			return 0;
		}
		return ant.getLastKnot();
	}

	/**
	 * Gibt an bei welchem Schritt des Tutorials man sich momentan befindet
	 * 
	 * @return nr Schrittnummer
	 */
	public int getState() {
		return state;
	}

	public void setState(int nr) {
		this.state = nr;
	}

	/**
	 * Setzt den Klartext.
	 * 
	 * @param t
	 *            Klartext
	 */
	public void setText(String t) {
		text = ""; //$NON-NLS-1$
		for (int i = 0; i < t.length(); i++) {
			if (Character.isLetter(t.charAt(i)))
				if ((Character.toLowerCase(t.charAt(i)) != '\u00e4')
						&& (Character.toLowerCase(t.charAt(i)) != '\u00f6')
						&& (Character.toLowerCase(t.charAt(i)) != '\u00fc')
						&& (Character.toLowerCase(t.charAt(i)) != '\u00df'))
					text = text.concat(Character.toUpperCase(t.charAt(i)) + ""); //$NON-NLS-1$
		}
		randomChars = new PseudoRandomChars(size - text.length() % size);
		graph = new GraphModel(getCipher(), size, language);
	}

	/**
	 * Gibt den Klartext zurueck.
	 * 
	 * @return text Klartext
	 */
	public String getText() {
		String s = text;
		while (s.length() % size != 0)
			s = s.concat(Character.toString((randomChars.getRandomChar())));

		return s;
	}


	private String encrypt(String text) {
		String s = text;
		String t = ""; //$NON-NLS-1$
		int depth = text.length() / size;
		if (text.length() % size != 0)
			depth++;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < depth; j++) {
				t = t.concat(s.charAt(i + j * size) + ""); //$NON-NLS-1$
			}
		}
		return t;
	}

	/**
	 * Gibt die Anzahl der Spalten der Verschluesselung zurueck.
	 * 
	 * @return size Anzahl der Spalten
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Setzt die Anzahl der Spalten auf den uebergebenen Wert.
	 * 
	 * @param s
	 *            neue Anzahl der Spalten
	 */
	public void setSize(int s) {
		size = s;
		randomChars = new PseudoRandomChars(size - text.length() % size);
		graph = new GraphModel(getCipher(), size, language);
	}

	/**
	 * Tutorial wird in naechsten Schritt ueberfuehrt
	 * 
	 */
	public void startAnalyse() {
		state = 1;
		replaceAnt();
	}

	/**
	 * Eine neue Ameise wird erzeugt und zufaellig auf dem Graph platziert.
	 * 
	 */
	public void replaceAnt() {
		ant = new AntModel(graph);
		antNr++;
		ant.set();
		state = 1;
	}

	public void initGraph() {
		graph = new GraphModel(getCipher(), size, language);
		replaceAnt();
	}

	public void setLanguage(String lang) {
		language = lang;
	}

	/**
	 * Die Ameise macht eine Schritt auf dem Graphen. Wenn damit der letzte
	 * Knoten erreicht wird, wird das Pheromon abgelegt und ueberprueft, ob es
	 * sich bei der erhaltenen Loesung um die bisher beste handelt.
	 * 
	 */
	public void toNextKnot() {
		ant.step();
		state = 2;
		if (ant.getTrail().size() == size) { // wenn letzter Knoten erreicht
			ant.calcScore();
			ant.dropPheromon();
			state = 3;
			if (ant.getScore() >= best) {
				best = ant.getScore();
				besttrail = getTrail();
			}
		}
	}

	/**
	 * Erzeugt zum aktuellen oder bisher besten Weg die zugehoerige
	 * Entschluesselung.
	 * 
	 * @param best
	 *            true wenn der beste Weg genommen werden soll
	 * @return text Klartext
	 */
	public String toText(boolean best) {
		if (best)
			return graph.toText(besttrail);
		return graph.toText(getTrail());
	}

	/**
	 * Gibt den aktuellen Weg der Ameise zurueck.
	 * 
	 * @return
	 */
	public Vector<Integer> getTrail() {
		return ant.getTrail();
	}

	/**
	 * Gibt die Wahrscheinlichkeiten zurueck mit denen die verbliebenen Knoten
	 * im naechsten Schritt von der Ameise erreicht werden.
	 * 
	 * @return
	 */
	public double[] getProbabilities() {
		return ant.getProbabilities();
	}

	/**
	 * Setzt den Alpha-Wert auf den uebergebenen.
	 * 
	 * @param d
	 *            neuer Alpha-Wert
	 */
	public void setAlpha(double d) {
		ACO.setAlpha(d);
	}

	/**
	 * Gibt den aktuellen Wert von Alpha zurueck.
	 * 
	 * @return alpha
	 */
	public double getAlpha() {
		return ACO.alpha;
	}

	/**
	 * Setzt den Beta-Wert auf den uebergebenen.
	 * 
	 * @param d
	 *            neuer Beta-Wert
	 */
	public void setBeta(double d) {
		ACO.setBeta(d);
	}

	/**
	 * Gibt den aktuellen Beta-Wert zurueck.
	 * 
	 * @return beta
	 */
	public double getBeta() {
		return ACO.beta;
	}

	/**
	 * Gibt den aktuellen Verdunstungswert zurueck.
	 * 
	 * @return verd
	 */
	public double getVerd() {
		return ACO.evaporation;
	}

	/**
	 * Setzt den Verdunstungswert auf den uebergebenen.
	 * 
	 * @param d
	 *            neuer Verdunstungswert
	 */
	public void setVerd(double d) {
		ACO.setVerd(d);
	}

	public int getAntNr() {
		return antNr;
	}

	public void reset() {
		state = 0;
		ACO.setAlpha(0.8);
		ACO.setBeta(0.8);
		isAnimateable = true;
		isSingleStepSelected = true;
		best = 0;
		ant = new AntModel(graph);
		antNr = 0;
	}

	public boolean isVisualizable() {
		return (size <= 5 && size > 2);
	}

	public boolean isSingleStep() {
		return isSingleStepSelected;
	}

	public void setSingleStep(boolean s) {
		isSingleStepSelected = s;
	}

	public boolean isGraphDisplayed() {
		return displayGraph;
	}

	public void setDisplayGraph(boolean s) {
		displayGraph = s;
	}
	public boolean isAtLastKnot() {
		return ant.getTrail().size() == size;
	}
}
