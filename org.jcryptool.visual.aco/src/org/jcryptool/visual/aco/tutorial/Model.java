// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.tutorial;

import java.util.Observable;
import java.util.Vector;

import org.jcryptool.visual.aco.antcolony.ACO;
import org.jcryptool.visual.aco.antcolony.Ant;
import org.jcryptool.visual.aco.antcolony.Graph;
import org.jcryptool.visual.aco.transposition.Transposition;

/**
 * Klasse, die die Daten des Tutorials verwaltet und die Operationen des
 * Ameisenalgorithmus kontrolliert.
 *
 * @author Philipp Blohm
 * @version 03.08.07
 *
 */
public class Model extends Observable {

	private int nr;
	private int size;
	private String text;
	private int[] perm;
	private Graph g;
	private Ant a;
	private boolean ani;
	private boolean working;
	private boolean second; // ab zweitem Durchgang
	private boolean enani;
	private boolean all;
	private double best;
	private Vector<Integer> besttrail;
	private PseudoRandomChars randomChars;
	private int antNr = 0;
	private boolean analyse = false;

	/**
	 * Der Konstruktor initialisiert die Variablen und setzt die Ausgangswerte.
	 *
	 */
	public Model() {
		nr = 0;
		size = 4;
		perm = new int[] { 0, 1, 2, 3, 4 };

		text = Messages
				.getString("Model.initial_plaintext").replaceAll(" ", "").toUpperCase(); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		randomChars = new PseudoRandomChars(size - text.length() % size);

		ACO.setAlpha(0.8);
		ACO.setBeta(0.8);
		ani = false;
		working = false;
		second = false;
		enani = true;
		all = false;
		best = 0;

		// String s = encrypt("1234567890abcdef");
		// analyse = true;
		// s = decrypt(s);
	}

	/**
	 * Gibt zurueck ob die Animation zugelassen wird.
	 *
	 * @return true wenn animiert werden soll
	 */
	public boolean getEnAni() {
		return enani;
	}

	/**
	 * Laesst Animation zu oder verhindert Animation.
	 *
	 * @param true wenn Animation zugelassen werden soll
	 */
	public void setEnAni(boolean b) {
		enani = b;
		setNotified();
	}

	/**
	 * Gibt den Wert von ani zurueck.
	 *
	 * @return ani
	 */
	public boolean getAni() {
		return ani;
	}

	/**
	 * Gibt an ob das Tutorial momentan mit der Animation beschaeftigt ist.
	 *
	 * @return working
	 */
	public boolean getWorking() {
		return working;
	}

	/**
	 * Setzt working.
	 *
	 * @param true wenn Tutorial mit Animation beschaeftigt ist.
	 */
	public void setWorking(boolean b) {
		working = b;
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
		return g.getKnots();
	}

	/**
	 * Gibt die Pheromonmatrix zurueck.
	 *
	 * @return m Die Pheromonmatrix
	 */
	public double[][] getMatrix() {
		return g.getMatrix();
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

		if (analyse)
			return s;
		else
			return encrypt(s);
	}

	/**
	 * Gibt die Nummer des aktuellen Knoten zurueck.
	 *
	 * @return i Knotennummer
	 */
	public int getKnot() {
		return a.getLastKnot();
	}

	/**
	 * Gibt an bei welchem Schritt des Tutorials man sich momentan befindet
	 *
	 * @return nr Schrittnummer
	 */
	public int getNr() {
		return nr;
	}

	/**
	 * Gibt den Schluessel der Permutation zurueck.
	 *
	 * @return key Schluessel der Permutation
	 */
	public int[] getPerm() {
		if (perm.length < size) {
			int[] newPerm = new int[size];
			for (int i = 0; i < size; i++) {
				if (i < perm.length) {
					newPerm[i] = perm[i];
				} else {
					newPerm[i] = i;
				}
			}
			perm = newPerm;
		}
		String s = ""; //$NON-NLS-1$
		for (int i = 0; i < size; i++) {
			s = s.concat(perm[i] + ","); //$NON-NLS-1$
		}

		return Transposition.toKey(s);
	}

	/**
	 * Setzt den Schluessel der Permutation
	 *
	 * @param s
	 *            Schluessel
	 */
	public void setPerm(String s) {
		if (s.length() > 0) {
			// aus String Schluessel machen
			int[] q = Transposition.toKey(s);
			if (q != null) {
				// removed due to errors
				// int[] p = new int[5];
				// for(int i=0;i<5;i++){
				// if(i<q.length)
				// p[i]=q[i];
				// else
				// p[i]=i;
				// }
				perm = q;
				setNotified();
			}
		}
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
		setNotified();
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

		if (analyse)
			return decrypt(s);
		else
			return s;
	}

