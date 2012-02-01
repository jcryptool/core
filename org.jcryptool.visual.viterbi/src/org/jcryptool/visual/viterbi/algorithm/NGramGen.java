// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.viterbi.algorithm;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jcryptool.core.util.directories.DirectoryService;

/**
 *
 * This class provides the function to create your own NGrams.
 *
 * It is not yet integrated into JCrypTool. It would be too complicated and exhausting for normal users to gather
 * language references.
 *
 * However, if you want to extend the functions of the viterbi algorithm, you should have a look at this code. * As this
 * step is not very complicated, you can easily generate them on your own. However, we recommend you to look at this
 * code first, as the rest of the program demands a special format.
 *
 * format looks like this:
 *
 * [string]|[probability]|\r\n
 *
 * For performance issues the total amount of 1Grams is hardcoded as:
 *
 * totalTextRead|[amount]|\r\n
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 *
 */
public class NGramGen {
    final static String firstSeparator = "|";
    final static String secondSeparator = "|\r\n";
    final static String targetFileName = DirectoryService.getUserHomeDir() + "/ngrams_en.txt";
    final static String sourcePath = DirectoryService.getUserHomeDir() + "/books_en";
    final static int nGramSize = 5;

    /**
     * Generates ngrams from
     *
     * @param NGramSize maximum length of ngrams
     * @param ngramDirectory directory that contains text files to analyze
     * @return
     */
    public Map<String, Integer> generate(int NGramSize, File ngramDirectory) {

        StringBuilder text = new StringBuilder("");

        IO io = new IO();

        File[] books = ngramDirectory.listFiles();
        for (File book : books) {
            text.append(io.read(book.getAbsolutePath(), " "));
        }
        return generate(NGramSize, text.toString());
    }

    /**
     *
     * Generates ngrams based on the text read.
     *
     * @param size of the ngrams
     * @param the text from which the ngrams should be generated
     *
     * @return a HashMap with the ngram as index, and the probability as value
     */

    public HashMap<String, Integer> generate(int NGramSize, String text) {

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (int n = 1; n <= nGramSize; n++) {
            for (int i = nGramSize; i < text.length(); i++) {
                String sequence = text.substring(i - n, i);

                if (map.containsKey(sequence)) {
                    int d = map.get(sequence);
                    d++;
                    map.put(sequence, d);
                } else {
                    map.put(sequence, 1);
                }
            }
        }

        map.put("totalTextRead", text.length());

        return map;
    }

    /**
     * Writes the ngrams to file
     *
     * @param ngrams the ngrams
     * @param destination file to write the ngrams to
     */
    public void writeNgrams(Map<String, Integer> ngrams, File destination) {
        int totalTextRead = ngrams.remove("totalTextRead");
        StringBuilder output = new StringBuilder("");
        ArrayList<String> list = new ArrayList<String>(ngrams.keySet());
        Collections.sort(list);

        output.append("totalTextRead" + firstSeparator + totalTextRead + secondSeparator);

        for (String sequence : list) {

            output.append(sequence + firstSeparator + ngrams.get(sequence) + secondSeparator);
        }

        new IO().write(output.toString(), targetFileName);
    }
}
