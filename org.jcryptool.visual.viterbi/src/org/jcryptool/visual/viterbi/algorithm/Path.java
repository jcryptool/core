//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.viterbi.algorithm;

/**
 *
 * This class represents a path of the Viterbi algorithm.
 *
 * Every way how the ciphertext can be generated is represented with a path.
 * To compare paths there is an probability for every path. <br />
 * A path therefore contains:
 * <ul>
 * <li>two plaintext suggestions which will form the ciphertext</li>
 * <li>a probability</li>
 * </ul>
 *
 * As this class will be used in collections, it implements Comparable for
 * better search and sort performance.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */
public class Path implements Comparable<Path> {

	private StringBuilder plain1;
	private StringBuilder plain2;
	private double probability;

	/**
	 *
	 * @param string1
	 *            representation of the possible plaintext 1
	 *
	 * @param string2
	 *            representation of the possible plaintext 2
	 *
	 * @param probability
	 *            the paths probability (0<=probability<=1)
	 */
	public Path(String string1, String string2, double probability) {
		this.plain1 = new StringBuilder(string1);
		this.plain2 = new StringBuilder(string2);
		this.probability = probability;
	}

	/**
	 * @return the representation of the possible plaintext 1
	 */
	public String getPlain1() {
		return plain1.toString();
	}

	/**
	 * @return the representation of the possible plaintext 2
	 */
	public String getPlain2() {
		return plain2.toString();
	}

	/**
	 * @return the probability
	 */
	public double getProbability() {
		return probability;
	}

	/**
	 * @param the
	 *            probability
	 */
	public void setProbability(double propability) {
		this.probability = propability;
	}

	/**
	 * compares this path to another path. this is done by comparing the
	 * probabilities of the paths. designed to give a descending order.
	 *
	 * @param anotherPath
	 * @return the value 0 if the probability of anotherPath is numerically
	 *         equal to this probability; a value LESS than 0 if the probability
	 *         of this Path is numerically GREATER than the probability of
	 *         anotherPath; and a value greater than 0 in the other case.
	 */
	public int compareTo(Path anotherPath) {

		return Double.compare(anotherPath.getProbability(),
				this.getProbability());
	}

	/**
	 * "\t" is used as seperator.
	 *
	 * @return a text representation of this path
	 */
	@Override
	public String toString() {
		return plain1 + "\t" + plain2 + "\t" + probability;
	}
}
