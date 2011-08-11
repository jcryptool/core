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
 * Die Klasse repraesentiert eine Ameise, die in einem Graphen die fuer den
 * Ameisenalgorithmus notwendigen Operationen durchfuehren kann.
 *
 * @author Philipp Blohm
 * @version 03.08.07
 *
 */
public class Ant {

	private Vector<Integer> tabuliste;
	private Vector<Integer> restliste;
	private Graph g;
	private int current;
	private double score;

	/**
	 * Konstruktor. Erzeugt eine neue Ameise.
	 *
	 * @param g
	 *            Der Graph, durch den die Ameise sich einen Weg suchen soll.
	 */
	public Ant(Graph g) {
		this.g = g;
		tabuliste = new Vector<Integer>();
		restliste = new Vector<Integer>();
		current = -1;
		score = 0;
		for (int i = 0; i < g.size; i++)
			restliste.add(i);
	}

	/**
	 * Gibt die Knotennummer zurueck bei der sich die Ameise gerade befindet.
	 *
	 * @return current aktueller Knoten
	 */
	public int getLastKnot() {
		return current;
	}

	/**
	 * Gibt den Weg aus, den die Ameise bis dato zurueckgelegt hat.
	 *
	 * @return tabuliste zurueckgelegter Weg
	 */
	public Vector<Integer> getTrail() {
		return tabuliste;
	}

	/**
	 * Setzt die Ameise zufaellig auf einen der Knoten im Graphen.
	 *
	 */
	public void set() {
		int pos = (int) (Math.random() * g.size - Double.MIN_VALUE);
		tabuliste.add(pos);
		restliste.removeElement(pos);
		current = pos;
	}

	/**
	 * Laesst die Ameise einen Knoten weiter laufen im Graphen. Der Uebergang
	 * erfolgt mit einer Monte-Carlo-Auswahl (Kanten mit hohen Pheromon- und
	 * Prioritaetswerten werden mit groesserer Wahrscheinlichkeit gewaehlt)
	 *
	 */
	public void step() {
		double sum = calcSum();
		double wert = 0;
		double rnd = Math.random(); // Zufallswert
		for (int i : restliste) {
			// Uebergangsgleichung
			wert += (Math.pow(g.pheromon[current][i], ACO.alpha) * Math.pow(
					g.getPrior(current, i), ACO.beta))
					/ sum;
			if (rnd < wert) {
				restliste.removeElement(i);
				tabuliste.addElement(i);
				current = i;
				break;
			}
		}
	}

	/**
	 * Gibt ein Array mit den Wahrscheinlichkeiten zurueck, mit denen die
	 * verbliebenen Knoten angesteuert werden. Da die Methode nur fuer das
	 * Tutorial benoetigt wird, werden lediglich die ersten fuenf Knoten
	 * beruecksichtigt. Knoten, die bereits passiert wurden erhalten eine
	 * Wahrscheinlichkeit von -1.
	 *
	 * @return prob Array mit den Wahrscheinlichkeiten
	 */
	public double[] getProbabilities() { // nur fuer Tutorial
		double[] prob = new double[5];
		double sum = calcSum();
		for (int i = 0; i < 5; i++) {
			if (restliste.contains(i))
				prob[i] = (Math.pow(g.pheromon[current][i], ACO.alpha) * Math
						.pow(g.getPrior(current, i), ACO.beta)) / sum;
			else
				prob[i] = -1;
		}
		return prob;
	}

	/**
	 * Addiert die Werte der Kanten fuer Pheromon und Prioritaetsregel gewichtet
	 * mit den jeweiligen Parametern aus.
	 *
	 * @return sum Die Summe der Werte
	 */
	private double calcSum() {
		double sum = 0;
		for (int i : restliste) {
			sum += Math.pow(g.pheromon[current][i], ACO.alpha)
					* Math.pow(g.getPrior(current, i), ACO.beta);
		}
		return sum;
	}

	/**
	 * Berechnet den Wert der ermittelten Loesung
	 *
	 */
	public void calcScore() {
		score = g.getPhero2(tabuliste);
	}

	/**
	 * Liefert die Bewertung des Weges, den die Ameise zurueckgelegt hat.
	 */
	public double getScore() {
		return score;
	}

	/**
	 * Updated die Pheromonmatrix nachdem die Ameise ihren Weg durch den Graphen
	 * vollendet hat.
	 *
	 */
	public void dropPheromon() {
		for (int i = 0; i < g.size; i++) {
			for (int j = 0; j < g.size; j++) {
				g.pheromon[i][j] *= ACO.evaporation;
			}
		}
		for (int i = 0; i < tabuliste.size() - 1; i++) {
			g.pheromon[tabuliste.elementAt(i)][tabuliste.elementAt(i + 1)] += (1 - ACO.evaporation)
					* score;
		}
	}
}
