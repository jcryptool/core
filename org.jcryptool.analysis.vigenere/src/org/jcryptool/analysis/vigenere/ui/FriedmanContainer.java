/* *****************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Composite;
import org.jcryptool.analysis.vigenere.interfaces.FriedmanCalcAdapter;
import org.jcryptool.analysis.vigenere.interfaces.FriedmanData;
import org.jcryptool.analysis.vigenere.interfaces.FriedmanGraphAdapter;

public class FriedmanContainer {
    private String chiffre;
    private FriedmanGraphAdapter graph;
    public Composite parent;

    public FriedmanContainer(final Composite parent, final String chiffre) {
        this.chiffre = chiffre;
        this.parent = parent;
    }

    protected void initGraph() {
        this.graph = new FriedmanGraphAdapter(parent);
        graph.showCalculation(replaceSpecials(chiffre));
    }

    /**
     * Converts all characters of this text to upper case and replaces all
     * special character afterwards.
     *
     * @param text
     *            the text to convert.
     * @return the converted text.
     */
    private String replaceSpecials(String text) {
        // regex pattern: all characters except A through Z.
        // reluctant quantifier: search one or more times.
        Pattern pattern = Pattern.compile("[^A-Z]+?"); //$NON-NLS-1$
        // matches pattern with this text transformed to upper case.
        Matcher matcher = pattern.matcher(text.toUpperCase());

        // search until all special characters (including white spaces)
        // are replaced
        while (matcher.find()) {
            String match = matcher.group();
            text = text.replace(match, ""); //$NON-NLS-1$
        }

        return text;
    }

    /**
     * Analyzes this chiffre text and looks for the length of the pass phrase.
     *
     * @param chiffre
     *            the chiffre text to analyze.
     * @return the estimated length of the pass phrase.
     */
    protected int findKeyLength(final String chiffre) {
        FriedmanCalcAdapter fried = new FriedmanCalcAdapter(chiffre);
        double[] values = fried.getAnalysis();
        final List<FriedmanData> list = new ArrayList<FriedmanData>();

        for (int i = 0; i < values.length; i++) {
            list.add(new FriedmanData(i, values[i]));
        }

        // sort the list by highest number of analysis values.
        Collections.sort(list, new Comparator<FriedmanData>() {
            @Override
			public int compare(FriedmanData o1, FriedmanData o2) {
                double t = o2.getValue() - o1.getValue();
                if (0 < t) {
                    return 1;
                } else if (0 > t) {
                    return -1;
                }

                return 0;
            }
        });

        final int hits = 3; // number of unchanged mode searches.

        int tally = 5;
        int ctr = 0;
        int cmode = 0;

        do { // repeat until mode doesn't change for 3 times.
            List<FriedmanData> peaks = getSample(tally, list);
            List<Integer> diffs = new ArrayList<Integer>();

            for (int i = 1; i < peaks.size(); i++) {
                int t = peaks.get(i).getIndex();
                int q = peaks.get(i - 1).getIndex();
                diffs.add(t - q);
            }

            List<Integer> modes = computeMode(diffs);
            Integer mode = modes.get(0);

            if (mode == cmode) {
                ctr++;

                if (ctr == hits) {
                    // end loop.
                    break;
                }
            } else {
                cmode = mode;
                ctr = 1;
            }

            tally += 5;
        } while (true);

        return cmode;
    }

    /**
     * Creates a sample set. Uses this percentages to create sample of the
     * original data set.
     *
     * @param perc
     *            the percentage to create sample set with.
     * @param data
     *            the data set to create the sample from.
     * @return the created sample set.
     */
    private List<FriedmanData> getSample(final int perc, final List<FriedmanData> data) {
        List<FriedmanData> peaks = new ArrayList<FriedmanData>();

        for (int i = 0; i < perc; i++) {
            peaks.add(data.get(i));
        }

        Collections.sort(peaks, new Comparator<FriedmanData>() {
            @Override
			public int compare(FriedmanData o1, FriedmanData o2) {
                return o1.getIndex() - o2.getIndex();
            }
        });

        return peaks;
    }

    /**
     * Calculates the mode of this list of integer values. The mode is the value
     * that occurs the most frequently in a data set. For example in the set {2,
     * 6, 9, 5, 3, 1, 5, 4, 2, 5} the mode is 5. Returns a list of modes because
     * it's possible a most frequently does not occur.
     *
     * @param data
     *            a set of integer to check for the mode.
     * @return a collection of found modes.
     */
    private List<Integer> computeMode(List<Integer> data) {
        Hashtable<Integer, Integer> ctr = new Hashtable<Integer, Integer>();

        for (Integer i : data) {
            Integer j = ctr.get(i);

            if (null == j) {
                j = 0;
            }

            ctr.put(i, ++j);
        }

        List<Integer> modi = new ArrayList<Integer>();
        Integer high = 0;

        for (Integer i : ctr.keySet()) {
            Integer t = ctr.get(i);

            if (high < t) {
                high = t;
                modi.clear();
                modi.add(i);
            } else if (high.equals(t)) {
                modi.add(i);
            }
        }

        return modi;
    }
}
