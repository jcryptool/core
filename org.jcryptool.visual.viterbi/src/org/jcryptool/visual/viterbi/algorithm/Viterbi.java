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

import java.util.Collections;
import java.util.LinkedList;

/**
 * This is the core of the viterbi plug-in.
 *
 * The viterbi algorithm can solve a running key cipher, depending on the
 * language model, ngram size and the prunning number. Higher values will
 * increase the percentage of solved characters, but the performance will
 * decrease rapidly. <br />
 * This class is able to run as separate thread, and can send status information
 * while running. When this thread is finished, it calls the observers
 * viterbiFinished. <br />
 * <br />
 * This algorithm is based on our own interpretation of the algorithm. As we
 * don't know any other implementations yet, this might not be exactly the
 * original Viterbi algorithm. We used a paper called
 * "Automatic solution in depth of one time pads" by Martin Ekera and Bjorn
 * Terelius which can be found <a href=
 * "http://www.csc.kth.se/utbildning/kth/kurser/DD2449/krypto09/pdf/rapport_vernam080309.pdf"
 * >here</a> <br />
 * This code was developed within a project as a part of our studies of
 * "secure information systems" at the "university of applied sciences" (FH)
 * Hagenberg, Austria.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */
public class Viterbi implements Runnable {

	private static final char DEFAULT_CHARACTER_SET_BEGIN = '\u0000';
	private static final char DEFAULT_CHARACTER_SET_END = '\u00ff';

	private int nGramSize;
	private int prunningNumber;
	private LanguageModel language;
	private Combination combi;
	private char characterSetBegin;
	private char chararcerSetEnd;
	private ViterbiObserver observer;
	private String cipher;
	private Path solution;
	private Boolean stop;
	private int maxDuplicatePathLength;

	/**
	 * This constructor will use the standard values for the character set
	 * start/end".
	 *
	 * @param nGramSize
	 *            , defines how the ngram size, we recommend 5
	 * @param prunningNumber
	 *            , defines the number of how many paths will be stored, paths
	 *            with low probability will be deleted
	 * @param language
	 *            , defines the LanguageModel
	 * @param combination
	 *            , Combination is an interface, so the viterbi algorithm is
	 *            very flexible, every inverable combination can be used
	 * @param observer
	 *            , the observer gets the best path with each iteration, and
	 *            will be informed when the algorithm is finished
	 * @param cipher
	 *            , the ciphertext to decrypt
	 */
	public Viterbi(int nGramSize, int prunningNumber, LanguageModel language,
			Combination combi, ViterbiObserver observer, String cipher) {

		this(nGramSize, prunningNumber, language, combi, observer, cipher,
				DEFAULT_CHARACTER_SET_BEGIN, DEFAULT_CHARACTER_SET_END);
	}

	/**
	 * This constructor allows to set more values.
	 *
	 * @param nGramSize
	 *            , defines the ngram size used, we recommend 5. Note: this
	 *            nGramSize has to be less or equal the n-gram size of the
	 *            LanguageModel used
	 * @param prunningNumber
	 *            , defines how many of the best paths to be stored, paths with
	 *            low probability will be deleted
	 * @param language
	 *            , defines the LanguageModel
	 * @param combination
	 *            , Combination is an interface, so the viterbi algorithm is
	 *            very flexible, every inverable combination can be used
	 * @param observer
	 *            , the observer gets the best path with each iteration, and
	 *            will be informed when the algorithm is finished
	 * @param cipher
	 *            , the ciphertext to decrypt/break
	 *
	 * @param characterSetBegin
	 *            , is the first character of the character set, typically the
	 *            first 20 characters are not printable
	 * @param chararcerSetEnd
	 *            , is the last character of the character set
	 */
	public Viterbi(int nGramSize, int prunningNumber, LanguageModel language,
			Combination combi, ViterbiObserver observer, String cipher,
			char characterSetBegin, char chararcerSetEnd) {
		this.nGramSize = nGramSize;
		this.prunningNumber = prunningNumber;
		this.language = language;
		this.combi = combi;
		this.observer = observer;
		this.characterSetBegin = characterSetBegin;
		this.chararcerSetEnd = chararcerSetEnd;
		this.cipher = cipher;
		this.stop = false;
		this.maxDuplicatePathLength = combi.getMaxDuplicatePathLength(cipher);
	}

