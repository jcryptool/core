package org.jcryptool.analysis.fleissner2.logic;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Logger;

public class TextValuator {
	
	private static final Logger log = Logger.getLogger( FleissnerGrilleSolver.class.getName() );
	
	public static double ngrams[];
	private String alphabet;
	private String filename;
	private int n,m;
	
//	Sets Alphabet for parameter language and loads text statistics to given name and size of statistics for text valuation
	public TextValuator(String statistics, String language, int n) throws FileNotFoundException
	{
		this.n= n;
		this.filename = statistics;
		
		switch(language) {
		case "german":	alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜß";
						m = 30;
						break;
		case "english":	alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
						m = 26;
						break;
		}

		ngrams = new double[(int) Math.pow(m, n)];

		try {
			loadBinNgramFrequencies();
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("File not found !");
		}
	}
	
//	Evaluates parameter text by weighing all nGrams of the text and calculating the overall weight of a text
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
	
	public double toDouble(byte[] bytes) {
	    return ByteBuffer.wrap(bytes).getDouble();
	}
	
//	load text statistic
	public void loadBinNgramFrequencies() throws FileNotFoundException
	{
		ByteBuffer myByteBuffer = ByteBuffer.allocate(((int) Math.pow(m, n)) * 8);
		myByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		DoubleBuffer doubleBuffer = myByteBuffer.asDoubleBuffer();

		try { 
			FileInputStream fileInputStream = new FileInputStream(filename);
			FileChannel fileChannel = fileInputStream.getChannel();
		    
		    fileChannel.read(myByteBuffer);
		    fileChannel.close();
		    fileInputStream.close();   
		    log.info("Statistics succesfully loaded");
		    	
		    } catch (IOException e) {
		    	// TODO Auto-generated catch block
		    	e.printStackTrace();
		    	throw new FileNotFoundException("File not found !");
		    } 	
		doubleBuffer.get(ngrams);
	}
}