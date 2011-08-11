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

import java.util.HashMap;
import java.util.Map;

/**
 * Reads ngrams from a specified path. Ngrams are generates by us (Georg
 * Chalupar, Niederwieser Martin, Scheuchepflug Simon).
 *
 * We used free books, to generete our ngrams to analyse a language.
 *
 * If you want to use your own ngrams, check out the NGramGen class. There is
 * the java code we used to generate the ngrams.
 *
 * The more informations (like a lot of books in the same language) you use, the
 * better the algorithm will work.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */

public class NGramProvider {

	String pathToNGrams;
	Map<String, Integer> ngrams;

	public NGramProvider(String pathToNGrams) {
		this.pathToNGrams = pathToNGrams;

		ngrams = new HashMap<String, Integer>();

		String input = new IO().read(pathToNGrams, "");
		String[] inputArray = input.split("\\|");

		for (int i = 0; i < inputArray.length - 1; i += 2) {
			ngrams.put(inputArray[i], Integer.parseInt(inputArray[i + 1]));
		}

		// just for printing values:
		// you can use this code to print the content of the ngrams file on
		// stdout

		// final String firstSeparator = "|";
		// final String secondSeparator = "|\r\n";
		// StringBuilder output = new StringBuilder("");
		// ArrayList<String> list = new ArrayList<String>(ngrams.keySet());
		// Collections.sort(list);
		//
		// for (String sequence : list) {
		// output.append(sequence + firstSeparator + ngrams.get(sequence)
		// + secondSeparator);
		// }
		// System.out.println(output.toString());
	}

	/**
	 * return a hashmap with the ngrams as index and their probability as value
	 */

	public Map<String, Integer> getNgrams() {
		return ngrams;
	}

	/**
	 * @return the amount of 1grams in the ngrams.
	 */

	public int getTotalMonoGrams() {

		Integer total = ngrams.get("totalTextRead");

		if (total != null) {
			return total;
		} else {
			return 0;
		}
	}
}