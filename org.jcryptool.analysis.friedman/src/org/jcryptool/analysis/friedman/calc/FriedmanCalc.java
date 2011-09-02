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
package org.jcryptool.analysis.friedman.calc;

import java.text.DecimalFormat;

/**
 * @author SLeischnig
 * Class for Friedman-analysing a text
 */
public class FriedmanCalc {
	private String text = ""; //$NON-NLS-1$
	private DecimalFormat twoDigits = new DecimalFormat("#0.0000"); //$NON-NLS-1$
	private int textlength = 0;
	private double[] analysis;
	private int maxShift = 0;
	String outputAnalysis = ""; //$NON-NLS-1$
		//(double)myAnalysis.analysis[realIndex]/(double)myAnalysis.getTextLength()


	/**
	 * Calculates the coincidence index for several shifts of the given text against itself, forming the friedman test.
	 * @param inputText the text
	 * @param maxIndex the max shift
	 */
	public FriedmanCalc(final String inputText, final int maxIndex)
	{
		String myText = inputText;
		for(int i=0; i<myText.length();)
		{
			if(myText.charAt(i) <65 || myText.charAt(i)>90) {
				myText = myText.substring(0, i).concat(myText.substring(i+1));
			} else {
				i++;
			}
		}

		text = myText;
		textlength = text.length();
		maxShift = Math.max(maxIndex, 1);

		doAnalysis();
	}

	/**
	 * Shifts a text rightwise
	 * @param text is the to-be-shifted-rightwise text
	 * @return the shifted text
	 */
	private String shiftfriedman(final String text, final int shift)
	{
		int sh = shift % text.length();
		//return text.substring(sh).concat(text.substring(0, sh));
		return text.substring(text.length()-sh).concat(text.substring(0, text.length()-sh));
	}


	/** calculates the coincidence index for a specified shift
	 * @param text the analyzed text
	 * @param shift the Friedman test current shift
	 * @return the coincidence index
	 */
	private double coincidenceIndex(final String text, final int shift)
	{
		String compare = shiftfriedman(text, shift);
		int count = 0;
		for(int i=0; i<text.length(); i++) if(text.charAt(i) == compare.charAt(i)) count++;
		return (double)(count/(double)text.length());
	}

	/**
	 * This procedure returns the double array representing the coincidence indices of the given text shifts rightwise.
	 * @param text is the text. What else?
	 * @param endShift is the upper limit for the sgift value. Lower limit is one.
	 * @return the coincidence indices in an array
	 */
	public double[] friedmantest(String text, int endShift)
	{
		double[] result = new double[Math.min(endShift, text.length()-1)];
		for(int i=1; i<=Math.min(endShift, text.length()-1); i++) result[i-1] = coincidenceIndex(text, i);
		return result;
	}

	public int getTextLength()
	{
		return textlength;
	}

	/**
	 * main analysis procedure
	 */
	private void doAnalysis()
	{

	 	analysis = friedmantest(text, maxShift);
	 	for(int i=0; i<analysis.length; i++) outputAnalysis = outputAnalysis.concat((int)(i+1) + ":\t" + twoDigits.format(analysis[i]) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
	}


	public String toString() { return outputAnalysis; }

	public double[] getAnalysis() {
		return analysis;
	}
}
