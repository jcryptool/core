//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.freqanalysis.calc;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.crypto.ui.background.BackgroundJob;

/**
 * @author SLeischnig Class for Frequency Analysis
 */
public class FreqAnalysisCalc {
	private String text = ""; //$NON-NLS-1$
	private FreqAnalysisData[] analysisAllchars = new FreqAnalysisData[0];
	private FreqAnalysisData[] analysisArray = new FreqAnalysisData[0];

	private String outputAnalysis = ""; //$NON-NLS-1$
	private int textlength = 0;
	private DecimalFormat twoDigits = new DecimalFormat("#0.00"); //$NON-NLS-1$

	/**
	 * rotates a text by a specified length and filters characters at recurring
	 * positions
	 * 
	 * @param text    the text String
	 * @param offset  the offset
	 * @param periodL returns only each periodL-th character (set it to 1 for the
	 *                full text)
	 * @return the rotated/filtered text
	 */
	private String getOffsetCharString(final String text, final int offset, final int periodL) {
		String myText = ""; //$NON-NLS-1$
		if (periodL > 1) {
			for (int i = 0; i < text.length(); i++) {
				if ((i - offset) % periodL == 0) {
					myText = myText.concat(String.valueOf(text.charAt(i)));
				}
			}
		} else {
			return text;
		}
		return myText;
	}

	/**
	 * Returns a String which contains each character and control sequence the text
	 * contains only and only once.
	 */
	private String countDifferentChars(final String text) {
		int i = 0;
		String myText = text;
		while (i < myText.length()) {
			myText = myText.substring(0, i + 1).concat(removeChar(myText.substring(i + 1), myText.charAt(i)));
			i++;
		}
		return myText;
	}

	/**
	 * removes a character from the text
	 */
	private static String removeChar(final String s, final char c) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != c) {
				result.append(s.charAt(i));
			}
		}
		return result.toString();
	}

	/**
	 * Analyse a text on his frequency distribution with a given character set
	 * (alphabet).
	 * 
	 * @param inputText       text to analyse
	 * @param length          set to a value other than 1 if you want to analyse
	 *                        only a periodic subset of the text (see offset, too)
	 * @param offset          set to a value other than 0 if you want to analyse
	 *                        only a periodic subset of the text (see offset, too)
	 * @param tModifySettings set zero, if you dont want to make restrictions on the
	 *                        character set of the text
	 */
	public FreqAnalysisCalc(final String inputText, final int length, final int offset,
			final TransformData tModifySettings, final String alphabet) {
		String myText = inputText;

		text = getOffsetCharString(myText, offset, length);
		if (tModifySettings != null) {
			text = Transform.transformText(text, tModifySettings);
		}
		textlength = text.length();

		doAnalysis(alphabet);
	}

	/**
	 * Analyse a text on his frequency distribution.
	 * 
	 * @param inputText       text to analyse
	 * @param length          set to a value other than 1 if you want to analyse
	 *                        only a periodic subset of the text (see offset, too)
	 * @param offset          set to a value other than 0 if you want to analyse
	 *                        only a periodic subset of the text (see offset, too)
	 * @param tModifySettings set zero, if you dont want to make restrictions on the
	 *                        character set of the text
	 */
	public FreqAnalysisCalc(final String inputText, final int length, final int offset,
			final TransformData tModifySettings) {
		String myText = inputText;

		if (!"".equals(myText) || text == null) //$NON-NLS-1$
		{
			text = getOffsetCharString(myText, offset, length);
			if (tModifySettings != null) {
				text = Transform.transformText(text, tModifySettings);
			}
			textlength = text.length();

			doAnalysis(countDifferentChars(text));
		}
	}

	public final int getTextLength() {
		return textlength;
	}

	/**
	 * returns a character representation string (for displaying)
	 */
	public final String charToReprString(final char input) {
		if (input == 9) {
			return "TAB"; //$NON-NLS-1$
		} else if (input == 10) {
			return "LF"; //$NON-NLS-1$
		} else if (input == 13) {
			return "CR"; //$NON-NLS-1$
		} else if (input < 32) {
			return "." + (int) input; //$NON-NLS-1$
		} else if (input == 32) {
			return " "; //$NON-NLS-1$
		} else if (input > 32) {
			return String.valueOf(input);
		}
		return "UNKNOWN"; //$NON-NLS-1$
	}

	/**
	 * finds a character's index in the specified analysis data set.
	 * 
	 * @param c    the character to be found
	 * @param data the analysis data set
	 */
	private int findCharIndex(final char c, final FreqAnalysisData[] data) {
		for (int i = 0; i < data.length; i++)
			if (charToReprString(c).equals(data[i].charPrinted))
				return i;
		return -1;
	}

	/**
	 * execute the analysis
	 * 
	 * @param alphabet the alphabet the analysis is accounting
	 */
	private void doAnalysis(String alphabet) {
		int charsCount = alphabet.length();

		analysisAllchars = new FreqAnalysisData[charsCount];
		for (int i = 0; i < analysisAllchars.length; i++) {
			analysisAllchars[i] = new FreqAnalysisData();
			analysisAllchars[i].charPrinted = charToReprString(alphabet.charAt(i));
			analysisAllchars[i].ch = alphabet.charAt(i);
		}

		/**
		 * @author SLeischnig a comparator for sorting characters
		 */
		class MyComparator implements Comparator<FreqAnalysisData> {
			@Override
			public int compare(FreqAnalysisData a, FreqAnalysisData b) {
				if ((a.ch) > (b.ch))
					return 1;
				if ((a.ch) == (b.ch))
					return 0;
				return -1;
			}
			// No need to override equals.
		}

		Arrays.sort(analysisAllchars, new MyComparator());

		int absoluteHits = 0;
		// Absolut Frequency
		for (int i = 0; i < textlength; i++) {
			int index = findCharIndex(text.charAt(i), analysisAllchars);
			if (index > -1) {
				analysisAllchars[index].absOcc++;
				absoluteHits++;
			}
		}
		// Relative frequency
		for (int i = 0; i < analysisAllchars.length; i++)
			analysisAllchars[i].relOcc = ((double) analysisAllchars[i].absOcc / (double) absoluteHits);
		// outputString
		for (int i = 0; i < analysisAllchars.length; i++)
			outputAnalysis = outputAnalysis.concat(String.valueOf((char) (byte) (i)) + ":\t" //$NON-NLS-1$
					+ analysisAllchars[i].absOcc + "\n\t" + twoDigits.format(analysisAllchars[i].relOcc * 100) + "%\n"); //$NON-NLS-1$ //$NON-NLS-2$
		// there should be no zero weighted fields
		// int elementCount = calcNonzeroElements(analysisAllchars);
		int elementCount = analysisAllchars.length;
		analysisArray = new FreqAnalysisData[elementCount];
		int counter = 0;
		for (int i = 0; i < analysisAllchars.length; i++) {
			if (analysisAllchars[i].absOcc > -1)// no zero-weighted-fields protection needed
			{
				analysisArray[counter] = new FreqAnalysisData();
				analysisArray[counter] = analysisAllchars[i];
				counter++;
			}
		}

	}

	@Override
	public String toString() {
		return outputAnalysis;
	}

	public FreqAnalysisData[] getAnalysisArray() {
		return analysisArray;
	}
}