	private String decrypt(String text) {
		String s = text;
		String t = ""; //$NON-NLS-1$
		int[] p = getPerm();

		int[] key = p.clone();
		for (int i = 0; i < p.length; i++) {
			key[Integer.parseInt("" + p[i])] = i;
		}
		p = key;

		int depth = text.length() / size;
		if (text.length() % size != 0)
			depth++;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < depth; j++) {
				t = t.concat(s.charAt(p[i] + j * size) + ""); //$NON-NLS-1$
			}
		}
		return t;
	}

	private String encrypt(String text) {
		String s = text;
		String t = ""; //$NON-NLS-1$
		int[] p = getPerm();
		int depth = text.length() / size;
		if (text.length() % size != 0)
			depth++;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < depth; j++) {
				t = t.concat(s.charAt(p[i] + j * size) + ""); //$NON-NLS-1$
			}
		}
		return t;
	}

	public void setAnalyse(boolean analyse) {
		this.analyse = analyse;
	}

	/**
	 * Gibt zurueck, ob man sich im zweiten oder hoeheren Durchlauf befindet.
	 *
	 * @return second
	 */
	public boolean getSecond() {
		return second;
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
		setNotified();
	}

	/**
	 * Tutorial wird in naechsten Schritt ueberfuehrt
	 *
	 */
	public void inc() {
		if (nr < 4)
			nr++;
		if (nr == 1) {
			g = new Graph(getCipher(), size);
			neuSetzen();
		}
		if (nr == 4)
			second = true;
		if (nr != 3)
			setNotified();
	}

	/**
	 * Eine neue Ameise wird erzeugt und zufaellig auf dem Graph platziert.
	 *
	 */
	public void neuSetzen() {
		a = new Ant(g);
		antNr++;
		a.set();
		if (nr == 4)
			nr = 1;
		setNotified();
	}

	/**
	 * Die Ameise macht eine Schritt auf dem Graphen. Wenn damit der letzte
	 * Knoten erreicht wird, wird das Pheromon abgelegt und ueberprueft, ob es
	 * sich bei der erhaltenen Loesung um die bisher beste handelt.
	 *
	 */
	public void step() {
		ani = true;
		a.step();
		if (a.getTrail().size() == size) { // wenn letzter Knoten erreicht
			a.calcScore();
			a.dropPheromon();
			nr = 3;
			if (a.getScore() >= best) {
				best = a.getScore();
				besttrail = getTrail();
			}
		}
		setNotified();
		ani = false;
	}

	/**
	 * Gibt den Wert von all zurueck (ob alle noch verbliebenen Knoten direkt
	 * hintereinander passiert werden sollen)
	 *
	 * @return all
	 */
	public boolean getAll() {
		return all;
	}

	/**
	 * Fuehrt alle noch notwendigen Schritte durch, damit die Ameise ihren Weg
	 * vollendet.
	 *
	 */
	public void steps() {
		if (a.getTrail().size() == size) { // letzter Knoten erreicht
			all = false;
		} else { // sonst weiterer Schritt
			all = true;
			step();
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
			return g.toText(besttrail);
		return g.toText(getTrail());
	}

	/**
	 * Setzt den Parameter ani auf den uebergebenen Wert
	 *
	 * @param b
	 */
	public void setAni(boolean b) {
		ani = b;
	}

	/**
	 * Gibt den aktuellen Weg der Ameise zurueck.
	 *
	 * @return
	 */
	public Vector<Integer> getTrail() {
		return a.getTrail();
	}

	/**
	 * Gibt die Wahrscheinlichkeiten zurueck mit denen die verbliebenen Knoten
	 * im naechsten Schritt von der Ameise erreicht werden.
	 *
	 * @return
	 */
	public double[] getProbabilities() {
		return a.getProbabilities();
	}

	/**
	 * Setzt den Alpha-Wert auf den uebergebenen.
	 *
	 * @param d
	 *            neuer Alpha-Wert
	 */
	public void setAlpha(double d) {
		ACO.setAlpha(d);
		setNotified();
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
		setNotified();
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
		setNotified();
	}

	/**
	 * Benachrichtigt die Oberserver, dass eine relevante Veraenderung
	 * eingetreten ist.
	 *
	 */
	public void setNotified() {
		setChanged();
		notifyObservers();
	}

	public int getAntNr() {
		return antNr;
	}

	public boolean getAnalyse() {
		return analyse;
	}
}
