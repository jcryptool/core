package org.jcryptool.analysis.fleissner.logic;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Locale;
import org.jcryptool.analysis.fleissner.Activator;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * @author Dinah
 *
 */
public class ParameterSettings {
	
	private ArrayList<Integer> possibleTemplateLengths = new ArrayList<>();
	
	private String textInLine = null;
	private String method = null;
	private int templateLength=0;
	private boolean isPlaintext = false;
	private int holes = 0;
	private int[] grille = null;
	private BigInteger restart = new BigInteger("0");
	private String language = null;
	private int nGramSize = 0;
	
	/**
	 * Sets all parameters and method to be applied. Checks, if combinations of given parameters are useful to start a method.
	 * Sets default values to non given parameters, if useful and throws Exception else.
	 * 
	 * @param args every necessary parameter as string in args array
	 * @throws InvalidParameterCombinationException
	 * @throws FileNotFoundException
	 */
	public ParameterSettings(String[] args) throws InvalidParameterCombinationException, FileNotFoundException{
		if (args.length>0) {
			
			for (int i=0;i<args.length;i+=2) {
				
				String arg = args[i];
				
				switch (arg) {
				
				case "-method":
					if ((args[i+1].equals("encrypt"))||(args[i+1].equals("analyze"))||(args[i+1].equals("decrypt"))) {
						method = args[i+1];
						LogUtil.logInfo("method: "+method);
					}
					else {
//						there is no other method deposited
						throw new InvalidParameterCombinationException("method has to be analyze, encrypt or decrypt");
					}
					break;
				case "-plaintext":
//					sets plaintext, if given
					isPlaintext = true;
					textInLine = args[i+1];
					LogUtil.logInfo("Text type: plaintext\nText: "+textInLine);
					break;
				case "-cryptedText":
//					sets crypted text, if given
					isPlaintext = false;
					textInLine = args[i+1];
					LogUtil.logInfo("Text type: crypted text\nText: "+textInLine);
					break;
				case "-restarts":
//					sets restarts, if given
					if (Integer.parseInt(args[i+1])>0) {
						restart = BigInteger.valueOf(Integer.parseInt(args[i+1]));
						LogUtil.logInfo("restarts: "+String.valueOf(restart));
					}
					else {
//						there has to be at least one restart
						throw new InvalidParameterCombinationException("Restarts has to be an integer greater or equal to 1");
					}
					break;
				case "-keyLength":
//					sets key length, if given
					if (Integer.parseInt(args[i+1])<2) {
//						there is no valid key with length smaller than 2
						throw new InvalidParameterCombinationException("Length of grille has to be an integer greater or equal to 2");
					}
					else {
						templateLength = Integer.parseInt(args[i+1]);
						if (templateLength%2==0) {
							holes = (int) (Math.pow(templateLength, 2))/4;
						}
						else {
							holes = (int) (Math.pow(templateLength, 2)-1)/4;
						}
						LogUtil.logInfo("keylength: "+templateLength+"\nholes: "+holes);
					}
					break;
				case "-key":
				    /**
				     * sets key in the following shape (example for 3x3 grille): 0,0,2,1 
				     * for two holes at coordinates (0,0) and (2,1)
				     * and calulates and sets key length and holes
				     */
					try {
						String template = args[i+1];
						String[] data;
						data=template.split(",");
						
						grille = new int[data.length];
						if (grille.length%2!=0) {
							throw new InvalidParameterCombinationException("Invalid key length");
						}
						holes = ((grille.length)/2);
						boolean isSquare= this.isSquare(holes);

						if (isSquare) {
							templateLength =(int) Math.sqrt(4*holes);
						}
						else {
							templateLength = (int) Math.sqrt((4*holes)+1);
						}
						
						for (int k=0;k<data.length;k++) {
							grille[k]= Integer.parseInt(data[k]);
						}
						LogUtil.logInfo("key: "+template+"\nkeylength: "+String.valueOf(templateLength)+"\nholes: "+String.valueOf(holes));									
					} catch (Exception e) {
						LogUtil.logError(Activator.PLUGIN_ID, "Invalid key", e, true);
						e.printStackTrace();
						throw new InvalidParameterCombinationException("Invalid key");
					}	
					break;
				case "-language":
//					sets language, if given
					if ((args[i+1].equals("german"))||(args[i+1].equals("english"))) {
						language = args[i+1];
						LogUtil.logInfo("language: "+language);
					}
					else {
//						currently there is no other alphabet deposited
						throw new InvalidParameterCombinationException("language has to be english or german");
					}
					break;
				case "-nGramSize":
//					there are just functioning tri- and quadgrams deposited
					if ((Integer.parseInt(args[i+1])<3) || (Integer.parseInt(args[i+1])>4)) {
						throw new InvalidParameterCombinationException("Please enter a tri- or quadgram");
					}
					else {
//						sets ngram, if given
						nGramSize = Integer.parseInt(args[i+1]);
						LogUtil.logInfo("Ngram size: "+nGramSize);
					}
					break;
				default: throw new InvalidParameterCombinationException("Please enter valid parameters");	
				}
			}
		}
			
//		clears input text of punctuation marks and blank spaces and sets all letters to upper case
		if (textInLine != null) {
			textInLine = textInLine.replaceAll("\\p{Punct}", "").replace(" ", "").replace('ß', '\u9999');
			textInLine = textInLine.toUpperCase(Locale.GERMAN).replace('\u9999', 'ß');
		}
	}

	/**
	 * @return boolean whether a number is square
	 */
	public boolean isSquare(int number) {
		
		int isSquare=number;
		int j = 0;

		do {
			isSquare-= (2*j+1);
			j++;
		}
		while(isSquare>0);
		
		if (isSquare==0) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @return the textInLine
	 */
	public String getTextInLine() {
		return textInLine;
	}

	/**
	 * @param textInLine the textInLine to set
	 */
	public void setTextInLine(String textInLine) {
		this.textInLine = textInLine;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the templateLength
	 */
	public int getTemplateLength() {
		return templateLength;
	}

	/**
	 * @param templateLength the templateLength to set
	 */
	public void setTemplateLength(int templateLength) {
		this.templateLength = templateLength;
	}

	/**
	 * @return the isPlaintext
	 */
	public boolean isPlaintext() {
		return isPlaintext;
	}

	/**
	 * @param isPlaintext the isPlaintext to set
	 */
	public void setPlaintext(boolean isPlaintext) {
		this.isPlaintext = isPlaintext;
	}

	/**
	 * @return the holes
	 */
	public int getHoles() {
		return holes;
	}

	/**
	 * @param holes the holes to set
	 */
	public void setHoles(int holes) {
		this.holes = holes;
	}

	/**
	 * @return the grille
	 */
	public int[] getGrille() {
		return grille;
	}

	/**
	 * @param grille the grille to set
	 */
	public void setGrille(int[] grille) {
		this.grille = grille;
	}

	/**
	 * @return the restart
	 */
	public BigInteger getRestart() {
		return restart;
	}

	/**
	 * @param restart the restart to set
	 */
	public void setRestart(BigInteger restart) {
		this.restart = restart;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the nGramSize
	 */
	public int getnGramSize() {
		return nGramSize;
	}

	/**
	 * @param nGramSize the nGramSize to set
	 */
	public void setnGramSize(int nGramSize) {
		this.nGramSize = nGramSize;
	}


	/**
	 * @return the possibleTemplateLengths
	 */
	public ArrayList<Integer> getPossibleTemplateLengths() {
		return possibleTemplateLengths;
	}
}
