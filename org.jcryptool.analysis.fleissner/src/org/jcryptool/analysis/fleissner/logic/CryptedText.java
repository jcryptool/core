package org.jcryptool.analysis.fleissner.logic;
import java.util.ArrayList;
import java.util.logging.Logger;


public class CryptedText {
	
	private static final Logger log = Logger.getLogger( CryptedText.class.getName() );
	
	private ArrayList<char[][]> text = new ArrayList<char[][]>();
	private String line;
	
	public void load(String textInLine, boolean isPlaintext, int templateLength, int[] coordinates, FleissnerGrille fg)
	{
		try {
			
			line=textInLine;
			int textLength = line.length();
			if (isPlaintext) {
				log.info("Plaintext: "+line);
				log.info("Length of plaintext: "+line.length());
			}
			else {
				log.info("Crypted text: "+line);
				log.info("Length of crypted text: "+line.length());
			}
			
			char[][] textPart;
			int k=0;
			
			if (isPlaintext) {
				
				int letterFields;
//				int filler = 0;
				double randomLetters;
				
				if (templateLength%2==0) {
					letterFields = (int) (Math.pow(templateLength, 2));
				}
				else {
					letterFields = (int) (Math.pow(templateLength, 2)-1);
//					if (!isPlaintext) {
//						filler = textLength/(letterFields+1);
//					}
				}

				randomLetters = letterFields-(textLength/*-filler*/%letterFields);
				
				String X = "X";
				if (randomLetters!=letterFields) {
					log.info(randomLetters+" random letters inserted");
					for (int i=textLength;i<textLength+randomLetters;i++) {
						
						line += (char) ('A' + 26*Math.random());
					}
					
					log.info("Modified plaintext: "+line);
				}
				else {
					
					log.info("no random letters inserted");
				}
				
				line = fg.encryptText(line, coordinates);
				log.info("plaintext successfully encrypted");
			}
			
			while (k <line.length()) {
				
				textPart = new char[templateLength][templateLength];

				for (int y=0; y< templateLength; y++) {
					for (int x = 0; x < templateLength; x++) {
						if (k!=line.length()) {
							
							textPart[x][y] = line.charAt(k);
							k++;
						}
						else {
							break;
						}
					}
				}

				text.add(textPart);
			}
				
		} catch (Exception e) {
			log.severe("An error occured during file handling: " + e);
			e.printStackTrace();
		}
	}
	
	public ArrayList<char[][]> getText()
	{
		return this.text;
	}
	
	@Override
	public String toString() {
		int size = this.getText().get(0).length;
		String s="\n\nCrypted Text:\n";
		for(char[][]textPart:text) {
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					s+=textPart[x][y];
				}
				s+="\n";
			}
		}	
		return s;
	}
}
