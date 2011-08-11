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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.aco.ACOPlugin;

/**
 * Die Klasse repraesentiert den Anagramming-Graph, der die Grundlage fuer die
 * Entschluesselung mit Hilfe des Ameisenalgorithmus darstellt.
 *
 * @author Philipp Blohm
 * @version 03.08.07
 *
 */
public class Graph {

	protected double[][] pheromon; // Pheromonmarkierungen
	protected int size; // Laenge Schluessel
	private double[][] bigrams; // Bigrammhaeufigkeiten
	private String[] text; // Spalten
	private Vector<Vector<Integer>> wege;
	private Vector<Double> punkte;
	private Vector<String> wortliste;

	/**
	 * Konstruktor. Erstellt Graph auf der Grundlage des uebergebenen Textes und
	 * der uebergebenen Anzahl der Knoten.
	 *
	 * @param s
	 *            Text
	 * @param l
	 *            Anzahl der Knoten (= Schluessellaenge)
	 */
	public Graph(final String s, final int l) {
		this.size = l;
		this.getBigrams();
		this.makeKanten();
		this.readText(s, l);
		this.wege = new Vector<Vector<Integer>>();
		this.punkte = new Vector<Double>();
		this.readWortliste();
	}

	/**
	 * Liest die Wortliste ein.
	 *
	 */
	private void readWortliste() {
		this.wortliste = new Vector<String>();
		try {
			String liste;
			liste = this
					.loadTextResource(
							"material", Messages.getString("Graph.language") + "/wordlist.txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final StringTokenizer stok = new StringTokenizer(liste);
			String token = stok.nextToken(); // in einzelne Woerter trennen
			while (stok.hasMoreTokens()) {
				if (token.length() > 2) {
					this.wortliste.add(token); // zur Liste hinzufuegen
				}
				token = stok.nextToken();
			}
		} catch (final IOException e) {
			LogUtil.logError(ACOPlugin.PLUGIN_ID,
					Messages.getString("Graph.noWordlist"), e, true); //$NON-NLS-1$
		}
	}

	/**
	 * Methode zum Einlesen der uebergebenen Ressource-Datei.
	 *
	 * @param pkgname
	 *            package-Name der Datei
	 * @param fname
	 *            Dateiname
	 * @return s ausgelesenen Text
	 * @throws IOException
	 *             e Wenn Datei nicht gefunden werden konnte
	 */
	public String loadTextResource(final String pkgname, final String fname)
			throws IOException {
		String ret = null;
		final InputStream is = this.getResourceStream(pkgname, fname);
		if (is != null) {
			final StringBuffer sb = new StringBuffer();
			while (true) {
				final int c = is.read();
				if (c == -1) {
					break;
				}
				sb.append((char) c);
			}
			is.close();
			ret = sb.toString();
		}
		return ret;
	}

	/**
	 * Laedt die uuebergebene Datei in einen InputStream und liefert diesen
	 * zurueck.
	 *
	 * @param pkgname
	 *            package-Name
	 * @param fname
	 *            Dateiname
	 * @return is Der InputStream mit der Datei.
	 */
	private InputStream getResourceStream(final String pkgname,
			final String fname) {
		final String resname = "/" + pkgname.replace('.', '/') + "/" + fname; //$NON-NLS-1$ //$NON-NLS-2$
		final Class<? extends Graph> cla = this.getClass();
		final InputStream is = cla.getResourceAsStream(resname);
		return is;
	}

	/**
	 * Liest das Array mit den relativen Bigrammwahrscheinlichkeiten ein.
	 *
	 */
	public void getBigrams() {
		String name;
		name = "/material/" + Messages.getString("Graph.language") + "/bigrams.dat"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		try {
			final InputStream fis = this.getClass().getResourceAsStream(name);
			final ObjectInputStream ois = new ObjectInputStream(fis);
			this.bigrams = (double[][]) ois.readObject();
			ois.close();
			fis.close();
		} catch (final IOException ex) {
			LogUtil.logError(ACOPlugin.PLUGIN_ID, ex);
		} catch (final ClassNotFoundException ex) {
			LogUtil.logError(ACOPlugin.PLUGIN_ID, ex);
		}
	}

	/**
	 * Praepariert den Text und ruft fillG zum fuellen des Graphen auf
	 *
	 * @param s
	 *            Text
	 * @param l
	 *            Anzahl der Knoten
	 */
	private void readText(String s, final int l) {
		String t = ""; //$NON-NLS-1$
		while (s.length() > 0) {
			final char c = s.charAt(0);
			s = s.substring(1);
			if (Character.isLetter(c)) {
				t = t.concat(Character.toString(c));
			}
		}
		t = t.toLowerCase();
		this.fillG(t, t.length() / l);
	}

	/**
	 * Fuellt das String-Array, das die Spalten der Verschluesselungsmatrix
	 * darstellt, mit dem uebergebenen Text.
	 *
	 * @param t
	 *            Text
	 * @param l
	 *            Anzahl der Zeilen einer Spalte
	 */
	private void fillG(String t, final int l) {
		// l hier zeilen einer spalte
		this.text = new String[this.size];
		for (int i = 0; i < this.size; i++) {
			this.text[i] = t.substring(0, l); // jeweils l Zeichen einlesen
			t = t.substring(l);
		}
	}

	/**
	 * Initialisiert die Pheromonmatrix.
	 *
	 */
	public void makeKanten() {
		this.pheromon = new double[this.size][this.size];
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				this.pheromon[i][j] = 1;
			}
		}
	}

	/**
	 * Berechnet den Wert der Prioritaetsregel fuer die Kante (i, j) und gibt
	 * diesen zurueck.
	 *
	 * @param i
	 *            Knoten am Kantenanfang
	 * @param j
	 *            Knoten am Kantenende
	 * @return score Wert der Prioritaetsregel
	 */
	public double getPrior(final int i, final int j) {
		double score = 0;
		// Formel der Prioritaetsregel:
		for (int k = 0; k < this.text[i].length(); k++) {
			score += this.bigrams[this.getNum(this.text[i].charAt(k))][this
					.getNum(this.text[j].charAt(k))];
		}
		return score / this.text[i].length();
	}

	/**
	 * Prueft ob Weg bereits gegangen und gibt in diesem Fall den bereits
	 * berechneten Wert zurueck. Ansonsten wird der Phero- monwert neu berechnet
	 * und der Weg in die Liste der bereits gegangenen aufgenommen.
	 *
	 * @param pfad
	 *            zu bewertender Weg
	 * @return points Pheromonwert des Weges
	 */
	public double getPhero2(final Vector<Integer> pfad) {
		if (this.wege.contains(pfad)) { // Pruefen ob schon gegangen
			return this.punkte.elementAt(this.wege.indexOf(pfad));
		}
		double points;
		points = this.calcPhero2(this.toText(pfad));
		this.wege.add(pfad);
		this.punkte.add(points);
		return points;
	}

	/**
	 * Bewertet den uebergebenen String durch einen Abgleich mit der Wortliste.
	 *
	 * @param t
	 *            zu bewertender String
	 * @return score Bewertung
	 */
	public double calcPhero2(final String t) {
		double score = 0;
		for (final String s : this.wortliste) {
			if (t.indexOf(s) != -1) {
				score += Math.pow(s.length(), 2); // quadrierte Wortlaenge
			}
		}
		return score / (this.size * this.text[0].length());
	}

	/**
	 * Erstellt aus einem Pfad durch den Graphen die zugehoerige
	 * Entschluesselung.
	 *
	 * @param pfad
	 *            Weg durch den Graph
	 * @return s Klartext
	 */
	public String toText(final Vector<Integer> pfad) {
		if (pfad == null) {
			return ""; //$NON-NLS-1$
		}

		String s = ""; //$NON-NLS-1$
		for (int j = 0; j < this.text[0].length(); j++) {
			for (int i = 0; i < pfad.size(); i++) {
				s = s.concat(Character.toString(this.text[pfad.elementAt(i)]
						.charAt(j)));
			}
		}
		return s;
	}

	/**
	 * Gibt die Pheromonmatrix zurueck.
	 *
	 * @return pheromon Die Pheromonmatrix
	 */
	public double[][] getMatrix() {
		return this.pheromon;
	}

	/**
	 * Gibt die Spalten der Verschluesselungsmatrix zurueck.
	 *
	 * @return text Array der Spalten
	 */
	public String[] getKnots() {
		return this.text;
	}

	/**
	 * Gibt den ASCII-Wert fuer den Kleinbuchstaben des uebergebenen Buchstaben
	 * zurueck
	 *
	 * @param c
	 *            uebergebener Buchstabe
	 * @return i ASCII-Wert
	 */
	private int getNum(final char c) {
		if (c < 123 && c > 96) {
			return c - 97;
		}
		return -1;
	}

}
