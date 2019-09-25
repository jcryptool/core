package org.jcryptool.analysis.fleissner.logic;
import java.io.FileNotFoundException;

public class TextValuator {
	
	public static double ngrams[];
	private String alphabet;
	private int n,m;
	
//	
	/**
	 * Sets Alphabet for parameter language and saves text statistics to give and size of statistics for text valuation
	 * 
	 * @param statistics
	 * @param language
	 * @param n
	 * @throws FileNotFoundException
	 */
	public TextValuator(double statistics[], String language, int n) throws FileNotFoundException
	{
	    this.n= n;
        TextValuator.ngrams = statistics;
		
		switch(language) {
		case "german":	alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜß";
						m = 30;
						break;
		case "english":	alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
						m = 26;
						break;
		}
	}
	
//	
	/**
	 * Evaluates parameter text by weighing all nGrams of the text and calculating the overall weight of a text
	 * 
	 * @param text
	 * @return value of the text
	 */
	public double evaluate(String text)
	{	
		double value = 0;
		int textLength = text.length();
		int[] letterNumber = new int[n];
		for (int i=0; i< textLength-n; i++)
		{
			String ngram = text.substring(i, i+n);
			for (int k=0; k<n; k++) {	
				for (int j=0;j<alphabet.length();j++) {
					if ((ngram.charAt(k))==(alphabet.charAt(j))) {
						letterNumber[k] = j;
					}
				}
			}
			int index = 0;
			for (int l=0;l<letterNumber.length;l++) {
				index += (letterNumber[l])*((int)  Math.pow(m, (letterNumber.length-1-l)));
			}
			if (TextValuator.ngrams[index]!=0) {
				value += TextValuator.ngrams[index];
			}
		}
		return -value;
	}
}