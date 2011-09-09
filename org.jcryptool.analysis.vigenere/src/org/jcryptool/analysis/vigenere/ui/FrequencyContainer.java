/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.jcryptool.analysis.freqanalysis.ui.FullAnalysisUI;
import org.jcryptool.analysis.vigenere.VigenereBreakerPlugin;
import org.jcryptool.analysis.vigenere.exceptions.IllegalInputException;
import org.jcryptool.analysis.vigenere.exceptions.NoContentException;
import org.jcryptool.analysis.vigenere.interfaces.DataProvider;
import org.jcryptool.analysis.vigenere.interfaces.FrequencyData;
import org.jcryptool.analysis.vigenere.interfaces.FrequencyGraphAdapter;
import org.jcryptool.core.logging.utils.LogUtil;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class FrequencyContainer {
    public static final String PREFERENCES = VigenereBreakerPlugin.PLUGIN_ID
            + ".references"; //$NON-NLS-1$
    public static final String BLOCKED_PREVIEW = PREFERENCES + ".preview"; //$NON-NLS-1$
    public static final String USED_ALPHABET = PREFERENCES + ".alphabet"; //$NON-NLS-1$
    public static final String USED_REF_TEXT = PREFERENCES + ".reference"; //$NON-NLS-1$
    public static final int TALLY = 470;

    private boolean blocked = false;
    private String alphident;
    private String alphtext;
    private String refident;

    /**
     * A string containing the reference text. This is due performance.
     * Accessing a file on hard disk is always expensive.
     */
    private String reftext;

    /**
     * A flag that indicated whether a character was already decrypted or not.
     */
    private boolean first = false;

    private boolean done = false;

    /**
     * An array containing a sample of already decrytped characters. Unknown
     * plain text is represented by <code>_</code>.
     */
    private char[] display;
    private final int passlenght;
    private FrequencyGraphAdapter graph = null;
    private PasswordContainer container;
    private String chiffretext = null;
    private int active = -1;

    public FrequencyContainer(final String chiffre, final int length)
            throws NoContentException {
        loadPreference();
        this.chiffretext = chiffre;
        this.passlenght = length;
        this.reftext = DataProvider.getInstance().readReferenceText(refident);
        this.alphtext = DataProvider.getInstance().getAlphabet(alphident);
    }

    protected void initGraph(final Composite parent) {
        this.graph = new FrequencyGraphAdapter(parent);
    }

    protected void activateComparator(final String reference,
            final String alphabet) {
        graph.setComparator(reference, alphabet);
        graph.activateComparator();
    }

    protected void enableButtons(final Composite parent) {
        this.container = new PasswordContainer(parent, this.passlenght, this);
    }

    private char calcCharacter(final int shift) {
        char[] ref = alphtext.toCharArray();
        int index = calculateModulo(ref.length - shift, ref.length);

        return ref[index];
    }

    protected int setCharacter() throws IllegalInputException {
        int index = this.active;

        if (-1 == index) {
            throw new IllegalInputException("No character selected!"); //$NON-NLS-1$
        }

        int shift = graph.getShift();
        char passchar = calcCharacter(shift);
        container.signalFound(String.valueOf(passchar), index);

        try {
            showGraph(findNext(), alphtext);
        } catch (NoContentException ncEx) {
            // all done. just reset graph.
            done = true;
            graph.refresh();
        }

        return index;
    }

    protected String decrypt(final int index) {
        // initially chiffre text is displayed. after identifying first
        // character of pass phrase the unknown characters of chiffre
        // will be replaced by underscores.
        if (!first) {
            this.display = createEmpty();
            this.first = true;
        }

        String chiffre = String.copyValueOf(selectCharacters(index));
        String phrase = container.getCharacter(index);

        String plain = DataProvider.getInstance().decrypt(chiffre, phrase,
                alphident);

        this.display = replace(index, this.display, plain.toCharArray());

        String preview = formatPreview(this.blocked, passlenght,
                String.valueOf(this.display));

        return preview;
    }

    private char[] selectCharacters(final int index) {
        StringBuffer buffer = new StringBuffer();

        // get every character of current index of pass phrase.
        // there is no need to descrypt entire chiffre text.
        for (int i = index - 1; i < chiffretext.length(); i += passlenght) {
            buffer.append(chiffretext.toCharArray()[i]);
        }

        return buffer.toString().toCharArray();
    }

    public char[] replace(final int start, final char[] orig,
            final char[] replace) {
        int j = 0;

        for (int i = start - 1; i < orig.length; i += passlenght) {
            orig[i] = replace[j++];
        }

        return orig;
    }

    protected void preview(final boolean blocks, final String reftext,
            final String alphabet) {
        try {
            int reset = active;
            this.active--;
            String text = DataProvider.getInstance().readReferenceText(reftext);
            String alph = DataProvider.getInstance().getAlphabet(alphabet);

            graph.setComparator(text, alph);
            graph.activateComparator();
            showGraph(reset, alph);
        } catch (NoContentException ioEx) {
            // not my fault. just in case.
            String message = Messages.FrequencyGui_mbox_missing;
            MessageBox box = new MessageBox(null, SWT.ICON_WARNING);
            box.setText(Messages.VigenereGlobal_mbox_info);
            box.setMessage(message);
            box.open();
        }
    }

    protected void save(final boolean blocks, final String reftext,
            final String alphabet) {
        savePreferences(blocks, reftext, alphabet);
        this.blocked = blocks;
        this.alphident = alphabet;
        this.alphtext = DataProvider.getInstance().getAlphabet(alphabet);

        try {
            this.reftext = DataProvider.getInstance()
                    .readReferenceText(reftext);
            this.refident = reftext;
        } catch (NoContentException ncEx) {

        }
    }

    protected void restore() {
        graph.setComparator(reftext, alphtext);
        graph.activateComparator();
        showGraph(active, alphtext);
    }

    protected boolean isBlocked() {
        return this.blocked;
    }

    private int findNext() throws NoContentException {
        int next = this.active + 1;

        // // reached last character of pass phrase.
        // // looking for other open characters.
        if (passlenght < next) {
            return container.findNextOpen(1);
        }

        // there is a next element after the active one.
        // is next element already found?
        if (container.isFound(next)) {
            return container.findNextOpen(next);
        }

        return next;
    }

    protected String formatPreview(final boolean block, final int length,
            final String chiffre) {
        if (!block) {
            return chiffre.toLowerCase();
        }

        int c = 0;
        int l = 0;
        String s = chiffre.trim();
        String formatted = "";
        String temp = "";

        while ((c * length) < s.length()) {
            int t0 = (c + 1) * length;
            int t1 = s.length();
            int end = t0 > t1 ? end = t1 : t0;

            temp += s.substring(c * length, end) + " "; //$NON-NLS-1$

            // case: last character is reached but line not over 93.
            if (0 != (end % length)) {
                formatted += temp;
            }

            // case: line is full.
            if (!(temp.length() <= 93 - length)) {
                formatted += temp + "\n"; // add CRLF. //$NON-NLS-1$
                temp = ""; // reset temp. //$NON-NLS-1$
                l++; // add line counter.

                // user interface shows only 5 lines.
                // why do additional work that nobody sees?
                // so end when reaching 5 lines.
                if (l >= 5) {
                    break;
                }
            }

            c++;
        }

        String back = formatted.toLowerCase();

        return back;
    }

    private char[] createEmpty() {
        StringBuffer buffer = new StringBuffer();
        int c = chiffretext.length() < 470 ? c = chiffretext.length() : 470;

        for (int i = 0; i < c; i++) {
            buffer.append("_"); //$NON-NLS-1$
        }

        return buffer.toString().toCharArray();
    }

    protected String getSampleText() {
        if (!first) {
            return cutText(chiffretext).toLowerCase();
        }

        return String.valueOf(display).toLowerCase();
    }

    protected String decryptAll() throws IllegalInputException {
        String pass = container.getCharacters();
        String plain = DataProvider.getInstance().decrypt(chiffretext, pass,
                alphident);
        return plain.toUpperCase();

    }

    protected String getPasswort() throws IllegalInputException {
        return container.getCharacters();
    }



    protected String decryptAll(final String phrase)
            throws IllegalInputException {
        String plain = DataProvider.getInstance().decrypt(chiffretext, phrase,
                alphident);
        return plain.toUpperCase();

    }

    protected String guessPass() {
        String whole = "";

        for (int i = 0; i < passlenght; i++) {
            String good = "";

            FrequencyData[] dta = countCharacters(chiffretext, passlenght, i);

            Arrays.sort(dta, new Comparator<FrequencyData>() {
                public int compare(FrequencyData o1, FrequencyData o2) {
                    return o2.getCount() - o1.getCount();
                }
            });

            double temp = 0d;
            // in case someone tries to evaluate a really, really short
            // chiffre text and dta don't have 5 entries.
            int max = 5 > dta.length ? dta.length : 5;

            for (int j = 0; j < max; j++) {
                int p = dta[j].getCharacter();
                int s = getShift(p);
                char c = calcCharacter(s);
                double l = likelihood(dta, s);

                if (temp < l) {
                    temp = l;
                    good = String.valueOf(c);
                }
            }

            whole = whole.concat(good);
        }

        return whole;
    }

    private double likelihood(final FrequencyData[] data, int shift) {
        HashMap<Integer, Float> map = DataProvider.getInstance()
                .getLetterFrequency(refident);
        double like = 0d;

        for (FrequencyData d : data) {
            char c = (char) d.getCharacter();
            int i = alphtext.indexOf(c);
            int n = i + shift;

            if (alphtext.length() <= n) {
                n -= alphtext.length();
            }

            String s = alphtext.substring(n, n + 1).toLowerCase();
            Float f = map.get(s.hashCode());

            if (null != f) {
                like += d.getCount() * f;
            }
        }

        return like;
    }

    private int getShift(final int character) {
        // get number of characters in chosen alphabet
        int l = alphtext.length();
        // look up index of character 'e' in this alphabet.
        int p = alphtext.indexOf('e'); //$NON-NLS-1$
        // look up index of this character.
        int c = alphtext.indexOf(character);
        // shift: index of 'e' - index of this character + length.
        int s = p - c + l;

        // if this shift greater as length of alphabet - this length.
        // using the symmetry.
        if (l < s) {
            s -= l;
        }

        return s;
    }

    private FrequencyData[] countCharacters(final String chiffre, final int length,
            final int number) {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (int i = number; i < chiffre.length(); i += length) {
            String s = chiffre.substring(i, i + 1);
            int j = s.hashCode();

            if (null != map.get(j)) {
                int t = map.get(j);
                t++;
                map.put(j, t);
            } else {
                map.put(j, 1);
            }
        }

        Set<Integer> set = map.keySet();
        Iterator<Integer> it = set.iterator();
        List<FrequencyData> list = new ArrayList<FrequencyData>();

        while (it.hasNext()) {
            Integer i0 = it.next();
            Integer i1 = map.get(i0);
            list.add(new FrequencyData(i0, i1));
        }

        return (FrequencyData[]) list.toArray(new FrequencyData[0]);
    }

    protected String showCompletePass(String in) {
        if (passlenght < in.length()) {
            in = in.substring(0, passlenght);
        }

        container.showCompletePass(in);
        String plain = DataProvider.getInstance().decrypt(chiffretext, in,
                alphident);
        String formatted = formatPreview(blocked, passlenght, plain);

        if (!first) {
            first = true;
        }

        display = cutText(plain).toCharArray();

        return formatted;
    }

    // =========================================================================

    /**
     * Creates the frequency analysis graph for the first character of the pass
     * phrase. Also highlights the first element of the user interaction area.
     */
    protected void show() {
        showGraph(1, alphtext);
    }

    /**
     * Creates the frequency analysis graph for the character with this number
     * of the pass phrase. Also highlights the element with this number of the
     * user interaction area.
     *
     * @param number
     *            the number of the character to show graph and highlight input
     *            area.
     */
    protected void showGraph(final int number, final String alphabet) {
        container.highlight(number);
        graph.setCalculation(chiffretext, passlenght, number, alphabet);
        graph.refresh();
        this.active = number;
    }

    /**
     * Calculates mathematically correct modulo operation:
     * <code>first mod second</code>.
     *
     * @param first
     *            the first value of modulo operation.
     * @param second
     *            the second value of modulo operation.
     * @return the result of modulo operation.
     * @see FullAnalysisUI#modulo(int, int)
     */
    private int calculateModulo(final int first, final int second) {
        if (first < 0) {
            double ceil = Math.ceil(-1.0d * first / second);
            int howmany = (int) Math.round(ceil);

            return first + howmany * second;
        }

        return first % second;
    }

    /**
     * Saves this preferences for the Vigenère plug-in.
     *
     * @param blocks
     *            the flag indicating whether to show a preview in blocks.
     * @param reftext
     *            the name of the reference text.
     * @param alphabet
     *            the identifying name of the used alphabet of the plain text.
     */
    private void savePreferences(final boolean blocks, final String reftext,
            final String alphabet) {
        Preferences preferences = ConfigurationScope.INSTANCE.getNode(VigenereBreakerPlugin.PLUGIN_ID);
        Preferences frequency = preferences.node(PREFERENCES);

        frequency.put(BLOCKED_PREVIEW, String.valueOf(blocks));
        frequency.put(USED_REF_TEXT, reftext);
        frequency.put(USED_ALPHABET, alphabet);

        try {
            preferences.flush();
        } catch (BackingStoreException ex) {
            LogUtil.logError(ex);
        }
    }

    /**
     * Loads the preferences for the Vigenère plug-in and sets global fields for
     * later use. Uses default values if no saved preferences could be found.
     * <p>
     * Preferences of the Vigenère plug-in are:
     * <ul>
     * <li>A Flag that indicates whether to show the preview of deciphered
     * chiffre text in block of the length of the pass phrase. Default value:
     * <code>false</code>.
     * <li>The designator for the used reference text. Default value:
     * <code>DEUTSCH Descartes</code>.
     * <li>The designator for the alphabet of the plain text. Default value:
     * <code>Upper and lower Latin (A-Z,a-z)</code>.
     *
     * @see DataProvider#getDefaultReference()
     * @see DataProvider#getDefaultAlphabet()
     */
    private void loadPreference() {
        Preferences preferences = ConfigurationScope.INSTANCE.getNode(VigenereBreakerPlugin.PLUGIN_ID);
        Preferences frequency = preferences.node(PREFERENCES);

        this.blocked = frequency.getBoolean(BLOCKED_PREVIEW, true);
        this.refident = frequency.get(USED_REF_TEXT, DataProvider.getInstance()
                .getDefaultReference());
        this.alphident = frequency.get(USED_ALPHABET, DataProvider
                .getInstance().getDefaultAlphabet());
    }

    /**
     * Returns the number of the character the user currently working with.
     *
     * @return the current pass phrase character number.
     */
    protected int getActiveNo() {
        return this.active;
    }

    /**
     * Returns the designator of the reference text which is currently used by
     * the plug-in.
     *
     * @return an identifier for the reference text.
     */
    public String getRefTextIdent() {
        return this.refident;
    }

    /**
     * Returns the content of the reference text.
     *
     * @return the content of the reference text.
     */
    public String getReferenceText() {
        return this.reftext;
    }

    /**
     * Returns the designator of the alphabet of the plain text.
     *
     * @return an identifier for the plain text alphabet.
     */
    public String getAlphabetIdent() {
        return alphident;
    }

    /**
     * Returns the characters of the plain text alphabet.
     *
     * @return the characters of the plain text alphabet.
     */
    public String getAlphabet() {
        return alphtext;
    }

    /**
     * Checks the length of this texts and shortens it when character count is
     * to high. This is due restrains of the number of characters for preview
     * text. Uses link {@link FrequencyContainer#TALLY} as basis for comparison.
     *
     * @param text
     *            the string to check and shorten.
     * @return the shortened string.
     */
    protected String cutText(final String text) {
        if (TALLY > text.length()) {
            return text.toLowerCase();
        } else {
            return text.substring(0, TALLY).toLowerCase();
        }
    }

    /**
     * Checks whether a characters was shifted before or not. Returns
     * <code>true</code> if a shift already occurred; or <code>false</code> if
     * not.
     *
     * @return a flag to show if a shift occurred before.
     */
    protected boolean isFirst() {
        return first;
    }

    /**
     * Checks whether character where all shifted or one. Returns
     * <code>true</code> if all characters were calculated at least once; or
     * <code>false</code> otherwise.
     *
     * @return the flag indicating if all characters were calculated.
     */
    protected boolean isDone() {
        return done;
    }

    /**
     * Returns the length of the current password.
     *
     * @return the length of the password.
     * @deprecated no longer needed.
     */
    protected int getPassLenght() {
        return passlenght;
    }

	public int getPasswordLength() {
		return container.getPasswordLength();
	}

	public int getFoundCharCount() {
		return container.getFoundCharCount();
	}
}
