package org.jcryptool.crypto.uitools.alphabets.composite;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

//TODO: change method names to sth more suitable (toStringShort....)
/**
 * Basic alphabet which may be created solely from its characters
 * 
 * @author Simon L
 *
 */
public class AtomAlphabet extends AbstractAlphabet {
	
	public static final String ATOM_ALPHABET_NAME = "AtomAlphabet";
	
	public static final boolean IS_STANDARD_ALPHABET_STD = false;
	public static final boolean IS_BASIC_STD = false;
	public static final String NAME_STD = null;
	public static final String SHORTNAME_STD = null;
	
	private List<Character> content;
	private boolean isBasic = IS_BASIC_STD;
	private boolean isDefault = IS_STANDARD_ALPHABET_STD;
	/**
	 * null => generated name is returned in getter
	 */
	private String name;
	/**
	 * null => generated short name is returned in getter
	 */
	private String shortName;
	
	/**
	 * @param characters
	 * @param isBasic
	 * @param isDefault
	 * @param name
	 * @param shortName
	 * 
	 * @deprecated the additional parameters will soon be managed in the alphabets manager
	 */
	public AtomAlphabet(List<Character> characters, boolean isBasic, boolean isDefault, String name, String shortName) {
		this.content = characters;
		setName(name);
		setShortName(shortName);
		setBasic(isBasic);
		setDefaultAlphabet(isDefault);
	}

	/**
	 * creates an alphabet solely from its containing characters, defaulting the other parameters
	 * (see public static standard fields like {@value AtomAlphabet#IS_STANDARD_ALPHABET_STD}.
	 * <br />
	 * Double characters will be ignored.<br />
	 * 
	 * @param characters the characters of the alphabet
	 */
	public AtomAlphabet(List<Character> characters) {
		this.content = filterDoublets(characters);
	}
	

	/**
	 * creates an alphabet solely from its containing characters, defaulting the other parameters
	 * (see public static standard fields like {@value AtomAlphabet#IS_STANDARD_ALPHABET_STD}.
	 * <br />
	 * by default, the names are generated from the alphabet content.
	 * 
	 * @param characters the characters of the alphabet
	 */
	public AtomAlphabet(String characters) {
		this(StringAlphabetFactory.stringToList(characters));
	}
	
	/**
	 * creates an alphabet solely from its containing characters, defaulting the other parameters
	 * (see public static standard fields like {@value AtomAlphabet#IS_STANDARD_ALPHABET_STD}.
	 * <br />
	 * by default, the names are generated from the alphabet content.
	 * 
	 * @param characters the characters of the alphabet
	 */
	public AtomAlphabet(char[] characters) {
		this(StringAlphabetFactory.stringToList(characters));
	}

	private static List<Character> filterDoublets(List<Character> characters) {
		List<Character> ref = new LinkedList<Character>();
		for(Character c: characters) {
			if(! ref.contains(c)) {
				ref.add(c);
			}
		}
		
		return ref;
	}

	public char[] getCharacterSet() {
		return StringAlphabetFactory.toCharArray(content);
	}
	public boolean contains(char e) {
		return content.contains(e);
	}
	
	@Override
	public String toString() {
		return StringAlphabetFactory.buildStringRepresentation(this);
	}

	@Override
	public boolean isDefaultAlphabet() {
		return isDefault;
	}

	@Override
	public int getDisplayMissingCharacters() {
		// wont be available for much longer
		return -1;
	}

	@Override
	public char getSubstituteCharacter() {
		// wont be available for much longer
		return 'X';
	}

	@Override
	public void setDefaultAlphabet(boolean b) {
		this.isDefault = b;
	}

	@Override
	public void setCharacterSet(char[] characterSet) {
		this.content = StringAlphabetFactory.stringToList(characterSet);
	}

	@Override
	public boolean isBasic() {
		return isBasic;
	}

	@Override
	public void setBasic(boolean basic) {
		this.isBasic = basic;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortName() {return shortName != null ? shortName : generateShortName();} //$NON-NLS-1$

	public String getName() {return name != null ? name : generateName();} //$NON-NLS-1$

	/**
	 * Method for creating a generated short name for the alphabet if no short name is explicitely set
	 * 
	 * @return a generated name
	 * @deprecated like {@link #getShortName()}
	 */
	private String generateShortName() {
		return generateName();
	}

	/**
	 * Method for creating a generated name for the alphabet if no short name is explicitely set
	 * 
	 * @return a generated name
	 * @deprecated like {@link #getName()}
	 */
	private String generateName() {
		return alphabetContentAsString(getCharacterSet());
	}

	private static Map<Character, String> specialCharactersForPrinting = new HashMap<Character, String>();
	static {
		specialCharactersForPrinting.put('\n', "\\n");
		specialCharactersForPrinting.put('\r', "\\r");
		specialCharactersForPrinting.put('\t', "\\t");
	}
	
	/**
	 * converts an alphabet's content to a String. This makes sure that linebreaks etc. are
	 * shown as "\n" etc.<br />
	 * 
	 * 
	 * @param alpha the alphabet
	 * @return a string containing a representation of every character in the alphabet
	 */
	public static String alphabetContentAsString(char[] alphaContent) {
		StringBuilder sb = new StringBuilder();
		
		for(char c: alphaContent) {
			String charRep = getPrintableCharRepresentation(c);
			sb.append(charRep);
		}
		
		return sb.toString();
	}
	
	public static String getPrintableCharRepresentation(char c) {
		if(specialCharactersForPrinting.containsKey(Character.valueOf(c))) {
			return specialCharactersForPrinting.get(Character.valueOf(c));
		} else if((int) c < 32) {
			return "{" + String.valueOf((int) c) + "}";
		} else {
			return String.valueOf(c);
		}
	}

	/**
	 * pendant to {@link #alphabetContentAsString(AbstractAlphabet)}
	 */
	public static char[] parseAlphaContentFromString(String alpha) {
		Pattern nonprintables;
		nonprintables = Pattern.compile("\\{\\d+}", Pattern.DOTALL);
		String newAlpha = alpha;
		
		Matcher m = nonprintables.matcher(alpha);
		
		while(m.find()) {
			int number = Integer.valueOf(newAlpha.substring(m.start()+1, m.end()-1));
			newAlpha = newAlpha.replace(m.group(), String.valueOf((char)number));
			m = nonprintables.matcher(newAlpha);
		}
		
		for(Entry<Character, String> replacement: specialCharactersForPrinting.entrySet()) {
			newAlpha = newAlpha.replace(replacement.getValue(), String.valueOf(replacement.getKey()));
		}
		
		return newAlpha.toCharArray();
	}
	
}
