package org.jcryptool.core.operations.alphabets;

/**
 * Reference interface for management of alphabets in the alphabet manager.
 * 
 * @author Simon L
 */
public class AlphabetReference {
	
	public String getAlphabetName() {
		return alphabetName;
	}

	public void setAlphabetName(String alphabetName) {
		this.alphabetName = alphabetName;
	}

	public String getShortAlphabetName() {
		return shortAlphabetName;
	}

	public void setShortAlphabetName(String shortAlphabetName) {
		this.shortAlphabetName = shortAlphabetName;
	}

	public boolean isDefaultAlphabet() {
		return defaultAlphabet;
	}

	public void setDefaultAlphabet(boolean defaultAlphabet) {
		this.defaultAlphabet = defaultAlphabet;
	}

	public boolean isIntegral() {
		return integral;
	}

	public void setIntegral(boolean integral) {
		this.integral = integral;
	}

	public New_AbstractAlphabetStore getOriginStore() {
		return originStore;
	}

	String alphabetName;
	String shortAlphabetName;
	boolean defaultAlphabet;
	boolean integral;
	New_AbstractAlphabetStore originStore;
	
	public AlphabetReference(String alphabetName, String shortAlphabetName,
			boolean defaultAlphabet, boolean integral,
			New_AbstractAlphabetStore originStore) {
		super();
		this.alphabetName = alphabetName;
		this.shortAlphabetName = shortAlphabetName;
		this.defaultAlphabet = defaultAlphabet;
		this.integral = integral;
		this.originStore = originStore;
	}
	
}