	/**
	 *
	 * This method provides the core calculations. Use stop() to cancel the
	 * operation. The observers viterbiFinished() method is called when finished
	 * (without being canceled)
	 *
	 * This method implements the viterbi algorithm for solving running keys.
	 * Basically, we compare parts of possible cihpertext combinations and
	 * memorize the most likely. For every of these likely combinations, we add
	 * a character to generate another combinations.
	 */
	public void run() {

		LinkedList<Path> currentPaths = new LinkedList<Path>();

		// in the first iteration there is already one path with the probability
		// 1
		currentPaths.add(new Path("", "", 1.0));

		LinkedList<Path> oldPaths;

		// loop for every character in the ciphertext
		for (int i = 0; i < cipher.length(); i++) {

			// the code will stop here if another method calls the stop() method
			synchronized (stop) {
				if (stop) {
					return;
				}
			}

			char cipherChar = cipher.charAt(i);

			// inform observer about current best path
			// TODO check for null
			observer.update(currentPaths.getFirst());

			// normalizing: the best path gets the probability 1
			// therefore we wont get very little numbers

			double maxProbabability = currentPaths.getFirst().getProbability();
			double factor = 1 / maxProbabability;

			for (Path path : currentPaths) {
				path.setProbability(path.getProbability() * factor);
			}

			// saving paths
			oldPaths = currentPaths;
			currentPaths = new LinkedList<Path>();

			// for every old path: create new paths by appending characters and
			// calculate probabilities
			for (Path path : oldPaths) {

				String plain1 = path.getPlain1();
				String plain2 = path.getPlain2();

				String nGramTemplate1;
				String nGramTemplate2;

				// selecting the last n characters
				if (plain1.length() >= nGramSize) {
					nGramTemplate1 = plain1.substring(plain1.length()
							- nGramSize + 1);
					nGramTemplate2 = plain2.substring(plain2.length()
							- nGramSize + 1);
				} else {
					nGramTemplate1 = plain1;
					nGramTemplate2 = plain2;
				}

				double oldPathProbability = path.getProbability();

				// we get every possible combination by looping trough all the
				// characters in the alphabet, and then calculating the other
				// character
				plainfor: for (char plain1Char = characterSetBegin; plain1Char <= chararcerSetEnd; plain1Char++) {

					// inverting the combination
					char plain2Char = combi.subtract(cipherChar, plain1Char);

					//to prevent duplicate paths
					if (i<=maxDuplicatePathLength && path.getPlain1().equals(path.getPlain2())) {
						for (Path otherPath : currentPaths) {
							//check if path is already there
							if (otherPath.getPlain2().equals(plain1+plain1Char)) {
								continue plainfor;
							}
						}
					}

					String nGram1 = nGramTemplate1 + plain1Char;
					String nGram2 = nGramTemplate2 + plain2Char;

					double plain1Probability = language.getProbability(nGram1);
					double plain2Probability = language.getProbability(nGram2);

					// calculating the probability
					double currentPathProbability = plain1Probability
							* plain2Probability * oldPathProbability;

					// the first few paths will always be stored
					if (currentPaths.size() < prunningNumber) {
						Path newPath = new Path(plain1 + plain1Char, plain2
								+ plain2Char, currentPathProbability);
						sortedInsert(newPath, currentPaths);

						// if the current path is better than the worst path
						// saved
						// the worst path will be removed
					} else if (currentPathProbability > currentPaths.getLast()
							.getProbability()) {
						currentPaths.removeLast();
						Path newPath = new Path(plain1 + plain1Char, plain2
								+ plain2Char, currentPathProbability);
						sortedInsert(newPath, currentPaths);
					}
				}
			}
		}
		solution = currentPaths.getFirst();
		observer.viterbiFinished(); // TODO check for null
	}

	/**
	 * Inserts the path into the list. The path will be inserted in the right
	 * place.
	 *
	 * On position 0 is the path with the best probability. Paths with lower
	 * probability have higher positions.
	 *
	 * @param path
	 * @param list
	 *
	 */
	private void sortedInsert(Path path, LinkedList<Path> list) {
		int index = Collections.binarySearch(list, path);
		if (index < 0)
			index = -index - 1;
		list.add(index, path);
	}

	/**
	 * @return the best path or null, if the viterbi algorithm is not finished
	 */
	public Path getSolution() {
		return solution;
	}

	/**
	 * Stops this thread. The current iteration will be finished.
	 */
	public void stop() {
		synchronized (stop) {
			stop = true;
		}
	}
}
